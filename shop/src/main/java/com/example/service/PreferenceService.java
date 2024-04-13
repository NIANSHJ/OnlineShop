package com.example.service;

import com.example.pojo.Goods;

import java.util.List;

public interface PreferenceService {

    //获取顾客个性化分类服务
    String categoryId();

    //获取分类ID对应分类名称服务
    String categoryName(String categoryId);

    //个性化推荐商品列表服务
    List<Goods> list(String categoryId);

}
