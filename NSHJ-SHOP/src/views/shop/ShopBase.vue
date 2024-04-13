<script setup>
import { ref } from 'vue'
import customerService from '@/api/customer/customer'
import getTokenStore from '@/stores/customer/token'
import getUserInfoStore from '@/stores/customer/userInfo'
import getCartCountStore from '@/stores/customer/cartCount'
import router from '@/router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, ShoppingCart, ArrowUp } from '@element-plus/icons-vue'

//搜索数据模型
const searchData = ref({
    keyword: ''
});

//校验输入
const checkKeyword = (rule, value, callback) => {
    searchData.value.keyword = value;
    if (value.trim().length > 0) callback();
};

//定义表单校验规则
const rules = ref({
    keyword: [
        { validator: checkKeyword, trigger: 'blur' }
    ]
});

//根据关键字搜索商品
const searchGoods = () => {
    router.push({ path: '/shop', query: { keyword: searchData.value.keyword.trim() } });
};

//表单引用
const formRef = ref(null);

//提交表单前整体校验
const submitForm = () => {
    formRef.value.validate((valid) => {
        if (valid) searchGoods();
    });
};

//token状态
const tokenStore = getTokenStore();

//info状态
const userInfoStore = getUserInfoStore();

//cartCount状态
const cartCountStore = getCartCountStore();

//判断info状态是否为空
const isLogin = ref(Object.keys(userInfoStore.info).length === 0);

//退出登录
const customerLogOut = () => {
    ElMessageBox.confirm(
        '您确认要退出吗?',
        '温馨提示',
        { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => {
        //清空pinia中存储的个人信息和token并跳转到登录页面
        await customerService.logOut();
        userInfoStore.removeInfo();
        tokenStore.removeToken();
        cartCountStore.removeCartCount();
        router.push('/login');
        ElMessage.success('已退出登录');
    }).catch((error) => { console.log(error); });
};
</script>

<template>
    <el-container class='layout-container'>
        <el-menu :default-active='$route.path' class='el-menu-demo' mode='horizontal' :ellipsis='false' router>
            <el-menu-item index='/index'>首页</el-menu-item>
            <el-menu-item index='/recommend'>个性化推荐</el-menu-item>
            <el-menu-item index='/shop'>小店</el-menu-item>

            <div class='flex-grow' />
            <el-menu-item index='/login' v-if='isLogin'>登录</el-menu-item>
            <el-menu-item index='/register' v-if='isLogin'>注册</el-menu-item>
            <el-menu-item index='' v-if='!isLogin'>{{ userInfoStore.info.username }}</el-menu-item>
            <el-menu-item index=''>个人中心</el-menu-item>
            <el-menu-item index='/order'>我的订单</el-menu-item>
            <el-menu-item index='' @click='customerLogOut' v-if='!isLogin'>退出登录</el-menu-item>
        </el-menu>

        <div class='header'>
            <div class='logo'>
                <router-link :to="{ path: '/index' }"><img src='@/assets/back/logo2.png'></router-link>
            </div>
            <div class='search'>
                <el-form ref='formRef' autocomplete='off' :model='searchData' :rules='rules' @submit.prevent>
                    <el-form-item prop='keyword'>
                        <el-input placeholder='请输入商品名称' v-model='searchData.keyword' @keyup.enter.native='submitForm'
                            clearable>
                            <template #append>
                                <el-button :icon='Search' @click='submitForm'></el-button>
                            </template>
                        </el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div class='cart'>
                <router-link :to="{ path: '/cart' }">
                    <el-button :icon='ShoppingCart'>
                        <span class='count'>{{ cartCountStore.cartCount }}</span>
                        <span>我的购物车</span>
                    </el-button>
                </router-link>
            </div>
        </div>

        <div>
            <router-view :key='$route.fullPath'></router-view>
        </div>

        <el-backtop :right='5' :bottom='100'>
            <span>
                <el-icon>
                    <ArrowUp />
                </el-icon>
            </span>
        </el-backtop>

        <el-footer>NSHJ小店 ©2023 Created By NSHJ</el-footer>
    </el-container>
</template>

<style lang='scss' scoped>
.layout-container {

    height: 100%;

    .el-menu {
        height: 6vh;
        background-color: #333333;

        .el-menu-item {
            font-size: 12px;
            color: #B0B0B0;
        }

        .el-menu-item:hover {
            color: #fff;
            background-color: #333333;
        }

        .el-menu-item.is-active {
            color: #fff !important;
            background: #000;
            border: none;
        }

        .flex-grow {
            flex-grow: 1;
        }
    }

    .header {
        display: flex;
        height: 15vh;
        align-items: center;
        background-color: #fff;

        img {
            width: 16vw;
            height: 10vh;
            background: no-repeat left / contain;
        }

        .logo {
            margin-left: 6vw;
        }

        .search {
            margin: 0 8vw 0 8vw;

            .el-form-item {
                margin: 0;
            }

            .el-input {
                width: 40vw;
                height: 6vh;
            }

            .el-button {
                width: 4vw;
                height: 6vh;
            }
        }

        .cart {
            .el-button {
                height: 6vh;

                .count {
                    display: inline-block;
                    text-align: center;
                    font-size: 12px;
                    width: auto;
                    min-width: 16px;
                    color: #fff;
                    margin: -15px 5px 0 -10px;
                    background-color: #FF251B;
                    border-radius: 7px;
                }
            }
        }
    }

    .el-backtop {
        width: 45px;
        height: 45px;
        text-align: center;
        color: #B0B0B0;
        font-size: 20px;
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
        background-color: #EAEAEA;
    }
}
</style>