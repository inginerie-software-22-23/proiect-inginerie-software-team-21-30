package com.example.springboot.services;

import com.example.springboot.exceptions.NoSuchUserExistsException;
import com.example.springboot.exceptions.UserAlreadyExistsException;
import com.example.springboot.models.Role;
import com.example.springboot.models.User;
import com.example.springboot.repositories.RoleRepository;
import com.example.springboot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    public User findById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(()
                        -> new NoSuchUserExistsException("No user found with id = " + id));
    }

    public User findByName(@PathVariable String name) {
        return userRepository.findByName(name)
                .orElseThrow(()
                        -> new NoSuchUserExistsException("No user found with name " + name));
    }

    public String create(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        User existingUser = userRepository.findByName(user.getName()).orElse(null);
        if (existingUser == null) {
            Set<Role> rolesToSave = new HashSet<>();
            for (Role role : user.getRoles()) {
                Optional<Role> existingRole = roleRepository.findFirstByName(role.getName());
                if (existingRole.isPresent()) {
                    role = existingRole.get();
                }
                rolesToSave.add(role);
            }
            user.setRoles(rolesToSave);
            userRepository.save(user);
            return "User saved successfully";
        } else {
            throw new UserAlreadyExistsException("User already exists");
        }
    }
}
