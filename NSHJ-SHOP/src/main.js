import '@/assets/back/main.scss'

import { createApp, ref } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from '@/router'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'
import locale from 'element-plus/dist/locale/zh-cn'

const app = createApp(App);
const pinia = createPinia();
const persist = createPersistedState();
app.use(ElementPlus, { locale });
app.use(router);
pinia.use(persist);
app.use(pinia);
app.mount('#app');

app.provide('cartCount', ref(0));
