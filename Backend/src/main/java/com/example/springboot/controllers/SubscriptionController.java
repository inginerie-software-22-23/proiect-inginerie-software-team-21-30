package com.example.springboot.controllers;

import com.example.springboot.DTOs.SubscriptionDTO;
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
    public List<SubscriptionDTO> getSubscriptionsOfUser(@PathVariable String username) {
        return subscriptionService.getSubscriptionsOfUser(username)
                .stream()
                .map(SubscriptionDTO::new)
                .collect(Collectors.toList());
    }

    @CrossOrigin
    @GetMapping("/{subscriptionId}")
    public SubscriptionDTO findById(@PathVariable Long subscriptionId) {
        return new SubscriptionDTO(subscriptionService.findById(subscriptionId));
    }

    @CrossOrigin
    @PostMapping("/unsubscribe/{userId}/{subscriptionId}")
    public String unsubscribeUser(@PathVariable Long userId, @PathVariable Long subscriptionId) {
        return subscriptionService.unsubscribeUserFromCourse(userId, subscriptionId);
    }
}
