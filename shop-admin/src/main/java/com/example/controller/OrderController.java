package com.example.controller;

import com.example.aop.Logging;
import com.example.pojo.Order;
import com.example.pojo.OrderGoods;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    //查询订单列表
    @GetMapping
    @PreAuthorize("hasAuthority('all:order:list')")
    @Logging(module = "订单", operation = "查询订单")
    public Result<PageBean<Order>> list(Integer pageNum, Integer pageSize,
                                        @RequestParam(required = false) String id,
                                        @RequestParam(required = false) Integer userId,
                                        @RequestParam(required = false) String username,
                                        @RequestParam(required = false) String createTime,
                                        @RequestParam(required = false) Boolean state) {
        PageBean<Order> orders = orderService.list(pageNum, pageSize, id, userId, username,
                createTime, state);
        return Result.success(orders);
    }

    //查询订单详情
    @GetMapping("/detail")
    @PreAuthorize("hasAuthority('all:order:list')")
    @Logging(module = "订单", operation = "查询订单详情")
    public Result<List<OrderGoods>> detail(String orderId) {
        List<OrderGoods> orders = orderService.detail(orderId);
        return Result.success(orders);
    }

    //发送订单确认邮件
    @PostMapping("/email")
    @PreAuthorize("hasAuthority('seller:order:update')")
    @Logging(module = "订单", operation = "订单确认")
    public Result<?> email(@RequestBody Map<String, String> params) {
        orderService.email(params.get("username"), params.get("orderId"));
        return Result.success();
    }

    //删除订单
    @DeleteMapping
    @PreAuthorize("hasAuthority('seller:order:update')")
    @Logging(module = "订单", operation = "删除订单")
    public Result<?> delete(String orderId) {
        orderService.delete(orderId);
        return Result.success();
    }

}
