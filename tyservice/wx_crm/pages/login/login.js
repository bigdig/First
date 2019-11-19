//获取应用实例
const app = getApp()

Page({
  data: {
    username: null,
    password: null,
    telError: false,
    passwordError:false, 
    passwordErrorText:'',
    selected: null,
    pwdUnselected: null,
    checked:true,
  },
  onLoad: function (options) {
    
  },
  onReady: function () {
    
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  //是否AD登录
  selectChange:function(e){
    var obj = e.currentTarget.dataset;
    if (obj.checked){
      this.setData({
        checked:false,
      })
    }else{
      this.setData({
        checked: true,
      })
    }
  },
  selected :function(e){
    this.setData({
      selected: '1px solid #FC6D51',
    })
  },
  Unselected :function(e){
    this.setData({
      selected: '1px solid #F2F4FA',
      telError: false,
    })  
  },
  pwdSelected: function (e) {
    this.setData({
      pwdUnselected: '1px solid #FC6D51',
    })
  },
  pwdUnselected: function (e) {
    this.setData({
      pwdUnselected: '1px solid #F2F4FA',
    })
  },
  // loginBtnClick: function () {
  //   //清楚缓存的数据
  //   wx.removeStorageSync("cookieInfo");
  //    // 用户名和密码验证的过程
  //    var that = this;
  //    //登录之前首先需要用户信息
  //   if(app.globalData.wxUserInfo == null){
  //     console.log(22);
  //     wx.getUserInfo({
  //       success: function (res) {
  //         app.globalData.wxUserInfo = res.userInfo;
  //         console.log(11+res.userInfo);
  //         if (that.userInfoReadyCallback) {
  //           that.userInfoReadyCallback(res)
  //         }
  //         that.userLogin();
  //       }
  //     })
  //   }else{
  //     this.userLogin();
  //   }
      
  // },

  userLogin : function(avatarUrl){
    var that = this;
    wx.login({
      success: function (res) {
         wx.request({
           url: app.crmRequestHost +'/wxlog', 
           method: 'POST',
           data: {
             adlogin: that.data.checked,     //是否AD登录
             account: that.data.username,
             password: that.data.password,
             wxCode: res.code,
             avatar : avatarUrl,
           },
           header: {
             'content-type': 'application/x-www-form-urlencoded' // 默认值
           },
           success: function (res) {
             console.log("登录成功后返回的res：",res)
             if (res.data.httpCode == 200){
                //保存cookie
                wx.setStorageSync("cookieInfo", res.header["Set-Cookie"]);
                app.globalData.cookieInfo = null;
                app.globalData.cookieInfo = res.header["Set-Cookie"];
                //保存crm中的用户信息
                app.globalData.userInfo = res.data.data.user;
                app.globalData.activateCustomers = res.data.data.activateCustomers;
                app.globalData.myActivityList = res.data.data.myActivityList + res.data.data.mySignActList;
                app.globalData.isSeller = res.data.data.isSeller;
                 console.log( '登录：：', app.globalData.userInfo);
                wx.switchTab({
                  url: '../index/index'
                })
             }else{
               that.setData({
                 passwordError: true,
                 passwordErrorText:res.data.msg,
               })
             }
           }
         })
      }
    })
  },

  usernameInput: function (e) {
    this.setData({username : e.detail.value });
  },
  passwordInput: function (e) {
    this.setData({ password: e.detail.value });
  },
  loginBtnClick : function (e) {
    console.log("get UserInfo ::",e);
    //清楚缓存的数据
    wx.removeStorageSync("cookieInfo");
     // 用户名和密码验证的过程
     this.userLogin(e.detail.userInfo.avatarUrl);
  },
})