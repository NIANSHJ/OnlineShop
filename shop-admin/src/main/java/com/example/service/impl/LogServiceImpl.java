package com.example.service.impl;

import com.example.mapper.LogMapper;
import com.example.pojo.Log;
import com.example.pojo.PageBean;
import com.example.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.constant.Constant.CUSTOMER_COLLECTION_NAME;
import static com.example.constant.Constant.SYSTEM_COLLECTION_NAME;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

    //获取顾客日志服务
    @Override
    public PageBean<Log> customerLog(Integer pageNum, Integer pageSize) {
        PageBean<Log> pageBean = new PageBean<>();
        List<Log> logs = logMapper.customerLog(pageNum, pageSize);
        pageBean.setTotal(logMapper.count(CUSTOMER_COLLECTION_NAME));
        pageBean.setItems(logs);
        return pageBean;
    }

    //获取系统日志服务
    @Override
    public PageBean<Log> systemLog(Integer pageNum, Integer pageSize) {
        PageBean<Log> pageBean = new PageBean<>();
        List<Log> logs = logMapper.systemLog(pageNum, pageSize);
        pageBean.setTotal(logMapper.count(SYSTEM_COLLECTION_NAME));
        pageBean.setItems(logs);
        return pageBean;
    }

    //删除顾客日志服务
    @Override
    public void deletCustomerLog(String id) {
        logMapper.deleteCustomerLog(id);
    }

    //删除系统日志服务
    @Override
    public void deleteSystemLog(String id) {
        logMapper.deleteSystemLog(id);
    }

}
