package com.example.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ValidUser {

    @JsonIgnore
    private Long id;//用户ID

    private String username;//用户名

    private String password;//密码

}
