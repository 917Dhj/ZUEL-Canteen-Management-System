<template>
    <div>
        <div class="user-container">
            <el-card class="user-profile" shadow="hover" :body-style="{ padding: '0px' }">
                <div class="user-profile-bg"></div>
                <div class="user-avatar-wrap">
                    <el-avatar class="user-avatar" :size="120" :src="avatarImg" />
                </div>
                <div class="user-name">{{ restaurantName }}</div>
            </el-card>
            <el-card
                class="user-content"
                shadow="hover"
                :body-style="{ padding: '20px 50px', height: '100%', boxSizing: 'border-box' }"
            >
                <el-tabs tab-position="left" v-model="activeName">
                    <el-tab-pane name="label1" label="消息通知" class="user-tabpane">
                        <TabsComp />
                    </el-tab-pane>
                  <el-tab-pane name="label3" label="修改信息" class="user-tabpane">
                    <el-form class="w500" label-position="top" :model="form" :rules="rules" ref="submit">
                      <el-form-item label="你的档口名称：" prop="name">
                        <el-input type="name" v-model="form.name"></el-input>
                      </el-form-item>
                      <el-form-item label="旧密码：" prop="old">
                        <el-input type="password" v-model="form.old"></el-input>
                      </el-form-item>
                      <el-form-item label="新密码：" prop="new">
                        <el-input type="password" v-model="form.new"></el-input>
                      </el-form-item>
                      <el-form-item label="确认新密码：" prop="new1">
                        <el-input type="password" v-model="form.new1"></el-input>
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary" @click="onSubmit(submit)">保存</el-button>
                      </el-form-item>
                    </el-form>
                  </el-tab-pane>
                  <el-tab-pane name="label2" label="我的头像" class="user-tabpane">
                        <div class="crop-wrap" v-if="activeName === 'label2'">
                            <vueCropper
                                ref="cropper"
                                :img="imgSrc"
                                :autoCrop="true"
                                :centerBox="true"
                                :full="true"
                                mode="contain"
                            >
                            </vueCropper>
                        </div>
                        <el-button class="crop-demo-btn" type="primary"
                            >选择图片
                            <input class="crop-input" type="file" name="image" accept="image/*" @change="setImage" />
                        </el-button>
                        <el-button type="success" @click="saveAvatar">上传并保存</el-button>
                    </el-tab-pane>
                </el-tabs>
            </el-card>
        </div>
    </div>
</template>

<script setup lang="ts" name="ucenter">
import { reactive, ref } from 'vue';
import { VueCropper } from 'vue-cropper';
import 'vue-cropper/dist/index.css';
import TabsComp from './element/tabs.vue';
import {ElMessage, ElMessageBox, type FormInstance, type FormRules} from "element-plus";
import {updateLoginInfo, updateRestaurantImage} from "@/api";

const restaurantId = localStorage.getItem('vuems_name');
const restaurantName = localStorage.getItem('restaurantName');
const form = reactive({
    name: restaurantName,
    new1: '',
    new: '',
    old: '',
});
const rules: FormRules = {
  name: [
    {
      required: true,
      message: '请输入档口名称',
      trigger: 'submit',
    },
  ],
  old: [{ required: true, message: '请输入旧密码', trigger: 'submit' }],
  new: [{ required: true, message: '请输入新密码', trigger: 'submit' }],
  new1: [{ required: true, message: '请确认新密码', trigger: 'submit' }],
};
const submit = ref<FormInstance>();
// 点击保存按钮触发
const onSubmit = (formEl: FormInstance | undefined) => {
  console.log(formEl);
  if (!formEl) return;
  formEl.validate(async (valid: boolean) => {
    if (valid) {
      // 表单信息填写完整
      ElMessageBox.confirm('确定保存修改吗？', '提示', {
        type: 'warning'
      })
          .then(async () => {
            await updateUserInfo();
          })
          .catch(() => { });
    } else {
      ElMessage.error("表单信息填写不完整")
    }
  })
};
interface updateUserInfo {
  restaurantId: string,
  restaurantName: string,
  password: string,
}
// 保存修改商家账号信息
const updateUserInfo = async () => {
  console.log(form)
  const oldPassword = localStorage.getItem("password"); // 获取用户的旧密码
  if (oldPassword === form.old) {
    // 旧密码输入正确
    if (form.new === form.new1) {
      // 确认密码与新密码相同
      const param: updateUserInfo = {
        restaurantId: restaurantId,
        restaurantName: form.name,
        password: form.new1,
      }
      await updateLoginInfo(param); // 将修改的信息发送到数据库
      ElMessage.success("修改账户信息成功");
      // 清空表单
      form.old = '';
      form.new = '';
      form.new1 = '';
    } else {
      ElMessage.error("确认新密码必须与新密码相同")
    }
  } else {
    ElMessage.error("旧密码输入错误")
  }
}

