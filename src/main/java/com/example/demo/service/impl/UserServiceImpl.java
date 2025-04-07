package com.example.demo.service.impl;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.JwtEntity;
import com.example.demo.model.User;
import com.example.demo.security.MyUserDetails;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> login(String username, String password) {
        try {
            // 使用 AuthenticationManager 进行认证
            System.out.println("Attempting to authenticate user: " + username);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            password
                    )
            );
            System.out.println("Authentication successful for user: " + username);

            // 将认证信息存入 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 构造 JwtEntity 对象：从认证对象中获取用户名和角色信息
            JwtEntity jwtEntity = new JwtEntity();
            jwtEntity.setUserName(authentication.getName());

            System.out.println(authentication.getName());

            // 这里假设你的 UserDetails 实现为 MyUserDetails，包装了 User 对象
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            jwtEntity.setRole(myUserDetails.getUser().getRole());

            // 根据 JwtEntity 生成 JWT Token
            String jwt = jwtTokenUtil.generateToken(jwtEntity);
            return ResponseEntity.ok(jwt);
        } catch (BadCredentialsException ex) {
            System.out.println("Authentication failed for user: " + username);
            return ResponseEntity.status(401).body("无效的用户名或密码");
        }
    }

    @Override
    public ResponseEntity<String> addUser(User user) {
        try {
            System.out.println("Attempting to add user: " + user.getUsername());

            // 检查用户名是否已存在
            if (userMapper.selectByUsername(user.getUsername()) != null) {
                return ResponseEntity.status(400).body("用户名已存在");
            }
            // 对密码进行加密
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            System.out.println(user);
            // 插入用户
            userMapper.insert(user);

            return ResponseEntity.ok("用户创建成功");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("用户创建失败");
        }
    }


}