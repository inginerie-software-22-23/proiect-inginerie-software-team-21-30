package com.example.springboot.services;

import com.example.springboot.models.Subscription;

import java.util.List;

public interface SubscriptionService {
    String subscribeUserToCourse(Long userId, Long courseId);
    List<Subscription> getSubscriptionsOfUser(String username);
    Subscription findById(Long id);
    String unsubscribeUserFromCourse(Long userId, Long subscriptionId);
}
