package com.example.pojo;

import lombok.Data;

@Data
public class OrderGoods {

    private String goodsId;//商品ID

    private String goodsName;//商品名称

    private String coverImg;//封面图像

    private Float price;//价格

    private Integer count;//数量

}
