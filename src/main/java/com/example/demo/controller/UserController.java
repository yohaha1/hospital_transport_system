package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.payload.LoginRequest;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getUsername(),loginRequest.getPassword());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_manager')")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        System.out.println(user);
        return userService.addUser(user);
    }

}
