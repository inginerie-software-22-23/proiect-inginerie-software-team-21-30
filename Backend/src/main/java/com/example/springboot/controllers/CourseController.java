package com.example.springboot.controllers;

import com.example.springboot.DTOs.CourseDTO;
import com.example.springboot.models.Course;
import com.example.springboot.models.User;
import com.example.springboot.services.CourseServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private UserServiceImpl userService;

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
    @GetMapping("/courses/mentor/{username}")
    public List<CourseDTO> findCoursesByMentor(@PathVariable String username) {
        List<Course> courses = courseService.findCoursesByMentor(username);
        List<CourseDTO> coursesToReturn = new ArrayList<>();
        for (Course course : courses) {
            coursesToReturn.add(new CourseDTO(course));
        }
        return coursesToReturn;
    }

    @CrossOrigin
    @GetMapping("/courses/{courseName}")
    public List<CourseDTO> filterByName(@PathVariable String courseName) throws Exception {
        List<Course> coursesFilteredByName = courseService.filterByName(courseName);
        List<CourseDTO> coursesToReturn = new ArrayList<>();
        for (Course course : coursesFilteredByName) {
            coursesToReturn.add(new CourseDTO(course));
        }
        return coursesToReturn;
    }

    @CrossOrigin
    @GetMapping("/{courseId}")
    public CourseDTO findById(@PathVariable Long courseId) throws Exception {
        return new CourseDTO(courseService.findById(courseId));
    }

    @CrossOrigin
    @PostMapping(value = "/create/{mentorId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody Course course, @PathVariable Long mentorId) {
        User mentor = userService.findById(mentorId);
        course.setUser(mentor);
        return courseService.create(course);
    }

    @CrossOrigin
    @PutMapping(value = "/update/{courseId}")
    public String update(@RequestBody Course newCourseData, @PathVariable Long courseId) throws Exception {
        Course courseToUpdate = courseService.findById(courseId);

        courseToUpdate.setName(newCourseData.getName());
        courseToUpdate.setShortDescription(newCourseData.getShortDescription());
        courseToUpdate.setLongDescription(newCourseData.getLongDescription());
        courseToUpdate.setMeetLink(newCourseData.getMeetLink());

        return courseService.update(courseToUpdate);
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{courseId}")
    public String delete(@PathVariable Long courseId) {
        return courseService.delete(courseId);
    }
}
