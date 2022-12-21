package com.example.springboot.DTOs;

import com.example.springboot.models.Course;

import java.util.Collection;
import java.util.stream.Collectors;

public class CourseDTO {
    private Long id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String meetLink;
    private UserDTO mentor;
    private Collection<SubscriptionDTO> subscriptions;

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.shortDescription = course.getShortDescription();
        this.longDescription = course.getLongDescription();
        this.meetLink = course.getMeetLink();
        this.mentor = new UserDTO(course.getUser());
        this.subscriptions = course.getSubscriptions()
                .stream()
                .map(SubscriptionDTO::new)
                .collect(Collectors.toList());
    }
}
