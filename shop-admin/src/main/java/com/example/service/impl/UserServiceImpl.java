package com.example.service.impl;

import com.example.mapper.UserMapper;
import com.example.pojo.User;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return String.valueOf(user.getUsername());
    }

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    //用户登录服务
    @Override
    public String login(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        claims.put("permissions", user.getPermissions());
        return JwtUtil.genToken(claims);
    }

    //用户更新信息服务
    @Override
    @Transactional
    public void updateInfo(User user) {
        userMapper.update(user);
    }

    //用户更新密码服务
    @Override
    @Transactional
    public void updatePassword(String username, String newPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        userMapper.updatePassword(passwordEncoder.encode(newPassword), user.getId());
    }

}
