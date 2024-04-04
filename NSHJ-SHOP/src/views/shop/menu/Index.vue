<script setup>
import { ref } from 'vue'
import goodsService from '@/api/customer/goods'

//滚动
const scrolls = ref([
    { url: new URL('@/assets/shop/scroll/1.png', import.meta.url).href },
    { url: new URL('@/assets/shop/scroll/2.png', import.meta.url).href },
    { url: new URL('@/assets/shop/scroll/3.png', import.meta.url).href },
    { url: new URL('@/assets/shop/scroll/4.png', import.meta.url).href }
]);

//推荐商品数据模型
const recommends = ref([]);

//获取推荐商品列表
const recommendList = async () => {
    let result = await goodsService.recommendList();
    recommends.value = result.data;
};

recommendList();

//设置小数点前价格样式
const priceBeforeStyle = (str) => {
    return str.substring(0, str.indexOf('.'));
};

//设置小数点后价格样式
const priceAfterStyle = (str) => {
    return str.substring(str.indexOf('.'));;
};
</script>

<template>
    <div class='page-container'>
        <div class='ad'>
            <div class='category'>
                <ul>
                    <li>手机</li>
                    <li>电视</li>
                    <li>家电</li>
                    <li>笔记本 平板</li>
                    <li>出行 穿戴</li>
                    <li>耳机 音箱</li>
                    <li>健康 儿童</li>
                    <li>生活 箱包</li>
                    <li>智能 路由器</li>
                    <li>电源 配件</li>
                </ul>
            </div>
            <el-carousel :interval='3000' arrow='always' height='70vh'>
                <el-carousel-item v-for='item in scrolls' :key='item'>
                    <img :src='item.url' alt='' class='img' />
                </el-carousel-item>
            </el-carousel>
        </div>

        <div class='list'>
            <div>
                <h3>手机</h3>
                <el-row :gutter='10'>
                    <el-col v-for='item in recommends[0]' :key='item.id' :span='5'>
                        <router-link class='router-link' :to="{ path: '/shop/goods', query: { id: item.id } }">
                            <el-card :body-style="{ padding: '0' }" shadow='hover'>
                                <img :src='item.coverImg' class='image' />
                                <div class='description'>
                                    <el-tooltip placement='top'>
                                        <template #content>
                                            {{ item.name }}
                                        </template>
                                        <div class='title'><span>{{ item.name }}</span></div>
                                    </el-tooltip>
                                    <div class='price'><i>￥ </i><span class='priceBefore'>
                                            {{ priceBeforeStyle(item.price.toFixed(2)) }}</span>
                                        <span class='priceAfter'>
                                            {{ priceAfterStyle(item.price.toFixed(2)) }}</span>
                                    </div>
                                </div>
                            </el-card>
                        </router-link>
                    </el-col>
                </el-row>
            </div>
            <div>
                <h3>电视</h3>
                <el-row :gutter='10'>
                    <el-col v-for='item in recommends[1]' :key='item.id' :span='5'>
                        <router-link class='router-link' :to="{ path: '/shop/goods', query: { id: item.id } }">
                            <el-card :body-style="{ padding: '0' }" shadow='hover'>
                                <img :src='item.coverImg' class='image' />
                                <div class='description'>
                                    <el-tooltip placement='top'>
                                        <template #content>
                                            {{ item.name }}
                                        </template>
                                        <div class='title'><span>{{ item.name }}</span></div>
                                    </el-tooltip>
                                    <div class='price'><i>￥ </i><span class='priceBefore'>
                                            {{ priceBeforeStyle(item.price.toFixed(2)) }}</span>
                                        <span class='priceAfter'>
                                            {{ priceAfterStyle(item.price.toFixed(2)) }}</span>
                                    </div>
                                </div>
                            </el-card>
                        </router-link>
                    </el-col>
                </el-row>
            </div>
            <div>
                <h3>家电</h3>
                <el-row :gutter='10'>
                    <el-col v-for='item in recommends[2]' :key='item.id' :span='5'>
                        <router-link class='router-link' :to="{ path: '/shop/goods', query: { id: item.id } }">
                            <el-card :body-style="{ padding: '0' }" shadow='hover'>
                                <img :src='item.coverImg' class='image' />
                                <div class='description'>
                                    <el-tooltip placement='top'>
                                        <template #content>
                                            {{ item.name }}
                                        </template>
                                        <div class='title'><span>{{ item.name }}</span></div>
                                    </el-tooltip>
                                    <div class='price'><i>￥ </i><span class='priceBefore'>
                                            {{ priceBeforeStyle(item.price.toFixed(2)) }}</span>
                                        <span class='priceAfter'>
                                            {{ priceAfterStyle(item.price.toFixed(2)) }}</span>
                                    </div>
                                </div>
                            </el-card>
                        </router-link>
                    </el-col>
                </el-row>
            </div>
            <div>
                <h3>笔记本 平板</h3>
                <el-row :gutter='10'>
                    <el-col v-for='item in recommends[3]' :key='item.id' :span='5'>
                        <router-link class='router-link' :to="{ path: '/shop/goods', query: { id: item.id } }">
                            <el-card :body-style="{ padding: '0' }" shadow='hover'>
                                <img :src='item.coverImg' class='image' />
                                <div class='description'>
                                    <el-tooltip placement='top'>
                                        <template #content>
                                            {{ item.name }}
                                        </template>
                                        <div class='title'><span>{{ item.name }}</span></div>
                                    </el-tooltip>
                                    <div class='price'><i>￥ </i><span class='priceBefore'>
                                            {{ priceBeforeStyle(item.price.toFixed(2)) }}</span>
                                        <span class='priceAfter'>
                                            {{ priceAfterStyle(item.price.toFixed(2)) }}</span>
                                    </div>
                                </div>
                            </el-card>
                        </router-link>
                    </el-col>
                </el-row>
            </div>
        </div>
    </div>
