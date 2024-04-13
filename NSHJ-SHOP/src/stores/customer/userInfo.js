import { defineStore } from 'pinia'
import { ref } from 'vue'

//顾客信息
const getUserInfoStore = defineStore('customerInfo', () => {
    //定义info的内容
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