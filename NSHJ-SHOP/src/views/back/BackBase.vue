<script setup>
import { ref } from 'vue'
import userService from '@/api/user/user'
import getTokenStore from '@/stores/user/token'
import getUserInfoStore from '@/stores/user/userInfo'
import router from '@/router'
import Bread from '@/views/Bread.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
    ArrowDown, HomeFilled, Goods, Menu, List, Checked, UserFilled, User, EditPen, SwitchButton, Briefcase, DataAnalysis, TrendCharts, Postcard, Files, Document
} from '@element-plus/icons-vue'

//头像
const avatar = ref(new URL('@/assets/back/default.png', import.meta.url).href);

//info状态
const userInfoStore = getUserInfoStore();

//token状态
const tokenStore = getTokenStore();

//获取管理员信息
const getUserInfo = async () => {
    let result = await userService.userInfo();
    userInfoStore.setInfo(result.data);
};

getUserInfo();

//判断命令
const handleCommand = (command) => {
    if (command === 'logout') {
        ElMessageBox.confirm(
            '您确认要退出吗?',
            '温馨提示',
            { confirmButtonText: '确认', cancelButtonText: '取消', type: 'warning' }
        ).then(async () => {
            await userService.logOut();
            userInfoStore.removeInfo();
            tokenStore.removeToken();
            router.push('/user/login');
            ElMessage.success('已退出登录');
        }).catch((error) => { console.log(error); });
    } else router.push('/user/center/' + command);
};
</script>

<template>
    <el-container class='layout-container'>
        <el-aside width='15vw'>
            <div class='logo'></div>

            <el-menu :default-active='$route.path' background-color='#232323' active-text-color='#ffd04b'
                text-color='#fff' router>
                <el-menu-item index='/user/index'>
                    <el-icon>
                        <HomeFilled />
                    </el-icon>
                    <span>店铺首页</span>
                </el-menu-item>
                <el-sub-menu index='/user/goods'>
                    <template #title>
                        <el-icon>
                            <Goods />
                        </el-icon>
                        <span>商品</span>
                    </template>
                    <el-menu-item index='/user/goods/category'>
                        <el-icon>
                            <Menu />
                        </el-icon>
                        <span>商品分类</span>
                    </el-menu-item>
                    <el-menu-item index='/user/goods/manage'>
                        <el-icon>
                            <List />
                        </el-icon>
                        <span>商品管理</span>
                    </el-menu-item>
                </el-sub-menu>

                <el-sub-menu index='/user/order'>
                    <template #title>
                        <el-icon>
                            <Checked />
                        </el-icon>
                        <span>订单</span>
                    </template>
                    <el-menu-item index='/user/order/manage'>
                        <el-icon>
                            <List />
                        </el-icon>
                        <span>订单管理</span>
                    </el-menu-item>
                </el-sub-menu>

                <el-sub-menu index='/user/sell'>
                    <template #title>
                        <el-icon>
                            <Briefcase />
                        </el-icon>
                        <span>销售</span>
                    </template>
                    <el-menu-item index='/user/sell/statistic'>
                        <el-icon>
                            <DataAnalysis />
                        </el-icon>
                        <span>销售统计</span>
                    </el-menu-item>
                    <el-menu-item index='/user/sell/prediction'>
                        <el-icon>
                            <TrendCharts />
                        </el-icon>
                        <span>趋势预测</span>
                    </el-menu-item>
                </el-sub-menu>

                <el-sub-menu index='/user/staff'>
                    <template #title>
                        <el-icon>
                            <Postcard />
                        </el-icon>
                        <span>人员</span>
                    </template>
                    <el-menu-item index='/user/staff/manage'>
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>销售员管理</span>
                    </el-menu-item>
                </el-sub-menu>

                <el-sub-menu index='/user/center'>
                    <template #title>
                        <el-icon>
                            <UserFilled />
                        </el-icon>
                        <span>个人中心</span>
                    </template>
                    <el-menu-item index='/user/center/info'>
                        <el-icon>
                            <User />
                        </el-icon>
                        <span>基本资料</span>
                    </el-menu-item>
                    <el-menu-item index='/user/center/resetPassword'>
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu>

                <el-sub-menu index='/user/log'>
                    <template #title>
                        <el-icon>
                            <Files />
                        </el-icon>
                        <span>日志</span>
                    </template>
                    <el-menu-item index='/user/log/customer'>
                        <el-icon>
                            <Document />
                        </el-icon>
                        <span>顾客日志</span>
                    </el-menu-item>
                    <el-menu-item index='/user/log/system'>
                        <el-icon>
                            <Document />
                        </el-icon>
                        <span>系统日志</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>

        <el-container>
            <el-header>
                <div>
                    <bread />
                </div>
                <div class='right'>
                    <div class='username'><span>{{ userInfoStore.info.username }}</span></div>
                    <el-dropdown placement='bottom-end' @command='handleCommand'>
                        <span class='el-dropdown-link'>
                            <el-avatar :src='avatar' />
                            <el-icon>
                                <ArrowDown />
                            </el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command='info' :icon='User'>基本资料</el-dropdown-item>
                                <el-dropdown-item command='resetPassword' :icon='EditPen'>重置密码</el-dropdown-item>
                                <el-dropdown-item command='logout' :icon='SwitchButton'>退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>

            <el-main>
                <router-view></router-view>
            </el-main>

            <el-footer>NSHJ小店 ©2023 Created By NSHJ</el-footer>
        </el-container>
    </el-container>
</template>

<style lang='scss' scoped>
.layout-container {

    height: 100vh;

    .el-aside {
        background-color: #232323;

        .logo {
            height: 20vh;
            background: url('@/assets/back/logo.png') no-repeat center / 25vh auto;
        }

        .el-menu {
            border-right: none;
        }
    }

    .el-header {
        background-color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;

        .username {
            font-size: 16px;
            margin-right: 1vw;
        }

        .right {
            display: flex;
            align-items: center;

            .el-dropdown-link {
                cursor: pointer;
                display: flex;
                align-items: center;

                &:focus {
                    outline: none;
                }
            }
        }
    }

    .el-main {
        padding: 0;
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #666;
        background-color: #EAEAEA;
    }
}
</style>@/api/user/user@/stores/user/token@/stores/user/userInfo