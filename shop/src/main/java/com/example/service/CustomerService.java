package com.example.service;

import com.example.pojo.Customer;

public interface CustomerService {

    //获取用户名
    String getUserName();

    //顾客查询服务
    Customer findByUserName(String username);

    //顾客验证码服务
    String code(String username);

    //顾客邮件服务
    void sendEmail(String to, String code);

    //顾客校验验证码服务
    boolean verifyCode(String username, String code);

    //顾客注册服务
    void register(String username, String password);

    //顾客登录服务
    String login(Customer customer);

    //顾客更新信息服务
    void updateInfo(Customer customer);

    //顾客更新密码服务
    void updatePassword(String username, String newPwd);

}
