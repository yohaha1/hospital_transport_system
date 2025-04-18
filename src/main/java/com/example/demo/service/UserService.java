package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    String login(String username, String password) throws Exception;
    String addUser (User user) throws Exception;

    User selectByUsername(String username) throws Exception;
}
