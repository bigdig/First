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
    beginTime : null ,  //开始时间
    beginDate : null , //开始日期   
    startTimeHidFlag : false  ,
    startDateHidFlag : false ,
    endTime : null ,  //结束时间
    endDate : null , //结束日期   
    endTimeHidFlag : false  ,
    endDateHidFlag : false ,
    actAddrHidFlag : false,
    personLimitInput : null,
    ACTIVITYTYPE : null,   //字典
    PRIORITYLEVEL : null, 
    ACTIVITYSTATUS: null,
    SysWorkAreas : null,
    activityInfo : null, //活动详情
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
    actExperts : [], //选择的专家，
    actListedComps : [] , //选择的上市公式
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that  = this;
    var actId = options.actId;
    console.log("活动的Id：：",actId)
    if(options.actId){
      this.setData({
        isUpdateAct : true,
      })
    }
    wx.request({
      url: app.crmRequestHost + '/ty/tyActivity/read/detail',  
      method: 'POST',
      data: {
        id :   ( actId == null ? "" : actId),   //活动id
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        console.log("查询活动详情返回的res：", res)
        if(res.data.httpCode == 200){
          that.data.ACTIVITYSTATUS =  res.data.dicts.ACTIVITYSTATUS ; // 活动状态
          that.data.ACTIVITYTYPE = app.myActivityTypeFileter(res.data.dicts.ACTIVITYTYPE); // 活动类型
          that.data.PRIORITYLEVEL =  res.data.dicts.PRIORITYLEVEL; //活动优先级别
          that.data.SysWorkAreas = res.data.dicts.SysWorkAreas ; //工作地点
          if(res.data.data != null && res.data.data.tyActivityBean){
            that.data.actInfo = res.data.data.tyActivityBean;
            that.data.actExperts = res.data.data.tyActivityBean.expertList;
            that.data.actListedComps = res.data.data.tyActivityBean.listedCompList;
            that.data.selCustomers = res.data.data.activitysignData ?  that.getCustomers(res.data.data.activitysignData) : [];
            that.data.actLabels = (res.data.data.tyActivityBean.labels ? (""+res.data.data.tyActivityBean.labels).split(" ") : []);
            that.data.addAttachs = (res.data.data.tyActivityBean.attachs ? (""+res.data.data.tyActivityBean.attachs).split(",") : []);
            that.data.addAttachNames = (res.data.data.tyActivityBean.addAttachNames ? (""+res.data.data.tyActivityBean.addAttachNames).split(",") : []);
            that.data.beginDate = res.data.data.tyActivityBean.beginDate;
            that.data.beginTime = res.data.data.tyActivityBean.beginTime;
            that.data.endDate = res.data.data.tyActivityBean.endDate;
            that.data.endTime = res.data.data.tyActivityBean.endTime;
            for(var i = 0 ; i < that.data.ACTIVITYTYPE.length ; i ++){
              if(res.data.data != null && res.data.data.tyActivityBean){
                that.data.allGoodsFilte[i] = that.data.ACTIVITYTYPE[i];
                //回显
                if(res.data.data.tyActivityBean.activityType == that.data.allGoodsFilte[i].id){
                  that.data.allGoodsFilte[i].checked = true;
                  that.data.tempActTypeInfo = that.data.allGoodsFilte[i];
                }else{
                  that.data.allGoodsFilte[i].checked = false;
                }
              }
            }
          }
          that.setData({
            actExperts : that.data.actExperts,
            actListedComps : that.data.actListedComps,
            selCustomers : that.data.selCustomers,
            tempActTypeInfo : that.data.tempActTypeInfo,
            allGoodsFilte : that.data.allGoodsFilte,
            addAttachs : that.data.addAttachs,
            actLabels : that.data.actLabels,
            actInfo : (that.data.actInfo.id == null ? that.data.actInfo : that.actInfoTimeFormat(that.data.actInfo)),
            ACTIVITYSTATUS : that.data.ACTIVITYSTATUS,  // 活动状态
            ACTIVITYTYPE : that.data.ACTIVITYTYPE,   // 活动类型
            PRIORITYLEVEL : that.data.PRIORITYLEVEL,   //活动优先级别
            SysWorkAreas : that.data.SysWorkAreas,    //工作地点
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
  serviceValChange: function (e) {
    var allGoodsFilte = this.data.allGoodsFilte;
    var checkArr = e.detail.value;
    console.log("checkedNum::",checkArr)
    for (var i = 0; i < allGoodsFilte.length; i++) {
      if(checkArr.length == 0){
        allGoodsFilte[i].checked = false;
      }else{
        for(var j = 0 ; j < checkArr.length  ; j++){
          if(allGoodsFilte[i].id == checkArr[j]){
            allGoodsFilte[i].checked = true;
            this.data.tempActTypeInfo = allGoodsFilte[i];
          }else{
            allGoodsFilte[i].checked = false ;
          }
        }
      }
    }
    this.setData({
      allGoodsFilte: allGoodsFilte,
      tempActTypeInfo : this.data.tempActTypeInfo,
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  //标题
  bindActTitleInput: function (e) {
    this.setData({
      tempActTitle : e.detail.value, 
    })
  },
  displayAll: function () {
    app.globalData.storageLabel = [];
    this.setData({
      displayAll: false,
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
  bindDateChange: function(e) {
    this.setData({
      beginDate: e.detail.value,
      endDate : e.detail.value,
      endDateHidFlag : true,
      beginDateHidFlag : true,
    })
  },
  bindTimeChange: function(e) {
    this.setData({
      beginTime: e.detail.value,
      endTime: e.detail.value,
      beginTimeHidFlag : true ,
      endTimeHidFlag : true ,
    })
  },
  bindEndDateChange: function(e) {
    if(this.data.beginDate > e.detail.value ){
      app.msgModal("错误提示：","结束日期必须在开始日期之后！");
    }else{
      this.setData({
        endDate: e.detail.value,
        endDateHidFlag : true,
      })
    }
  },
  bindEndTimeChange: function(e) {
    var beginDateTime = parseInt((this.data.beginDate + "" + this.data.beginTime).replace(/-/g, "").replace(/:/, ""));
    var endDateTime =  parseInt((this.data.endDate + "" +e.detail.value).replace(/-/g, "").replace(/:/, ""));
    if(beginDateTime > endDateTime ){
      app.msgModal("错误提示：","结束时间必须在开始时间之后！");
    }else{
      this.setData({
        endTime: e.detail.value,
        endTimeHidFlag : true ,
      })
    }
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
    var param = {};
    param.id = this.data.actInfo.id;
    param.activityType = null;
    if (this.data.tempActTypeInfo != null) {
      param.activityType = this.data.tempActTypeInfo.id;
    }
    param.title  = (this.data.tempActTitle == null ? this.data.actInfo.title : this.data.tempActTitle);
    param.createBy =  app.globalData.userInfo.id;
    param.createDeptId = app.globalData.userInfo.deptId;
    param.createName = app.globalData.userInfo.staffName;
    param.locale = (this.data.actAddr == null ? this.data.actInfo.locale : this.data.actAddr);
    param.detailAddr = (this.data.detailAddr == null ? this.data.actInfo.detailAddr : this.data.detailAddr);
    param.content = (this.data.actContent == null ? this.data.actInfo.content : this.data.actContent);
    param.priorityLevel = (this.data.priorityLevel == null ? this.data.actInfo.priorityLevel : this.data.priorityLevel);
    param.labels = this.data.actLabels.join(" ");
    param.attachs = this.data.addAttachs.join(",");
    param.attachsName = this.data.addAttachNames.join(",");
    param.remark = (this.data.remark == null ? this.data.actInfo.remark : this.data.remark);
    param.userIds = [];
    param = this.putExpertIntoParam(param); //专家
    param = this.putListedCompIntoParam(param); //上市公司
    param.custIds = this.getCustomerIds(this.data.selCustomers);
    param.activityCate = "1";
    param.deleteFlag = (this.data.actInfo.deleteFlag ? this.data.actInfo.deleteFlag : '0');
    //时间日期处理
    param.beginDate = ((this.data.beginDate + "").indexOf("-") == -1 ? this.data.beginDate : this.data.beginDate.replace(/-/g, ""));
    param.beginTime = ((this.data.beginTime + "").indexOf(":") == -1 ? this.data.beginTime : (this.data.beginTime.replace(/:/g, "") + "00"));  //00：秒
    param.endDate = ((this.data.endDate + "").indexOf("-") == -1 ? this.data.endDate : this.data.endDate.replace(/-/g, ""));
    param.endTime = ((this.data.endTime + "").indexOf(":") == -1 ? this.data.endTime : (this.data.endTime.replace(/:/g, "") + "00"));
    // param.activityStatus = "0";// if(this.data.beginDate == null || this.data.beginTime == null || this.data.endDate == null || this.data.endTime == null){param.activityType && param.title && param.createBy
    if (!param.activityType || param.activityType == null || param.activityType == '') {
      app.msgModal("提示消息：", "活动类型未填完整或者填写错误，请重新填写！");
      return;
    }
    if (!param.title || param.title == null || param.title == '') {
      app.msgModal("提示消息：", "活动标题未填完整或者填写错误，请重新填写！");
      return;
    }
    if (!param.createBy || param.createBy == null || param.createBy == '') {
      app.msgModal("提示消息：", "活动发起人未填完整或者填写错误，请重新填写！");
      return;
    }
    if (!param.beginDate || param.beginDate == null || param.beginDate == '') {
      app.msgModal("提示消息：", "开始日期未填完整或者填写错误，请重新填写！");
      return;
    }
    if ( param.beginTime == null || param.beginTime === '') {
      app.msgModal("提示消息：", "开始时间未填完整或者填写错误，请重新填写！");
      return;
    }
    if (!param.endDate || param.endDate == null || param.endDate == '') {
      app.msgModal("提示消息：", "结束日期未填完整或者填写错误，请重新填写！");
      return;
    }
    if ( param.endTime == null || param.endTime === '') {
      app.msgModal("提示消息：", "结束时间未填完整或者填写错误，请重新填写！");
      return;
    }
    param = this.copyTitleToContent(param);
    param = app.clearNullValue(param);
    this.submitActInfo(param);
  },
  submitActInfo : function(param){
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyActivity/' + (that.data.isUpdateAct ? "update" : "add"),  
      method: 'POST',
      data: param,
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        console.log("添加活动返回的res：", res)
        if(res.data.httpCode == 200){
          //个人中心我的活动总个数+1
          app.globalData.myActivityList = app.globalData.myActivityList + 1;
          app.globalData.currentTab = '1';
          var pageArray =  getCurrentPages();
          wx.navigateBack({
            delta: 1,
            success : function(){
              if(that.data.isUpdateAct){
                pageArray[pageArray.length -2].getActInfo();
              }
            },
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
  //人数限制输入
  personLimitInput : function(e){
    if(Number.isInteger(parseInt(e.detail.value))){
      this.setData({
        personLimitInput: parseInt(e.detail.value),
      })
    }else{
      app.msgModal("错误提示：","您输入的人数有误，请重新输入！");
    }
  },
  toSelectCustomers  : function(){
    //初始化客户选择列表选中状态
    var list = this.data.selCustomers;
    var custId_customerArr=[];
    for (var i = 0; i < list.length; i++) {
      var cust = {};
      cust.id = list[i].id;
      cust.custName = list[i].custName;
      cust.orgName = list[i].orgName;

      custId_customerArr.push(cust);
    }
    app.globalData.custId_customer = custId_customerArr;
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
  //
  tochooseLabels : function(){
    app.globalData.storageLabel = this.data.actLabels;
    wx.navigateTo({
      url: './../addLabel/addLabel?addActLabel=true',
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
    record.beginDateText = tempBeginDateString.substring(0,4)+"-"+tempBeginDateString.substring(4,6)+"-"+tempBeginDateString.substring(6,8);
    record.endDateText = tempEndDateString.substring(0,4)+"-"+tempEndDateString.substring(4,6)+"-"+tempEndDateString.substring(6,8);
    record.beginTimeText = tempBeginTimeString.substring(0,2) +":"+tempBeginTimeString.substring(2,4);
    record.endTimeText = tempendTimeString.substring(0,2) +":"+tempendTimeString.substring(2,4);
    return record;
  },
  getCustomers : function(list){
    var templist = [];
    var tempCustomer = null;
    if(list.length > 0){
      for(var signData of list){
        tempCustomer = {};
        tempCustomer.id = signData.custId;
        tempCustomer.custName = signData.custName;
        tempCustomer.custMobile = signData.custMobile;
        tempCustomer.orgName = signData.orgName;
        templist.push(tempCustomer);
      }
    }
    return templist;
  },
  tochooseExpert : function(){
    app.globalData.expertList = this.data.actExperts; //活动专家
    wx.navigateTo({
      url: './../selectExpert/selectExpert?',
    })
  },
  tochooseListedComp :  function(){
    app.globalData.listedCompList = this.data.actListedComps; //活动上市公司
    wx.navigateTo({
      url: './../selectListedComp/selectListedComp?',
    })
  },
  putExpertIntoParam : function(param){
    var expertIds = [];
    if(this.data.actExperts && this.data.actExperts.length > 0){
      for(var expert of this.data.actExperts){
        expertIds.push(expert.id);
      }
      param.expertIds = expertIds;
    }
    return param;
  },
  putListedCompIntoParam : function(param){
    var listedCompIds = [];
    if(this.data.actListedComps && this.data.actListedComps.length > 0){
      for(var listedComp of this.data.actListedComps){
        listedCompIds.push(listedComp.id);
      }
      param.listedCompIds = listedCompIds;
    }
    return param;
  },
  copyTitleToContent : function (param) {
    if(param.content == null  || param.content == ''){
      param.content = param.title;
    }
    return param;
  },
})