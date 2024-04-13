<script setup>
import { ref } from 'vue'
import userService from '@/api/user/user'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { Lock, SuccessFilled } from '@element-plus/icons-vue'

//密码数据模型
const userPasswordData = ref({
    oldPassword: '', newPassword: '', rePassword: ''
});

//校验重复密码
const checkRePassword = (rule, value, callback) => {
    if (value !== userPasswordData.value.newPassword) {
        callback(new Error('两次输入的新密码不一致'));
    } else callback();
};

//定义表单校验规则
const rules = {
    oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' },],
    newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,30}$/, message: '密码长度为8~30位，至少包含大小写字母和数字', trigger: 'blur' }
    ],
    rePassword: [
        { required: true, message: '请再次输入新密码', trigger: 'blur' },
        { validator: checkRePassword, trigger: 'blur' }
    ]
};

//重置密码
const updatePassword = async () => {
    await userService.resetPassword(userPasswordData.value);
    ElMessage.success('重置密码成功');
    router.push('/user/login');
};

//表单引用
const formRef = ref(null);

//提交表单前整体校验
const submitForm = () => {
    formRef.value.validate((valid) => {
        if (valid) updatePassword();
    });
};
</script>

<template>
    <el-card class='page-container'>
        <el-row>
            <el-col :span='15'>
                <el-form ref='formRef' :model='userPasswordData' :rules='rules' label-width='100' size='large'>
                    <el-form-item label='旧密码' prop='oldPassword'>
                        <el-input :prefix-icon='Lock' type='password' v-model='userPasswordData.oldPassword'
                            show-password></el-input>
                    </el-form-item>
                    <el-form-item label='新密码' prop='newPassword'>
                        <el-input :prefix-icon='Lock' type='password' v-model='userPasswordData.newPassword'
                            show-password></el-input>
                    </el-form-item>
                    <el-form-item label='重复密码' prop='rePassword'>
                        <el-input :prefix-icon='Lock' type='password' v-model='userPasswordData.rePassword'></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type='primary' :icon='SuccessFilled' @click='submitForm'>保存</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang='scss' scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;
    margin: 3vh 2vw;
}
</style>@/api/user/user