// pages/4chenhui/index.js
const app = getApp();

Page({
  /**
   * 页面的初始数据
   */
  data: {
    week: '', //星期几
    dateText: '', //当天日期
    choose_type: '', //选择类型
    index: 0, // 现场参加对应地址 所选择的 key
    choose_img_show: null, //请假申请凭证图片
    imgURL: null,
    imgPath: '',

    user_id: '',
    user_name: '',
    is_attend: false,
    atw: '',
    pw: '',
    aw: '',


    attendence_way: [], // 现场参加对应地址
    absence_way: [],
    present_way: []
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log('chenhui onLoad')
    let This = this;

    //今天日期参数
    var curDate = new Date()
    var weekday = new Array(7)
    weekday = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"]
    let month = (curDate.getMonth() + 1) < 10 ? '0' + (curDate.getMonth() + 1) : '' + (curDate.getMonth() + 1)
    let day = curDate.getDate() < 10 ? '0' + curDate.getDate() : ''+curDate.getDate()
    This.setData({
      week: weekday[curDate.getDay()],
      dateText: curDate.getFullYear() + '-' + month + '-' + day
    });


    var that = this;

    //是否已经签到了
    that.setData({
      user_id: app.globalData.userInfo.id,
      user_name: app.globalData.userInfo.userName,
    })

    wx.request({
      url: app.helperRequestHost + '/pr/goUserMorningMeeting/read/list',
      method: 'POST',
      data: {
        userId: This.data.user_id,
        meetingDate: This.data.dateText
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function(res) {
        if (res.data.httpCode == 200) {
          var l = res.data.data.list
          // console.log(l)
          if (l.length > 0) {
            that.setData({
              is_attend: true,
              atw: l[0].attendenceWayText,
              pw: l[0].presentWayText,
              aw: l[0].absenceWayText,
              imgPath: l[0].absencePicture,
              imgURL: app.helperRequestHost + '/'+l[0].absencePicture
            })
          }
        }
      }
    });



    wx.request({
      url: app.helperRequestHost + '/pr/myCatalog/read/catalog',
      method: 'POST',
      data: {
        type: 'MEETING_WAY'
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function(res) {
        if (res.data.httpCode == 200) {
          that.setData({
            attendence_way: res.data.dicts.MEETING_WAY,
          })
        }
      }
    });

    wx.request({
      url: app.helperRequestHost + '/pr/myCatalog/read/catalog',
      method: 'POST',
      data: {
        type: 'ABSENCE_WAY'
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function(res) {
        if (res.data.httpCode == 200) {
          that.setData({
            absence_way: res.data.dicts.ABSENCE_WAY,
          })
        }
      }
    });

    wx.request({
      url: app.helperRequestHost + '/pr/myCatalog/read/catalog',
      method: 'POST',
      data: {
        type: 'PRESENT_WAY'
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function(res) {
        if (res.data.httpCode == 200) {
          that.setData({
            present_way: res.data.dicts.PRESENT_WAY,
          })
        }
      }
    });

  },
  //选择类别：现场参加 / 电话接入 / 请假（出差/调研/路演/休假/事假）
  choose_type(e) {
    let This = this;
    This.setData({
      choose_type: e.currentTarget.dataset.choose_type
    });
  },
  //选择 现场参加对应地址
  bindPickerChange: function(e) {
    let This = this;
    console.log('picker发送选择改变，携带值为', e)
    This.setData({
      index: e.detail.value
    })
  },
  //选择图片
  chooseImg() {
    let that = this;
    app.globalData.uploadImg = true
    
    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success(res) {
        let tempFilePaths = res.tempFilePaths
        wx.uploadFile({
          url: app.helperRequestHost + '/uploadfile/toLocal',
          header: {
            'content-type': 'application/x-www-form-urlencoded', // 默认值
            'cookie': app.globalData.cookieInfo
          },
          filePath: tempFilePaths[0],
          name: 'file',
          success: function(res) {
            app.globalData.uploadImg = true;
            var data = JSON.parse(res.data);
            if (data.httpCode == 200) {
              that.setData({
                choose_img_show: data.files[0],
                imgURL: app.helperRequestHost + '/' + 'uploadfile/previewfile?fileUrl=' + data.files[0],
                imgPath: '/uploadfile/previewfile?fileUrl=' + data.files[0]
              })
            } else {
              app.msgModal("提示消息：", "上传图片出错，请稍后重试！");
            }
          },
        })
      }
    })
  },
  //提交选择
  sendData() {
    let This = this;
    if (!This.data.choose_type) {
      wx.showModal({
        title: '提示',
        content: '请选择参会方式',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (This.data.choose_type == 3 && !This.data.choose_img_show) {
      wx.showModal({
        title: '提示',
        content: '请上传凭证',
        showCancel: false,
        success(res) {}
      });
      return false;
    }

    wx.showLoading({
      title: '提交中',
    })


    let chenhui = {
      meetingDate: This.data.dateText,
      attendenceWay: '',
      presentWay: '',
      absenceWay: '',
      absencePicture: ''
    };

    if (This.data.choose_type == 1) {
      chenhui.attendenceWay = 'PRESENT';
      chenhui.presentWay = This.data.present_way[This.data.index].id
    } else if (This.data.choose_type == 2) {
      chenhui.attendenceWay = 'CLIENT';
    } else {
      chenhui.attendenceWay = 'ABSENT';
      chenhui.absenceWay = This.data.absence_way[This.data.index].id
      chenhui.absencePicture = This.data.imgPath
    }

    //提交数据
    wx.request({
      url: app.helperRequestHost + '/pr/goUserMorningMeeting/attend',
      data: chenhui,
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success(res) {
        wx.hideLoading();
        wx.showToast({
          title: '提交成功',
          icon: 'success',
          duration: 1000
        })
        This.onLoad()
      }
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
    //从上传图片返回时onShow不做处理
    console.log('onShow')
    if (app.globalData.uploadImg) {
      console.log('从上传图片返回时onShow不做处理')
      app.globalData.uploadImg = false
      return
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