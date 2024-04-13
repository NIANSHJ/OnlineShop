import { defineStore } from 'pinia'
import { ref } from 'vue'

//顾客登录token
const getTokenStore = defineStore('customerToken', () => {
    //定义token的内容
    const token = ref('');

    //修改token
    const setToken = (newToken) => {
        token.value = newToken;
    };

    //移除token
    const removeToken = () => {
        token.value = null;
    };

    return {
        token, setToken, removeToken
    };
}, { persist: true });

export default getTokenStore;