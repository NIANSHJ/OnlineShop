package com.example.service.impl;

import com.example.mapper.CustomerMapper;
import com.example.pojo.Customer;
import com.example.service.CustomerService;
import com.example.utils.JwtUtil;
import com.example.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.constant.Constant.*;

@Service
public class CustomersServiceImpl implements CustomerService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        return customer.getUsername();
    }

    //顾客查询服务
    @Override
    public Customer findByUserName(String username) {
        return customerMapper.findByUserName(username);
    }

    //顾客注册服务
    @Override
    @Transactional
    public void register(String username, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        customerMapper.add(username, passwordEncoder.encode(password));
    }

    //顾客验证码服务
    @Override
    public String code(String username) {
        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        redisUtil.set(CODE_KEY + username, code, CODE_EXPIRE);
        return code;
    }

    //顾客邮件服务
    @Override
    public void sendEmail(String to, String code) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject("【NSHJ】邮箱验证码");
        mailMessage.setText("尊敬的顾客您好！\n\n您的验证码是：" + code + "，请在 5 分钟内进行验证。如果该验证码不为您本人申请，请无视。");
        javaMailSender.send(mailMessage);
    }

    //顾客校验验证码服务
    @Override
    public boolean verifyCode(String username, String code) {
        return code.equals(redisUtil.get(CODE_KEY + username));
    }

    //顾客登录服务
    @Override
    public String login(Customer customer) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", customer.getId());
        claims.put("username", customer.getUsername());
        String token = JwtUtil.genToken(claims);
        redisUtil.set(CUSTOMER_KEY + customer.getUsername(), token, LOGIN_EXPIRE);
        return token;
    }

    //顾客更新信息服务
    @Override
    @Transactional
    public void updateInfo(Customer customer) {
        customerMapper.update(customer);
    }

    //顾客更新密码服务
    @Override
    @Transactional
    public void updatePassword(String username, String newPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        customerMapper.updatePassword(passwordEncoder.encode(newPassword), customer.getId());
    }

}
