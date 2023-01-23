package com.example.springboot.controllers;

import com.example.springboot.DTOs.UserDTO;
import com.example.springboot.models.User;
import com.example.springboot.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

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

    @CrossOrigin
    @PutMapping(value = "/update/{id}")
    public String update(@RequestBody User newUserData, @PathVariable Long id) {
        return userService.update(newUserData, id);
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{id}")
    public String delete(@PathVariable Long id) {
        return userService.delete(id);
    }
}