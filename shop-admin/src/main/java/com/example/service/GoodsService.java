package com.example.service;

import com.example.pojo.Goods;
import com.example.pojo.GoodsSell;
import com.example.pojo.PageBean;

import java.util.List;

public interface GoodsService {
    void recommend(Integer categoryId);

    //商品添加服务
    void add(Goods goods);

    //商品列表查询服务
    PageBean<Goods> list(Integer pageNum, Integer pageSize, String id, String name, Integer categoryId,
                         String createTime, String uodateTime);

    //商品销量统计服务
    List<GoodsSell> statistic(String id);

    //商品更新服务
    void update(Goods goods);

    //商品删除服务
    void delete(String id);

}
