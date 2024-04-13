import request from '@/utils/customer_request'

//购物车商品总数查询服务
const cartCount = () => {
    return new Promise((resolve) => {
        request.get('/cart/count').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//购物车列表查询服务
const cartList = () => {
    return new Promise((resolve) => {
        request.get('/cart').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//购物车添加服务
const addCartItem = (cartItemData) => {
    return new Promise((resolve) => {
        request.post('/cart', cartItemData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//购物车更新服务
const updateCartItem = (cartItemData) => {
    return new Promise((resolve) => {
        request.patch('/cart/update', cartItemData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//购物车删除服务
const deleteCartItem = (goodsIds) => {
    return new Promise((resolve) => {
        request.delete('/cart', { data: goodsIds }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default {
    cartCount, cartList, addCartItem, updateCartItem, deleteCartItem
};