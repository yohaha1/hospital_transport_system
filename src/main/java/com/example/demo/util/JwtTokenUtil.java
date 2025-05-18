package com.example.demo.util;

import com.example.demo.model.JwtEntity;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
public class JwtTokenUtil {

    private static final String jwtSecret = "ukc8BDbRigUDaY6pZFfWus2jZWLPHOsdfhgsdgdsgfdf";

    private final int jwtExpirationMs = 3600000;

    private static final String USER_INFO_CLAIM = "user-info";
    public static final String TOKEN_HEADER = "Authorization";

//    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String generateToken(JwtEntity jwtEntity) {
        return Jwts.builder()
                .setSubject(jwtEntity.getUserName())     // 设置 token 主体为用户名
                .setIssuedAt(new Date())            // 设置发行时间
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // 设置过期时间
                .claim("id", jwtEntity.getUserId())
                .claim("role", jwtEntity.getRole())
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // 根据用户名生成 JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // 从 token 中提取用户名
    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 从 token 中提取用户ID
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userid", Integer.class);
    }

    // 验证 token 是否有效
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        } catch (JwtException e) {
            log.info("token不存在,错误信息:{}",e.getMessage());
            return false;
        }
        return true;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public static boolean validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(TOKEN_HEADER);
        return validateToken(jwtToken);
    }
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }


}
