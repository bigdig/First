// pages/22xinxishenpi/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    name:'',
    title:'',
    content:'',
    choose_img_show: '', //图片
    
    choose_type: '', //选择类型（同意、退回）
    backText:'', //退回理由
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let This = this;
    console.log('id:' + options.id);

    wx.showLoading({
      title: '加载中',
    })

    /*test*/
    let res = {
      status : 1,
      data : {
        name: '99陈晓敏',
        title: '宏观点评：如何理解最近一年房地产数据“异常”',
        content: 'http://xxxxxxxxxxxxxxxxxxxxxxxx.com',
        choose_img_show: '/img/wj_none.png', //图片
      }
    }
    wx.hideLoading();
    This.setData({
      name: res.data.name,
      title: res.data.title,
      content: res.data.content,
      choose_img_show: res.data.choose_img_show,
    });
    /*test*/

    return;

    //根据ID查询数据
    wx.request({
      url: 'test.php', //仅为示例，并非真实的接口地址
      data: { id: options.id },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        wx.hideLoading();
        This.setData({
          name: res.data.name,
          title: res.data.title,
          content: res.data.content,
          choose_img_show: res.data.choose_img_show,
        });
      }
    })
  },
  //选择类型（同意、退回）
  choose_type(e) {
    let This = this;
    This.setData({
      choose_type: e.currentTarget.dataset.choose_type
    });
  },
  //填写退回理由
  bindInputVal(e){
    let This = this;
      This.setData({
        backText: e.detail.value
      });
  },
  //提交
  sendData() {
    let This = this;
    if (!This.data.choose_type) {
      wx.showModal({
        title: '提示',
        content: '选择通过或退回再提交',
        showCancel: false,
        success(res) {
        }
      });
      return false;
    }

    if (This.data.choose_type == 2 && !This.data.backText) {
      wx.showModal({
        title: '提示',
        content: '请输入退回理由',
        showCancel: false,
        success(res) {
        }
      });
      return false;
    }

    wx.showLoading({
      title: '提交中',
    })

    //提交的信息 json
    let sendData = {
      choose_type: This.data.choose_type,
    };
    console.log(sendData);

    /*test*/
    setTimeout(function () {
      wx.hideLoading();
      wx.showToast({
        title: '提交成功',
        icon: 'success',
        duration: 1000
      })
    }, 1000)
    /*test*/

    return;

    //提交数据
    wx.request({
      url: 'test.php', //仅为示例，并非真实的接口地址
      data: sendData,
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        wx.hideLoading();
        wx.showToast({
          title: '提交成功',
          icon: 'success',
          duration: 1000
        })
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