package com.example.utils;

import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MongoDBUtil {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 功能描述: 创建一个集合
     * 同一个集合中可以存入多个不同类型的对象，我们为了方便维护和提升性能，
     * 后续将限制一个集合中存入的对象类型，即一个集合只能存放一个类型的数据
     *
     * @param name 集合名称，相当于传统数据库的表名
     */
    public void createCollection(String name) {
        mongoTemplate.createCollection(name);
    }

    /**
     * 功能描述: 创建索引
     * 索引是顺序排列，且唯一的索引
     *
     * @param collectionName 集合名称，相当于关系型数据库中的表名
     * @param filedName      对象中的某个属性名
     */
    public String createIndex(String collectionName, String filedName) {
        //配置索引选项
        IndexOptions options = new IndexOptions();
        // 设置为唯一
        options.unique(true);
        //创建按filedName升序排的索引
        return mongoTemplate.getCollection(collectionName).createIndex(Indexes.ascending(filedName), options);
    }

    /**
     * 功能描述: 获取当前集合对应的所有索引的名称
     *
     * @param collectionName 集合名称，相当于关系型数据库中的表名
     */
    public List<String> getAllIndexes(String collectionName) {
        ListIndexesIterable<Document> list = mongoTemplate.getCollection(collectionName).listIndexes();
        //上面的list不能直接获取size，因此初始化arrayList就不设置初始化大小了
        List<String> indexes = new ArrayList<>();
        for (Document document : list) {
            document.forEach((key, value) -> {
                //提取出索引的名称
                if (key.equals("name")) {
                    indexes.add(value.toString());
                }
            });
        }
        return indexes;
    }

    /**
     * 功能描述: 往对应的集合中插入一条数据
     *
     * @param info           存储对象
     * @param collectionName 集合名称
     */
    public <T> void insert(T info, String collectionName) {
        mongoTemplate.insert(info, collectionName);
    }

    /**
     * 功能描述: 往对应的集合中批量插入数据，注意批量的数据中不要包含重复的id
     *
     * @param infos 对象列表
     */
    public <T> void insertMulti(List<T> infos, String collectionName) {
        mongoTemplate.insert(infos, collectionName);
    }

    /**
     * 功能描述: 根据id删除集合中的内容
     *
     * @param id             序列id
     * @param collectionName 集合名称
     * @param clazz          集合中对象的类型
     */
    public <T> void deleteById(String id, Class<T> clazz, String collectionName) {
        // 设置查询条件，当id=#{id}
        Query query = new Query(Criteria.where("id").is(id));
        // mongodb在删除对象的时候会判断对象类型，如果你不传入对象类型，只传入了集合名称，它是找不到的
        // 上面我们为了方便管理和提升后续处理的性能，将一个集合限制了一个对象类型，所以需要自行管理一下对象类型
        // 在接口传入时需要同时传入对象类型
        mongoTemplate.remove(query, clazz, collectionName);
    }

    /**
     * 功能描述: 根据id查询信息
     *
     * @param id             注解
     * @param clazz          类型
     * @param collectionName 集合名称
     */
    public <T> T selectById(String id, Class<T> clazz, String collectionName) {
        // 查询对象的时候，不仅需要传入id这个唯一键，还需要传入对象的类型，以及集合的名称
        return mongoTemplate.findById(id, clazz, collectionName);
    }

    /**
     * 功能描述: 分页查询列表信息
     *
     * @param clazz       对象类型
     * @param collectionName 集合名称
     * @param currentPage 当前页码
     * @param pageSize    分页大小
     */
    public <T> List<T> selectList(Class<T> clazz, String collectionName, int currentPage, int pageSize) {
        //设置分页参数
        Query query = new Query();
        //设置分页信息
        query.limit(pageSize);
        query.skip((long) pageSize * (currentPage - 1));
        return mongoTemplate.find(query, clazz, collectionName);
    }

    /**
     * 功能描述: 根据条件查询集合
     *
     * @param clazz       对象类型
     * @param collectionName 集合名称
     * @param conditions  查询条件
     * @param currentPage 当前页码
     * @param pageSize    分页大小
     */
    public <T> List<T> selectByCondition(Class<T> clazz, String collectionName, Map<String, String> conditions, int currentPage, int pageSize) {
        if (ObjectUtils.isEmpty(conditions)) {
            return selectList(clazz, collectionName, currentPage, pageSize);
        } else {
            //设置分页参数
            Query query = new Query();
            query.limit(pageSize);
            query.skip(currentPage);
            // 往query中注入查询条件
            conditions.forEach((key, value) -> query.addCriteria(Criteria.where(key).is(value)));
            return mongoTemplate.find(query, clazz, collectionName);
        }
    }

    /**
     * 功能描述: 根据条件查询集合
     *
     * @param query       查询条件
     * @param clazz       对象类型
     * @param collectionName 集合名称
     */
    public <T> List<T> selectByQuery(Query query, Class<T> clazz, String collectionName) {
        return mongoTemplate.find(query, clazz, collectionName);
    }

    /**
     * 功能描述: 根据条件查询记录数
     *
     * @param query 查询条件
     * @param collectionName 集合名称
     */
    public long count(Query query, String collectionName) {
        return mongoTemplate.count(query, collectionName);
    }

    /**
     * 功能描述: 聚合查询
     *
     * @param aggregation 聚合操作列表
     * @param collectionName 集合名称
     * @param clazz 对象类型
     */
    public <T> List<T> aggregateQuery(Aggregation aggregation, String collectionName, Class<T> clazz) {
        AggregationResults<T> results = mongoTemplate.aggregate(aggregation, collectionName, clazz);
        return results.getMappedResults();
    }

}
