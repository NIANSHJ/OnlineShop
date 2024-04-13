package com.example.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

//校验参数类
@Data
public class ValidUser {

    @Email
    private String username;//用户名

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,30}$")
    private String password;//密码

    private String code;//验证码

}
