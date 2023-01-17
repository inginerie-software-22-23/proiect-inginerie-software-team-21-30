package com.example.springboot.controllers;

import com.example.springboot.DTOs.RecipeCardDTO;
import com.example.springboot.DTOs.RecipeDetailsDTO;
import com.example.springboot.models.Recipe;
import com.example.springboot.models.User;
import com.example.springboot.services.RecipeServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipe")
public class RecipeController {
    @Autowired
    private RecipeServiceImpl recipeService;
    @Autowired
    private UserServiceImpl userService;

    @SneakyThrows
    @CrossOrigin
    @PostMapping(value = "/create/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestParam("data") String data, @RequestParam(value = "image", required = false) MultipartFile image, @PathVariable Long userId) {
        User user = userService.findById(userId);

        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = mapper.readValue(data, Recipe.class);

        recipe.setUser(user);
        recipe.setImage(image.getBytes());

        return recipeService.create(recipe);
    }

    @CrossOrigin
    @GetMapping("/{recipeId}")
    public RecipeDetailsDTO findById(@PathVariable Long recipeId) {
        return new RecipeDetailsDTO(recipeService.findById(recipeId));
    }

    @CrossOrigin
    @GetMapping("/recipes")
    public List<RecipeCardDTO> findAll() {
        List<Recipe> recipes = recipeService.findAll();
        List<RecipeCardDTO> recipesToReturn = recipes.stream()
                .sorted(Comparator.comparing(Recipe::getId))
                .map(RecipeCardDTO::new)
                .collect(Collectors.toList());
        return recipesToReturn;
    }

    @CrossOrigin
    @GetMapping("/recipes/user/{username}")
    public List<RecipeCardDTO> findRecipesByUser(@PathVariable String username) {
        User user = userService.findByName(username);

        List<Recipe> recipes = recipeService.findRecipesByUser(user.getName());
        List<RecipeCardDTO> recipesToReturn = recipes.stream()
                .sorted(Comparator.comparing(Recipe::getId))
                .map(RecipeCardDTO::new)
                .collect(Collectors.toList());
        return recipesToReturn;
    }

    @CrossOrigin
    @GetMapping("/recipes/{recipeName}")
    public List<RecipeCardDTO> filterByName(@PathVariable String recipeName) {
        List<Recipe> recipesFilteredByName = recipeService.filterByName(recipeName);
        List<RecipeCardDTO> recipesToReturn = recipesFilteredByName.stream()
                .sorted(Comparator.comparing(Recipe::getId))
                .map(RecipeCardDTO::new)
                .collect(Collectors.toList());
        return recipesToReturn;
    }

    @SneakyThrows
    @CrossOrigin
    @PutMapping(value = "/update/{recipeId}")
    public String update(@RequestParam("data") String newRecipeData, @RequestParam(value = "image", required = false) MultipartFile newRecipeImage, @PathVariable Long recipeId) {
        Recipe recipeToUpdate = recipeService.findById(recipeId);

        ObjectMapper mapper = new ObjectMapper();
        Recipe recipe = mapper.readValue(newRecipeData, Recipe.class);

        recipeToUpdate.setName(recipe.getName());
        recipeToUpdate.setImage(newRecipeImage.getBytes());
        recipeToUpdate.setDescription(recipe.getDescription());
        recipeToUpdate.setIngredients(recipe.getIngredients());
        recipeToUpdate.setEstimatedPrepTimeInMinutes(recipe.getEstimatedPrepTimeInMinutes());

        return recipeService.update(recipeToUpdate);
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{recipeId}")
    public String delete(@PathVariable Long recipeId) {
        return recipeService.delete(recipeId);
    }
}
