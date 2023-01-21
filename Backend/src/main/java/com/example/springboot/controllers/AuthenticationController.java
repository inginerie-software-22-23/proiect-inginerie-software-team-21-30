package com.example.springboot.controllers;

import com.example.springboot.DTOs.JwtDTO;
import com.example.springboot.models.User;
import com.example.springboot.models.UserDetailsImpl;
import com.example.springboot.services.UserDetailsServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import com.example.springboot.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/login")
    public JwtDTO createAuthenticationToken(@RequestBody User user) throws Exception {
        authenticate(user.getName(), user.getPassword());

        final UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(user.getName());
        System.out.println("Roles: " + userDetails.getAuthorities());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return new JwtDTO(token);
    }

    @CrossOrigin
    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody User user) {
        return userService.create(user);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            System.out.println("Name: " + username);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}