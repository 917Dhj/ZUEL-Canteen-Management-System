// 导入
const { request } = require('../utils/request.js')
const base = require('./base')
const { baseUrl } = require('./base')

// 登录接口
function login(data) {
  return request(baseUrl + '/login', 'POST', data)
}

// 注册接口
function register(data) {
  return request(baseUrl + '/register', 'POST', data)
}

// 获得用户信息接口
function getInfo() {
  return request(baseUrl + '/getInfo', 'GET')
}

// 验证登录信息接口
function verifyLogin(data) {
  return request(baseUrl + '/verify', 'POST', data)
}

// 修改用户名（昵称）接口
function modifyUserName(data) {
  return request(baseUrl + '/modify/name', 'POST', data)
}

// 修改密码接口
function modifyPassword(data) {
  return request(baseUrl + '/modify/password', 'POST', data)
}

// 获取推荐的帖子接口
function getRecommendPost(data) {
  return request(baseUrl + '/post/recommend', 'POST', data)
}

// 点赞接口
function userLike(data) {
  return request(baseUrl + '/post/like', 'POST', data)
}

// 取消点赞接口
function userDislike(data) {
  return request(baseUrl + '/post/dislike', 'POST', data)
}

// 收藏接口
function userStar(data) {
  return request(baseUrl + '/post/star', 'POST', data)
}

// 取消收藏接口
function userCancelStar(data) {
  return request(baseUrl + '/post/cancelstar', 'POST', data)
}

// 获取用户收藏的帖子接口
function getStaredPost(data) {
  return request(baseUrl + '/post/stared', 'POST', data)
}

// 获取最新的帖子接口
function getNewPost(data) {
  return request(baseUrl + '/post/new', 'POST', data)
}

// 获取失物招领帖子接口
function getFindPost(data) {
  return request(baseUrl + '/post/find', 'POST', data)
}

// 获取帖子评论接口
function getComments(data) {
  return request(baseUrl + '/post/comments', 'POST', data)
}

// 发布帖子接口
function submitPost(data) {
  return request(baseUrl + '/post/submit', 'POST', data)
}

// 发布失物招领接口
function submitFindPost(data) {
  return request(baseUrl + '/post/submit/find', 'POST', data)
}

// 获取商家名称列表接口
function getRestaurantList() {
  return request(baseUrl + '/restaurant/name', 'GET')
}

// 获取帖子详情接口
function getPostDetail(data) {
  return request(baseUrl + '/post/detail', 'POST', data)
}

// 发布评论接口
function submitComment(data) {
  return request(baseUrl + '/post/comments/insert', 'POST', data)
}

// 点赞评论接口
function likeComment(data) {
  return request(baseUrl + '/post/comments/like', 'POST', data)
}

// 取消评论点赞接口
function dislikeComment(data) {
  return request(baseUrl + '/post/comments/dislike', 'POST', data)
}

//获取菜品信息接口
function getDish_1_1() {
  return request(baseUrl + '/course/all','GET')
}

// 订单信息接口
function orderinfo(data){
  return request( baseUrl + '/order/submit', 'POST', data)
}
//获取id接口
function getDishid(data){
  return request( baseUrl + '/order/dishId' ,'POST', data)
}

//获取档口名称与评分接口
function getCanteen(){
  return request( baseUrl + '/restaurant/all','GET')
}

// 获取所有订单信息接口
function getAllOrders(data) {
  return request(baseUrl + '/order/all', 'POST', data)
}

// 获取售后信息接口
function getAfterSale(data) {
  return request(baseUrl + '/order/afterSale', 'POST', data)
}

// 提交售后申请接口
function addAfterSale(data) {
  return request(baseUrl + '/aftersale/add', 'POST', data)
}

// 修改订单状态接口
function updateOrderStatus(data) {
  return request(baseUrl + '/order/update/status', 'POST', data)
}

// 修改订单评价状态接口
function updateOrderEvaluationStatus(data) {
  return request(baseUrl + '/order/update/evaluationStatus', 'POST', data)
}

// 获取评价信息接口
function getEvaluation(data) {
  return request(baseUrl + '/order/evaluations', 'POST', data)
}

// 提交用户反馈接口
function addFeedback(data) {
  return request(baseUrl + '/feedback/insert', 'POST', data)
}

// 获取消息列表接口
function getMessageList(data) {
  return request(baseUrl + '/message/user', 'POST', data) 
}

// 导出
module.exports = { login, register, getInfo, verifyLogin, modifyUserName, modifyPassword, getRecommendPost, userLike, userDislike, userStar, userCancelStar, getStaredPost, getNewPost, getFindPost, getComments, submitPost, submitFindPost, getRestaurantList, getPostDetail, submitComment, likeComment, dislikeComment, getDish_1_1, orderinfo, getDishid, getCanteen, getAllOrders, getAfterSale, addAfterSale, updateOrderStatus, getEvaluation, addFeedback, updateOrderEvaluationStatus, getMessageList }
