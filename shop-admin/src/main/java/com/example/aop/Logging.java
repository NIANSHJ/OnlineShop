package com.example.aop;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logging {
    //模块名称
    String module() default "";

    //操作名称
    String operation() default "";
}
