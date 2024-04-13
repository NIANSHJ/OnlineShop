package com.example.mapper;

import com.example.pojo.Prediction;
import com.example.pojo.CategorySell;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SellMapper {

    //查询销售额
    @Select("select category.name as categoryName,SUM(goods.sell*goods.price) as sales from goods " +
            "left join shop.category on goods.category_id = category.id group by category.name")
    List<CategorySell> statistic();

    //查询销售额
    @Select("SELECT DATE(create_time) as date,SUM(total) as sales FROM customer_order " +
            "where create_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) GROUP BY date")
    List<Prediction> prediction();

}
