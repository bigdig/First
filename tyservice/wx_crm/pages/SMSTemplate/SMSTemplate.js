// pages/SMSTemplate/SMSTemplate.js

const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
     items:[],
     msgContent:null,  //选择模板内容
     custId:null,   //选择客户的数组
     activityId:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    if(options.activityId != 'null'){
      this.setData({
        activityId : options.activityId,
      })
    }
    var lists = app.globalData.custId_customer;
    this.setData({
      custId: lists,
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.template();
  },
  //选择短信模板
  radioChange:function(e){
    this.setData({
      msgContent: e.detail.value,
    })
  },
  //模板选择完成提交
  submit:function(){
    var custlist = this.data.custId;
    var custId = [];
    for (var i = 0; i < custlist.length;i++){
      custId.push(custlist[i].id)
    }
    custId = custId.join(',');
    if (custId.length > 0){
      var that = this;
      wx.request({
        url: app.crmRequestHost + '/ty/tyOrgcustomer/sendActMsg',
        method: 'POST',
        data: {
          msgType: 'sms',
          msgContent: that.data.msgContent,
          custListStr: custId,
          activityId: (that.data.activityId == null ? '' : that.data.activityId),
          title: '',
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          if (res.data.httpCode == 200) {
            console.log(res);
            // wx.navigateTo({
            //   url: '../sendMessages/sendMessages'  //跳转到发送消息页面
            // })
            if(that.data.activityId){
              var arr = getCurrentPages();
              wx.navigateBack({
                delta: 2,
                success: function () {
                  app.msgModal("提示消息：", '短信发送成功')
                  arr[arr.length - 3].getActInfo();
                  arr[arr.length - 3].setData({
                    showModal: false,
                  })
                }
              })
            }else{
              // wx.showModal({
              //   title: '提示',
              //   content: '这是一个模态弹窗',
              //   success: function (res) {
              //     if (res.confirm) {
              //       console.log('用户点击确定')
              //     }
              //   }
              // })
              wx.navigateTo({
                  url: '../sendMessages/sendMessages'  //跳转到发送消息页面
                })
            }
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
    }else{
      app.msgModal("提示消息：", '请选择发送人');
    }
  },
  //模板数据遍历
  template:function(){
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyMsgtemplate/read/list',
      method: 'POST',
      data: {
        sendShortmsg: 1,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var item = res.data.data.list;
          var items = [];
          for (var i = 0; i < item.length; i++) {
            items.push({ id: item[i].id, text: item[i].tmpContent })
          }
          that.setData({
            items: items,
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