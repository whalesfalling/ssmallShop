// about.js
var app = getApp()
var util = require("../../utils/util.js");


var api = require("../../config/api.js");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    load_statue: true,
    shopInfo: {
      name: '一元乐购微信小程序',
      address: '上海市',
      latitude: 31.201900,
      longitude: 121.587839,
      linkPhone: '021-xxxx-xxxx',
      qqNumber: '72961'
    },
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.showAbout();
  },
  showAbout: function() {
    var that = this;
    util.request(api.LocalAbout).then(function(res){
      if (res.errno === 0){
         that.setData({
           shopInfo: {
             name: res.data.litemall_mall_name,
             address: res.data.litemall_mall_address,
             linkPhone: res.data.litemall_mall_phone,
             qqNumber: res.data.litemall_mall_qq,
           }
         });
      }  
    });
  },
  showLoading:function (e) {
    var that = this;
    wx.openLocation({
      latitude: that.data.shopInfo.latitude,
      longitude: that.data.shopInfo.longitude,
      name: that.data.shopInfo.name,
      address: that.data.shopInfo.address,
    })
  },
  callPhone: function (e) {
    var that = this
    wx.makePhoneCall({
      phoneNumber: that.data.shopInfo.linkPhone,
    })
  },
  reLoad: function (e) {
    this.showAbout();
  }
})