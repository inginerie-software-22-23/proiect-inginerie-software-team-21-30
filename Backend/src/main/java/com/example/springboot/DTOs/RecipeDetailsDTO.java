package com.example.springboot.DTOs;

import com.example.springboot.models.Recipe;
import lombok.Data;

@Data
public class RecipeDetailsDTO {
    private Long id;
    private UserDTO author;
    private String name;
    private byte[] image;
    private String description;
    private String ingredients;
    private Integer estimatedPrepTimeInMinutes;

    public RecipeDetailsDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.author = new UserDTO(recipe.getUser());
        this.name = recipe.getName();
        this.image = recipe.getImage();
        this.description = recipe.getDescription();
        this.ingredients = recipe.getIngredients();
        this.estimatedPrepTimeInMinutes = recipe.getEstimatedPrepTimeInMinutes();
    }
}
