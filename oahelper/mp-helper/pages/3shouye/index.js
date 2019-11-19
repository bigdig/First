// pages/3shouye/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tongzhi: '', //通知显示文本及链接配置
    toupiao: '', //投票显示文本及链接配置
    huodong: '' //活动显示文本及链接配置
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let This = this;

    wx.showLoading({
      title: '页面加载中',
    })
    //查询数据 所需参数
    let dataJson = {
      
    }
    
    /*test start*/
    let res = {
      tongzhi: {
        0: { text: '通知1显示文本', link: '链接配置' },
        1: { text: '通知2', link: '链接配置' },
        2: { text: '通知2', link: '链接配置' }
      },
      toupiao: {
        0: { text: '投票1显示文本', link: '链接配置' },
        1: { text: '投票2显示文本', link: '链接配置' },
        2: { text: '投票2显示文本', link: '链接配置' }
      },
      huodong: {
        0: { text: '活动显示文本', link: '链接配置' },
        1: { text: '活动显示887文本', link: '链接配置' },
        2: { text: '活动显示文本', link: '链接配置' }
      }
    }
    
    This.setData({
      tongzhi: res.tongzhi,
      toupiao: res.toupiao,
      huodong: res.huodong
    });
    setTimeout(function () {
      wx.hideLoading();
    }, 1000)
    /*test end*/


    return;


    //请求获取数据
    wx.request({
      url: 'test.php', //仅为示例，并非真实的接口地址
      data: dataJson,
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        //绑定列表数据
        This.setData({
          tongzhi: res.tongzhi,
          toupiao: res.toupiao,
          huodong: res.huodong
        });
        wx.hideLoading();
      }
    })
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

  }
})