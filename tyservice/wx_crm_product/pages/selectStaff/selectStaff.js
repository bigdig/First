const app = getApp();
var pageSize = 15;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    existStaffList: [],//前一页面传递过来的员工列表
    staffList : [], //员工列表
    //selectedStaff: [], //选择的员工
    keyword : '' , //查询关键字
    tempStaff : null , //选择的员工(联系人)
    tempParticiStaff : [] , //选择的员工(参与员工)
    isMutilSelect : false , //是否是多选
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
    if (app.globalData.choseNametype == '1'){//单选
      var tempStaff = app.globalData.actcipant;
      if (tempStaff.length > 0){
        for (var item of tempStaff){
          item.checked = true;
          break;
        }
      }
      
      lists = null;
      if (tempStaff != null) {
        tempStaff.checked = true;
      }
      this.setData({
        existStaffList: tempStaff,
      })
    }else{//多选
      var lists = app.globalData.activeParticipant;
      //tempStaff =null;
      if (lists != null) {
        for (var i = 0; i < lists.length; i++) {
          lists[i].checked = true;
        }
        this.setData({
          existStaffList: lists,
        })
      }
    }

    //是否是多选
    if(options.isMutilSelect){
      this.setData({
        isMutilSelect : options.isMutilSelect,
      })
      wx.setNavigationBarTitle({
        title: '选择参与人'
      })
    }
    this.getStaffListInfo();
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
  getStaffListInfo : function(){
    var that = this;
    that.setData({
      hidden: false
    });
    if(app.globalData.cookieInfo){ //已登录
      console.log(app.globalData.cookieInfo)
        //查询我的活动信息
        wx.request({
          url: app.crmRequestHost + '/ty/tyStafflist/read/list',
          method: 'POST',
          data: {
            deleteFlag : '0',
            keyword: that.data.keyword,
            pageNum: that.data.pageNum,
            pageSize: pageSize,
          },
          header: {
            'content-type': 'application/x-www-form-urlencoded', // 默认值
            'cookie': app.globalData.cookieInfo
          },
          success: function (res) {
            console.log("ty/tyStafflist/read/list成功后返回的res：", res)
            if(res.data.httpCode == 200){
              var lists = res.data.data.list;
              var staffList = that.data.staffList;
              //var tempParticiStaff = that.data.tempParticiStaff;
              //var tempStaff = that.data.tempStaff;
              //给所有数据添加cheched属性;
              for (var item of lists){
                item.checked = false;
                var exists=false;
                for (var i = 0; i < that.data.existStaffList.length; i++) {
                  if (item.id == that.data.existStaffList[i].id){
                    exists=true;
                    break;
                  }
                }
                if (!exists){
                    staffList.push(item);
                }
              }

              //hasOwnProperty
              that.setData({
                staffList: staffList,
                hidden: true,
              })
              that.data.pageNum++;
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
  //页面滑动到底部
  bindDownLoad: function () {
    var that = this;
    this.getStaffListInfo();
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


  radioChange : function(e){
    var inputArr = [];
    inputArr.push(e.detail.value);
    //var userId = e.detail.value;
    this.handleExistList(inputArr);
    this.handleQueryList(inputArr);
  },
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
    for (var i = 0; i < this.data.existStaffList.length; i++) {
      this.data.existStaffList[i].checked = false;
    }
    for (var userId of inputUserId) {
      for (var i = 0; i < this.data.existStaffList.length; i++) {
        if (userId == this.data.existStaffList[i].id) {
          this.data.existStaffList[i].checked = true;
        }
      }
    }
  },
  handleQueryList: function (inputUserId) {
    for (var i = 0; i < this.data.staffList.length; i++) {
      this.data.staffList[i].checked = false;
    }
    for (var userId of inputUserId) {
      for (var i = 0; i < this.data.staffList.length; i++) {
        if (userId == this.data.staffList[i].id) {
          this.data.staffList[i].checked = true;
        }
      }
    }
  },
  searchConfrm : function(e){
    this.setData({
      keyword: e.detail.value,
      staffList: [],
      pageNum: 1,
    })
    this.getStaffListInfo();
  },
  //确认，跳转回活动创建页面
  confmContactPerson : function(){
    var that = this;
    var selectedStaff=[];
    for (var i = 0; i < this.data.existStaffList.length; i++) {
      if (this.data.existStaffList[i].checked) {
        selectedStaff.push(this.data.existStaffList[i]);
      }
    }
    for (var i = 0; i < this.data.staffList.length; i++) {
      if (this.data.staffList[i].checked) {
         selectedStaff.push(this.data.staffList[i]);
      }
    }
    if(!this.data.isMutilSelect){
      if (selectedStaff.length>=1){
        var arr = getCurrentPages();
        wx.navigateBack({
          delta: 1,
          success: function () {
            arr[arr.length - 2].setData({
              contactStaff: selectedStaff[0],
            })
          }
        })
      }else{
        that.msgModal("提示消息：","您未选择联系人，请选择联系人！");
      }
    }else{  // 添加参与人
      var arr = getCurrentPages();
      wx.navigateBack({
        delta: 1,
        success: function () {
          arr[arr.length - 2].setData({
            actAnticepate: selectedStaff,//that.data.tempParticiStaff,
          })
        }
      })
    }
  },
   //模态弹窗
   msgModal : function(title,msg,showCancel){
    wx.showModal({
      title: title,
      content : msg,
      showCancel: (showCancel == null ? false : showCancel),
    })
  },
})