// components/t-order-2/index.js
import{ getAfterSale} from '../../api/userAPI';
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
      getAfterSale(data).then(res =>{
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
  // 售后详情页面
goToRefundDetail: function(event) {   
  wx.navigateTo({  
    url: '/pages/refundDetail/refundDetail'  
  });
  var price = event.currentTarget.dataset.price;
  var dish = event.currentTarget.dataset.dish;
  var id = event.currentTarget.dataset.id;
  getApp().globalData.price = price;   // 存入全局变量  
  getApp().globalData.dish = dish;   // 存入全局变量  
  getApp().globalData.id = id;   // 存入全局变量  
} 
}})