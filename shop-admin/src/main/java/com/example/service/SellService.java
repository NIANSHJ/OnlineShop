package com.example.service;

import com.example.pojo.Prediction;
import com.example.pojo.Sell;

import java.util.List;

public interface SellService {

    //获取各分类销售额服务
    List<Sell> statistic();

    //获取热销商品服务
    List<Prediction> prediction();

}
