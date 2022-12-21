package com.example.springboot.DTOs;

import com.example.springboot.models.Course;
import com.example.springboot.models.Subscription;
import com.example.springboot.models.User;

public class SubscriptionDTO {
    private Long id;
    private Course course;
    private User trainee;
    private String joinDate;

    public SubscriptionDTO(Subscription subscription) {
        this.id = subscription.getId();
        this.course = subscription.getCourse();
        this.trainee = subscription.getTrainee();
        this.joinDate = subscription.getJoinDate();
    }
}
