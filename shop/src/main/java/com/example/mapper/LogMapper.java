package com.example.mapper;

import com.example.pojo.CustomerLog;
import com.example.pojo.Preference;
import com.example.pojo.PreferenceLog;
import com.example.utils.MongoDBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.constant.Constant.CUSTOMER_COLLECTION_NAME;
import static com.example.constant.Constant.PREFERENCE_COLLECTION_NAME;

@Repository
public class LogMapper {

    @Autowired
    private MongoDBUtil mongoDBUtil;

    @Autowired
    private CategoryMapper categoryMapper;

    //添加顾客日志
    public void customerAdd(CustomerLog customerLog) {
        mongoDBUtil.insert(customerLog, CUSTOMER_COLLECTION_NAME);
    }

    //添加个性化日志
    public void preferenceAdd(PreferenceLog preferenceLog) {
        mongoDBUtil.insert(preferenceLog, PREFERENCE_COLLECTION_NAME);
    }

    //分析个性化日志
    public String analyze(String username) {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("username").is(username));
        MatchOperation matchOperation = Aggregation.match(criteria);
        GroupOperation groupOperation = Aggregation.group();
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("categoryId").sum("duration").as("total"),
                Aggregation.sort(Sort.Direction.DESC, "total"),
                Aggregation.limit(1)
        );
        List<Preference> preferences = mongoDBUtil.aggregateQuery(aggregation, PREFERENCE_COLLECTION_NAME, Preference.class);
        if (preferences.isEmpty()) {
            return null;
        } else {
            return preferences.getFirst().get_id();
        }
    }

}
