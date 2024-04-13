package com.example.service.impl;

import com.example.pojo.Category;
import com.example.service.CategoryService;
import com.example.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.constant.Constant.CATEGORY_KEY;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private RedisUtil redisUtil;

    //分类列表查询服务
    @Override
    public List<Category> list() {
        Map<Object, Object> categoryMap = redisUtil.hashGetAll(CATEGORY_KEY);
        List<Category> categorys = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : categoryMap.entrySet()) {
            Category category = new Category();
            category.setId(String.valueOf(entry.getKey()));
            category.setName(String.valueOf(entry.getValue()));
            categorys.add(category);
        }
        return categorys;
    }

}
