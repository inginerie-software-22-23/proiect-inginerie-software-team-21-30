package com.example.springboot.controllers;

import com.example.springboot.models.User;
import com.example.springboot.services.UserDetailsServiceImpl;
import com.example.springboot.services.UserServiceImpl;
import com.example.springboot.utils.JwtTokenUtil;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @InjectMocks
    AuthenticationController controller;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserServiceImpl userService;

    @Before
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createAuthenticationToken_disabledUser_throwsDisabledException()
    {
        doThrow(DisabledException.class).when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(anyString(),anyString()));
        assertThrows(DisabledException.class, ()->{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("John","password123"));
        });
    }
    @Test
    public void createAuthenticationToken_badCredentials_throwsBadCredentialsException()
    {
        doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(anyString(),anyString()));
        assertThrows(BadCredentialsException.class, ()->{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Michael","password@"));
        });
    }
    @Test
    public void create_validUser_UserSavedMessage()
    {
        when(userService.create(any())).thenReturn("User saved successfully");
        assertEquals("User saved successfully",userService.create(new User()));
    }




}