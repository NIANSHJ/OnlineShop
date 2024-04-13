<script setup>
import { ref } from 'vue'
import userService from '@/api/user/user'
import getTokenStore from '@/stores/user/token'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

//登录数据模型
const loginData = ref({
    username: '', password: ''
});

//token状态
const tokenStore = getTokenStore();

//登录
const login = async () => {
    let result = await userService.login(loginData.value);
    ElMessage.success('登录成功');
    tokenStore.setToken(result.data);
    router.push('/user/goods/manage');
};

//提交表单
const submitForm = () => {
    login();
};
</script>

<template>
    <el-card>
        <el-form size='large' autocomplete='off' :model='loginData'>
            <el-form-item>
                <h1>后台管理系统</h1>
            </el-form-item>
            <el-form-item>
                <el-input :prefix-icon='User' placeholder='请输入用户名' v-model='loginData.username'></el-input>
            </el-form-item>
            <el-form-item>
                <el-input :prefix-icon='Lock' type='password' placeholder='请输入密码' v-model='loginData.password'
                    show-password></el-input>
            </el-form-item>
            <el-form-item>
                <el-button class='form_button' type='primary' auto-insert-space @click="submitForm()">登录</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<style lang='scss' scoped>
h1 {
    width: 100%;
    text-align: center;
}

.form_button {
    width: 100%;
    margin: 0 auto;
}
</style>
