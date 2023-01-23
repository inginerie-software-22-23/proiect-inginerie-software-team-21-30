package com.example.springboot.services;

import com.example.springboot.models.User;

public interface UserService {
    String create(User user);
    User findById(Long id);
    User findByName(String name);
    String update(User user, Long id);
    String delete(Long id);
}
