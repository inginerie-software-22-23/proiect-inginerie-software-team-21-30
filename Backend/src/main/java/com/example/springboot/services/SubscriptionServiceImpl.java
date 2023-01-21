package com.example.springboot.services;

import com.example.springboot.exceptions.NoSuchCourseExistsException;
import com.example.springboot.exceptions.NoSuchSubscriptionExists;
import com.example.springboot.exceptions.NoSuchUserExistsException;
import com.example.springboot.exceptions.TraineeAlreadySubscribedToCourseException;
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
public class SubscriptionServiceImpl implements SubscriptionSerivce {
    @Autowired
    private SubscriptionsRepositiory subscriptionsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public String subscribeTraineeToCourse(Long traineeId, Long courseId) {
        Optional<User> trainee = userRepository.findById(traineeId);
        Optional<Course> course = courseRepository.findById(courseId);

        if (trainee.isEmpty()) {
            throw new NoSuchUserExistsException("User with id " + traineeId + " not found");
        }

        if (course.isEmpty()) {
            throw new NoSuchCourseExistsException("Course with id: " + courseId + " not found");
        }

        Subscription newSubscription = new Subscription(trainee.get(), course.get());

        if (trainee.get().getSubscriptions().contains(newSubscription)) {
            throw new TraineeAlreadySubscribedToCourseException(
                    "Trainee with id " + traineeId + " is already subscribed to course with id " + courseId);
        } else {
            trainee.get().getSubscriptions().add(newSubscription);
            course.get().getSubscriptions().add(newSubscription);

            subscriptionsRepository.save(newSubscription);
            userRepository.save(trainee.get());
            courseRepository.save(course.get());
            return "Trainee subscribed to course successfully";
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

}
