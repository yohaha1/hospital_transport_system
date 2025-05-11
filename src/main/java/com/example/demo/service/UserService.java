package com.example.demo.service;

import com.example.demo.model.Notification;
import com.example.demo.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {

    String login(String username, String password) throws Exception;
    String addUser (User user) throws Exception;

    User selectByUsername(String username) throws Exception;

    void changePassword(int userId, String oldPassword, String newPassword) throws Exception;

    Map<String,Integer> getFreeTransCount() throws Exception;

    List<Map<String, Object>> getNotifications(int userId);

    Map<String, Object> getUserStatisticData(int userId);
}


