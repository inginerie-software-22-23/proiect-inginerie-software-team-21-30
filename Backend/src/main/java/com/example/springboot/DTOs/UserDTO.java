package com.example.springboot.DTOs;

import com.example.springboot.models.User;
import lombok.Data;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String password;
    private String email;
    private Collection<RoleDTO> roles;
    private Collection<CourseSuccinctDTO> courses;
    private Collection<SubscriptionWithCourseDTO> subscriptions;
    private Collection<RecipeSuccinctDTO> recipes;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.roles = user.getRoles()
                .stream()
                .map(RoleDTO::new)
                .collect(Collectors.toList());
        this.courses = user.getCourses()
                .stream()
                .map(CourseSuccinctDTO::new)
                .collect(Collectors.toList());
        this.subscriptions = user.getSubscriptions()
                .stream()
                .map(SubscriptionWithCourseDTO::new)
                .collect(Collectors.toList());
        this.recipes = user.getRecipes()
                .stream()
                .map(RecipeSuccinctDTO::new)
                .collect(Collectors.toList());
    }
}
