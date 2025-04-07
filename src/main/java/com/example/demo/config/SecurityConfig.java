package com.example.demo.config;

import com.example.demo.filter.JwtAuthenticationFilter;
import com.example.demo.security.JwtAuthenticationEntryPoint;
import com.example.demo.service.impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    // 1. 配置认证管理器
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationProvider()::authenticate;
    }

    // 2. 配置认证提供者
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // 3. 配置密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 4. 安全过滤器链
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF
                .csrf(csrf -> csrf.disable())
                // 基于 token，所以不需要 session
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 配置请求授权
                .authorizeHttpRequests(auth -> auth
                        // 允许网站静态资源无授权访问
                        .requestMatchers(HttpMethod.GET,
                                "/",
                                "/favicon.ico",
                                "/**.html",   // 匹配所有 HTML 文件
                                "/**.css",    // 匹配所有 CSS 文件
                                "/**.js",     // 匹配所有 JS 文件
                                "/swagger-resources/**",  // 匹配 Swagger 资源路径
                                "/v2/api-docs/**"  // 匹配 API 文档路径
                        ).permitAll()
                        // 登录和注册允许匿名访问
                        .requestMatchers("/user/add","/user/login").permitAll()
                        // 允许 OPTIONS 请求
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        // 其他请求需要认证
                        .anyRequest().authenticated()
                )
                // 异常处理
                .exceptionHandling(exception -> exception
//                        .accessDeniedHandler(customAccessDeniedHandler) // 处理没有权限访问
                                .authenticationEntryPoint(unauthorizedHandler) // 处理未认证
                )
                // 允许跨域
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PUT"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    config.setMaxAge(3600L);
                    return config;
                }));


        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}