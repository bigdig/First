const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    cBorderBottom: '2rpx solid #FC6D51',
    mBorderBottom: '0rpx',
    cColorText: '#282C3F',
    mColorText: '#9FA3BB',
    activityId:[],   //用于装选中活动ID
    customerList: [], //数据遍历
    obj:{},
    custId:'',
    scrollTop: 0,
    scrollHeight: 0,
    pageNum: 1,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.data.obj = options;
    var that = this;
    console.log(options);
    this.setData({
      custId: options.custId,
    })
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          scrollHeight: res.windowHeight
        });
      }
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.info();
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
  //新建跳转到活动
  tab_mechanism: function () {
    wx.navigateTo({
      url: '../addMyAct/addMyAct',
    })
  },
  //操作选择
  checkboxChange: function(e){
    console.log(e.currentTarget.dataset)
    var obj = e.currentTarget.dataset;
    var lists = this.data.customerList;
    var activityId = this.data.activityId;
    if (!obj.checked){
      for (var i = 0; i < lists.length; i++) {
        if (lists[i].id == obj.id) {
          lists[i].checked = true;
          activityId.push(obj.id)
          this.setData({
            activityId: activityId,
            customerList: lists,
          })
        }
      }
    }else{
      for (var i = 0; i < lists.length; i++) {
        if (lists[i].id == obj.id) {
          lists[i].checked = false;
          for (var j = 0; j < activityId.length;j++){
            if (activityId[j] == obj.id ){
               activityId.splice(j--,1);
            }
          }
        }
      }
      this.setData({
        activityId: activityId,
        customerList: lists,
      })
    }
  },
  //数据遍历
  info:function(){
    var that = this;
    that.setData({
      hidden: false
    });
    //console.log(app.globalData.userInfo.staffName)
    var custId = that.data.custId;
    wx.request({
      url: app.crmRequestHost + '/ty/tyActivity/read/list',
      method: 'POST',
      data: {
        activityCate:'0',
        pageNum: that.data.pageNum,
        // createBy: app.globalData.userInfo.id
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var lists = res.data.data.list;
          for (var i = 0; i < lists.length;i++){
            lists[i].checked = false;
          }
          that.setData({
            customerList: that.data.customerList.concat(lists),
            hidden: true,
          })
          that.data.pageNum++;
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
  },
  //提交
  submit:function(){
    var that = this;
    var staffName = app.globalData.userInfo.staffName;
    var staffId = app.globalData.userInfo.id;
    wx.request({
      url: app.crmRequestHost + '/ty/tyActivitysign/add/actSignServiceLog',
      method: 'POST',
      data: {
        custId: that.data.obj.custId,
        signId : staffId,
        signName : staffName,
        activityIds: that.data.activityId,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var arr = getCurrentPages();
          // wx.redirectTo({
          //   url: './../customerDetails/customerDetails?id=' + that.data.obj.custId,
          // })
          wx.navigateBack({
            delta: 1,
            success : function(){
              arr[arr.length -2].select();
            }
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
    
  },
  bindDownLoad: function () {
    this.info();
  },
})