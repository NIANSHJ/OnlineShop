package com.example.controller;

import com.example.aop.Logging;
import com.example.pojo.Category;
import com.example.pojo.Result;
import com.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //添加分类
    @PostMapping
    @PreAuthorize("hasAuthority('seller:category:update')")
    @Logging(module = "分类", operation = "新增分类")
    public Result<?> add(@RequestBody @Validated Category category) {
        categoryService.add(category);
        return Result.success();
    }

    //查询分类列表
    @GetMapping
    @PreAuthorize("hasAuthority('all:category:list')")
    @Logging(module = "分类", operation = "查询分类")
    public Result<List<Category>> list() {
        List<Category> categorys = categoryService.list();
        return Result.success(categorys);
    }

    //更新分类
    @PutMapping
    @PreAuthorize("hasAuthority('seller:category:update')")
    @Logging(module = "分类", operation = "更新分类")
    public Result<?> update(@RequestBody @Validated Category category) {
        categoryService.update(category);
        return Result.success();
    }

    //删除分类
    @DeleteMapping
    @PreAuthorize("hasAuthority('seller:category:update')")
    @Logging(module = "分类", operation = "删除分类")
    public Result<?> delete(String id) {
        categoryService.delete(id);
        return Result.success();
    }

}
