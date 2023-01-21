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
    @PostMapping("/all/{username}")
    public List<SubscriptionWithCourseDTO> getSubscriptionsOfUser(@PathVariable String username) {
        return subscriptionService.getSubscriptionsOfUser(username)
                .stream()
                .map(SubscriptionWithCourseDTO::new)
                .collect(Collectors.toList());
    }
}
