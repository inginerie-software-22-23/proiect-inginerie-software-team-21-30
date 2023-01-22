package com.example.springboot.DTOs;

import com.example.springboot.models.Subscription;
import lombok.Data;

@Data
public class SubscriptionDTO {
    private Long id;
    private String mentor;
    private CourseDTO course;
    private String joinDate;

    public SubscriptionDTO(Subscription subscription) {
        this.id = subscription.getId();
        this.mentor = subscription.getUser().getName();
        this.course = new CourseDTO(subscription.getCourse());
        this.joinDate = subscription.getJoinDate();
    }
}
