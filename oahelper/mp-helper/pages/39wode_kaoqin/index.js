// pages/39wode_kaoqin/index.js

const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    search_text: '', //搜索关键词
    if_search: false, //是否搜索状态

    userId: '',
    pageSize: 10,
    listData: [],
    cur_page: 1, //当前页码
    ifHaveMore: false, //是否还有下一页
    srcType: 'RMD_PC'
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  },
  //加载更多
  nextPage() {
    let This = this;
    This.setData({
      cur_page: This.data.cur_page + 1
    });
    //加载列表数据
    This.getData();
  },


  //获取当前页数据
  getData: function () {
    let This = this;
    //查询数据 所需
    wx.showLoading({
      title: '页面加载中',
    })

    var that = this;
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goInformUser/selectUserInform',
        method: 'POST',
        data: {
          userId: that.data.userId,
          srcType: 'RMD_PC',
          pageSize: that.data.pageSize,
          pageNum: that.data.cur_page,
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          // console.log("查询活动详情返回的res：", res)
          if (res.data.httpCode == 200) {
            let data = res.data.data;
            //绑定列表数据
            This.setPageList(data);
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
  //绑定列表数据
  setPageList(res) {
    let This = this;
    /*查询数据库后给变量赋值，这样配置页面数据*/
    if (res.list != '') {
      let aa;
      if (This.data.listData != '' && This.data.cur_page > 1) {
        aa = This.data.listData;
      } else {
        aa = new Array();
      }
      for (var item in res.list) {
        if(res.list[item].isReplied=='NO'){
          res.list[item].status = '待确认'
        } else if (res.list[item].isReplied == 'DISAGREE') {
          res.list[item].status = '有异议'
        } else{
          res.list[item].status = '已确认'
        }
        aa.push(res.list[item]);
      }
      This.setData({
        ifHaveMore: res.hasNextPage, //是否还有下一页
        listData: aa
      });
    } else {
      This.setData({
        ifHaveMore: res.hasNextPage, //是否还有下一页
        listData: []
      });
    }
  },




/*    以下搜索功能，本页面效果图没有搜索，所以需要加上时，将.wxml文件顶部代码开启   */




  /*    =========搜索相关 start==========    */
  //输入搜索关键词
  bindSearch(e) {
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
    if (!This.data.search_text) {
      wx.showModal({
        title: '提示',
        content: '请输入搜索关键词',
        showCancel: false,
        success(res) {
        }
      });
      return false;
    }
    This.setData({
      if_search: true,
      cur_page: 1
    });
    //加载列表数据
    This.getData();
  },
  //取消搜索
  cancelSearch() {
    let This = this;
    //当前页 搜索的
    This.setData({
      search_text: '',
      if_search: false,
      cur_page: 1
    });
    //加载列表数据
    This.getData();
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
    let This = this;
    This.setData({
      userId: app.globalData.userInfo.id,
      listData: [],
      cur_page: 1
    })
    This.getData();
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