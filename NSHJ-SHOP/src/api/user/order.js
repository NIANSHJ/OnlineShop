import request from '@/utils/user_request'

//订单列表查询服务
const orderList = (params) => {
    return new Promise((resolve) => {
        request.get('/order', { params: params }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//订单详情服务
const orderDetail = (orderId) => {
    return new Promise((resolve) => {
        request.get('/order/detail', { params: { orderId: orderId } }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//订单确认邮件服务
const orderEmail = (emailData) => {
    return new Promise((resolve) => {
        request.post('/order/email', emailData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//订单删除服务
const deleteOrder = (orderId) => {
    return new Promise((resolve) => {
        request.delete('/order', { params: { orderId: orderId } }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default {
    orderList, orderDetail, orderEmail, deleteOrder
};