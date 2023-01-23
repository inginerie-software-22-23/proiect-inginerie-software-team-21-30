package com.example.springboot.services;

import com.example.springboot.exceptions.NoSuchRecipeExistsException;
import com.example.springboot.models.Recipe;
import com.example.springboot.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Recipe findById(Long id) {
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

    public List<Recipe> filterByName(String recipeName) {
        return recipeRepository.findAll().stream().
                filter(x -> x.getName().contains(recipeName)).
                collect(Collectors.toList());
    }

    public String update(Recipe newRecipeData, Long id) {
        Optional<Recipe> recipeToUpdate = recipeRepository.findById(id);

        if (recipeToUpdate.isEmpty()) {
            throw new NoSuchRecipeExistsException("No recipe found with id = " + id);
        }

        recipeToUpdate.get().setName(newRecipeData.getName());
        recipeToUpdate.get().setImage(newRecipeData.getImage());
        recipeToUpdate.get().setDescription(newRecipeData.getDescription());
        recipeToUpdate.get().setIngredients(newRecipeData.getIngredients());
        recipeToUpdate.get().setEstimatedPrepTimeInMinutes(newRecipeData.getEstimatedPrepTimeInMinutes());

        recipeRepository.save(recipeToUpdate.get());
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
