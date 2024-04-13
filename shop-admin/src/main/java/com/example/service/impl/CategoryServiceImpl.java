package com.example.service.impl;

import com.example.mapper.CategoryMapper;
import com.example.pojo.Category;
import com.example.service.CategoryService;
import com.example.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.constant.Constant.CATEGORY_KEY;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private RedisUtil redisUtil;

    //分类添加服务
    @Override
    @Transactional
    public void add(Category category) {
        categoryMapper.add(category);
        redisUtil.hashSet(CATEGORY_KEY, category.getId(), category.getName());
    }

    //分类列表查询服务
    @Override
    public List<Category> list() {
        return categoryMapper.list();
    }

    //分类更新服务
    @Override
    @Transactional
    public void update(Category category) {
        categoryMapper.update(category);
        redisUtil.hashSet(CATEGORY_KEY, category.getId(), category.getName());
    }

    //分类删除服务
    @Override
    @Transactional
    public void delete(String id) {
        categoryMapper.deleteById(id);
        redisUtil.hashDelete(CATEGORY_KEY, id);
    }
}
