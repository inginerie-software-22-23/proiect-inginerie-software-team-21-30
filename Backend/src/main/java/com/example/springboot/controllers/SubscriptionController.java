package com.example.springboot.controllers;

import com.example.springboot.DTOs.SubscriptionWithCourseDTO;
import com.example.springboot.services.SubscriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    @Autowired
    SubscriptionServiceImpl subscriptionService;

    @CrossOrigin
    @PostMapping("/subscribe/{traineeId}/{courseId}")
    public String subscribeTraineeToCourse(@PathVariable Long traineeId, @PathVariable Long courseId) {
        return subscriptionService.subscribeTraineeToCourse(traineeId, courseId);
    }

    @CrossOrigin
    @GetMapping("/all/{username}")
    public List<SubscriptionWithCourseDTO> getSubscriptionsOfUser(@PathVariable String username) {
        return subscriptionService.getSubscriptionsOfUser(username)
                .stream()
                .map(SubscriptionWithCourseDTO::new)
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @GetMapping("/{subscriptionId}")
    public SubscriptionWithCourseDTO findById(@PathVariable Long subscriptionId) {
        return new SubscriptionWithCourseDTO(subscriptionService.findById(subscriptionId));
    }

    @CrossOrigin
    @PostMapping("/unsubscribe/{traineeId}/{subscriptionId}")
    public String unsubscribeTrainee(@PathVariable Long traineeId, @PathVariable Long subscriptionId) {
        return subscriptionService.unsubscribeTraineeFromCourse(traineeId, subscriptionId);
    }
}
