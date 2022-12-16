package com.example.springboot.DTOs;

import com.example.springboot.models.Course;

public class CourseDTO {
    private Long id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private String meetLink;
    private UserDTO mentor;

    public CourseDTO(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.shortDescription = course.getShortDescription();
        this.longDescription = course.getLongDescription();
        this.meetLink = course.getMeetLink();
        this.mentor = new UserDTO(course.getUser());
    }
}
