<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { ArrowRight } from '@element-plus/icons-vue'

//路由
const route = useRoute();

//面包屑路由列表
const breadList = computed(() => {
    return route.matched;
});
</script>

<template>
    <div class='bread-wrap'>
        <el-breadcrumb :separator-icon='ArrowRight'>
            <el-breadcrumb-item v-for='item in breadList'>
                <span>{{ item.meta.title }}</span>
            </el-breadcrumb-item>
        </el-breadcrumb>
    </div>
</template>

<style lang='scss' scoped>
.bread-wrap {

    display: flex;
    justify-content: space-between;

    span {
        font-size: 15px;
    }
}
</style>