// app.js
import { verifyLogin } from './api/userAPI'
App({
  globalData: {
    userInfo: [],
    isGuest: true,
    token: ''
  },

  onLaunch() {
    this.verifyUser();
  },

  onShow() {
    this.verifyUser();
  },

  verifyUser:function() {
    const token = wx.getStorageSync('MyToken');
    console.log(token);
    if(token) {
      this.globalData.token = token;
      // 构建对象
      let data = {
        token: this.globalData.token
      }
      verifyLogin(data).then(res => {
        if (res.statusCode == 200) {
          console.log(res)
          console.log(res.data.userId)
          this.globalData.userInfo = res.data
          this.globalData.isGuest = false
        } else {
          console.log('不存在token', res.statusCode)
        }  
      });
    } else {
      console.log('不存在token');
    }
  }
})
