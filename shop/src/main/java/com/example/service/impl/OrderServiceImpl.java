package com.example.service.impl;

import com.example.mapper.OrderMapper;
import com.example.pojo.*;
import com.example.service.OrderService;
import com.example.utils.RedisUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.constant.Constant.CART_KEY;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    //添加订单服务
    @Override
    @Transactional
    public void addOrder(Order order) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        String id = String.valueOf(System.currentTimeMillis()) + customer.getId();
        order.setId(id);
        order.setUserId(customer.getId());
        orderMapper.addOrder(order);
    }

    //添加订单商品服务
    @Override
    public List<CartItem> addOrderGoods(String orderId, List<String> ids) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        List<CartItem> cartItems = redisUtil.hashMultiGet(CART_KEY + customer.getUsername(), ids);
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        for (CartItem cartItem : cartItems) orderMapper.addOrderGoods(orderId, cartItem);
        sqlSession.commit();
        sqlSession.close();
        return cartItems;
    }

    //订单列表查询服务
    @Override
    public PageBean<Order> list(Integer pageNum, Integer pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        PageBean<Order> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Order> as = orderMapper.list(customer.getId());
        Page<Order> page = (Page<Order>) as;
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }

    //订单详情服务
    @Override
    public List<OrderGoods> detail(String orderId) {
        return orderMapper.detail(orderId);
    }

    //订单删除服务
    @Override
    @Transactional
    public void delete(String orderId) {
        orderMapper.delete(orderId);
    }

}
