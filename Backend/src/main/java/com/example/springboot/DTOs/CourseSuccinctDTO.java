package com.example.springboot.DTOs;

import com.example.springboot.models.Course;
import lombok.Data;

@Data
public class CourseSuccinctDTO {
    private Long id;
    private String name;

    public CourseSuccinctDTO(Course course) {
        this.id = course.getId();
        this.name = course.getName();
    }
}
