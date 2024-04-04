<script setup>
import { ref } from 'vue'
import orderService from '@/api/user/order'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Filter, Document, Message, Delete } from '@element-plus/icons-vue'

//订单列表数据模型
const orders = ref([]);

//正则表达式判断表单输入
const regExp = /^\s*$/;

//搜索数据模型
const searchData = ref({
    id: null, userId: null, username: null,
    createTime: null, state: null
});

//分页数据模型
const paginationData = ref({
    pageNum: 1, pageSize: 10, total: 0
});

//获取订单列表数据
const orderList = async () => {
    let params = {
        pageNum: paginationData.value.pageNum, pageSize: paginationData.value.pageSize,
        id: regExp.test(searchData.value.id) ? null : searchData.value.id,
        userId: regExp.test(searchData.value.userId) ? null : searchData.value.userId,
        username: regExp.test(searchData.value.username) ? null : searchData.value.username,
        createTime: searchData.value.createTime, state: searchData.value.state
    };
    let result = await orderService.orderList(params);
    paginationData.value.total = result.data.total;
    orders.value = result.data.items;
};

orderList();

//清空搜索数据
const clearSearchData = () => {
    searchData.value.id = null;
    searchData.value.orderId = null;
    searchData.value.username = null;
    searchData.value.createTime = null;
    searchData.value.state = null;
    orderList();
};

//每页条数变化
const onSizeChange = (size) => {
    paginationData.value.pageSize = size;
    orderList();
};

//当前页码变化
const onCurrentChange = (num) => {
    paginationData.value.pageNum = num;
    orderList();
};

//订单数据模型
const orderData = ref([]);

//控制抽屉是否显示
const drawerVisible = ref(false);

//展示详情抽屉
const showDetailDrawer = async (row) => {
    drawerVisible.value = true;
    let result = await orderService.orderDetail(row.id);
    orderData.value = result.data;
};

//邮件内容数据模型
const emailData = ref({
    username: '', orderId: ''
});

//发送订单确认邮件
const confirmOrder = async (row) => {
    emailData.value.username = row.username;
    emailData.value.orderId = row.id;
    await orderService.orderEmail(emailData.value);
    ElMessage.success('邮件发送成功');
    orderList();
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
    <div class='page-container'>

        <h3>筛选搜索</h3>

        <el-card shadow='never'>
            <el-form label-width="10vw" :model='searchData'>
                <el-row>
                    <el-col :span='8'>
                        <el-form-item label='订单ID：'>
                            <el-input style='width:100%' placeholder='请输入订单ID' v-model='searchData.id'>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span='8'>
                        <el-form-item label='用户ID：'>
                            <el-input style='width:100%' placeholder='请输入用户ID' v-model='searchData.userId'>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span='8'>
                        <el-form-item label='用户名：'>
                            <el-input style='width:100%' placeholder='请输入用户名' v-model='searchData.username'>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span='8'>
                        <el-form-item label='下单时间：'>
                            <el-date-picker style='width:100%' placeholder='请选择下单时间' v-model='searchData.createTime'
                                type='date' value-format='YYYY-MM-DD' />
                        </el-form-item>
                    </el-col>
                    <el-col :span='8'>
                        <el-form-item label='状态：'>
                            <el-select style='width:100%' placeholder='请选择状态' v-model='searchData.state' clearable>
                                <el-option label='未发货' value='false'>未发货</el-option>
                                <el-option label='已发货' value='true'>已发货</el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div class='search'>
                <el-button type='primary' :icon='Search' @click='orderList'>搜索</el-button>
                <el-button :icon='Filter' @click='clearSearchData'>重置</el-button>
            </div>
        </el-card>

        <h3>订单列表</h3>

        <el-table :data='orders' border style='width: 100%'>
            <el-table-column type='expand'>
                <template #default='{ row }'>
                    <div>
                        <p>用户ID: {{ row.userId }}</p>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label='订单ID' width='190' prop='id' align='center'></el-table-column>
            <el-table-column label='用户名' prop='username' align='center'> </el-table-column>
            <el-table-column label='订单金额' width='130' prop='total' align='center'>
                <template #default='{ row }'>
                    ￥{{ row.total.toFixed(2) }}
                </template>
            </el-table-column>
            <el-table-column label='下单时间' width='180' prop='createTime' align='center'></el-table-column>
            <el-table-column label='订单状态' width='90' prop='state' align='center'>
                <template #default='{ row }'>
                    {{ row.state === false ? '未发货' : '已发货' }}
                </template>
            </el-table-column>
            <el-table-column label='操作' align='center'>
                <template #default='{ row }'>
                    <el-button :icon='Document' @click='showDetailDrawer(row)'>详情</el-button>
                    <el-button v-if='!row.state' :icon='Message' type='primary'
                        @click='confirmOrder(row)'>发货</el-button>
                    <el-button :icon='Delete' type='danger' @click='deleteOrder(row)'>删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination v-model:current-page='paginationData.pageNum' v-model:page-size='paginationData.pageSize'
            :page-sizes='[10, 20, 50, 100]' layout='jumper, total, sizes, prev, pager, next' background
            :total='paginationData.total' @size-change='onSizeChange' @current-change='onCurrentChange'
            style='margin: 5vh 0 5vh; justify-content: flex-end' />

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
    </div>
</template>

<style lang='scss' scoped>
.page-container {

    min-height: 100%;
    box-sizing: border-box;
    padding: 0 2vw;

    .search {
        display: flex;
        align-items: center;
        justify-content: right;
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

    .el-table .el-button {
        width: 5vw;
    }
}
</style>@/api/user/order
