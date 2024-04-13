package com.example.pojo;

import lombok.Data;

@Data
public class CartItem {

    private String goodsId;//商品ID

    private String goodsName;//商品名称

    private String coverImg;//商品封面图后缀

    private Float price;//商品单价

    private Integer count;//商品数量
}
