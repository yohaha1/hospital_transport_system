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
    public String login(String username, String password) throws Exception {
        // 使用 AuthenticationManager 进行认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        System.out.println("登录结果："+ authentication.getPrincipal());

        // 将认证信息存入 SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 构造 JwtEntity 对象：从认证对象中获取用户名和角色信息
        JwtEntity jwtEntity = new JwtEntity();
        jwtEntity.setUserName(authentication.getName());

        // UserDetails 实现为 MyUserDetails，包装了 User 对象
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        jwtEntity.setUserId(myUserDetails.getUser().getUserid()); // 设置用户 ID
        jwtEntity.setRole(myUserDetails.getUser().getRole());

        String token = jwtTokenUtil.generateToken(jwtEntity);
        // 根据 JwtEntity 生成 JWT Token
        return token;
    }

    @Override
    public String addUser(User user) throws Exception {
        System.out.println("Attempting to add user: " + user.getUsername());

        // 检查用户名是否已存在
        if (userMapper.selectByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user);

        // 插入用户
        userMapper.insert(user);

        return "用户创建成功";
    }

    @Override
    public User selectByUsername(String username) throws Exception {
        // 查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        return user;
    }

}