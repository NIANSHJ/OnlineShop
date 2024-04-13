import request from '@/utils/user_request'

//销售员列表查询服务
const sellerList = () => {
    return new Promise((resolve) => {
        request.get('/staff').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//销售员添加服务
const addSeller = (sellerData) => {
    return new Promise((resolve) => {
        request.post('/staff', sellerData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//销售员删除服务
const deleteSeller = (id) => {
    return new Promise((resolve) => {
        request.delete('/staff', { params: { id: id } }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default {
    sellerList, addSeller, deleteSeller
};