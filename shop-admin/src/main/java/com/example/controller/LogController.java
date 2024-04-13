package com.example.controller;

import com.example.pojo.Log;
import com.example.pojo.PageBean;
import com.example.pojo.Result;
import com.example.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    //获取顾客日志
    @GetMapping("/customer")
    @PreAuthorize("hasAuthority('all:log:customer')")
    public Result<PageBean<Log>> customerLog(Integer pageNum, Integer pageSize) {
        PageBean<Log> logs = logService.customerLog(pageNum, pageSize);
        return Result.success(logs);
    }

    //获取系统日志
    @GetMapping("/system")
    @PreAuthorize("hasAuthority('admin:log:system')")
    public Result<PageBean<Log>> systemLog(Integer pageNum, Integer pageSize) {
        PageBean<Log> logs = logService.systemLog(pageNum, pageSize);
        return Result.success(logs);
    }

    //删除顾客日志
    @DeleteMapping("/customer")
    @PreAuthorize("hasAuthority('all:log:customer')")
    public Result<?> deleteCustomerLog(String id) {
        logService.deletCustomerLog(id);
        return Result.success();
    }

    //删除系统日志
    @DeleteMapping("/system")
    @PreAuthorize("hasAuthority('admin:log:system')")
    public Result<?> deleteSystemLog(String id) {
        logService.deleteSystemLog(id);
        return Result.success();
    }

}
