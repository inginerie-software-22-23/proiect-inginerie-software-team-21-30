package com.example.springboot.controllers;

import com.example.springboot.DTOs.UserDTO;
import com.example.springboot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @CrossOrigin
    @GetMapping("/id/{id}")
    public UserDTO findById(@PathVariable Long id) {
        return new UserDTO(userService.findById(id));
    }

    @CrossOrigin
    @GetMapping("/name/{name}")
    public UserDTO findByName(@PathVariable String name) {
        return new UserDTO(userService.findByName(name));
    }
}