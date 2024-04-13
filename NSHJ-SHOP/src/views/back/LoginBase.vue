<script setup>
</script>

<template>
    <el-row class='login-page'>
        <el-col :span='12' class='bg'></el-col>
        <el-col :span='6' :offset='3' class='form'>
            <router-view></router-view>
        </el-col>
    </el-row>
</template>

<style lang='scss' scoped>
.login-page {

    height: 100vh;
    background-color: #fff;

    .bg {
        background: url('@/assets/back/logo2.png') no-repeat 50% center / 60vh auto,
            url('@/assets/back/login_bg.jpg') no-repeat center / cover;
        border-radius: 0 20px 20px 0;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;
    }
}
</style>
