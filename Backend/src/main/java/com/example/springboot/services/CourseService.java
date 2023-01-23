package com.example.springboot.services;

import com.example.springboot.models.Course;

import java.util.List;

public interface CourseService {
    String create(Course course);
    Course findById(Long id);
    List<Course> findAll();
    List<Course> findCoursesByMentor(String username);
    List<Course> filterByName(String name);
    String update(Course course, Long id);
    String delete(Long id);
}
