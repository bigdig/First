//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    vote_group: {
      vote_group_title: '公司内部职员基本调查',
      end_time: '2018-10-21',
      votes: [{
          vote_name: '你认为公司目前的工作环境？',
          vote_items: [{
              vote_item_name: '很好',
            },
            {
              vote_item_name: '一般',
            }
          ]
        },

        {
          vote_name: '你认为公司的主要优势是什么？',
          vote_items: [{
              vote_item_name: '技术',
            },
            {
              vote_item_name: '市场',
            }
          ]
        },

        {
          vote_name: '除薪酬外，你最看重：',
          vote_items: [{
              vote_item_name: '提高自己能力的机会',
            },
            {
              vote_item_name: '好的工作环境',
            },
            {
              vote_item_name: '工作的成就感',
            },
            {
              vote_item_name: '给力的团队伙伴和领导',
            }
          ]
        },

      ]
    },
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function() {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  getUserInfo: function(e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  }
})