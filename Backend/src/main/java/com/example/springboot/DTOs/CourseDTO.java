package com.example.springboot.DTOs;

import com.example.springboot.models.Course;

import java.io.Serializable;

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getMeetLink() {
        return meetLink;
    }

    public UserDTO getMentor() {
        return mentor;
    }
}
