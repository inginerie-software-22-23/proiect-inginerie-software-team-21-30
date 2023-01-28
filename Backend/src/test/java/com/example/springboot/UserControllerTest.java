package com.example.springboot;

import com.example.springboot.config.WebSecurityConfig;
import com.example.springboot.controllers.UserController;
import com.example.springboot.exceptions.NoSuchCourseExistsException;
import com.example.springboot.models.*;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@Import(WebSecurityConfig.class)
public class UserControllerTest {

    private ObjectMapper objectMapper;
    private User mentor;
    private Recipe recipe1;
    private Recipe recipe2;
    private Role roleTrainee;
    private Role roleMentor;
    private List<Recipe> recipeList;
    private List<Subscription> subscriptionList;
    private Subscription subscription;
    private Course course1;
    private Course course2;
    private List<Course> courseList;
    final Long NON_EXISTENT_ID = 9999L;

    @Before
    public void setup()
    {
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

        recipe1 = new Recipe();
        recipe1.setId(1L);
        recipe1.setName("recipeName1");
        recipe1.setDescription("desc1");
        recipe1.setIngredients("ingredients1");
        recipe1.setImage(new byte[]{(byte)0x0, 0x1, (byte)0x2});
        recipe1.setEstimatedPrepTimeInMinutes(10);
        recipe1.setUser(mentor);

        recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("recipeName2");
        recipe2.setDescription("desc2");
        recipe2.setIngredients("ingredients2");
        recipe2.setImage(new byte[]{(byte)0x0, 0x1, (byte)0x2});
        recipe2.setEstimatedPrepTimeInMinutes(10);
        recipe2.setUser(mentor);

        recipeList = new ArrayList<>();
        recipeList.add(recipe1);
        recipeList.add(recipe2);

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

        subscriptionList = new ArrayList<>();
        subscriptionList.add(subscription);

        course1.setSubscriptions(new HashSet<>(Collections.singletonList(subscription)));
        mentor.setCourses(new HashSet<>(courseList));
        mentor.setSubscriptions(new HashSet<>(subscriptionList));
        mentor.setRecipes(new HashSet<>(recipeList));
    }
    @Autowired
    public MockMvc mvc;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenAuthenticated_whenFindUserById_thenReturnUser() throws Exception {
        given(userService.findById(anyLong())).willReturn(mentor);

        mvc.perform(get("/user/id/" + mentor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(8)))
                .andExpect(jsonPath("$.name", is(mentor.getName())))
                .andExpect(jsonPath("$.password", is(mentor.getPassword())))
                .andExpect(jsonPath("$.email", is(mentor.getEmail())));
    }


    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenNonExistentId_whenFindById_thenReturn404() throws Exception {
        given(userService.findById(NON_EXISTENT_ID)).willThrow(new NoSuchCourseExistsException());

        mvc.perform(get("/user/id/" + NON_EXISTENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(404)));
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenName_whenGetFindByName_thenReturnUser() throws Exception {
        given(userService.findByName(anyString())).willReturn(mentor);

        mvc.perform(get("/user/name/" + mentor.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", aMapWithSize(8)))
                .andExpect(jsonPath("$.name", is(mentor.getName())))
                .andExpect(jsonPath("$.password", is(mentor.getPassword())))
                .andExpect(jsonPath("$.email", is(mentor.getEmail())));
    }


    @Test
    @WithMockUser(roles = {"MENTOR", "TRAINEE"})
    public void givenAuthenticated_whenUpdateUser_thenReturnStatusOk() throws Exception {
        given(userService.update(ArgumentMatchers.any(User.class), anyLong())).willReturn("User updated succesfully");

        Map<String, Object> body = new HashMap<>();
        body.put("name", "newName");
        body.put("email", "email");

        mvc.perform(put("/user/update/" + mentor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotAuthenticated_whenUpdateUser_thenReturnStatusForbidden() throws Exception {
        mvc.perform(put("/user/update/" + mentor.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"MENTOR", "TRAINEE"})
    public void givenAuthenticated_whenDeleteUser_thenReturnStatusOk() throws Exception {
        given(userService.delete(anyLong())).willReturn("User deleted successfully");

        mvc.perform(delete("/user/delete/" + anyLong())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
