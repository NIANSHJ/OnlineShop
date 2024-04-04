package com.example.mapper;

import com.example.pojo.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper {

    //获取商品列表(默认)
    List<Goods> list(String name, Integer categoryId);

    //获取商品列表(销量降序)
    List<Goods> listSDesc(String name, Integer categoryId);

    //获取商品列表(价格降序)
    List<Goods> listPDesc(String name, Integer categoryId);

    //获取商品列表(价格升序)
    List<Goods> listPAsc(String name, Integer categoryId);

    //根据商品id查找商品
    @Select("select * from goods where id=#{id}")
    Goods findById(String id);

    //更新商品库存和销量
    @Update("update goods set stock=stock-#{count},sell=sell+#{count} where id=#{id}")
    void update(String id, Integer count);

    //获取个性化商品列表
    @Select("select id,cover_img,name,price from goods where category_id=#{categoryId}")
    List<Goods> findByCategoryId(String categoryId);

}
