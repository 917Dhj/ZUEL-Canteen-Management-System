<template>
  <div>
    <TableSearch :query="query" :options="searchOpt" :search="handleSearch" />
    <div class="container">
      <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                   :delFunc="handleDelete" :refresh="getData" :currentPage="page.index" :changePage="changePage" :editFunc="handleEdit">
      </TableCustom>

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

<script setup lang="ts" name="system-adorder">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { Adorder } from '@/types/adorder';
import {fetchAdorderData, fetchAdorderDataFiltered, fetchMenusData, fetchMenusDataFiltered} from '@/api';
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import TableSearch from '@/components/table-search.vue';
import { FormOption, FormOptionList } from '@/types/form-option';

interface orderInfo {
  orderId: number
}

// 查询相关
const query = reactive({
  orderId: '',
});
const searchOpt = ref<FormOptionList[]>([
  { type: 'input', label: '订单号：', prop: 'orderId' }
])
const handleSearch = () => {
  getData(page.index);
};

// 表格相关
let columns = ref([
  { type: 'index', label: '序号', width: 55, align: 'center' },
  { prop: 'orderId', label: '订单ID' },
  { prop: 'userName', label: '用户名' },
  { prop: 'userId', label: '用户账号' },
  { prop: 'dishName', label: '菜品名称' },
  { prop: 'restaurantName', label: '档口名称'},
  { prop: 'canteenName', label: '食堂名称' },
  { prop: 'status',label:'订单状态'},
])
const page = reactive({
  index: 1,
  size: 10,
  total: 0,
})
const tableData = ref<Adorder[]>([]);
const getData = async (index:number) => {
  const input = query.orderId
  let res;
  if(input == ''){
    // 如果搜索框中没有内容，则显示全部帖子
    res = await fetchAdorderData();
  } else {
    // 如果搜索框中有内容，则会根据搜索框来设置数据
    const param: orderInfo = {
      orderId: parseInt(input),
    }
    res = await fetchAdorderDataFiltered(param);
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
  console.log(val);
  page.index = val;
  getData(page.index);
};

// 新增/编辑弹窗相关
let options = ref<FormOption>({
  labelWidth: '100px',
  span: 12,
  list: [
    { type: 'input', label: '订单ID', prop: 'orderId', required: true },
    { type: 'input', label: '菜品ID', prop: 'dishId', required: true },
    { type: 'input', label: '用户名', prop: 'userName', required: true },
    { type: 'input', label: '用户账号', prop: 'userId', required: true },
    { type: 'input', label: '食堂名称', prop: 'canteenName', required: true },
    { type: 'input', label: '档口名称', prop: 'restaurantName', required: true },
    { type: 'input', label: '菜品名称', prop: 'dishName', required: true },
    { type: 'input', label: '订单状态', prop: 'status', required: true },
  ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref({});
const handleEdit = (row: Adorder) => {
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

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
  row: {},
  list: []
});
const handleView = (row: Adorder) => {
  viewData.value.row = { ...row }
  viewData.value.list = [
    {
      prop: 'orderId',
      label: '订单ID',
    },
    {
      prop: 'userName',
      label: '用户名',
    },
    {
      prop: 'userId',
      label: '用户账号',
    },
    {
      prop: 'dishName',
      label: '菜品名称',
    },
    {
      prop: 'canteenName',
      label: '食堂名称',
    },
    {
      prop: 'restaurantName',
      label: '档口名称',
    },
    {
      prop: 'status',
      label: '订单状态',
    },
  ]
  visible1.value = true;
};

// 删除相关
const handleDelete = (row: Adorder) => {
  ElMessage.success('删除成功');
}
</script>

<style scoped></style>