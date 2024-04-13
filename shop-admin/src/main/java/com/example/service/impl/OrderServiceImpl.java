package com.example.service.impl;

import com.example.mapper.OrderMapper;
import com.example.pojo.Order;
import com.example.pojo.OrderGoods;
import com.example.pojo.PageBean;
import com.example.service.OrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private OrderMapper orderMapper;

    //订单列表查询服务
    @Override
    public PageBean<Order> list(Integer pageNum, Integer pageSize, String id, Integer userId,
                                String username, String createTime, Boolean state) {
        PageBean<Order> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orders = orderMapper.list(id, userId, username, createTime, state);
        Page<Order> page = (Page<Order>) orders;
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }

    //订单详情服务
    @Override
    public List<OrderGoods> detail(String orderId) {
        return orderMapper.detail(orderId);
    }

    //订单确认邮件服务
    @Override
    public void email(String to, String orderId) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject("【NSHJ】发货确认");
        mailMessage.setText("尊敬的顾客您好！\n\n您的订单号为" + orderId + "的订单已成功下单，快递将于数天后到达，请注意查收。");
        javaMailSender.send(mailMessage);
        orderMapper.update(orderId);
    }

    //订单删除服务
    @Override
    @Transactional
    public void delete(String orderId) {
        orderMapper.delete(orderId);
    }
}
