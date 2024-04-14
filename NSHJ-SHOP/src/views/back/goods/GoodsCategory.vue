<script setup>
import { ref } from 'vue'
import categoryService from '@/api/user/category'
import { ElMessage, ElMessageBox } from 'element-plus'
import { CirclePlus, Edit, Delete } from '@element-plus/icons-vue'

//分类列表数据模型
const categorys = ref([]);

//获取分类列表
const categoryList = async () => {
    let result = await categoryService.categoryList();
    categorys.value = result.data;
};

categoryList();

//控制弹窗
const dialogVisible = ref({
    add: false, update: false
});

//添加分类数据模型
const categoryData = ref({
    id: '', name: ''
});

//定义表单校验规则
const rules = ref({
    id: [
        { required: true, message: '请输入分类ID', trigger: 'blur' },
        { pattern: /^\d{1,5}$/, message: '分类ID为1~5位数字', trigger: 'blur' }
    ],
    name: [
        { required: true, message: '请输入分类名称', trigger: 'blur' },
        { pattern: /^\S(.){0,8}\S$/, message: '分类名称为2~10位字符', trigger: 'blur' }
    ]
});

//展示添加弹窗
const showAddDialog = () => {
    dialogVisible.value.add = true;
    categoryData.value = { id: '', name: '' };
};

//添加分类
const addCategory = async () => {
    await categoryService.addCategory(categoryData.value);
    ElMessage.success('添加成功');
    dialogVisible.value.add = false;
    categoryList();
};

//展示修改弹窗
const showUpdateDialog = (row) => {
    dialogVisible.value.update = true;
    categoryData.value.id = row.id;
    categoryData.value.name = row.name;
};

//修改分类
const updateCategory = async () => {
    await categoryService.updateCategory(categoryData.value);
    ElMessage.success('修改成功');
    dialogVisible.value.update = false;
    categoryList();
};

//表单引用
const formRef = ref(null);

//提交表单前整体校验
const submitForm = (type) => {
    formRef.value.validate((valid) => {
        if (valid) {
            if (type === 1) addCategory();
            else updateCategory();
        }
    });
};

//删除分类
const deleteCategory = (row) => {
    ElMessageBox.confirm(
        '删除分类',
        '温馨提示',
        { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => {
        await categoryService.deleteCategory(row.id);
        ElMessage.success('删除成功');
        categoryList();
    }).catch((error) => { console.log(error) });
};
</script>

<template>
    <div class='page-container'>

        <h3>分类列表</h3>

        <div class='add'>
            <el-button type='primary' :icon='CirclePlus' @click='showAddDialog'>添加分类</el-button>
        </div>

        <el-table :data='categorys' border style='width: 75%'>
            <el-table-column label='分类ID' width='150' prop='id' sortable align='center'> </el-table-column>
            <el-table-column label='分类名称' prop='name' align='center'></el-table-column>
            <el-table-column label='创建时间' width='200' prop='createTime' sortable align='center'></el-table-column>
            <el-table-column label='更新时间' width='200' prop='updateTime' sortable align='center'></el-table-column>
            <el-table-column label='操作' width='200' align='center'>
                <template #default='{ row }'>
                    <el-button :icon='Edit' @click='showUpdateDialog(row)'>编辑</el-button>
                    <el-button :icon='Delete' type='danger' @click='deleteCategory(row)'>删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model='dialogVisible.add' title='添加分类' width='30%'>
            <el-form ref='formRef' :model='categoryData' :rules='rules' label-width='100px' style='padding-right: 30px'>
                <el-form-item label='分类ID' prop='id'>
                    <el-input v-model='categoryData.id'></el-input>
                </el-form-item>
                <el-form-item label='分类名称' prop='name'>
                    <el-input v-model='categoryData.name'></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span>
                    <el-button @click='dialogVisible.add = false'>取消</el-button>
                    <el-button type='primary' @click="submitForm(1)">确认</el-button>
                </span>
            </template>
        </el-dialog>

        <el-dialog v-model='dialogVisible.update' title='修改分类' width='30%'>
            <el-form ref='formRef' :model='categoryData' :rules='rules' label-width='100px' style='padding-right: 30px'>
                <el-form-item label='分类ID' prop='id'>
                    <el-input v-model='categoryData.id' disabled></el-input>
                </el-form-item>
                <el-form-item label='分类名称' prop='name'>
                    <el-input v-model='categoryData.name'></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span>
                    <el-button @click='dialogVisible = false'>取消</el-button>
                    <el-button type='primary' @click="submitForm(2)">确认</el-button>
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

        .el-button {
            width: 5vw;
        }
    }
}
</style>@/api/user/category
