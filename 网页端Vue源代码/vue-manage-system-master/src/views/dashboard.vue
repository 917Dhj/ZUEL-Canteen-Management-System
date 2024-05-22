<template>
    <div>
        <el-row :gutter="20" class="mgb20">
            <el-col :span="6">
                <el-card shadow="hover" body-class="card-body">
                    <el-icon class="card-icon bg1">
                        <User />
                    </el-icon>
                    <div class="card-content">
                        <countup class="card-num color1" :end="totalOrderUser" />
                        <div>累计下单人数</div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card shadow="hover" body-class="card-body">
                    <el-icon class="card-icon bg2">
                      <credit-card />
                    </el-icon>
                    <div class="card-content">
                        <countup class="card-num color2" :end="totalTurnover" />
                        <div>累计收入</div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card shadow="hover" body-class="card-body">
                    <el-icon class="card-icon bg3">
                      <ShoppingCartFull />
                    </el-icon>
                    <div class="card-content">
                        <countup class="card-num color3" :end="totalCourseCount" />
                        <div>商品数量</div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="6">
                <el-card shadow="hover" body-class="card-body">
                    <el-icon class="card-icon bg4">
                        <ShoppingCartFull />
                    </el-icon>
                    <div class="card-content">
                        <countup class="card-num color4" :end="totalOrderCount" />
                        <div>订单量</div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <el-row :gutter="20" class="mgb20">
            <el-col :span="16">
                <el-card shadow="hover">
                    <div class="card-header">
                        <p class="card-header-title">营业额动态</p>
                        <p class="card-header-desc">最近一周的营业额变化</p>
                    </div>
                    <v-chart class="chart" :option="dash1" />
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover">
                    <div class="card-header">
                        <p class="card-header-title">订单分布</p>
                        <p class="card-header-desc">最近一个月各个食堂的销售情况</p>
                    </div>
                    <v-chart class="chart-2" :option="dash2" />
              </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="12">
                <el-card shadow="hover" :body-style="{ height: '400px' }">
                    <div class="card-header">
                        <p class="card-header-title">评价广场</p>
                        <p class="card-header-desc">最新评价信息</p>
                    </div>
                    <el-timeline>
                        <el-timeline-item v-for="(activity, index) in activities" :key="index" :color="activity.color">
                            <div class="timeline-item">
                                <div>
                                    <p>{{ activity.posterName }}</p>
                                    <p class="timeline-desc">{{ activity.postContext }}</p>
                                </div>
                                <div class="timeline-time">{{ activity.timeDifference }}前</div>
                            </div>
                        </el-timeline-item>
                    </el-timeline>
                </el-card>
            </el-col>

            <el-col :span="12">
                <el-card shadow="hover" :body-style="{ height: '400px' }">
                    <div class="card-header">
                        <p class="card-header-title">排行榜</p>
                        <p class="card-header-desc">菜品的热门榜单Top5</p>
                    </div>
                    <div>
                        <div class="rank-item" v-for="(rank, index) in ranks">
                            <div class="rank-item-avatar">{{ index + 1 }}</div>
                            <div class="rank-item-content">
                                <div class="rank-item-top">
                                    <div class="rank-item-title">{{ rank.restaurantName }}</div>
                                    <div class="rank-item-desc">销量：{{ rank.orderCount }}</div>
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

<script setup lang="ts" name="dashboard">
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
import VChart from 'vue-echarts';
import { dashOpt1, dashOpt2 } from './chart/options';
import chinaMap from '@/utils/china';
import {CreditCard} from "@element-plus/icons-vue";
import {ref} from "vue";
import {fetchDash1Data, fetchDash2Data, fetchNewPostData, fetchOrderRankData, fetchTotalData} from "@/api";
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

// 定义 ranks 为响应式的 ref 对象
// 当你更新 ranks 的值时，Vue 会自动检测到变化并触发页面重新渲染，确保页面上显示的数据是最新的
const ranks = ref([]);
const activities = ref([]);
const dash1 = ref({});
const dash2 = ref({});

// 数据统计
const totalOrderUser = ref();
const totalTurnover = ref();
const totalCourseCount = ref();
const totalOrderCount = ref();

// 请求排行榜的数据
const getTotalData = async () => {
  try {
    const res = await fetchTotalData();
    console.log(res);
    totalOrderUser.value = res.data.totalOrderUser;
    totalTurnover.value = res.data.totalTurnover;
    totalCourseCount.value = res.data.totalCourseCount;
    totalOrderCount.value = res.data.totalOrderCount;
  } catch (error) {
    console.error(error);
  }
}

// 请求柱状图的数据
const getDash1Data = async () => {
  try {
    const res = await fetchDash1Data();
    console.log(res);
    let data = dashOpt1;
    data.series[0].data = res.data;
    dash1.value = data;
  } catch (error) {
    console.error(error);
  }
}

// 请求环形图数据
const getDash2Data = async () => {
  try {
    const res = await fetchDash2Data();
    console.log(res);
    let data = dashOpt2;
    data.series[0].data = res.data;
    dash2.value = data;
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

// 请求最新评论列表数据
const getNewPost = async () => {
  try {
    const res = await fetchNewPostData();
    activities.value = res.data;
  } catch (error) {
    console.log(error);
  }
}

const fetchData = async () => {
  await getTotalData(); // 初始化统计数据
  await getDash1Data(); // 初始化柱状图数据
  await getDash2Data(); // 初始化环形图数据
  await getOrderRank(); // 初始化商家订单量排行
  await getNewPost(); // 初始化最新评价列表
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
    font-size: 14px;
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

.chart {
    width: 100%;
    height: 400px;
    justify-content: center;
}

.chart-2 {
    width: 400px;
    height: 400px;
    justify-content: center;
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

.timeline-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 16px;
    color: #000;
}

.timeline-time,
.timeline-desc {
    font-size: 12px;
    color: #787878;
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
.map-chart {
    width: 100%;
    height: 350px;
}
</style>
