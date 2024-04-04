package com.example.aop;

import com.example.mapper.LogMapper;
import com.example.pojo.*;
import com.example.utils.IPUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    //切入点1
    @Pointcut("@annotation(com.example.aop.CLogging)")
    public void pointCut1() {

    }

    //切入点2
    @Pointcut("@annotation(com.example.aop.PLogging)")
    public void pointCut2() {

    }

    //环绕通知
    @Around("pointCut1()")
    public Object log1(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        CustomerLog customerLog = customerLog(proceedingJoinPoint);
        Result<?> result = (Result<?>) proceedingJoinPoint.proceed();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof String) {
            if (result.getData2() != null) {
                ValidUser validUser = (ValidUser) result.getData2();
                customerLog.setUsername(validUser.getUsername());
            }
        } else {
            Customer customer = (Customer) authentication.getPrincipal();
            customerLog.setUsername(customer.getUsername());
        }
        customerLog.setStatus(result.getCode());
        logMapper.customerAdd(customerLog);
        return result;
    }

    //记录日志
    private CustomerLog customerLog(ProceedingJoinPoint proceedingJoinPoint) throws JsonProcessingException {
        CustomerLog customerLog = new CustomerLog();
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CLogging CLogging = method.getAnnotation(CLogging.class);
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.
                requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        customerLog.setIp(IPUtil.getIpAddress(httpServletRequest));
        customerLog.setModule(CLogging.module());
        customerLog.setOperation(CLogging.operation());
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        customerLog.setMethod(className + "." + methodName + "()");
        Object[] objects = proceedingJoinPoint.getArgs();
        String params = objectMapper.writeValueAsString(objects);
        customerLog.setParams(params);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        customerLog.setCreateTime(dateTimeFormatter.format(LocalDateTime.now()));
        return customerLog;
    }

    //环绕通知
    @Before("pointCut2()")
    public void log2(JoinPoint joinPoint) {
        PreferenceLog preferenceLog = preferenceLog(joinPoint);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof String)) {
            Customer customer = (Customer) authentication.getPrincipal();
            preferenceLog.setUsername(customer.getUsername());
        }
        logMapper.preferenceAdd(preferenceLog);
    }

    private PreferenceLog preferenceLog(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        Preference preference = (Preference) objects[0];
        PreferenceLog preferenceLog = new PreferenceLog();
        preferenceLog.setCategoryId(preference.get_id());
        preferenceLog.setDuration(preference.getDuration());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        preferenceLog.setCreateTime(dateTimeFormatter.format(LocalDateTime.now()));
        return preferenceLog;
    }

}
