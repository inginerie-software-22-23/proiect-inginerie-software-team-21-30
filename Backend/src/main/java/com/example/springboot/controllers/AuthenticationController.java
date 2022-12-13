package com.example.springboot.controllers;

import com.example.springboot.DTOs.JwtDTO;
import com.example.springboot.models.User;
import com.example.springboot.services.UserDetailsServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import com.example.springboot.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getName());

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
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}