<script setup>
import { ref } from 'vue'
import logService from '@/api/user/log'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'

//日志列表数据模型
const logs = ref([]);

//分页数据模型
const paginationData = ref({
    pageNum: 1, pageSize: 10, total: 0
});

//获取日志列表
const logList = async () => {
    let params = {
        pageNum: paginationData.value.pageNum, pageSize: paginationData.value.pageSize
    };
    let result = await logService.customerLog(params);
    paginationData.value.total = result.data.total;
    logs.value = result.data.items;
};

logList();

//每页条数变化
const onSizeChange = (size) => {
    paginationData.value.pageSize = size;
    goodsList();
};

//当前页码变化
const onCurrentChange = (num) => {
    paginationData.value.pageNum = num;
    goodsList();
};

//删除日志
const deleteLog = (row) => {
    ElMessageBox.confirm(
        '删除日志',
        '温馨提示',
        { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => {
        await logService.deleteCustomerLog(row.id);
        ElMessage.success('删除成功');
        logList();
    }).catch((error) => { console.log(error) });
};
</script>

<template>
    <div class='page-container'>

        <h3>日志列表</h3>

        <el-table :data='logs' border style='width: 100%'>
            <el-table-column type='expand'>
                <template #default='{ row }'>
                    <div>
                        <p>方法名称: {{ row.method }}</p>
                        <p>参数列表: {{ row.params }}</p>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label='日志ID' width='230' prop='id' sortable align='center'> </el-table-column>
            <el-table-column label='IP' width='170' prop='ip' align='center'></el-table-column>
            <el-table-column label='用户名' width='210' prop='username' align='center'></el-table-column>
            <el-table-column label='模块' width='100' prop='module' align='center'></el-table-column>
            <el-table-column label='操作' width='120' prop='operation' align='center'></el-table-column>
            <el-table-column label='记录时间' width='200' prop='createTime' sortable align='center'></el-table-column>
            <el-table-column label='状态码' width='70' prop='status' align='center'></el-table-column>
            <el-table-column width='120' align='center'>
                <template #default='{ row }'>
                    <el-button :icon='Delete' type='danger' @click='deleteLog(row)'>删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination v-model:current-page='paginationData.pageNum' v-model:page-size='paginationData.pageSize'
            :page-sizes='[10, 20, 50, 100]' layout='jumper, total, sizes, prev, pager, next' background
            :total='paginationData.total' @size-change='onSizeChange' @current-change='onCurrentChange'
            style='margin: 5vh 0 5vh; justify-content: flex-end' />

    </div>
</template>

<style lang='scss' scoped>
.page-container {

    min-height: 100%;
    box-sizing: border-box;
    padding: 0 2vw;

    .add {
        display: flex;
        align-items: center;
        justify-content: right;
    }

    .el-table {
        margin-top: 3vh;

        .el-button {
            width: 5vw;
        }
    }
}
</style>