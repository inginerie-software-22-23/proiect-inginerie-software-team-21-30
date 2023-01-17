package com.example.springboot.services;

import com.example.springboot.exceptions.NoSuchRecipeExistsException;
import com.example.springboot.models.Recipe;
import com.example.springboot.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public String create(Recipe recipe) {
        recipeRepository.save(recipe);
        return "Recipe saved successfully";
    }

    public Recipe findById(@PathVariable Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(()
                        -> new NoSuchRecipeExistsException("No recipe found with id = " + id));
    }

    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public List<Recipe> findRecipesByUser(String username) {
        return recipeRepository.findAll().stream().
                filter(x -> x.getUser().getName().equals(username)).
                collect(Collectors.toList());
    }

    public List<Recipe> filterByName(@PathVariable String recipeName) {
        return recipeRepository.findAll().stream().
                filter(x -> x.getName().contains(recipeName)).
                collect(Collectors.toList());
    }

    public String update(Recipe recipe) {
        Optional<Recipe> recipeToUpdate = recipeRepository.findById(recipe.getId());

        if (recipeToUpdate.isEmpty()) {
            throw new NoSuchRecipeExistsException("No recipe found with id = " + recipe.getId());
        }

        recipeRepository.save(recipe);
        return "Recipe updated successfully";
    }

    public String delete(Long id) {
        Optional<Recipe> recipeToDelete = recipeRepository.findById(id);

        if (recipeToDelete.isEmpty()) {
            throw new NoSuchRecipeExistsException("No recipe found with id = " + id);
        }

        recipeRepository.deleteById(id);
        return "Recipe deleted successfully";
    }
}
