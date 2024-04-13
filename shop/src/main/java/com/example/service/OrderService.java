package com.example.service;

import com.example.pojo.CartItem;
import com.example.pojo.Order;
import com.example.pojo.OrderGoods;
import com.example.pojo.PageBean;

import java.util.List;

public interface OrderService {

    //添加订单服务
    void addOrder(Order order);

    //添加订单商品服务
    List<CartItem> addOrderGoods(String orderId, List<String> ids);

    //订单列表查询服务
    PageBean<Order> list(Integer pageNum, Integer pageSize);

    //订单详情服务
    List<OrderGoods> detail(String orderId);

    //订单删除服务
    void delete(String orderId);
}
