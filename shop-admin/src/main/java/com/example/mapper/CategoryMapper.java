package com.example.mapper;

import com.example.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {
    //新增分类
    @Insert("insert into category(id,name,create_time,update_time) " +
            "values(#{id},#{name},now(),now())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void add(Category category);

    //查询分类列表
    @Select("select * from category")
    List<Category> list();

    //更新分类
    @Update("update category set name=#{name},update_time=now() where id=#{id}")
    void update(Category category);

    //删除分类
    @Delete("delete from category where id=#{id}")
    void deleteById(String id);
}
