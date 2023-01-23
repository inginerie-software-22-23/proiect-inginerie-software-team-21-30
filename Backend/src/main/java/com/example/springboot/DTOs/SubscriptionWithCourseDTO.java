package com.example.springboot.DTOs;

import com.example.springboot.models.Subscription;
import lombok.Data;

@Data
public class SubscriptionWithCourseDTO {
    private Long id;
    private CourseDTO course;
    private String joinDate;

    public SubscriptionWithCourseDTO(Subscription subscription) {
        this.id = subscription.getId();
        this.course = new CourseDTO(subscription.getCourse());
        this.joinDate = subscription.getJoinDate();
    }
}
