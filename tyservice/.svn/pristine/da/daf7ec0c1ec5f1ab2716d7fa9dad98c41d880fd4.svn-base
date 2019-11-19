const app  = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    labelBackColor : ['#ECFBF1','#D6E0F8','#FFE3DD','#DAF0F7','#E4DCF9'],
    labelColor : ['#23D75F','#3466D3','#FC6E57','#46B6DB','#7549E9'],
    displayAll: true,
    allGoodsFilte: [],
    actCustomer : [] , // 参会客户
    displayAll:true,
    allGoodsFilte: [],
    tempActTypeInfo : null ,// 临时的活动类型
    tempActTitle : null , //临时的活动标题
    actAddrHidFlag : false,
    personLimitInput : null,
    ACTIVITYTYPE : null,   //字典
    PRIORITYLEVEL : null, 
    ACTIVITYSTATUS: null,
    SysWorkAreas : null,
    actInfo : null , // 活动详情
    actAddr : null,  //活动地址
    detailAddr : null , //活动详细地址
    actContent : null , //活动内容
    priorityLevel : null,  //活动优先级
    priorityLevelText : null,  //活动优先级
    remark : null , //备注信息
    addAttachNames : [], //附件名称
    addAttachs : [] , // 附件路径
    contactStaff : null , //联系人
    actAnticepate : [], //活动参与人
    actLabels : [], //活动标签
    selCustomers : [] , //选中的客户列表（包含id和用户姓名）
    


    showCustomerPart  : false,
    showLocalePart : false,
    showContentPart : false,
    showLabelPart : false,
    showPriorityPart : false,
    showAttachPart : false,
    showRemarkPart : false,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that  = this;
    //传入的参数
    console.log("addActContent：：",options)
    this.showConentPart(options);
    wx.request({
      url: app.crmRequestHost + '/ty/tyActivity/read/detail',  
      method: 'POST',
      data: {
        id :   options.actId == null ? "" : options.actId,   //活动id
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        console.log("查询活动详情返回的res：", res)
        if(res.data.httpCode == 200){
          if(res.data.data != null && res.data.data.tyActivityBean){
            that.data.actInfo  = res.data.data.tyActivityBean;
          }
          that.setData({
            actInfo : that.data.actInfo,
            ACTIVITYSTATUS : res.data.dicts.ACTIVITYSTATUS,  // 活动状态
            ACTIVITYTYPE : app.activityTypeFilter(res.data.dicts.ACTIVITYTYPE),   // 活动类型
            PRIORITYLEVEL : res.data.dicts.PRIORITYLEVEL,   //活动优先级别
            SysWorkAreas : res.data.dicts.SysWorkAreas,    //工作地点 
            allGoodsFilte : that.data.allGoodsFilte,
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

  },
  bindAddrChange : function(e){
    this.setData({
      actAddr: this.data.SysWorkAreas[e.detail.value].text,
      actAddrHidFlag : true ,
    })
  },
  bindDetailAddrInput : function(e){
    this.setData({
      detailAddr : e.detail.value,
    })
  },
  bindActContentInput : function(e){
    this.setData({
      actContent : e.detail.value,
    })
  },
  bindPriorityChange : function(e){
    this.setData({
      priorityLevel : this.data.PRIORITYLEVEL[e.detail.value].id,
      priorityLevelText: this.data.PRIORITYLEVEL[e.detail.value].text,
      priorityHidFlag : true ,
    })
  },
  bindRemarkInput : function(e){
    this.setData({
      remark : e.detail.value,
    })
  },
  //上传图片数据
  openMyFile : function(e){
    app.globalData.uploadImg = true;
    var that = this;
    wx.chooseImage({
      success: function(res) {
        var tempFilePaths = res.tempFilePaths
        for(var i = 0 ;  i < tempFilePaths.length ; i++){
          wx.uploadFile({
            url: app.crmRequestHost + '/uploadfile/activityfile',  
            header: {
              'content-type': 'application/x-www-form-urlencoded', // 默认值
              'cookie': app.globalData.cookieInfo
            },
            filePath: tempFilePaths[i],
            name: 'file',
            success: function(res){
              var data = JSON.parse(res.data);
              if(data.httpCode == 200){
                that.data.addAttachNames.push(data.data.addAttachNames);
                that.data.addAttachs.push(data.data.addAttachs);
                that.setData({
                  addAttachNames : that.data.addAttachNames,
                  addAttachs : that.data.addAttachs,
                })
              }else{
                  app.msgModal("提示消息：","上传图片出错，请稍后重试！");
              }
            }
          })
        }
      }
    })
  },
  //提交我的活动内容
  submitMyAct : function(e){
    this.data.disableSubAct = true;
    this.setData({
      disableSubAct :  this.data.disableSubAct,
    })
    var that = this;
    var param = this.data.actInfo;
    param.userIds = [];
    if(this.data.showCustomerPart){
      if(this.data.selCustomers.length < 1){
        app.msgModal("提示消息：","您有信息未填完整或者填写错误，请重新填写！");
        return ;
      }
      param.custIds = this.getCustomerIds(this.data.selCustomers);
    }
    if(this.data.showLocalePart){
      if(this.data.actAddr == null || this.data.actAddr.length < 1){
        app.msgModal("提示消息：","您有信息未填完整或者填写错误，请重新填写！");
        return ;
      }
      param.locale = this.data.actAddr;
      param.detailAddr = this.data.detailAddr;
    }
    if(this.data.showContentPart){
      if(this.data.actContent == null){
        app.msgModal("提示消息：","您有信息未填完整或者填写错误，请重新填写！");
        return ;
      }
      param.content = this.data.actContent;
    }
    if(this.data.showLabelPart){
      if(this.data.actLabels.length < 1){
        app.msgModal("提示消息：","您有信息未填完整或者填写错误，请重新填写！");
        return ;
      }
      param.labels = this.data.actLabels.join(",");
    }
    if(this.data.showPriorityPart){
      if(this.data.priorityLevel == null ){
        app.msgModal("提示消息：","您有信息未填完整或者填写错误，请重新填写！");
        return ;
      }
      param.priorityLevel = this.data.priorityLevel;
    }
    if(this.data.showAttachPart){
      if(this.data.addAttachs.length < 1){
        app.msgModal("提示消息：","您有信息未填完整或者填写错误，请重新填写！");
        return ;
      }
      param.attachs = this.data.addAttachs.join(",");
      param.attachsName = this.data.addAttachNames.join(",");
    }
    if(this.data.showRemarkPart){
      if(this.data.remark == null){
        app.msgModal("提示消息：","您有信息未填完整或者填写错误，请重新填写！");
        return ;
      }
      param.remark = this.data.remark;
    }
    // param.userIds = [];
    // param.activityCate = "1";
    // param.activityStatus = "0";
    if(param){
      param  = app.clearNullValue(param);
      this.submitActInfo(param);
    }else{
        app.msgModal("提示消息：","您有信息未填完整或者填写错误，请重新填写！");
    }
  },
  submitActInfo : function(param){
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyActivity/update',  
      method: 'POST',
      data: param,
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        console.log("查询活动详情返回的res：", res)
        if(res.data.httpCode == 200){
          var arr = getCurrentPages();
          wx.navigateBack({
            delta: 1,
            success: function () {
              arr[arr.length -2].getActInfo();
              arr[arr.length -2].setData({
                showModal : false,
              })
            }
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
  },
  toSelectCustomers  : function(){
    wx.navigateTo({
      url: './../selectCustomer/selectCustomer?actCustomersFlag=true',
    })
  },
  getCustomerIds : function(list){
    var custIds = [];
    for(var index in  list){
      custIds.push(list[index].id);
    }
    return custIds;
  },
  //页面显示与隐藏
  showConentPart : function(options){
    if(options.addCustomer){
      this.data.showCustomerPart = true;
    }
    if(options.addLocale){
      this.data.showLocalePart  =true;
    }
    if(options.addContent){
      this.data.showContentPart = true;
    }
    if(options.addLabel){
      this.data.showLabelPart = true;
    }
    if(options.addPriority){
      this.data.showPriorityPart = true;
    }
    if(options.addAttaches){
      this.data.showAttachPart = true;
    }
    if(options.addRemark){
      this.data.showRemarkPart = true;
    }
    this.setData({
      showCustomerPart : this.data.showCustomerPart,
      showLocalePart : this.data.showLocalePart,
      showContentPart : this.data.showContentPart,
      showLabelPart : this.data.showLabelPart,
      showPriorityPart : this.data.showPriorityPart ,
      showAttachPart : this.data.showAttachPart,
      showRemarkPart : this.data.showRemarkPart,
    })
  },
  tochooseLabels : function(){
    wx.navigateTo({
      url: './../addLabel/addLabel?addActLabel=true',
    })
  },
})