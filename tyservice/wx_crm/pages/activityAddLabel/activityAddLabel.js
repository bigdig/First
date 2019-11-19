// pages/activityDetail/activityDetail.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    priority_color : ['','4rpx solid #FFAD4C','4rpx solid #FC6D51'],
    custBorder : ['2px solid #FFC24C','2px solid #26BB85'],
    labelBackColor : ['#ECFBF1','#D6E0F8','#FFE3DD','#DAF0F7','#E4DCF9'],
    labelColor : ['#23D75F','#3466D3','#FC6E57','#46B6DB','#7549E9'],
    priority_color : ['','4rpx solid #FFAD4C','4rpx solid #FC6D51'],
    addActContentSuccess : false, //添加活动内容返回成功标志位
    hasAll : false,
    selCustomers : [],
    actContentAnimation : null ,
    showModal : false , 
    showActStatus  : false ,
    actInfo : null , // 活动详情
    ACTIVITYSTATUS  : null ,
    ACTIVITYTYPE : null ,
    PRIORITYLEVEL : null,
    SysWorkAreas : null,
    hiddenModal : true,
    activitysignData : null, // 当前活动已经报名客户
    tempActId  : null,
    allGoodsFilte: [
      { name: '参会客户', value: '0', checked: false ,img : '../../image/customer.png', isNotNull : false,},
      { name: '活动地点', value: '1', checked: false, img: '../../image/address.png', isNotNull : false,},
      { name: '活动内容', value: '2', checked: false, img: '../../image/content.png',isNotNull : false, },
      { name: '添加标签', value: '3', checked: false, img: '../../image/label.png',isNotNull : false, },
      { name: '优先级', value: '4', checked: false, img: '../../image/priority.png', isNotNull : false,},
      { name: '附件', value: '5', checked: false, img: '../../image/attaches.png',isNotNull : false, },
      { name: '备注', value: '6', checked: false, img: '../../image/remark.png', isNotNull : false,},
    ], 
    smsMessges : [],
    usreId   :null ,
    isShare : false, // 是否是从分享页面跳转过来
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("onload")
    console.log("myactivityDetail::",options)
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
    var animation = wx.createAnimation({
      duration: 500,
      transformOrigin: "50% 50%",
      timingFunction: 'ease',
    })
    this.animation = animation;
    //跳转页面返回首页按钮处理
    if(options.isShare){
      this.setData({
        isShare : options.isShare,
      })
    }
  },
  getActInfo  : function(){
    var that =this;
    if(this.data.tempActId  && app.globalData.cookieInfo){
      wx.request({
        url: app.crmRequestHost + '/ty/tyActivity/read/detail',  
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
            that.data.activitysignData = res.data.data.activitysignData;
            that.data.smsMessges = res.data.data.smsMessges;
            if(res.data.data != null && res.data.data.tyActivityBean){
              tempActBean = that.actInfoTimeFormat(res.data.data.tyActivityBean);
              //只有自己才能看到关闭活动checkbox
              // that.showActStatusCheckbox(tempActBean);
              //加载图片
              tempActBean = that.loadingImages(tempActBean);
              //显示图标
              tempActBean = that.loadingLabels(tempActBean);
            }
            if(that.data.activitysignData){
              that.data.selCustomers = that.data.activitysignData;
              // for(var i in that.data.activitysignData){
              //   // that.data.selCustomers = that.data.selCustomers.concat(that.data.activitysignData[i]);
              //   that.data.selCustomers.push(that.data.activitysignData[i]);
              //   // for(var j of that.data.activitysignData[i]){
              //   //   that.data.selCustomers.push(j);
              //   // }
              // }
            }
            that.setData({
              smsMessges : that.data.smsMessges ,
              selCustomers : that.data.selCustomers, // 客户列表
              activitysignData : that.data.activitysignData,
              actInfo : tempActBean,
              ACTIVITYSTATUS : res.data.dicts.ACTIVITYSTATUS,  // 活动状态
              ACTIVITYTYPE : app.activityTypeFilter(res.data.dicts.ACTIVITYTYPE),   // 活动类型
              PRIORITYLEVEL : res.data.dicts.PRIORITYLEVEL,   //活动优先级别
              SysWorkAreas : res.data.dicts.SysWorkAreas,    //工作地点
            })
            //判断有哪些内容是有的，哪些是没有的(已经有的内容不需要再次添加)
            that.checkActContent();
            //添加内容之后进行第二次人数校验
            // that.data.allGoodsFilte[0].checked = false;
            // if(that.data.selCustomers.length > that.data.actInfo.perLimit){
            //   that.data.allGoodsFilte[0].isNotNull = true;
            // }
            // that.setData({
            //   allGoodsFilte : that.data.allGoodsFilte,
            // })
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
    app.globalData.custId_customer = [];
  },
  checkActContent : function(e){
    var actInfo = this.data.actInfo;
    //创建人才拥有更改资格
    if(actInfo.createBy == app.globalData.userInfo.id){
      this.data.hasAll = false;
    }else{
      this.data.hasAll = true;
    }
    //判断对应的内容项是否为空
    if(actInfo.locale){
      this.data.allGoodsFilte[1].isNotNull = true;
    }
    if(actInfo.content){
      this.data.allGoodsFilte[2].isNotNull = true;
    }
    if(actInfo.labels){
      this.data.allGoodsFilte[3].isNotNull = true;
    }
    if(actInfo.priorityLevel){
      this.data.allGoodsFilte[4].isNotNull = true;
    }
    if(actInfo.attachs){
      this.data.allGoodsFilte[5].isNotNull = true;
    }
    if(actInfo.remark){
      this.data.allGoodsFilte[6].isNotNull = true;
    }
    // if(this.data.selCustomers && actInfo.locale && actInfo.content && actInfo.labels && actInfo.priorityLevel && actInfo.attachs && actInfo.remark){
    //   this.data.hasAll = true;
    // }
    this.setData({
      hasAll : this.data.hasAll,
      allGoodsFilte : this.data.allGoodsFilte,
    })
  },
  serviceValChange: function (e) {
    var allGoodsFilte = this.data.allGoodsFilte;
    var checkArr = e.detail.value;
    for (var i = 0; i < allGoodsFilte.length; i++) {
      if (checkArr.indexOf(i + "") != -1) {
        allGoodsFilte[i].checked = true;
      } else {
        allGoodsFilte[i].checked = false;
      }
    }
    this.setData({
      allGoodsFilte: allGoodsFilte
    })
  } ,
  submit:function(){
    this.setData({
      'showModal': false,
    })
  },
  //跳转发送短信页面
  toSendMsg:function(){
    //获取客户信息传到短信模板页面
    var list = this.data.selCustomers;
    console.log(list);
    app.globalData.custId_customer = list;
    if (app.globalData.custId_customer.length == 0) {
      app.msgModal("提示消息：", "客户信息不能为空");
    } else {
      wx.navigateTo({
        url: '../sendMessages/sendMessages?activityId='+this.data.actInfo.id,  //跳转到短信模板页面
      })
    }
  },
  add_label :function(){
    this.setData({
      'showModal': true,
    })
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    console.log("onshow")
    this.setData({
      showModal : false,
    })
    // if(this.data.addActContentSuccess){
    //   this.getActInfo();
    // }
    // this.checkActContent();
    // 计算客户的人数是否超多最大值
    // this.data.allGoodsFilte[0].checked = false;
    // if(this.data.selCustomers.length > this.data.actInfo.perLimit){
    //   this.data.allGoodsFilte[0].isNotNull = true;
    // }
    // this.setData({
    //   allGoodsFilte : this.data.allGoodsFilte,
    // })

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
  onShareAppMessage: function (res) {
    console.log(res)
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title: this.data.actInfo.title,//'客户服务活动分享',
      path: '/pages/activityDetail/activityDetail?actId=' + this.data.actInfo.id+'&isShare=true',
      //imageUrl: './../../image/logo.png',
      success: function (res) {
        // 转发成功
      },
      fail: function (res) {
        // 转发失败
      }
    }
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
    record.beginTimeText = parseInt(tempBeginTimeString.substring(0,2))  
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
  toSelectCustomers  : function(){
    wx.navigateTo({
      url: './../selectCustomer/selectCustomer?actCustomersFlag=true',
    })
  },
   // 添加活动内容
  addActContent : function(e){
    this.startActContentModalAnimation(true);
  },
  startActContentModalAnimation : function(show){
    var that = this
    if (show) {
      that.animation.translateY(0 + 'vh').step()
    } else {
      that.animation.translateY(40 + 'vh').step()
    }
    for(var i of this.data.allGoodsFilte){
      if(i.checked){
        i.checked = false;
      }
    }
    that.setData({
      allGoodsFilte : that.data.allGoodsFilte,
      actContentAnimation: that.animation.export(),
      showModal: show,
    })
  },
  cfmAddActContent : function(e){
    var paramStr = '';
    var allGoodsFilte = this.data.allGoodsFilte;
    if(allGoodsFilte[0].checked){
      paramStr += "addCustomer=true&";
    }
    if(allGoodsFilte[1].checked){
      paramStr += "addLocale=true&";
    }
    if(allGoodsFilte[2].checked){
      paramStr += "addContent=true&";
    }
    if(allGoodsFilte[3].checked){
      paramStr += "addLabel=true&";
    }
    if(allGoodsFilte[4].checked){
      paramStr += "addPriority=true&";
    }
    if(allGoodsFilte[5].checked){
      paramStr += "addAttaches=true&";
    }
    if(allGoodsFilte[6].checked){
      paramStr += "addRemark=true";
    }
    //没有选任何一个选项
    if(!allGoodsFilte[0].checked && !allGoodsFilte[1].checked && !allGoodsFilte[2].checked 
      && !allGoodsFilte[3].checked && !allGoodsFilte[4].checked && !allGoodsFilte[5].checked &&!allGoodsFilte[6].checked){
        this.setData({
          showModal : false,
        })
      }else{
        wx.navigateTo({
          url: './../addActContent/addActContent?actId='+this.data.actInfo.id+"&"+ paramStr,
        })
      }
  },
  shareAct :function(e){
    this.setData({
      hiddenModal: false,
    })
  },
  // showActStatusCheckbox : function(actBean){
  //   if(app.globalData.userInfo.id == actBean.createBy || app.globalData.userInfo.id == actBean.contactId){
  //     this.data.showActStatus = true;
  //   }else{
  //     this.data.showActStatus = false;
  //   }
  //   this.setData({
  //     showActStatus : this.data.showActStatus,
  //   })
  // },
  loadingImages :function(tempActBean){
    var imgPathStr = tempActBean.attachs + "";
    tempActBean.attachImgs = imgPathStr.split(",");
    for(var i = 0 ; i < tempActBean.attachImgs.length ; i ++){
      tempActBean.attachImgs[i] = app.crmRequestHost + tempActBean.attachImgs[i];
    }
    return tempActBean;
  },
  loadingLabels : function(tempActBean){
    var labelStr = tempActBean.labels + "";
    tempActBean.labelsText = labelStr.split(" ");
    return tempActBean;
  },
  previewImg  : function(e){
    wx.previewImage({
      current: '', // 当前显示图片的http链接
      urls: [e.target.dataset.image], // 需要预览的图片http链接列表
      success : function(res){
        console.log("previewImage::",res);
      },
      fail : function(res){
        app.msgModal("提示消息：","预览图片失败，请稍后重试！");
      },
      complete : function(){
        app.globalData.previewImg = true;
      }
    })
  },
  hideAddContent : function(){
    this.setData({
      showModal : false,
    })
  },
  //更新活动内容
  updateActContent : function(e){
    wx.navigateTo({
      url: './../addMyAct/addMyAct?actId='+this.data.actInfo.id,
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
  hideShareModal : function(){
    this.setData({
      hiddenModal : true,
    })
  },
  backToHome : function (e){
    wx.reLaunch({
      url: './../index/index'
    })
  },
})