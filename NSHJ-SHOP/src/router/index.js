import { createRouter, createWebHistory } from 'vue-router'
import shopLoginBaseVue from '@/views/shop/LoginBase.vue'
import shopLoginVue from '@/views/shop/Login.vue'
import shopRegisterVue from '@/views/shop/Register.vue'
import shopBaseVue from '@/views/shop/ShopBase.vue'
import indexVue from '@/views/shop/menu/Index.vue'
import preferenceVue from '@/views/shop/menu/Preference.vue'
import shopVue from '@/views/shop/menu/Shop.vue'
import goodsVue from '@/views/shop/menu/Goods.vue'
import cartVue from '@/views/shop/menu/Cart.vue'
import settlementVue from '@/views/shop/menu/Settlement.vue'
import orderVue from '@/views/shop/menu/Order.vue'
import userLoginBaseVue from '@/views/back/LoginBase.vue'
import userLoginVue from '@/views/back/Login.vue'
import backBaseVue from '@/views/back/BackBase.vue'
import userIndexVue from '@/views/back/index/Index.vue'
import goodsCategoryVue from '@/views/back/goods/GoodsCategory.vue'
import goodsManageVue from '@/views/back/goods/GoodsManage.vue'
import orderManageVue from '@/views/back/order/OrderManage.vue'
import statisticVue from '@/views/back/sell/Statistic.vue'
import predictionVue from '@/views/back/sell/Prediction.vue'
import staffManageVue from '@/views/back/staff/StaffManage.vue'
import userInfoVue from '@/views/back/user/UserInfo.vue'
import userResetPasswordVue from '@/views/back/user/UserResetPassword.vue'
import customerLogVue from '@/views/back/log/CustomerLog.vue'
import systemLogVue from '@/views/back/log/SystemLog.vue'

//定义路由关系
const routes = [
    {
        path: '/login', component: shopLoginBaseVue,
        children: [
            { path: '/login', name: 'shopLoginVue', component: shopLoginVue, meta: { title: '欢迎登录' } },
            { path: '/register', name: 'shopRegisterVue', component: shopRegisterVue, meta: { title: '注册' } },
        ]
    },
    {
        path: '/', component: shopBaseVue, redirect: '/index',
        children: [
            { path: '/index', name: 'indexVue', component: indexVue, meta: { title: 'NSHJ小店 - 首页' } },
            { path: '/recommend', name: 'preferenceVue', component: preferenceVue, meta: { title: '个性化推荐' } },
            { path: '/shop', name: 'shopVue', component: shopVue, meta: { title: 'NSHJ小店' } },
            { path: '/shop/goods', name: 'goodsVue', component: goodsVue, meta: { title: '商品详情' } },
            { path: '/cart', name: 'cartVue', component: cartVue, meta: { title: 'NSHJ小店 - 购物车' } },
            { path: '/settlement', name: 'settlementVue', component: settlementVue, meta: { title: '订单结算页 - NSHJ小店' } },
            { path: '/order', name: 'orderVue', component: orderVue, meta: { title: '我的订单' } }
        ]
    },
    {
        path: '/user/login', component: userLoginBaseVue,
        children: [
            { path: '/user/login', name: 'userLoginVue', component: userLoginVue, meta: { title: '欢迎登录' } }
        ]
    },
    {
        path: '/user', component: backBaseVue, redirect: '/user/index',
        children: [
            { path: '/user/index', name: 'userIndexVue', component: userIndexVue, meta: { title: '首页' } }
        ]
    },
    {
        path: '/user/goods', component: backBaseVue, meta: { title: '商品' }, redirect: '/user/goods/manage',
        children: [
            { path: '/user/goods/category', name: 'goodsCategoryVue', component: goodsCategoryVue, meta: { title: '商品分类' } },
            { path: '/user/goods/manage', name: 'goodsManageVue', component: goodsManageVue, meta: { title: '商品管理' } }
        ]
    },
    {
        path: '/user/order', component: backBaseVue, meta: { title: '订单' }, redirect: '/user/order/manage',
        children: [
            { path: '/user/order/manage', name: 'orderManageVue', component: orderManageVue, meta: { title: '订单管理' } },
        ]
    },
    {
        path: '/user/sell', component: backBaseVue, meta: { title: '销售' }, redirect: '/user/sell/statistic',
        children: [
            { path: '/user/sell/statistic', name: 'statisticVue', component: statisticVue, meta: { title: '销售统计' } },
            { path: '/user/sell/prediction', name: 'predictionVue', component: predictionVue, meta: { title: '趋势预测' } }
        ]
    },
    {
        path: '/user/staff', component: backBaseVue, meta: { title: '人员' }, redirect: '/user/staff/manage',
        children: [
            { path: '/user/staff/manage', name: 'staffManageVue', component: staffManageVue, meta: { title: '销售员管理' } },
        ]
    },
    {
        path: '/user/center', component: backBaseVue, meta: { title: '个人中心' }, redirect: '/user/center/info',
        children: [
            { path: '/user/center/info', name: 'userInfoVue', component: userInfoVue, meta: { title: '基本资料' } },
            { path: '/user/center/resetPassword', name: 'userResetPasswordVue', component: userResetPasswordVue, meta: { title: '重置密码' } }
        ]
    },
    {
        path: '/user/log', component: backBaseVue, meta: { title: '日志' }, redirect: '/user/log/system',
        children: [
            { path: '/user/log/customer', name: 'customerLogVue', component: customerLogVue, meta: { title: '顾客日志' } },
            { path: '/user/log/system', name: 'systemLogVue', component: systemLogVue, meta: { title: '系统日志' } }
        ]
    }
];

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: routes
});

//在进入路由前修改标题
router.beforeEach((to, from, next) => {
    document.title = to.meta.title;
    next();  //执行进入路由
});

export default router;
