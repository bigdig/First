const app  = getApp()

Page({
  /**
   * 页面的初始数据
   */
  data: {
    user: null,
    nameImg:"",
    myActivities:0,
    ActiveCustomers:0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    console.log("1111111111111", app.globalData.userInfo)
    that.setData({
      user: app.globalData.userInfo,
      myActivities: app.globalData.myActivityList,
      ActiveCustomers: app.globalData.activateCustomers,
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function (e) {
    var that = this;
    // console.log("app.globalData::",app.globalData)
    // if(app.globalData.wxUserInfo == null){
    //   wx.getUserInfo({
    //     success: function (res) {
    //       app.globalData.wxUserInfo = res.userInfo;
    //       console.log("userInfo::",res.userInfo);
    //       if (that.userInfoReadyCallback) {
    //         that.userInfoReadyCallback(res)
    //       }
    //       that.setData({
    //         nameImg : app.globalData.wxUserInfo.avatarUrl,
    //         user: app.globalData.userInfo
    //       })
    //     }
    //   })
    // }else{
    //   that.setData({
    //     nameImg : app.globalData.wxUserInfo.avatarUrl,
    //   })
    // }
   
  },
  myActivities:function(){
    app.globalData.currentTab = '1';
    wx.switchTab({
      url: '../index/index',
      success :function(){
        var arr = getCurrentPages();
        arr[0].setData({
          addHistoryMyActFlag : false,
        })
      }
    })
  },
  ActiveCustomers: function () {
    wx.navigateTo({
      url: '../ActiveCustomers/ActiveCustomers'
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
  scanImg : function(){
    app.globalData.scanImg = true;
    //选择图片
    // wx.chooseImage({
    //   count: 1, // 默认9
    //   sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
    //   sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
    //   success: function (res) {
    //     // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
    //     var tempFilePaths = res.tempFilePaths
    //    //获取图片信息
    //    wx.getImageInfo({
    //       src: tempFilePaths[0],
    //       success: function (res) {
    //         console.log("imgInfo::",res)
    //         //显示图片
    //         wx.previewImage({
    //           current: '', // 当前显示图片的http链接
    //           urls: [res.path], // 需要预览的图片http链接列表
    //           success : function(res){
    //             console.log("previewImage::",res);
    //           },
    //           fail : function(res){

    //           }
    //         })
    //       }
    //     })
    //   }
    // })
    //上传图片进行识别
    wx.chooseImage({
      success: function(res) {
        var tempFilePaths = res.tempFilePaths
        wx.uploadFile({
          url: app.crmRequestHost + '/uploadfile/ocrToCard',  
          header: {
            'content-type': 'application/x-www-form-urlencoded', // 默认值
            'cookie': app.globalData.cookieInfo
          },
          filePath: tempFilePaths[0],
          name: 'file',
          success: function(res){
            app.globalData.scanImg = true;
            if(res.statusCode!=200){
              app.msgModal("提示消息：", "上传图片出错，请稍后重试！");
              return;
            }
            var data = JSON.parse(res.data);
            if(data.httpCode == 200){
              var customer={}
              customer.orgName = data.data.comp.length > 0 ? data.data.comp[0] : "";
              customer.userName = data.data.name.length > 0 ? data.data.name[0]:"";
              customer.custTel = data.data.tel.length > 0 ? data.data.tel[0] : "";
              customer.custMobile = data.data.mobile.length > 0 ? data.data.mobile[0] : "";
              customer.custEmail = data.data.email.length > 0 ? data.data.email[0] : "";
              customer.department = data.data.dept.length > 0 ? data.data.dept[0] : "";
              customer.title = data.data.title.length > 0 ? data.data.title[0] : "";
              var customerStr = JSON.stringify(customer);
              console.log(customerStr);
              wx.navigateTo({
                url: '../add_customer/add_customer?customerStr=' + customerStr,  //跳转到用户新增页面
              })
              console.log("ocrTestResult:",res);
            }else{
              app.msgModal("提示消息：", "识别名片出错！" + data.msg);
            }
          },
        })
      }
    })
  },
})