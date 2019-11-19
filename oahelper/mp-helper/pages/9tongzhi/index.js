// pages/9tongzhi/index.js
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    search_text:'', //搜索关键词
    if_search:false, //是否搜索状态

    list:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let This = this;
    wx.showLoading({
      title: '页面加载中',
    })

    var that = this;
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/myCatalog/read/catalog',
        method: 'POST',
        data: {
          type: 'NOTICE_TYPE'
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {

          if (res.data.httpCode == 200) {
            that.setData({
              list: res.data.dicts.NOTICE_TYPE,
            })
            
          } else if (res.data.httpCode == 401) {
            wx.redirectTo({
              url: './../login/login'
            })
          } else {
            app.msgModal("提示消息：", res.data.msg);
          }

          wx.hideLoading();
        },
        fail: function (res) {
          app.msgModal("提示消息：", res.data.msg);
        }
      })
    }
 
  },



  /*    =========搜索相关 start==========    */
  //输入搜索关键词
  bindSearch(e){
    let This = this;
    This.setData({
      search_text: e.detail.value
    });
  },
  //清除搜索关键词
  clearSearch() {
    let This = this;
    This.setData({
      search_text: ''
    });
  },
  //开始搜索
  toSearch() {
    let This = this;
    if (!This.data.search_text){
      wx.showModal({
        title: '提示',
        content: '请输入搜索关键词',
        showCancel: false,
        success(res) {
        }
      });
      return false;
    }
    //本页面中跳转至列表页面（搜索结果显示在列表页面）
    wx.navigateTo({
      url: '/pages/10tongzhi_list/index?search_text=' + This.data.search_text
    })
    return false;
  },
  /*    =========搜索相关 end  ==========    */






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
  
  }
})