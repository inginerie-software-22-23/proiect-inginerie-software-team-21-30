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
    @PostMapping("/subscribe/{userId}/{courseId}")
    public String subscribeUserToCourse(@PathVariable Long userId, @PathVariable Long courseId) {
        return subscriptionService.subscribeUserToCourse(userId, courseId);
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
    @PostMapping("/unsubscribe/{userId}/{subscriptionId}")
    public String unsubscribeUser(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        return subscriptionService.unsubscribeUserFromCourse(userId, subscriptionId);
    }
}
