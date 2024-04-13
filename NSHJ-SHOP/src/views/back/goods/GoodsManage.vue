<script setup>
import { ref, onMounted } from 'vue'
import categoryService from '@/api/user/category'
import goodsService from '@/api/user/goods'
import getTokenStore from '@/stores/user/token'
import { ElMessage, ElMessageBox } from 'element-plus'
import { CirclePlus, Search, Filter, Edit, Delete, Plus, DataAnalysis } from '@element-plus/icons-vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import * as echarts from 'echarts'

//分类列表数据模型
const categorys = ref([]);

//商品列表数据模型
const goodss = ref([]);

//正则表达式判断表单输入
const regExp = /^\s*$/;

//搜索数据模型
const searchData = ref({
    id: null, name: null, categoryId: null,
    createTime: null, updateTime: null
});

//分页数据模型
const paginationData = ref({
    pageNum: 1, pageSize: 10, total: 0
});

//获取商品分类
const categoryList = async () => {
    let result = await categoryService.categoryList();
    categorys.value = result.data;
};

categoryList();

//获取商品列表
const goodsList = async () => {
    let params = {
        pageNum: paginationData.value.pageNum, pageSize: paginationData.value.pageSize,
        id: regExp.test(searchData.value.id) ? null : searchData.value.id,
        name: regExp.test(searchData.value.name) ? null : searchData.value.name,
        categoryId: searchData.value.categoryId, createTime: searchData.value.createTime,
        updateTime: searchData.value.updateTime
    };
    let result = await goodsService.goodsList(params);
    paginationData.value.total = result.data.total;
    goodss.value = result.data.items;
};

goodsList();

//清空搜索数据
const clearSearchData = () => {
    searchData.value.id = null;
    searchData.value.name = null;
    searchData.value.categoryId = null;
    searchData.value.createTime = null;
    searchData.value.updateTime = null;
    goodsList();
};

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

//商品数据模型
const goodsData = ref({
    id: '', name: '', content: '<p></p>',
    coverImg: '', categoryId: '', price: '',
    stock: ''
});

//定义表单校验规则
const rules = ref({
    name: [
        { required: true, message: '请输入商品名称', trigger: 'blur' },
        { pattern: /^\S(.){0,78}\S$/, message: '商品名称为2~80位字符', trigger: 'blur' }
    ],
    categoryId: [{ required: true }],
    coverImg: [{ required: true }],
    price: [
        { required: true, message: '请输入商品单价', trigger: 'blur' },
        { pattern: /^[0-9]+([.]{1}[0-9]{0,2}){0,1}$/, message: '商品单价非法', trigger: 'blur' }
    ],
    stock: [
        { required: true, message: '请输入商品库存', trigger: 'blur' },
        { pattern: /^[1-9]\d*$/, message: '商品库存非法', trigger: 'blur' }
    ]
});

//控制抽屉是否显示
const drawerVisible = ref(false);

//展示添加抽屉
const showAddDrawer = () => {
    drawerVisible.value = true;
    title.value = '添加商品';
    goodsData.value = {
        id: Date.now().toString(), name: '', content: '<p></p>',
        coverImg: '', categoryId: '', price: '',
        stock: ''
    }
};

//token状态
const tokenStore = getTokenStore();

//上传成功
const uploadSuccess = (result) => {
    if (result.code === 0) {
        goodsData.value.coverImg = result.data;
        ElMessage.success('上传成功');
    } else ElMessage.error(result.message);
};

//添加商品
const addGoods = async () => {
    await goodsService.addGoods(goodsData.value);
    ElMessage.success('添加成功');
    drawerVisible.value = false;
    goodsList();
};

//定义变量,控制名称的展示
const title = ref('');

//展示修改抽屉
const showUpdateDrawer = (row) => {
    drawerVisible.value = true;
    title.value = '修改商品';
    goodsData.value.id = row.id;
    goodsData.value.name = row.name;
    goodsData.value.content = row.content;
    goodsData.value.coverImg = row.coverImg;
    goodsData.value.categoryId = row.categoryId;
    goodsData.value.price = row.price;
    goodsData.value.stock = row.stock;
};

//修改商品
const updateGoods = async () => {
    await goodsService.updateGoods(goodsData.value);
    ElMessage.success('修改成功');
    drawerVisible.value = false;
    goodsList();
};

//表单引用
const formRef = ref(null);

//提交表单前整体校验
const submitForm = (type) => {
    formRef.value.validate((valid) => {
        if (valid) {
            if (type === 'Add') addGoods();
            else updateGoods();
        }
    });
};

