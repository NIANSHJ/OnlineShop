package com.example.service.impl;

import com.example.pojo.CartItem;
import com.example.pojo.Customer;
import com.example.service.CartService;
import com.example.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.constant.Constant.CART_KEY;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisUtil redisUtil;

    //购物车商品总数查询服务
    @Override
    public Long count() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        return redisUtil.hashGetSize(CART_KEY + customer.getUsername());
    }

    //购物车添加服务
    @Override
    public int addCartItem(CartItem cartItem) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        String key = CART_KEY + customer.getUsername();
        CartItem item = redisUtil.hashGet(key, cartItem.getGoodsId());
        if (item != null) {
            int count = item.getCount() + cartItem.getCount();
            item.setCount(Math.min(count, 10));
            redisUtil.hashSet(key, cartItem.getGoodsId(), item);
            return 0;
        } else {
            if (redisUtil.hashGetSize(key) < 100) {
                redisUtil.hashSet(key, cartItem.getGoodsId(), cartItem);
                return 1;
            } else return -1;
        }
    }

    //购物车更新服务
    @Override
    public void updateCartItem(String goodsId, Integer count) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        String key = CART_KEY + customer.getUsername();
        CartItem cartItem = redisUtil.hashGet(key, goodsId);
        cartItem.setCount(count);
        redisUtil.hashSet(key, goodsId, cartItem);
    }

    //购物车查询服务
    @Override
    public List<CartItem> list() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        Map<Object, Object> cartItemMap = redisUtil.hashGetAll(CART_KEY + customer.getUsername());
        List<CartItem> cartItems = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : cartItemMap.entrySet()) cartItems.add((CartItem) entry.getValue());
        return cartItems;
    }

    //购物车删除服务
    @Override
    public void delete(List<String> goodsIds) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        redisUtil.hashDelete(CART_KEY + customer.getUsername(), goodsIds.toArray());
    }

}
