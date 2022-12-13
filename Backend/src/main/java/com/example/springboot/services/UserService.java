package com.example.springboot.services;

import com.example.springboot.models.User;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {
    User findById(@PathVariable Long id);
    User findByName(@PathVariable String name);
    String create(User user);
}
