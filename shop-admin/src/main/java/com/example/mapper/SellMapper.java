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
    @Select("SELECT DATE(new_dates.date) AS date,COALESCE(SUM(origin_date.total), 0) AS sales " +
            "FROM(SELECT DATE(DATE_SUB(CURDATE(), INTERVAL n DAY)) AS date FROM " +
            "(SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 " +
            "UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 " +
            "UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 " +
            "UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 " +
            "UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 " +
            "UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 " +
            "UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 " +
            "UNION ALL SELECT 28 UNION ALL SELECT 29) AS all_dates) AS new_dates LEFT JOIN " +
            "(SELECT DATE(create_time) AS create_time, total FROM customer_order WHERE " +
            "create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)) AS origin_date ON new_dates.date = " +
            "origin_date.create_time GROUP BY new_dates.date ORDER BY new_dates.date")
    List<Prediction> prediction();

}
