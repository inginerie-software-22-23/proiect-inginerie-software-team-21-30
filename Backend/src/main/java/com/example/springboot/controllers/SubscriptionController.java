package com.example.springboot.controllers;

import com.example.springboot.DTOs.SubscriptionDTO;
import com.example.springboot.DTOs.UserDTO;
import com.example.springboot.models.SubscribeForm;
import com.example.springboot.models.Subscription;
import com.example.springboot.models.User;
import com.example.springboot.services.SubscriptionServiceImpl;
import com.example.springboot.services.UserServiceImpl;
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
    @PostMapping("/")
    public String subscribeTraineeToCourse(@RequestBody SubscribeForm data) {
        return subscriptionService.subscribeTraineeToCourse(data.getTraineeId(), data.getCourseId());
    }

}
