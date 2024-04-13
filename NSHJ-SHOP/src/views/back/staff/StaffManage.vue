<script setup>
import { ref } from 'vue'
import staffService from '@/api/user/staff'
import { ElMessage, ElMessageBox } from 'element-plus'
import { CirclePlus, Delete } from '@element-plus/icons-vue'

//分类列表数据模型
const sellers = ref([]);

//获取分类列表
const sellerList = async () => {
    let result = await staffService.sellerList();
    sellers.value = result.data;
};

sellerList();

//销售员数据模型
const sellerData = ref({
    username: '', password: ''
});

//定义表单校验规则
const rules = ref({
    username: [
        { required: true, message: '请输入销售员名称', trigger: 'blur' },
        { pattern: /^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,10}$/, message: '名称长度为1~10位，只能包含汉字、英文字母和数字', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入销售员密码', trigger: 'blur' },
        { pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,30}$/, message: '密码长度为8~30位，至少包含大小写字母和数字', trigger: 'blur' }
    ]
});

//控制弹窗
const dialogVisible = ref(false);

//展示添加弹窗
const showAddDialog = () => {
    dialogVisible.value = true;
    sellerData.value = { username: '', password: '' };
};

//添加销售员
const addSeller = async () => {
    await staffService.addSeller(sellerData.value);
    ElMessage.success('添加成功');
    dialogVisible.value = false;
    sellerList();
};

//表单引用
const formRef = ref(null);

//提交表单前整体校验
const submitForm = (type) => {
    formRef.value.validate((valid) => {
        if (valid) addSeller();
    });
};

//删除销售员
const deleteSeller = (row) => {
    ElMessageBox.confirm(
        '删除销售员',
        '温馨提示',
        { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => {
        await staffService.deleteSeller(row.id);
        ElMessage.success('删除成功');
        sellerList();
    }).catch((error) => { console.log(error) });
};
</script>

<template>
    <div class='page-container'>

        <h3>销售员列表</h3>

        <div class='add'>
            <el-button type='primary' :icon='CirclePlus' @click='showAddDialog'>添加销售员</el-button>
        </div>

        <el-table :data='sellers' border style='width: 65%'>
            <el-table-column label='销售员ID' width='150' prop='id' align='center'></el-table-column>
            <el-table-column label='销售员名称' width='250' prop='username' align='center'></el-table-column>
            <el-table-column label='创建时间' width='200' prop='createTime' align='center'></el-table-column>
            <el-table-column label='操作' align='center'>
                <template #default='{ row }'>
                    <el-button :icon='Delete' type='danger' @click='deleteSeller(row)'>删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model='dialogVisible' title='添加销售员' width='30%'>
            <el-form ref='formRef' :model='sellerData' :rules='rules' label-width='100px' style='padding-right: 30px'>
                <el-form-item label='销售员名称' prop='username'>
                    <el-input v-model='sellerData.username'></el-input>
                </el-form-item>
                <el-form-item label='销售员密码' prop='password'>
                    <el-input v-model='sellerData.password'></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span>
                    <el-button @click='dialogVisible = false'>取消</el-button>
                    <el-button type='primary' @click="submitForm">确认</el-button>
                </span>
            </template>
        </el-dialog>

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
    }
}
</style>@/api/user/order