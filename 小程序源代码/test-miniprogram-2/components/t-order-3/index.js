// components/t-order-3/index.js
import{getEvaluation} from '../../api/userAPI';
const app = getApp()
Component({
  
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    // 卡片中的数据列表，列表的长度就是卡片的数量
    cardList: [
      
    ],
    userInfo: []
  },

  // 组件的生命周期
  lifetimes: {
    attached: function() {
      this.fetchData();
    },
  },

  /**
   * 组件的方法列表
   */
  methods: {
    fetchData: function() {
      this.setData({
        userInfo: app.globalData.userInfo
      })
      let data = {
        userId: this.data.userInfo.userId
      }
      getEvaluation(data).then(res =>{
      if(res.statusCode == 200){
        //在请求成功时更新页面数据
        this.setData({
          cardList: res.data  //保存后端返回的数据
        });
      }else{
        console.error('请求失败',error);
      }
    })
  },
  //订单详情页面
  navigateToOrderDetail:function(event) {    
    console.log('navigateToOrderDetail 被调用了');  
    wx.navigateTo({  
      url: '/pages/orderDetail/orderDetail',  
    });  
    var id = event.currentTarget.dataset.id;
    var cant = event.currentTarget.dataset.cant;
    var rest = event.currentTarget.dataset.rest;
    var dish = event.currentTarget.dataset.dish;
    var price = event.currentTarget.dataset.price;
    var pungency = event.currentTarget.dataset.pungency;
    var season = event.currentTarget.dataset.season;
    var notes = event.currentTarget.dataset.notes;
    getApp().globalData.id = id; 
    getApp().globalData.cant = cant;  
    getApp().globalData.rest = rest;    
    getApp().globalData.dish = dish;  
    getApp().globalData.price = price;  
    getApp().globalData.pungency = pungency;  
    getApp().globalData.season = season; 
    getApp().globalData.notes = notes;  
},

 // 点击评价按钮触发
  onEvaluateTap(event) {    
    //console.log('onEvaluateTap 被调用了');  
    const index = event.currentTarget.dataset.index;
      console.log('index:',index);
      const cardList = this.data.cardList;
      const orderId = cardList[index].orderID;
      console.log('点击的orderId:',orderId);
      wx.navigateTo({
        url: '/pages/editPost/editPost?data=' + orderId,
      }) 
  },
  }
})