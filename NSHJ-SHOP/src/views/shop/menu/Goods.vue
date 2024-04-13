<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import goodsService from '@/api/customer/goods'
import cartService from '@/api/customer/cart'
import categoryService from '@/api/customer/category'
import getCartCountStore from '@/stores/customer/cartCount'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ShoppingCart } from '@element-plus/icons-vue'

//路由
const route = useRoute();

//商品ID
const goodsId = ref(route.query.id);

//商品数据模型
const goods = ref({});

//购物车项数据模型
const cartItemData = ref({});

//个性化数据模型
const preferenceData = ref({});

//购买数量
const count = ref(1);

//获取商品信息
const goodsDetails = async () => {
    let result = await goodsService.goodsDetail(goodsId.value);
    goods.value = result.data;
    cartItemData.value.goodsId = goods.value.id;
    cartItemData.value.goodsName = document.title = goods.value.name;
    let dotPos = goods.value.coverImg.lastIndexOf('.');
    cartItemData.value.coverImg = goods.value.coverImg.substring(dotPos);
    cartItemData.value.price = goods.value.price;
    cartItemData.value.count = count.value;
    preferenceData.value._id = goods.value.categoryId;
};

goodsDetails();

//更新购物车项数目
const updateCount = () => {
    cartItemData.value.count = count.value;
};

//cartCount状态
const cartCountStore = getCartCountStore();

//加入购物车
const addCart = async () => {
    let result = await cartService.addCartItem(cartItemData.value);
    if (result.data === 1) ++cartCountStore.cartCount;
    ElMessage.success('商品已成功加入购物车')
};

//起始时间
const startTimer = ref(0);

//页面加载后
onMounted(() => {
    startTimer.value = Date.now();
});

//页面卸载前
onUnmounted(() => {
    preferenceData.value.duration = Math.floor((Date.now() - startTimer.value) / 1000);
    categoryService.categoryDuration(preferenceData.value);
});
</script>
<template>
    <el-card class='page-container'>

        <div class='goods'>
            <div class='cover'>
                <img :src='goods.coverImg' class='image'>
            </div>
            <div class='description'>
                <div class='title'>
                    <h4>{{ goods.name }}</h4>
                </div>
                <div class='price'>
                    <span class='label'>价格&nbsp&nbsp</span>
                    <span class='symbol'>￥</span>
                    <span class='cost'>{{ parseFloat(goods.price).toFixed(2) }}</span>
                </div>
                <div class='count'>
                    <span class='label'>数量&nbsp&nbsp</span>
                    <el-input-number size='small' v-model='count' :min='1' :max='10' />
                </div>
                <div class='cart'>
                    <el-button type='primary' :icon='ShoppingCart' @click='updateCount(); addCart()'>加入购物车</el-button>
                </div>
                <div class='notice'>温馨提示&nbsp&nbsp·支持7天无理由退货</div>
                <div class='soldOut' v-if='goods.stock === 0'>已售罄</div>
            </div>
        </div>

        <div class='detail'>
            <el-tabs type='border-card'>
                <el-tab-pane label='商品介绍' v-html='goods.content'></el-tab-pane>
                <el-tab-pane label='商品评价'></el-tab-pane>
            </el-tabs>
        </div>
    </el-card>
</template>

<style lang='scss' scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;
    padding: 0 5vw;

    .goods {
        display: flex;
        justify-content: left;

        .cover {
            margin-left: 5vw;

            .image {
                width: 30vw;
                border: 1px solid #d3dce6;
            }
        }

        .description {
            margin-left: 5vw;

            .title {
                color: #666;
                width: 50vw;
                word-wrap: break-word;
            }

            .price {
                margin-top: 5vh;

                .symbol {
                    font-size: 15px;
                    color: #e1251b;
                }

                .cost {
                    font-size: 20px;
                    color: #e1251b;
                }
            }

            .label {
                font-size: 12px;
                color: #999;
            }

            .count {
                margin-top: 5vh;
            }

            .cart {
                margin-top: 5vh;
            }

            .notice {
                margin-top: 5vh;
                font-size: 12px;
                color: #999;
            }

            .soldOut {
                margin-top: 5vh;
                font-size: 20px;
                color: #e1251b;
            }
        }
    }

    .el-button {
        width: 10vw;
        height: 6vh;
    }

    .detail {
        margin: 10vh 0 3vh 0;
    }
}
</style>