// pages/17wenjian_list/index.js
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    page_type: '', //进入页面方式（菜单进入则是 菜单对应类型、搜索进入则 为空）
    page_title: '', //列表页进入时，设置页面上方标题
    search_text: '', //搜索关键词
    if_search: false, //是否搜索状态

    pageSize: 10,
    listData: [],
    cur_page: 1, //当前页码
    ifHaveMore: false, //是否还有下一页

    host: app.helperRequestHost,
    myFavor: [], //我的收藏信息

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let This = this;
    if (options.page_title) {
      This.setData({
        page_type: options.page_type, //通过 菜单 过来的
        page_title: options.page_title
      });
    }
    if (options.search_text) {
      This.setData({
        if_search: true,
        search_text: options.search_text //通过 搜索 过来的
      });
    }
    //设置标题文字
    if (This.data.page_title) {
      wx.setNavigationBarTitle({
        title: This.data.page_title + '文件模板'
      })
    } else {
      wx.setNavigationBarTitle({
        title: '文件模板'
      })
    }

    //加载我的收藏
    var that = this;
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goUserFavorite/selectByUser',
        method: 'POST',
        data: {
          favoriteType: 'FILE'
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          if (res.data.httpCode == 200) {
            let data = res.data.data;
            //绑定列表数据
            that.setData({
              myFavor: data
            })
          } else if (res.data.httpCode == 401) {
            wx.redirectTo({
              url: './../login/login'
            })
          } else {
            app.msgModal("提示消息：", res.data.msg);
          }
        },
        fail: function (res) {
          app.msgModal("提示消息：", res.data.msg);
        }
      })
    }
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
  //收藏
  shoucang(e) {

    let This = this;
    var that = this;
    let index = e.currentTarget.dataset.index;
    let newData = This.data.listData;
    let c_url = '';
    let params = {}

    if (newData[index].ifsc) {
      c_url = app.helperRequestHost + '/pr/goUserFavorite/delete';
    } else {
      c_url = app.helperRequestHost + '/pr/goUserFavorite/add'
    }

    if (app.globalData.cookieInfo) {
      wx.request({
        url: c_url,
        method: 'POST',
        data: {
          favoriteType: 'FILE',
          favoriteId: newData[index].id
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          if (res.data.httpCode == 200) {
            newData[index].ifsc = !newData[index].ifsc;
            let msg = '';
            if (newData[index].ifsc) {
              msg = '收藏成功'
            } else {
              msg = '取消成功'
            }
            This.setData({
              listData: newData
            });
            wx.showToast({
              title: msg,
              icon: 'success',
              duration: 1000
            })
          } else if (res.data.httpCode == 401) {
            wx.redirectTo({
              url: './../login/login'
            })
          } else {
            app.msgModal("提示消息：", res.data.msg);
          }
        },
        fail: function (res) {
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
            console.log(res.data) // data
          }
        })
      }
    })
  },

openFile(e){
  let that = this;
  let fileUrl = e.currentTarget.dataset.linksrc;
  let fileType = "";
  var startIndex = fileUrl.lastIndexOf(".");
  if (startIndex != -1){
    fileType = fileUrl.substring(startIndex + 1, fileUrl.length).toLowerCase();
  }
  if (fileType == 'jpg' || fileType == 'png' || fileType == 'gif' || fileType == 'jpeg' || fileType == 'bmp'){
    let fielUrlArr = []
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
    //从类别列表 搜索 进入的，取消搜索再回到前一页
    if (!This.data.page_title) {
      //wx.redirectTo({
      //  url: '/pages/16wenjian/index'
      //})

      wx.navigateBack({
        delta: 1
      })

      return false;
    }
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
    //查询数据 所需
    let dataJson = {
      page_type: This.data.page_type, //所属菜单页类别
      search_text: This.data.search_text, //搜索关键词
      cur_page: This.data.cur_page, //当前页码
    }

    wx.showLoading({
      title: '数据加载中',
    })

    var that = this;
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goFileArchive/read/list',
        method: 'POST',
        data: {
          pageSize: this.data.pageSize,
          pageNum: this.data.cur_page,
          fileCatalog: this.data.page_type,
          fileName: this.data.search_text
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

        res.list[item].ifsc = false
        for (var item2 in This.data.myFavor) {
          // console.log(res.list[item] + '-' + This.data.myFavor[item2])
          if (This.data.myFavor[item2].favoriteId == res.list[item].id) {
            res.list[item].ifsc = true;
            res.list[item].userFavorId = This.data.myFavor[item2].id
            break
          }
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