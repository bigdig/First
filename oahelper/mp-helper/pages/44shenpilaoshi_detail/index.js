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
    fileTypeArr: [],
    choose_type:null,
    backText:null,
    audityStatus:'',
    isPassed:'',
    audityComment:'',
    contentUrl:[],
    tuihui_detail: '' //退回原因

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
              if (arr[i].indexOf('/') == 0){
              }else{
                arr[i] = '/' + arr[i]
              }
              fileTypeArr.push(that.getFileType(arr[i]))
            }

}
          let contentArr=[]
          let content=null
          if (res.data.data.audityLink.indexOf('http')==0){
            contentArr = res.data.data.audityLink.split(',')
          }else{
            content = res.data.data.audityLink
          }
          console.log(contentArr)
          This.setData({
            userId: app.globalData.userInfo.id,
            audityId: options.id,
            name: res.data.data.userName, //姓名
            title: res.data.data.audityTitle, //信息标题
            content: content, //信息内容
            contentArr:contentArr,
            imgArr: arr,
            fileTypeArr: fileTypeArr,
            choose_img_show: app.helperRequestHost + '/' + res.data.data.audityPicture, //图片
            status: res.data.data.audityStatusText, //审核状态
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
          if (res.data.data.length>0){
            let latestNoPass = null
            for (let i = res.data.data.length-1; i >=0;i--){
              if (latestNoPass == null && res.data.data[i].isPassed=='NO'){
                latestNoPass = (res.data.data[i].audityComment == null ? '' : res.data.data[i].audityComment);
                console.log(latestNoPass)
              }
              res.data.data[i].createTime_d = res.data.data[i].createTime.substring(0,10);
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
    let linkSrc = e.currentTarget.dataset.linksrc;
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
    if (fileType == 'png' || fileType == 'jpg' || fileType == 'jpeg' || fileType == 'bmp' || fileType == 'gif') {
      let fielUrlArr = []
      //
      fielUrlArr.push(fileUrl)
      wx.previewImage({
        current: fileUrl, // 当前显示图片的http链接
        urls: fielUrlArr // 需要预览的图片http链接列表
      })
    } else {
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
  //选择类型（同意、退回）
  choose_type(e) {
    let This = this;
    This.setData({
      choose_type: e.currentTarget.dataset.choose_type
    });
  },
  //填写退回理由
  bindInputVal(e) {
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

    let audity = {
      audityId: This.data.audityId,
      audityStatus: This.data.choose_type == 2 ? 'RETURNED' :'PASSED' ,//RECOMMITED,
      isPassed: This.data.choose_type == 2 ? 'NO' : 'YES',//NO,
      audityComment: This.data.backText// 
    };

    let c_url = app.helperRequestHost + '/pr/goAudity/modify'
    //提交数据
    wx.request({
      url: c_url,
      data: audity,
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success(res) {
        if (res.data.httpCode == 200) {
          wx.showToast({
            title: '提交成功',
            icon: 'success',
            duration: 1000
          })

          setTimeout(function () {
            wx.switchTab({
              url: "/pages/index/index"
            })
          }, 1000)
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