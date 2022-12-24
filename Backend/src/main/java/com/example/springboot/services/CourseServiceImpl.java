package com.example.springboot.services;

import com.example.springboot.models.Course;
import com.example.springboot.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    public Course findById(@PathVariable Long courseId) throws Exception {
        return courseRepository.findById(courseId)
                .orElseThrow(()
                        -> new Exception("No course found with id = " + courseId));
    }

    public List<Course> filterByName(@PathVariable String courseName) throws Exception {
        return courseRepository.findAll().stream().
                filter(x -> x.getName().contains(courseName)).
                collect(Collectors.toList());
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

    public String update(Course course) {
        courseRepository.save(course);
        return "Course updated successfully";
    }

    public String delete(Long courseId) {
        courseRepository.deleteById(courseId);
        return "Course deleted successfully";
    }
}
