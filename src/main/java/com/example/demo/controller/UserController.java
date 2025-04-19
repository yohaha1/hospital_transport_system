package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.payload.LoginRequest;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        try{
            String res = userService.addUser(user);
            return ResponseEntity.ok(res);
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

}
