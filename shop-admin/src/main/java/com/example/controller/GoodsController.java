package com.example.controller;

import com.example.aop.Logging;
import com.example.pojo.*;
import com.example.service.CloudFileService;
import com.example.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CloudFileService cloudFileService;

    //添加商品
    @PostMapping
    @PreAuthorize("hasAuthority('seller:goods:update')")
    @Logging(module = "商品", operation = "新增商品")
    public Result<?> add(@RequestBody @Validated Goods goods) {
        goodsService.add(goods);
        goodsService.recommend(goods.getCategoryId());
        return Result.success();
    }

    //查询商品列表
    @GetMapping
    @PreAuthorize("hasAuthority('all:goods:list')")
    @Logging(module = "商品", operation = "查询商品")
    public Result<PageBean<Goods>> list(Integer pageNum, Integer pageSize,
                                        @RequestParam(required = false) String id,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) Integer categoryId,
                                        @RequestParam(required = false) String createTime,
                                        @RequestParam(required = false) String updateTime) {
        PageBean<Goods> goodss = goodsService.list(pageNum, pageSize, id, name, categoryId, createTime, updateTime);
        return Result.success(goodss);
    }

    @GetMapping("/statistic")
    @PreAuthorize("hasAuthority('all:goods:list')")
    public Result<?> statistic(String id) {
        List<GoodsSell> statistic = goodsService.statistic(id);
        return Result.success(statistic);
    }

    //更新商品
    @PutMapping
    @PreAuthorize("hasAuthority('seller:goods:update')")
    @Logging(module = "商品", operation = "更新商品")
    public Result<?> update(@RequestBody @Validated Goods goods) {
        goodsService.update(goods);
        goodsService.recommend(goods.getCategoryId());
        return Result.success();
    }

    //删除商品
    @DeleteMapping
    @PreAuthorize("hasAuthority('seller:goods:update')")
    @Logging(module = "商品", operation = "删除商品")
    public Result<?> delete(String id, Integer categoryId, String coverImg) throws IOException {
        goodsService.delete(id);
        cloudFileService.delete(coverImg);
        goodsService.recommend(categoryId);
        return Result.success();
    }

}
