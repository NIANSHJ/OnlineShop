package com.example.service;

import com.example.pojo.Order;
import com.example.pojo.OrderGoods;
import com.example.pojo.PageBean;

import java.util.List;

public interface OrderService {
    //订单列表查询服务
    PageBean<Order> list(Integer pageNum, Integer pageSize, String id, Integer userId,
                         String username, String createTime, Boolean state);

    //订单详情服务
    List<OrderGoods> detail(String orderId);

    //订单确认邮件服务
    void email(String username, String orderId);

    //订单删除服务
    void delete(String orderId);
}
