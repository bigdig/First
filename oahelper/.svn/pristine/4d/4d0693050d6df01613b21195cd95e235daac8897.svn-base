//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    userName: '',
    pwd: '',
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  //事件处理函数

  bindPwdInput:function(e){
    var that = this;
    that.setData({
      pwd: e.detail.value
    })
  },

  bindUserNameInput: function (e) {
    var that = this;
    that.setData({
      userName: e.detail.value
    })
  },

  onLoad: function () {
  }
})
