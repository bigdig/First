const app = getApp();

//var pageNum = 1;
var pageSize = 15;
Page({
  /**
   * 页面的初始数据
   */
  data: {
    //seachStateText: '',
    inputVal: '',
    custStatusId: '',
    custLists: [],
    custdata: [],
    groupId:'',
    companyMap: {},
    hidden: true,
    scrollTop: 0,
    scrollHeight: 0,
    custId:[],   //用于装选中的客户id;
    group:null,    //用于判断是否是新建分组跳转过来的
    disableSubmit : false,
    disableConfm  : false,
    actCustomersFlag : false , //个人活动选择对应的客户
    cooperateCustomerLimit : null , //协作活动限制人数
    cooActAddCustFlag : false , // 协作活动添加客户标志位
    cooActId : null , //协作活动id
    pageNum : 1,
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    if (options.groupId){
      this.setData({
        groupId: options.groupId,
      })
    }
    if (options.inputVal){
      this.setData({
        inputVal: options.inputVal,
      })
    }
    //协作活动，销售为客户报名
    if(options.cooActAddCustFlag){
      this.setData({
        cooActId : options.actId,
        cooActAddCustFlag : options.cooActAddCustFlag,
      })
    }
    if(options.cooperateCustomerLimit){
      this.setData({
        cooperateCustomerLimit : options.cooperateCustomerLimit,
      })
    }
    if(options.actCustomersFlag){
      this.setData({
        actCustomersFlag : options.actCustomersFlag,
      })
    }
    if (options.group != undefined){
      this.setData({
        group: options.group,
      })
    }
    this.setData({
        custId: app.globalData.custId_customer
    })
    
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          scrollHeight: res.windowHeight
        });
      }
    });
    // this.listTraversal();
  },
  inputBlur:function(e){
    this.setData({
      inputVal: e.detail.value,
      custLists:[],
      companyMap: {},
      pageNum: 1,
    })
    this.listTraversal();
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    //app.globalData.custId_customer = [];
    var inputVal = this.data.inputVal;
    if (inputVal != null) {
      this.setData({
        inputVal: inputVal,
      })
    };
    this.listTraversal();
  },
  //数据请求
  listTraversal: function () {
    var that = this;
    that.setData({
      hidden: false
    });
    wx.request({
      url: app.crmRequestHost + '/ty/tyOrgcustomer/read/list',
      method: 'POST',
      data: {
        custStatus: that.data.custStatusId,
        keyword: that.data.inputVal,
        pageNum: that.data.pageNum,
        pageSize: pageSize,
        groupId: that.data.groupId,
        orderBy: 'org_id asc ,id desc',
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success: function (res) {
        if (res.data.httpCode == 200) {
          var lists = res.data.data.list;
          var companyMap = that.data.companyMap;
          //判断当前是否是从关键字搜索
//        if (that.data.inputVal != '') {
//          var inputdata = that.data.custLists;
//          that.deweighting(lists, inputdata);
//          that.listProcess(lists, inputdata, companyMap)
//          that.setData({
//            custLists: inputdata,
//            custdata: [],
//            groupData: [],
//            hidden: true,
//          })
//        } else if (that.data.groupId != '') {    //是否是从分组过来
//          var groupData = that.data.custLists;
//          that.deweighting(lists, groupData);
//          that.listProcess(lists, groupData, companyMap);
//          that.setData({
//            custLists: groupData,
//            inputdata: [],
//            custdata: [],
//            hidden: true,
//          })
//        } else {
//          var custdata = that.data.custLists;
//          that.deweighting(lists, custdata);
//          that.listProcess(lists, custdata, companyMap)
//          that.setData({
//            custLists: custdata,
//            inputdata: [],
//            groupData: [],
//            hidden: true,
//          })
//        }
            var custdata = that.data.custLists;
            //that.deweighting(lists, custdata);
            that.listProcess(lists, custdata, companyMap)
            that.setData({
              custLists: custdata,
              hidden: true,
            })
          
          console.log(that.data.custLists);
          that.data.pageNum++;
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
  listProcess: function (dataList, custList, map) {
    console.log(dataList)
    console.log(custList)
    for (var i = 0; i < dataList.length; i++) {
      if (map.hasOwnProperty(dataList[i].orgName)) {
        dataList[i].showOrgname = false;
        dataList[i].checked = false;
        custList.push(dataList[i]);
      } else {
        dataList[i].showOrgname = true;
        dataList[i].checked = false;
        map[dataList[i].orgName] = false;
        custList.push(dataList[i]);
      }
    }
    var selectedCust = this.data.custId;
	  for (var i = 0; i < custList.length; i++) {
	    custList[i].checked = false;
      for (var k = 0; k < selectedCust.length; k++) {
        if (selectedCust[k].id == custList[i].id) {
	        custList[i].checked = true;
	        break;
	      }
	    }
	  }
    console.log(dataList)
    console.log(custList)
    return custList;
  },
  submit: function () {
    this.data.disableSubmit = true;
    this.setData({
      disableSubmit : this.data.disableSubmit,
    })
    //app.globalData.custId_group = this.data.custId;     //存储选中数据到新建分组
    app.globalData.custId_customer = this.data.custId;    //存储选中数据到短信模板
    var group = this.data.group;
    var custId_group = JSON.stringify(this.data.custId);
    //var custId_customer = JSON.stringify(this.data.custId);
    if (group !=null){
      wx.navigateTo({
        url: '../newGroup/newGroup?custId_group=' + custId_group,    //跳转到新建分组页面
      })
    }else{
      wx.navigateTo({
        url: '../sendMessages/sendMessages',  //跳转到短信模板页面
      })
    }
  },
  //页面滑动到底部
  bindDownLoad: function () {
    var that = this;
    this.listTraversal();
  },
  // scroll: function (event) {
  //   //该方法绑定了页面滚动时的事件，我这里记录了当前的position.y的值,为了请求数据之后把页面定位到这里来。
  //   this.setData({
  //     scrollTop: event.detail.scrollTop
  //   });
  // },
  //全选
  selectChange:function(e){
    console.log(e)
    var obj = e.currentTarget.dataset;
    var lists = this.data.custLists;
    var custId = this.data.custId;
    var companyMap = this.data.companyMap;
    companyMap[obj.orgname] = !obj.checked;
    this.setData({ companyMap: companyMap });
    if (obj.checked){   //判断当前机构是否选中
      for (var i = 0; i < lists.length;i++){
        if (lists[i].orgName == obj.orgname) {    //选中机构名称和data数据里面的名称相同
          lists[i].checked = false;
          for (var k = 0; k < custId.length; k++) {  //遍历选中custId数组中的所有数据
            if (lists[i].id == custId[k].id) { //判断data数据里面的每条数据ID是否和选中custId数组中的每条数据ID相等情况下,应在custId数组中书按掉该条数据;
              custId.splice(k--, 1)
            }
          }
        }
      }
      this.setData({
        custId: custId,
        custLists: lists,
      })
    }else{
      var custs= [];
      for (var k = 0; k < custId.length; k++) {
        custs.push(custId[k].id);
      }
      for (var i = 0; i < lists.length; i++) {
        if (lists[i].orgName == obj.orgname) {    //选中机构名称和data数据里面的名称相同
          lists[i].checked = true;
          custId.push(lists[i]);
          if (custs.indexOf(lists[i].id) != -1){
            custId.splice(i, 1)
          }
        }  
        this.setData({
          custId: custId,
          custLists: lists,
        });
      }
    }
    console.log(this.data.custLists)
  },
  //单选
  checkboxChange:function(e){
      console.log("选择一个客户：：",e)
      var obj = e.currentTarget.dataset;
      var lists = this.data.custLists;
      var custId = this.data.custId;
      if (obj.checkbox){
        for (var i = 0; i < lists.length; i++) {
          if (lists[i].id == obj.id) {
            lists[i].checked = false;
            break;
          }
        }
        for (var k = 0; k < custId.length; k++) {
          if (custId[k].id == obj.id){
            custId.splice(k--, 1);
            break;
          }
        }
        this.setData({
          custId: custId,
          custLists: lists,
        })
      }else{
        for (var i = 0; i < lists.length;i++){
          if (lists[i].id == obj.id){
            lists[i].checked = true;
            custId.push(lists[i]);
            break;
          }
        }
        this.setData({
          custId: custId,
          custLists: lists,
        })
      }
      console.log(this.data.custLists)
      console.log(this.data.custId)
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.setData({ disableSubmit:false});
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
  //我的活动添加客户，回带数据
  cfmSelCustomers : function(){
     //协作活动客户报名（人数有限制）
     if(this.data.cooperateCustomerLimit ){
       if(this.data.custId.length == 0){
        app.msgModal("提示消息：","您未选择客户，请选择！");
        return ;
       }
       if(this.data.custId.length > this.data.cooperateCustomerLimit){
          app.msgModal("提示消息：","选择的人数超过了限定人数，请重新选择！");
          return ;
        }
     } 
    var that =this;
    this.data.disableConfm = true;
    this.setData({
      disableConfm : this.data.disableConfm,
      custId : this.data.custId,
    })
    //添加客户
    this.addCustForCoopAct(this.data.custId );
  },
    //协作活动添加对应的客户
    addCustForCoopAct : function(list){
      var param = {};
      param.activityId  = this.data.cooActId;
      // param.custName = cust.custName;
      // param.orgSimpleName = cust.orgName;
      // param.orgName = cust.orgName;
      param.custIds = this.getCustomerIds(list);
      // param.orgId = cust.orgId;
      // param.custMobile = cust.custMobile;
      // param.inWhitelist = "";
      param.signId = app.globalData.userInfo.id;
      param.signName = app.globalData.userInfo.staffName;
      // param.signStatus = '0';
      var that = this;
      if(app.globalData.cookieInfo && this.data.cooActAddCustFlag){ //已登录
        console.log(app.globalData.cookieInfo)
          //添加协作活动客户
          wx.request({
            url: app.crmRequestHost + '/ty/tyActivitysign/add',
            method: 'POST',
            data: param,
            header: {
              'content-type': 'application/x-www-form-urlencoded', // 默认值
              'cookie': app.globalData.cookieInfo
            },
            success: function (res) {
              console.log("添加协作活动返回的res：", res)
              //成功跳转
              if(res.data.httpCode == 200){
                var arr = getCurrentPages();
                wx.navigateBack({
                  delta: 1,
                  success: function () {
                    arr[arr.length -2].getActInfo();
                    // arr[arr.length - 2].setData({
                    //   selCustomers: list,
                    //   addActSignSuccess : true,
                    // })
                  }
                })
              }else if(res.data.httpCode == 401){
                wx.redirectTo({
                  url: './../login/login'
                })
              }else{
                app.msgModal("提示消息：", res.data.msg);
              }
            },
            fail : function (res){
              app.msgModal("提示消息：", res.data.msg);
            }
          })
      }else{
        //我的活动跳转
        var arr = getCurrentPages();
        wx.navigateBack({
          delta: 1,
          success: function () {
            arr[arr.length - 2].setData({
              selCustomers: list,
            })
          }
        })
      }
    },
    getCustomerIds : function(list){
      var custIds = [];
      for(var index in  list){
        custIds.push(list[index].id);
      }
      return custIds;
    },
})