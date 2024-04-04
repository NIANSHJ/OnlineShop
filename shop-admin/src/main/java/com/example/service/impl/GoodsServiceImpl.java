package com.example.service.impl;

import com.example.mapper.GoodsMapper;
import com.example.pojo.Goods;
import com.example.pojo.PageBean;
import com.example.service.GoodsService;
import com.example.utils.RedisUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.constant.Constant.RECOMMEND_KEY;


@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisUtil redisUtil;

    //商品推荐服务
    public void recommend(Integer categoryId) {
        redisUtil.hashDelete(RECOMMEND_KEY, String.valueOf(categoryId));
        List<Goods> recommends = goodsMapper.recommend(categoryId);
        redisUtil.hashSet(RECOMMEND_KEY, String.valueOf(categoryId), recommends);
    }

    //商品添加服务
    @Override
    @Transactional
    public void add(Goods goods) {
        goodsMapper.add(goods);
    }

    //商品列表查询服务
    @Override
    public PageBean<Goods> list(Integer pageNum, Integer pageSize, String id, String name, Integer categoryId,
                                String createTime, String updateTime) {
        PageBean<Goods> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodss = goodsMapper.list(id, name, categoryId, createTime, updateTime);
        Page<Goods> page = (Page<Goods>) goodss;
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }

    //商品更新服务
    @Override
    @Transactional
    public void update(Goods goods) {
        goodsMapper.update(goods);
    }

    //商品更新服务
    @Override
    @Transactional
    public void delete(String id) {
        goodsMapper.deleteById(id);
    }
}
