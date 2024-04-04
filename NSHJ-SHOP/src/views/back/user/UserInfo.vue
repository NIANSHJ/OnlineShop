<script setup>
import { ref } from 'vue'
import userService from '@/api/user/user'
import getUserInfoStore from '@/stores/user/userInfo'
import { ElMessage } from 'element-plus'
import { Edit, SuccessFilled } from '@element-plus/icons-vue'

//info状态
const userInfoStore = getUserInfoStore();

//个人信息数据模型
const userInfoData = ref({ ...userInfoStore.info });

//新个人信息数据模型
const newUserInfoData = ref({
    phone: userInfoData.value.phone,
    email: userInfoData.value.email
});

//定义表单校验规则
const rules = ref({
    phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3456789]\d{9}$/, message: '手机号非法', trigger: 'blur' }
    ],
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式非法', trigger: 'blur' }
    ]
});

//编辑个人信息
const uneditable = ref(true);

//修改个人信息
const updateUserInfo = async () => {
    userInfoData.value.phone = newUserInfoData.value.phone;
    userInfoData.value.email = newUserInfoData.value.email;
    await userService.updateUserInfo(userInfoData.value);
    ElMessage.success('修改成功');
    userInfoStore.setInfo(userInfoData.value);
    uneditable.value = true;
};

//表单引用
const formRef = ref(null);

//提交表单前整体校验
const submitForm = () => {
    formRef.value.validate((valid) => {
        if (valid) updateUserInfo();
    });
};
</script>

<template>
    <el-card class='page-container'>
        <el-row>
            <el-col :span='15'>
                <el-form ref='formRef' :model='newUserInfoData' :rules='rules' label-width='100' size='large'>
                    <el-form-item label='用户名'>
                        <el-input v-model='userInfoData.username' disabled></el-input>
                    </el-form-item>
                    <el-form-item label='手机号' prop='phone'>
                        <el-input :disabled='uneditable' v-model='newUserInfoData.phone'></el-input>
                    </el-form-item>
                    <el-form-item label='电子邮箱' prop='email'>
                        <el-input :disabled='uneditable' v-model='newUserInfoData.email'></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button :icon='Edit' @click='uneditable = false'>编辑</el-button>
                        <el-button :icon='SuccessFilled' :disabled='uneditable' type='primary'
                            @click='submitForm'>保存</el-button>
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
</style>@/api/user/user@/stores/user/userInfo