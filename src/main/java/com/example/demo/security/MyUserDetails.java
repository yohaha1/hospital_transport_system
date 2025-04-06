package com.example.demo.security;

import com.example.demo.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * MyUserDetails 用于包装数据库中的 User 对象，
 * 实现 UserDetails 接口以便于 Spring Security 使用。
 */
public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }

    /**
     * 根据 user.getRole() 返回权限。这里简单示例为返回单个角色，
     * 注意 Spring Security 默认在 hasRole 检查时会自动添加 "ROLE_" 前缀
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 以下方法返回 true，表示账户未过期、未锁定、凭证未过期且已启用
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * 返回原始 User 对象，方便在业务中进一步使用
     */
    public User getUser() {
        return user;
    }
}
