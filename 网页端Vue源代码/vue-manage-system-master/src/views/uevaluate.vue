<template>
  <div>
    <div class="container">
      <TableCustom :columns="columns" :tableData="tableData" :total="page.total"
                   :editFunc="handleEdit" :refresh="getData" :currentPage="page.index"
                   :changePage="changePage">
      </TableCustom>
    </div>
  </div>
</template>

<script setup lang="ts" name="basetable">
import { ref, reactive } from 'vue';
import { fetchUserEvaluateData } from '@/api';
import TableCustom from '@/components/table-custom.vue';
import {userEvaluateTbleItem} from "@/types/uevaluate";

// 表格相关
let columns = ref([
  { type: 'index', label: '序号', width: 55, align: 'center' },
  { prop: 'posterName', label: '用户名' , width: 100},
  { prop: 'posterId', label: '账号' },
  { prop: 'postContext', label: '评价' },
  { prop: 'rate', label: '评分' },
])
//页面相关
const page = reactive({
  index: 1,
  size: 10,
  total: 0,
})

interface userInfo {
  restaurantId: string;
}
const restaurantId = localStorage.getItem('vuems_name');
console.log(restaurantId);
const param: userInfo = {
  restaurantId: restaurantId
};

const tableData = ref<userEvaluateTbleItem[]>([]);//表格数据
const getData = async (index:number) => {       //从 API 获取数据并更新 tableData
  const res = await fetchUserEvaluateData(param);
  console.log(res);
  let datalist = res.data;
  const length = datalist.length;
  const i = (index-1)*10;
  const j = getLine(index, length); // 计算页尾索引
  // 将页面数据显示
  tableData.value = datalist.slice(i, j);
  console.log(tableData.value);
  //获取订单数量，更新total值
  page.total = res.data.length;
};
getData(page.index);

// 计算出这一页的最后一条数据的索引
const getLine = (index: number, length: number) => {
  const i = (index-1)*10;
  if (i+10 > length-1) {
    return length;
  } else {
    return i+10;
  }
};

//用于改变当前页码并重新获取数据
const changePage = (val: number) => {
  page.index = val;
  getData(page.index);
};

const visible = ref(false);
const isEdit = ref(false);
const rowData = ref({});
const handleEdit = (row: userEvaluateTbleItem) => {
  rowData.value = { ...row };
  isEdit.value = true;
  visible.value = true;
};
const updateData = () => {
  closeDialog();
  getData(page.index);
};

const closeDialog = () => {
  visible.value = false;
  isEdit.value = false;
};

</script>
