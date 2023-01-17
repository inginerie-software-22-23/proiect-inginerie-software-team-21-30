package com.example.springboot.DTOs;

import com.example.springboot.models.Recipe;
import lombok.Data;

@Data
public class RecipeSuccinctDTO {
    private Long id;
    private String name;

    public RecipeSuccinctDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
    }
}
