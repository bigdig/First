const app = getApp();

//var pageNum = 1;
var pageSize = 15;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    seachStateText: null,
    inputVal: '',
    custStatusId: '',
    mechLists: [],
    hidden: true,
    scrollTop: 0,
    scrollHeight: 0,
    pageNum : 1,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (options != null) {
      for (var k in options) {
        if (k == 'custStatusId') {
          this.setData({
            custStatusId: options[k],
          })
        } else {
          this.setData({
            seachStateText: options[k],
          })
        }
      }
    }
    if (options.custStatusId == 2){
      wx.setNavigationBarTitle({
        title: '潜在机构',
      })
    }
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
    var seachStateText = this.data.seachStateText;
    if (seachStateText != null) {
      this.setData({
        inputVal: seachStateText,
      });
    };
    this.listTraversal();
  },
  inputBlur: function (e) {
    this.setData({
      inputVal: e.detail.value,
    });
    this.listTraversal();
  },
  listTraversal:function(){
    var that = this;
    that.setData({
      hidden: false
    });
    wx.request({
      url: app.crmRequestHost + '/ty/tyServiceorg/read/list',
      method: 'POST',
      data: {
        custStatus: that.data.custStatusId,
        orgName: that.data.inputVal,
        pageNum: that.data.pageNum,
        pageSize: pageSize,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var lists = res.data.data.list;
          var mechLists = that.data.mechLists;
          //判断当前是否是从关键字搜索
          if (that.data.inputVal != '') {
            if (mechLists.length == 0){
              mechLists = lists;
            }else{
              for (var i = 0; i < lists.length;i++){
                mechLists.push(lists[i]);
              }
            }
          } else {
            if (mechLists.length == 0) {
              mechLists = lists;
            } else {
              for (var i = 0; i < lists.length; i++) {
                mechLists.push(lists[i]);
              }
            }
          }
          that.setData({
            mechLists: mechLists,
            hidden: true
          })
          that.data.pageNum++;
        } else if (res.data.httpCode == 401) {
          wx.redirectTo({
            url: './../login/login'
          })
        } else {
          that.msgModal("提示消息：", res.data.msg);
        }
      },
      fail: function (res) {
        that.msgModal("提示消息：", res.data.msg);
      }
    })
  },
  //页面滑动到底部
  bindDownLoad: function () {
    var that = this;
    this.listTraversal();
  },
  scroll: function (event) {
    //该方法绑定了页面滚动时的事件，我这里记录了当前的position.y的值,为了请求数据之后把页面定位到这里来。
    this.setData({
      scrollTop: event.detail.scrollTop
    });
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    // this.setData({
    //   mechLists: [],
    // })
    // this.listTraversal();
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