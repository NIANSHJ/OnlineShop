package com.example.controller;

import com.example.aop.Logging;
import com.example.pojo.Prediction;
import com.example.pojo.Result;
import com.example.pojo.CategorySell;
import com.example.service.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sell")
public class SellController {

    @Autowired
    private SellService sellService;

    //获取各分类销售额
    @GetMapping("/statistic")
    @PreAuthorize("hasAuthority('all:statistic')")
    @Logging(module = "数据统计", operation = "分类销量")
    public Result<List<CategorySell>> statistic() {
        List<CategorySell> categorySells = sellService.statistic();
        return Result.success(categorySells);
    }

    //获取销售趋势预测
    @GetMapping("/prediction")
    @PreAuthorize("hasAuthority('all:prediction')")
    @Logging(module = "数据统计", operation = "趋势预测")
    public Result<List<Prediction>> prediction() {
        List<Prediction> predictions = sellService.prediction();
        return Result.success(predictions);
    }

}
