<template>
  <div>
    <TableSearch :query="query" :options="searchOpt" :search="handleSearch" />
    <div class="container">

      <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                   :delFunc="handleDelete" :refresh="getData" :currentPage="page.index" :changePage="changePage" :editFunc="handleEdit">
        <template #status="{ rows }">
          <el-tag type="success" v-if="rows.status">活跃</el-tag>
          <el-tag type="danger" v-else>禁用</el-tag>
        </template>
        <template #permissions="{ rows }">
          <el-button type="primary" size="small" plain @click="handlePermission(rows)">管理</el-button>
        </template>
      </TableCustom>
    </div>
    <el-dialog :title="isEdit ? '编辑' : '新增'" v-model="visible" width="700px" destroy-on-close
               :close-on-click-modal="false" @close="closeDialog">
      <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData" />
    </el-dialog>
    <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
      <TableDetail :data="viewData">
        <template #status="{ rows }">
          <el-tag type="success" v-if="rows.status">启用</el-tag>
          <el-tag type="danger" v-else>禁用</el-tag>
        </template>
      </TableDetail>
    </el-dialog>
    <el-dialog title="权限管理" v-model="visible2" width="500px" destroy-on-close>
      <RolePermission :permiss-options="permissOptions" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="system-role">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Role } from '@/types/role';
import {fetchMenusData, fetchMenusDataFiltered, fetchRoleData, fetchRoleDataFiltered, sendMessageToUser} from '@/api';
import TableCustom from '@/components/table-user.vue';
import TableDetail from '@/components/table-detail.vue';
import RolePermission from './role-permission.vue'
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import TableEdit from "@/components/table-user-edit.vue";

interface userId {
  userId: string;
}
// 查询相关
const query = reactive({
  userId: '',
});
const searchOpt = ref<FormOptionList[]>([
  { type: 'input', label: '用户账号：', prop: 'userId' }
])
const handleSearch = () => {
  getData(page.index);
};

// 表格相关
let columns = ref([
  { type: 'index', label: '序号', width: 55, align: 'center' },
  { prop: 'userName', label: '用户名称' },
  { prop: 'userId', label: '用户账号' },
  { prop: 'userFigure', label: '用户头像' },
  { prop: 'operator', label: '操作', width: 250 },
])
const page = reactive({
  index: 1,
  size: 10,
  total: 0,
})
const tableData = ref<Role[]>([]);
// 请求页面表单数据
const getData = async (index: number) => {
  const input = query.userId
  let res;
  if(input == ''){
    // 如果搜索框中没有内容，则显示全部帖子
    res = await fetchRoleData();
  } else {
    // 如果搜索框中有内容，则会根据搜索框来设置数据
    const param: userId = {
      userId: input
    }
    console.log(param)
    res = await fetchRoleDataFiltered(param);
  }
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

const changePage = (val: number) => {
  page.index = val;
  getData(page.index);
};

// 新增/编辑弹窗相关
const options = ref<FormOption>({
  labelWidth: '120px',
  span: 22,
  list: [
    { type: 'textarea', label: '消息/提示内容', prop: '', placeholder: '输入您要向该用户发送的消息内容', required: true },
  ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref<Role>({password: "", permiss: [], userFigure: "", userId: "", userName: ""});
// 点击发送消息按钮触发
const handleEdit = (row: Role) => {
  rowData.value = { ...row };
  console.log(rowData.value.userId)
  isEdit.value = true;
  visible.value = true;
};
interface messageInfo {
  id: string,
  content: string
}
// 点击发送按钮触发
const updateData = async () => {
  console.log(options.value.list[0].prop); // 输入的消息内容
  const param: messageInfo = {
    id: rowData.value.userId,
    content: options.value.list[0].prop
  }
  await sendMessageToUser(param);
  ElMessage.success("向用户" + rowData.value.userId + "发送消息成功");
  closeDialog();
  await getData(page.index);
};
const closeDialog = () => {
  visible.value = false;
  isEdit.value = false;
  rowData.value = {password: "", permiss: [], userFigure: "", userId: "", userName: ""};
};

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
  row: {},
  list: [],
  column: 1
});
const handleView = (row: Role) => {
  viewData.value.row = { ...row }
  viewData.value.list = [
    {
      prop: 'userName',
      label: '用户名称',
    },
    {
      prop: 'userId',
      label: '用户账号',
    },
    {
      prop: 'password',
      label: '用户密码',
    },
  ]
  visible1.value = true;
};

// 删除相关
const handleDelete = (row: Role) => {
  ElMessage.success('删除成功');
}


// 权限管理弹窗相关
const visible2 = ref(false);
const permissOptions = ref({})
const handlePermission = (row: Role) => {
  visible2.value = true;
  permissOptions.value = {
    // id: row.id,
    // permiss: row.permiss
  };
}
</script>

<style scoped></style>