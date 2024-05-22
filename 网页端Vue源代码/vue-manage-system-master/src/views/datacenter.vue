<template>
    <div>
        <el-row :gutter="20" class="mgb20">
            <el-col :span="6">
                <el-card shadow="hover" body-class="card-body">
                    <el-icon class="card-icon bg1">
                        <User />
                    </el-icon>
                    <div class="card-content">
                        <countup class="card-num color1" :end="todayOrderUser" />
                        <div>今日下单人数</div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card shadow="hover" body-class="card-body">
                    <el-icon class="card-icon bg2">
                        <credit-card />
                    </el-icon>
                    <div class="card-content">
                        <countup class="card-num color2" :end="todayTurnover" />
                        <div>今日实际收入</div>
                    </div>
                </el-card>
            </el-col>
          <el-col :span="6">
            <el-card shadow="hover" body-class="card-body">
              <el-icon class="card-icon bg4">
                <ShoppingCartFull />
              </el-icon>
              <div class="card-content">
                <countup class="card-num color4" :end="todayOrderCount" />
                <div>今日订单数量</div>
              </div>
            </el-card>
          </el-col>
            <el-col :span="6">
                <el-card shadow="hover" body-class="card-body">
                    <el-icon class="card-icon bg3">
                        <Comment />
                    </el-icon>
                    <div class="card-content">
                        <countup class="card-num color3" :end="todayPostCount" />
                        <div>今日获得评价</div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

<!--        待处理事项：包括待制作订单、已完成订单、待处理退款申请-->
        <el-row :gutter="20" class="mgb20">
          <el-col :span="24">
              <el-card shadow="hover">
                  <div class="card-header">
                      <p class="card-header-title">{{ restaurantName }}</p>
                      <p class="card-header-desc">你目前的评分是{{restaurantRate}}分</p>
                  </div>
                <el-card class="mgb20" shadow="hover">
                  <el-row>
                    <el-col :span="8" style="text-align: center">
                      <p class="mgb10">未完成订单</p>
                      <countup class="countup" :end="unfinishedOrderCount" />
                    </el-col>
                    <el-col :span="8" style="text-align: center">
                      <p class="mgb10">已完成订单</p>
                      <countup class="countup" :end="finishedOrderCount" />
                    </el-col>
                    <el-col :span="8" style="text-align: center">
                      <p class="mgb10">待处理退款</p>
                      <countup class="countup" :end="deadValue" />
                    </el-col>
                  </el-row>
                </el-card>
              </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="24">
                <el-card shadow="hover" :body-style="{ height: '400px' }">
                    <div class="card-header">
                        <p class="card-header-title">排行榜</p>
                        <p class="card-header-desc">商家热门榜单Top5</p>
                    </div>
                    <div>
                        <div class="rank-item" v-for="(rank, index) in ranks">
                            <div class="rank-item-avatar">{{ index + 1 }}</div>
                            <div class="rank-item-content">
                                <div class="rank-item-top">
                                    <div class="rank-item-title">{{ rank.restaurantName }}</div>
                                    <div class="rank-item-desc">订单量：{{ rank.orderCount }}</div>
                                </div>
                                <el-progress
                                    :show-text="false"
                                    striped
                                    :stroke-width="10"
                                    :percentage="rank.percentage"
                                    :color="rank.color"
                                />
                            </div>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script setup lang="ts" name="datacenter">
import countup from '@/components/countup.vue';
import { use, registerMap } from 'echarts/core';
import { BarChart, LineChart, PieChart, MapChart } from 'echarts/charts';
import {
    GridComponent,
    TooltipComponent,
    LegendComponent,
    TitleComponent,
    VisualMapComponent,
} from 'echarts/components';
import { CanvasRenderer } from 'echarts/renderers';
import chinaMap from '@/utils/china';
import {ref} from "vue";
import {CreditCard} from "@element-plus/icons-vue";
import {fetchOrderRankData, fetchTodayData} from "@/api";
use([
    CanvasRenderer,
    BarChart,
    GridComponent,
    LineChart,
    PieChart,
    TooltipComponent,
    LegendComponent,
    TitleComponent,
    VisualMapComponent,
    MapChart,
]);
registerMap('china', chinaMap);

interface userInfo {
  restaurantId: string;
}
const restaurantId = localStorage.getItem('vuems_name');
console.log(restaurantId);
const param: userInfo = {
  restaurantId: restaurantId
};
// 定义 ranks 为响应式的 ref 对象
// 当你更新 ranks 的值时，Vue 会自动检测到变化并触发页面重新渲染，确保页面上显示的数据是最新的
const ranks = ref([]);

//今日数据统计
const restaurantRate = ref();
const todayOrderUser = ref();
const todayTurnover = ref();
const todayOrderCount = ref();
const todayPostCount = ref();
const restaurantName = localStorage.getItem('restaurantName');

// 待处理事项统计
const unfinishedOrderCount = ref();
const finishedOrderCount = ref();
const deadValue = ref();

// 请求今日统计数据
const getTodayData = async () => {
  try {
    const res = await fetchTodayData(param);
    console.log(res);
    restaurantRate.value = res.data.restaurantRate;
    todayOrderUser.value = res.data.todayOrderUser;
    todayTurnover.value = res.data.todayTurnover;
    todayOrderCount.value = res.data.todayOrderCount;
    todayPostCount.value = res.data.todayPostCount;
    unfinishedOrderCount.value = res.data.unfinishedOrderCount;
    finishedOrderCount.value = res.data.finishedOrderCount;
  } catch (error) {
    console.error(error);
  }
}

// 请求订单量排行的数据
const getOrderRank = async () => {
  try {
    const res = await fetchOrderRankData(); // 从后端请求数据
    console.log(res);
    ranks.value =  res.data;
  } catch (error) {
    console.log(error);
  }
}

const fetchData = async () => {
  await getTodayData();
  await getOrderRank();
}
// 初始化页面数据
fetchData();
</script>

<style>
.card-body {
    display: flex;
    align-items: center;
    height: 100px;
    padding: 0;
}
</style>
<style scoped>
.card-content {
    flex: 1;
    text-align: center;
    font-size: 15px;
    color: #999;
    padding: 0 20px;
}

.card-num {
    font-size: 30px;
}

.card-icon {
    font-size: 50px;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
    color: #fff;
}

.bg1 {
    background: #2d8cf0;
}

.bg2 {
    background: #64d572;
}

.bg3 {
    background: #f25e43;
}

.bg4 {
    background: #e9a745;
}

.color1 {
    color: #2d8cf0;
}

.color2 {
    color: #64d572;
}

.color3 {
    color: #f25e43;
}

.color4 {
    color: #e9a745;
}

.countup {
  font-size: 20px;
  color: #f56c6c;

}

.card-header {
    padding-left: 10px;
    margin-bottom: 20px;
}

.card-header-title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 5px;
}

.card-header-desc {
    font-size: 14px;
    color: #999;
}

.rank-item {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

.rank-item-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background: #f2f2f2;
    text-align: center;
    line-height: 40px;
    margin-right: 10px;
}

.rank-item-content {
    flex: 1;
}

.rank-item-top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: #343434;
    margin-bottom: 10px;
}

.rank-item-desc {
    font-size: 14px;
    color: #999;
}
</style>
