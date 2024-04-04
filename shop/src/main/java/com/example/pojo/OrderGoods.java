package com.example.pojo;

import lombok.Data;

@Data
public class OrderGoods {

    private String goodsId;//商品ID

    private String goodsName;//商品名称

    private String coverImg;//封面图像

    private Float price;//商品单价

    private Integer count;//商品数量
}
