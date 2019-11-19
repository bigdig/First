// pages/activityDetail/activityDetail.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
     signStatusColor:['#000','#ffc24c','#9FA3BB'],
     custBorder : ['2px solid #FFC24C','2px solid #26BB85'],
     labelBackColor : ['#ECFBF1','#D6E0F8','#FFE3DD','#DAF0F7','#E4DCF9'],
     labelColor : ['#23D75F','#3466D3','#FC6E57','#46B6DB','#7549E9'],
     priority_color : ['','4rpx solid #FFAD4C','4rpx solid #FC6D51'],
     actInfo : null , // 活动详情
     hasAll : false,  //是否有所有活动内容部分
     ACTIVITYSTATUS  : null ,
     ACTIVITYTYPE : null ,
     PRIORITYLEVEL : null,
     SysWorkAreas : null,
     hiddenModal : true,
     selCustomers : null , //用户提交的客户信息
     activitysignData: null, // 当前活动已经报名客户activitysignData
     tempActId  : null,
     showActStatus : null,
     allGoodsFilte: [
      { name: '参会客户', value: '0', checked: false ,img : '../../image/customer.png', isNotNull : false,},
      { name: '活动地点', value: '1', checked: false, img: '../../image/address.png', isNotNull : false,},
      { name: '活动内容', value: '2', checked: false, img: '../../image/content.png',isNotNull : false, },
      { name: '添加标签', value: '3', checked: false, img: '../../image/label.png',isNotNull : false, },
      { name: '优先级', value: '4', checked: false, img: '../../image/priority.png', isNotNull : false,},
      { name: '附件', value: '5', checked: false, img: '../../image/attaches.png',isNotNull : false, },
      { name: '备注', value: '6', checked: false, img: '../../image/remark.png', isNotNull : false,},
    ], 
    smsMessges : [], //已发送的短信消息
    userId : null, //当前用户id
    isShare : false, // 是否是从分享页面跳转过来
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("onload:activityDetail::",options)
    //跳转页面返回首页按钮处理
    if (options.isShare) {
      this.setData({
        isShare: options.isShare == 'true' ? true : false,
      })
    }
    //延时加载，首页数据初始化
    wx.showLoading({
      title: '加载中',
    })
    setTimeout(function(){
      wx.hideLoading()
    },1000)
    console.log('onload:Loading getActInfo:' + options.actId + ",isShare:" + this.data.isShare)
    if(!this.data.isShare){
      if (options.actId) {
        this.setData({
          usreId: app.globalData.userInfo.id,
          tempActId: options.actId,
        })
        this.getActInfo();
      }
    }else{
      if (options.actId) {
        this.setData({
          tempActId: options.actId,
        })
        this.getActInfoAndWaitLogin();
      }
    }

    var animation = wx.createAnimation({
      duration: 500,
      transformOrigin: "50% 50%",
      timingFunction: 'ease',
    })
    this.animation = animation;

  },

  getActInfoAndWaitLogin:function(){
    var that = this;
    console.log('getActInfoAndWaitLogin');
    if (app.globalData.cookieInfo && app.globalData.userInfo){
      this.setData({
        usreId: app.globalData.userInfo.id,
      })
      console.log('cookieInfo'+app.globalData.cookieInfo);
      that.getActInfo();
    }else{
      console.log('WaitLogin');
      setTimeout(
        that.getActInfoAndWaitLogin,1000
      )
    }
  },

  getActInfo : function(){
    var that =this;
    if(that.data.tempActId  && app.globalData.cookieInfo){
      wx.request({
        url: app.crmRequestHost + '/ty/tyActivity/read/detail',
        method: 'POST',
        data: {
          id: (that.data.tempActId == null ? "" : that.data.tempActId),   //活动id
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
            //已发送消息的消息记录
            that.data.smsMessges = res.data.data.smsMessges;
            if(res.data.data != null && res.data.data.tyActivityBean){
              tempActBean = that.actInfoTimeFormat(res.data.data.tyActivityBean);
              //加载图片
              tempActBean = that.loadingImages(tempActBean);
              //显示图标
              tempActBean = that.loadingLabels(tempActBean);
              //只有自己和联系人才能结束活动
              that.data.showActStatus = that.showActStatusCheckbox(tempActBean);
            }
            
            that.setData({
              userId : app.globalData.userInfo.id,
              smsMessges : that.data.smsMessges,
              showActStatus:that.data.showActStatus,
              activitysignData : that.data.activitysignData,
              actInfo : tempActBean,
              ACTIVITYSTATUS : res.data.dicts.ACTIVITYSTATUS,  // 活动状态
              ACTIVITYTYPE : res.data.dicts.ACTIVITYTYPE,   // 活动类型
              PRIORITYLEVEL : res.data.dicts.PRIORITYLEVEL,   //活动优先级别
              SysWorkAreas : res.data.dicts.SysWorkAreas,    //工作地点
            })
             //判断有哪些内容是有的，哪些是没有的(已经有的内容不需要再次添加)
             that.checkActContent();
             
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
  checkActContent : function(e){
    var actInfo = this.data.actInfo;
    var userIsInStaffListFlag  = false;
    //判断当前用户是否是活动创建人，活动联系人，活动参与人
    userIsInStaffListFlag = this.currentUserIsInStaffList(actInfo.staffList);
    if(actInfo.createBy == app.globalData.userInfo.id || actInfo.contactId == app.globalData.userInfo.id || userIsInStaffListFlag){
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
    // if(actInfo.locale && actInfo.content && actInfo.labels && actInfo.priorityLevel && actInfo.attachs && actInfo.remark){
    //   this.data.hasAll = true;   //允许当前用户进行所有修改（不是新增）
    // }
    this.setData({
      hasAll : this.data.hasAll,
      allGoodsFilte : this.data.allGoodsFilte,
    })
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
    // setTimeout(function(){
    //   that.addCustomerlistAndSubmit();
    // },300);
    // if(this.data.addActContentSuccess){
    //   that.getActInfo();
    // }
  },
  //遍历导入的客户数据
  //  addCustomerlistAndSubmit : function(){
  //   if(this.data.selCustomers && this.data.selCustomers.length > 0){
  //     // this.data.activitysignData = {};
  //     if(this.data.activitysignData[app.globalData.userInfo.staffName] && this.data.activitysignData[app.globalData.userInfo.staffName].length > 0){
  //       this.data.activitysignData[app.globalData.userInfo.staffName] = this.data.activitysignData[app.globalData.userInfo.staffName].concat(this.data.selCustomers);
  //     }else{
  //       this.data.activitysignData[app.globalData.userInfo.staffName] = this.data.selCustomers;
  //     }
  //     this.setData({
  //       activitysignData : this.data.activitysignData,
  //     })
  //   }
  //  },
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
      title: this.data.actInfo.title,//'客户服务活动分享',
      path: '/pages/activityDetail/activityDetail?actId=' + this.data.actInfo.id+"&isShare=true",
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
  toSelOrgCustomer : function(){
    wx.navigateTo({
      url: './../selectCustomer/selectCustomer?cooActAddCustFlag=true&actCustomersFlag=true&cooperateCustomerLimit='+((this.data.actInfo.totalLimit  + 2  - this.data.actInfo.joinNum ) > 2 ? 2 : (this.data.actInfo.totalLimit  + 2  - this.data.actInfo.joinNum ))+'&actId='+this.data.actInfo.id,
    })
  },
  toSendMsg:function(){
    //获取客户信息传到短信模板页面
    var list = this.data.activitysignData;
    var custId_customerArr=[];
    
    for (var i = 0; i < list.length; i++) {
        var cust = {};
        cust.id = list[i].custId;
        cust.custName = list[i].custName;
        cust.orgName = list[i].orgName;

        custId_customerArr.push(cust);
      }
    
    app.globalData.custId_customer = custId_customerArr;
    console.log(app.globalData.custId_customer)

    if (app.globalData.custId_customer.length == 0){
      app.msgModal("提示消息：", "客户信息不能为空");
    }else{
      wx.navigateTo({
        url: '../sendMessages/sendMessages?activityId='+this.data.actInfo.id,  //跳转到短信模板页面
      })
    }
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
  // downloadImages : function(fileUrl,fileName){
  //   var that = this;
  //   wx.downloadFile({
  //     url: app.crmRequestHost + '/uploadfile/downActFile?fileUrl='+fileUrl+"&fileName="+fileName, //仅为示例，并非真实的资源
  //     header: {
  //       'content-type': 'application/x-www-form-urlencoded', // 默认值
  //       'cookie': app.globalData.cookieInfo
  //     },
  //     success: function(res) {
  //       // 只要服务器有响应数据，就会把响应内容写入文件并进入 success 回调，业务需要自行判断是否下载到了想要的内容
  //       if (res.statusCode === 200) {
  //         console.log("downloadFile::",res);
  //         that.setData({
  //           testImg : res.tempFilePath
  //         })
  //         //显示图片
  //         wx.previewImage({
  //           current: '', // 当前显示图片的http链接
  //           urls: [res.tempFilePath], // 需要预览的图片http链接列表
  //           success : function(res){
  //             console.log("previewImage::",res);
  //           },
  //           fail : function(res){
  //             app.msgModal("提示消息：","预览图片失败，请稍后重试！");
  //           }
  //         })
  //       }else{
  //         app.msgModal("提示消息：","预览图片失败，请稍后重试！");
  //       }
  //     },
  //     fail : function(res){
  //       app.msgModal("提示消息：","预览图片失败，请稍后重试！");
  //     }
  //   })
  // },
  //预览图片
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
  callContact : function(e){
    var that = this;
    if(that.data.actInfo.contactStaff){
      wx.makePhoneCall({
        phoneNumber: that.data.actInfo.contactStaff.tel ,//  联系人电话
        success : function(){
          console.log("confirm make phonecall")
        },
        fail : function(){
          console.log("cancle make phonecall")
        },
        complete : function(){
          console.log("complete make phonecall")
          app.globalData.makeCallAndappHide = true;
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
    //that.setData({
    //  showActStatus: isShow,
    //})
  },
  //更新活动内容
  updateActContent : function(e){
    wx.navigateTo({
      url: './../addCooperateAct/addCooperateAct?actId='+this.data.actInfo.id,
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
  //确认添加活动内容
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
  hideAddContent : function(){
    this.setData({
      showModal : false,
    })
  },
  hideShareModal : function(){
    this.setData({
      hiddenModal : true,
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
  },
  currentUserIsInStaffList : function(staffList){
    if(staffList != null && staffList.length > 0){
      for(var staff of staffList){
        if(staff.id == app.globalData.userInfo.id){
          return true;
        }
      }
    }
    return false;
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
  cfmSignIn : function(e){
    let signStatus = e.target.dataset.signstatus;
    if(signStatus == '1') {
      this.singStatusProcess(e.target.dataset.id);
      return ;
    }
    this.updateCustSignStatus('1',e.target.dataset.custid,e.target.dataset.actid);
  },
  cancelSignIn : function(e){
    let signStatus = e.target.dataset.signstatus;
    if(signStatus == '2') {
      this.singStatusProcess(e.target.dataset.id);
      return;
    }
    this.updateCustSignStatus('2',e.target.dataset.custid,e.target.dataset.actid);
  },
  //更新用户的报名状态
  updateCustSignStatus : function(signStatus,custId,actId){
    var that = this;
    if(signStatus){
      wx.request({
        url: app.crmRequestHost + '/ty/tyActivitysign/updateSignStatus',  
        method: 'POST',
        data: {
          signStatus : signStatus,
          custId : custId,
          actId  : actId,
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          console.log("查询活动详情返回的res：", res)
          if(res.data.httpCode == 200){
            that.getActInfo();
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
  //点击方框弹出对应的状态改变按钮
  changeSignStatus : function(e){
    let signId = e.currentTarget.dataset.id;
    this.singStatusProcess(signId);
  },
  singStatusProcess : function(signId){
    for(let i=0; i < this.data.activitysignData.length; i++){
      if(signId == this.data.activitysignData[i].id){
        this.data.activitysignData[i].showSignBtn = !this.data.activitysignData[i].showSignBtn;  
      }else{
        if(this.data.activitysignData[i].showSignBtn){
          this.data.activitysignData[i].showSignBtn = false;
        }
      }
    }
    this.setData({
      activitysignData : this.data.activitysignData,
    })
  },
  backToHome : function (e){
    console.log("back to home",e);
    wx.reLaunch({
      url: './../index/index'
    })
  },
})