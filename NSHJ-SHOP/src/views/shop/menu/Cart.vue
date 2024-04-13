<script setup>
import { ref } from 'vue'
import cartService from '@/api/customer/cart'
import getCartCountStore from '@/stores/customer/cartCount'
import router from '@/router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Wallet } from '@element-plus/icons-vue'

//购物车数据模型
const cartData = ref([]);

//OSS地址
const url = 'http://cdn.mynshj.top/shop/';

//获取购物车列表
const cartList = async () => {
    let result = await cartService.cartList();
    cartData.value = result.data;
};

cartList();

//购物车项数据模型
const cartItemData = ref({});

//选中的购物车项数据模型
const cartItems = ref({
    cartItemList: [], cartItemIds: []
});

//合计数据模型
const total = ref({
    count: 0, price: 0
})

//表格合计
const getSummaries = () => {
    total.value.count = 0;
    total.value.price = 0;
    for (let i = 0; i < cartItems.value.cartItemList.length; i++) {
        total.value.count += cartItems.value.cartItemList[i].count;
        total.value.price += cartItems.value.cartItemList[i].price * cartItems.value.cartItemList[i].count;
    }
};

//修改商品的数量
const updateCartItem = async (row) => {
    cartItemData.value.goodsId = row.goodsId;
    cartItemData.value.count = row.count;
    await cartService.updateCartItem(cartItemData.value);
    getSummaries();
};

//删除单个商品
const deleteCartItem = (row) => {
    ElMessageBox.confirm(
        '删除商品',
        '温馨提示',
        { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => {
        await cartService.deleteCartItem([row.goodsId]);
        --cartCountStore.cartCount;
        ElMessage.success('删除成功');
        cartList();
    }).catch((error) => { console.log(error) });
};

//表单引用
const tableRef = ref(null);

//获取被选中的商品对应的行及对应的商品ID
const handleSelectChange = (list) => {
    cartItems.value.cartItemList = list;
    cartItems.value.cartItemIds.length = 0;
    for (let i = 0; i < cartItems.value.cartItemList.length; i++)
        cartItems.value.cartItemIds.push(cartItems.value.cartItemList[i].goodsId);
    getSummaries();
};

//全选商品
const selectAll = () => {
    tableRef.value.toggleAllSelection();
};

//cartCount状态
const cartCountStore = getCartCountStore();

//删除被选中的商品
const deleteAllCartItem = () => {
    if (cartItems.value.cartItemList.length === 0) ElMessage.info("请至少选中一件商品");
    else {
        ElMessageBox.confirm(
            '删除选中商品',
            '温馨提示',
            { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
        ).then(async () => {
            await cartService.deleteCartItem(cartItems.value.cartItemIds);
            cartCountStore.cartCount -= cartItems.value.cartItemList.length;
            ElMessage.success('删除成功');
            cartList();
        }).catch((error) => { console.log(error) });
    }
};

//结算
const getSettlement = async () => {
    if (cartItems.value.cartItemList.length === 0) ElMessage.info("请至少选中一件商品");
    else {
        router.push({
            path: '/settlement', state: {
                params: {
                    items: JSON.stringify(cartItems.value.cartItemList),
                    ids: JSON.stringify(cartItems.value.cartItemIds),
                    total: total.value.price.toFixed(2)
                }
            }
        });
    }
};
</script>

<template>
    <el-card class='page-container'>
        <el-table ref='tableRef' :data='cartData' style='width: 100%' row-key='goodsId'
            @selection-change='handleSelectChange'>
            <el-table-column type='selection' width='100' align='center'></el-table-column>
            <el-table-column label='商品' width='500' align='center' :show-overflow-tooltip='true'>
                <template #default='{ row }'>
                    <router-link class='router-link' :to="{ path: '/shop/goods', query: { id: row.goodsId } }">
                        <el-image :src='url + row.goodsId + row.coverImg'></el-image>
                        <div class='name'>{{ row.goodsName }}</div>
                    </router-link>
                </template>
            </el-table-column>
            <el-table-column label='单价' width='150' prop='price' align='center'>
                <template #default='{ row }'>
                    ￥{{ row.price.toFixed(2) }}
                </template>
            </el-table-column>
            <el-table-column label='数量' width='150' prop='count' align='center'>
                <template #default='{ row }'>
                    <el-input-number size='small' v-model='row.count' :min='1' :max='10' @change='updateCartItem(row)' />
                </template>
            </el-table-column>
            <el-table-column label='小计' align='center'>
                <template #default='{ row }'>
                    ￥{{ (row.price * row.count).toFixed(2) }}
                </template>
            </el-table-column>
            <el-table-column label='操作' width='150' align='center'>
                <template #default='{ row }'>
                    <el-button :icon='Delete' circle plain type='danger' @click='deleteCartItem(row)'></el-button>
                </template>
            </el-table-column>
            <template #append>
                <div class='bottom' v-if='cartData.length'>
                    <div class='left'>
                        <el-link @click='selectAll' :underline='false'>全选</el-link>&nbsp;&nbsp;
                        <el-link @click='deleteAllCartItem' :underline='false'>删除选中的商品</el-link>
                    </div>
                    <div>
                        已选择&nbsp;<span>{{ total.count }}</span>&nbsp;件商品
                        &nbsp;&nbsp;总价：<span>￥{{ total.price.toFixed(2) }}</span>
                        <el-button :icon='Wallet' @click='getSettlement'>去结算</el-button>
                    </div>
                </div>
            </template>
        </el-table>
    </el-card>
</template>

<style lang='scss' scoped>
.page-container {

    min-height: 100vh;
    box-sizing: border-box;
    padding: 0 6vw;

    .router-link {
        color: #606266;
        text-decoration: none;

        .name {
            display: flex;
            height: 5vw;
            align-items: center;
        }
    }

    .el-image {
        float: left;
        width: 5vw;
        height: 5vw;
    }

    .bottom {
        display: flex;
        width: 100%;
        height: 8vh;
        align-items: center;
        justify-content: space-between;

        .left {
            margin-left: 3vw;
        }

        .el-button {
            background-color: #e54346;
            font-size: 18px;
            font-weight: bold;
            color: #fff;
            margin-left: 2vw;
            font-size: 18px;
            width: 8vw;
            height: 8vh;
        }
    }

    span {
        font-size: 16px;
        font-weight: bold;
        color: #e2231a;
    }
}
</style>