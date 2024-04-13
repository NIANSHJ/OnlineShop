package com.example.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Goods {

    private String id;//主键ID

    private String name;//商品名称

    private String content;//商品介绍

    private String coverImg;//封面图像

    private Integer categoryId;//分类ID

    private String categoryName;//分类名称

    @JsonIgnore
    private LocalDateTime createTime;//创建时间

    @JsonIgnore
    private LocalDateTime updateTime;//更新时间

    private Float price;//商品价格

    private Integer stock;//商品库存

    private Integer sell;//商品销量

}
