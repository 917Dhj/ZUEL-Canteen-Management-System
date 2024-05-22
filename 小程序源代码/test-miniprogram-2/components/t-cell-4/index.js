// components/t-cell-4/index.js
import { getFindPost, userLike, userDislike, userStar, userCancelStar } from '../../api/userAPI'
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
    userInfo: [],
    // 卡片中的数据列表，列表的长度就是卡片的数量
    cardList: [
      
    ],
    show: false
  },
  // 组件的生命周期
  lifetimes: {
    attached: function() {
      // this.fetchData();
    },
  },

  /**
   * 组件的方法列表
   */
  methods: {
    fetchData: function() {
      this.data.userInfo = app.globalData.userInfo
      const userInfo = this.data.userInfo
      console.log(userInfo)
      let data
      if(userInfo == '') {
        console.log('用户未登录')
        data = {
          userId: ''
        }
      } else {
        console.log('用户已登录', userInfo)
        data = {
          userId: userInfo.userId
        }
      }
      getFindPost(data).then(res => {
        // console.log(res.data)
        if (res.statusCode == 200) {
          // 在请求成功时更新页面数据
          this.setData({
            cardList: res.data // 将后端返回的数据保存到页面数据中
          });
        } else {
          console.error('请求失败', error);
        }
      })
    },

    // 点击点赞按钮触发
    clickLike:function(event) {
      const index = event.currentTarget.dataset.index;
      console.log(index);
      const cardList = this.data.cardList;
      const postId = cardList[index].postId;
      console.log(postId);
      const userId = this.data.userInfo.userId;
      console.log(userId);
      // 判断用户是否已经登录
      if (userId !== undefined) {
        // 用户已经登录
        let data = {
          userId: userId,
          postId: postId
        }
        if (cardList[index].icon_like == 'like-o') {
          // 点赞操作
          cardList[index].icon_like = 'like'; // 点击后将爱心图标变为实心爱心:点赞
          cardList[index].likesCount += 1 // 点赞数+1
          // 向数据库中添加用户的点赞记录
          userLike(data).then(res => {
            if (res.statusCode == 200) {
              console.log('点赞成功')
            } else {
              console.log('点赞失败')
            }
          })
        } else {
          // 取消点赞操作
          cardList[index].icon_like = 'like-o'; // 取消点赞
          cardList[index].likesCount -= 1 // 点赞数-1
          // 从数据库中删除用户的点赞记录
          userDislike(data).then(res => {
            if (res.statusCode == 200) {
              console.log('取消点赞成功')
            } else {
              console.log('取消点赞失败')
            }
          })
        }
        // console.log(cardList)
        // 把图标的更改显示在页面上
        this.setData({ cardList: cardList });
      } else {
        // 用户还未登录
        console.log('用户未登录，登录后才可以点赞')
        this.setData({ show: true });
      }
    },
    
    // 点击收藏按钮触发
    clickStar:function(event) {
      const index = event.currentTarget.dataset.index;
      console.log(index);
      const cardList = this.data.cardList;
      const postId = cardList[index].postId;
      console.log(postId);
      const userId = this.data.userInfo.userId;
      console.log(userId);
      // 判断用户是否已经登录
      if (userId !== undefined) {
        // 用户已经登录
        let data = {
          userId: userId,
          postId: postId
        }
        if (cardList[index].icon_star == 'star-o') {
          // 收藏操作
          cardList[index].icon_star = 'star'; // 点击后将 star 图标变为实心星星:收藏
          cardList[index].starsCount += 1 // 收藏数+1
          // 向数据库中添加用户的收藏记录
          userStar(data).then(res => {
            if (res.statusCode == 200) {
              console.log('收藏成功')
            } else {
              console.log('收藏失败')
            }
          })
        } else {
          // 取消收藏操作
          cardList[index].icon_star = 'star-o'; // 取消收藏
          cardList[index].starsCount -= 1 // 收藏数-1
          // 从数据库中删除用户的收藏记录
          userCancelStar(data).then(res => {
            if (res.statusCode == 200) {
              console.log('取消收藏成功')
            } else {
              console.log('取消收藏失败')
            }
          })
        }
        // console.log(cardList)
        // 把图标的更改显示在页面上
        this.setData({ cardList: cardList });
      } else {
        // 用户还未登录
        console.log('用户未登录，登录后才可以收藏')
        this.setData({ show: true });
      }
    },

    onConfirm() {
      this.setData({ show: false });
      wx.navigateTo({
        url: '/pages/login/login',
      })
    },
  
    onClose() {
      this.setData({ show: false });
    },

    clickCell:function(event) {
      const index = event.currentTarget.dataset.index;
      console.log('index:',index);
      const cardList = this.data.cardList;
      const postId = cardList[index].postId;
      console.log('点击的postId:',postId);
      wx.navigateTo({
        url: '/pages/postDetail/postDetail?data=' + postId,
      })
    }
  }
})