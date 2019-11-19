<style lang="less">
    @import '../../styles/common.less';
    @import '../system/components/table.less';
</style>

<template>
    <div class="audity-bd">

        <Row class="status-bg">
            <Row class="status-text font-24">{{audityItem.audityStatusText}}</Row>
            <Row class="status-text font-18">审核状态</Row>
        </Row>
        <Row class="audity-reject" v-if="audityComment && (audityItem.audityStatus=='COMMITED' || audityItem.audityStatus=='RECOMMITED')">
            上次退回原因:{{audityComment}}
        </Row>
        <hr v-if="audityComment" style="border:5px solid #eeeeee;">
        <Row class="audity-title">自媒体信息</Row>
            <Form  label-position="left" :label-width="80" style="padding-left:15px;">
                <FormItem label="提交人：" style="margin-bottom: 0px">
                    {{audityItem.userName}}
                </FormItem>
                <FormItem label="标题：" style="margin-bottom: 0px">
                    {{audityItem.audityTitle}}
                </FormItem>
                <FormItem label="链接地址：" style="margin-bottom: 0px">
                    <div v-if="audityLinkArr.length>0" v-for="(item,index) in audityLinkArr">
                        <a v-bind:href="item" target="_blank">链接地址{{ index +1}}</a>
                    </div>
                    <div v-if="audityLinkArr.length==0" >
                        <div v-html="audityContent" style="word-wrap:break-word;word-break:break-all"></div>
                    </div>
                </FormItem>
                <FormItem label="图片/文件：" style="margin-bottom: 5px;padding-right: 10px;" v-if="audityPictureArr.length>0">
                    <div v-for="(item,index) in audityPictureArr">
                        <img v-if="validateImg(item)"  style="width: 100%" v-bind:src="item" @click="previewImageInWeixin"/>
                        <div v-if="!validateImg(item)"  style="width: 100%">
                            <a target="_blank" :href="item">
                                <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='pdf'" :src="pdfImg">
                                <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='docx'" :src="docxImg">
                                <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='doc'" :src="docxImg">
                                <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='xlsx'" :src="xlsxImg">
                                <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='xls'" :src="xlsxImg">
                                <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='pptx'" :src="pptxImg">
                                <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='ppt'" :src="pptxImg">
                            </a>
                        </div>
                    </div>
                </FormItem>
            </Form>
            <Form ref="record" v-if="audityItem.audityStatus=='COMMITED' || audityItem.audityStatus=='RECOMMITED'" :model="record" style="padding-left:15px;margin-bottom: 20px;":rules="formValidate">
                <Row class="audity-title">审批意见</Row>
                <FormItem prop="isPassed" style="margin-bottom: 0px">
                    <RadioGroup v-model="record.isPassed">
                        <Radio label="YES" value="YES">通过</Radio>
                        <Radio label="NO" value="NO">退回</Radio>
                    </RadioGroup>
                </FormItem>
                <FormItem v-if="record.isPassed==='NO'" prop="audityComment" style="padding-right: 10px;">
                    <Input  v-model="record.audityComment" placeholder="请输入退回理由" type="textarea" :autosize="{minRows: 6,maxRows:6}" ></Input>
                </FormItem>
            </Form>
            <Collapse v-model="value1" style="margin: 0 10px 10px 10px;">
                <Panel name="1">
                    <span style="color:#FB7F03;">审批记录</span>
                    <div class="table" slot="content">
                        <div v-for="(item,index) in audityRecordList" >
                            <div class="tr bg-g" v-if="index % 2 == 0">
                                <div class="td" style="width:20%;text-align:center">{{item.userName}}</div>
                                <div class="td" style="width:30%;text-align:center">
                                    <div>{{item.createTime_d}}</div>
                                    <div>{{item.createTime_t}}</div>
                                </div>
                                <div class="td" style="width:50%">
                                    <div>{{item.isPassedText}}</div>
                                    <div v-if="item.isPassed=='NO'">{{item.audityComment}}</div>
                                </div>
                            </div>
                            <div class="tr" v-else>
                                <div class="td" style="width:20%;text-align:center">{{item.userName}}</div>
                                <div class="td" style="width:30%;text-align:center">
                                    <div>{{item.createTime_d}}</div>
                                    <div>{{item.createTime_t}}</div>
                                </div>
                                <div class="td" style="width:50%">
                                    <div>{{item.isPassedText}}</div>
                                    <div v-if="item.isPassed=='NO'">{{item.audityComment}}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </Panel>
            </Collapse>


            <div class="audity-btn" v-if="audityItem.audityStatus=='COMMITED' || audityItem.audityStatus=='RECOMMITED'">
                <Button type="warning" @click="handleSubmit" long>审批</Button>
            </div>
            <br>
            <br>
    </div>

