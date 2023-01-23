package com.example.springboot.controllers;

import com.example.springboot.DTOs.CourseDTO;
import com.example.springboot.models.Course;
import com.example.springboot.models.User;
import com.example.springboot.services.CourseServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private UserServiceImpl userService;

    @CrossOrigin
    @PostMapping(value = "/create/{mentorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody Course course, @PathVariable Long mentorId) {
        User mentor = userService.findById(mentorId);
        course.setUser(mentor);
        return courseService.create(course);
    }

    @CrossOrigin
    @GetMapping("/{courseId}")
    public CourseDTO findById(@PathVariable Long courseId) {
        return new CourseDTO(courseService.findById(courseId));
    }

    @CrossOrigin
    @GetMapping("/courses")
    public List<CourseDTO> findAll(@RequestParam(required = false) String username) {
        List<Course> courses = courseService.findAll();
        List<CourseDTO> coursesToReturn = courses.stream()
                .filter(x -> !x.getUser().getName().equals(username))
                .sorted(Comparator.comparing(Course::getId))
                .map(CourseDTO::new)
                .collect(Collectors.toList());
        return coursesToReturn;
    }

    @CrossOrigin
    @GetMapping("/courses/mentor/{username}")
    public List<CourseDTO> findCoursesByMentor(@PathVariable String username) {
        User user = userService.findByName(username);

        List<Course> courses = courseService.findCoursesByMentor(user.getName());
        List<CourseDTO> coursesToReturn = courses.stream()
                .sorted(Comparator.comparing(Course::getId))
                .map(CourseDTO::new)
                .collect(Collectors.toList());
        return coursesToReturn;
    }

    @CrossOrigin
    @GetMapping("/courses/{courseName}")
    public List<CourseDTO> filterByName(@PathVariable String courseName) {
        List<Course> coursesFilteredByName = courseService.filterByName(courseName);
        List<CourseDTO> coursesToReturn = coursesFilteredByName.stream()
                .sorted(Comparator.comparing(Course::getId))
                .map(CourseDTO::new)
                .collect(Collectors.toList());
        return coursesToReturn;
    }

    @CrossOrigin
    @PutMapping(value = "/update/{courseId}")
    public String update(@RequestBody Course newCourseData, @PathVariable Long courseId) {
        return courseService.update(newCourseData, courseId);
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{courseId}")
    public String delete(@PathVariable Long courseId) {
        return courseService.delete(courseId);
    }
}
