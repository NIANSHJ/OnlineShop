<script setup>
import { ref } from 'vue'
import orderService from '@/api/customer/order'
import getUserInfoStore from '@/stores/customer/userInfo'
import getCartCountStore from '@/stores/customer/cartCount'
import router from '@/router'
import { ElMessage, ElMessageBox } from 'element-plus'

//默认选中的支付方式
const radio1 = ref('在线支付');

//默认选中的配送方式
const radio2 = ref('快递运输');

//info状态
const userInfoStore = getUserInfoStore();

//购物车数据模型
const cartData = ref(JSON.parse(history.state.params.items));

//订单数据模型
const orderData = ref({
    username: userInfoStore.info.username,
    total: history.state.params.total,
    ids: JSON.parse(history.state.params.ids)
});

//OSS地址
const url = 'http://cdn.mynshj.top/shop/';

//cartCount状态
const cartCountStore = getCartCountStore();

//提交订单
const submitOrder = async () => {
    ElMessageBox.confirm(
        '确认提交订单',
        '温馨提示',
        { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => {
        await orderService.addOrder(orderData.value);
        cartCountStore.cartCount -= cartData.value.length;
        ElMessage.success('提交订单成功');
        router.push('/cart');
    }).catch((error) => { console.log(error); });
};
</script>

<template>
    <el-card class='page-container'>
        <div class='pay'>
            <h3>支付方式</h3>
            <div class='payWay'>
                <el-radio-group v-model='radio1'>
                    <el-radio-button label='货到付款' value='1' />
                    <el-radio-button label='在线支付' value='2' />
                </el-radio-group>
            </div>
        </div>
        <div class='hr'></div>
        <h3>送货清单</h3>
        <div class='list'>
            <div class='send'>
                <h4>配送方式</h4>
                <div class='sendWay'>
                    <el-radio-group v-model='radio2'>
                        <el-radio-button label='快递运输' value='1' />
                    </el-radio-group>
                </div>
            </div>
            <div class='cart'>
                <h4>商品列表</h4>
                <el-table :data='cartData' :show-header='false'>
                    <el-table-column width='600' align='center' :show-overflow-tooltip='true'>
                        <template #default='{ row }'>
                            <router-link class='router-link' :to="{ path: '/shop/goods', query: { id: row.goodsId } }">
                                <el-image :src='url + row.goodsId + row.coverImg'></el-image>
                                <div class='name'>{{ row.goodsName }}</div>
                            </router-link>
                        </template>
                    </el-table-column>
                    <el-table-column width='200' prop='price' align='center'>
                        <template #default='{ row }'>
                            <span class='price'>￥{{ row.price.toFixed(2) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop='count' align='center'>
                        <template #default='{ row }'>
                            x{{ row.count }}
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <div class='total'>
            应付总额：<span class='totalPrice'>￥{{ orderData.total }}</span>
        </div>
        <div class='order'>
            <el-button @click='submitOrder'>提交订单</el-button>
        </div>
    </el-card>
</template>

<style lang='scss' scoped>
.page-container {
    min-height: 100vh;
    box-sizing: border-box;

    h3 {
        font-size: 14px;
        margin: 0 0;
    }

    h4 {
        font-size: 13px;
        margin: 0 0;
        color: #666;
    }

    .pay {
        height: 15vh;

        .payWay {
            margin-top: 2vh;
        }
    }

    .hr {
        border-bottom: 1px solid #e6e6e6;
        height: 0;
        margin-bottom: 2vh;
    }

    .list {
        display: flex;
        justify-content: space-between;

        .send {
            width: 25vw;
            padding: 1vw;
            margin-top: 1vh;
            background-color: #F7F7F7;

            .sendWay {
                margin-top: 1vh;
            }
        }

        .cart {
            width: 75vw;
            padding: 1vw;
            margin-top: 1vh;
            background-color: #F3FBFE;

            .router-link {
                color: #606266;
                text-decoration: none;

                .name {
                    display: flex;
                    height: 5vw;
                    align-items: center;
                }
            }

            .price {
                font-weight: bold;
                color: #e4393c;
            }
        }
    }

    .el-image {
        float: left;
        width: 5vw;
        height: 5vw;
    }

    .total {
        margin-top: 1vh;
        display: flex;
        height: 10vh;
        align-items: center;
        justify-content: right;
        padding-right: 2vw;
        background-color: #F4F4F4;
        color: #666;

        .totalPrice {
            font-size: 20px;
            font-weight: bold;
            color: #e4393c;
        }
    }

    .order {
        display: flex;
        margin-top: 1vh;
        justify-content: right;
    }

    .el-button {
        width: 9vw;
        height: 6vh;
        background-color: #e00;
        font-size: 18px;
        font-weight: bold;
        color: #fff;
    }
}
</style>
