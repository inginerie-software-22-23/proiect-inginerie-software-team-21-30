package com.example.springboot.services;

import com.example.springboot.exceptions.NoSuchCourseExistsException;
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

    public String create(Course course) {
        courseRepository.save(course);
        return "Course saved successfully";
    }

    public Course findById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .orElseThrow(()
                        -> new NoSuchCourseExistsException("No course found with id = " + id));
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public List<Course> findCoursesByMentor(String username) {
        return courseRepository.findAll().stream().
                filter(x -> x.getUser().getName().equals(username)).
                collect(Collectors.toList());
    }

    public List<Course> filterByName(@PathVariable String courseName) {
        return courseRepository.findAll().stream().
                filter(x -> x.getName().contains(courseName)).
                collect(Collectors.toList());
    }

    public String update(Course course) {
        Optional<Course> courseToUpdate = courseRepository.findById(course.getId());

        if (courseToUpdate.isEmpty()) {
            throw new NoSuchCourseExistsException("No course found with id = " + course.getId());
        }

        courseRepository.save(course);
        return "Course updated successfully";
    }

    public String delete(Long id) {
        Optional<Course> courseToDelete = courseRepository.findById(id);

        if (courseToDelete.isEmpty()) {
            throw new NoSuchCourseExistsException("No course found with id = " + id);
        }

        courseRepository.deleteById(id);
        return "Course deleted successfully";
    }
}
