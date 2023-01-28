package com.example.springboot;

import com.example.springboot.config.WebSecurityConfig;
import com.example.springboot.controllers.CourseController;
import com.example.springboot.exceptions.NoSuchCourseExistsException;
import com.example.springboot.models.Course;
import com.example.springboot.models.Role;
import com.example.springboot.models.Subscription;
import com.example.springboot.models.User;
import com.example.springboot.services.CourseServiceImpl;
import com.example.springboot.services.UserDetailsServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import com.example.springboot.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
@Import(WebSecurityConfig.class)
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
    private ObjectMapper objectMapper;

    private User mentor;
    private Course course1;
    private Course course2;
    private Role roleTrainee;
    private Role roleMentor;
    private List<Course> courseList;
    private Subscription subscription;
    final Long NON_EXISTENT_ID = 9999L;


    @Before
    public void setup() {
        objectMapper = new ObjectMapper();

        roleMentor = new Role();
        roleMentor.setName("ROLE_MENTOR");

        roleTrainee = new Role();
        roleTrainee.setName("ROLE_TRAINEE");

        mentor = new User();
        mentor.setId(1L);
        mentor.setName("mentor");
        mentor.setEmail("email");
        mentor.setRoles(new HashSet<>(Arrays.asList(roleMentor, roleTrainee)));

        course1 = new Course();
        course1.setId(1L);
        course1.setName("courseName");
        course1.setLongDescription("Long");
        course1.setShortDescription("Short");
        course1.setMeetLink("meetLink");
        course1.setUser(mentor);

        course2 = new Course();
        course2.setId(2L);
        course2.setName("courseName2");
        course2.setLongDescription("Long2");
        course2.setShortDescription("Short2");
        course2.setMeetLink("meetLink2");
        course2.setUser(mentor);
        course2.setSubscriptions(new HashSet<>());

        courseList = new ArrayList<>();
        courseList.add(course1);
        courseList.add(course2);

        subscription = new Subscription();
        subscription.setId(1L);
        subscription.setJoinDate("1/1/2022");
        subscription.setUser(mentor);
        subscription.setCourse(course1);

        course1.setSubscriptions(new HashSet<>(Collections.singletonList(subscription)));
        mentor.setCourses(new HashSet<>(courseList));
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenAuthenticated_whenGetAllCourses_thenReturnJsonArray() throws Exception {
        given(courseService.findAll()).willReturn(courseList);

        mvc.perform(get("/course/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(course1.getName())))
                .andExpect(jsonPath("$[1].name", is(course2.getName())));
    }

    @Test
    public void givenNotAuthenticated_whenGetAllCourses_thenReturnJsonArray() throws Exception {
        given(courseService.findAll()).willReturn(courseList);

        mvc.perform(get("/course/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(course1.getName())))
                .andExpect(jsonPath("$[1].name", is(course2.getName())));
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenAuthenticated_whenGetCourseById_thenReturnJson() throws Exception {
        given(courseService.findById(anyLong())).willReturn(course1);

        mvc.perform(get("/course/" + course1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(7)))
                .andExpect(jsonPath("$.name", is(course1.getName())))
                .andExpect(jsonPath("$.shortDescription", is(course1.getShortDescription())))
                .andExpect(jsonPath("$.longDescription", is(course1.getLongDescription())))
                .andExpect(jsonPath("$.longDescription", is(course1.getLongDescription())))
                .andExpect(jsonPath("$.meetLink", is(course1.getMeetLink())))
                .andExpect(jsonPath("$.subscriptions", hasSize(1)));
    }

    @Test
    public void givenNotAuthenticated_whenGetCourseById_thenReturn403() throws Exception {
        mvc.perform(get("/course/" + course1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenNonExistentId_whenGetCourseById_thenReturn404() throws Exception {
        given(courseService.findById(NON_EXISTENT_ID)).willThrow(new NoSuchCourseExistsException());

        mvc.perform(get("/course/" + NON_EXISTENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(404)));
    }

    @Test
    @WithMockUser(roles = {"MENTOR", "TRAINEE"})
    public void givenMentor_whenCreateCourse_thenReturnStatusCreated() throws Exception {
        given(userService.findById(anyLong())).willReturn(mentor);
        given(courseService.create(ArgumentMatchers.any(Course.class))).willReturn("Course saved successfully");

        Map<String, String> body = new HashMap<>();
        body.put("name", "newCourse");

        mvc.perform(post("/course/create/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenNotAuthenticated_whenCreateCourse_thenReturn403() throws Exception {
        mvc.perform(post("/course/create/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "TRAINEE")
    public void givenTrainee_whenCreateCourse_thenReturn403() throws Exception {
        mvc.perform(post("/course/create/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenMentorWithCourses_whenGetCoursesOfMentor_thenReturnJsonArray() throws Exception {
        given(userService.findByName(anyString())).willReturn(mentor);
        given(courseService.findCoursesByMentor(anyString())).willReturn(courseList);
        int a;

        mvc.perform(get("/course/courses/mentor/ " + anyString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(course1.getName())))
                .andExpect(jsonPath("$[1].name", is(course2.getName())));
    }
    @Test
    public void givenNotAuthenticated_whenGetCoursesOfMentor_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/course/courses/mentor/ " + mentor.getName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenCourses_whenGetCoursesFilteredByName_thenReturnCourse() throws Exception {
        given(courseService.filterByName(anyString())).willReturn(Collections.singletonList(course1));

        mvc.perform(get("/course/courses/ " + anyString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(course1.getName())));
    }

    @Test
    public void givenNotAuthenticated_whenGetCoursesFilteredByName_thenIsOk() throws Exception {
        mvc.perform(get("/course/courses/ " + anyString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"MENTOR", "TRAINEE"})
    public void givenMentorAndCourse_whenUpdateCourse_thenReturnStatusOk() throws Exception {
        given(courseService.update(ArgumentMatchers.any(Course.class), anyLong())).willReturn("Course updated succesfully");

        Map<String, String> body = new HashMap<>();
        body.put("name", "newName");
        body.put("longDescription", "newLong");
        body.put("shortDescription", "newShort");
        body.put("meetLink", "newLink");

        mvc.perform(put("/course/update/" + course1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotAuthenticated_whenUpdateCourse_thenReturnStatusForbidden() throws Exception {
        mvc.perform(put("/courses/update/" + course1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

     @Test
     @WithMockUser(roles = {"MENTOR", "TRAINEE"})
     public void givenMentorAndCourse_whenDeleteCourse_thenReturnStatusOk() throws Exception {
         given(courseService.delete(anyLong())).willReturn("Course deleted successfully");

         mvc.perform(delete("/course/delete/" + anyLong())
                         .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk());
    }
}
