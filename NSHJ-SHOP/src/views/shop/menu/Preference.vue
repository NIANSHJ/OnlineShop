<script setup>
import { ref } from 'vue'
import preferenceService from '@/api/customer/preference'

//顾客个性化分类
const category = ref('');

//个性化商品列表数据模型
const goodss = ref([]);

//分页数据模型
const paginationData = ref({
    pageNum: 1, pageSize: 20, total: 0
});

//获取商品列表数据
const preferenceList = async () => {
    let result = await preferenceService.preferenceList();
    goodss.value = result.data;
    category.value = result.data2;
};

preferenceList();

//当前页码变化
const onCurrentChange = (num) => {
    paginationData.value.pageNum = num;
    preferenceList();
};
</script>

<template>
    <div class='page-container'>

        <div class='like'>
            <span v-if='category'>您比较喜欢浏览"{{ category }}"分类的商品</span>
            <span v-else>您还没有比较喜欢的分类</span>
        </div>

        <div class='list'>
            <el-row :gutter='10'>
                <el-col v-for='item in goodss' :span='5'>
                    <router-link class='router-link' :to="{ path: '/shop/goods', query: { id: item.id } }">
                        <el-card :body-style="{ padding: '0px' }" shadow='hover'>
                            <img :src='item.coverImg' class='image' />
                            <div class='description'>
                                <el-tooltip placement='top'>
                                    <template #content>
                                        {{ item.name }}
                                    </template>
                                    <span class='title'>{{ item.name }}</span>
                                </el-tooltip>
                                <span class='price'>￥{{ item.price.toFixed(2) }}</span>
                            </div>
                        </el-card>
                    </router-link>
                </el-col>
            </el-row>
        </div>

        <el-pagination v-model:current-page='paginationData.pageNum' v-model:page-size='paginationData.pageSize'
            layout='jumper, total, prev, pager, next' background :total='paginationData.total'
            @current-change='onCurrentChange' style='margin-top: 5vh; justify-content: flex-end' />
    </div>
</template>

<style lang='scss' scoped>
.page-container {

    min-height: 100vh;
    box-sizing: border-box;
    margin: 0 6vw;

    .like {
        margin-top: 3vh;
    }


    .list {
        margin-top: 3vh;

        .router-link {
            text-decoration: none;

            .el-card {
                height: 45vh;
                margin: 0 0 2vh;

                .image {
                    display: block;
                    height: 22vh;
                    width: 22vh;
                    margin: 4vh auto;
                }

                .description {
                    width: 28vh;
                    padding: 2vh;
                    margin: 3.5vh 0 0 1vh;
                    white-space: nowrap;

                    .title {
                        font-size: 15px;
                        color: #666;
                        display: block;
                        overflow: hidden;
                        text-overflow: ellipsis;
                    }

                    .price {
                        font-size: 16px;
                        color: #e1251b;
                        margin-top: 1.5vh;
                        display: block;
                    }
                }
            }
        }
    }

    .el-col {
        max-width: 20%;
    }
}
</style>
