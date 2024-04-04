package com.example.service.impl;

import com.example.mapper.GoodsMapper;
import com.example.pojo.CartItem;
import com.example.pojo.Goods;
import com.example.pojo.PageBean;
import com.example.service.GoodsService;
import com.example.utils.RedisUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.constant.Constant.CATEGORY_KEY;
import static com.example.constant.Constant.RECOMMEND_KEY;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    //首页商品推荐列表服务
    @Override
    public List<List<Goods>> recommend() {
        Set<Object> categorySet = redisUtil.hashGetAllKey(CATEGORY_KEY);
        List<String> ids = new ArrayList<>();
        for (Object id : categorySet) ids.add(String.valueOf(id));
        return redisUtil.hashMultiGet(RECOMMEND_KEY, ids);
    }

    //商品列表查询服务
    @Override
    public PageBean<Goods> list(Integer pageNum, Integer pageSize, String name,
                                Integer categoryId, Integer type) {
        PageBean<Goods> pageBean = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodss = switch (type) {
            case 1 -> goodsMapper.listSDesc(name, categoryId);
            case 2 -> goodsMapper.listPDesc(name, categoryId);
            case 3 -> goodsMapper.listPAsc(name, categoryId);
            default -> goodsMapper.list(name, categoryId);
        };
        Page<Goods> page = (Page<Goods>) goodss;
        pageBean.setTotal(page.getTotal());
        pageBean.setItems(page.getResult());
        return pageBean;
    }

    //商品查询服务
    @Override
    public Goods findById(String id) {
        return goodsMapper.findById(id);
    }

    @Override
    public void update(List<CartItem> cartItems) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        GoodsMapper goodsMapper = sqlSession.getMapper(GoodsMapper.class);
        for (CartItem cartItem : cartItems) goodsMapper.update(cartItem.getGoodsId(), cartItem.getCount());
        sqlSession.commit();
        sqlSession.close();
    }
}
