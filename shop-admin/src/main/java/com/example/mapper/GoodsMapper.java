package com.example.mapper;

import com.example.pojo.Goods;
import com.example.pojo.GoodsSell;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper {

    //添加商品
    @Insert("insert into goods(id,name,content,cover_img,category_id,create_time,update_time,price,stock,sell) " +
            "values(#{id},#{name},#{content},#{coverImg},#{categoryId},now(),now(),#{price},#{stock},0)")
    void add(Goods goods);

    //查询首页商品推荐列表
    @Select("select id,name,cover_img,price from goods where category_id = #{categoryId} order by id desc limit 5")
    List<Goods> recommend(Integer categoryId);

    //查询商品列表
    List<Goods> list(String id, String name, Integer categoryId, String createTime, String updateTime);

    @Select("SELECT DATE(new_dates.date) AS date,COALESCE(SUM(origin_date.count), 0) AS sell " +
            "FROM(SELECT DATE(DATE_SUB(CURDATE(), INTERVAL n DAY)) AS date FROM " +
            "(SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 " +
            "UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 " +
            "UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 " +
            "UNION ALL SELECT 12 UNION ALL SELECT 13 UNION ALL SELECT 14 UNION ALL SELECT 15 " +
            "UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18 UNION ALL SELECT 19 " +
            "UNION ALL SELECT 20 UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 " +
            "UNION ALL SELECT 24 UNION ALL SELECT 25 UNION ALL SELECT 26 UNION ALL SELECT 27 " +
            "UNION ALL SELECT 28 UNION ALL SELECT 29) AS all_dates) AS new_dates LEFT JOIN " +
            "(SELECT goods_id,DATE(create_time) as create_time, count FROM order_goods WHERE " +
            "create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND goods_id=#{id}) AS origin_date " +
            "ON new_dates.date = origin_date.create_time GROUP BY new_dates.date ORDER BY new_dates.date")
    //查询商品销量情况
    List<GoodsSell> statistic(String id);

    //更新商品
    @Update("update goods set name=#{name},content=#{content},cover_img=#{coverImg},category_id=#{categoryId}," +
            "update_time=now(),price=#{price},stock=#{stock} where id=#{id}")
    void update(Goods goods);

    //删除商品
    @Delete("delete from goods where id=#{id}")
    void deleteById(String id);

}
