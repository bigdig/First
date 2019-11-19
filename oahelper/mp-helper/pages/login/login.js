//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    userName: '',
    pwd: '',
    checked:true
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
  loginBtnClick: function (e) {
    //清楚缓存的数据
    wx.removeStorageSync("cookieInfo");
    // 用户名和密码验证的过程
    this.userLogin();
  },
  checkboxChange:function(e){
    if(this.data.checked){
      this.setData({
        checked: false
      })
    }else{
      this.setData({
        checked: true
      })
    }
    console.log(this.data.checked);
  },
  userLogin: function () {
    var that = this;
    wx.login({
      success: function (res) {
        wx.request({
          url: app.helperRequestHost + '/wxlog',
          method: 'POST',
          data: {
            adlogin: that.data.checked,     //是否AD登录
            account: that.data.userName,
            password: that.data.pwd,
            wxCode: res.code
          },
          header: {
            'content-type': 'application/x-www-form-urlencoded' // 默认值
          },
          success: function (res) {
            //console.log("登录成功后返回的res：", res)
            if (res.data.httpCode == 200) {
              //保存cookie
              wx.setStorageSync("cookieInfo", res.header["Set-Cookie"]);
              app.globalData.cookieInfo = null;
              app.globalData.cookieInfo = res.header["Set-Cookie"];
              //保存crm中的用户信息
              app.globalData.userInfo = res.data.data;
              console.log('登录：：', app.globalData.userInfo);
              wx.switchTab({
                url: '../index/index'
              })
            } else {
              that.setData({
                passwordError: true,
                passwordErrorText: res.data.msg,
              })
            }
          }
        })
      }
    })
  },
  onLoad: function () {
  },
  onShow:function(){
    console.log('Login page onShow');
    wx.login({
      timeout: 5000,
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        console.log(res.code);
        this.userLogin(res.code);
      },
      complete: res => {
        console.log('complete')
      }
    })
  }
})
