package com.example.springboot.services;

import com.example.springboot.models.Recipe;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface RecipeService {
    String create(Recipe recipe);
    Recipe findById(@PathVariable Long id);
    List<Recipe> findAll();
    List<Recipe> findRecipesByUser(String username);
    List<Recipe> filterByName(@PathVariable String name);
    String update(Recipe recipe);
    String delete(@PathVariable Long id);
}
