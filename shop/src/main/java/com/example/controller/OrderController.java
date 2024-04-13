package com.example.controller;

import com.example.aop.CLogging;
import com.example.pojo.*;
import com.example.service.CartService;
import com.example.service.GoodsService;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@SuppressWarnings("unchecked")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private GoodsService goodsService;

    //添加订单
    @PostMapping
    @CLogging(module = "订单", operation = "顾客新增订单")
    public Result<?> add(@RequestBody Map<String, Object> params) {
        Order order = new Order();
        order.setUsername(String.valueOf(params.get("username")));
        order.setTotal(Float.parseFloat(String.valueOf(params.get("total"))));
        orderService.addOrder(order);
        List<String> ids = (List<String>) params.get("ids");
        List<CartItem> cartItems = orderService.addOrderGoods(order.getId(), ids);
        cartService.delete(ids);
        goodsService.update(cartItems);
        return Result.success();
    }

    //查询订单列表
    @GetMapping
    public Result<PageBean<Order>> list(Integer pageNum, Integer pageSize) {
        PageBean<Order> orders = orderService.list(pageNum, pageSize);
        return Result.success(orders);
    }

    //查询订单详情
    @GetMapping("/detail")
    public Result<List<OrderGoods>> detail(String orderId) {
        List<OrderGoods> orderGoodss = orderService.detail(orderId);
        return Result.success(orderGoodss);
    }

    //删除订单
    @DeleteMapping
    @CLogging(module = "订单", operation = "顾客删除订单")
    public Result<?> delete(String orderId) {
        orderService.delete(orderId);
        return Result.success();
    }

}
