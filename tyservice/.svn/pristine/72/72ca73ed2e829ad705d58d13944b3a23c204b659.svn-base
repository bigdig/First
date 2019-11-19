// pages/customerInformation/customerInformation.js
const app = getApp();
//var pageNum = 1;
var pageSize = 15;
var custStatus = 3;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    id:'', //从客户详情跳转过来，带一个客户id
    custId:[],  //用于数据列表
    custLIstanbul:[], //用于被选中的数据
    scrollTop: 0,
    scrollHeight: 0,
    hidden: true,
    pageNum : 1,
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var custId = [];
    var lists = app.globalData.custId_customer;
    //var lists = JSON.parse(options.custId);
    for (var i = 0; i < lists.length; i++){
      lists[i].checked = true;
    }
    this.setData({
      custId: lists,
    })
    // if (options.id != ""){
    //   this.setData({
    //     id: options.id,
    //   })
    //   this.detailList();
    // }else{
    //   custId = app.globalData.custId_customer;
    //   for (var i = 0; i < custId.length;i++){
    //     custId[i].checked = true;
    //   }
    //   this.setData({
    //     custId: custId,
    //   })
    // }
    var that = this;
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
    this.listTraversal();
  },
  //选择客户
  checkboxChange: function (e) {
    var custId = this.data.custId;
    var obj = e.currentTarget.dataset;
    console.log(obj)
    if (obj.checked){
      for (var i = 0; i < custId.length; i++) {
        if (custId[i].id == obj.id){
          custId[i].checked = false;
        } 
      }
    }else{
      for (var i = 0; i < custId.length; i++) {
        if (custId[i].id == obj.id) {
          custId[i].checked = true;
        }
      }
    }
    this.setData({
      custId: custId
    })
  },
  //页面滑动到底部
  bindDownLoad: function () {
    var that = this;
    this.listTraversal();
  },
  //完成
  submit:function(){
    var custLIstanbul = this.data.custLIstanbul;
    var custId = this.data.custId;
    for (var i = 0; i < custId.length;i++){
      if (custId[i].checked){
        custLIstanbul.push(custId[i]);
      }
    }
    if (custLIstanbul.length == 0){
      app.msgModal("提示消息：", "请选择客户");
    }else{
      app.globalData.custId_customer = custLIstanbul; //吧选中数据从新赋值给全局app.globalData.custId_customer;
      //var custId_customer = JSON.stringify(custLIstanbul);
      wx.navigateTo({
        url: '../sendMessages/sendMessages',    //重新跳转到发送短信消息页面
      })
    }
  },
  //数据请求
  listTraversal: function () {
    var that = this;
    that.setData({
      hidden: false
    });
    wx.request({
      url: app.crmRequestHost + '/ty/tyOrgcustomer/read/list',
      method: 'POST',
      data: {
        custStatus: custStatus,
        pageNum: that.data.pageNum,
        pageSize: pageSize,
        orderBy: 'org_id asc ,id desc',
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var lists = res.data.data.list;
          var custId= that.data.custId;
          for (var i = 0; i < lists.length;i++){
            lists[i].checked = false;
          }
          for (var i = 0; i < lists.length; i++) {
            var findExist =false;
            for (var j = 0; j < custId.length; j++) {
              if (lists[i].id == custId[j].id) {
                findExist=true;
                break;
              }
            }
            if (!findExist) {
              custId.push(lists[i]);
            }
          }
          that.setData({
            custId: custId,
            hidden: true,
          })
          console.log(that.data.custId);
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
  // //如果是单条数据ID过来,请求单挑详情
  // detailList:function(){
  //   var that = this;
  //   wx.request({
  //     url: app.crmRequestHost + '/ty/tyOrgcustomer/read/detail',
  //     method: 'POST',
  //     data: {
  //       id: that.data.id,
  //     },
  //     header: {
  //       'content-type': 'application/x-www-form-urlencoded', // 默认值
  //       'cookie': app.globalData.cookieInfo
  //     },
  //     success: function (res) {
  //       if (res.data.httpCode == 200) {
  //         var lists = res.data.data;
  //         var custId = [];
  //         lists.checked = true;
  //         custId.push(lists);
  //         that.setData({
  //           custId: custId,
  //         })
  //       } else if (res.data.httpCode == 401) {
  //         wx.redirectTo({
  //           url: './../login/login'
  //         })
  //       } else {
  //         app.msgModal("提示消息：", "请求白名单列表失败，请稍后重试！");
  //       }
  //     },
  //     fail: function (res) {
  //       app.msgModal("提示消息：", "请求白名单列表失败，请稍后重试！");
  //     }
  //   })
  // },
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