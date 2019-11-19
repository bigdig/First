// pages/33wode/index.js
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    name: '', //姓名
    email: '', //邮箱
    headimg: '/img/touxiang.png', //头像
    userInfo: {},
    hasUserInfo: false,
    hasShenPiAuth:false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let This = this;
    wx.showLoading({
      title: '加载中',
    })
    console.log(app.globalData.userInfo)
    /*test*/
    let res = {
      status: 1,
      data: {
        name: app.globalData.userInfo.userName, //姓名
        email: app.globalData.userInfo.account+'@tfzq.com', //邮箱
        headimg: '/img/touxiang.png', //头像
      }
    }
    wx.hideLoading();
    let hasShenPiAuth = app.globalData.userInfo.remark.indexOf('auditArticle') > -1 || app.globalData.userInfo.remark.indexOf('auditView') > -1;
    This.setData({
      name: res.data.name, //姓名
      email: res.data.email, //邮箱
      headimg: res.data.headimg, //头像
      hasShenPiAuth: hasShenPiAuth
    });
    /*test*/

    return;

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