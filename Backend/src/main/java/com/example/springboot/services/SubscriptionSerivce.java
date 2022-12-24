package com.example.springboot.services;

import org.springframework.web.bind.annotation.PathVariable;

public interface SubscriptionSerivce {
    String subscribeTraineeToCourse(@PathVariable Long traineeId, @PathVariable Long courseId);
}
