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
    private Collection<RoleDTO> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.roles = user.getRoles()
                .stream()
                .map(RoleDTO::new)
                .collect(Collectors.toList());
    }
}