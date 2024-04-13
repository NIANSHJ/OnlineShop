<script setup>
import { ref, computed, watch } from 'vue'
import customerService from '@/api/customer/customer'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

//注册数据模型
const registerData = ref({
    username: '', code: '', password: ''
});

//校验确认密码的函数
const checkRePassword = (rule, value, callback) => {
    if (value !== registerData.value.password) callback(new Error('两次输入的密码不一致'));
    else callback();
};

//定义表单校验规则
const rules = ref({
    username: [
        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
        { type: 'email', message: '邮箱地址非法', trigger: 'blur' }
    ],
    code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,30}$/, message: '密码长度为8~30位，至少包含大小写字母和数字', trigger: 'blur' }
    ],
    rePassword: [
        { required: true, message: '请再次输入密码', trigger: 'blur' },
        { validator: checkRePassword, trigger: 'blur' }
    ]
});

//倒计时变量
const countDown = ref(0);

//设置按钮文本
const countDownText = computed(() => {
    if (countDown.value === 0) return '获取验证码';
    else return `${countDown.value}s后重新发送`;
});

//读取倒计时状态
const initCountDown = () => {
    let endTime = localStorage.getItem('countDown');
    if (endTime) {
        let remainingTime = Math.floor(Math.max(0, endTime - Date.now()) / 1000);
        countDown.value = remainingTime;
        if (remainingTime > 0) {
            let timer = setInterval(() => {
                if (countDown.value > 0) countDown.value--;
                else clearInterval(timer);
            }, 1000);
        };
    }
};

initCountDown();

//重置倒计时变量
watch(countDown, (value) => {
    if (value === 0) localStorage.removeItem('countDown');
});

//获取验证码
const getCode = async () => {
    let result = await customerService.code(registerData.value.username);
    if (result.code === 0) {
        countDown.value = 60;
        localStorage.setItem('countDown', Date.now() + 60000);
        initCountDown();
        ElMessage.success('验证码已成功发送');
    }
};

const userNamePattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

//检验邮箱地址
const checkUserName = () => {
    return userNamePattern.test(registerData.value.username);
};

//开始倒计时
const startCountDown = () => {
    if (checkUserName()) getCode();
};

//注册
const register = async () => {
    await customerService.register(registerData.value);
    ElMessage.success('注册成功');
    router.push('/login');
};

//表单引用
const formRef = ref(null);

//提交表单前整体校验
const submitForm = () => {
    formRef.value.validate((valid) => {
        if (valid) register();
    });
};
</script>

<template>
    <el-card class='form'>
        <el-form ref='formRef' size='large' autocomplete='off' :model='registerData' :rules='rules'>
            <el-form-item>
                <h1>注册</h1>
            </el-form-item>
            <el-form-item prop='username'>
                <el-input :prefix-icon='User' placeholder='邮箱地址' v-model='registerData.username'></el-input>
            </el-form-item>
            <el-form-item prop='code'>
                <el-input class='code' placeholder='邮箱验证码' v-model='registerData.code'></el-input>
                <el-button class='code_button' :disabled='countDown > 0' @click='startCountDown'>{{
                    countDownText
                }}</el-button>
            </el-form-item>
            <el-form-item prop='password'>
                <el-input :prefix-icon='Lock' type='password' placeholder='密码' v-model='registerData.password'
                    show-password></el-input>
            </el-form-item>
            <el-form-item prop='rePassword'>
                <el-input :prefix-icon='Lock' type='password' placeholder='重复密码'
                    v-model='registerData.rePassword'></el-input>
            </el-form-item>
            <el-form-item>
                <el-button class='form_button' type='primary' auto-insert-space @click="submitForm()">注册</el-button>
            </el-form-item>
            <el-form-item>
                <div class='bottom'>
                    <div class='hasAccount'>已经有账号?</div><el-link :underline='false' href='/login'>立即登录</el-link>
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

        .hasAccount {
            margin-right: 1vw;
        }
    }
}

.el-card {
    width: 25%;
    margin-right: 10vw;
}
</style>