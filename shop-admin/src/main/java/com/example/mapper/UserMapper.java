package com.example.mapper;

import com.example.pojo.Permission;
import com.example.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.Set;

@Mapper
public interface UserMapper {

    //根据用户名查询用户与用户对应的角色
    User findByUserName(String username);

    //根据角色查询权限
    Set<Permission> findPermission(Set<Integer> roleIds);

    //更新用户信息
    @Update("update user set phone=#{phone},email=#{email} where id=#{id}")
    void update(User user);

    //更新用户密码
    @Update("update user set password=#{password} where id=#{id}")
    void updatePassword(String password, Long id);

}