</template>

<style lang='scss' scoped>
.page-container {

    min-height: 100vh;
    box-sizing: border-box;
    margin: 0 6vw;

    .ad {
        display: flex;
        height: 70vh;

        .category {
            width: 24%;
            justify-content: left;
            background-color: #3F3C3C;

            ul {
                height: 100%;
                list-style-type: none;
                padding: 0;
                margin: 0;

                li {
                    font-size: 14px;
                    color: #FFFFFF;
                    text-indent: 2em;
                    display: flex;
                    align-items: center;
                    height: 10%;
                }

                li:hover {
                    background-color: #FF6700;
                }
            }
        }
    }

    .el-carousel {
        width: 100%;
        justify-content: left;

        .el-carousel__item img {
            object-fit: contain;
            width: 100%;
            height: 100%;
        }

        .el-carousel__item h3 {
            color: #475669;
            opacity: 0.75;
            line-height: 300px;
            margin: 0;
            text-align: center;
        }

        .el-carousel__item:nth-child(2n) {
            background-color: #d3dce6;
        }

        .el-carousel__item:nth-child(2n + 1) {
            background-color: #d3dce6;
        }
    }

    .list {
        margin-top: 5vh;
    }

    h3 {
        font-weight: lighter;
    }

    .el-col {
        max-width: 20%;
    }

    .el-card {
        height: 45vh;
        padding: 0;
        margin: 0 0 2vh;
    }

    .router-link {
        text-decoration: none;

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
                overflow: hidden;
                text-overflow: ellipsis;
            }

            .price {
                margin-top: 1.5vh;

                .priceBefore {
                    font-size: 18px;
                    font-weight: bold;
                    color: #e1251b;
                }

                .priceAfter {
                    font-size: 12px;
                    font-weight: bold;
                    color: #e1251b;
                }
            }
        }
    }

    i {
        font-style: normal;
        font-size: 12px;
        color: #e1251b;
    }
}
</style>
