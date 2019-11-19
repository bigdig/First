const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
     id:null,
     companyName:"",
     userName:'',
     department:"",
     position:"",
     phone:"",
     email:"",
     serviceSaler:'-',
     custTel:'-',
     industry:'-',
     domain:'-',
     area:'-',
     custId_customer:'',
     tyfund:false,  //用于判断是否展示基金模块。在职位是基金经理的时候才展示
     stastSelect:'类型筛选',
     modelShow:false,
     activityType:'',  //用于服务记录筛选条件
     checkedbox: false,  //用于控制消息推送
     recEmail:null,  //0为不推送。1为推送；
     recSms: null,   //0为不推送。1为推送；
     fundYield: null,   //基金收益率，数据刷新时用来判断正收益#FC6D51,负收益#62CBA4;
     activitype:[     //筛选条件

     ] ,
     itemDetail:null,
     fundList:[],
     industryLabel:[],
     recordList:[],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    app.globalData.custId_customer = [];
    this.setData({
      id: options.id
    })
  },
  //是否接受推荐信息
  checkboxChange:function(e){
    var checkedbox = e.detail.value[0];
    if (checkedbox){
      this.setData({
        checkedbox: true,
        recEmail: 0, 
        recSms: 0,
      }) 
    }else{
      this.setData({
        checkedbox: false,
        recEmail: 1,
        recSms: 1,
      })
    }
    this.pushMessage();
  },
  //选择服务关闭遮罩层
  noShow:function(){
    this.setData({
      modelShow: false,
    })
  },
  //标签列表
  labelList:function(){
    var that = this;
    var id = that.data.id;
    wx.request({
      url: app.crmRequestHost + '/ty/tyCustomerlabel/read/list',
      method: 'POST',
      data: {
        customerId:id,
      },
      header: {
         'content-type': 'application/x-www-form-urlencoded', // 默认值
         'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var lists = res.data.data;
          that.setData({
            industryLabel: lists,
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
  //新增标签
  addLabel:function(){ 
    var id = this.data.id;
    var industryLabel = this.data.industryLabel;
    var storageLabel = [];
    if (industryLabel.length > 0){
      for (var i = 0; i < industryLabel.length;i++){
        var children = industryLabel[i].children;
        for (var k = 0; k < children.length;k++){
          storageLabel.push(children[k].labelName)
        }
      }
    }
    app.globalData.storageLabel = storageLabel;
    wx.navigateTo({
      url: '../addLabel/addLabel?customerId=' + id,
    })
  },
  //推送
  pushMessage:function(){
    var that = this;
    var item = that.data.itemDetail;
    item.recEmail = that.data.recEmail;
    item.recSms = that.data.recSms;
    for (var k in item){
      if (item[k] == null){
        item[k] = '';
      }
    }
    wx.request({
      url: app.crmRequestHost + '/ty/tyOrgcustomer/update',
      method: 'POST',
      data: item,
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          app.msgModal("提示消息", "状态修改成功");
          
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
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var that = this;
    //个人详情请求
    wx.request({
      url: app.crmRequestHost + '/ty/tyOrgcustomer/read/detail',
      method: 'POST',
      data: {
        id:that.data.id,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
           var item = res.data.data;
           if (item.recSms == 1){
             that.setData({
               checkedbox : false,
             })
           }else{
             that.setData({
               checkedbox : true,
             })
           }
           //用于跳转发送短信页面传数组
           var custId_customer = [];
           custId_customer.push(item);
           that.setData({
             itemDetail: item,
             userName: item.custName === null ? '-' : item.custName ,
             companyName: item.orgName === null ? '-' : item.orgName ,
             department: item.department === null ? '-' : item.department ,
             position: item.title === null ? '-' : item.title ,
             phone: item.custMobile === null ? '-' : item.custMobile ,
             email: item.custEmail === null ? '-' : item.custEmail ,
             serviceSaler: item.serviceSaler === null ? '-' : item.serviceSaler ,
             custTel: item.custTel === null ? '-' : item.custTel ,
             industry: item.industry === null ? '-' : item.industry ,
             domain: item.domain === null ? '-' : item.domain ,
             area: item.area === null ? '-' : item.area,
             custId_customer: custId_customer,
           })
           //基金模块请求
           if (that.data.position == '基金经理'){
             that.setData({
               tyfund:true,
             })
              that.tyfund();
           }
           //标签模块加载
           that.labelList(); 
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
    //服务记录请求
    this.select();
  },
  //添加服务记录
  addrecord:function(){
    var id = this.data.id;
    var custName = this.data.userName;
    wx.navigateTo({
      url: '../addService/addService?custId=' + id ,
    })
  },
  //帅选按钮
  stastSelect:function(){
    this.setData({
      modelShow:true,
    })
  },
  //选择条件
  tablist:function(e){
    console.log(e.target.dataset)
    this.setData({
      modelShow: false,
      activityType: e.target.dataset.id,
      stastSelect: e.target.dataset.state,
    })
    this.select();
  },
  //发送消息
  sms:function(){
    var that = this;
    app.globalData.custId_customer = this.data.custId_customer;
    //var custId_customer = JSON.stringify(this.data.custId_customer);
    wx.navigateTo({
      url: '../sendMessages/sendMessages',
    })
  },
  //帅选条件
  select:function(){
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyActivity/read/list',
      method: 'POST',
      data: {
        activityType: that.data.activityType,
        custId: that.data.id,
        pageSize:10,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
            var lists = res.data.data.list;
            var recordList = [];
            for (var i = 0; i < lists.length;i++){
              var position = '';
              var staffName = '';
              if (lists[i].staffList.length != 0){
                position = lists[i].staffList[0].position;
              }
              if (lists[i].staffList.length != 0){
                staffName = lists[i].staffList[0].staffName;
              }
              if(staffName==''){
                staffName = lists[i].createName
              }
              recordList.push(lists[i].beginDate + ' : ' + that.data.userName + '参与了' + position + staffName  + '组织的' + lists[i].activityTypeText)
            }
            that.setData({
              recordList: recordList,
              activitype: app.activityTypeFilter(res.data.dicts.ACTIVITYTYPE),
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
  //基金股票查询
  tyfund:function(){
    var that = this;
    wx.request({
      url: app.crmRequestHost + '/ty/tyCfund/read/fundlist',
      method: 'POST',
      data: {
        fundmanager: that.data.userName,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var lists = res.data.data;
          that.setData({
            fundList: lists,
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
    
  }
})