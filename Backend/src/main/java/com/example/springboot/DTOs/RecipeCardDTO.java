package com.example.springboot.DTOs;

import com.example.springboot.models.Recipe;
import lombok.Data;

@Data
public class RecipeCardDTO {
    private Long id;
    private String name;
    private byte[] image;
    private Integer estimatedPrepTimeInMinutes;

    public RecipeCardDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.image = recipe.getImage();
        this.estimatedPrepTimeInMinutes = recipe.getEstimatedPrepTimeInMinutes();
    }
}
