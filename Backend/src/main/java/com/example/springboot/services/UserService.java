package com.example.springboot.services;

import com.example.springboot.models.User;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {
    String create(User user);
    User findById(@PathVariable Long id);
    User findByName(@PathVariable String name);
    String update(User user);
    String delete(@PathVariable Long id);
}
