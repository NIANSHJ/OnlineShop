import request from '@/utils/customer_request'

//分类列表查询服务
const categoryList = () => {
    return new Promise((resolve) => {
        request.get('/category').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//分类停留时间服务
const categoryDuration = (preferenceData) => {
    return new Promise((resolve) => {
        request.post('/category', preferenceData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default {
    categoryList, categoryDuration
};