package com.example.springboot.DTOs;

import com.example.springboot.models.Recipe;
import lombok.Data;

@Data
public class RecipeCardDTO {
    private Long id;
    private UserDTO author;
    private String name;
    private byte[] image;
    private Integer estimatedPrepTimeInMinutes;

    public RecipeCardDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.author = new UserDTO(recipe.getUser());
        this.name = recipe.getName();
        this.image = recipe.getImage();
        this.estimatedPrepTimeInMinutes = recipe.getEstimatedPrepTimeInMinutes();
    }
}
