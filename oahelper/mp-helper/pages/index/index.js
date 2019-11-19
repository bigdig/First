// pages/3shouye/index.js
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userId: '',
    pageSize: 5,
    tongzhi: [], //通知显示文本及链接配置
    toupiao: [], //投票显示文本及链接配置
    huodong: [] //活动显示文本及链接配置
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let that = this;
    that.setData({
      userId: app.globalData.userInfo.id
    })
    // console.log(that.data)
  },

  /*通知 */
  getNoticeInfo: function() {
    var that = this;
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goNotice/read/list',
        method: 'POST',
        data: {
          pageSize: this.data.pageSize
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function(res) {
          console.log("活动"+res.data)
          if (res.data.httpCode == 200) {
            that.setData({
              tongzhi: res.data.data.list,
            })
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
  /*投票 */
  getVoteInfo: function() {
    var that = this;
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goVoteGroup/read/list',
        method: 'POST',
        data: {
          pageSize: this.data.pageSize,
          isActivity: 'NO',
          isOpen: 'OPEN',
          pageNum: 1
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function(res) {
          console.log("投票"+res.data)
          if (res.data.httpCode == 200) {
            // that.setData({
            //   toupiao: res.data.data.list,
            // })
            let tmp = []
            let li = res.data.data.list
            let now = new Date()
            for (var ii = 0; ii < li.length; ii++) {
              //let t = new Date(new Date(li[ii].endTime).getTime() + 24 * 60 * 60 * 1000)

              //if (t.getTime() > now.getTime()) {
                tmp.push(li[ii])
              //}
            }
            that.setData({
              toupiao: tmp,
            })

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
  /**活动 */
  getActInfo: function() {
    var that = this;
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goVoteGroup/read/list',
        method: 'POST',
        data: {
          pageSize: this.data.pageSize,
          isActivity: 'YES',
          isOpen: 'OPEN',
          pageNum: 1
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function(res) {

          if (res.data.httpCode == 200) {
            // that.setData({
            //   huodong: res.data.data.list,
            // })
            let tmp = []
            let li = res.data.data.list
            let now = new Date()
            for (var ii = 0; ii < li.length; ii++) {
              //let t = new Date(new Date(li[ii].endTime).getTime() + 24 * 60 * 60 * 1000)

              //if (t.getTime() > now.getTime()) {
                tmp.push(li[ii])
              //}
            }
            that.setData({
              huodong: tmp,
            })
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

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    wx.showLoading({
      title: '页面加载中',
    })
    this.getNoticeInfo();
    this.getVoteInfo();
    this.getActInfo();

    setTimeout(function() {
      wx.hideLoading();
    }, 1000)
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