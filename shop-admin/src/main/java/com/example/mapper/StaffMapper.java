package com.example.mapper;

import com.example.pojo.User;
import com.example.pojo.ValidUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StaffMapper {

    //添加销售员
    @Insert("insert into user(username, password, create_time)" +
            "values (#{username}, #{password}, now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(ValidUser validUser);

    //添加销售员对应的角色
    @Insert("insert into user_role(user_id, role_id) values (#{id},2)")
    void addRole(ValidUser validUser);

    //查询销售员列表
    @Select("select id, username, create_time from user where id>1")
    List<User> list();

    //删除销售员
    @Delete("delete from user where id=#{id}")
    void deleteById(Integer id);

}
