// pages/addLabel/addLabel.js
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    customerId:null,
    recommendLabel:[],
    addLabel:[],
    inputVal:'',
    searchLabel:false,
    existenceLabel:false,
    addActLabel : false,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var addLabel = app.globalData.storageLabel;
    var customerId = options.customerId;
    var addActLabel = options.addActLabel;
    if (addLabel.length > 0){
      this.setData({
        addLabel: addLabel,
        existenceLabel:true,
      })
    }
    if(customerId){
      this.setData({
        customerId : customerId,
      })
    }
    if(addActLabel){
      this.setData({
        addActLabel : addActLabel,
      })
    }
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
  //推荐标签
  info:function(){
    var that = this;
    var inputVal = that.data.inputVal;
    wx.request({
      url: app.crmRequestHost + '/ty/tyLabel/read/list',
      method: 'POST',
      data: {
        labelName:inputVal,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
           var lists = res.data.data.list;
           that.setData({
             recommendLabel: lists,
             searchLabel:true,
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
  //input事件
  inputBlur:function(e){
    if (e.detail.value == ''){
      this.setData({
        searchLabel:false,
        existenceLabel:false,
      })
    }else{
      this.setData({
        inputVal: e.detail.value,
      })
      this.info();
    }
  },
  //新增选中标签
  addSelect:function(e){
    var addLabel = this.data.addLabel;
    var labelname = e.currentTarget.dataset.labelname;
    if (addLabel.length == 0){
      addLabel.push(labelname);
    }else{
      if (addLabel.indexOf(labelname) == -1){
        addLabel.push(labelname);
      }
    }
    this.setData({
      existenceLabel:true,
      addLabel: addLabel,
    })
  },
  //新增input标签
  add:function(e){
    if (this.data.inputVal == ""){
      this.setData({
        searchLabel: false,
        existenceLabel:false,
      })
    }else{
      var inputVal = this.data.inputVal;
      var addLabel = this.data.addLabel;
      if (addLabel.indexOf(inputVal) == -1) {
        addLabel.push(inputVal);
      }
      this.setData({
        addLabel: addLabel,
        searchLabel: true,
        existenceLabel:true,
      })
    }
  },
  //删除选中标签
  deleteLabel:function(e){
    var addLabel = this.data.addLabel;
    var labelname = e.currentTarget.dataset.labelname;
    var $index = addLabel.indexOf(labelname);
    if ($index > -1){
      addLabel.splice($index,1);
    }
    this.setData({
      addLabel: addLabel,
    })
  },
  //提交
  submit:function(){
    var that = this;
    var addLabel = that.data.addLabel;
    var customerId = that.data.customerId;
    wx.request({
      url: app.crmRequestHost + '/ty/tyCustomerlabel/add',
      method: 'POST',
      data: {
        'labelName[]': addLabel,
        customerId: customerId,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          wx.navigateTo({
            url: '../customerDetails/customerDetails?id=' + that.data.customerId,
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
  
  },
  addActLabel : function(){
    var that = this;
    var arr = getCurrentPages();
        wx.navigateBack({
          delta: 1,
          success: function () {
            arr[arr.length - 2].setData({
              actLabels : that.data.addLabel,
            })
          }
        })
  },
})