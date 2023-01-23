package com.example.springboot.services;

import com.example.springboot.exceptions.*;
import com.example.springboot.models.Course;
import com.example.springboot.models.Subscription;
import com.example.springboot.models.User;
import com.example.springboot.repositories.CourseRepository;
import com.example.springboot.repositories.SubscriptionsRepositiory;
import com.example.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    private SubscriptionsRepositiory subscriptionsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public String subscribeUserToCourse(Long userId, Long courseId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (user.isEmpty()) {
            throw new NoSuchUserExistsException("User with id " + userId + " not found");
        }

        if (course.isEmpty()) {
            throw new NoSuchCourseExistsException("Course with id: " + courseId + " not found");
        }

        Subscription newSubscription = new Subscription(user.get(), course.get());

        if (user.get().getSubscriptions().contains(newSubscription)) {
            throw new UserAlreadySubscribedToCourseException(
                    "User with id " + userId + " is already subscribed to course with id " + courseId);
        } else {
            user.get().getSubscriptions().add(newSubscription);
            course.get().getSubscriptions().add(newSubscription);

            subscriptionsRepository.save(newSubscription);
            userRepository.save(user.get());
            courseRepository.save(course.get());
            return "User subscribed to course successfully";
        }
    }

    public List<Subscription> getSubscriptionsOfUser(String username) {
        return subscriptionsRepository.findAll().stream()
                .filter(x -> x.getUser().getName().equals(username))
                .collect(Collectors.toList());
    }

    public Subscription findById(Long id) {
        return subscriptionsRepository.findById(id)
                .orElseThrow(()
                        -> new NoSuchSubscriptionExists("No subscription found with id = " + id));
    }

    public String unsubscribeUserFromCourse(Long userId, Long subscriptionId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Subscription> subscription = subscriptionsRepository.findById(subscriptionId);

        if (user.isEmpty()) {
            throw new NoSuchUserExistsException("User with id " + userId + " not found");
        }

        if (subscription.isEmpty()) {
            throw new NoSuchSubscriptionExists("Subscription with id: " + subscriptionId + " not found");
        }

        if (!user.get().getSubscriptions().contains(subscription.get())) {
            throw new UserNotSubscribedToCourseException(
                    "User with id " + userId + " does not have subscription with id " + subscriptionId +
                            " to course with id " + subscription.get().getCourse().getId());
        } else {
            user.get().getSubscriptions()
                    .removeIf(sub -> sub.getId().equals(subscription.get().getId()));

            userRepository.save(user.get());
            subscriptionsRepository.delete(subscription.get());

            return "User unsubscribed successfully";
        }
    }

}
