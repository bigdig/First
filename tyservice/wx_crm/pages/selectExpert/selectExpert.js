const app = getApp();
var pageSize = 15;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    existExpertList: [],//前一页面传递过来的专家列表
    expertList : [], //专家列表(显示)
    //selectedStaff: [], //选择的
    keyword : '' , //查询关键字
    // tempExpert : null , //选择的专家
    // tempParticiStaff : [] , //选择的
    // isMutilSelect : false , //是否是多选
    hidden: true,   //用于控制刷新图标显示
    scrollTop: 0,
    scrollHeight: 0,
    pageNum: 1,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //吧存在APP里面的全局参数带过来
    // if (app.globalData.choseNametype == '1'){//单选
    //   var tempExpert = app.globalData.actcipant;
    //   if (tempExpert.length > 0){
    //     for (var item of tempExpert){
    //       item.checked = true;
    //       break;
    //     }
    //   }
      
    //   lists = null;
    //   if (tempExpert != null) {
    //     tempExpert.checked = true;
    //   }
    //   this.setData({
    //     existExpertList: tempExpert,
    //   })
    // }else{//多选
      var lists = null;
      if(options.isCoopAct){
        lists = app.globalData.coopExpertList ;
      }else{
        lists = app.globalData.expertList;
      }
      //tempExpert =null;
      if (lists != null) {
        for (var i = 0; i < lists.length; i++) {
          lists[i].checked = true;
        }
        this.setData({
          existExpertList: lists,
        })
      }
    // }

    // //是否是多选
    // if(options.isMutilSelect){
    //   this.setData({
    //     isMutilSelect : options.isMutilSelect,
    //   })
    //   wx.setNavigationBarTitle({
    //     title: '选择参与人'
    //   })
    // }
    this.getExpertListInfo();
    //获取设备的高度
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          scrollHeight: res.windowHeight
        });
      }
    });
  },
  getExpertListInfo : function(){
    var that = this;
    that.setData({
      hidden: false
    });
    if(app.globalData.cookieInfo){ //已登录
      console.log(app.globalData.cookieInfo)
        //查询我的活动信息
        wx.request({
          url: app.crmRequestHost + '/ty/tyExpert/read/list',
          method: 'POST',
          data: {
            keyword: that.data.keyword,
            pageNum: that.data.pageNum,
            pageSize: pageSize,
          },
          header: {
            'content-type': 'application/x-www-form-urlencoded', // 默认值
            'cookie': app.globalData.cookieInfo
          },
          success: function (res) {
            console.log("ty/tyExpert/read/list成功后返回的res：", res)
            if(res.data.httpCode == 200){
              var lists = res.data.data.list;   //查询的数据
              var expertList = that.data.expertList;  //显示的数据
              //var tempParticiStaff = that.data.tempParticiStaff;
              //var tempExpert = that.data.tempExpert;
              //给所有数据添加cheched属性;
              for (var item of lists){
                item.checked = false;
                var exists=false;
                for (var i = 0; i < that.data.existExpertList.length; i++) {   //已选择的专家列表
                  if (item.id == that.data.existExpertList[i].id){
                    exists=true;
                    break;
                  }
                }
                if (!exists){
                    expertList.push(item);   //添加到显示数据中
                }
              }

              //hasOwnProperty
              that.setData({
                expertList: expertList,
                hidden: true,
              })
              that.data.pageNum++;
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
    }else{//没有cookie，尝试跳转登录页面
      wx.redirectTo({
        url: './../login/login'
      })
    }
  },
  //页面滑动到底部
  bindDownLoad: function () {
    var that = this;
    this.getExpertListInfo();
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


  // radioChange : function(e){
  //   var inputArr = [];
  //   inputArr.push(e.detail.value);
  //   //var userId = e.detail.value;
  //   this.handleExistList(inputArr);
  //   this.handleQueryList(inputArr);
  // },
  existCheckboxChange: function (e) {
    //console.log("checkboxChange::", e)
    // var userIds = e.detail.value;
    this.handleExistList(e.detail.value);
  },
  checkboxChange : function(e){
    console.log("checkboxChange::",e)
    //var userIds = e.detail.value;
    this.handleQueryList(e.detail.value)

  },
  handleExistList: function (inputUserId) {
    for (var i = 0; i < this.data.existExpertList.length; i++) {
      this.data.existExpertList[i].checked = false;
    }
    for (var userId of inputUserId) {
      for (var i = 0; i < this.data.existExpertList.length; i++) {
        if (userId == this.data.existExpertList[i].id) {
          this.data.existExpertList[i].checked = true;
        }
      }
    }
  },
  handleQueryList: function (inputUserId) {
    for (var i = 0; i < this.data.expertList.length; i++) {
      this.data.expertList[i].checked = false;
    }
    for (var userId of inputUserId) {
      for (var i = 0; i < this.data.expertList.length; i++) {
        if (userId == this.data.expertList[i].id) {
          this.data.expertList[i].checked = true;
        }
      }
    }
  },
  searchConfrm : function(e){
    this.setData({
      keyword: e.detail.value,
      expertList: [],
      pageNum: 1,
    })
    this.getExpertListInfo();
  },
  //确认，跳转回活动创建页面
  confmExpert : function(){
    var that = this;
    var selectExpert=[];
    for (var i = 0; i < this.data.existExpertList.length; i++) {
      if (this.data.existExpertList[i].checked) {
        selectExpert.push(this.data.existExpertList[i]);
      }
    }
    for (var i = 0; i < this.data.expertList.length; i++) {
      if (this.data.expertList[i].checked) {
        selectExpert.push(this.data.expertList[i]);
      }
    }
    // if(!this.data.isMutilSelect){
    //   if (selectedStaff.length>=1){
    //     var arr = getCurrentPages();
    //     wx.navigateBack({
    //       delta: 1,
    //       success: function () {
    //         arr[arr.length - 2].setData({
    //           contactStaff: selectedStaff[0],
    //         })
    //       }
    //     })
    //   }else{
    //     app.msgModal("提示消息：","您未选择联系人，请选择联系人！");
    //   }
    // }else{  // 添加参与人
      var arr = getCurrentPages();
      wx.navigateBack({
        delta: 1,
        success: function () {
          arr[arr.length - 2].setData({
            actExperts: selectExpert,//
          })
        }
      })
  },
})