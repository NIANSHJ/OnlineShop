package com.example.service.impl;

import com.example.mapper.GoodsMapper;
import com.example.mapper.LogMapper;
import com.example.pojo.Customer;
import com.example.pojo.Goods;
import com.example.service.PreferenceService;
import com.example.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.constant.Constant.CATEGORY_KEY;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    @Autowired
    private LogMapper logMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisUtil redisUtil;

    //获取顾客个性化分类ID服务
    @Override
    public String categoryId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = (Customer) authentication.getPrincipal();
        return logMapper.analyze(customer.getUsername());
    }

    @Override
    //获取分类名称服务
    public String categoryName(String categoryId) {
        return redisUtil.hashGet(CATEGORY_KEY, categoryId);
    }

    //个性化推荐商品列表服务
    @Override
    public List<Goods> list(String categoryId) {
        return goodsMapper.findByCategoryId(categoryId);
    }

}
