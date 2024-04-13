package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private String id;//主键ID

    private Integer userId;//用户ID

    private String username;//用户名

    private Float total;//订单金额

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//下单时间

    private Boolean state;//订单状态,0表示未发货,1表示已发货

}
