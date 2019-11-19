//获取应用实例
const app = getApp()
var pageSize = 15;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    cBorderBottom: '2rpx solid #FC6D51',
    mBorderBottom: '0rpx',
    cColorText: '#282C3F',
    mColorText: '#9FA3BB',
    fBorderColor: "6rpx solid #FC6D51",
    sBorderColor: "6rpx solid #FFAD4C",
    taber: true,
    customerList: null,
    mechanismList: null,
    pageNum:1,
    singId:'',
    createBy:'',
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  tab_customer: function () {
    this.setData({
      taber: true,
      cBorderBottom: '2rpx solid #FC6D51',
      mBorderBottom: '0rpx',
      cColorText: '#282C3F',
      mColorText: '#9FA3BB',
    })
  },
  tab_mechanism: function () {
    this.setData({
      
    })
    //请求接口
    this.info();
    //gaibain
    this.setData({
      taber: false,
      cBorderBottom: '0rpx',
      mBorderBottom: '2rpx solid #FC6D51',
      cColorText: '#9FA3BB',
      mColorText: '#282C3F',
    })
    
  },
  info:function(){
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyActivity/read/list',
      method: 'POST',
      data: {
        singId: app.globalData.userInfo.id
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        console.log("myActivities成功后返回的res：", res)
        that.setData({
          mechanismList: res.data.data
        })
      }
    })
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