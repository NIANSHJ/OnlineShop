package com.example.service;

import com.example.pojo.Log;
import com.example.pojo.PageBean;

public interface LogService {

    //获取顾客日志
    PageBean<Log> customerLog(Integer pageNum, Integer pageSize);

    //获取系统日志
    PageBean<Log> systemLog(Integer pageNum, Integer pageSize);

    //删除顾客日志
    void deletCustomerLog(String id);

    //删除系统日志
    void deleteSystemLog(String id);

}
