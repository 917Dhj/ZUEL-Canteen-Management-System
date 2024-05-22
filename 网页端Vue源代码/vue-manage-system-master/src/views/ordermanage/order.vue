<template>
  <div>
    <div class="container">
<!--      引用自定义组件order-table-->
      <OrderTable :columns="columns" :tableData="tableData" :total="page.total"
                   :delFunc="handleDelete" :editFunc="handleEdit" :refresh="getData" :currentPage="page.index"
                   :changePage="changePage">
<!--        自定义组件的某些部分的内容展示-->
        <template #money="{ rows }">
          ￥{{ rows.money }}
        </template>
        <template #thumb="{ rows }">
          <el-image class="table-td-thumb" :src="rows.thumb" :z-index="10" :preview-src-list="[rows.thumb]"
                    preview-teleported>
          </el-image>
        </template>
        <template #status="{ rows }">
          <el-tag :type="rows.status === '已完成' ? 'success' : 'danger'">
            {{ rows.status === '已完成' ? '已完成' : '未完成' }}
          </el-tag>
        </template>
      </OrderTable>
    </div>
    <el-dialog :title="'修改订单状态'" v-model="visible" width="700px" destroy-on-close
               :close-on-click-modal="false" @close="closeDialog">
      <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData">
        <template #thumb="{ rows }">
          <img class="table-td-thumb" :src="rows.thumb"></img>
        </template>
      </TableEdit>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="order">
import { ref, reactive } from 'vue';
import { ElMessage, } from 'element-plus';
import { CirclePlusFilled } from '@element-plus/icons-vue';
import {deleteOrder, fetchOrderTableData, updateOrderStatus} from '@/api/index';
import OrderTable from '@/components/order-table.vue';
//表格的数据类型
import { TableItem } from '@/types/table';
import { FormOption, FormOptionList } from '@/types/form-option';
import TableEdit from "@/components/table-edit.vue";


// 表格相关
let columns = ref([
  { prop: 'orderId', label: '订单号', width:80, align: 'center' },
  { prop: 'dishName', label: '商品名称' },
  { prop: 'pungencyDegree', label: '辣度' },
  { prop: 'seasoning', label: '加料' },
  { prop: 'price', label: '订单总额' },
  { prop: 'userId', label: '用户ID' },
  { prop: 'orderTime', label: '下单时间' },
  { prop: 'status', label: '订单状态' },
  { prop: 'operator', label: '操作', width: 180 },
])
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

const tableData = ref<TableItem[]>([]);
const getData = async (index: number) => {
  const res = await fetchOrderTableData(param);
  console.log(res);
  let datalist = res.data;
  const length = datalist.length;
  const i = (index-1)*10;
  const j = getLine(index, length); // 计算页尾索引
  // 将页面数据显示
  tableData.value = datalist.slice(i, j);
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

// 修改订单状态弹窗相关
let options = ref<FormOption>({
  labelWidth: '100px',
  span: 24,
  list: [
    { type: 'switch', activeText: '已完成', inactiveText: '未完成', activeValue: '已完成', inactiveValue: '制作中', label: '订单状态', prop: '', required: true },
  ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref<TableItem>({
  dishName: "",
  orderId: 0,
  orderTime: "",
  price: 0,
  pungencyDegree: "",
  seasoning: "",
  status: "",
  userId: ""
});

// 点击编辑按钮触发
const handleEdit = (row: TableItem) => {
  rowData.value = { ...row }; // 获取点击的表单行的数据
  console.log(rowData.value)
  isEdit.value = true;
  options.value.list[0].prop = rowData.value.status; // 将switch的值设为当前订单的状态值
  visible.value = true;
  console.log(options.value.list)
};

interface orderStatus {
  status: string,
  orderId: number;
}
//点击“保存”触发
const updateData = async () => {
  console.log(options.value.list)
  // 将当前订单的状态设置为选中的值
  const status = options.value.list[0].prop // 当前选中的状态值
  const param: orderStatus = {
    status: status,
    orderId: rowData.value.orderId
  }
  console.log(param);
  await updateOrderStatus(param); // 在数据库中更新订单状态
  ElMessage.success("修改订单状态成功");
  closeDialog();
  await getData(page.index);
};
//点击“关闭”
const closeDialog = () => {
  visible.value = false;
  isEdit.value = false;
};

interface orderInfo {
  orderId: number;
}
// 删除相关
const handleDelete = async (row: TableItem) => {

}
</script>

<style scoped>
.table-td-thumb {
  display: block;
  margin: auto;
  width: 50px;
  height: 40px;
}
</style>
