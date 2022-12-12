package com.example.springboot.DTOs;

import com.example.springboot.models.Role;
import lombok.Data;

@Data
public class RoleDTO {
    private String name;

    public RoleDTO(Role role) {
        this.name = role.getName();
    }
}