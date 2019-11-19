//app.js
App({
  // helperRequestHost: 'http://127.0.0.1:8088',
  //helperRequestHost: 'http://oahelpersim.dangdaifintech.com',
  helperRequestHost: 'https://tytool.tfzq.com',
  globalData: {
    userInfo: null,
    uploadImg: false,
    userId: '',
    userName: ''
  },
  //处理时间方法
  setDateTime(datetime) {
    if (datetime.indexOf(' ') != -1) {
      return datetime.substr(0, datetime.indexOf(' '))
    } else {
      return datetime;
    }
  },
  //模态弹窗
  msgModal: function(title, msg, showCancel) {
    wx.showModal({
      title: title,
      content: msg,
      showCancel: (showCancel == null ? false : showCancel),
    })
  },
  onLaunch: function() {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 获取用户信息
    // wx.getSetting({
    //   success: res => {
    //     if (res.authSetting['scope.userInfo']) {
    //       // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
    //       wx.getUserInfo({
    //         success: res => {
    //           // 可以将 res 发送给后台解码出 unionId
    //           this.globalData.userInfo = res.userInfo

    //           // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //           // 所以此处加入 callback 以防止这种情况
    //           if (this.userInfoReadyCallback) {
    //             this.userInfoReadyCallback(res)
    //           }
    //         }
    //       })
    //     }
    //   }
    // })

  },

  onShow: function () {
    // 登录
    if (this.globalData.uploadImg) {
      console.log('从上传图片返回时App onShow不做处理')
      return
    }
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
  },

  userLogin: function(code, options) {
    var that = this;
    
    wx.removeStorageSync("cookieInfo"); //清除上一次登录信息
    wx.request({
      url: that.helperRequestHost + '/wxlog',
      method: 'POST',
      data: {
        adlogin: 'false', //是否AD登录
        account: '',
        password: '',
        wxCode: code,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded' // 默认值
      },
      success: function(res) {
        //console.log("登录成功后返回的res：", res)
        if (res.data.httpCode == 200) {
          //保存cookie
          wx.setStorageSync("cookieInfo", res.header["Set-Cookie"]);
          that.globalData.cookieInfo = null;
          that.globalData.cookieInfo = res.header["Set-Cookie"];
          //保存crm中的用户信息
          that.globalData.userInfo = res.data.data;
          console.log('登录：：', that.globalData);
          if (!that.globalData.uploadImg) { //如果是上传图片导致的onhide则不跳转
            if (options && options.query.isShare) {
              //console.log('不跳转到首页');
              //不跳转到首页
            } else {
              console.log('ReLaunch到首页');
              wx.reLaunch({
                url: '/pages/index/index'
              })
            }
          } else {
            that.globalData.uploadImg = false;
          }

        } else if (res.data.httpCode == 401) {
          wx.redirectTo({
            url: '/pages/login/login'
          })
        } else if (res.data.httpCode == 303) {
          let pages = getCurrentPages()
          let currentPage = pages[pages.length - 1]
          console.log(currentPage);
          if (currentPage.route!='pages/login/login'){
            wx.redirectTo({
              url: '/pages/login/login'
            })
          }
        }
      }
    })
  }
})