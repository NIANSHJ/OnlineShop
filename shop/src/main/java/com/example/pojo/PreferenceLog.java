package com.example.pojo;

import lombok.Data;

//个性化对象
@Data
public class PreferenceLog {

    private String id;//日志ID

    private String username;//用户名

    private String categoryId;//分类ID

    private Long duration;//停留时间

    private String createTime;//记录时间

}
