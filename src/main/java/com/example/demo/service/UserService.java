package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    ResponseEntity<String> login(String username, String password);
    ResponseEntity<String> addUser (User user);
}