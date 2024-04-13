package com.example.service;

import com.example.pojo.User;

public interface UserService {

    //获取用户名服务
    String getUserName();

    //用户查询服务
    User findByUserName(String username);

    //用户登录服务
    String login(User user);

    //用户更新信息服务
    void updateInfo(User user);

    //用户更新密码服务
    void updatePassword(String username, String newPwd);

}
