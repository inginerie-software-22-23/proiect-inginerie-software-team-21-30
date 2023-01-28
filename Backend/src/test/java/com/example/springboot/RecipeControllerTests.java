package com.example.springboot;

import com.example.springboot.config.WebSecurityConfig;
import com.example.springboot.controllers.RecipeController;
import com.example.springboot.exceptions.NoSuchCourseExistsException;
import com.example.springboot.models.Recipe;
import com.example.springboot.models.Role;
import com.example.springboot.models.Subscription;
import com.example.springboot.models.User;
import com.example.springboot.services.RecipeServiceImpl;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RecipeController.class)
@Import(WebSecurityConfig.class)
public class RecipeControllerTests {

    //configuration
    /*
    @TestConfiguration
    static class RecipeControllerTestsContextConfiguration
    {
        @Bean
        public RecipeController recipeController()
        {
            return new RecipeController();
        }
    }
    */
    //end configuration

    private ObjectMapper objectMapper;
    private User mentor;
    private Recipe recipe1;
    private Recipe recipe2;
    private Role roleTrainee;
    private Role roleMentor;
    private List<Recipe> recipeList;
    private Subscription subscription;
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

        mentor.setRecipes(new HashSet<>(recipeList));
    }

    @Autowired
    public MockMvc mvc;
    @MockBean
    private RecipeServiceImpl recipeService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenMentor_whenCreateRecipe_thenReturnStatusCreated() throws Exception
    {
        given(userService.findById(anyLong())).willReturn(mentor);
        given(recipeService.create(ArgumentMatchers.any(Recipe.class))).willReturn("Recipe saved successfully");

        Map<String, String> body = new HashMap<>();
        body.put("name", "newRecipe");

        mvc.perform(post("/recipe/create/" + anyLong())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenNotAuthenticated_whenCreateRecipe_thenReturn403() throws Exception {
        mvc.perform(post("/recipe/create/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenNotAuthenticated_whenFindRecipeById_thenReturn403() throws Exception {
        mvc.perform(get("/recipes/" + recipe1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenNonExistentId_whenFindRecipeById_thenReturn404() throws Exception {
        given(recipeService.findById(NON_EXISTENT_ID)).willThrow(new NoSuchCourseExistsException());

        mvc.perform(get("/recipe/" + NON_EXISTENT_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode", is(404)));
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenAuthenticated_whenGetAllRecipes_thenReturnJsonArray() throws Exception {
        given(recipeService.findAll()).willReturn(recipeList);

        mvc.perform(get("/recipe/recipes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(recipe1.getName())))
                .andExpect(jsonPath("$[1].name", is(recipe2.getName())));
    }

    @Test
    public void givenNotAuthenticated_whenGetAllRecipes_thenReturnJsonArray() throws Exception {
        given(recipeService.findAll()).willReturn(recipeList);

        mvc.perform(get("/recipe/recipes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(recipe1.getName())))
                .andExpect(jsonPath("$[1].name", is(recipe2.getName())));
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenUserWithRecipes_whenGetRecipesOfUser_thenReturnJsonArray() throws Exception {
        given(userService.findByName(anyString())).willReturn(mentor);
        given(recipeService.findRecipesByUser(anyString())).willReturn(recipeList);

        mvc.perform(get("/recipe/recipes/user/ " + anyString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(recipe1.getName())))
                .andExpect(jsonPath("$[1].name", is(recipe2.getName())));
    }
    @Test
    public void givenNotAuthenticated_whenGetRecipesOfUser_thenReturnJsonArray() throws Exception {
        mvc.perform(get("/recipe/recipes/user/ " + mentor.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"TRAINEE", "MENTOR"})
    public void givenRecipes_whenGetRecipesFilteredByName_thenReturnRecipe() throws Exception {
        given(recipeService.filterByName(anyString())).willReturn(Collections.singletonList(recipe1));

        mvc.perform(get("/recipe/recipes/ " + anyString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(recipe1.getName())));
    }

    @Test
    public void givenNotAuthenticated_whenGetRecipesFilteredByName_thenIsOk() throws Exception {
        mvc.perform(get("/recipe/recipes/ " + anyString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"MENTOR", "TRAINEE"})
    public void givenMentorAndRecipe_whenUpdateRecipes_thenReturnStatusOk() throws Exception {
        given(recipeService.update(ArgumentMatchers.any(Recipe.class), anyLong())).willReturn("Recipe updated succesfully");

        Map<String, Object> body = new HashMap<>();
        body.put("name", "newName");
        body.put("image",new byte[]{(byte)0x0, 0x1, (byte)0x2});
        body.put("description","desc");
        body.put("ingredients", "ing");
        body.put("estimatedPrepTimeInMinutes",10);

        mvc.perform(put("/recipe/update/" + recipe1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNotAuthenticated_whenUpdateRecipe_thenReturnStatusForbidden() throws Exception {
        mvc.perform(put("/recipes/update/" + recipe1.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"MENTOR", "TRAINEE"})
    public void givenMentorAndRecipe_whenDeleteRecipe_thenReturnStatusOk() throws Exception {
        given(recipeService.delete(anyLong())).willReturn("Recipe deleted successfully");

        mvc.perform(delete("/recipe/delete/" + anyLong())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
