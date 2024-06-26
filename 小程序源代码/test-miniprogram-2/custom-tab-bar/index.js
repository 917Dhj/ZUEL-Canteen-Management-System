// custom-tab-bar/index.js
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
    active:0,
    list: [
			{
				icon: 'home-o',
				text: '主页',
				url: '/pages/index/index'
			},
			{
				icon: 'apps-o',
				text: '美食',
				url: '/pages/food/food'
      },
      {
				icon: 'records-o',
				text: '我的订单',
				url: '/pages/order/order'
      },
      {
				icon: 'user-o',
				text: '我的',
				url: '/pages/user/user'
			}
		]
  },

  /**
   * 组件的方法列表
   */
  methods: {
    onChange(event) {
			this.setData({ active: event.detail });
			wx.switchTab({
				url: this.data.list[event.detail].url
			});
		},

		init() {
			const page = getCurrentPages().pop();
			this.setData({
				active: this.data.list.findIndex(item => item.url === `/${page.route}`)
			});
		}
  }
})