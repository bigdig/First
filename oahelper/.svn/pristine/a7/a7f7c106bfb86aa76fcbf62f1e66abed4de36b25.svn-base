// pages/23xinxishenpi_detail/index.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    auditRecordsData: [],
    showIndex: 0,
    audityId: '',
    userId: '',
    name: '', //姓名
    title: '', //信息标题
    content: '', //信息内容
    choose_img_show: '', //图片
    status: '', //审核状态
    isImg: '',
    isUrl:false,
    prefix: '',
    imgArr:[],
    fileTypeArr:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let This = this;
    let that = this

    This.setData({
      userId: app.globalData.userInfo.id,
      audityId: options.id,
      prefix: app.helperRequestHost 
    })

    wx.showLoading({
      title: '加载中',
    })
    wx.request({
      url: app.helperRequestHost + '/pr/goAudity/read/detail',
      method: 'POST',
      data: {
        id: This.data.audityId
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function(res) {
        // console.log("查询活动详情返回的res：", res)
        if (res.data.httpCode == 200) {
          let arr = []
          let fileTypeArr = []
          let isLink = false
          if (res.data.data.audityPicture && res.data.data.audityPicture.length
>0){
            arr = res.data.data.audityPicture.split(',');
            for (let i = 0; i < arr.length; i++) {
              if (arr[i].indexOf('/') == 0) {
              } else {
                arr[i] = '/' + arr[i]
              }
              fileTypeArr.push(that.getFileType(arr[i]))
            }

}

          This.setData({
            userId: app.globalData.userInfo.id,
            audityId: options.id,
            name: res.data.data.userName, //姓名
            title: res.data.data.audityTitle, //信息标题
            content: res.data.data.audityLink, //信息内容
            //isImg: res.data.data.audityPicture,
            imgArr: arr,
            fileTypeArr: fileTypeArr,
            choose_img_show: app.helperRequestHost + '/' + res.data.data.audityPicture, //图片
            status: res.data.data.audityStatusText //审核状态
          })
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

    //退回原因查询
    wx.request({
      url: app.helperRequestHost + '/pr/goAudityRecord/selectByCondition',
      data: {
        audityId: This.data.audityId,
        //isPassed: 'NO'
      },
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success(res) {
        if (res.data.httpCode == 200) {
          if (res.data.data.length > 0) {
            let latestNoPass = null
            for (let i = res.data.data.length - 1; i >= 0; i--) {
              if (latestNoPass == null && res.data.data[i].isPassed == 'NO') {
                latestNoPass = (res.data.data[i].audityComment == null ? '' : res.data.data[i].audityComment);
                console.log(latestNoPass)
              }
              res.data.data[i].createTime_d = res.data.data[i].createTime.substring(0, 10);
              res.data.data[i].createTime_t = res.data.data[i].createTime.substring(11);
            }

            This.setData({
              auditRecordsData: res.data.data,
              tuihui_detail: latestNoPass
            })
          }
        } else if (res.data.httpCode == 401) {
          wx.redirectTo({
            url: './../login/login'
          })
        } else {
          app.msgModal("提示消息：", res.data.msg);
        }
        wx.hideLoading();
      }
    })
  
  },

  panel: function (e) {
    if (e.currentTarget.dataset.index != this.data.showIndex) {
      this.setData({
        showIndex: e.currentTarget.dataset.index
      })
    } else {
      this.setData({
        showIndex: 0
      })
    }
  },

  //复制链接
  copyLink(e) {
    //let This = this;
    let linkSrc = this.data.content;
    wx.setClipboardData({
      data: linkSrc,
      success(res) {
        wx.getClipboardData({
          success(res) {
            console.log(res.data) // data
          }
        })
      }
    })
  },
  openFile(e) {
    let that = this;
    let fileUrl = e.currentTarget.dataset.linksrc;
    let fileType = this.getFileType(fileUrl)
    if (fileType == 'png' || fileType == 'jpg' || fileType == 'jpeg' || fileType == 'bmp' || fileType == 'gif'){
      let fielUrlArr = []
      //
      app.globalData.uploadImg = true

      fielUrlArr.push(fileUrl)
      wx.previewImage({
        current: fileUrl, // 当前显示图片的http链接
        urls: fielUrlArr // 需要预览的图片http链接列表
      })
    }else{
      this.showLoading();

      wx.downloadFile({
        url: fileUrl,
        success: function (res) {
          console.log(res)

          let Path = res.tempFilePath              //返回的文件临时地址，用于后面打开本地预览所用
          wx.openDocument({
            filePath: Path,
            success: function (res) {
              that.cancelLoading()
              app.globalData.uploadImg = true
              console.log('打开成功');
            },
            fail: function (res) {
              console.log(res);
              app.msgModal("提示消息：", res.data.msg);
            }
          })
        },
        fail: function (res) {
          console.log(res);
        }
      })
    }
  },
  getFileTypeArr:function(arr){
    let fileTypeArr =[]
    for (let i = 0; i < arr.length; i++) {
      fileTypeArr.push(this.getFileType(arr[i]))
    }
    return fileTypeArr
  },
  getFileType: function (value) {
    console.log(value)
    return value.substring((value.lastIndexOf('.') + 1), value.length).toLocaleLowerCase();
  },
  showLoading: function () {
    wx.showToast({
      title: '加载中',
      icon: 'loading'
    });
  },
  cancelLoading: function () {
    wx.hideToast();
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