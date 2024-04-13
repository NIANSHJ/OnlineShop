package com.example.pojo;

import lombok.Data;

//顾客日志对象
@Data
public class CustomerLog {

    private String id;//日志ID

    private String ip;//IP地址

    private String username;//用户名

    private String module;//模块名称

    private String operation;//操作名称

    private String method;//方法名称

    private String params;//参数列表

    private String createTime;//记录时间

    private Integer status;//状态码

}
