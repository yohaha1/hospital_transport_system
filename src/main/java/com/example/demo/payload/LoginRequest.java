package com.example.demo.payload;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录请求 DTO，包含用户名和密码
 */
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
