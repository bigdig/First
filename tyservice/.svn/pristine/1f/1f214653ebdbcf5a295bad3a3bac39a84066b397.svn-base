// pages/newGroup/newGroup.js

const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    num:'',
    custId:null,
    disabled:false,
    disabutton:false,
    allGoodsFilte: [], 
    groupId:null,
    groupName:null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    //this.data.custId = app.globalData.custId_group;
    var custId = JSON.parse(options.custId_group);
    this.setData({
      num: custId.length,
      custId: custId,
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.group();
  },
  //选择现有组
  tab: function (e) {
    if (this.data.groupName == null || this.data.groupName == ''){
      var item = e.currentTarget.dataset;
      var lists = this.data.allGoodsFilte;
      var custIds = [];
      var groupId = null;
      if (!item.checked) {
        for (var i = 0; i < lists.length; i++) {
          if (lists[i].id == item.id) {
            groupId = item.id;
            lists[i].checked = true;
          } else {
            lists[i].checked = false;
          }
        }
        this.setData({
          groupId: groupId,
          allGoodsFilte: lists,
          disabled: true,
        })
      } else {
        for (var i = 0; i < lists.length; i++) {
          if (lists[i].id == item.id) {
            lists[i].checked = false;
          }
        }
        this.setData({
          groupId: null,
          allGoodsFilte: lists,
          disabled: false,
        })
      }
    }else{
      return;
    }
  },
  //新建组
  bindTextAreaBlur:function(e){
    console.log(e.detail.value)
    var groupName = null;
    groupName = e.detail.value;
    if (groupName != ''){
      this.setData({
        groupName: groupName,
        disabutton: true,
      })
    }else{
      this.setData({
        groupName: groupName,
        disabutton: false,
      })
    }
    console.log(this.data.groupName)
  },
  //新建分组
  submit:function(){
    var that = this;
    var custIds = [];
    for (var i = 0; i < that.data.custId.length;i++){
      custIds.push(that.data.custId[i].id);
    }
    var obj = {};
    if (that.data.groupId == null) {
      obj['custIds[]'] = custIds;
      obj.groupName = that.data.groupName;
    } else {
      obj['custIds[]'] = custIds;
      obj.groupId = that.data.groupId;
    }
    if (that.data.groupId != null || that.data.groupName != null){
      wx.request({
        url: app.crmRequestHost + '/ty/tyCustgroup/batchUpdateGroupDetail',
        method: 'POST',
        data: obj,
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          if (res.data.httpCode == 200) {
            //app.msgModal("提示消息", "新建分组成功");
            wx.switchTab({
              url: './../customer/customer',
            })
          } else if (res.data.httpCode == 401) {
            wx.redirectTo({
              url: './../login/login'
            })
          } else {
            app.msgModal("提示消息", res.data.msg);
          }
        },
        fail: function (res) {
          app.msgModal("提示消息", res.data.msg);
        }
      });
    }else{
      app.msgModal("提示消息", '请选择一个分组或者新建一个分组');
    }
    
    
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
      
  },
  group:function(){
    var that = this;
    //个人详情请求
    wx.request({
      url: app.crmRequestHost + '/ty/tyCustgroup/read/list',
      method: 'POST',
      data: {

      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var item = res.data.data.list;
          var allGoodsFilte = [];
          for (var i = 0; i < item.length;i++){
            allGoodsFilte.push({ 'id': item[i].id , 'name': item[i].custGroupname , 'checked':false});
          }
          that.setData({
            allGoodsFilte: allGoodsFilte,
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