import request from '@/utils/customer_request'

//商品列表查询服务
const recommendList = () => {
    return new Promise((resolve) => {
        request.get('/goods/recommend').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//商品列表查询服务
const goodsList = (searchData) => {
    return new Promise((resolve) => {
        request.get('/goods', { params: searchData }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//商品详情服务
const goodsDetail = (id) => {
    return new Promise((resolve) => {
        request.get('/goods/detail', { params: { id: id } }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default { recommendList, goodsList, goodsDetail };