</template>

<script>
//  var weiXin = require('http://res.wx.qq.com/open/js/jweixin-1.0.0.js')
  var docxImg = require('../../images/docx.png')
  var xlsxImg = require('../../images/xlsx.png')
  var pptxImg = require('../../images/pptx.png')
  var pdfImg = require('../../images/pdf.png')
  export default {
    name: 'searchable-table',
    data () {
      return {
        docxImg:docxImg,
        xlsxImg:xlsxImg,
        pptxImg:pptxImg,
        pdfImg:pdfImg,
        record: {
          isPassed:'',
          audityComment:''
        },
        bDate:null,
        eDate:null,
        audityPictureArr:[],
        audityLinkArr:[],
        audityContent:null,
        audityItem: {},
        audityComment:null,
        viewModal: false,
        editModal: false,
        pageList: {},
        searchParams: {},
        userParams: {},
        allDicts: {},
        isPassed: [],
        audityStatus: [],
        audityRecord: [],
        audityRecordList: [],
        formValidate: {
          audityComment: [
            {required: false, message: '请输入审批意见', trigger: 'blur'}
          ],
          isPassed: [
            {required: true, message: '请选择审批结果', trigger: 'blur'}
          ]
        },
        operatorId :null,
        audityId :null
    }
    },
    methods: {
      init () {
        this.operatorId = this.$route.params.operatorId;
        this.audityId = this.$route.params.audityId;
        //this.initFormatter()
        this.search(this.audityId)
        this.viewAudityRecords(this.audityId)
      },
      search (audityId) {
        var params = {}
        params.id = audityId

        this.$axiosInstance({
          method: 'post',
          url: '/bizspace/goAudityH5/getAudityDetail',
          data: params
        }).then((resp) => {
          this.audity(resp.data.data)
        })
      },
      searchCancel () {
        this.searchParams = {}
        this.bDate=null
        this.eDate=null

        this.search()
      },
      changePage (currentPage) {
        this.searchParams.pageNum = currentPage
        this.search()
      },
      changePageNum (pageSize) {
        this.searchParams.pageSize = pageSize
        this.search()
      },

      exportData () {
        this.$refs.recordTable.exportCsv({
          filename: 'data'
        })
      },

      viewAudityRecords (id) {
        this.audityComment = null;
        var params = {}
        params.audityId = id
        //params.isPassed = 'NO'
        this.$axiosInstance({
          method: 'post',
          url: '/bizspace/goAudityH5/getAudityRecord',
          data: params
        }).then((res) => {
          if(res.data.data.length>0){

            let latestNoPass = null
            for (let i = res.data.data.length-1; i >=0;i--){
              if (latestNoPass == null && res.data.data[i].isPassed=='NO'){
                latestNoPass = (res.data.data[i].audityComment == null ? '' : res.data.data[i].audityComment);
              }
              res.data.data[i].createTime_d = res.data.data[i].createTime.substring(0,10);
              res.data.data[i].createTime_t = res.data.data[i].createTime.substring(11);
            }


            this.audityComment = latestNoPass
            this.audityRecordList = res.data.data
          }
        })
      },

      audity (row) {
        console.log(row)
        this.audityPictureArr=[]
        this.audityLinkArr=[]
        this.audityContent=null
        this.audityItem.id = row.id
        this.audityItem.audityStatus = row.audityStatus
        this.audityItem.audityStatusText = row.audityStatusText
        this.audityItem.userName = row.userName
        this.audityItem.audityTitle = row.audityTitle
        this.audityItem.audityLink = row.audityLink
        this.audityItem.audityPicture = row.audityPicture
        if(this.audityItem.audityPicture!=null && this.audityItem.audityPicture!=''){
          this.audityPictureArr = this.audityItem.audityPicture.split(',')
        }
        if(this.audityItem.audityLink ){
          //this.audityLinkArr = row.audityLink.split(',')
          var textR = this.audityItem.audityLink;
          var reg = /(http:\/\/|https:\/\/)([-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|])/g;
          textR = textR.replace(reg, "<a href='$1$2'>$1$2</a>")
          textR=textR.replace(/\r\n/g,"<br>")
          textR=textR.replace(/\n/g,"<br>");

          this.audityContent = textR;
        }
        this.record = {}
      },

      handleSubmit () {
        this.$refs['record'].validate((valid) => {
          if (valid) {
            var params = {}
            params.audityUserId=this.operatorId
            params.audityId = this.audityItem.id
            params.isPassed = this.record.isPassed
            params.audityComment = this.record.audityComment

            this.$axiosInstance({
              method: 'post',
              url: '/bizspace/goAudityH5/modify',
              data: params
            }).then((resp) => {
              this.editModal = false
              if (resp.data.httpCode === 200) {
                this.search(this.audityItem.id);
                this.$Message.info('审批成功')
              } else {
                this.$Message.info('审批失败')
              }
            })
          }
        })
      },
      cancel () {
        this.editModal = false
      },
      chgBegin(e){
        console.log(e)
        this.searchParams.beginDate=e
      },
      chgEnd(e){
        console.log(e)
        this.searchParams.endDate=e
      },
      previewImageInWeixin(event){
        let nowImgurl = event.currentTarget.src
        console.log(nowImgurl)
        let imgs = []
        imgs.push(nowImgurl);
        WeixinJSBridge.invoke("imagePreview", {
          "urls": imgs,
          "current": nowImgurl
        })
      },
      exportFile () {
        if (this.searchParams.beginDate=== undefined|| this.searchParams.beginDate === '' || this.searchParams.endDate=== undefined|| this.searchParams.endDate === '') {
          this.$Message.info('请选择开始日期和结束日期！')
          return
        }
        if (this.bDate > this.eDate ) {
          this.$Message.info('开始日期不能大于结束日期！')
          return
        }
//          var t = new Date(this.beginDate)
//          var year = t.getFullYear()
//          var month = (t.getMonth() + 1) < 10 ? '0' + (t.getMonth() + 1) : (t.getMonth() + 1)
//          var n = year + '-' + month
        var url = '/pr/goAudityRecord/export'
                + '?beginDate=' + this.searchParams.beginDate
                + '&endDate=' + this.searchParams.endDate
                + ((this.searchParams.userName!=undefined &&this.searchParams.userName!='')?'&userName=' + this.searchParams.userName:'')
                + ((this.searchParams.audityTitle!=undefined &&this.searchParams.audityTitle!='')?'&audityTitle=' + this.searchParams.audityTitle:'')
                + ((this.searchParams.audityStatus!=undefined &&this.searchParams.audityStatus!='')?'&audityStatus=' + this.searchParams.audityStatus:'')
        console.log(url)
        window.open(url)
      },
      removeEmpty (obj) {
        Object.keys(obj).forEach(function (key) {
          (obj[key] && typeof obj[key] === 'object') && this.removeEmpty(obj[key]) ||
          (obj[key] === undefined || obj[key] === null) && delete obj[key]
        })
        return obj
      },
      initFormatter () {
        Date.prototype.Format = function (fmt) { //
          let o = {
            'M+': this.getMonth() + 1,                 //月份
            'd+': this.getDate(),                    //日
            'h+': this.getHours(),                   //小时
            'm+': this.getMinutes(),                 //分
            's+': this.getSeconds(),                 //秒
            'q+': Math.floor((this.getMonth() + 3) / 3), //季度
            'S': this.getMilliseconds()             //毫秒
          }
          if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length))
          for (var k in o)
            if (new RegExp('(' + k + ')').test(fmt))
              fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
          return fmt
        }
      },
      validateImg: function (value) {
        var h = value.substring((value.lastIndexOf('.')+1),value.length).toLocaleLowerCase();
        if(h == 'png'||h == 'jpg'||h == 'jpeg'||h == 'gif'||h=='bmp'){
          return true;
        }
        return false;
      }
    },
    mounted () {
      this.init()
    }
  }
</script>

<style>
    body .ivu-modal .ivu-select-dropdown{
        position: fixed !important;
    }
    .table {
        border: 0px solid darkgray;
    }
    .tr {
        display: flex;
        width: 100%;
        justify-content: center;
        align-items: center;
    }
    .td {
        justify-content: center;
        word-wrap: break-word;
        word-break: break-all;
    }
    .bg-w{
        background: snow;
    }
    .bg-g{
        background: #E6F3F9;
    }
    .th {
        width: 40%;
        justify-content: center;
        background: #3366FF;
        color: #fff;
        display: flex;
        height: 3rem;
        align-items: center;
    }
</style>