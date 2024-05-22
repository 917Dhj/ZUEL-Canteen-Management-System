<template>
  <div>
    <TableSearch :query="query" :options="searchOpt" :search="handleSearch" />
    <div class="container">
      <AdminMenuTable :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                   :delFunc="handleDelete" :addFunc="handleNew" :refresh="getData" :currentPage="page.index"
                   :changePage="changePage">
      </AdminMenuTable>

    </div>
    <el-dialog :title="isEdit ? '编辑' : '新增'" v-model="visible" width="700px" destroy-on-close
               :close-on-click-modal="false" @close="closeDialog">
      <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData" />
    </el-dialog>
    <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
      <TableDetail :data="viewData"></TableDetail>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="system-menu">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { Menus } from '@/types/menu';
import {
  deleteMenusData,
  fetchEvaluateData,
  fetchEvaluateDataFiltered,
  fetchMenusData,
  fetchMenusDataFiltered, onsaleMenusData
} from '@/api';
import AdminMenuTable from '@/components/admin-menu-table.vue';
import TableDetail from '@/components/table-detail.vue';
import TableSearch from '@/components/table-search.vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import TableEdit from "@/components/table-edit.vue";

interface dishName {
  dishName: string;
}

// 查询相关
const query = reactive({
  dishName: '',
});
const searchOpt = ref<FormOptionList[]>([
  { type: 'input', label: '菜品名称：', prop: 'dishName' }
])
const handleSearch = () => {
  getData(page.index);
};

// 表格相关
let columns = ref([
  { type: 'index', label: '序号', width: 55, align: 'center' },
  { prop: 'canteenName', label: '食堂名称' },
  { prop: 'restaurantName', label: '档口名称'},
  { prop: 'dishName', label: '菜品名称' },
  { prop: 'dishId', label: '菜品ID' },
  { prop: 'dishStatus', label: '上架状态' },
  { prop: 'operator', label: '操作', width: 250 },
])
const page = reactive({
  index: 1,
  size: 10,
  total: 0,
})
const tableData = ref<Menus[]>([]);
// 获取页面表达数据
const getData = async (index:number) => {
  const input = query.dishName
  let res;
  if(input == ''){
    // 如果搜索框中没有内容，则显示全部帖子
    res = await fetchMenusData();
  } else {
    // 如果搜索框中有内容，则会根据搜索框来设置数据
    const param: dishName = {
      dishName: input
    }
    res = await fetchMenusDataFiltered(param);
  }
  // console.log(res);
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

const changePage = (val: number) => {
  page.index = val;
  getData(page.index);
};

// 新增/编辑弹窗相关
let options = ref<FormOption>({
  labelWidth: '100px',
  span: 12,
  list: [
    { type: 'input', label: '食堂名称', prop: 'canteenName', required: true },
    { type: 'input', label: '档口名称', prop: 'restaurantName', required: true },
    { type: 'input', label: '菜品名称', prop: 'dishName', required: true },
    { type: 'input', label: '菜品ID', prop: 'dishId', required: true },
  ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref({});
const updateData = () => {
  closeDialog();
  getData(page.index);
};

const closeDialog = () => {
  visible.value = false;
  isEdit.value = false;
};

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
  row: {},
  list: []
});
const handleView = (row: Menus) => {
  viewData.value.row = { ...row }
  viewData.value.list = [
    {
      prop: 'canteenName',
      label: '食堂名称',
    },
    {
      prop: 'restaurantName',
      label: '档口名称',
    },
    {
      prop: 'dishName',
      label: '菜品名称',
    },
    {
      prop: 'dishId',
      label: '菜品ID',
    },
  ]
  visible1.value = true;
};

interface dishInfo {
  dishId: number;
}
// 下架菜品相关
const handleDelete = async (row: Menus) => {
  console.log(row.dishId);
  const param: dishInfo = {
    dishId: row.dishId
  };
  await deleteMenusData(param);
  ElMessage.success('下架菜品成功');
  await getData(page.index);
}

//商品上架相关
const handleNew = async (row: Menus) => {
  console.log(row.dishId);
  const param: dishInfo = {
    dishId: row.dishId
  };
  await onsaleMenusData(param);
  ElMessage.success('上架菜品成功');
  await getData(page.index);
};

</script>

<style scoped></style>