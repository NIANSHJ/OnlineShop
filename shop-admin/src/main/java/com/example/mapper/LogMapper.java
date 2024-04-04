package com.example.mapper;

import com.example.pojo.Log;
import com.example.utils.MongoDBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.constant.Constant.*;

@Repository
public class LogMapper {

    @Autowired
    private MongoDBUtil mongoDBUtil;

    //添加系统日志
    public void add(Log log) {
        mongoDBUtil.insert(log, SYSTEM_COLLECTION_NAME);
    }

    //查询顾客日志
    public List<Log> customerLog(Integer pageNum, Integer pageSize) {
        return mongoDBUtil.selectList(Log.class, CUSTOMER_COLLECTION_NAME, pageNum, pageSize);
    }

    //查询系统日志
    public List<Log> systemLog(Integer pageNum, Integer pageSize) {
        return mongoDBUtil.selectList(Log.class, SYSTEM_COLLECTION_NAME, pageNum, pageSize);
    }

    //查询总记录数
    public long count(String collectionName) {
        return mongoDBUtil.count(new Query(), collectionName);
    }

    //删除顾客日志
    public void deleteCustomerLog(String id) {
        mongoDBUtil.deleteById(id, Log.class, CUSTOMER_COLLECTION_NAME);
    }

    //删除系统日志
    public void deleteSystemLog(String id) {
        mongoDBUtil.deleteById(id, Log.class, SYSTEM_COLLECTION_NAME);
    }

}
