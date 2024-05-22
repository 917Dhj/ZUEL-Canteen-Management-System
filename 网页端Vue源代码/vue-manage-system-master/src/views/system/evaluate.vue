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

<script setup lang="ts" name="system-evaluate">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { Evaluate } from '@/types/evaluate';
import {deleteEvaluateData, fetchEvaluateData, fetchEvaluateDataFiltered} from '@/api';
import TableCustom from '@/components/table-evaluate.vue';
import TableDetail from '@/components/table-detail.vue';
import TableSearch from '@/components/table-search.vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import TableEdit from "@/components/table-edit.vue";

const tableData = ref<Evaluate[]>([]);
const page = reactive({
  index: 1,
  size: 10,
  total: 0,
})
// 查询相关
const query = reactive({
  posterId: ''
});
const searchOpt = ref<FormOptionList[]>([
  { type: 'input', label: '发帖账号：', prop: 'posterId' }
])

interface posterInfo {
  posterId: string;
}
// 点击搜索按钮触发
const handleSearch = async () => {
  await getData(page.index);
};

// 表格相关
let columns = ref([
  { type: 'index', label: '序号', width: 55, align: 'center' },
  { prop: 'posterName', label: '发帖用户名' },
  { prop: 'posterId', label: '发帖账号' },
  { prop: 'postContext', label: '点评内容' },
  { prop: 'operator', label: '操作', width: 200 },
])

// 请求页面数据
const getData = async (index:number) => {
  const input = query.posterId
  let res;
  if(input == ''){
    // 如果搜索框中没有内容，则显示全部帖子
    res = await fetchEvaluateData();
  } else {
    // 如果搜索框中有内容，则会根据搜索框来设置数据
    const param: posterInfo = {
      posterId: input
    }
    res = await fetchEvaluateDataFiltered(param);
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

// 点击切换页面触发
const changePage = (val: number) => {
  page.index = val;
  getData(page.index);
};

// 新增/编辑弹窗相关
let options = ref<FormOption>({
  labelWidth: '100px',
  span: 12,
  list: [
    { type: 'input', label: '用户名', prop: 'posterName', required: true },
    { type: 'input', label: '账号', prop: 'posterId', required: true },
    { type: 'input', label: '评价', prop: 'postContext', required: true },
  ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref({});
const handleEdit = (row: Evaluate) => {
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
const handleView = (row: Evaluate) => {
  viewData.value.row = { ...row }
  viewData.value.list = [
    {
      prop: 'posterName',
      label: '用户名',
    },
    {
      prop: 'posterId',
      label: '账号',
    },
    {
      prop: 'postContext',
      label: '评价',
    },
    {
      prop: 'postTime',
      label: '时间',
    },
    {
      prop: 'shopName',
      label: '对象',
    },
  ]
  visible1.value = true;
};


interface postInfo {
  postId: number;
}
// 删除相关
const handleDelete = async (row: Evaluate) => {
  console.log(row.postId);
  const param: postInfo = {
    postId: row.postId
  };
  await deleteEvaluateData(param); // 从数据库中将这一行的delete设为1
  ElMessage.success('删除成功');
  await getData(page.index);
}
</script>

<style scoped></style>