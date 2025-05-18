package com.example.demo.filter;

import com.example.demo.service.impl.CustomUserDetailsService;
import com.example.demo.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JWT 过滤器：检查每个请求的 Authorization 头中的 Bearer Token，验证并设置认证信息
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;

    // 构造器注入
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 如果是 WebSocket 路径，直接放行
        if (requestURI.startsWith("/ws")) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
//        System.out.println("Header: " + header);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);
//            System.out.println("提取 Token: " + token);

            if (JwtTokenUtil.validateToken(token)) {
                String username = jwtTokenUtil.extractUsername(token);
                System.out.println("jwt已验证: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        // 构造认证对象
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // 将认证对象存入 SecurityContext
                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                        System.out.println("保存认证信息  " + username);
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}