// pages/13tongzhi_detail/index.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id:null,
    notice:null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let This = this;
    // console.log('id:' + options.id);
    this.setData({
      id:options.id
    })
  },

  /*活动 */
  getNoticeDetail: function () {
    var that = this;
    // console.log(app.globalData.cookieInfo);
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goNotice/read/detail',
        method: 'POST',
        data: {
          id: this.data.id
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          // console.log("查询活动详情返回的res：", res)
          if (res.data.httpCode == 200) {
            //处理时间（去掉时分秒）
            res.data.data.createTime = app.setDateTime(res.data.data.createTime);
            that.setData({
              notice: res.data.data,
            })
          } else if (res.data.httpCode == 401) {
            wx.redirectTo({
              url: './../login/login'
            })
          } else {
            app.msgModal("提示消息：", res.data.msg);
          }
        },
        fail: function (res) {
          app.msgModal("提示消息：", res.data.msg);
        }
      })
    }
  },

  //复制链接
  copyLink(e) {
    //let This = this;
    let linkSrc = e.currentTarget.dataset.linksrc;
    wx.setClipboardData({
      data: linkSrc,
      success(res) {
        wx.getClipboardData({
          success(res) {
            console.log(res.data) // data
          }
        })
      }
    })
  },
  
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.getNoticeDetail();
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})