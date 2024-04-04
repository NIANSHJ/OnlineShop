import request from '@/utils/user_request'

//顾客日志查询服务
const customerLog = (params) => {
    return new Promise((resolve) => {
        request.get('/log/customer', { params: params }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//系统日志查询服务
const systemLog = (params) => {
    return new Promise((resolve) => {
        request.get('/log/system', { params: params }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//删除顾客日志服务
const deleteCustomerLog = (id) => {
    return new Promise((resolve) => {
        request.delete('/log/customer', { params: { id: id } }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//删除系统日志服务
const deleteSystemLog = (id) => {
    return new Promise((resolve) => {
        request.delete('/log/system', { params: { id: id } }).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};


export default {
    customerLog, systemLog, deleteCustomerLog, deleteSystemLog
};







