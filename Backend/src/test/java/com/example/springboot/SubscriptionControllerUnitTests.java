package com.example.springboot;

import com.example.springboot.config.WebSecurityConfig;
import com.example.springboot.controllers.SubscriptionController;
import com.example.springboot.exceptions.NoSuchSubscriptionExists;
import com.example.springboot.models.Course;
import com.example.springboot.models.Role;
import com.example.springboot.models.Subscription;
import com.example.springboot.models.User;
import com.example.springboot.services.SubscriptionServiceImpl;
import com.example.springboot.services.UserDetailsServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import com.example.springboot.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SubscriptionController.class)
@Import(WebSecurityConfig.class)
public class SubscriptionControllerUnitTests {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private SubscriptionServiceImpl subscriptionService;
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
    private List<Subscription> subscriptionList;
    private Subscription subscription1;
    private Subscription subscription2;
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

        subscription1 = new Subscription();
        subscription1.setId(1L);
        subscription1.setJoinDate("1/1/2022");
        subscription1.setUser(mentor);
        subscription1.setCourse(course1);

        subscription2 = new Subscription();
        subscription2.setId(2L);
        subscription2.setJoinDate("2/1/2022");
        subscription2.setUser(mentor);
        subscription2.setCourse(course2);

        subscriptionList = new ArrayList<>();
        subscriptionList.add(subscription1);
        subscriptionList.add(subscription2);

        course1.setSubscriptions(new HashSet<>(Collections.singletonList(subscription1)));
        mentor.setCourses(new HashSet<>(courseList));
        mentor.setSubscriptions(new HashSet<>(subscriptionList));
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenUserWithSubscriptions_whenGetAllSubscriptions_thenReturnJsonArray() throws Exception {
        given(subscriptionService.getSubscriptionsOfUser(anyString())).willReturn(subscriptionList);

        mvc.perform(get("/subscriptions/all/ " + anyString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].joinDate", is(subscription1.getJoinDate())))
                .andExpect(jsonPath("$[0].course.name", is(subscription1.getCourse().getName())))
                .andExpect(jsonPath("$[1].joinDate", is(subscription2.getJoinDate())))
                .andExpect(jsonPath("$[1].course.name", is(subscription2.getCourse().getName())));
    }

    @Test
    public void givenNotAuthenticated_whenGetAllSubscriptions_thenReturn403() throws Exception {
        mvc.perform(get("/course/" + course1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenUserAndCourse_whenSubscribeUserToCourse_thenReturnStatusOk() throws Exception {
        given(subscriptionService.subscribeUserToCourse(anyLong(), anyLong())).willReturn("User subscribed successfully");

        mvc.perform(post("/subscriptions/subscribe/0/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotAuthenticated_whenSubscribeUserToCourse_thenReturn403() throws Exception {
        mvc.perform(get("/subscriptions/subscribe/0/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenSubscription_whenGetSubscriptionById_thenReturnJson() throws Exception {
        given(subscriptionService.findById(anyLong())).willReturn(subscription1);

        mvc.perform(get("/subscriptions/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.joinDate", is(subscription1.getJoinDate())))
                .andExpect(jsonPath("$.course.name", is(subscription1.getCourse().getName())));
    }

    @Test
    public void givenNotAuthenticated_whenGetSubscriptionById_thenReturn403() throws Exception {
        mvc.perform(get("/subscriptions/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenNonExistentId_whenGetSubscriptionById_thenReturn404() throws Exception {
        given(subscriptionService.findById(anyLong())).willThrow(new NoSuchSubscriptionExists());

        mvc.perform(get("/subscriptions/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(404)));
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenUserAndCourse_whenUnsubscribeUserFromCourse_thenReturnStatusOk() throws Exception {
        given(subscriptionService.unsubscribeUserFromCourse(anyLong(), anyLong())).willReturn("User unsubscribed successfully");

        mvc.perform(post("/subscriptions/unsubscribe/0/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenUnsubscribedUserAndCourse_whenUnsubscribeUserFromCourse_thenReturnStatusOk() throws Exception {
        given(subscriptionService.unsubscribeUserFromCourse(anyLong(), anyLong())).willThrow(new NoSuchSubscriptionExists());

        mvc.perform(post("/subscriptions/unsubscribe/0/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(404)));
    }
}
