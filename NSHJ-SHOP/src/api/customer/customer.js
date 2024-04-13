import request from '@/utils/customer_request'

//顾客验证码服务
const code = (username) => {
    return new Promise((resolve) => {
        request.post('/customer/email', { 'username': username }).then((response) => { resolve(response); })
            .catch((error) => { resolve(error); })
    });
};

//顾客注册服务
const register = (registerData) => {
    return new Promise((resolve) => {
        request.post('/customer/register', registerData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//顾客登录服务
const login = (loginData) => {
    return new Promise((resolve) => {
        request.post('/customer/login', loginData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//顾客获取信息服务
const customerInfo = () => {
    return new Promise((resolve) => {
        request.get('/customer/customerInfo').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//顾客更新信息服务
const updateCustomerInfo = (userInfoData) => {
    return new Promise((resolve) => {
        request.put('/customer/update', userInfoData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//顾客更新密码服务
const resetPassword = (userPasswordData) => {
    return new Promise((resolve) => {
        request.patch('/customer/password', userPasswordData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//顾客退出登录服务
const logOut = () => {
    return new Promise((resolve) => {
        request.post('/customer/logOut').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default {
    code, register, login, customerInfo, updateCustomerInfo, resetPassword, logOut
};