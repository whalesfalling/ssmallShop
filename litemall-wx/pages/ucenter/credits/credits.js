var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');

var app = getApp();

Page({
  data: {
    myCredits: 0,
    creditsLogList: [],
    page: 1,
    limit: 10,
    totalPages: 1
  },
  getCreditsInfo() {
    wx.showLoading({
      title: '加载中...',
    });
    let that = this;
    util.request(api.CreditsInfo).then(function (res) {
      // console.log("积分", res.data);
      if (res.errno === 0) {
        that.setData({
          myCredits: res.data
        });
      }
      
    });
    util.request(api.CreditsLogList, {
      page: that.data.page,
      limit: that.data.limit
    }).then(function(res) {
      if (res.errno === 0) {
        let f1 = that.data.creditsLogList;
        let f2 = res.data.list;
        for (let i = 0; i < f2.length; i++) {
          f2[i].addDate = f2[i].addTime.substring(0, 10)
          let last = f1.length - 1;
          if (last >= 0 && f1[last][0].addDate === f2[i].addDate) {
            f1[last].push(f2[i]);
          } else {
            let tmp = [];
            tmp.push(f2[i])
            f1.push(tmp);
          }
        }

        that.setData({
          creditsLogList: f1,
          totalPages: res.data.pages
        });
      }
      wx.hideLoading();
    });
  },
  onLoad: function(options) {
    this.getCreditsInfo();
  },
  onReachBottom() {
    if (this.data.totalPages > this.data.page) {
      this.setData({
        page: this.data.page + 1
      });
      this.getCreditsInfo();
    } else {
      wx.showToast({
        title: '没有更多积分记录了',
        icon: 'none',
        duration: 2000
      });
      return false;
    }
  },
  onReady: function() {

  },
  onShow: function() {

  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭
  }
})