package com.example.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class Goods {

    @NotNull
    private String id;//主键ID

    @Pattern(regexp = "^\\S(.){0,78}\\S$")
    private String name;//商品名称

    @NotEmpty
    private String content;//商品介绍

    @NotEmpty
    @URL
    private String coverImg;//封面图像

    @NotNull
    private Integer categoryId;//分类id

    private String categoryName;//分类名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;//更新时间

    @NotNull
    @Min(0)
    private Float price;//商品单价

    @NotNull
    @Min(0)
    private Integer stock;//商品库存

    private Integer sell;//商品销量

}
