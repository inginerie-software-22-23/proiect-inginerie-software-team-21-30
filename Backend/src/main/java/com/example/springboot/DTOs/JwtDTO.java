package com.example.springboot.DTOs;

import lombok.Data;

@Data
public class JwtDTO {
    private String jwt;

    public JwtDTO(String jwt) {
        this.jwt = jwt;
    }
}