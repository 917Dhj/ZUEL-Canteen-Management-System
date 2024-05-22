<template>
  <div>
    <div class="container">
      <TableCustom :columns="columns" :tableData="tableData" :total="page.total"
                   :editFunc="handleEdit" :refresh="getData" :currentPage="page.index"
                   :changePage="changePage" :hasToolbar="false" :hasPagination="false">
        <template #dishName="{ rows }">
          <el-input v-if="rows.editing" v-model="rows.dishName"></el-input>
          <span v-else>{{ rows.dishName }}</span>
        </template>
        <template #price="{ rows }">
          <el-input v-if="rows.editing" v-model="rows.price"></el-input>
          <span v-else>{{ rows.price }}</span>
        </template>
        <template #operator="{ rows, index }">
          <div class="operation-buttons"> <!-- 添加一个包裹按钮的div，并应用样式 -->
            <template v-if="!rows.editing">
              <el-button type="primary" size="small" :icon="Edit" @click="handleEdit(rows)">
                编辑
              </el-button>
              <el-button type="success" size="small" :icon="Plus" @click="newProduct(rows)" :disabled="rows.dishStatus == '上架'">
                上架
              </el-button>
              <el-button type="danger" size="small" :icon="Delete" @click="deleteRow(rows)" :disabled="rows.dishStatus == '下架'">
                下架
              </el-button>
            </template>
            <template v-else>
              <el-button type="success" size="small" :icon="Select" @click="editRow(rows)">
                保存
              </el-button>
              <el-button type="danger" size="small" :icon="CloseBold" @click="rows.editing = false">
                取消
              </el-button>
            </template>
          </div>
        </template>
      </TableCustom>
    </div>
    <div class="add">
      <el-form ref="formRef" :rules="rules" :model="form" label-width="120px">
        <el-row :gutter="50">
          <el-col :span="10">
            <el-form-item label="菜品" prop="dishName">
              <el-input v-model="form.dishName"></el-input>
            </el-form-item>
            <el-form-item label="价格" prop="price">
              <el-input v-model="form.price"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item>
              <el-button type="primary" @click="onSubmit(formRef)">菜品提交</el-button>
              <el-button @click="onReset(formRef)">重置表单</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts" name="table-editor">
import {reactive, ref} from 'vue';
import {Delete, Edit, CloseBold, Select, Plus} from '@element-plus/icons-vue';
import TableCustom from '@/components/dish-table.vue';
import {fetchDishData, fetchAddData, deleteMenusData, onsaleMenusData, updateDishData} from '@/api/index';
import {ElMessage, ElMessageBox, FormInstance, type FormRules} from "element-plus";
import { DishItem} from '@/types/dish';
import OrderTable from "@/components/order-table.vue";
const rules: FormRules = {
  name: [{ required: true, message: '请输入表单名称', trigger: 'blur' }],
};

const form = reactive({
  dishName: '',
  price: '',
});

const formRef = ref<FormInstance>();

let columns = ref([
  { type: 'index', label: '序号', width: 55, align: 'center' },
  { prop: 'dishId', label: '菜品ID' },
  { prop: 'dishName', label: '菜品' },
  { prop: 'price', label: '价格' },
  { prop: 'dishStatus', label: '上架状态' },
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

const tableData = ref<DishItem[]>([]);
const getData = async (index:number) => {
  const res = await fetchDishData(param);
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

const rowData = ref({})

const handleEdit = (row) => {
  rowData.value = { ...row };
  row.editing = true;
};

interface dishInfo {
  dishId: number;
}
// 确认下架菜品时
const handleDelete = async (row: DishItem) => {
  console.log(row.dishId);
  const param: dishInfo = {
    dishId: row.dishId
  };
  await deleteMenusData(param);
  ElMessage.success('下架菜品成功');
  await getData(page.index);
};

// 确认上架菜品
const handleOnSaleDish = async (row: DishItem) => {
  console.log(row.dishId);
  const param: dishInfo = {
    dishId: row.dishId
  };
  await onsaleMenusData(param);
  ElMessage.success('上架菜品成功');
  await getData(page.index);
}
// 翻页
const changePage = (val: number) => {
  page.index = val;
  getData(page.index);
};

// 点击上架按钮触发
const newProduct = (row) => {
  ElMessageBox.confirm('确定要上架吗？', '提示', {
    type: 'warning'
  })
      .then(async () => {
        // 当点击确认时
        await handleOnSaleDish(row);
      })
      .catch(() => { });
}

// 点击下架按钮触发
const deleteRow = (row) => {
  ElMessageBox.confirm('确定要下架吗？', '提示', {
    type: 'warning'
  })
      .then(async () => {
        // 当点击确认时
        await handleDelete(row);
      })
      .catch(() => { });
};
// 点击保存修改表单
const editRow = (row) => {
  ElMessageBox.confirm('确定要修改菜品吗？', '提示', {
    type: 'warning'
  })
      .then(async () => {
        // 当点击确认时
        await confirmEdit(row);
      })
      .catch(() => { });
}
interface dishUpdate{
  dishId: number,
  dishName: string,
  price: number;
}
// 确认修改表单
const confirmEdit = async (row: DishItem) => {
  console.log(row.dishId);
  const param: dishUpdate = {
    dishId: row.dishId,
    dishName: row.dishName,
    price: row.price
  };
  await updateDishData(param);
  ElMessage.success('修改菜品成功');
  await getData(page.index);
}

// 控制弹窗显示与隐藏的变量
const showAddDialog = ref(false);
// 新增菜品的表单数据
const newDishForm = ref(null);

// 弹窗关闭前的处理函数，可根据需要进行表单验证等操作
function handleClose(done) {
  // 这里可以进行表单验证等操作
  done();
}

const generateData = () => {
  const data = []
  for (let i = 1; i <= 15; i++) {
    data.push({
      key: i,
      label: `Option ${i}`,
      disabled: i % 4 === 0,
    })
  }
  return data
}

const transferData = ref(generateData())
interface addDish {
  restaurantId: number,
  dishName: string,
  price: string
}
// 提交
const onSubmit = (formEl: FormInstance | undefined) => {
  // 表单校验
  if (!formEl) return;
  formEl.validate(async (valid) => {
    if (valid) {
      // 如果表单没有漏填
      try {
        let param: addDish = {
          restaurantId: parseInt(restaurantId),
          dishName: form.dishName,
          price: form.price,
        }
        console.log(param);
        // 调用 fetchAddData 函数，并传递表单数据
        await fetchAddData(param);
        ElMessage.success('提交成功！');
        // 可以选择清空表单或者进行其他操作
        form.dishName = '';
        form.price = '';
        await getData(page.index); // 刷新页面数据
      } catch (error) {
        ElMessage.error('提交失败！');
        console.error(error);
      }
    } else {
      return false;
    }
  });
};
// 重置
const onReset = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
};

</script>

<style scoped>
.operation-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.add{
  margin-top:30px;
}
</style>
