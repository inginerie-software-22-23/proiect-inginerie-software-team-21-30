package com.example.springboot;

import com.example.springboot.controllers.CourseController;
import com.example.springboot.models.Course;
import com.example.springboot.models.Role;
import com.example.springboot.models.User;
import com.example.springboot.services.CourseServiceImpl;
import com.example.springboot.services.UserDetailsServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import com.example.springboot.utils.JwtTokenUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
public class CourseControllerUnitTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CourseServiceImpl courseService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    private User user;
    private Course course1;
    private Course course2;
    private Role roleTrainee;
    private Role roleMentor;
    private List<Course> courseList;

    @Before
    public void setup() {
        roleMentor = new Role();
        roleMentor.setName("ROLE_MENTOR");

        roleTrainee = new Role();
        roleTrainee.setName("ROLE_TRAINEE");

        user = new User();
        user.setId(1L);
        user.setName("mentor");
        user.setEmail("email");
        user.setRoles(new HashSet<>(Arrays.asList(roleMentor, roleTrainee)));

        course1 = new Course();
        course1.setId(1L);
        course1.setName("courseName");
        course1.setLongDescription("Long");
        course1.setShortDescription("Short");
        course1.setMeetLink("meetLink");
        course1.setUser(user);
        course1.setSubscriptions(new HashSet<>());

        course2 = new Course();
        course2.setId(2L);
        course2.setName("courseName2");
        course2.setLongDescription("Long2");
        course2.setShortDescription("Short2");
        course2.setMeetLink("meetLink2");
        course2.setUser(user);
        course2.setSubscriptions(new HashSet<>());

        courseList = new ArrayList<>();
        courseList.add(course1);
        courseList.add(course2);
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenCourses_whenGetAllCourses_thenReturnJsonArray() throws Exception{
        given(courseService.findAll()).willReturn(courseList);

        mvc.perform(get("/course/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(course1.getName())))
                .andExpect(jsonPath("$[1].name", is(course2.getName())));
    }

     @Test
     @WithMockUser()
    public void givenNotAuthenticated_whenGetAllCourses_thenReturnJsonArray() throws Exception{
        given(courseService.findAll()).willReturn(courseList);

        mvc.perform(get("/course/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(course1.getName())))
                .andExpect(jsonPath("$[1].name", is(course2.getName())));
    }

    @Test
    @WithMockUser()
    public void givenNotAuthenticated_whenCreateCourse_thenReturn403() throws Exception{
        mvc.perform(post("/course/create")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