const activeName = ref('label1');

const avatarImg = ref(localStorage.getItem('restaurantImage'));
const imgSrc = ref(avatarImg.value);
const cropImg = ref('');
const cropper: any = ref();

const setImage = (e: any) => {
    const file = e.target.files[0];
    if (!file.type.includes('image/')) {
        return;
    }
    const reader = new FileReader();
    reader.onload = (event: any) => {
        imgSrc.value = event.target.result;
        // cropper.value && cropper.value.replace(event.target.result);
    };
    reader.readAsDataURL(file);
};

const cropImage = () => {
    cropImg.value = cropper.value?.getCroppedCanvas().toDataURL();
};

// 点击保存头像
const saveAvatar = () => {
  // avatarImg.value = cropImg.value;
  ElMessageBox.confirm('确定修改头像吗？', '提示', {
    type: 'warning'
  })
      .then(async () => {
        await upLoadImage();
      })
      .catch(() => { });

};
interface restaurantImage {
  image: string,
  restaurantId: string
}
// 上传图片到服务器
const upLoadImage = async () => {
  ElMessage.info("上传头像可能需要一些时间，请耐心等待");
  const image64 = imgSrc.value.split(',')[1]; // image64是上传的新图片的base64编码格式照片文件，用split去掉其头部
  console.log(image64);
  const param: restaurantImage = {
    image: image64,
    restaurantId: restaurantId,
  }
  const res = await updateRestaurantImage(param);
  ElMessage.success("修改新头像成功")
  console.log(res);
  avatarImg.value = res.data; // 更改页面中的头像为新头像
  localStorage.setItem('restaurantImage', res.data);
}
</script>

<style scoped>
.user-container {

    width: 100%;
}

.user-profile {
    position:relative;
    width: 100%;
    margin-right: 20px;
    flex: 0 0 auto;
    align-self: flex-start;
}

.user-profile-bg {
    width: 100%;
    height: 200px;
    background-image: url('../assets/img/ucenter-bg1.jpg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
}

.user-avatar-wrap {
    position: absolute;
    top: 50px;
    width: 100%;
    text-align: center;
}

.user-avatar {
    border: 5px solid #fff;
    border-radius: 50%;
    overflow: hidden;
    box-shadow: 0 7px 12px 0 rgba(62, 57, 107, 0.16);
}

.user-name {
  text-align: center;
  margin: 0 0 20px;
  font-size: 22px;
  font-weight: 500;
}

.info-icon i {
    font-size: 30px;
    margin: 0 10px;
    color: #343434;
}

.user-content {
    width: 100%;
    height: auto;
    position: relative;
}

.user-tabpane {
    padding: 10px 20px;
}

.crop-wrap {
    width: 100px;
    height: 100px;
    margin-bottom: 20px;
}

.crop-demo-btn {
    position: relative;
}

.crop-input {
    position: absolute;
    width: 100px;
    height: 40px;
    left: 0;
    top: 0;
    opacity: 0;
    cursor: pointer;
}

.w500 {
    width: 500px;
}

.user-footer {
    display: flex;
    border-top: 1px solid rgba(83, 70, 134, 0.1);
}

.user-footer-item {
    padding: 20px 0;
    width: 33.3333333333%;
    text-align: center;
}

.el_card-body{
    width: 100%;
}

.user-footer > div + div {
    border-left: 1px solid rgba(83, 70, 134, 0.1);
}
</style>

<style>
.el-tabs.el-tabs--left {
    height: 100%;
}
</style>
