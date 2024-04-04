package com.example.mapper;

import com.example.pojo.Order;
import com.example.pojo.OrderGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMapper {
    //查询订单列表
    List<Order> list(String id, Integer userId, String username, String createTime, Boolean state);

    //订单详情
    @Select("select order_goods.goods_id,order_goods.goods_name,goods.cover_img,order_goods.price,order_goods.count " +
            "from order_goods left join goods on order_goods.goods_id=goods.id where order_goods.order_id=#{orderId}")
    List<OrderGoods> detail(String orderId);

    //修改订单
    @Update("update customer_order set state=true where id=#{orderId}")
    void update(String orderId);

    //删除订单
    void delete(String orderId);
}
