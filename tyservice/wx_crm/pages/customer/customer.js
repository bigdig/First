const app = getApp();

Page({
  /**
   * 页面的初始数据
   */
  data: {
    cBorderBottom: '2rpx solid #FC6D51',
    mBorderBottom : '0rpx',
    cColorText: '#282C3F',
    mColorText: '#9FA3BB',
    taber: true,
    seachState:'1',   //用于判断查询框对应的是哪一个tab;
    seachStateText: '',  //用于判断查询框的值;
    groupList:[],  //分组数据
    customerList : [{
      text: "白名单客户",
      img:"../../image/bku.png",
      url:'./../bListCustomer/bListCustomer',
      id:'3',
    },{
      text : "潜在客户",
      img: "../../image/qzku.png",
      url:'./../bListCustomer/bListCustomer',
      id: '2',
    }],
    mechanismList: [{
      text: "白名单机构",
      img: "../../image/bku.png",
      url: './../bListMechanism/bListMechanism',
      id: '3',
    }, {
      text: "潜在机构",
      img: "../../image/qzku.png",
      url: './../bListMechanism/bListMechanism',
      id: '2',
    }],
    whiteDeadlineOrgShow : false,
    whiteDeadlineOrgNum : 0 , //白名单机构个数
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
    this.groupList()
  },
  tab_customer : function(){
     this.setData({
      whiteDeadlineOrgShow : false,
       taber: true,
       cBorderBottom: '2rpx solid #FC6D51',
       mBorderBottom: '0rpx',
       cColorText: '#282C3F',
       mColorText: '#9FA3BB',
       seachState: '1',
     })
  },
  tab_mechanism : function(){
    this.setData({
      whiteDeadlineOrgShow : true,
      taber: false,
      cBorderBottom: '0rpx',
      mBorderBottom: '2rpx solid #FC6D51',
      cColorText: '#9FA3BB',
      mColorText: '#282C3F',
      seachState:'2',
    })
    //获取当前用户的白名单期限机构个数（默认取一周内到期机构）
    this.getDeadlineOrgs();
  },
  //查询事件
  inputBlur:function(e){
    this.setData({
      seachStateText:e.detail.value,
    })
    var seachState = this.data.seachState;
    var seachStateText=''
    if (this.data.seachStateText != undefined){
      seachStateText = this.data.seachStateText;
    }
    if (this.data.seachStateText != ''){
      if (seachState == '1') {
        wx.navigateTo({
          url: './../bListCustomer/bListCustomer?seachStateText=' + seachStateText + '&custStatusId=' + '3',   //custStatusId用于传是否是白名单客户。3为白名单客户，2位潜在客户
        })
      } else {
        wx.navigateTo({
          url: './../bListMechanism/bListMechanism?seachStateText=' + seachStateText + '&custStatusId=' + '3',
        })
      }
    } 
  },
  customerDetails : function(e){
    //通过e来获取每条数据的数据
    console.log(e.target.dataset.url + e.target.dataset.id);
    wx.navigateTo({
      url: e.target.dataset.url + '?custStatusId=' + e.target.dataset.id + '&seachStateText=' + this.data.seachStateText ,
    })
  },
  mechanismDetails: function (e) {
    //通过e来获取每条数据的数据
    console.log(e.target.dataset.url + e.target.dataset.id);
    wx.navigateTo({
      url: e.target.dataset.url + '?custStatusId=' + e.target.dataset.id + '&seachStateText=' + this.data.seachStateText ,
    })
  },
  //分组点击跳转
  customerGroup:function(e){
    console.log(e.target.dataset.id);
    var id = e.target.dataset.id;
    var custGroupname = e.target.dataset.group;   //分组名称
    wx.navigateTo({
      url: './../bListCustomer/bListCustomer?groupId=' + id + '&custGroupname=' + custGroupname + '&seachStateText=' + this.data.seachStateText , 
    })
  },
  //分组数据请求
  groupList:function(){
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyCustgroup/read/list',
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var lists = res.data.data.list;
          that.setData({
            groupList: lists,
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
  // 新增客户
  add_customer:function(){
    wx.navigateTo({
      url: '../add_customer/add_customer'
    })
  },
  // 新建分组
  add_grouping: function () {
    app.globalData.custId_customer = [];
    wx.navigateTo({
      url: '../selectCustomer/selectCustomer?group='+1,
    })
  },
  //扫一扫
  scanImg: function () {
    app.globalData.scanImg = true;
    //上传图片进行识别
    wx.chooseImage({
      success: function (res) {
        var tempFilePaths = res.tempFilePaths
        wx.uploadFile({
          url: app.crmRequestHost + '/uploadfile/ocrToCard',
          header: {
            'content-type': 'application/x-www-form-urlencoded', // 默认值
            'cookie': app.globalData.cookieInfo
          },
          filePath: tempFilePaths[0],
          name: 'file',
          success: function (res) {
            app.globalData.scanImg = true;
            if (res.statusCode != 200) {
              app.msgModal("提示消息：", "上传图片出错，请稍后重试！");
              return;
            }
            var data = JSON.parse(res.data);
            if (data.httpCode == 200) {
              var customer = {}
              customer.orgName = data.data.comp.length > 0 ? data.data.comp[0] : "";
              customer.userName = data.data.name.length > 0 ? data.data.name[0] : "";
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
              console.log("ocrTestResult:", res);
            } else {
              app.msgModal("提示消息：", "识别名片出错！" + data.msg);
            }
          },
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.groupList()
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
  getDeadlineOrgs : function () {
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyReport/read/getDeadlineOrgs',
      method: 'POST',
      data: {
        whiteDeadline :   '1' ,   //默认一周内的白名单过期机构
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          that.setData({
            whiteDeadlineOrgNum : res.data.data,
          })
        } else if (res.data.httpCode == 401) {
          wx.redirectTo({
            url: '/pages/login/login'
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
})