import { defineStore } from 'pinia'
import { ref } from 'vue'

//管理员信息
const getUserInfoStore = defineStore('userInfo', () => {
    //定义info内容
    const info = ref({});

    //修改info
    const setInfo = (newInfo) => {
        info.value = newInfo;
    };

    //移除info
    const removeInfo = () => {
        info.value = {};
    };

    return { info, setInfo, removeInfo };
}, { persist: true }
);

export default getUserInfoStore;