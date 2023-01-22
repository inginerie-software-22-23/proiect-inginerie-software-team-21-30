package com.example.springboot.controllers;

import com.example.springboot.DTOs.RecipeCardDTO;
import com.example.springboot.DTOs.RecipeDetailsDTO;
import com.example.springboot.models.Recipe;
import com.example.springboot.models.User;
import com.example.springboot.services.RecipeServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin
    @PostMapping(value = "/create/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody Recipe recipe, @PathVariable Long userId) {
        User user = userService.findById(userId);
        recipe.setUser(user);
        return recipeService.create(recipe);
    }

    @CrossOrigin
    @GetMapping("/{recipeId}")
    public RecipeDetailsDTO findById(@PathVariable Long recipeId) {
        return new RecipeDetailsDTO(recipeService.findById(recipeId));
    }

    @CrossOrigin
    @GetMapping("/recipes")
    public List<RecipeCardDTO> findAll(@RequestParam(required = false) String username) {
        List<Recipe> recipes = recipeService.findAll();
        List<RecipeCardDTO> recipesToReturn = recipes.stream()
                .filter(x -> !x.getUser().getName().equals(username))
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

    @CrossOrigin
    @PutMapping(value = "/update/{recipeId}")
    public String update(@RequestBody Recipe newRecipeData, @PathVariable Long recipeId) {
        Recipe recipeToUpdate = recipeService.findById(recipeId);

        recipeToUpdate.setName(newRecipeData.getName());
        recipeToUpdate.setImage(newRecipeData.getImage());
        recipeToUpdate.setDescription(newRecipeData.getDescription());
        recipeToUpdate.setIngredients(newRecipeData.getIngredients());
        recipeToUpdate.setEstimatedPrepTimeInMinutes(newRecipeData.getEstimatedPrepTimeInMinutes());

        return recipeService.update(recipeToUpdate);
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{recipeId}")
    public String delete(@PathVariable Long recipeId) {
        return recipeService.delete(recipeId);
    }
}
