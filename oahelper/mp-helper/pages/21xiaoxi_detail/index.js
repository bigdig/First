// pages/21xiaoxi_detail/index.js

const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    informId: '',
    listData: [],
    imgArr:[],
    prefix:'',
    ifDisagree:false,
    backText:''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

    wx.showLoading({
      title: '页面加载中',
    })
    let This = this;
    This.setData({
      id: options.id,
      prefix: app.helperRequestHost + '/'
    })

    var that = this;
    // console.log(app.globalData.cookieInfo);
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goInformUser/read/detail',
        method: 'POST',
        data: {
          id: that.data.id
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function(res) {
          // console.log("查询活动详情返回的res：", res)
          if (res.data.httpCode == 200) {
            let arr=[]
            let imgStr = res.data.data.inform.externalLink
            
            if (res.data.data.inform.srcType != 'AUDITY' && imgStr && imgStr.length > 0) {
              if (imgStr.substr(0, 1) == ',') {
                imgStr = imgStr.substr(1);
              }
              imgStr = (imgStr.substring(imgStr.length - 1) == ',') ? imgStr.substring(0, imgStr.length - 1) : imgStr;
              arr = imgStr.split(',');
            }
            console.log(arr);
            that.setData({
              listData: res.data.data,
              imgArr:arr
            })
            // console.log(that.data)
            wx.hideLoading();
          } else if (res.data.httpCode == 401) {
            wx.redirectTo({
              url: './../login/login'
            })
          } else {
            app.msgModal("提示消息：", res.data.msg);
          }
        },
        fail: function(res) {
          app.msgModal("提示消息：", res.data.msg);
        }
      })
    }
  },
  //复制链接
  copyLink(e) {
    //let This = this;
    let linkSrc = e.currentTarget.dataset.linksrc;
    wx.setClipboardData({
      data: linkSrc,
      success(res) {
        wx.getClipboardData({
          success(res) {
            // console.log(res.data) // data
          }
        })
      }
    })
  },
  confirm(){
    this.go_list('YES');
  },
  //填写异议理由
  bindInputVal(e) {
    let This = this;
    This.setData({
      backText: e.detail.value
    });
  },
  disagree() {
    this.setData({
      ifDisagree:true
    })
  },
  cancelDisagree(){
    this.setData({
      ifDisagree: false
    })
  },
  submitDisagree() {
    if (this.data.backText==''){
      app.msgModal("提示消息：", '请输入异议理由');
      return;
    }
    this.go_list('DISAGREE');
  },
  go_list(isReplied) {
    let that = this
    let remark = ''
    if (isReplied == 'DISAGREE'){
      remark = that.data.backText
    }
    wx.showLoading({
      title: '提交中',
    })
    wx.request({
      url: app.helperRequestHost + '/pr/goInformUser/confirm',
      method: 'POST',
      data: {
        id: that.data.id,
        remark: remark,
        isReplied: isReplied
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function(res) {
        // console.log("查询活动详情返回的res：", res)
        if (res.data.httpCode == 200) {
          // console.log(that.data)
          wx.hideLoading();
        } else if (res.data.httpCode == 401) {
          wx.redirectTo({
            url: './../login/login'
          })
        } else {
          app.msgModal("提示消息：", res.data.msg);
        }
      },
      fail: function(res) {
        app.msgModal("提示消息：", res.data.msg);
      }
    })

    wx.navigateBack({
      delta: 1
    })

  },
  openImg(e) {
    let fileUrl = e.currentTarget.dataset.linksrc;
    let fielUrlArr = []
    console.log(fileUrl);
    for (let i = 0; i < this.data.imgArr.length; i++) {
      fielUrlArr.push(this.data.prefix + this.data.imgArr[i])
    }
    //fielUrlArr.push(fileUrl)
    wx.previewImage({
      current: fileUrl, // 当前显示图片的http链接
      urls: fielUrlArr // 需要预览的图片http链接列表
    })
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