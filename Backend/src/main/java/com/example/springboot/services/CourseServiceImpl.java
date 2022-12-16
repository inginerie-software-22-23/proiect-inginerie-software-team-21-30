package com.example.springboot.services;

import com.example.springboot.models.Course;
import com.example.springboot.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Course findById(@PathVariable Long id) throws Exception {
        return courseRepository.findById(id)
                .orElseThrow(()
                        -> new Exception("No course found with id = " + id));
    }

    public Course findByName(@PathVariable String name) throws Exception {
        return courseRepository.findByName(name)
                .orElseThrow(()
                        -> new Exception("No course found with name " + name));
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> findCoursesByMentor(String username) {
        return courseRepository.findAll().stream().
                filter(x -> x.getUser().getName().equals(username)).
                collect(Collectors.toList());
    }

    public String create(Course course) {
        courseRepository.save(course);
        return "Course saved successfully";
    }
}
