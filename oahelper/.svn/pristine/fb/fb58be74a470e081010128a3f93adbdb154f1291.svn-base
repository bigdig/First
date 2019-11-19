// pages/15toupiao_detail/index.js

const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userId: '',
    voteGroupId: '',
    is_activity: 'NO',
    is_ended: false,
    is_open: '',
    myVote: [],
    voteGroup: {},
    votes: [],
    voteItems: [],

    cursendId: '', // 当前投票ID，用于用户提交信息时绑定的参与投票ID
    title: '', //标题
    datetime: '', //时间

    questionArr: [] //题库
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let This = this;
    // This.data.userId = app.globalData.userInfo.id;
    if (options.voteGroupId) {
      This.setData({
        voteGroupId: options.voteGroupId,
        userId: app.globalData.userInfo.id
      });
    }

    wx.showLoading({
      title: '数据加载中',
    })

    var that = this;
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goVoteGroup/selectUserVote?id=' + This.data.voteGroupId + '&userId=' + This.data.userId,
        method: 'GET',
        data: {},
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function(res) {

          if (res.data.httpCode == 200) {
            let data = res.data.data;
            //绑定列表数据
            This.setData({
              myVote: data.myVote,
              voteGroup: data.voteGroup,
              votes: data.votes,
              voteItems: data.voteItems,
              title: data.voteGroup.voteGroupName,
              datetime: app.setDateTime(data.voteGroup.endTime),
              is_open: data.voteGroup.isOpen
            });

            //处理时间（去掉时分秒）
            var e_time = new Date(This.data.voteGroup.endTime).getTime()
            var now_time = new Date().getTime()
            if (e_time < now_time) {
              This.setData({
                is_ended: true
              })
            } else {
              This.setData({
                is_ended: false
              })
            }

            This.initListData();

            // console.log(This.data)
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

  //初始化题库
  initListData() {
    let This = this;
    let aa = [];
    for (var item in This.data.votes) {
      let vote = This.data.votes[item]
      vote.question = vote.voteName
      vote.questiontype = vote.isMulti === 'YES' ? 2 : 1
      vote.answer = []
      vote.choosearr = {}
      vote.choosekey = ''

      for (var item2 in This.data.voteItems) {
        let voteItem = This.data.voteItems[item2]
        if (voteItem.voteId == vote.id) {
          let answerItem = {}
          answerItem.id = voteItem.id
          answerItem.content = voteItem.voteItemContent
          vote.answer.push(answerItem)
        }
      }
      if (vote.isCustomizable === 'YES') {
        vote.answer.push({
          id: '',
          content: '其他'
        })
      }

      This.gearVote(vote)

      aa.push(vote);
    }

    This.setData({
      questionArr: aa, //题库
    });
    // console.log(This.data.questionArr)
  },

  //用户已投票，则显示用户投票信息
  gearVote(vote) {
    let This = this
    let myVote = This.data.myVote
    for (var item in myVote) {
      if (myVote[item].voteId == vote.id) {
        if (myVote[item].voteItemId === '' || myVote[item].voteItemId == null) {
          vote.content = myVote[item].voteItemContent
        }
        let answer = vote.answer
        for (var item2 in answer) {
          if (answer[item2].content === '其他' && (myVote[item].voteItemId === '' || myVote[item].voteItemId == null)) {
            vote.choosearr[item2] = true
            vote.choosekey = vote.choosekey == '' ? item2 : vote.choosekey + ',' + item2
          } else if (myVote[item].voteItemId == answer[item2].id) {
            // console.log(answer[item2])
            vote.choosearr[item2] = true
            vote.choosekey = vote.choosekey == '' ? item2 : vote.choosekey + ',' + item2
          }
        }
      }
    }
  },

  //处理获取的或时时修改的（选题时）数据，绑定page中data 的题库
  manageListData(resquestionArr) {
    let This = this;

    let aa = [];
    for (var item in resquestionArr) {
      for (var item2 in resquestionArr[item].answer) {
        if (resquestionArr[item].choosekey.toString().indexOf(item2.toString()) != -1) {
          resquestionArr[item].choosearr[item2] = true;
        } else {
          resquestionArr[item].choosearr[item2] = false;
        }
      }
      aa.push(resquestionArr[item]);
    }

    This.setData({
      questionArr: aa, //题库
    });
  },

  //选择
  choose_type(e) {
    let This = this;

    if (This.data.myVote[0]) {
      return
    }

    let qindex = e.currentTarget.dataset.qindex;
    let chooseindex = e.currentTarget.dataset.choose_type;

    let newdata = This.data.questionArr;
    if (newdata[qindex].questiontype == 2) { //多选
      if (newdata[qindex].choosekey) {
        let keyindex = newdata[qindex].choosekey.indexOf(chooseindex);
        if (keyindex != -1) { //取消选择
          //console.log(keyindex, newdata[qindex].choosekey);
          if (keyindex > 0) { //取消 非 第1个选 择的选项
            newdata[qindex].choosekey = newdata[qindex].choosekey.replace(',' + chooseindex, '');
          } else if (newdata[qindex].choosekey.length > 1) { //取消 第1个 选择的选项
            newdata[qindex].choosekey = newdata[qindex].choosekey.replace(chooseindex + ',', '');
          } else { //取消 唯一 1个 选择的选项
            //newdata[qindex].choosekey = '';
          }
        } else { //本题选择 非 第1个选项时
          newdata[qindex].choosekey = newdata[qindex].choosekey + ',' + chooseindex
        }
      } else { //本题选择 第1个选项时
        newdata[qindex].choosekey = '' + chooseindex;
      }
    } else { //单选
      //if (newdata[qindex].choosekey == chooseindex){ //取消选择
      //  newdata[qindex].choosekey = '';
      //} else { //选择
      newdata[qindex].choosekey = '' + chooseindex;
      //}
    }
    //更新并绑定题库
    This.manageListData(newdata);
  },
  //选择输入框时取消冒泡所用
  kong() {},




  //其他选择时的输入简述内容
  bindSetContent(e) {
    let This = this;
    let val = e.detail.value;
    let qindex = e.currentTarget.dataset.qindex;
    let chooseindex = e.currentTarget.dataset.choose_type;

    let newdata = This.data.questionArr;
    newdata[qindex].content = val;
    //更新并绑定题库
    This.manageListData(newdata);
  },

  //提交选择
  sendData() {
    let This = this;

    let aa = [];
    let newdata = This.data.questionArr;
    let choosekey;
    let content;
    let canSend = true;
    for (var item in newdata) {
      choosekey = newdata[item].choosekey;
      if (choosekey.toString() == '') {
        wx.showModal({
          title: '提示',
          content: '请完成所有选择后再提交',
          showCancel: false,
          success(res) {}
        });
        canSend = false;
        break;
      }
      if (!canSend) {
        return false;
      }
      content = newdata[item].content;
      for (var item2 in newdata[item].choosearr) {
        console.log();
        if (newdata[item].choosearr[item2] && newdata[item].answer[item2] == '其他' && !content) {
          wx.showModal({
            title: '提示',
            content: '请填写您选择的其他的简述内容',
            showCancel: false,
            success(res) {}
          });
          canSend = false;
          break;
        }
      }
      if (!canSend) {
        break;
        return false;
      }
    }
    if (!canSend) {
      return false;
    }

    //提交的信息 json
    let sendData = []; //注：如果需要全部的数据则可：let sendData = newdata; 以下for循环处理可以省去
    //处理只留下要上传的数据
    for (var item in newdata) {
      for (var item2 in newdata[item].choosearr) {
        if (newdata[item].choosearr[item2] == true) { //如果被选中
          let vote_answer = {}
          vote_answer.voteId = newdata[item].id
          vote_answer.voteItemId = newdata[item].answer[item2].id
          vote_answer.voteItemContent = newdata[item].answer[item2].content

          vote_answer.userId = app.globalData.userInfo.id
          vote_answer.userName = app.globalData.userInfo.userName
          if (vote_answer.voteItemContent === '其他') {
            vote_answer.voteItemContent = newdata[item].content
          }
          sendData.push(vote_answer)
        }
      }

    }
    console.log(sendData)
    wx.showLoading({
      title: '提交中',
    })
    var that = this;
    // console.log(app.globalData.cookieInfo);
    if (app.globalData.cookieInfo) {
      wx.request({
        url: app.helperRequestHost + '/pr/goVoteUser/addAll',
        method: 'POST',
        data: {
          data: JSON.stringify(sendData)
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function(res) {
          // console.log("查询活动详情返回的res：", res)
          if (res.data.httpCode == 200) {
            wx.hideLoading();
            wx.showToast({
              title: '提交成功',
              icon: 'success',
              duration: 2000
            })
            setTimeout(function() {
              This.onLoad({
                voteGroupId: This.data.voteGroupId
              })
            }, 2000)
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