package com.example.springboot.services;

import com.example.springboot.models.Subscription;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SubscriptionSerivce {
    String subscribeUserToCourse(@PathVariable Long userId, @PathVariable Long courseId);
    List<Subscription> getSubscriptionsOfUser(String username);
    Subscription findById(Long id);
    String unsubscribeUserFromCourse(Long userId, Long subscriptionId);
}
