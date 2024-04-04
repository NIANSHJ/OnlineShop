package com.example.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//统一响应结果
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {

    private Integer code;//业务状态码  0成功  1失败

    private String message;//提示信息

    private T data;//响应数据

    private T data2;//内部响应数据

    //返回成功响应结果
    public static Result<?> success() {
        return new Result<>(0, "操作成功", null, null);
    }

    //返回成功响应结果(带响应数据)
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "操作成功", data, null);
    }

    //返回成功响应结果(带响应数据和内部响应数据)
    public static <T> Result<T> success(T data, T data2) {
        return new Result<>(0, "操作成功", data, data2);
    }

    //返回失败响应结果
    public static Result<?> error(String message) {
        return new Result<>(1, message, null, null);
    }

}
