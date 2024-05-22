<template>
  <div class="login-bg">
    <div class="login-container">
      <div class="login-header">
        <img class="logo mr10" src="../../assets/img/logo.svg" alt="" />
        <div class="login-title">中南大饭圈后台管理系统</div>
      </div>
      <el-form :model="param" :rules="rules" ref="login" size="large">
        <el-form-item prop="username">
          <el-input v-model="param.username" placeholder="管理员/商家账号">
            <template #prepend>
              <el-icon>
                <User />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              type="password"
              placeholder="密码"
              v-model="param.password"
              @keyup.enter="submitForm(login)"
          >
            <template #prepend>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <div class="pwd-tips">
          <el-checkbox class="pwd-checkbox" v-model="checked" label="记住密码" />
<!--          <el-link type="primary" @click="$router.push('/reset-pwd')">忘记密码</el-link>-->
        </div>
        <el-button class="login-btn" type="primary" size="large" @click="submitForm(login)">登录</el-button>
        <p class="login-tips">Tips : 管理员账号为admin</p>
<!--        <p class="login-text">-->
<!--          没有账号？<el-link type="primary" @click="$router.push('/register')">立即注册</el-link>-->
<!--        </p>-->
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useTabsStore } from '@/store/tabs';
import { usePermissStore } from '@/store/permiss';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import {confirmLogin, getRestaurantInfo} from '@/api';
import type { FormInstance, FormRules } from 'element-plus';

interface LoginInfo {
  username: string;
  password: string;
}

const lgStr = localStorage.getItem('login-param');
const defParam = lgStr ? JSON.parse(lgStr) : null;
const checked = ref(lgStr ? true : false);

const router = useRouter();
const param = reactive<LoginInfo>({
  username: defParam ? defParam.username : '',
  password: defParam ? defParam.password : '',
});

const rules: FormRules = {
  username: [
    {
      required: true,
      message: '账号不能为空',
      trigger: 'blur',
    },
  ],
  password: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
};
const permiss = usePermissStore();
const login = ref<FormInstance>();
const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.validate(async (valid: boolean) => {
    if (valid) {
      // 表单中的每一项都填写了
      if (param.username !== 'admin') {
        // 如果登录的是商家账号
        await getLoginInfo();
      } else {
        // 如果登录的是管理员账号,且账号输入正确
        if (param.password == '123456') {
          // 如果管理员密码输入正确
          ElMessage.success('管理员登录成功');
          localStorage.setItem('vuems_name', param.username);
          localStorage.setItem('restaurantName', "管理员");
          localStorage.setItem('restaurantImage', "https://tdesign.gtimg.com/mobile/demos/avatar1.png");
          localStorage.setItem('password', "123456");
          localStorage.setItem('loginType', "admin");
          // 根据用户输入的用户名来获取相应的权限列表，最后将这个权限列表储存到全局store的key中，供整个程序访问
          const keys = permiss.defaultList[param.username == 'admin' ? 'admin' : 'user'];
          permiss.handleSet(keys);
          await router.push('/');
          // 检查用户是否选择了记住账号密码，如果是的话将账号密码存入存储中
          if (checked.value) {
            localStorage.setItem('login-param', JSON.stringify(param));
          } else {
            localStorage.removeItem('login-param');
          }
        } else {
          // 如果密码输入不正确
          ElMessage.error('管理员密码输入错误')
        }
      }
    } else {
      // 假如表单中有空的
      ElMessage.error('用户名或密码填写不完整');
      return false;
    }
  });
};

interface userInfo {
  restaurantId: string;
}
// 检验登录信息是否正确
const getLoginInfo = async () => {
  confirmLogin(param).then(async res => {
    console.log(res);
    if (res.status == 200) {
      ElMessage.success('商家登录成功');
      localStorage.setItem('vuems_name', param.username);
      const data:userInfo = {
        restaurantId: param.username
      }
      const res = await getRestaurantInfo(data);
      console.log(res);
      localStorage.setItem('restaurantName', res.data.restaurantName);
      localStorage.setItem('canteenId', res.data.canteenId);
      localStorage.setItem('restaurantImage', res.data.restaurantImage);
      localStorage.setItem('password', param.password);
      localStorage.setItem('loginType', "user");
      // 根据用户输入的用户名来获取相应的权限列表，最后将这个权限列表储存到全局store的key中，供整个程序访问
      const keys = permiss.defaultList[param.username == 'admin' ? 'admin' : 'user'];
      permiss.handleSet(keys);
      await router.push('/');
      // 检查用户是否选择了记住账号密码，如果是的话将账号密码存入存储中
      if (checked.value) {
        localStorage.setItem('login-param', JSON.stringify(param));
      } else {
        localStorage.removeItem('login-param');
      }
    } else {
      console.error('登录出错');
      ElMessage.error('出错了，请稍后再试');
    }
  }).catch(error => {
    console.error('登录出错:', error);
    ElMessage.error('出错了，请稍后再试');
  });
}

const tabs = useTabsStore();
tabs.clearTabs();
</script>

<style scoped>
.login-bg {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100vh;
  background: url(../../assets/img/login-bg.jpg) center/cover no-repeat;
}

.login-header {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 40px;
}

.logo {
  width: 35px;
}

.login-title {
  font-size: 22px;
  color: #333;
  font-weight: bold;
}

.login-container {
  width: 450px;
  border-radius: 5px;
  background: #fff;
  padding: 40px 50px 50px;
  box-sizing: border-box;
}

.pwd-tips {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  margin: -10px 0 10px;
  color: #787878;
}

.pwd-checkbox {
  height: auto;
}

.login-btn {
  display: block;
  width: 100%;
}

.login-tips {
  font-size: 12px;
  color: #999;
}

.login-text {
  display: flex;
  align-items: center;
  margin-top: 20px;
  font-size: 14px;
  color: #787878;
}
</style>
