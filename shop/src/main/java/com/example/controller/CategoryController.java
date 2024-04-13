package com.example.controller;

import com.example.aop.PLogging;
import com.example.pojo.Category;
import com.example.pojo.Preference;
import com.example.pojo.Result;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //获取商品分类列表
    @GetMapping
    public Result<List<Category>> recommend() {
        List<Category> categorys = categoryService.list();
        return Result.success(categorys);
    }

    //记录分类停留时长
    @PostMapping
    @PLogging
    public void duration(@RequestBody Preference preference) {

    }

}
