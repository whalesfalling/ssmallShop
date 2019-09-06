var util = require('../../utils/util.js');
var api = require('../../config/api.js');

var app = getApp();

Page({
  data: {
    checkedGoodsList: [],
    checkedAddress: {},
    usableCredits: false,
    maxCreditsPrice: 0,//积分抵扣金额
    maxCredits: 0,//积分抵扣消耗分数
    goodsTotalPrice: 0.00, //商品总价
    freightPrice: 0.00, //快递费
    grouponPrice: 0.00, //团购优惠价格
    orderTotalPrice: 0.00, //订单总价
    actualPrice: 0.00, //实际需要支付的总价
    cartId: 0,
    addressId: 0,
    message: '',
    useCredits: 0, //是否使用积分
    grouponLinkId: 0, //参与的团购，如果是发起则为0
    grouponRulesId: 0 //团购规则ID
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
  },

  //获取checkou信息
  getCheckoutInfo: function() {
    let that = this;
    util.request(api.CartCheckout, {
      cartId: that.data.cartId,
      addressId: that.data.addressId,
      grouponRulesId: that.data.grouponRulesId
    }).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          checkedGoodsList: res.data.checkedGoodsList,
          checkedAddress: res.data.checkedAddress,
          usableCredits: res.data.usableCredits,
          maxCreditsPrice: res.data.maxCreditsPrice,
          maxCredits: res.data.maxCredits,
          actualPrice: res.data.actualPrice,
          grouponPrice: res.data.grouponPrice,
          freightPrice: res.data.freightPrice,
          goodsTotalPrice: res.data.goodsTotalPrice,
          orderTotalPrice: res.data.orderTotalPrice,
          addressId: res.data.addressId,
          grouponRulesId: res.data.grouponRulesId,
        });
      }
      wx.hideLoading();
    });
  },
  selectAddress() {
    wx.navigateTo({
      url: '/pages/ucenter/address/address',
    })
  },
  bindMessageInput: function(e) {
    this.setData({
      message: e.detail.value
    });
  },
  switchChange: function(e) {
    let that = this;
    if (e.detail.value){
      this.setData({
        useCredits: 1,
        actualPrice: that.data.actualPrice - that.data.maxCreditsPrice
      });
    } else {
      this.setData({
        useCredits: 0,
        actualPrice: that.data.actualPrice + that.data.maxCreditsPrice
      });
    }
  },
  onReady: function() {
    // 页面渲染完成

  },
  onShow: function() {
    // 页面显示
    wx.showLoading({
      title: '加载中...',
    });
    try {
      var cartId = wx.getStorageSync('cartId');
      if (cartId === "") {
        cartId = 0;
      }
      var addressId = wx.getStorageSync('addressId');
      if (addressId === "") {
        addressId = 0;
      }
      var grouponRulesId = wx.getStorageSync('grouponRulesId');
      if (grouponRulesId === "") {
        grouponRulesId = 0;
      }
      var grouponLinkId = wx.getStorageSync('grouponLinkId');
      if (grouponLinkId === "") {
        grouponLinkId = 0;
      }

      this.setData({
        cartId: cartId,
        addressId: addressId,
        grouponRulesId: grouponRulesId,
        grouponLinkId: grouponLinkId
      });

    } catch (e) {
      // Do something when catch error
      console.log(e);
    }

    this.getCheckoutInfo();
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },
  submitOrder: function() {
    if (this.data.addressId <= 0) {
      util.showErrorToast('请选择收货地址');
      return false;
    }
    if (this.data.useCredits != 0 && this.data.maxCreditsPrice == 0){
      util.showErrorToast('无积分抵扣');
      return false;
    }
    util.showLoading("系统加载中...");
    util.request(api.OrderSubmit, {
      cartId: this.data.cartId,
      addressId: this.data.addressId,
      message: this.data.message,
      grouponRulesId: this.data.grouponRulesId,
      grouponLinkId: this.data.grouponLinkId,
      useCredits: this.data.useCredits,
      credits: this.data.maxCredits
    }, 'POST').then(res => {
      if (res.errno === 0) {
        const orderId = res.data.orderId;
        util.request(api.OrderPrepay, {
          orderId: orderId
        }, 'POST').then(function(res) {
          if (res.errno === 0) {
            const payParam = res.data;
            console.log("支付过程开始");
            wx.requestPayment({
              'timeStamp': payParam.timeStamp,
              'nonceStr': payParam.nonceStr,
              'package': payParam.packageValue,
              'signType': payParam.signType,
              'paySign': payParam.paySign,
              'success': function(res) {
                util.hideLoading();
                console.log("支付过程成功");
                wx.redirectTo({
                  url: '/pages/payResult/payResult?status=1&orderId=' + orderId
                });
              },
              'fail': function(res) {
                util.hideLoading();
                console.log("支付过程失败");
                wx.redirectTo({
                  url: '/pages/payResult/payResult?status=0&orderId=' + orderId
                });
              },
              'complete': function(res) {
                util.hideLoading();
                console.log("支付过程结束")
              }
            });
          } else {
            util.hideLoading();
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=0&orderId=' + orderId
            });
          }
        });

      } else {
        util.hideLoading();
      }
    });
  }
});