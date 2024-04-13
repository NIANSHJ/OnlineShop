import request from '@/utils/user_request'

//获取销售统计服务
const statistic = () => {
    return new Promise((resolve) => {
        request.get('/sell/statistic').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//获取趋势预测服务
const prediction = () => {
    return new Promise((resolve) => {
        request.get('/sell/prediction').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default {
    statistic, prediction
};