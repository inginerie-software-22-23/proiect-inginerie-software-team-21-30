package com.example.springboot.services;

import com.example.springboot.models.Course;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CourseService {
    String create(Course course);
    Course findById(@PathVariable Long id);
    List<Course> findAll();
    List<Course> findCoursesByMentor(String username);
    List<Course> filterByName(@PathVariable String name);
    String update(Course course);
    String delete(@PathVariable Long id);
}
