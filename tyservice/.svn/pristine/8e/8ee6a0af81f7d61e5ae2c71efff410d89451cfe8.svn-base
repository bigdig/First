//app.js
App({
  crmRequestHost: 'https://tyservice.tfzq.com',
  globalData: {
    wxUserInfo : null,
    userInfo: null,
    phone: null,
    cookieInfo : null,
    custId_group:null,     //用于新建分组页面actcipant
    currentTab:0,          //跳转到index页面用于判断tab
    custId_customer:null,   //用于存储客户选中信息到发送短信模板
    activeParticipant:[],  //用于存储活动参与人;
    actcipant:[],         //用于存储活动联系人;
    coopExpertList : [],     //选择的专家列表（协作活动)
    coopListedCompList : [], // 选择的上市公司列表（协作活动)
    expertList : [],     //选择的专家列表（协作活动)
    listedCompList : [], // 选择的上市公司列表（协作活动)
    choseNametype: null,       //用于标志是活动参与人还是活动联系人;
    myActivityList:0,      //我的活动数
    activateCustomers:0   , //活跃客户数
    storageLabel:[],      //用于存储标签
    makeCallAndappHide : false , //活动详情页调用打电话会导致onHide触发
    previewImg : false,
    scanImg : false,  //扫描图片
    uploadImg : false, //上传附件
    isSeller : false, //是否是销售
  },
  //模态弹窗
  msgModal: function (title, msg, showCancel) {
    wx.showModal({
      title: title,
      content: msg,
      showCancel: (showCancel == null ? false : showCancel),
    })
  },
  onLaunch: function () {
    console.log("onlaunch");
    // this.globalData.cookieInfo = wx.getStorageSync("cookieInfo");
    //  var that = this;
    //  // 获取用户信息
    // //  wx.getSetting({
    // //    success: function (res) {
    // //      //console.log("app.js中getSetting的res对象：")
    // //      console.log(res)
    // //      if (res.authSetting['scope.userInfo']) {
    //        // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
    //       // console.log("get userInfo Authorise");
    //        wx.getUserInfo({
    //           success: function (res) {
    //             that.globalData.wxUserInfo = res.userInfo;
    //             //console.log(res.userInfo);
    //             // var nickName = userInfo.nickName;
    //             // var avatarUrl = userInfo.avatarUrl;
    //             // var gender = userInfo.gender; //性别 0：未知、1：男、2：女
    //             // var province = userInfo.province;
    //             // var city = userInfo.city;
    //             // var country = userInfo.country;
    //             // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //             // 所以此处加入 callback 以防止这种情况
    //             if (that.userInfoReadyCallback) {
    //               that.userInfoReadyCallback(res)
    //             }
    //             wx.login({
    //               success: function(res) {
    //                 if (res.code) {
    //                   that.userLogin(res.code);
    //                 }
    //               }
    //             })
    //           }
    //         })
    //      }else{
    //        console.log("refuse authorise");
    //      }
    //    }
    //  })

  },
  /**
    * 生命周期函数--监听页面显示
    */
   onShow: function (options) {
    console.log("app onshow");
    console.log("app options ::",options)
    // this.globalData.isShare = options.query.isShare;
    this.globalData.cookieInfo = wx.getStorageSync("cookieInfo");
    var that = this;
    // 获取用户信息
   //  wx.getSetting({
   //    success: function (res) {
   //      //console.log("app.js中getSetting的res对象：")
   //      console.log(res)
   //      if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
         // console.log("get userInfo Authorise");
          // wx.getUserInfo({
          //    success: function (res) {
          //      that.globalData.wxUserInfo = res.userInfo;
               //console.log(res.userInfo);
               // var nickName = userInfo.nickName;
               // var avatarUrl = userInfo.avatarUrl;
               // var gender = userInfo.gender; //性别 0：未知、1：男、2：女
               // var province = userInfo.province;
               // var city = userInfo.city;
               // var country = userInfo.country;
               // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
               // 所以此处加入 callback 以防止这种情况
              //  if (that.userInfoReadyCallback) {
              //    that.userInfoReadyCallback(res)
              //   }
                wx.login({
                  success: function(res) {
                  //  console.log("app onshow login::",res);
                  //  return ;
                    if (res.code) {
                      that.userLogin(res.code,options);
                    }
                  }
                })
          //    }
          //  })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    var that = this;
    console.log("---app-onHide---");
  },
  clearNullValue : function(param){
    for(var key in param){
      if(param[key] == null){
        param[key] = '';
      }
    }
    return param;
  },
  userLogin : function(code,options){
    var that = this;
    wx.removeStorageSync("cookieInfo");//清除上一次登录信息
    wx.login({
      success: function (res) {
         wx.request({
           url: that.crmRequestHost +'/wxlog', 
           method: 'POST',
           data: {
             adlogin: '',     //是否AD登录
             account: '',
             password: '',
             wxCode: code,
             avatar : '',
           },
           header: {
             'content-type': 'application/x-www-form-urlencoded' // 默认值
           },
           success: function (res) {
             console.log("登录成功后返回的res：",res)
             if (res.data.httpCode == 200){
                //保存cookie
                wx.setStorageSync("cookieInfo", res.header["Set-Cookie"]);
                that.globalData.cookieInfo = null;
                that.globalData.cookieInfo = res.header["Set-Cookie"];
                //保存crm中的用户信息
                that.globalData.userInfo = res.data.data.user;
                that.globalData.activateCustomers = res.data.data.activateCustomers;
                that.globalData.myActivityList = res.data.data.myActivityList + res.data.data.mySignActList;
                that.globalData.isSeller = res.data.data.isSeller;
                 console.log( '登录：：', that.globalData);
                 if(!that.globalData.makeCallAndappHide && !that.globalData.previewImg && !that.globalData.scanImg && !that.globalData.uploadImg ){  //如果是活动详情页打电话导致的onhide则不跳转
                  if ((options.query.isShare)&& options.query.actId){
                     console.log('不跳转到首页');
                    //不跳转到首页
                  }else{
                     console.log('ReLaunch到首页');
                    wx.reLaunch({
                      url: '/pages/index/index'
                    })
                  }
                 }else{
                  that.globalData.makeCallAndappHide = false;  //还原值
                  that.globalData.previewImg = false;  //还原值
                  that.globalData.scanImg = false;  //还原值
                  that.globalData.uploadImg = false;
                 }
             }else if(res.data.httpCode == 401){
              wx.redirectTo({
                url: '/pages/login/login'
              })
            }else if(res.data.httpCode == 303){
              wx.redirectTo({
                url: '/pages/login/login'
              })
            }
           }
         })
      }
    })
  },
  activityTypeFilter : function(dict){
    var tempActivityType = [];
    for(var dic of dict){
      //沙龙管理、调研、委托课题、语言微路演
      if(dic.id == 5 || dic.id == 7 || dic.id == 0 || dic.id == 8){
        tempActivityType.push(dic);
      }
    }
    return tempActivityType;
  },
  myActivityTypeFileter : function(dict){
    var tempActivityType = [];
    for(var dic of dict){
      //晨会接入，电话服务，资料整理，其他专项服务
      if(dic.id == 2 || dic.id == 9 || dic.id == 10 || dic.id == 11 || dic.id == 12){
        tempActivityType.push(dic);
      }
    }
    return tempActivityType; 
  }
})