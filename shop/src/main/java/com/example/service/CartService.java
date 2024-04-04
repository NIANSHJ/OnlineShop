package com.example.service;

import com.example.pojo.CartItem;

import java.util.List;

public interface CartService {

    //购物车商品总数查询服务
    Long count();

    //购物车添加服务
    int addCartItem(CartItem cartItem);

    //购物车更新服务
    void updateCartItem(String goodsId, Integer count);

    //购物车查询服务
    List<CartItem> list();

    //购物车删除服务
    void delete(List<String> goodsIds);
}
