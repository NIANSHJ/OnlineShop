import { defineStore } from 'pinia'
import { ref } from 'vue'

//顾客购物车商品总数
const getCartCountStore = defineStore('cartCount', () => {
    //定义cartCount的内容
    const cartCount = ref(0);

    //修改cartCount
    const setCartCount = (newCartCount) => {
        cartCount.value = newCartCount;
    };

    //移除cartCount
    const removeCartCount = () => {
        cartCount.value = 0;
    };

    return {
        cartCount, setCartCount, removeCartCount
    };
}, { persist: true });

export default getCartCountStore;