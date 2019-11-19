// pages/8mingpianshenqing/index.js
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    user_id: '',
    user_name: '',
    name: '',
    en_name: '',
    bumen: '',
    en_bumen: '',
    hangye: [],
    en_hangye: [],
    zhiwu: [],
    en_zhiwu: [],

    phone: '',
    tel: '',
    fax: [], //传真数组
    email: '',
    address: [],

    imgURL: '',

    choose_img_show: '', //微信二维码
    cardNumber: [], // 名片数量
    index: 0, // 名片数量 所选择的 key
    bumenIndex:0,
    hangyeIndex:0,
    hangyeText:'',
    en_hangyeText:'',
    zhiwuIndex:0,
    zhiwuText:'',
    en_zhiwuText:'',
    faxIndex:0,
    addressIndex:0,
    cardNumberIndex:0,
    note: '',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    let This = this;
    /*test start*/
    /*查询数据库后给变量赋值，这样配置页面数据*/
    This.setData({
      bumen: ['研究所'],
      en_bumen: ['Research Dept'],
      cardNumber: [ '2', '4', '5', '6',  '8', '10'], // 名片数量
      fax: ['021-50155671', '021-61069806', '0755-82571995','无'],
      //[其他]有特殊控制，一定要放最后一个
      zhiwu: ['研究所所长', '首席分析师', '资深分析师', '高级分析师', '分析师', '助理分析师','研究助理', ' 销售总监', ' 销售副总监', ' 高级销售经理', ' 销售经理', '机构销售助理', ' 董事总经理', '合伙人','其他'],
      en_zhiwu: ['Director of the Research Dept', 'Chief Analyst', 'Senior Analyst','Advanced Analyst', 'Analyst', 'Associate  Analyst', 'Research Assistant', 'Sales Director', 'Assistant Director of Sales', 'Senior Sales Manager', 'Sales Manager', 'Sales Assistant', 'Managing Director', 'Partner', 'Other'],
      hangye: ['传媒互联网', '商贸及社会服务', '宏观经济', '电子行业', '机械行业', '医药行业', 
        '策略', '石油化工行业', '食品饮料', '海外研究', '家电行业', '银行', '军工', '金融工程', '交通运输', '电力设备与新能源', '煤炭行业', '固定收益', '纺织服装行业', '农林牧渔', '有色金属新材料', '钢铁', '基础化工行业', '汽车行业', '轻工行业', '电力及公用事业行业', '房地产', '非银金融', '通信行业', '计算机行业', '中小市值', '销售交易部', 'FOF研究', '建筑材料行业', '建筑工程', '产品团队', '活动团队', '其他'],
      en_hangye: ['Media and Internet', 'Business and Social Services', 'Macro-economy', 'ElectronicIndustry', 'Machinery Industry', 'Pharmaceutical Industry', 'Investment Strategy', 'Petrochemical  Industry', 'Food and Beverage', 'Overseas Research', 'Home appliance Industry', 'Banking', 'Military Industry', 'Financial Engineering', 'Logistics and Freigh', 'Electric Power Equipment and New Energy Industry', 'Coal Industry', 'Fixed Income', 'Textile and Apparel Industry', 'Agriculture Industry', 'Nonferrous metal and new material', 'Steel Industry', 'Chemical  Industry', 'Automotive Industry', 'Light manufacturing', 'Electric Power Industry and Public Utilities', 'Real Estate', 'non - banking financial institutions', 'TMT / Telecommunication', 'Computer Industry', 'Small and Mid Cap', 'Sales Branch', 'Fund of Funds', 'Construction Materials Industry', 'Construction Engineering', 'Product Team', 'Operations Team', 'Other'],
      address: ['上海市浦东新区兰花路333号333世纪大厦20楼(20/F Century333,333 Lanhua Road Pudong New District,Shanghai)', '上海市浦东新区浦明路1500号万得大厦19楼(19/F Wande,1500 Puming Road Pudong New District,Shanghai)', '北京市西城区佟麟阁路36号（ NO.36, Tonglinge Road, Xicheng District, Beijing, China）', '深圳市福田区益田路5033号平安金融中心71楼（71/F PingAn Financial Center,5033 Yitian Road,Futian District,Shenzhen）']
    });
  
    var that = this;
    that.setData({
      user_id: app.globalData.userInfo.id,
      user_name: app.globalData.userInfo.userName,
      hangyeText: that.data.hangye[that.data.hangyeIndex],
      en_hangyeText: that.data.en_hangye[that.data.hangyeIndex],
      zhiwuText: that.data.zhiwu[that.data.zhiwuIndex],
      en_zhiwuText: that.data.en_zhiwu[that.data.zhiwuIndex],
    })


  },
  //获取、绑定输入的内容
  bindInputVal(e) {
    let This = this;
    if (e.currentTarget.dataset.type == 'name') {
      This.setData({
        name: e.detail.value
      });
    }
    if (e.currentTarget.dataset.type == 'hangyeText') {
      This.setData({
        hangyeText: e.detail.value
      });
    }
    if (e.currentTarget.dataset.type == 'en_hangyeText') {
      This.setData({
        en_hangyeText: e.detail.value
      });
    }
    if (e.currentTarget.dataset.type == 'zhiwuText') {
      This.setData({
        zhiwuText: e.detail.value
      });
    }
    if (e.currentTarget.dataset.type == 'en_zhiwuText') {
      This.setData({
        en_zhiwuText: e.detail.value
      });
    }    
    if (e.currentTarget.dataset.type == 'en_name') {
      This.setData({
        en_name: e.detail.value
      });
    }

    if (e.currentTarget.dataset.type == 'phone') {
      This.setData({
        phone: e.detail.value
      });
    }
    if (e.currentTarget.dataset.type == 'tel') {
      This.setData({
        tel: e.detail.value
      });
    }
    
    if (e.currentTarget.dataset.type == 'email') {
      This.setData({
        email: e.detail.value
      });
    }

    if (e.currentTarget.dataset.type == 'note') {
      This.setData({
        note: e.detail.value
      });
    }
  },
  //选择 名片数量
  bindbumenPickerChange: function(e) {
    let This = this;
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    This.setData({
      bumenIndex: e.detail.value
    })
  },
  bindhangyePickerChange: function (e) {
    let This = this;
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    This.setData({
      hangyeIndex: e.detail.value,
      hangyeText: This.data.hangye[e.detail.value],
      en_hangyeText: This.data.en_hangye[e.detail.value],
    })
  },
  bindzhiwuPickerChange: function (e) {
    let This = this;
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    This.setData({
      zhiwuIndex: e.detail.value,
      zhiwuText: This.data.zhiwu[e.detail.value],
      en_zhiwuText: This.data.en_zhiwu[e.detail.value],
    })
  },
  bindfaxPickerChange: function (e) {
    let This = this;
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    This.setData({
      faxIndex: e.detail.value
    })
  },
  bindaddressPickerChange: function (e) {
    let This = this;
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    This.setData({
      addressIndex: e.detail.value
    })
  },
  bindcardNumberPickerChange: function (e) {
    let This = this;
    //console.log('picker发送选择改变，携带值为', e.detail.value)
    This.setData({
      cardNumberIndex: e.detail.value
    })
  },
  
  //选择图片
  chooseImg() {
    let that = this;
    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success(res) {
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
            app.globalData.uploadImg = true;
            var data = JSON.parse(res.data);
            if (data.httpCode == 200) {
              that.setData({
                choose_img_show: data.files[0],
                imgURL: app.helperRequestHost + '/' + 'uploadfile/previewfile?fileUrl=' + data.files[0],
                imgPath: 'uploadfile/previewfile?fileUrl=' + data.files[0]
              })
            } else {
              app.msgModal("提示消息：", "上传图片出错，请稍后重试！");
            }
          },
        })
      }
    })
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
    if (!This.data.en_name) {
      wx.showModal({
        title: '提示',
        content: '请输入英文名',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.bumen) {
      wx.showModal({
        title: '提示',
        content: '请输入中文部门',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.en_bumen) {
      wx.showModal({
        title: '提示',
        content: '请输入英文部门',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.hangye) {
      wx.showModal({
        title: '提示',
        content: '请输入中文行业',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.en_hangye) {
      wx.showModal({
        title: '提示',
        content: '请输入英文行业',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.zhiwu) {
      wx.showModal({
        title: '提示',
        content: '请输入中文职务',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.en_zhiwu) {
      wx.showModal({
        title: '提示',
        content: '请输入英文职务',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.phone) {
      wx.showModal({
        title: '提示',
        content: '请输入手机号码',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!/^1\d{10}$/.test(This.data.phone)) {
      wx.showModal({
        title: '提示',
        content: '请输入正确的手机号码',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.tel) {
      wx.showModal({
        title: '提示',
        content: '请输入电话',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.fax) {
      wx.showModal({
        title: '提示',
        content: '请输入传真',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.email) {
      wx.showModal({
        title: '提示',
        content: 'Email',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    if (!This.data.address) {
      wx.showModal({
        title: '提示',
        content: '请输入地址',
        showCancel: false,
        success(res) {}
      });
      return false;
    }
    //    if (!This.data.note) {
    //      wx.showModal({
    //        title: '提示',
    //        content: '请输入备注',
    //        showCancel: false,
    //        success(res) {
    //        }
    //      });
    //      return false;
    //    }
    // if (!This.data.choose_img_show) {
    //   wx.showModal({
    //     title: '提示',
    //     content: '请上传微信二维码',
    //     showCancel: false,
    //     success(res) {}
    //   });
    //   return false;
    // }

    wx.showLoading({
      title: '提交中',
    })

    //提交的信息 json
    let sendData = {
      usereId: This.data.user_id,
      userName: This.data.user_name,
      cardName: This.data.name,
      cardNameEn: This.data.en_name,
      cardDepartment: This.data.bumen[This.data.bumenIndex],
      cardDepartmentEn: This.data.en_bumen[This.data.bumenIndex],
      cardProfession: This.data.hangyeText,//This.data.hangye[This.data.hangyeIndex],
      cardProfessionEn: This.data.en_hangyeText,//This.data.en_hangye[This.data.hangyeIndex],
      cardPost: This.data.zhiwuText,//This.data.zhiwu[This.data.zhiwuIndex],
      cardPostEn: This.data.en_zhiwuText,//This.data.en_zhiwu[This.data.zhiwuIndex],
      cardMobile: This.data.phone,
      cardPhone: This.data.tel,
      cardFax: This.data.fax[This.data.faxIndex],
      cardEmail: This.data.email,
      cardAddress: This.data.address[This.data.addressIndex],
      cardQuantity: This.data.cardNumber[This.data.cardNumberIndex],
      cardPicture: This.data.imgPath ? This.data.imgPath:'',
      cardStatus: 'WAITING',
    };
    console.log(sendData);

    //提交数据
    wx.request({
      url: app.helperRequestHost + '/pr/goCardApply/add',
      data: sendData,
      method: 'POST',
      header: {
        'content-type': 'application/x-www-form-urlencoded', // 默认值
        'cookie': app.globalData.cookieInfo
      },
      success(res) {
        if (res.data.httpCode == 200) {
          wx.hideLoading();
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
        }else{
          wx.showToast({
            title: '提交失败',
            icon: 'success',
            duration: 1000
          })
        }        
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