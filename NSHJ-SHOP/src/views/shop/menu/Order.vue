<script setup>
import { ref } from 'vue'
import orderService from '@/api/customer/order'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document, Delete } from '@element-plus/icons-vue'

//订单列表数据模型
const orders = ref([]);

//控制抽屉是否显示
const drawerVisible = ref(false);

//分页数据模型
const paginationData = ref({
    pageNum: 1, pageSize: 20, total: 0
});

//获取订单列表
const orderList = async () => {
    let params = {
        pageNum: paginationData.value.pageNum, pageSize: paginationData.value.pageSize
    };
    let result = await orderService.orderList(params);
    paginationData.value.total = result.data.total;
    orders.value = result.data.items;
};

orderList();

//当前页码变化
const onCurrentChange = (num) => {
    paginationData.value.pageNum = num;
    orderList();
};

//订单数据模型
const orderData = ref([]);

//展示详情抽屉
const showDetailDrawer = async (row) => {
    drawerVisible.value = true;
    let result = await orderService.orderDetail(row.id);
    orderData.value = result.data;
};

//删除订单
const deleteOrder = async (row) => {
    ElMessageBox.confirm(
        '删除订单',
        '温馨提示',
        { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => {
        await orderService.deleteOrder(row.id);
        ElMessage.success('删除成功');
        orderList();
    }).catch((error) => { console.log(error) });
};
</script>

<template>
    <el-card class='page-container'>
        <el-table :data='orders' style='width: 100%'>
            <el-table-column label='订单ID' width='250' prop='id' align='center'></el-table-column>
            <el-table-column label='收货人' width='300' prop='username' align='center'> </el-table-column>
            <el-table-column label='订单金额' width='150' prop='total' align='center'>
                <template #default='{ row }'>
                    ￥{{ row.total.toFixed(2) }}
                </template>
            </el-table-column>
            <el-table-column label='下单时间' width='250' prop='createTime' align='center'></el-table-column>
            <el-table-column label='订单状态' prop='state' align='center'>
                <template #default='{ row }'>
                    {{ row.state === false ? '未发货' : '已发货' }}
                </template>
            </el-table-column>
            <el-table-column label='操作' align='center'>
                <template #default='{ row }'>
                    <el-button :icon='Document' circle plain @click='showDetailDrawer(row)'></el-button>
                    <el-button v-if='row.state' :icon='Delete' circle plain type='danger'
                        @click='deleteOrder(row)'></el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination v-model:current-page='paginationData.pageNum' v-model:page-size='paginationData.pageSize'
            layout='jumper, total, prev, pager, next' background :total='paginationData.total'
            @current-change='onCurrentChange' style='margin-top: 5vh; justify-content: flex-end' />

        <el-drawer v-model='drawerVisible' title='订单详情' direction='rtl' size='50%'>
            <el-table :data='orderData' style='width: 100%'>
                <el-table-column label='商品ID' prop='goodsId' align='center'></el-table-column>
                <el-table-column label='商品' width='250' :show-overflow-tooltip='true' align='center'>
                    <template #default='{ row }'>
                        <el-image :src='row.coverImg'></el-image>
                        <div class='name'>{{ row.goodsName }}</div>
                    </template>
                </el-table-column>
                <el-table-column label='商品单价' prop='price' align='center'>
                    <template #default='{ row }'>
                        {{ row.price.toFixed(2) }}
                    </template>
                </el-table-column>
                <el-table-column label='数量' width='100' prop='count' align='center'>
                    <template #default='{ row }'>
                        x{{ row.count }}
                    </template>
                </el-table-column>
            </el-table>
        </el-drawer>
    </el-card>
</template>

<style lang='scss' scoped>
.page-container {

    min-height: 100vh;
    box-sizing: border-box;
    padding: 0 6vw;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    .el-image {
        float: left;
        width: 3vw;
        height: 3vw;
    }

    .name {
        display: flex;
        height: 3vw;
        align-items: center;
    }
}
</style>