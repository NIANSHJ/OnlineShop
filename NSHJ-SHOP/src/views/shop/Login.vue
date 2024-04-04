<script setup>
import { ref } from 'vue'
import customerService from '@/api/customer/customer'
import cartService from '@/api/customer/cart'
import getTokenStore from '@/stores/customer/token'
import getUserInfoStore from '@/stores/customer/userInfo'
import getCartCountStore from '@/stores/customer/cartCount'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

//登录数据模型
const loginData = ref({
    username: '', password: ''
});

//定义表单校验规则
const rules = ref({
    username: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '邮箱地址非法', trigger: 'blur' }
    ]
});

//token状态
const tokenStore = getTokenStore();

//info状态
const userInfoStore = getUserInfoStore();

//cartCount状态
const cartCountStore = getCartCountStore();

//获取购物车商品总数
const getCartCount = async () => {
    let result = await cartService.cartCount();
    cartCountStore.setCartCount(result.data);
};

//登录
const login = async () => {
    let result = await customerService.login(loginData.value);
    ElMessage.success('登录成功');
    tokenStore.setToken(result.data);
    result = await customerService.customerInfo();
    userInfoStore.setInfo(result.data);
    getCartCount();
    router.push('/index');
};

//表单引用
const formRef = ref(null);

//提交表单前整体校验
const submitForm = () => {
    formRef.value.validate((valid) => {
        if (valid) login();
    });
};
</script>

<template>
    <el-card class='form'>
        <el-form ref='formRef' size='large' autocomplete='off' :model='loginData' :rules='rules'>
            <el-form-item>
                <h1>登录</h1>
            </el-form-item>
            <el-form-item prop='username'>
                <el-input :prefix-icon='User' placeholder='邮箱地址' v-model='loginData.username'></el-input>
            </el-form-item>
            <el-form-item>
                <el-input :prefix-icon='Lock' type='password' placeholder='密码' v-model='loginData.password'
                    show-password></el-input>
            </el-form-item>
            <el-form-item>
                <div class='remember'>
                    <el-checkbox>记住我</el-checkbox>
                    <el-link type='primary' :underline='false'>忘记密码?</el-link>
                </div>
            </el-form-item>
            <el-form-item>
                <el-button class='form_button' type='primary' auto-insert-space @click="submitForm()">登录</el-button>
            </el-form-item>
            <el-form-item>
                <div class='bottom'>
                    <div class='noAccount'>还没有账号?</div><el-link :underline='false' href='/register'>立即注册</el-link>
                </div>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<style lang='scss' scoped>
.form {
    display: flex;
    flex-direction: column;
    justify-content: center;
    user-select: none;

    .title {
        margin: 0 auto;
    }

    .code {
        width: 70%;
    }

    .code_button {
        width: 30%;
    }

    .form_button {
        width: 100%;
        margin: 0 auto;
    }

    .remember {
        width: 100%;
        display: flex;
        justify-content: space-between;
    }

    .bottom {
        width: 100%;
        display: flex;
        justify-content: center;

        .noAccount {
            margin-right: 1vw;
        }
    }
}

.el-card {
    width: 25%;
    margin-right: 10vw;
}
</style>