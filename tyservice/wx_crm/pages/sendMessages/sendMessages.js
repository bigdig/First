// pages/sendMessages/sendMessages.js
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
      custId:'',
      num:'',
      smsTemplate:'',
      user:'',
      smsTemplateList:[],
      msgActivityId : null, //活动页面发送短信传入参数（activityId)
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //活动页面传递活动id到短信发送页面
    if(options.activityId){
      this.setData({
        msgActivityId : options.activityId,
      })
    }
    var num = app.globalData.custId_customer;
    //console.log(options.custId_customer)
    // var lists = JSON.parse(options.custId_customer);
    // var custId = JSON.stringify(lists);
    this.setData({
      num: num.length,
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