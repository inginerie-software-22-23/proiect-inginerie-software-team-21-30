package com.example.springboot.controllers;

import com.example.springboot.DTOs.CourseDTO;
import com.example.springboot.models.Course;
import com.example.springboot.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
//    @Autowired
    private CourseService courseService;

//    @CrossOrigin
//    @GetMapping("/id/{id}")
//    public CourseDTO findById(@PathVariable Long id) throws Exception {
//        return new CourseDTO(courseService.findById(id));
//    }
//
//    @CrossOrigin
//    @GetMapping("/name/{name}")
//    public CourseDTO findByName(@PathVariable String name) throws Exception {
//        return new CourseDTO(courseService.findByName(name));
//    }

    @CrossOrigin
    @GetMapping("/courses")
    public List<CourseDTO> findAll() {
        List<Course> courses = courseService.findAll();
        List<CourseDTO> coursesToReturn = new ArrayList<>();
        for (Course course : courses) {
            coursesToReturn.add(new CourseDTO(course));
        }
        return coursesToReturn;
    }

    @CrossOrigin
    @GetMapping("/courses/{username}")
    public List<CourseDTO> findCoursesByMentor(@PathVariable String username) {
        List<Course> courses = courseService.findCoursesByMentor(username);
        List<CourseDTO> coursesToReturn = new ArrayList<>();
        for (Course course : courses) {
            coursesToReturn.add(new CourseDTO(course));
        }
        return coursesToReturn;
    }

    @CrossOrigin
    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody Course course) {
        return courseService.create(course);
    }
}
