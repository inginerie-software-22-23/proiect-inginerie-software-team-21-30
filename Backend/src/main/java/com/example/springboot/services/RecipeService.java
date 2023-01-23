package com.example.springboot.services;

import com.example.springboot.models.Recipe;

import java.util.List;

public interface RecipeService {
    String create(Recipe recipe);
    Recipe findById(Long id);
    List<Recipe> findAll();
    List<Recipe> findRecipesByUser(String username);
    List<Recipe> filterByName(String name);
    String update(Recipe recipe, Long id);
    String delete(Long id);
}
