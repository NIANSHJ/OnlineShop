<script setup>
import { ref, onMounted } from 'vue';
import sellService from '@/api/user/sell'
import * as echarts from 'echarts'

//容器引用
const main = ref(null);

//指定图表的配置项和数据
const option = {
    title: { text: '预测销售额' }, tooltip: {}, legend: { data: [] },
    xAxis: {
        type: 'category', data: [], name: '分类',
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
        type: 'value', name: '销售额',
        axisLabel: {
            formatter: (value) => {
                return value.toFixed(2);
            }
        },
        axisLine: {
            show: true,
            symbol: ['none', 'arrow']
        }
    },
    tooltip: {
        formatter: (datas) => {
            return '￥' + datas.value.toFixed(2);
        }
    },
    series: [{
        type: 'line', smooth: true, data: [], markPoint: {
            data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' }
            ]
        },
        markLine: {
            data: [{ type: 'average', name: 'Avg' }]
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

//获取各分类销量数据
const display = async () => {
    let result = await sellService.prediction();
    for (let i = 0; i < result.data.length; i++) {
        option.xAxis.data.push(result.data[i].date);
        option.series[0].data.push(result.data[i].sales);
    }
    echarts.init(main.value).setOption(option);
};

//组件挂载后显示数据
onMounted(() => { display(); });
</script>

<template>
    <div ref='main' class='page-container'>
        <div></div>
    </div>
</template>

<style lang='scss' scoped>
.page-container {

    min-height: 100%;
    box-sizing: border-box;
    margin: 3vh 1vw 0;
}
</style>@/api/user/statistic@/api/user/sell