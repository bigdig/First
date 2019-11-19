//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    vote_group_id: '',
    is_activity: 'NO',
    vote_group: {
      vote_group_title: '公司内部职员基本调查',
      end_time: '2018-10-21',
      votes: [{
          vote_name: '你认为公司目前的工作环境？',
          vote_items: [{
              vote_item_id: 111,
              vote_item_name: '很好',
            },
            {
              vote_item_id: 112,
              vote_item_name: '一般',
            }
          ]
        },

        {
          vote_name: '你认为公司的主要优势是什么？',
          vote_items: [{
              vote_item_id: 121,
              vote_item_name: '技术',
            },
            {
              vote_item_id: 122,
              vote_item_name: '市场',
            }
          ]
        },

        {
          vote_name: '除薪酬外，你最看重：',
          vote_items: [{
              vote_item_id: 131,
              vote_item_name: '提高自己能力的机会',
            },
            {
              vote_item_id: 132,
              vote_item_name: '好的工作环境',
            },
            {
              vote_item_id: 133,
              vote_item_name: '工作的成就感',
            },
            {
              vote_item_id: 134,
              vote_item_name: '给力的团队伙伴和领导',
            }
          ]
        },

      ]
    }
  },

  onLoad: function(options) {
    let This = this
    if (options.id) {
      This.setData({
        vote_group_id: options.id
      });
    }

    if (options.type) {
      if (options.type != 1) {
        wx.setNavigationBarTitle({
          title: '活动详情'
        })
        This.setData({
          is_activity: 'YES',
        });
      }
    }

  },


})