//删除商品
const deleteGoods = (row) => {
    ElMessageBox.confirm(
        '删除商品',
        '温馨提示',
        { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
    ).then(async () => {
        await goodsService.deleteGoods(row.id, row.categoryId, row.coverImg);
        ElMessage.success('删除成功');
        goodsList();
    }).catch((error) => { console.log(error) });
};

//缺货商品样式设置
const cellStyle = (data) => {
    if (data.row.stock < 100) {
        return {
            background: '#FFB6C1'
        };
    }
};

//图表容器引用
const chart = ref(null);

//图表实例引用
const chartInstance = ref(null);

//控制图表抽屉是否显示
const chartVisible = ref(false);

//加载图表
const loadChart = async (row) => {
    chartVisible.value = true;
    let result = await goodsService.statistic(row.id);
    if (chartInstance.value === null) {
        chartInstance.value = echarts.init(chart.value);
    }

    //指定图表的配置项和数据
    const option = {
        title: { text: '该商品近一周销售统计' }, tooltip: {}, legend: { data: [] },
        xAxis: {
            type: 'category', data: [], name: '日期',
            axisLabel: {
                formatter: (value) => {
                    if (value.length > 20) return value.substring(0, 20) + '...';
                    else return value;
                }
            },
            axisTick: {
                show: false
            },
            axisLine: {
                show: true,
                symbol: ['none', 'arrow']
            },
        },
        yAxis: {
            type: 'value', name: '销量',
            axisLine: {
                show: true,
                symbol: ['none', 'arrow']
            },
            minInterval: 1
        },
        tooltip: {
            formatter: (datas) => {
                return datas.value.toFixed(0);
            }
        },
        series: [{
            type: 'line', smooth: true, data: [], markPoint: {
                data: [
                    { type: 'max', name: 'Max' },
                    { type: 'min', name: 'Min' }
                ]
            },
            label: {
                show: true,
                position: 'left',
                textStyle: {
                    fontSize: 20,
                    color: '#5470C6'
                },
                formatter: (params) => {
                    if (option.series[0].data.length - 1 === params.dataIndex) {
                        return '预测值';
                    } else {
                        return '';
                    }
                }
            }
        }]
    };
    for (let i = 0; i < result.data.length; i++) {
        option.xAxis.data.push(result.data[i].date);
        option.series[0].data.push(result.data[i].sell);
    }
    chartInstance.value.setOption(option);
};
</script>

<template>
    <div class='page-container'>

        <h3>筛选搜索</h3>

        <el-card shadow='never'>
            <el-form label-width="10vw" :model='searchData'>
                <el-row>
                    <el-col :span='8'>
                        <el-form-item label='商品ID：'>
                            <el-input style='width:100%' placeholder='请输入商品ID' v-model='searchData.id'>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span='8'>
                        <el-form-item label='商品名称：'>
                            <el-input style='width:100%' placeholder='请输入商品名称' v-model='searchData.name'>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span='8'>
                        <el-form-item label='商品分类：'>
                            <el-select style='width:100%' placeholder='请选择分类' v-model='searchData.categoryId' clearable>
                                <el-option v-for='category in categorys' :key='category.id' :label='category.name'
                                    :value='category.id'>
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span='8'>
                        <el-form-item label='创建时间：'>
                            <el-date-picker style='width:100%' placeholder='请选择创建时间' v-model='searchData.createTime'
                                type='date' value-format='YYYY-MM-DD' />
                        </el-form-item>
                    </el-col>
                    <el-col :span='8'>
                        <el-form-item label='更新时间：'>
                            <el-date-picker style='width:100%' placeholder='请选择更新时间' v-model='searchData.updateTime'
                                type='date' value-format='YYYY-MM-DD' />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div class='search'>
                <el-button type='primary' :icon='Search' @click='goodsList'>搜索</el-button>
                <el-button :icon='Filter' @click='clearSearchData'>重置</el-button>
            </div>
        </el-card>

        <h3>商品列表</h3>

        <div class='add'>
            <el-button type='primary' :icon='CirclePlus' @click='showAddDrawer'>添加商品</el-button>
        </div>

        <el-table :data='goodss' border style='width: 100%' :cell-style="cellStyle">
            <el-table-column type='expand'>
                <template #default='{ row }'>
                    <div>
                        <p>创建时间: {{ row.createTime }}</p>
                        <p>更新时间: {{ row.updateTime }}</p>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label='商品ID' width='160' prop='id' :show-overflow-tooltip='true' sortable
                align='center'></el-table-column>
            <el-table-column label='商品' width='300' :show-overflow-tooltip='true' align='center'>
                <template #default='{ row }'>
                    <el-image :src="row.coverImg + '?v=' + Date.now()"></el-image>
                    <div class='name'>{{ row.name }}</div>
                </template>
            </el-table-column>
            <el-table-column label='分类' width='100' prop='categoryName' align='center'></el-table-column>
            <el-table-column label='单价' width='130' prop='price' sortable align='center'>
                <template #default='props'>
                    ￥{{ props.row.price.toFixed(2) }}
                </template>
            </el-table-column>
            <el-table-column label='库存' width='120' prop='stock' sortable align='center'></el-table-column>
            <el-table-column label='销量' width='120' prop='sell' sortable align='center'></el-table-column>
            <el-table-column label='操作' width='250' align='center'>
                <template #default='{ row }'>
                    <el-button :icon='Edit' @click='showUpdateDrawer(row)'>编辑</el-button>
                    <el-button :icon='DataAnalysis' type='primary' @click='loadChart(row)'>统计</el-button>
                    <el-button :icon='Delete' type='danger' @click='deleteGoods(row)'>删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination v-model:current-page='paginationData.pageNum' v-model:page-size='paginationData.pageSize'
            :page-sizes='[10, 20, 50, 100]' layout='jumper, total, sizes, prev, pager, next' background
            :total='paginationData.total' @size-change='onSizeChange' @current-change='onCurrentChange'
            style='margin: 5vh 0 5vh; justify-content: flex-end' />

        <el-drawer v-model='drawerVisible' :title='title' direction='rtl' size='50%'>
            <el-form ref='formRef' :model='goodsData' :rules='rules' label-width='100px'>
                <el-form-item label='商品名称' prop='name'>
                    <el-input v-model='goodsData.name' placeholder='请输入名称'></el-input>
                </el-form-item>
                <el-form-item label='商品分类' prop='categoryId'>
                    <el-select placeholder='请选择' v-model='goodsData.categoryId' clearable>
                        <el-option v-for='category in categorys' :key='category.id' :label='category.name'
                            :value='category.id'>
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label='商品封面' prop='coverImg'>
                    <el-upload class='avatar-uploader' :auto-upload='true' :show-file-list='false' action='/back/upload'
                        name='file' :headers="{ 'Authorization': tokenStore.token }"
                        :data="{ 'fileName': goodsData.id }" :on-success='uploadSuccess'>
                        <img v-if='goodsData.coverImg' :src="goodsData.coverImg + '?v=' + Date.now()" class='avatar' />
                        <el-icon v-else class='avatar-uploader-icon'>
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label='商品单价' prop='price'>
                    <el-input v-model='goodsData.price' placeholder='请输入价格'></el-input>
                </el-form-item>
                <el-form-item label='商品库存' prop='stock'>
                    <el-input v-model='goodsData.stock' placeholder='请输入库存'></el-input>
                </el-form-item>
                <el-form-item label='商品详情'>
                    <div class='editor'>
                        <quill-editor theme='snow' v-model:content='goodsData.content' contentType='html'
                            placeholder='请输入详情'>
                        </quill-editor>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type='primary' @click="title === '添加商品' ? submitForm('Add') :
                        submitForm('Update')">上架</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>

        <el-drawer v-model='chartVisible' size='50%'>
            <div ref='chart' class='chart'>

            </div>
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

    .add {
        display: flex;
        align-items: center;
        justify-content: right;
    }

    .el-image {
        float: left;
        width: 3vw;
        height: 3vw;
    }

    .el-table {
        margin-top: 3vh;

        .el-button {
            width: 5vw;
        }

        .name {
            display: flex;
            height: 3vw;
            align-items: center;
        }

        .avatar-uploader {

            :deep() {
                .avatar {
                    width: 10vw;
                    height: 10vw;
                    display: block;
                }

                .el-upload {
                    border: 1px dashed var(--el-border-color);
                    border-radius: 6px;
                    cursor: pointer;
                    position: relative;
                    overflow: hidden;
                    transition: var(--el-transition-duration-fast);
                }

                .el-upload:hover {
                    border-color: var(--el-color-primary);
                }

                .el-icon.avatar-uploader-icon {
                    font-size: 28px;
                    color: #8c939d;
                    width: 10vw;
                    height: 10vw;
                    text-align: center;
                }
            }
        }
    }

    .el-drawer {
        .avatar-uploader {

            :deep() {
                .avatar {
                    width: 10vw;
                    height: 10vw;
                    display: block;
                }

                .el-upload {
                    border: 1px dashed var(--el-border-color);
                    border-radius: 6px;
                    cursor: pointer;
                    position: relative;
                    overflow: hidden;
                    transition: var(--el-transition-duration-fast);
                }

                .el-upload:hover {
                    border-color: var(--el-color-primary);
                }

                .el-icon.avatar-uploader-icon {
                    font-size: 28px;
                    color: #8c939d;
                    width: 10vw;
                    height: 10vw;
                    text-align: center;
                }
            }
        }

        .editor {
            width: 100%;

            :deep(.ql-editor) {
                min-height: 200px;
            }
        }
    }

    .chart {
        width: 100%;
        height: 100%;
    }
}
</style>@/api/user/category@/api/user/goods@/stores/user/token