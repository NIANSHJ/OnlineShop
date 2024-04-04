package com.example.mapper;

import com.example.pojo.CartItem;
import com.example.pojo.Order;
import com.example.pojo.OrderGoods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMapper {
    //添加订单
    @Insert("insert into customer_order(id,user_id,username,total,create_time,state,visible)" +
            "values(#{id},#{userId},#{username},#{total},now(),false,true)")
    void addOrder(Order order);

    //添加订单
    @Insert("insert into order_goods(order_id,goods_id,goods_name,price,count)" +
            "values(#{orderId},#{cartItem.goodsId},#{cartItem.goodsName},#{cartItem.price},#{cartItem.count})")
    void addOrderGoods(String orderId, CartItem cartItem);

    //查询订单列表
    @Select("select * from customer_order where user_id=#{userId} and visible=true")
    List<Order> list(Long userId);

    //订单详情
    @Select("select order_goods.goods_id,order_goods.goods_name,goods.cover_img,order_goods.price,order_goods.count " +
            "from order_goods left join goods on order_goods.goods_id=goods.id where order_goods.order_id=#{orderId}")
    List<OrderGoods> detail(String orderId);

    //删除订单
    @Update("update customer_order set visible=false where id=#{orderId}")
    void delete(String orderId);
}
