package com.example.aop;

import java.lang.annotation.*;

//个性化日志注解
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PLogging {

}
