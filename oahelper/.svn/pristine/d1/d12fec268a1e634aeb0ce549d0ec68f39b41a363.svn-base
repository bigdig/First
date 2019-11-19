// pages/32kaoqin_detail/index.js

const app = getApp()

Page({

  /** 
   * 页面的初始数据
   */
  data: {
    name: '', //姓名
    content: '', //显示内容
    day: '', //考勤天数

    isReplied: 'NO', //是否已确认
    backText:'',
    ifDisagree: false,
    listData: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

    wx.showLoading({
      title: '页面加载中',
    })
    
    let This = this;
    This.setData({
      informUserId: options.id,
      name: app.globalData.userInfo.userName
    })

    var that = this;
    // console.log(app.globalData.cookieInfo);
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goInformUser/read/detail',
        method: 'POST',
        data: {
          id: that.data.informUserId
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function(res) {
          // console.log("查询活动详情返回的res：", res)
          if (res.data.httpCode == 200) {
            console.log()
            let re = res.data.data.isReplied
            that.setData({
              listData: res.data.data,
              content: res.data.data.inform.informContent,
              isReplied: re
            })
            // console.log(that.data)
            wx.hideLoading();
          } else if (res.data.httpCode == 401) {
            wx.redirectTo({
              url: './../login/login'
            })
          } else {
            app.msgModal("提示消息：", res.data.msg);
          }
        },
        fail: function(res) {
          app.msgModal("提示消息：", res.data.msg);
        }
      })
    }
  },
  confirm() {
    this.go_list('YES');
  },
  //填写异议理由
  bindInputVal(e) {
    let This = this;
    This.setData({
      backText: e.detail.value
    });
  },
  disagree() {
    this.setData({
      ifDisagree: true
    })
  },
  cancelDisagree() {
    this.setData({
      ifDisagree: false
    })
  },
  submitDisagree() {
    if (this.data.backText == '') {
      app.msgModal("提示消息：", '请输入异议理由');
      return;
    }
    this.go_list('DISAGREE');
  },

  //确认
  go_list(isReplied) {
    let This = this;
    let remark = ''
    if (isReplied == 'DISAGREE') {
      remark = This.data.backText
    }

    wx.showLoading({
      title: '提交中',
    })

    wx.request({
      url: app.helperRequestHost + '/pr/goInformUser/confirm',
      method: 'POST',
      data: {
        id: This.data.listData.id,
        remark: remark,
        isReplied: isReplied
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function(res) {
        // console.log("查询活动详情返回的res：", res)
        if (res.data.httpCode == 200) {
          // console.log(that.data)
          setTimeout(function() {
            This.setData({
              isReplied: isReplied,
              ifDisagree: false
            })
            wx.hideLoading();
            wx.showToast({
              title: '确认成功',
              icon: 'success',
              duration: 1000
            })
          }, 1000)
        } else if (res.data.httpCode == 401) {
          wx.redirectTo({
            url: './../login/login'
          })
        } else {
          app.msgModal("提示消息：", res.data.msg);
        }
      },
      fail: function(res) {
        app.msgModal("提示消息：", res.data.msg);
      }
    })
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