// pages/activityDetail/activityDetail.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
     custBorder : ['2px solid #FFC24C','2px solid #26BB85'],
     labelBackColor : ['#ECFBF1','#D6E0F8','#FFE3DD','#DAF0F7','#E4DCF9'],
     labelColor : ['#23D75F','#3466D3','#FC6E57','#46B6DB','#7549E9'],
     priority_color : ['','4rpx solid #FFAD4C','4rpx solid #FC6D51'],
     actInfo : null , // 活动详情
     ACTIVITYSTATUS  : null ,
     ACTIVITYTYPE : null ,
     PRIORITYLEVEL : null,
     SysWorkAreas : null,
     hiddenModal : true,
     selCustomers : null , //用户提交的客户信息
     activitysignData: null, // 当前活动已经报名客户activitysignData
     tempActId  : null,
     showActStatus : null,
    userId : null, //当前用户id
    testList : [{"title": "test1","activityTypeText": "沙龙","totalLimit" : '10',"beginDate" : 20180416 ,"endDate": 20180511,"beginTime" : 50000,"endTime" : 600000, "activityStatusText" : "已结束" ,"createName" : '测试啊啊'},
    {"title": "test2","activityTypeText": "沙龙","totalLimit" : '10',"beginDate" : 20180416 ,"endDate": 20180511,"beginTime" : 50000,"endTime" : 600000, "activityStatusText" : "已结束" ,"createName" : '测试啊啊'}],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("onload")
    //延时加载，首页数据初始化
    wx.showLoading({
      title: '加载中',
    })
    setTimeout(function(){
      wx.hideLoading()
    },1000)
    if(options.actId){
      this.setData({
        usreId : app.globalData.userInfo.id,
        tempActId : options.actId,
      })
      this.getActInfo();
    }
  },
  getActInfo  : function(){
    var that =this;
    if(this.data.tempActId  && app.globalData.cookieInfo){
      wx.request({
        url: app.crmRequestHost + '/ty/tyActivity/read/subActlist',  
        method: 'POST',
        data: {
          id :   ( this.data.tempActId == null ? "" : this.data.tempActId),   //活动id
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          console.log("查询活动详情返回的res：", res)
          if(res.data.httpCode == 200){
            var tempActBean = null;
            if(res.data.data != null && res.data.data){
              tempActBean = that.actInfoTimeFormat(res.data.data);
              //只有自己和联系人才能结束活动
              that.data.showActStatus = that.showActStatusCheckbox(tempActBean);
            }

            that.setData({
              userId : app.globalData.userInfo.id,
              showActStatus:that.data.showActStatus,
              actInfo : tempActBean,
              ACTIVITYSTATUS : res.data.dicts.ACTIVITYSTATUS,  // 活动状态
              ACTIVITYTYPE : res.data.dicts.ACTIVITYTYPE,   // 活动类型
              PRIORITYLEVEL : res.data.dicts.PRIORITYLEVEL,   //活动优先级别
              SysWorkAreas : res.data.dicts.SysWorkAreas,    //工作地点
            })
             
          }else if(res.data.httpCode == 401){
            wx.redirectTo({
              url: './../login/login'
            })
          }else{
            app.msgModal("提示消息：", res.data.msg);
          }
        },
        fail : function (res){
          app.msgModal("提示消息：", res.data.msg);
        }
      })
    }
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    console.log("onload2")
    app.globalData.custId_customer = [];
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    //页面重新加载,展现加入的客户信息
    var that = this;
    this.setData({
      showModal : false,
    })
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    console.log("onload4")
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    console.log("onload5")
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    console.log("onload6")
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("onload7")
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function (res) {
    console.log(res)
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title:this.data.actInfo.title,// '客户服务活动分享',
      path: '/pages/activityDetail/activityDetail?actId=' + this.data.actInfo.id,
      //imageUrl: './../../image/logo.png',
      success: function (res) {
        // 转发成功
      },
      fail: function (res) {
        // 转发失败
      }
    }
  },
  shareAct :function(e){
    this.setData({
      hiddenModal: false,
    })
  },
  actInfoTimeFormat : function(record){
    var tempBeginDateString = record.beginDate +"";
    var tempEndDateString = record.endDate +"";
    var tempBeginTimeString = record.beginTime +"";
    var tempendTimeString = record.endTime +"";
    if(tempBeginTimeString.length == 1){
      tempBeginTimeString = "00000"+tempBeginTimeString;
    }else if(tempBeginTimeString.length == 5){
      tempBeginTimeString = "0"+tempBeginTimeString;
    }
    if(tempendTimeString.length == 1){
      tempendTimeString = "00000"+tempendTimeString;
    }else if(tempendTimeString.length == 5){
      tempendTimeString = "0"+tempendTimeString;
    }
    record.beginDateText = tempBeginDateString.substring(2,4)+"."+tempBeginDateString.substring(4,6)+"."+tempBeginDateString.substring(6,8);
    record.endDateText = tempEndDateString.substring(2,4)+"."+tempEndDateString.substring(4,6)+"."+tempEndDateString.substring(6,8);
    record.beginTimeText =  parseInt(tempBeginTimeString.substring(0,2)) 
                       +":"+tempBeginTimeString.substring(2,4);
    record.endTimeText = parseInt(tempendTimeString.substring(0,2)) 
                      +":"+tempendTimeString.substring(2,4);
    return record;
  },
  closeMask: function() {
    this.setData({
      hiddenModal: true,
    })
  },
  updateActStatus :function(e){
    var that = this;
    if(e.target.dataset.status == null || e.target.dataset.status == "0" ){
      this.data.actInfo.activityStatus = "1";
    }else{
      this.data.actInfo.activityStatus = '0';
    }
    if(this.data.actInfo){
      this.data.actInfo = app.clearNullValue(this.data.actInfo);
      wx.request({
        url: app.crmRequestHost + '/ty/tyActivity/update',  
        method: 'POST',
        data: this.data.actInfo,
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          console.log("查询活动详情返回的res：", res)
          if(res.data.httpCode == 200){
            that.setData({
              actInfo : that.data.actInfo,
            })
           
          }else if(res.data.httpCode == 401){
            wx.redirectTo({
              url: './../login/login'
            })
          }else{
            that.setData({
              actInfo : that.data.actInfo,
            })
            app.msgModal("提示消息：", res.data.msg);
          }
        },
        fail : function (res){
          that.setData({
            actInfo : that.data.actInfo,
          })
          app.msgModal("提示消息：", res.data.msg);
        }
      })
    }
  },
  showActStatusCheckbox : function(actBean){
    if(app.globalData.userInfo.id == actBean.createBy || app.globalData.userInfo.id == actBean.contactId){
      return true;
    }else{
      return false;
    }
  },
  hideShareModal : function(){
    this.setData({
      hiddenModal : true,
    })
  },
  //删除活动
  deleteAct : function(){
    var that = this;
    if(this.data.actInfo && this.data.actInfo.deleteFlag == '0'){
      wx.showModal({
        title: '删除提示',
        content: '您确定删除此次活动吗？',
        success: function(res) {
          if (res.confirm) {
            console.log('用户点击确定')
            that.data.actInfo = app.clearNullValue(that.data.actInfo);
            that.data.actInfo.deleteFlag = '1';
            wx.request({
              url: app.crmRequestHost + '/ty/tyActivity/update',  
              method: 'POST',
              data: that.data.actInfo,
              header: {
                'content-type': 'application/x-www-form-urlencoded', // 默认值
                'cookie': app.globalData.cookieInfo
              },
              success: function (res) {
                console.log("查询活动详情返回的res：", res)
                if(res.data.httpCode == 200){
                //删除活动
                wx.navigateBack({
                  delta: 1
                })
                }else if(res.data.httpCode == 401){
                  wx.redirectTo({
                    url: './../login/login'
                  })
                }else{
                  app.msgModal("提示消息：", res.data.msg);
                }
              },
              fail : function (res){
                app.msgModal("提示消息：", res.data.msg);
              }
            })
          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })
    }
  },
  toActDetail  : function(e){
    console.log("toActDetail::",e)
    var actId = e.currentTarget.dataset.id;
    wx.navigateTo({
          url : './../activityDetail/activityDetail?actId='+actId,
     })
  },
})