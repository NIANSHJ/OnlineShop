package com.example.service.impl;

import com.example.mapper.UserMapper;
import com.example.pojo.Permission;
import com.example.pojo.Role;
import com.example.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUserName(username);
        if (user != null) {
            Set<Role> roles = user.getRoles();
            Set<Integer> roleIds = new HashSet<>(roles.size());
            for (Role role : roles) {
                roleIds.add(role.getRoleId());
            }
            Set<Permission> perms = userMapper.findPermission(roleIds);
            List<String> permissions = user.getPermissions();
            for (Permission permission : perms) {
                permissions.add(permission.getPerms());
            }
        }
        return user;
    }

}
