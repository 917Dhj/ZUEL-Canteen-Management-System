<template>
  <div>
    <div class="container">
      <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                   :delFunc="handleDelete" :agreeFunc="handleAgree" :editFunc="handleEdit" :refresh="getData" :currentPage="page.index"
                   :changePage="changePage" :disabled="disabled">
        <template #userID="{ rows }">
          <el-input v-if="rows.editing" v-model="rows.userID"></el-input>
          <span v-else>{{ rows.userID }}</span>
        </template>
        <template #orderID="{ rows }">
          <el-input v-if="rows.editing" v-model="rows.orderID"></el-input>
          <span v-else>{{ rows.orderID }}</span>
        </template>
        <template #dishName="{ rows }">
          <el-input v-if="rows.editing" v-model="rows.dishName"></el-input>
          <span v-else>{{ rows.dishName }}</span>
        </template>
        <template #price="{ rows }">
          <el-input v-if="rows.editing" v-model="rows.price"></el-input>
          <span v-else>{{ rows.price }}</span>
        </template>
        <template #aftersaleStatus="{ rows }">
          <el-input v-if="rows.editing" v-model="rows.aftersaleStatus"></el-input>
          <span v-else>{{ rows.aftersaleStatus }}</span>
        </template>
        <template #audit="{ rows }">
          <el-tag :type="rows.audit === '同意' ? 'success' : (rows.audit === '拒绝' ? 'danger' : 'info')">
            {{ rows.audit === '同意' ? '同意' : (rows.audit === '拒绝' ? '拒绝' : '待审核') }}
          </el-tag>
        </template>
      </TableCustom>

    </div>
    <el-dialog title="编辑" v-model="dialogVisible" width="50%" @close="closeDialog">
      <el-form :model="editForm" ref="editFormRef">
        <el-form-item label="审核">
          <el-switch v-model="editForm.audit"></el-switch>
        </el-form-item>
        <span slot="footer" class="dialog-footer">
          <el-button @click="closeDialog">取 消</el-button>
          <el-button type="primary" @click="saveEdit">确 定</el-button>
        </span>
      </el-form>
    </el-dialog>
    <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
      <TableDetail :data="viewData">
        <template #thumb="{ rows }">
          <el-image :src="rows.thumb"></el-image>
        </template>
      </TableDetail>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="after">
import { ref, reactive } from 'vue';
import { ElMessage, } from 'element-plus';
import {agreeAftersale, disagreeAftersale, fetchAfterData} from '@/api/index';
import TableCustom from '@/components/after-table.vue';
import TableDetail from '@/components/table-detail.vue';
import { AfterTableItem } from '@/types/after';

// 表格相关
let columns = ref([
  { prop: 'userId', label: '用户ID' },
  { prop: 'orderId', label: '订单号' },
  { prop: 'dishName', label: '菜品信息' },
  { prop: 'refundPrice', label: '退款金额' },
  { prop: 'reason', label: '售后原因' },
  { prop: 'audit', label: '审核' },
  { prop: 'operator', label: '操作', width: 200 },
])
const page = reactive({
  index: 1,
  size: 10,
  total: 0,
})


let disabled = false;

interface userInfo {
  restaurantId: string;
}
const restaurantId = localStorage.getItem('vuems_name');
console.log(restaurantId);
const param: userInfo = {
  restaurantId: restaurantId
};
// 获取表中的数据
const tableData = ref<AfterTableItem[]>([]);
const getData = async (index: number) => {
  const res = await fetchAfterData(param);
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

// 翻页相关
const changePage = (val: number) => {
  page.index = val;
  getData(page.index);
};

//修改 弹窗相关
const dialogVisible = ref(false);
const editForm = reactive({ /* 初始表单数据 */ id: null, audit: true,});
const handleEdit = (row: AfterTableItem) => {
  // editForm.id = row.id;
  Object.assign(editForm, row); // 复制行数据到表单模型（可能需要排除某些字段）
  dialogVisible.value = true;
};
const saveEdit = async () => {
  try {
    // await updateData(editForm); // 假设的API函数用于更新数据
    ElMessage.success('编辑成功');
    dialogVisible.value = false;
    // 可能需要重新获取数据
    // getData();
  } catch (error) {
    console.error(error);
    ElMessage.error('编辑失败');
  }
};
const closeDialog = () => {
  dialogVisible.value = false;
  // 重置表单（如果需要）
  // editForm.id = null;
  // editForm.audit = false;
  // ...重置其他字段
};

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
  row: {},
  list: []
});
const handleView = (row: AfterTableItem) => {
  viewData.value.row = { ...row }
  viewData.value.list = [
    {
      prop: 'userId',
      label: '用户ID',
    },
    {
      prop: 'orderId',
      label: '订单编号',
    },
    {
      prop: 'dishName',
      label: '餐品信息',
    },
    {
      prop: 'refundPrice',
      label: '退款金额',
    },
    {
      prop: 'reason',
      label: '售后原因',
    },
    {
      prop: 'audit',
      label: '审核',
    },
  ]
  visible1.value = true;
};

interface orderInfo {
  orderId: number;
}
// 拒绝相关
const handleDelete = async (row: AfterTableItem) => {
  console.log(row.orderId);
  const param: orderInfo = {
    orderId: row.orderId
  };
  await disagreeAftersale(param)// 从数据库中修改这一订单
  ElMessage.success('拒绝售后申请成功');
  await getData(page.index);
}

// 点击同意
const handleAgree = async (row: AfterTableItem) => {
  console.log(row.orderId);
  const param: orderInfo = {
    orderId: row.orderId
  };
  await agreeAftersale(param);
  ElMessage.success('同意售后申请成功');
  await getData(page.index);
}

</script>

<style scoped>
</style>
