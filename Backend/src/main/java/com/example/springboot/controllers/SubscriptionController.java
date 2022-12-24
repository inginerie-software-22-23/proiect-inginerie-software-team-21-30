package com.example.springboot.controllers;

import com.example.springboot.services.SubscriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
