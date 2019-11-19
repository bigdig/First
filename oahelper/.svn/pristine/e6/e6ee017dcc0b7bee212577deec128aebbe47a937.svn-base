// pages/19xiaoxi/index.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgList: {
      'MEETING': '/img/xx1.png',
      'AUDITY': '/img/xx2.png',
      'INFO': '/img/xx3.png',
      'NOTICE': '/img/xx3.png',
      'RMD_EN': '/img/xx5.png',
      'RMD_ER': '/img/xx5.png',
      'RMD_ET': '/img/xx5.png',
      'RMD_PC': '/img/xx6.png',
      'RMD_BC': '/img/xx5.png',
    },

    userId: '',
    pageSize: 10,
    listData: [],
    cur_page: 1, //当前页码
    ifHaveMore: false, //是否还有下一页
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function() {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    let This = this;
    This.setData({
      userId: app.globalData.userInfo.id,
      listData: [],
      cur_page: 1
    })
    This.getData();
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
  getData: function() {
    let This = this;
    //查询数据 所需
    let dataJson = {
      search_text: This.data.search_text, //搜索关键词
      cur_page: This.data.cur_page, //当前页码
    }


    wx.showLoading({
      title: '页面加载中',
    })

    var that = this;
    // console.log(app.globalData.cookieInfo);
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goInformUser/selectUserInform',
        method: 'POST',
        data: {
          userId: that.data.userId,
          pageSize: that.data.pageSize,
          pageNum: that.data.cur_page,
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function(res) {
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
        fail: function(res) {
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
        //处理时间（去掉时分秒）
        // res.list[item].createTime = app.setDateTime(res.list[item].createTime);
        var inform = res.list[item].inform
        if (inform.srcType === 'MEETING') {
          inform.TypeText = '晨会提醒'
        } else if (inform.srcType === 'NOTICE') {
          inform.TypeText = '通知提醒'
        } else if (inform.srcType === 'RMD_EN') {
          inform.TypeText = '一般报销单提醒'
        } else if (inform.srcType === 'RMD_ER') {
          inform.TypeText = '退回报销单提醒'
        } else if (inform.srcType === 'RMD_ET') {
          inform.TypeText = '累计报销提醒'
        } else if (inform.srcType === 'RMD_PC') {
          inform.TypeText = '考勤确认提醒'
        } else if (inform.srcType === 'INFO') {
          inform.TypeText = '一般消息'
        } else if (inform.srcType === 'AUDITY') {
        inform.TypeText = '信息审批'
        } else if (inform.srcType === 'RMD_BC') {
          inform.TypeText = '账单核对通知'
        }else{
          inform.TypeText = '其他'
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


  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})