// pages/36wode_shoucang/index.js

const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    search_text: '', //搜索关键词
    if_search: false, //是否搜索状态

    pageSize: 10,
    listData: [],
    cur_page: 1, //当前页码
    ifHaveMore: false, //是否还有下一页
    host: app.helperRequestHost

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let This = this;

    //加载列表数据
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
  //复制链接

  //复制链接
  copyLink(e) {
    //let This = this;
    let linksrc = e.currentTarget.dataset.linksrc;
    wx.setClipboardData({
      data: linksrc,
      success(res) {
        wx.getClipboardData({
          success(res) {
            console.log(res.data) // data
          }
        })
      }
    })
  },

  //取消收藏
  qxshoucang(e) {
    let This = this;

    wx.showModal({
      title: '友情提示',
      content: '您确定要取消此收藏吗？',
      success(res) {
        if (res.confirm) {
          let index = e.currentTarget.dataset.index;
          console.log(This.data.listData[index].id);

          if (app.globalData.cookieInfo) {
            wx.request({
              url: app.helperRequestHost + '/pr/goUserFavorite/delete',
              method: 'POST',
              data: {
                favoriteType: This.data.listData[index].favoriteType,
                favoriteId: This.data.listData[index].favoriteId
              },
              header: {
                'content-type': 'application/x-www-form-urlencoded', // 默认值
                'cookie': app.globalData.cookieInfo
              },
              success: function(res) {

                if (res.data.httpCode == 200) {

                  let newData = This.data.listData;
                  newData.splice(index, 1);
                  This.setData({
                    listData: newData
                  });
                  //当前页面收藏的内容全部取消，则刷新页面调取最新数据
                  if (!This.data.listData[0]) {
                    setTimeout(function() {
                      This.onLoad();
                    }, 300)
                  }


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

        } else if (res.cancel) {
          console.log('用户点击取消');
        }
      }
    })

  },

  openFile(e) {
    let that = this;
    let fileUrl = e.currentTarget.dataset.linksrc;
    let fileType = "";
    var startIndex = fileUrl.lastIndexOf(".");
    if (startIndex != -1) {
      fileType = fileUrl.substring(startIndex + 1, fileUrl.length).toLowerCase();
    }
    if (fileType == 'jpg' || fileType == 'png' || fileType == 'gif') {
      let fielUrlArr = []
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
  showLoading: function () {
    wx.showToast({
      title: '加载中',
      icon: 'loading'
    });
  },
  cancelLoading: function () {
    wx.hideToast();
  },

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
        success(res) {}
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


  //获取当前页数据
  getData: function() {
    let This = this;
    var that = this;
    //查询数据 所需
    let dataJson = {
      search_text: This.data.search_text, //搜索关键词
      cur_page: This.data.cur_page, //当前页码
    }
    wx.showLoading({
      title: '数据加载中',
    })

    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goUserFavorite/read/list',
        method: 'POST',
        data: {
          userId: app.globalData.userInfo.id,
          pageSize: this.data.pageSize,
          pageNum: this.data.cur_page,
          favoriteTitle: This.data.search_text
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function(res) {

          if (res.data.httpCode == 200) {
            let data = res.data.data;
            //绑定列表数据
            This.setPageList(data);
            that.setData({
              ifHaveMore: res.data.data.hasNextPage
            })
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

    return;
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
        res.list[item].favoriteTime = app.setDateTime(res.list[item].favoriteTime);
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