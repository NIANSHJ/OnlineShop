import request from '@/utils/user_request'

//用户登录服务
const login = (loginData) => {
    return new Promise((resolve) => {
        request.post('/user/login', loginData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//用户获取信息服务
const userInfo = () => {
    return new Promise((resolve) => {
        request.get('/user/userInfo').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//用户更新信息服务
const updateUserInfo = (userInfoData) => {
    return new Promise((resolve) => {
        request.put('/user/update', userInfoData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//用户重置密码服务
const resetPassword = (userPasswordData) => {
    return new Promise((resolve) => {
        request.patch('/user/password', userPasswordData).then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

//用户退出登录服务
const logOut = () => {
    return new Promise((resolve) => {
        request.post('/user/logOut').then((response) => { resolve(response); })
            .catch((error) => { console.log(error); })
    });
};

export default {
    login, userInfo, updateUserInfo, resetPassword, logOut
};