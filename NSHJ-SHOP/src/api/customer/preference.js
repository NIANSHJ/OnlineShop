import request from '@/utils/customer_request'

//商品列表查询服务
const preferenceList = () => {
    return new Promise((resolve) => {
        request.get('/preference').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default { preferenceList };