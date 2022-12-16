package com.example.springboot.services;

import com.example.springboot.models.Course;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CourseService {
    Course findById(@PathVariable Long id) throws Exception;
    Course findByName(@PathVariable String name) throws Exception;
    List<Course> findAll();
    List<Course> findCoursesByMentor(String username);
    String create(Course course);
}
