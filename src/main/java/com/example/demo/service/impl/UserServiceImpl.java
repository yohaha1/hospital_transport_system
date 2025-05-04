package com.example.demo.service.impl;

import com.example.demo.mapper.NotificationMapper;
import com.example.demo.mapper.ReceivedNotificationMapper;
import com.example.demo.mapper.TransportTaskMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.JwtEntity;
import com.example.demo.model.Notification;
import com.example.demo.model.TransportTask;
import com.example.demo.model.User;
import com.example.demo.security.MyUserDetails;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtTokenUtil;
import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


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

    @Autowired
    private TransportTaskMapper transportTaskMapper;

    @Autowired
    private ReceivedNotificationMapper receivedNotificationMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public String login(String username, String password) throws Exception {
        // 使用 AuthenticationManager 进行认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

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
//        System.out.println(user);

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

    @Override
    public void changePassword(int userId, String oldPassword, String newPassword) throws Exception {
        // 1. 查询用户
        User user = userMapper.selectByPrimaryKey((long)userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        // 2. 校验原密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("原密码错误");
        }
        // 3. 加密新密码并更新
        String encodedNewPwd = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPwd);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public Map<String, Integer> getFreeTransCount() throws Exception {
        int all = userMapper.getAllTransCount();
        int busy = transportTaskMapper.getBusyTransCount();
        int free = all - busy;
        Map<String, Integer> counts = new HashMap<>();
        counts.put("all", all);
        counts.put("free", free);
        return counts;
    }

    @Override
    public List<Map<String, Object>> getNotifications(int userId){
        //获取所有通知id
        List<Integer> notIds = receivedNotificationMapper.getNotIdsByUserId(userId);
        if (notIds == null || notIds.isEmpty()) {
            return Collections.emptyList();
        }
        //获取所有通知
        List<Notification> notifications = new ArrayList<>();
        for(Integer notId : notIds) {
            Notification notification = notificationMapper.selectByPrimaryKey((long) notId);
            notifications.add(notification);
        }

        //获取任务详情并拼接
        List<Map<String, Object>> result = new ArrayList<>();
        for (Notification notification : notifications) {
            Map<String, Object> notificationMap = new HashMap<>();

            // 添加通知信息
            notificationMap.put("notification", notification);

            // 获取关联任务信息
            if (notification.getTaskid() != null) {
                TransportTask task = transportTaskMapper.selectByPrimaryKey(
                        notification.getTaskid().longValue()
                );
                notificationMap.put("task", task);
            }

            result.add(notificationMap);
        }

        return result;
    }

}