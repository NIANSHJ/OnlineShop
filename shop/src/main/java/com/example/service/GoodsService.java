package com.example.service;

import com.example.pojo.CartItem;
import com.example.pojo.Goods;
import com.example.pojo.PageBean;

import java.util.List;

public interface GoodsService {

    //首页商品推荐列表服务
    List<List<Goods>> recommend();

    //商品列表查询服务
    PageBean<Goods> list(Integer pageNum, Integer pageSize, String name,
                         Integer categoryId, Integer type);

    //商品查询服务
    Goods findById(String id);

    //更新商品库存和销量服务
    void update(List<CartItem> cartItems);
}
