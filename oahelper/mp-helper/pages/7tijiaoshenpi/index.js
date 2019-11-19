// pages/7tijiaoshenhe/index.js
const app = getApp();


Page({

  /**
   * 页面的初始数据
   */
  data: {
    auditRecordsData: [],
    showIndex: 0,
    height:160,
    user_id: '',
    name: '',
    title: '',
    content: '',
    choose_img_show: '', //图片
    chooseImgArr:[],
    fileTypeArr: [],

    prefix: '',
    imgURL: null,
    imgPath: '',

    curId: '', //审批未通过时的ID
    sendTime: '', //上次提交的时间
    status: '', //审核状态
    tuihui_detail: '' //退回原因
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let This = this;
    This.setData({
      user_id: app.globalData.userInfo.id,
      name: app.globalData.userInfo.userName,
      prefix: app.helperRequestHost 
    })

    if (options.id) {
      This.setData({
        curId: options.id
      })

      wx.showLoading({
        title: '提交中',
      })

      //提交数据
      wx.request({
        url: app.helperRequestHost + '/pr/goAudityRecord/selectByCondition',
        data: {
          audityId: This.data.curId,
          isPassed: 'NO'
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

    }

  },
  //删除上传的图片
  delImg: function(e) {
    let This = this;
    let array = [];
    for (let i = 0; i < This.data.chooseImgArr.length; i++) {
      if (This.data.chooseImgArr[i] == e.currentTarget.dataset.linksrc) {
      } else {
        array.push(This.data.chooseImgArr[i])
      }
    }
    let fileTypeArr = This.getFileTypeArr(array)
    This.setData({
      chooseImgArr: array,
      fileTypeArr: fileTypeArr
    });
  },
  //输入姓名
  bindSetName(e) {
    let This = this;
    This.setData({
      name: e.detail.value
    });
  },
  //信息标题
  bindSetTitle(e) {
    let This = this;
    This.setData({
      title: e.detail.value
    });
  },
  //信息内容
  bindSetContent(e) {
    let This = this;
    let h = (Math.ceil(e.detail.value.length / 30)+2)*60
    This.setData({
      content: e.detail.value,
      height: h
    });
  },
  //选择图片
  chooseImg() {
    let that = this;
    app.globalData.uploadImg = true
    console.log(that.data.chooseImgArr)
    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success(res) {
        //
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
            var data = JSON.parse(res.data);
            if (data.httpCode == 200) {
              let arr = that.data.chooseImgArr
              console.log(that.data.chooseImgArr)
              arr.push(data.files[0])
              //统一改为以'/'开头
              for (let i = 0; i < arr.length; i++) {
                if (arr[i].indexOf('/') == 0) {
                } else {
                  arr[i] = '/' + arr[i]
                }
              }
              let fileTypeArr = that.getFileTypeArr(arr)
              that.setData({
                chooseImgArr: arr,
                fileTypeArr: fileTypeArr
              })
            } else {
              app.msgModal("提示消息：", "上传图片出错，请稍后重试！");
            }
            
          }
        })
      },
    })
  },
  openFile(e) {
    let that = this;
    let fileUrl = e.currentTarget.dataset.linksrc;
    let fileType = this.getFileType(fileUrl)
    if (fileType == 'png' || fileType == 'jpg' || fileType == 'jpeg' || fileType == 'bmp' || fileType == 'gif') {
      let fielUrlArr = []
      //
      app.globalData.uploadImg = true
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

          var Path = res.tempFilePath              //返回的文件临时地址，用于后面打开本地预览所用
          wx.openDocument({
            filePath: Path,
            success: function (res) {
              that.cancelLoading()
              app.globalData.uploadImg = true
              console.log('打开成功');
            }
          })
        },
        fail: function (res) {
          console.log(res);
        }
      })
    }
  },
  getFileTypeArr: function (arr) {
    let fileTypeArr = []
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
  //提交
  sendData() {
    let This = this;
    if (!This.data.name) {
      wx.showModal({
        title: '提示',
        content: '请输入姓名',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.title) {
      wx.showModal({
        title: '提示',
        content: '请输入标题',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.content) {
      wx.showModal({
        title: '提示',
        content: '请输入文字或链接地址',
        showCancel: false,
        success(res) {}
      });
      return false;
    }

    wx.showLoading({
      title: '提交中',
    })
    let audity = {
      userId: This.data.user_id,
      userName: This.data.name,
      audityTitle: This.data.title,
      audityLink: This.data.content,
      audityPicture: This.data.chooseImgArr.join(','),
      audityStatus: 'COMMITED'
    };

    let c_url = app.helperRequestHost + '/pr/goAudity/add'
    if (This.data.curId) {
      audity.id = This.data.curId
      audity.audityStatus = 'RECOMMITED'
      c_url = app.helperRequestHost + '/pr/goAudity/update'
    }

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
          // wx.redirectTo({
          //   url: '/pages/index/index'
          // })
          setTimeout(function() {
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

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    let This = this
    //从上传图片返回时onShow不做处理
    console.log('onShow')
    if (app.globalData.uploadImg){
      console.log('从上传图片返回时onShow不做处理')
      app.globalData.uploadImg = false
      return
    }
    if (This.data.curId) {
      wx.request({
        url: app.helperRequestHost + '/pr/goAudity/read/detail',
        data: {
          id: This.data.curId
        },
        method: 'POST',
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success(res) {
          let arr=[]
          let fileTypeArr = []
          if (res.data.data.audityPicture!=null && res.data.data.audityPicture!=''){
            arr = res.data.data.audityPicture.split(',')
            //统一改为以'/'开头
            for (let i = 0; i < arr.length; i++) {
              if (arr[i].indexOf('/') == 0) {
              } else {
                arr[i] = '/' + arr[i]
              }
              fileTypeArr.push(This.getFileType(arr[i]))
            }
          }

          This.setData({
            title: res.data.data.audityTitle,
            content: res.data.data.audityLink,
            chooseImgArr:arr,
            fileTypeArr: fileTypeArr,
            //imgPath: res.data.data.audityPicture,
            //imgURL: app.helperRequestHost + res.data.data.audityPicture
          })
          console.log(This.data)
          wx.hideLoading();
        }
      })
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