package com.example.springboot.controllers;

import com.example.springboot.DTOs.CourseDTO;
import com.example.springboot.exceptions.NoSuchCourseExistsException;
import com.example.springboot.models.Course;
import com.example.springboot.models.Subscription;
import com.example.springboot.models.User;
import com.example.springboot.services.CourseServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;
    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private UserServiceImpl userService;
    @Before
    public void init(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void findCoursesByMentor_validCourses_listCourse()
    {
        Stream<Subscription> streams = Stream.of(new Subscription());
        Set<Subscription> subscriptions = streams.collect(Collectors.toSet());

        List<Course> list = Stream.of(new Course(1L,"Test1","Description1","longDescription1","htttp://test1.com",new User(),subscriptions)).collect(Collectors.toList());
        when(courseService.findCoursesByMentor(anyString())).thenReturn(list);

    }
    @Test
    public void findAll_validData_listCourse()
    {
        Stream<Subscription> streams = Stream.of(new Subscription());
        Set<Subscription> subscriptions = streams.collect(Collectors.toSet());

        List<Course> list = Stream.of(new Course(1L,"Test1","Description1","longDescription1","htttp://test1.com",new User(),subscriptions)).collect(Collectors.toList());
        when(courseService.findAll()).thenReturn(list);
    }

    @Test
    public void filterByName_validData_listCourse()
    {
        Stream<Subscription> streams = Stream.of(new Subscription());
        Set<Subscription> subscriptions = streams.collect(Collectors.toSet());

        List<Course> list = Stream.of(new Course(1L,"Test1","Description1","longDescription1","htttp://test1.com",new User(),subscriptions)).collect(Collectors.toList());
        when(courseService.filterByName(anyString())).thenReturn(list);
    }
    @Test
    public void findById_validId_CourseDTO()
    {
        Stream<Subscription> streams = Stream.of(new Subscription());
        Set<Subscription> subscriptions = streams.collect(Collectors.toSet());
        Course n = new Course(1L,"Test1","Description1","longDescription1","htttp://test1.com",new User(),subscriptions);
        when(courseService.findById(anyLong())).thenReturn(n);
    }

    @Test
    public void findById_invalidId_throwsNoSuchCourseExistsException()
    {
        doThrow(NoSuchCourseExistsException.class).when(courseService.findById(anyLong()));
    }


}