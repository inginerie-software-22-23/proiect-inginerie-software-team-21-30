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
    @PutMapping(value = "/update/{userId}")
    public String update(@RequestBody User newUserData, @PathVariable Long userId) {
        User userToUpdate = userService.findById(userId);

        userToUpdate.setName(newUserData.getName());
        userToUpdate.setEmail(newUserData.getEmail());

        return userService.update(userToUpdate);
    }

    @CrossOrigin
    @DeleteMapping(value = "/delete/{userId}")
    public String delete(@PathVariable Long userId) {
        return userService.delete(userId);
    }
}