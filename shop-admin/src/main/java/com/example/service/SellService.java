package com.example.service;

import com.example.pojo.Prediction;
import com.example.pojo.CategorySell;

import java.util.List;

public interface SellService {

    //获取各分类销售额服务
    List<CategorySell> statistic();

    //获取热销商品服务
    List<Prediction> prediction();

}
