package com.example.service;

import com.example.pojo.User;
import com.example.pojo.ValidUser;

import java.util.List;

public interface StaffService {

    //添加销售员
    void add(ValidUser validUser);

    //销售员列表查询服务
    List<User> list();

    //销售员删除服务
    void delete(Integer id);

}
