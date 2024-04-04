package com.example.mapper;

import com.example.pojo.Goods;
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

    //更新商品
    @Update("update goods set name=#{name},content=#{content},cover_img=#{coverImg},category_id=#{categoryId}," +
            "update_time=now(),price=#{price},stock=#{stock} where id=#{id}")
    void update(Goods goods);

    //删除商品
    @Delete("delete from goods where id=#{id}")
    void deleteById(String id);

}
