Component({  
  properties: {  
    show: {  
      type: Boolean,  
      value: false,  
    },  
    imageUrl: {  
      type: String,  
      value: 'https://www.imgbb.host/images/OyvM7.jpeg', // 这里设置二维码或图片URL  
    },  
  },  
  methods: {  
    onConfirm() {  
      this.triggerEvent('confirm');
      console.log('onConfirm called');
      wx.navigateTo({
        url: '/pages/food/page1',
      })  
    },  
    onCancel() {  
      this.triggerEvent('cancel');  
    },  
  },  
});