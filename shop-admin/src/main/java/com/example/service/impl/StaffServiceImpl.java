package com.example.service.impl;

import com.example.mapper.StaffMapper;
import com.example.pojo.User;
import com.example.pojo.ValidUser;
import com.example.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

    //销售员添加服务
    @Override
    @Transactional
    public void add(ValidUser validUser) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        validUser.setPassword(passwordEncoder.encode(validUser.getPassword()));
        staffMapper.add(validUser);
        staffMapper.addRole(validUser);
    }

    @Override
    public List<User> list() {
        return staffMapper.list();
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        staffMapper.deleteById(id);
    }

}
