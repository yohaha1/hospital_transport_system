package com.example.demo.controller;


import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.payload.LoginRequest;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("正在登录: "+loginRequest.getUsername());
        try{
            String jwt = userService.login(loginRequest.getUsername(),loginRequest.getPassword());
            System.out.println("生成token: "+jwt);
            return ResponseEntity.ok(ApiResponse.success(jwt));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }catch (BadCredentialsException e){
            return ResponseEntity.status(400).body(ApiResponse.failure(e.getMessage()));
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, Object> params) {
        System.out.println("testtttttttttt"+params);
        int userId = (int)params.get("userId");
        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");
        try{
            userService.changePassword(userId,oldPassword,newPassword);
            return ResponseEntity.ok(ApiResponse.success("密码修改成功！"));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<?> getAllUsers() {
        try{
            List<User> res = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success(res));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try{
            String res = userService.addUser(user);
            return ResponseEntity.ok(ApiResponse.success(res));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }

    @PostMapping("/del/{userId}")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<?> delUser(@PathVariable int userId) {
        try{
            String res = userService.delUser(userId);
            return ResponseEntity.ok(ApiResponse.success(res));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }

    @GetMapping("/getByUsername/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.selectByUsername(username);
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }

    @GetMapping("/getFreeTransCount")
    public ResponseEntity<?> getFreeTransCount() {
        try{
            Map<String, Integer> counts;
            counts = userService.getFreeTransCount();
            return ResponseEntity.ok(ApiResponse.success(counts));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }

    @GetMapping("/getNotifications/{userId}")
    @PreAuthorize("hasAnyRole('doctor', 'transporter', 'admin')")
    public ResponseEntity<?> getNotifications(@PathVariable("userId") int userId) {
        try{
            List<Map<String, Object>> notifications = userService.getNotifications(userId);
            return ResponseEntity.ok(ApiResponse.success(notifications));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }

    @GetMapping("/getUserStatisticData/{userId}")
    @PreAuthorize("hasAnyRole('doctor', 'transporter')")
    public ResponseEntity<?> getUserStatisticData(@PathVariable("userId") int userId) {
        try{
            Map<String, Object> data = userService.getUserStatisticData(userId);
            return ResponseEntity.ok(ApiResponse.success(data));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(ApiResponse.failure(e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(ApiResponse.failure(e.getMessage()));
        }
    }
}
