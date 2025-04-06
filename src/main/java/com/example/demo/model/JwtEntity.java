package com.example.demo.model;

import lombok.Data;

/**
 * JWT 实体类：用于存储 JWT 解析后的信息
 */
@Data
public class JwtEntity {
    private Integer userId;
    private String userName;
    private String name;
    private String role;

}