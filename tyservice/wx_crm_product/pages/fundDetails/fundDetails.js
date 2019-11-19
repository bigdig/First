const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    windcode:null,
    detail:[{
      text:"重仓股票"
    },
      {
        text: "持仓占比"
      }],
    detailList:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      windcode: options.windcode,
    })
      
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyCfund/read/fundstocklist',
      method: 'POST',
      data: {
        windcode: that.data.windcode,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var item = res.data.data;
          that.setData({
            detailList: item,
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
    });
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
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