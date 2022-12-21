package com.example.springboot.DTOs;

import com.example.springboot.models.Course;
import com.example.springboot.models.Subscription;
import lombok.Data;

@Data
public class SubscriptionWithCourseDTO {
    private Long id;
    private Course course;
    private String joinDate;

    public SubscriptionWithCourseDTO(Subscription subscription) {
        this.id = subscription.getId();
        this.course = subscription.getCourse();
        this.joinDate = subscription.getJoinDate();
    }
}
