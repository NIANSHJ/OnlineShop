import request from '@/utils/user_request'

//商品列表查询服务
const goodsList = (params) => {
    return new Promise((resolve) => {
        request.get('/goods', { params: params }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//商品添加服务
const addGoods = (goodsData) => {
    return new Promise((resolve) => {
        request.post('/goods', goodsData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//商品上传封面图服务
const fileGoods = (goodsData) => {
    return new Promise((resolve) => {
        request.post('/file', goodsData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//商品更新服务
const updateGoods = (goodsData) => {
    return new Promise((resolve) => {
        request.put('/goods', goodsData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//商品删除服务
const deleteGoods = (id, categoryId, coverImg) => {
    return new Promise((resolve) => {
        request.delete('/goods', { params: { id: id, categoryId: categoryId, coverImg: coverImg } }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default {
    goodsList, addGoods, updateGoods, fileGoods, deleteGoods
};