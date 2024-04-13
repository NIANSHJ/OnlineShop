package com.example.controller;

import com.example.pojo.CartItem;
import com.example.pojo.Result;
import com.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    //获取购物车商品总数
    @GetMapping("/count")
    public Result<Long> count() {
        Long count = cartService.count();
        return Result.success(count);
    }

    //添加商品进购物车
    @PostMapping
    public Result<?> add(@RequestBody CartItem cartItem) {
        int result = cartService.addCartItem(cartItem);
        if (result != -1) return Result.success(result);
        else return Result.error("购物车容量已达上限");
    }

    //更新购物车商品数量
    @PatchMapping("/update")
    public Result<?> update(@RequestBody Map<String, Object> params) {
        cartService.updateCartItem(params.get("goodsId").toString(), (Integer) params.get("count"));
        return Result.success();
    }

    //购物车列表
    @GetMapping
    public Result<List<CartItem>> list() {
        List<CartItem> cartItems = cartService.list();
        return Result.success(cartItems);
    }

    //删除购物车中的商品
    @DeleteMapping
    public Result<?> delete(@RequestBody List<String> goodsIds) {
        cartService.delete(goodsIds);
        return Result.success();
    }

}
