var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    myCredits: 0,
    hasLogin: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.getCreditsInfo();
  },

  getCreditsInfo() {
    wx.showLoading({
      title: '加载中...',
    });
    let that = this;
    util.request(api.CreditsInfo).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          myCredits: res.data
        });
      }
    });
    wx.hideLoading();
  },

  goCredits() {
    if (!this.data.hasLogin) {
      wx.navigateTo({
        url: "/pages/auth/login/login"
      });
    } else {
      util.request(api.CreditsLogList, {
        page: 1,
        limit: 1
      }).then(function(res) {
        if (res.errno === 0) {
          if (res.data.list.length === 0) {
            wx.showToast({
              title: '您暂时还没有积分呢,快去分享吧！',
              icon: 'none',
              duration: 2000
            });
          } else {
            wx.navigateTo({
              url: "/pages/ucenter/credits/credits"
            });
          }         
        }
      });
  }
},

/**
 * 生命周期函数--监听页面初次渲染完成
 */
onReady: function() {

},

/**
 * 生命周期函数--监听页面显示
 */
onShow: function() {
  if (app.globalData.hasLogin) {
    this.setData({
      hasLogin: true
    });
  }
},

/**
 * 生命周期函数--监听页面隐藏
 */
onHide: function() {

},

/**
 * 生命周期函数--监听页面卸载
 */
onUnload: function() {

},

/**
 * 页面相关事件处理函数--监听用户下拉动作
 */
onPullDownRefresh: function() {

},

/**
 * 页面上拉触底事件的处理函数
 */
onReachBottom: function() {

},

/**
 * 用户点击右上角分享
 */
onShareAppMessage: function() {

}
})