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
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashSet;
import java.util.List;
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

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()
                        -> new NoSuchUserExistsException("No user found with id = " + id));
    }

    public User findByName(String name) {
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

    public String update(User newUserData, Long id) {
        Optional<User> userToUpdate = userRepository.findById(id);

        if (userToUpdate.isEmpty()) {
            throw new NoSuchUserExistsException("No user found with id = " + id);
        }

        if (!newUserData.getName().equals(userToUpdate.get().getName())) {
            long nrOfUsersWithIdenticalName = userRepository.findAll().stream().
                    filter(x -> x.getName().equals(newUserData.getName())).
                    count();
            if (nrOfUsersWithIdenticalName == 1) {
                return "Username already taken.";
            }
        }

        userToUpdate.get().setName(newUserData.getName());
        userToUpdate.get().setEmail(newUserData.getEmail());

        userRepository.save(userToUpdate.get());
        return "User updated successfully";
    }

    public String delete(Long id) {
        Optional<User> userToDelete = userRepository.findById(id);

        if (userToDelete.isEmpty()) {
            throw new NoSuchUserExistsException("No user found with id = " + id);
        }

        userRepository.deleteById(id);
        return "User deleted successfully";
    }
}
