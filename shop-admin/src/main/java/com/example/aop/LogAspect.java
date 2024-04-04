package com.example.aop;

import com.example.mapper.LogMapper;
import com.example.pojo.Log;
import com.example.pojo.Result;
import com.example.pojo.User;
import com.example.pojo.ValidUser;
import com.example.utils.IPUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogMapper logMapper;

    //切入点
    @Pointcut("@annotation(com.example.aop.Logging)")
    public void pointCut() {
    }

    //环绕通知
    @Around("pointCut()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Log log = recordLog(proceedingJoinPoint);
        Result<?> result = (Result<?>) proceedingJoinPoint.proceed();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof String) {
            ValidUser validUser = (ValidUser) result.getData2();
            log.setUsername(validUser.getUsername());
        } else {
            User user = (User) authentication.getPrincipal();
            log.setUsername(user.getUsername());
        }
        log.setStatus(result.getCode());
        logMapper.add(log);
        return result;
    }

    //记录日志
    private Log recordLog(ProceedingJoinPoint proceedingJoinPoint) throws JsonProcessingException {
        Log log = new Log();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Logging logging = method.getAnnotation(Logging.class);
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.
                requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        log.setIp(IPUtil.getIpAddress(httpServletRequest));
        log.setModule(logging.module());
        log.setOperation(logging.operation());
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        log.setMethod(className + "." + methodName + "()");
        Object[] objects = proceedingJoinPoint.getArgs();
        String params = objectMapper.writeValueAsString(objects);
        log.setParams(params);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setCreateTime(dateTimeFormatter.format(LocalDateTime.now()));
        return log;
    }
}
