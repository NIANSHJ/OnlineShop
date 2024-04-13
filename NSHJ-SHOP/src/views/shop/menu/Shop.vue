<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import goodsService from '@/api/customer/goods'
import categoryService from '@/api/customer/category'
import { ArrowRight, ArrowUp, ArrowDown } from '@element-plus/icons-vue'

//路由
const route = useRoute();

//商品列表数据模型
const goodss = ref([]);

//分类列表数据模型
const categorys = ref([]);

//用户搜索数据模型
const searchData = ref({
    keyword: route.query.keyword, categoryId: 0, type: 0
});

//分页数据模型
const paginationData = ref({
    pageNum: 1, pageSize: 20, total: 0
});

//获取商品列表数据
const goodsList = async () => {
    let params = {
        pageNum: paginationData.value.pageNum, pageSize: paginationData.value.pageSize,
        name: searchData.value.keyword, categoryId: searchData.value.categoryId,
        type: searchData.value.type
    };
    let result = await goodsService.goodsList(params);
    paginationData.value.total = result.data.total;
    goodss.value = result.data.items;
};

goodsList();

//获取分类列表数据
const categoryList = async () => {
    let result = await categoryService.categoryList();
    categorys.value = result.data;
};

categoryList();

//当前页码变化
const onCurrentChange = (num) => {
    paginationData.value.pageNum = num;
    goodsList();
};
</script>

<template>
    <div class='page-container'>
        <div class='bread'>
            <div class='result'>全部结果
                <el-icon>
                    <ArrowRight />
                </el-icon>
            </div>
            <div class='keyword' v-if='searchData.keyword'>"{{ searchData.keyword }}"</div>
        </div>

        <el-card class='search' shadow='never'>
            <el-form>
                <el-form-item label='分类：'>
                    <el-radio :label='0' v-model='searchData.categoryId' @change='goodsList'>全部</el-radio>
                    <el-radio-group v-for='c in categorys' v-model='searchData.categoryId'>
                        <el-radio :label='c.id' @change='goodsList'>{{ c.name }}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label='高级筛选：'>
                    <el-radio :label='0' v-model='searchData.type' @change='goodsList'>综合</el-radio>
                    <el-radio :label='1' v-model='searchData.type' @change='goodsList'>销量
                        <el-icon>
                            <ArrowDown />
                        </el-icon>
                    </el-radio>
                    <el-radio :label='2' v-model='searchData.type' @change='goodsList'>价格
                        <el-icon>
                            <ArrowDown />
                        </el-icon>
                    </el-radio>
                    <el-radio :label='3' v-model='searchData.type' @change='goodsList'>价格
                        <el-icon>
                            <ArrowUp />
                        </el-icon>
                    </el-radio>
                </el-form-item>
            </el-form>
        </el-card>

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

    .bread {
        margin: 2vh 0;
        display: flex;
        align-items: center;

        .result {
            font-size: 12px;
            margin-right: 1vw;
        }

        .keyword {
            color: #666;
            font-size: 15px;
        }
    }

    .el-icon {
        vertical-align: middle;
    }

    .search {
        height: 20vh;

        .el-radio {
            margin-right: 5vw;
        }

        :deep(.el-radio__input) {
            display: none;
        }
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
