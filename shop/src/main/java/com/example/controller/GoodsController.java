package com.example.controller;

import com.example.pojo.Goods;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import com.example.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    //获取首页商品推荐列表
    @GetMapping("/recommend")
    public Result<List<List<Goods>>> recommend() {
        List<List<Goods>> recommends = goodsService.recommend();
        return Result.success(recommends);
    }

    //获取商品列表
    @GetMapping
    public Result<PageBean<Goods>> list(Integer pageNum, Integer pageSize,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) Integer categoryId,
                                        @RequestParam(required = false) Integer type) {
        PageBean<Goods> goodss = goodsService.list(pageNum, pageSize, name, categoryId, type);
        return Result.success(goodss);
    }

    //获取商品信息
    @GetMapping("/detail")
    public Result<Goods> goodsInfo(String id) {
        Goods goods = goodsService.findById(id);
        return Result.success(goods);
    }

}
