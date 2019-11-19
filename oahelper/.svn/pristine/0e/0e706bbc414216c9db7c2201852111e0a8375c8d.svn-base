// pages/27baoxiao_detail/index.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    name: '', //姓名
    content: '', //显示内容
    status: '', //审核状态
    money: '' //报销的金额(注：如果有金额就是报销成功)
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let This = this;
    console.log('id:' + options.id);

    wx.showLoading({
      title: '加载中',
    })

    //根据 options.id 查询数据
    wx.request({
      url: app.helperRequestHost + '/pr/goRemindInform/read/detail',
      method: 'POST',
      data: {
        id: options.id
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function(res) {
        wx.hideLoading();
        This.setData({
          name: res.data.data.userName, //姓名
          content: res.data.data.remindContent, //显示内容
          status: res.data.data.remindStatusText, //审核状态
          // money: res.data.money //报销的金额(注：如果有金额就是报销成功)
        });
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