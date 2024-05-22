<template>
  <div>
    <TableSearch :query="query" :options="searchOpt" :search="handleSearch" />
    <div class="container">
      <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                   :delFunc="handleDelete" :refresh="getData" :currentPage="page.index" :changePage="changePage" :editFunc="handleEdit">
        <template #toolbarBtn>
          <el-button type="warning" :icon="CirclePlusFilled" @click="visible2 = true">新增商家</el-button>
          <text class="tips">Tips:只有拥有至少一个已上架菜品的商家才会出现在商家管理表中</text>
        </template>
      </TableCustom>

    </div>
    <el-dialog :title="isEdit ? '编辑' : '新增'" v-model="visible" width="700px" destroy-on-close
               :close-on-click-modal="false" @close="closeDialog">
      <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData" />
    </el-dialog>
    <el-dialog :title="isEdit2 ? '编辑' : '新增'" v-model="visible2" width="700px" destroy-on-close
               :close-on-click-modal="false" @close="closeDialog2">
      <TableEdit2 :form-data="rowData" :options="options2" :edit="isEdit2" :update="updateData2" />
    </el-dialog>
    <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
      <TableDetail :data="viewData"></TableDetail>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="system-user">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { User } from '@/types/user';
import {
  addNewRestaurant,
  fetchMenusData,
  fetchMenusDataFiltered,
  fetchUserData,
  fetchUserDataFiltered,
  sendMessageToRestaurant
} from '@/api';
import TableCustom from '@/components/table-user.vue';
import TableDetail from '@/components/table-detail.vue';
import TableSearch from '@/components/table-search.vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import OrderTable from "@/components/order-table.vue";
import TableEdit from "@/components/table-user-edit.vue";
import TableEdit2 from "@/components/table-user-edit2.vue";

interface restaurantName {
  restaurantName: string;
}

// 查询相关
const query = reactive({
  restaurantName: '',
});
const searchOpt = ref<FormOptionList[]>([
  { type: 'input', label: '档口名称：', prop: 'restaurantName' }
])
const handleSearch = () => {
  getData(page.index)
};

// 表格相关
let columns = ref([
  { type: 'index', label: '序号', width: 55, align: 'center' },
  { prop: 'canteenId', label: '食堂ID' },
  { prop: 'canteenName', label: '食堂名称' },
  { prop: 'restaurantName', label: '档口名称' },
  { prop: 'restaurantId', label: '档口ID' },
  { prop: 'operator', label: '操作', width: 250 },
])
const page = reactive({
  index: 1,
  size: 10,
  total: 0,
})
const tableData = ref<User[]>([]);
// 获取页面表单数据
const getData = async (index:number) => {
  const input = query.restaurantName
  let res;
  if(input == ''){
    // 如果搜索框中没有内容，则显示全部帖子
    res = await fetchUserData();
  } else {
    // 如果搜索框中有内容，则会根据搜索框来设置数据
    const param: restaurantName = {
      restaurantName: input
    }
    res = await fetchUserDataFiltered(param);
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
  console.log(page.index);
  getData(page.index);
};

// 新增/编辑弹窗相关
let options = ref<FormOption>({
  labelWidth: '120px',
  span: 22,
  list: [
    { type: 'textarea', label: '消息/提示内容', prop: '', placeholder: '输入您要向该商家发送的消息内容', required: true },
  ]
})
let options2 = ref<FormOption>({
  labelWidth: '100px',
  span: 22,
  list: [
    { type: 'input', label: '食堂名称', prop: '', placeholder: '新增的商家所在的食堂', required: true },
    { type: 'input', label: '档口名称', prop: '', placeholder: '新增的商家名称', required: true },
  ]
})
const visible = ref(false);
const visible2 = ref(false);
const isEdit = ref(false);
const isEdit2 = ref(false);
const rowData = ref<User>({
  canteenId: 0,
  canteenName: "",
  dishCount: "",
  password: "",
  restaurantId: 0,
  restaurantName: ""
});
// 点击发送消息按钮
const handleEdit = (row: User) => {
  rowData.value = { ...row };
  console.log(rowData.value.restaurantId);
  isEdit.value = true;
  visible.value = true;
};
interface messageInfo {
  id: string,
  content: string
}

// 点击发送按钮
const updateData = async () => {
  console.log(options.value.list[0].prop); // 输入的消息内容
  const param: messageInfo = {
    id: rowData.value.restaurantId.toString(),
    content: options.value.list[0].prop
  }
  console.log(param);
  await sendMessageToRestaurant(param);
  ElMessage.success("向商家" + rowData.value.restaurantName + "发送消息成功");
  closeDialog();
  await getData(page.index);
};
interface addRestaurant {
  canteenId: string,
  restaurantName: string,
}

// 点击确认新增商家按钮
const updateData2 = async () => {
  const canteenName = options2.value.list[0].prop;
  const restaurantName = options2.value.list[1].prop;
  if (canteenName !== '' && restaurantName !== '') {
    // 如果表单信息填写完整
    console.log((canteenName));
    console.log(restaurantName);
    let canteenId;
    switch (canteenName) {
      case '环湖园': canteenId = "1"; break;
      case '望湖园': canteenId = "2"; break;
      case '云湖园': canteenId = "3"; break;
      case '晓湖园': canteenId = "4"; break;
      case '滨湖园': canteenId = "5"; break;
      default: ElMessage.error("您输入的食堂名称不存在"); break;
    }
    const param:addRestaurant = {
      canteenId: canteenId,
      restaurantName: restaurantName,
    }
    console.log(param);
    await addNewRestaurant(param); // 在数据库中新增商家
    ElMessage.success("在食堂"+canteenName+"新增商家"+restaurantName+"成功")
    closeDialog2();
    await getData(page.index);
  } else {
    ElMessage.error("表单信息填写不完整");
  }

};

const closeDialog = () => {
  visible.value = false;
  isEdit.value = false;
};

const closeDialog2 = () => {
  visible2.value = false;
  isEdit2.value = false;
};

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
  row: {},
  list: []
});
const handleView = (row: User) => {
  viewData.value.row = { ...row }
  viewData.value.list = [
    {
      prop: 'canteenName',
      label: '食堂名称',
    },
    {
      prop: 'restaurantName',
      label: '商家名称',
    },
    {
      prop: 'restaurantId',
      label: '商家账号',
    },
    {
      prop: 'password',
      label: '密码',
    },
    {
      prop: 'dishCount',
      label: '菜品数量',
    },
  ]
  visible1.value = true;
};

// 删除相关
const handleDelete = (row: User) => {
  ElMessage.success('删除成功');
}
</script>

<style scoped>
.tips {
  color: #d9534f;
  font-size: small;
  margin-left: 10px;
}
</style>