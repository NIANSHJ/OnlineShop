package com.example.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class User implements UserDetails {

    @NotNull
    private Long id;//主键ID

    private String username;//用户名

    @JsonIgnore
    private String password;//密码

    @Pattern(regexp = "^1[3456789]\\d{9}$")
    private String phone;//手机号

    @Email
    private String email;//邮箱

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间

    private Set<Role> roles;//角色集合

    private List<String> permissions = new ArrayList<>();//权限集合

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (permissions != null && !permissions.isEmpty()) {
            return permissions.stream().map(SimpleGrantedAuthority::new).
                    collect(Collectors.toSet());
        }
        return null;
    }

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

}
