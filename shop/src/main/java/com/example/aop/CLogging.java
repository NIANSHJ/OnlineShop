package com.example.aop;

import java.lang.annotation.*;

//顾客日志注解
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CLogging {

    //模块名称
    String module() default "";

    //操作名称
    String operation() default "";

}
