const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    id:'',
    state: false,
    userName: '',
    department: "",
    position: "",
    phone: "",
    email: "",
    serviceSaler: '',
    teamCat: '',
    contactorName: '',
    contactorTel: '',
    address: '',
    listDetail:null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options.id)
    this.setData({
      id: options.id,
    })
    

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.listDetail();
  },
  //数据遍历
  listDetail: function () {
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyServiceorg/read/detail',
      method: 'POST',
      data: {
        id: that.data.id,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var item = res.data.data;
          var state = null;
          if (item.custStatusName == '已签约'){
              state = true;
          }else{
              state = false;
          }
          that.setData({
            state: state,
            userName: item.orgName,
            department: item.custCatName,
            position: item.orgLevelName,
            phone: item.companyTel,
            email: item.companyMail,
            serviceSaler: item.serviceSaler,
            teamCat: item.teamCat,
            contactorName: item.contactorName,
            contactorTel: item.contactorTel,
            address: item.address,
            listDetail:item,
          })
        } else if (res.data.httpCode == 401) {
          wx.redirectTo({
            url: './../login/login'
          })
        } else {
          that.msgModal("提示消息：", res.data.msg);
        }
      },
      fail: function (res) {
        that.msgModal("提示消息：", res.data.msg);
      }
    })
  },
  //修改状态
  signState:function(){
    var that = this;
    var pasue = that.data.listDetail;
    pasue.custStatusName = '未签约';
    pasue.custStatus = '1';    //1未签约，2潜在客户，3已签约
    wx.request({
      url: app.crmRequestHost + '/ty/tyServiceorg/read/detail',
      method: 'POST',
      data: pasue,
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          that.setData({
            state : true,
          });

        } else if (res.data.httpCode == 401) {
          wx.redirectTo({
            url: './../login/login'
          })
        } else {
          that.msgModal("提示消息：", res.data.msg);
        }
      },
      fail: function (res) {
        that.msgModal("提示消息：", res.data.msg);
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