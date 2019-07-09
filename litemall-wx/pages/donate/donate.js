const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../utils/user.js');

//获取应用实例
const app = getApp();

Page({
  data: {
    topics: [],
    page: 1,
    limit: 5,
    totalPages: 1
  },

  onShareAppMessage: function () {
    return {
      title: '一元乐购小程序商场',
      desc: '一元乐购小程序商城',
      path: '/pages/index/index'
    }
  },

  onPullDownRefresh() {
    wx.showNavigationBarLoading() //在标题栏中显示加载
    let that = this;
    that.setData({
      topics: [],
      page: 1
    });
    this.getIndexData();
    wx.hideNavigationBarLoading() //完成停止加载
    wx.stopPullDownRefresh() //停止下拉刷新
  },

  getIndexData: function () {
    let that = this;
    util.request(api.TopicList ,{
      page: that.data.page,
      limit: that.data.limit
    }).then(function (res) {
      console.log(res.data);
      if (res.errno === 0) {
        that.setData({
          topics: that.data.topics.concat(res.data.list),
          totalPages: res.data.pages
        });
      }
    });
  },
  onReachBottom() {
    if (this.data.totalPages > this.data.page) {
      this.setData({
        page: this.data.page + 1
      });
      this.getIndexData();
    } else {
      wx.showToast({
        title: '没有更多专题了',
        icon: 'none',
        duration: 2000
      });
      return false;
    }
  },
  switchTab: function (event) {
    let showType = event.currentTarget.dataset.index;
    this.setData({
      topics: [],
      page: 1,
      limit: 5,
      totalPages: 1
    });
    this.getIndexData();
  },
  onLoad: function (options) {
    
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
    this.getIndexData();
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  getCoupon(e) {
    if (!app.globalData.hasLogin) {
      wx.navigateTo({
        url: "/pages/auth/login/login"
      });
    }
  },
})