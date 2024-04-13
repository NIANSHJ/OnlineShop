import axios from 'axios'
import { ElMessage } from 'element-plus'
import getTokenStore from '@/stores/customer/token'
import router from '@/router'

//公共前缀
const baseURL = '/front';
const instance = axios.create({ baseURL });

//添加请求拦截器
instance.interceptors.request.use(
    (config) => {
        const tokenStore = getTokenStore();
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token;
        }
        return config;
    },
    (error) => {
        Promise.reject(error);
    }
);

//添加响应拦截器
instance.interceptors.response.use(
    (result) => {
        if (result.data.code === 0) {
            return result.data;
        }
        else if (!result.data) { }
        else {
            ElMessage.error(result.data.message ? result.data.message : '服务异常');
            return Promise.reject(result.data);
        }
    },
    (error) => {
        if (error.response.status === 401 || error.response.status === 403) {
            ElMessage.error('请先登录');
            router.push('/login');
        } else {
            ElMessage.error('服务异常');
        }
        return Promise.reject(error);
    }
);

export default instance;