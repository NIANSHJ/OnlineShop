import { defineStore } from 'pinia'
import { ref } from 'vue'

//管理员登录token
const getTokenStore = defineStore('userToken', () => {
    //定义token内容
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