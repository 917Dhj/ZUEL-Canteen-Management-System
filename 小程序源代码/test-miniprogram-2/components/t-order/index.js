// components/t-order/index.js
import{ getAllOrders} from '../../api/userAPI';
import{ updateOrderStatus } from '../../api/userAPI';
const app = getApp()
Component({

  properties: {
  },

  data: {
    cardList: [   
    ],
    userInfo: [],
    index: ''
  },
  lifetimes: {
    attached: function() {
      this.fetchData();
    },
  },
  methods: {
    fetchData: function() {
      this.setData({
        userInfo: app.globalData.userInfo
      })
      let data = {
        userId: this.data.userInfo.userId
      }
      getAllOrders(data).then(res =>{
      if(res.statusCode == 200){
        this.setData({
          cardList: res.data
        });
      }else{
        console.error('请求失败', res.statusCode);
      }
    })
},

showDialog(event) {  
  console.log(event)
  const index = event.currentTarget.dataset.index;
  this.setData({  
    isDialogVisible: true, // 设置弹窗为显示状态  
    index: index
  });  
},  

// 确认按钮点击事件处理函数  
onConfirm:function() {  
  const cardList = this.data.cardList;
  const index = this.data.index;
  console.log(index)
  const orderID = cardList[index].orderID;
  console.log('用户点击了确认');  
  const newStatus = '已完成';
  // 这里可以添加确认后的处理逻辑，比如向服务器发送请求等 
  this.setData({  
    isDialogVisible: false, // 设置弹窗为隐藏状态  
  });  
  let data = {
    orderId: orderID,
    newStatus: newStatus
  }
  updateOrderStatus(data).then(res => {
    if(res.statusCode == 200) {
      console.log('修改订单状态成功')
      cardList[index].status = newStatus;
      wx.reLaunch({
        url: '/pages/order/order',
      })
    } else {
      console.log('修改订单状态失败')
    }
    this.setData({
      cardList: cardList
    })
  })
  console.log(newStatus)
},  


// 取消按钮点击事件处理函数  
onCancel() {  
  console.log('用户点击了取消');  
  this.setData({  
    isDialogVisible: false, // 设置弹窗为隐藏状态 
  });  
}, 
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
  var status = event.currentTarget.dataset.status;
  getApp().globalData.id = id; 
  getApp().globalData.cant = cant;  
  getApp().globalData.rest = rest;    
  getApp().globalData.dish = dish;  
  getApp().globalData.price = price;  
  getApp().globalData.pungency = pungency;  
  getApp().globalData.season = season; 
  getApp().globalData.notes = notes;  
  getApp().globalData.status = status;
},
  // 申请售后页面
  goToRefundDetail: function(event) {   
    wx.navigateTo({  
      url: '/pages/refundApply/refundApply'  
    });
    var id = event.currentTarget.dataset.id;
    var cant = event.currentTarget.dataset.cant;
    var rest = event.currentTarget.dataset.rest;
    var dish = event.currentTarget.dataset.dish;
    var price = event.currentTarget.dataset.price;
    var pungency = event.currentTarget.dataset.pungency;
    var season = event.currentTarget.dataset.season;
    var notes = event.currentTarget.dataset.notes;
    var status = event.currentTarget.dataset.status;
    getApp().globalData.id = id; 
    getApp().globalData.cant = cant;  
    getApp().globalData.rest = rest;    
    getApp().globalData.dish = dish;  
    getApp().globalData.price = price;  
    getApp().globalData.pungency = pungency;  
    getApp().globalData.season = season; 
    getApp().globalData.notes = notes;
    getApp().globalData.status = status;  
  }
}}
)