package com.example.service;

import com.example.pojo.Category;

import java.util.List;

public interface CategoryService {
    //分类新增服务
    void add(Category category);

    //分类列表查询服务
    List<Category> list();

    //分类更新服务
    void update(Category category);

    //分类删除服务
    void delete(String id);
}
