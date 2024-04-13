<script setup>
</script>

<template>
    <el-container class='layout-container'>

        <div>
            <router-link :to="{ path: '/index' }"><img src='@/assets/back/logo2.png'></router-link>
        </div>

        <div class='page-container'>
            <router-view></router-view>
        </div>

        <el-footer>NSHJ小店 ©2023 Created by NSHJ</el-footer>
    </el-container>
</template>

<style lang='scss' scoped>
.layout-container {

    height: 120vh;

    img {
        width: 16vw;
        height: 10vh;
        background: no-repeat left / contain;
    }

    .page-container {
        height: 100vh;
        box-sizing: border-box;
        background: url('@/assets/shop/login/bg.png') no-repeat center / cover;
        display: flex;
        align-items: center;
        justify-content: right;
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
    }
}
</style>