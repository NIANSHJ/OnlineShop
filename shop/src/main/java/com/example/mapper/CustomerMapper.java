package com.example.mapper;

import com.example.pojo.Customer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CustomerMapper {
    //根据用户名查询顾客
    @Select("select * from customer where username=#{username}")
    Customer findByUserName(String username);

    //顾客注册
    @Insert("insert into customer(username,password,create_time)" +
            " values(#{username},#{password},now())")
    void add(String username, String password);

    //更新顾客信息
//    @Update("update customer set update_time=now() where id=#{id}")
    void update(Customer customer);

    //更新顾客密码
    @Update("update customer set password=#{password} where id=#{id}")
    void updatePassword(String password, Long id);
}
