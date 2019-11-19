const app = getApp();
//var pageNum = 1;
var pageSize = 15;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    seachStateText:null,
    inputVal:'',
    custStatusId :'',
    inputdata:[],
    custLists: [],
    groupData:[],
    custdata:[],
    groupId:'',
    hidden: true,
    scrollTop : 0,
    scrollHeight:0,
    companyMap:{},
    pageNum:1,
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    app.globalData.storageLabel = [];
    console.log(options);
    if (options != null){
      for (var k in options) {
        if (k == 'custStatusId'){
          console.log(options)
          if (options[k] == '3'){
            wx.setNavigationBarTitle({
              title: '白名单客户',
            })
          }else{
            wx.setNavigationBarTitle({
              title: '潜在客户',
            })
          }
          this.setData({
            custStatusId: options[k],
          })
        }  
        if (k == 'groupId'){
          console.log(options)
          wx.setNavigationBarTitle({
            title: options.custGroupname,
          })
          this.setData({
            groupId: options[k],
          })
        }
        if (k = 'seachStateText'){
          console.log(options)
          this.setData({
            seachStateText: options[k],
          })
        }
      }
    }
    var that = this;
    wx.getSystemInfo({
      success:function(res) {
        that.setData({
          scrollHeight:res.windowHeight
        });
      }
    });
    // this.listTraversal();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var seachStateText = this.data.seachStateText;
    if (seachStateText != null){
      this.setData({
        inputVal: seachStateText,
      })
    };
    this.listTraversal();
  },
  inputBlur :function(e){
    this.setData({
      inputVal: e.detail.value,
      companyMap: {},
      custLists: [],
      pageNum:1,
    })
    this.listTraversal();
  },
  //数据请求
  listTraversal:function(){
    var that = this;
    that.setData({
      hidden:false
    });
    //传参组对象
    var obj = {};
    if (that.data.custStatusId=='3'){
      obj.custStatus = that.data.custStatusId;
    } else if (that.data.custStatusId == '2'){//排除已经签约的
      obj.noSigncontract='1'
    }
    obj.groupId = that.data.groupId;
    obj.keyword = that.data.inputVal;
    obj.pageNum = that.data.pageNum;
    obj.pageSize = pageSize;
    obj.orderBy = 'org_id asc ,id desc';
    wx.request({
      url: app.crmRequestHost + '/ty/tyOrgcustomer/read/list',
      method: 'POST',
      data: obj,
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var lists = res.data.data.list;
          var companyMap = that.data.companyMap;
          //判断当前是否是从关键字搜索
          if (that.data.inputVal != ''){
            var inputdata = that.data.custLists;
            that.listProcess(lists, inputdata, companyMap)
            that.setData({
              custLists: inputdata,
              custdata:[],
              groupData:[],
              hidden: true,
            })
          } else if (that.data.groupId != ''){    //是否是从分组过来
            var groupData = that.data.custLists;
            that.deweighting(lists, groupData);
            that.listProcess(lists, groupData, companyMap);
            that.setData({
              custLists: groupData,
              inputdata: [],
              custdata: [],
              hidden: true,
            })
          }else{
            var custdata = that.data.custLists;
            that.listProcess(lists, custdata, companyMap)
            that.setData({
              custLists: custdata,
              inputdata:[],
              groupData: [],
              hidden: true,
            })
          }
          console.log(that.data.custLists);
          that.data.pageNum ++;
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
  },
  //数据处理方法
  listProcess: function (oldList, newList, map){
    for (var i = 0; i < oldList.length; i++) {
      if (map.hasOwnProperty(oldList[i].orgName)) {
        oldList[i].showOrgname = false;
        newList.push(oldList[i]);
      } else {
        oldList[i].showOrgname = true;
        map[oldList[i].orgName] = 1;
        newList.push(oldList[i]);
      }
    }
    return newList;
  },
  //数组去重
  deweighting: function (oldList, newList){
    if (newList.length > 0){
      for (var i = 0; i < newList.length; i++) {
        for (var j = 0; j < oldList.length; j++){
          if (oldList[j].id == newList[i].id){
            oldList.splice(j--, 1)
           }
        }
      }
    }
    return oldList;
  },
  submit:function(){
    app.globalData.custId_customer = [];
    wx.navigateTo({
      url: '../selectCustomer/selectCustomer?groupId=' + this.data.groupId + '&inputVal=' + this.data.inputVal,
    })
  },
  //页面滑动到底部
  bindDownLoad: function () {
    var that = this;
    this.listTraversal();
  },
  // scroll:function(event) {
  //   //该方法绑定了页面滚动时的事件，我这里记录了当前的position.y的值,为了请求数据之后把页面定位到这里来。
  //   this.setData({
  //     scrollTop : event.detail.scrollTop
  //   });
  // },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    
  },
})