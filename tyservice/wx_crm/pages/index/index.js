const app = getApp()
const util = require('../../utils/util.js')
Page({
  /**
   * 页面的初始数据
   */
  data: {
    cBorderBottom: '2rpx solid #FC6D51',
    mBorderBottom: '0rpx',
    signBorderBottom: '0rpx',
    groupBorderBottom: '0rpx',
    swiperBorderleft: ['','6rpx solid #FFAD4C','6rpx solid #FC6D51'],
    swiperBgColor : ['#578AAB',"#FFC24C","#26BB85","#8C898D"],
    mask_box_allBottom : '2rpx solid #FC6D51',
    mask_box_itemBottom : '2rpx solid #FC6D51',
    mask_box_allColor : '#fc6d51',
    mask_box_itemColor : '#fc6d51',
    cColorText: '#282C3F',
    mColorText: '#9FA3BB',
    signColorText: '#9FA3BB',
    groupColorText: '#9FA3BB',
    taber: true,
    currentTab: "0",
    hidden: true,
    scrollTop: 0,
    scrollHeight: 0,
    screenList:[{ text:"区域", id : "1"},{text: "活动类型", id : "2"},{text: "活动状态", id : "3"},],
    mask_add:false,
    swiperList:[{
       id:"1",
       list: [] ,
    },{
        id: "2",
        list:  [] ,
    },{
      id: "3",
      list:  [] ,
    },{
      id: "4",
      list:  [] ,
    }
    ],
    ACTIVITYSTATUS : null,  // 活动状态
    ORI_ACTIVITYTYPE : null , // 初始活动类型
    ACTIVITYTYPE : null,   // 活动类型
    PRIORITYLEVEL : null,   //活动优先级别
    // Researchdept : null,   // 
    // Researchers  : null ,   // 研究员
    // ResearchersEx : null ,  // 研究员拓展
    SysWorkAreas : null,    //工作地点
    // TOPICTYPE : null ,   // 主体类型
    // recommendTypes  : null , // 推荐类型
    dictHiddenFlag: true, //隐藏字典显示模态框
    tempDictIndexId : null , // 上一次字典索引值
    dictAnimation : null , //字典显示动画
    actAnimation : null , //添加活动动画
    showDict : [null,null,null] ,  // 实际字典值(过滤值)
    tempDict : null , //临时字典
    isMyActList : false , // 初始时只请求所有活动列表数据
    tempCurrentItemId : 1 , //当前哪一种活动（所有活动=1/我的活动=2）
    tempAllDictChooseFlag : false, //当前字典全选
    tempActPageNum : 1,
    actMaxPageNum : 0, // 所有活动最大的pageNum
    tempMyActPageNum : 1, //我的活动初始页码
    tempMySignActPageNum : 1, //我参加的活动初始页码
    tempGroupActPageNum : 1, //团队活动的初始页码
    myActMaxPageNum : 0 , //我的活动最大的pageNum
    addHistoryActFlag : false, //添加历史记录
    addHistoryMyActFlag : false, //添加历史记录
    addHistoryMySignActFlag : false, //添加我参加历史记录
    addHistoryGroupActFlag : false, //添加团队活动历史记录
    userId : null,//用户id
    isSeller : false , //是否是销售
    lastClickTimeStamp : null , //上次活动列表点击时间戳
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("app.globalData::",app.globalData);
    //this.getActList(1); 
    //延时加载，首页数据初始化
    wx.showLoading({
      title: '加载中',
    })
    setTimeout(function(){
      wx.hideLoading()
    },2000)
    // 初始化动画变量
    var animation = wx.createAnimation({
      duration: 500,
      transformOrigin: "50% 50%",
      timingFunction: 'ease',
    })
    this.animation = animation;
    this.addActAnimation = animation;
    //滚动加载
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
    console.log("onready::")
  },
  tab_customer: function () {
    this.startDictAnimation(false)
    app.globalData.currentTab = '0';
    this.setData({
      taber: true,
      cBorderBottom: '2rpx solid #FC6D51',
      mBorderBottom: '0rpx',
      signBorderBottom: '0rpx',
      groupBorderBottom: '0rpx',
      cColorText: '#282C3F',
      mColorText: '#9FA3BB',
      signColorText: '#9FA3BB',
      groupColorText: '#9FA3BB',
      currentTab : '0',
      showDict : [null,null,null] , // 清楚字典过滤数据
      tempDict : null,
      ACTIVITYTYPE : app.activityTypeFilter(this.data.ORI_ACTIVITYTYPE),
    })
  },
  tab_mechanism: function () {
    this.startDictAnimation(false)
    app.globalData.currentTab = '1';
    this.setData({
      taber: false,
      cBorderBottom: '0rpx',
      signBorderBottom: '0rpx',
      groupBorderBottom: '0rpx',
      mBorderBottom: '2rpx solid #FC6D51',
      cColorText: '#9FA3BB',
      signColorText: '#9FA3BB',
      groupColorText: '#9FA3BB',
      mColorText: '#282C3F',
      currentTab : '1',
      showDict : [null,null,null] ,//清楚字典顾虑数据
      tempDict : null,
      ACTIVITYTYPE : app.myActivityTypeFileter(this.data.ORI_ACTIVITYTYPE),
    })
  },
  tab_signInAct: function () {
    this.startDictAnimation(false)
    app.globalData.currentTab = '2';
    this.setData({
      taber: false,
      cBorderBottom: '0rpx',
      mBorderBottom: '0rpx',
      groupBorderBottom: '0rpx',
      signBorderBottom: '2rpx solid #FC6D51',
      cColorText: '#9FA3BB',
      mColorText: '#9FA3BB',
      groupColorText: '#9FA3BB',
      signColorText: '#282C3F',
      currentTab : '2',
      showDict : [null,null,null] ,//清楚字典顾虑数据
      tempDict : null,
      ACTIVITYTYPE : app.activityTypeFilter(this.data.ORI_ACTIVITYTYPE),
    })
  },
  tab_groupAct: function () {
    this.startDictAnimation(false)
    app.globalData.currentTab = '2';   //客户参与和团队参与只显示一个，currentTab索引值不变
    this.setData({
      taber: false,
      cBorderBottom: '0rpx',
      mBorderBottom: '0rpx',
      signBorderBottom: '0rpx',
      groupBorderBottom: '2rpx solid #FC6D51',
      cColorText: '#9FA3BB',
      mColorText: '#9FA3BB',
      signColorText: '#9FA3BB',
      groupColorText: '#282C3F',
      currentTab : '2',
      showDict : [null,null,null] ,//清楚字典顾虑数据
      tempDict : null,
      ACTIVITYTYPE : app.activityTypeFilter(this.data.ORI_ACTIVITYTYPE),
    })
  },
  swiper:function(e){
      if(e.timeStamp - this.data.lastClickTimeStamp > 500){   //两次事件最小间隔时间
        this.data.lastClickTimeStamp = e.timeStamp;
        //隐藏下拉列表
        this.startDictAnimation(false);
        console.log(e);
        var currentItemId = e.detail.currentItemId;
        if (currentItemId == '1'){
          this.tab_customer();
          console.log("test1")      //切换到协作活动
          this.data.swiperList[0].list = [];
          this.getActList(1);
          this.setData({
          tempActPageNum : 1,
        })
        }else if(currentItemId == '2'){
           this.tab_mechanism();    //切换到我的活动    // 查询我的活动列表数据
             console.log("test2")
             this.data.swiperList[1].list = [];
             this.getMyActList(1);
             this.setData({
              tempMyActPageNum : 1,
            })
        }else if(currentItemId == '3'){
          this.tab_signInAct();    //切换到我参加的活动   // 查询我参加的活动列表数据
             console.log("test3")
             this.data.swiperList[2].list = [];
             this.getMySignActList(1);
             this.setData({
              tempMySignActPageNum : 1,
            })
        }else if(currentItemId == '4'){
          this.tab_groupAct();    //切换到团队活动   // 查询团队活动的活动列表数据
             console.log("test4")
             this.data.swiperList[3].list = [];
             this.getGroupActList(1);
             this.setData({
              tempGroupActPageNum : 1,
            })
        }
        //切换活动类型的时候，清除字典中原有的数据
        this.cleanChooseFlag();
        this.setData({
          lastClickTimeStamp : this.data.lastClickTimeStamp,
          tempCurrentItemId  : currentItemId ,
        })
      }
  },
  //显示历史记录
  showActivity:function(){
     if(this.data.tempCurrentItemId == 1){
      this.setData({
        tempActPageNum : this.data.tempActPageNum +1,     //协作活动
        addHistoryActFlag : true,
      })
      this.getActList(this.data.tempActPageNum);
     }else if(this.data.tempCurrentItemId == 2){  //我的活动
      this.setData({
        tempMyActPageNum : this.data.tempMyActPageNum +1,
        addHistoryMyActFlag : true,
      })
      this.getMyActList(this.data.tempMyActPageNum);
     }else if(this.data.tempCurrentItemId == 3){  //参与活动
      this.setData({
        tempMySignActPageNum : this.data.tempMySignActPageNum +1,
        addHistoryMySignActFlag : true,
      })
      this.getMySignActList(this.data.tempMySignActPageNum);
     }else if(this.data.tempCurrentItemId == 4){  //团队活动
      this.setData({
        tempGroupActPageNum : this.data.tempGroupActPageNum +1,
        addHistoryGroupActFlag : true,
      })
      this.getGroupActList(this.data.tempGroupActPageNum);
     }
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var currentTab = app.globalData.currentTab;
    //app.globalData.currentTab = '0';
    this.setData({
      isSeller : app.globalData.isSeller,
      currentTab: currentTab,
    });
    console.log("onshow:" + currentTab)
    this.setData({
      swiperList: [{
        id: "1",
        list: [],
      }, {
        id: "2",
        list: [],
      }, {
        id: "3",
        list: [],
      }, {
        id: "4",
        list: [],
      }]
    })
    if (currentTab == 1) {
      this.getMyActList(1);
    } else if (currentTab == 2) {
      this.getMySignActList(1);
    }else if(currentTab == 3){
      this.getGroupActList(1);
    }else{
        this.getActList(1);
    }

    this.setData({
      tempActPageNum : 1,
      tempMyActPageNum : 1,
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    console.log("onhide::")
    this.cancleAddAct();
    this.startDictAnimation(false);
    //app.globalData.currentTab = '0';
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    console.log("onunload::")
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

  //首页判断用户是否登录过
  getActList : function(pageNum){
    app.globalData.currentTab=0;
    var that = this;
    if(app.globalData.cookieInfo){ //已登录
      console.log(app.globalData.cookieInfo)
        //查询所有的活动信息
        wx.request({
          url: app.crmRequestHost + '/ty/tyActivity/read/list',
          method: 'POST',
          data: {
            locale :   ( this.data.showDict == null || this.data.showDict[0] == null ? "" : this.data.showDict[0].text),   //地址
            activityType :    ( this.data.showDict == null || this.data.showDict[1] == null ? "" : this.data.showDict[1].id) , //
            activityStatus :  ( this.data.showDict == null || this.data.showDict[2] == null ? "" : this.data.showDict[2].id),
            pageNum : pageNum  == null ? "1" : pageNum,
            pageSize:20,
            activityCate:'0',
            orderBy: " activity_status asc,nvl(trim(priority_level),0) desc, begin_date desc , id desc",
          },
          header: {
            'content-type': 'application/x-www-form-urlencoded', // 默认值
            'cookie': app.globalData.cookieInfo
          },
          success: function (res) {
            console.log("myActivities成功后返回的res：", res)
            if(res.data.httpCode == 200){
              if(that.data.addHistoryActFlag){
                that.data.swiperList[0].list = that.data.swiperList[0].list.concat(that.timeFormatFunction(res.data.data.list));
              }else{
                that.data.swiperList[0].list = that.timeFormatFunction(res.data.data.list);
              }
              if(that.data.ACTIVITYSTATUS == null){
                that.data.ACTIVITYSTATUS = res.data.dicts.ACTIVITYSTATUS;
              }
              if(that.data.ACTIVITYTYPE == null){
                that.data.ORI_ACTIVITYTYPE = res.data.dicts.ACTIVITYTYPE;
                that.data.ACTIVITYTYPE = app.activityTypeFilter(that.data.ORI_ACTIVITYTYPE);
              }
              if(that.data.PRIORITYLEVEL == null){
                that.data.PRIORITYLEVEL = res.data.dicts.PRIORITYLEVEL;
              }
              if(that.data.SysWorkAreas == null){
                that.data.SysWorkAreas = res.data.dicts.SysWorkAreas;
              }
              that.setData({
                userId : app.globalData.userInfo.id,
                swiperList : that.data.swiperList,
                ACTIVITYSTATUS : that.data.ACTIVITYSTATUS,  // 活动状态
                ORI_ACTIVITYTYPE : that.data.ORI_ACTIVITYTYPE, //原始活动类型
                ACTIVITYTYPE :  that.data.ACTIVITYTYPE,   // 活动类型
                PRIORITYLEVEL :  that.data.PRIORITYLEVEL,   //活动优先级别
                // Researchdept : res.data.dicts.Researchdept,   // 
                // Researchers  : res.data.dicts.Researchers ,   // 研究员
                // ResearchersEx : res.data.dicts.ResearchersEx ,  // 研究员拓展
                SysWorkAreas :  that.data.SysWorkAreas,    //工作地点
                // TOPICTYPE : res.data.dicts.TOPICTYPE ,   // 主体类型
                // recommendTypes  : res.data.dicts.recommendTypes , // 推荐类型
              })
            }else if(res.data.httpCode == 401){
              wx.redirectTo({
                url: './../login/login'
              })
            }else{
              that.msgModal("提示消息：", res.data.msg);
            }
          },
          fail : function (res){
            that.msgModal("提示消息：", res.data.msg);
          }
        })
    }else{//没有cookie，尝试跳转登录页面
      wx.redirectTo({
        url: './../login/login'
      })
    }
  },
  //获取我的活动列表数据
  getMyActList  : function(pageNum){
    app.globalData.currentTab = 1;
    var that = this;
    if(app.globalData.cookieInfo){ //已登录
      console.log(app.globalData.cookieInfo)
        //查询我的活动信息
        wx.request({
          url: app.crmRequestHost + '/ty/tyActivity/read/list',
          method: 'POST',
          data: {
            locale :   ( this.data.showDict == null || this.data.showDict[0] == null ? "" : this.data.showDict[0].text),   //地址
            activityType :    ( this.data.showDict == null || this.data.showDict[1] == null ? "" : this.data.showDict[1].id) , //
            activityStatus :  ( this.data.showDict == null || this.data.showDict[2] == null ? "" : this.data.showDict[2].id) ,
            createBy : app.globalData.userInfo.id,
            activityCate: '1',
            pageNum : pageNum == null ? "1" : pageNum,
            pageSize: 20,
            orderBy : "begin_date desc , id desc",
          },
          header: {
            'content-type': 'application/x-www-form-urlencoded', // 默认值
            'cookie': app.globalData.cookieInfo
          },
          success: function (res) {
            console.log("myActivities成功后返回的res：", res)
            if(res.data.httpCode == 200){
              if(that.data.addHistoryMyActFlag){
                that.data.swiperList[1].list = that.data.swiperList[1].list.concat(that.timeFormatFunction(res.data.data.list));
              }else{
                that.data.swiperList[1].list = that.timeFormatFunction(res.data.data.list);
              }
              // that.data.swiperList[1].list = that.data.swiperList[1].list.concat(that.timeFormatFunction(res.data.data));
              that.setData({
                swiperList : that.data.swiperList,
              })
            }else if(res.data.httpCode == 401){
              wx.redirectTo({
                url: './../login/login'
              })
            }else{
              that.msgModal("提示消息：", res.data.msg);
            }
          },
          fail : function (res){
            that.msgModal("提示消息：", res.data.msg);
          }
        })
    }else{//没有cookie，尝试跳转登录页面
      wx.redirectTo({
        url: './../login/login'
      })
    }
  },
  getMySignActList : function(pageNum){
    app.globalData.currentTab = 2;
    var that = this;
    if(app.globalData.cookieInfo){ //已登录
      console.log(app.globalData.cookieInfo)
        //查询所有的活动信息
        wx.request({
          url: app.crmRequestHost + '/ty/tyActivity/read/list',
          method: 'POST',
          data: {
            locale :   ( this.data.showDict == null || this.data.showDict[0] == null ? "" : this.data.showDict[0].text),   //地址
            activityType :    ( this.data.showDict == null || this.data.showDict[1] == null ? "" : this.data.showDict[1].id) , //
            activityStatus :  ( this.data.showDict == null || this.data.showDict[2] == null ? "" : this.data.showDict[2].id),
            pageNum : pageNum  == null ? "1" : pageNum,
            pageSize: 20,
            signId: app.globalData.userInfo.id,
            orderBy: "begin_date desc ,id desc",
          },
          header: {
            'content-type': 'application/x-www-form-urlencoded', // 默认值
            'cookie': app.globalData.cookieInfo
          },
          success: function (res) {
            console.log("myActivities成功后返回的res：", res)
            if(res.data.httpCode == 200){
              if(that.data.addHistoryMySignActFlag){
                that.data.swiperList[2].list = that.data.swiperList[2].list.concat(that.timeFormatFunction(res.data.data.list));
              }else{
                that.data.swiperList[2].list = that.timeFormatFunction(res.data.data.list);
              }
              that.setData({
                userId : app.globalData.userInfo.id,
                swiperList : that.data.swiperList,
              })
            }else if(res.data.httpCode == 401){
              wx.redirectTo({
                url: './../login/login'
              })
            }else{
              that.msgModal("提示消息：", res.data.msg);
            }
          },
          fail : function (res){
            that.msgModal("提示消息：", res.data.msg);
          }
        })
    }else{//没有cookie，尝试跳转登录页面
      wx.redirectTo({
        url: './../login/login'
      })
    }
  },
  getGroupActList : function(pageNum){
    app.globalData.currentTab = 3;
    var that = this;
    if(app.globalData.cookieInfo){ //已登录
      console.log(app.globalData.cookieInfo)
        //查询所有的活动信息
        wx.request({
          url: app.crmRequestHost + '/ty/tyActivity/read/list',
          method: 'POST',
          data: {
            locale :   ( this.data.showDict == null || this.data.showDict[0] == null ? "" : this.data.showDict[0].text),   //地址
            activityType :    ( this.data.showDict == null || this.data.showDict[1] == null ? "" : this.data.showDict[1].id) , //
            activityStatus :  ( this.data.showDict == null || this.data.showDict[2] == null ? "" : this.data.showDict[2].id),
            pageNum : pageNum  == null ? "1" : pageNum,
            pageSize: 20,
            deptId: app.globalData.userInfo.deptId,
            orderBy: "begin_date desc , id desc",
            activityCate:'0',
          },
          header: {
            'content-type': 'application/x-www-form-urlencoded', // 默认值
            'cookie': app.globalData.cookieInfo
          },
          success: function (res) {
            console.log("myActivities成功后返回的res：", res)
            if(res.data.httpCode == 200){
              if(that.data.addHistoryGroupActFlag){
                that.data.swiperList[3].list = that.data.swiperList[3].list.concat(that.timeFormatFunction(res.data.data.list));
              }else{
                that.data.swiperList[3].list = that.timeFormatFunction(res.data.data.list);
              }
              that.setData({
                userId : app.globalData.userInfo.id,
                swiperList : that.data.swiperList,
              })
            }else if(res.data.httpCode == 401){
              wx.redirectTo({
                url: './../login/login'
              })
            }else{
              that.msgModal("提示消息：", res.data.msg);
            }
          },
          fail : function (res){
            that.msgModal("提示消息：", res.data.msg);
          }
        })
    }else{//没有cookie，尝试跳转登录页面
      wx.redirectTo({
        url: './../login/login'
      })
    }
  },
  showDictInfo : function(e){  // 显示对应的字典
    var dictIndex = e.currentTarget.dataset.id;
    var that = this;
    //判断各种出现地址的条件
    if(this.data.tempDictIndexId == null){  // 第一次点击
      this.startDictAnimation(true)
    }
    //不是第一次
    if(this.data.tempDictIndexId == dictIndex){
      if(!this.data.dictHiddenFlag){ //已显现则隐藏
        this.startDictAnimation(this.data.dictHiddenFlag)
      }else{  //已隐藏则显现
        this.startDictAnimation(this.data.dictHiddenFlag)
      }
    }else{  
      //先隐藏之前的字典，然后显示后面的字典
      this.startDictAnimation(false)
      setTimeout(function(){
        that.startDictAnimation(true);
      },150)
    }

    if(dictIndex == 1){
      this.data.tempDict = this.data.SysWorkAreas
    }else if(dictIndex == 2){
      this.data.tempDict = this.data.ACTIVITYTYPE
    }else if(dictIndex == 3){
      this.data.tempDict = this.data.ACTIVITYSTATUS
    }

    this.data.tempAllDictChooseFlag = false ;
    for(var dict of this.data.tempDict){
        if(dict.choose){
          this.data.tempAllDictChooseFlag = true;
          break;
        }
      }

    this.setData({
      tempAllDictChooseFlag : this.data.tempAllDictChooseFlag ,
      tempDictIndexId  : dictIndex,
      tempDict : this.data.tempDict,
    })

  },
  startDictAnimation : function(show){
    var that = this
    if (show) {
      that.animation.translateY(0 + 'vh').step()
    } else {
      that.animation.translateY(40 + 'vh').step()
    }
    that.setData({
      dictAnimation: that.animation.export(),
      dictHiddenFlag: !show,
    })
  },
  //选择过滤条件
  chooseDic : function(e){
    var that = this;
    var dictnum = e.currentTarget.dataset.dictnum;
    for(var i = 0 ; i < this.data.tempDict.length ; i ++){
      if(dictnum == this.data.tempDict[i].id){
        this.data.showDict[this.data.tempDictIndexId - 1 ] = this.data.tempDict[i];  //显示赋值
        this.data.tempDict[i].choose = true;
      }else{
        if(this.data.tempDict[i].choose){
          this.data.tempDict[i].choose = false;  //清除选择样式
        }
      }
    }
    if(this.data.tempDictIndexId == 1){
      this.data.SysWorkAreas = this.data.tempDict;   //记录选择的字典(回写)
    }else if(this.data.tempDictIndexId == 2){
      this.data.ACTIVITYTYPE = this.data.tempDict;
    }else if(this.data.tempDictIndexId == 3){
      this.data.ACTIVITYSTATUS = this.data.tempDict;
    }
    this.setData({
      showDict : this.data.showDict ,
      tempDict : this.data.tempDict,
      SysWorkAreas: this.data.SysWorkAreas, 
      ACTIVITYTYPE : this.data.ACTIVITYTYPE,
      ACTIVITYSTATUS : this.data.ACTIVITYSTATUS,
    })
    //隐藏下拉列表
    this.startDictAnimation(false);
    //根据对应的活动（所有活动/我的活动请求不同的接口)
    if(this.data.tempCurrentItemId == 1){
      that.data.swiperList[0].list = [];
      this.setData({
        swiperList : this.data.swiperList,
      })
      this.getActList(1);
      this.data.tempActPageNum = 1;
    }else if(this.data.tempCurrentItemId == 2){
      that.data.swiperList[1].list = [];
      this.setData({
        swiperList : this.data.swiperList,
      })
      this.getMyActList(1);
      this.data.tempMyActPageNum = 1;
    }else if(this.data.tempCurrentItemId == 3){
      that.data.swiperList[2].list = [];
      this.setData({
        swiperList : this.data.swiperList,
      })
      this.getMySignActList(1);
      this.data.tempMySignActPageNum = 1;
    }else if(this.data.tempCurrentItemId == 4){
      that.data.swiperList[3].list = [];
      this.setData({
        swiperList : this.data.swiperList,
      })
      this.getGroupActList(1);
      this.data.tempGroupActPageNum = 1;
    }
    this.setData({
      tempActPageNum : this.data.tempActPageNum ,
      tempMyActPageNum : this.data.tempMyActPageNum,
      tempMySignActPageNum : this.data.tempMySignActPageNum,
      tempGroupActPageNum : this.data.tempGroupActPageNum,
    })
  },
  //清楚字典，去除过滤参数
  clearDic  : function (e){
    var that = this;
    var dictIndex = this.data.tempDictIndexId;   //清楚上一次的保留状态
    if(dictIndex == 1){
      for(var i of this.data.SysWorkAreas){
        i.choose = false;
      }
    }else if(dictIndex == 2){
      for(var i of this.data.ACTIVITYTYPE){
        i.choose = false;
      }
    }else if(dictIndex == 3){
      for(var i of this.data.ACTIVITYSTATUS){
        i.choose = false;
      }
    }
    this.setData({
      SysWorkAreas : this.data.SysWorkAreas,
      ACTIVITYSTATUS : this.data.ACTIVITYSTATUS,
      ACTIVITYTYPE : this.data.ACTIVITYTYPE,
    })


    if(this.data.showDict){    //清除接口传入参数和显示字典值
      this.data.showDict[this.data.tempDictIndexId -1] = null;
      this.setData({
        showDict : this.data.showDict,
      })
    }
    this.startDictAnimation(false)
    if(this.data.tempCurrentItemId == 1){
      that.data.swiperList[0].list = [];
      this.setData({
        swiperList : this.data.swiperList,
      })
      this.getActList(1);
      this.data.tempActPageNum = 1;
      
    }else if(this.data.tempCurrentItemId == 2){
      that.data.swiperList[1].list = [];
      this.setData({
        swiperList : this.data.swiperList,
      })
      this.getMyActList(1);
      this.data.tempMyActPageNum = 1;
    }else if(this.data.tempCurrentItemId == 3){
      that.data.swiperList[2].list = [];
      this.setData({
        swiperList : this.data.swiperList,
      })
      this.getMySignActList(1);
      this.data.tempMySignActPageNum = 1;
    }else if(this.data.tempCurrentItemId == 4){
      that.data.swiperList[3].list = [];
      this.setData({
        swiperList : this.data.swiperList,
      })
      this.getGroupActList(1);
      this.data.tempGroupActPageNum = 1;
    }

    this.setData({
      tempActPageNum : this.data.tempActPageNum,
      tempMyActPageNum : this.data.tempMyActPageNum,
      tempMySignActPageNum : this.data.tempMySignActPageNum,
      tempGroupActPageNum : this.data.tempGroupActPageNum,
    })
  },
  //添加活动
  addActivity : function(e){
    this.startDictAnimation(false);
    this.startActAnimation(true);
  },
  //去往活动详情
  toActDetail  : function(e){
    console.log("toActDetail::",e)
    var actId = e.currentTarget.dataset.id;
    var actcate = e.currentTarget.dataset.actcate;
    var hasSubAct = e.currentTarget.dataset.hassubact
    console.log("toActDetail::",e);
    if(hasSubAct == '1'){
      wx.navigateTo({
        url : './../salonActDetail/salonActDetail?actId='+actId,
      })
    }else{
      if(actcate == 0){
        wx.navigateTo({
          url : './../activityDetail/activityDetail?actId='+actId,
        })
      }else if(actcate == 1){
        wx.navigateTo({
          url : './../activityAddLabel/activityAddLabel?actId='+actId,
        })
      }
    }
  },
  //添加协作活动
  addCooporateAct : function(e){
    wx.navigateTo({
      url: './../addCooperateAct/addCooperateAct'
    })
    this.startActAnimation(false);
  },
  //添加我的活动
  addMyAct : function(e){
    console.log("MyAct")
    wx.navigateTo({
      url: './../addMyAct/addMyAct'
    })
    this.startActAnimation(false);
  },
  //取消添加活动
  cancleAddAct : function(e){
    this.startActAnimation(false);
  },
  startActAnimation : function(show){
    var that = this
    if (show) {
      that.addActAnimation.translateY(0 + 'vh').step()
    } else {
      that.addActAnimation.translateY(40 + 'vh').step()
    }
    that.setData({
      actAnimation: that.addActAnimation.export(),
      mask_add: show,
    })
  },
   //模态弹窗
   msgModal : function(title,msg,showCancel){
    wx.showModal({
      title: title,
      content : msg,
      showCancel: (showCancel == null ? false : showCancel),
    })
  },
  timeFormatFunction : function(list){
    for(var i = 0 ; i < list.length ; i++){
      var tempBeginDateString = list[i].beginDate +"";
      var tempEndDateString = list[i].endDate +"";
      var tempBeginTimeString = list[i].beginTime +"";
      var tempendTimeString = list[i].endTime +"";
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
      list[i].beginDate = tempBeginDateString.substring(2,4)+"."+tempBeginDateString.substring(4,6)+"."+tempBeginDateString.substring(6,8);
      list[i].endDate = tempEndDateString.substring(2,4)+"."+tempEndDateString.substring(4,6)+"."+tempEndDateString.substring(6,8);
      list[i].beginTime = (parseInt(tempBeginTimeString.substring(0,2)))  
                         +":"+tempBeginTimeString.substring(2,4);
      list[i].endTime = (parseInt(tempendTimeString.substring(0,2)))
                        +":"+tempendTimeString.substring(2,4);
    }
    return list;
  },
  cleanChooseFlag :function(){
    for(var i of this.data.SysWorkAreas){
      i.choose = false;
    }
    for(var i of this.data.ACTIVITYSTATUS){
      i.choose = false;
    }
    for(var i  of this.data.ACTIVITYTYPE){
      i.choose = false;
    }
    this.setData({
      SysWorkAreas : this.data.SysWorkAreas,
      ACTIVITYSTATUS : this.data.ACTIVITYSTATUS,
      ACTIVITYTYPE : this.data.ACTIVITYTYPE,
    })
  },
  hideAddAct : function(e){
    console.log('hideAddAct');
    this.cancleAddAct();
  },
  hidedict : function(){
    console.log("hidedict")
    this.startDictAnimation(false);
  },
})