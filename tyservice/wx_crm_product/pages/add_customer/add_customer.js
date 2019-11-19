const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userName : '',
    custMobile : '',
    custEmail : '',
    orgName : '',
    department : '',
    title : '',
    marketing : '',
    industry : '',
    field :'',
    custTel : '',
    label : '',
    
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var marketing = app.globalData.userInfo.staffName;
    this.setData({
      marketing: marketing,
    })
    console.log(options.customerStr)
      if(options.customerStr && options.customerStr!='') {
          var customer = JSON.parse(options.customerStr);
          this.setData({
              orgName: customer.orgName,
              userName: customer.userName,
              custTel: customer.custTel,
              custMobile: customer.custMobile,
              custEmail: customer.custEmail,
              department: customer.department,
              title: customer.title,
          })
      }
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },
  //提交
  submit : function(){
    var that = this;
    if (this.data.userName == ''){
      app.msgModal("提示消息：", "请填写客户姓名");
    } else if (this.data.custMobile == '') {
      app.msgModal("提示消息：", "请填写手机号码");
    } else if (this.data.custEmail == ''){
      app.msgModal("提示消息：", "请填写邮箱");
    } else if (this.data.orgName == '') {
      app.msgModal("提示消息：", "请填写所属机构");
    } else if (this.data.department == '') {
      app.msgModal("提示消息：", "请填写部门");
    } else if (this.data.title == '') {
      app.msgModal("提示消息：", "请填写职位");
    }else{
      wx.request({
        url: app.crmRequestHost + '/ty/tyOrgcustomer/addSimpleInfo',
        method: 'POST',
        data: {
          custName: that.data.userName,
          custMobile: that.data.custMobile,
          custEmail: that.data.custEmail,
          orgName: that.data.orgName,
          department: that.data.department,
          title: that.data.title,
          serviceSaler: that.data.marketing,
          industry: that.data.industry,
          domain: that.data.field,
          custTel: that.data.custTel,
          mark: that.data.label,
        },
        header: {
          'content-type': 'application/x-www-form-urlencoded', // 默认值
          'cookie': app.globalData.cookieInfo
        },
        success: function (res) {
          if (res.data.httpCode == 200) {
            wx.switchTab({
              url: './../customer/customer'
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
  //姓名
  userName: function(e){
    this.setData({
      userName: e.detail.value
    })
  },
  //手机号
  custMobile: function(e){
    this.setData({
      custMobile: e.detail.value
    })
  },
  //邮箱
  custEmail: function(e){
    this.setData({
      custEmail: e.detail.value
    })
  },
  //所属机构
  orgName: function(e){
    this.setData({
      orgName: e.detail.value
    })
  },
  //部门
  department: function(e){
    this.setData({
      department: e.detail.value
    })
  },
  //职位
  title: function(e){
    this.setData({
      title: e.detail.value
    })
  },
  //对口销售
  // marketing: function(e){
  //   this.setData({
  //     marketing: e.detail.value
  //   })
  // },
  //研究行业
  industry: function(e){
    this.setData({
      industry: e.detail.value
    })
  },
  //关注领域
  field: function(e){
    this.setData({
      field: e.detail.value
    })
  },
  //座机号
  custTel: function(e){
    this.setData({
      custTel: e.detail.value
    })
  },
  //标签
  label: function(e){
    this.setData({
      label: e.detail.value
    })
  },
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
    
  }
})