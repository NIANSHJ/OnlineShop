package com.example.controller;

import com.example.pojo.Goods;
import com.example.pojo.Result;
import com.example.service.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/preference")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    //获取个性化推荐
    @GetMapping
    public Result<?> recommend() {
        String categoryId = preferenceService.categoryId();
        if (categoryId != null) {
            String categoryName = preferenceService.categoryName(categoryId);
            List<Goods> goodss = preferenceService.list(categoryId);
            return Result.success(goodss, categoryName);
        } else {
            return Result.success(null, null);
        }
    }

}
