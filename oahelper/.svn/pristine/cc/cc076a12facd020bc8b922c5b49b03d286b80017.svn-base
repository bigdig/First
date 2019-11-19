<style lang="less">
    @import '../../styles/common.less';
    @import '../system/components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
            <Card>
                <row><h2 style="margin-bottom:20px">信息审批</h2></row>
                <Row>
                    <Col span="24">
                    <span>标题：</span>
                    <Input v-model="searchParams.audityTitle" placeholder="标题" style="width: 100px"/>
                    <span>提交人：</span>
                    <Input v-model="searchParams.userName" placeholder="提交人" style="width: 100px"/>
                    <span>审批状态：</span>
                    <Select v-model="searchParams.audityStatus" style="width:100px">
                        <Option v-for="item in audityStatus" :value="item.id" :key="item.id">{{ item.text }}</Option>
                    </Select>
                    <Date-picker placeholder="开始日期" type="datetime"
                                 v-model="bDate" @on-change="chgBegin" format="yyyy-MM-dd" style="width: 150px"></Date-picker>
                    <Date-picker placeholder="结束日期" type="datetime"
                                 v-model="eDate"  @on-change="chgEnd" format="yyyy-MM-dd" style="width: 150px"></Date-picker>
                    <span @click="search" style="margin: 0 10px;">
                        <Button type="primary" icon="search">搜索</Button></span>
                    <!--<Button @click="searchCancel" type="ghost">取消</Button>-->
                    <Button @click="exportFile" type="ghost">导出</Button>
                    </Col>

                </Row>
                <Row class="margin-top-10 ">
                    <Table :columns="columns1" :data="pageList.list"></Table>
                </Row>
                <Row class="margin-top-10 ">
                    <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage"
                          @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                </Row>
            </Card>
            </Col>
        </Row>
        <Modal width="800" v-model="viewModal" title="查看详情" :mask-closable="false">
            <Row>
                <Button type="primary" @click="exportData()" style="margin-bottom: 20px;">
                    <Icon type="ios-download-outline"></Icon>
                    导出审批记录
                </Button>
            </Row>
            <Row>
                <Table ref="recordTable" height="400" stripe border :columns="audityRecordColumn"
                       :data="audityRecordList"></Table>
            </Row>
            <div slot="footer">
            </div>
        </Modal>

        <Modal width="600" v-model="editModal" title="审批" :mask-closable="false" >

            <Form ref="record" :model="record" label-position="right" :label-width="100" style="margin-top:20px;position: relative;"
                  :rules="formValidate">
                <FormItem label="提交人：" style="margin-bottom: 5px">
                    {{audityItem.userName}}
                </FormItem>
                <FormItem label="提交时间：" style="margin-bottom: 5px">
                    {{audityItem.createTime}}
                </FormItem>
                <FormItem label="标题：" style="margin-bottom: 5px">
                    {{audityItem.audityTitle}}
                </FormItem>
                <FormItem label="链接地址：" style="margin-bottom: 5px">
                    <div v-if="audityLinkArr.length>0" v-for="(item,index) in audityLinkArr">
                        <a v-bind:href="item" target="_blank">链接地址{{ index +1}}</a>
                    </div>
                    <div v-if="audityLinkArr.length==0" >
                        <div v-html="audityContent" style="word-wrap:break-word;word-break:break-all"></div>
                    </div>
                </FormItem>
                <FormItem label="图片/文件：" style="margin-bottom: 5px" v-if="audityPictureArr.length>0">
                    <div v-for="(item,index) in audityPictureArr">
                        <img v-if="validateImg(item)"  style="width: 100%" v-bind:src="item"/>
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
                    <!--<a :href="audityItem.audityPicture" :target="_blank">查看图片</a>-->
                </FormItem>

                <FormItem label="是否通过：" prop="isPassed" >
                    <Select v-model="record.isPassed" style="width:150px" >
                        <Option v-for="item in isPassed" :value="item.id" :key="item.id">{{ item.text }}</Option>
                    </Select>
                </FormItem>
                <FormItem v-if="record.isPassed==='NO'" label="退回意见：" prop="audityComment">
                    <Input v-model="record.audityComment" type="textarea" :autosize="{minRows: 6,maxRows:6}"  placeholder=""></Input>
                </FormItem>
                <br>

            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" @click="handleSubmit">审批</Button>
            </div>
        </Modal>
    </div>

</template>

<script>
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
        record: {},
        bDate:null,
        eDate:null,
        audityPictureArr:[],
        audityLinkArr:[],
        audityContent:null,
        audityItem: {},
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
        audityRecordColumn: [
          {
            key: 'userName',
            title: '提交人',
            width: 100
          },
          {
            key: 'isPassedText',
            title: '操作',
            width: 100
          },
          {
            key: 'audityComment',
            title: '审批内容/意见'
          },
          {
            key: 'audityPicture',
            title: '附件',
            render: (h, params) => {
              if(params.row.audityPicture!=null && params.row.audityPicture!=''){
                var urls = params.row.audityPicture.split(',')
                var links = ''
                for (let i=0;i < urls.length;i++) {
                  links = links + '<a href="' + urls[i] + '" target="_blank">附件'+ (i+1) + '</a>' + '<br>'
                }
              }else{
                links = '<span></span>'
              }
              return h('div', {
                domProps: {
                  innerHTML: links
                }
              })
            }
          },
          {
            key: 'createTime',
            title: '处理时间',
            width: 200
          }
        ],
        columns1: [
          {
            key: 'audityTitle',
            title: '标题'
          },
          {
            key: 'userName',
            title: '提交人'
          },
          {
            key: 'audityLink',
            title: '链接地址',
            width: 300,
            render: (h, params) => {
              var textR = params.row.audityLink;
              var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
              textR = textR.replace(reg, "<a target='_blank' href='$1$2'>$1$2</a>")
              textR=textR.replace(/\r\n/g,"<br>")
              textR=textR.replace(/\n/g,"<br>");

              return h('div', {
                domProps: {
                  innerHTML: textR
                }
              })
            }
          },
          {
            key: 'audityStatusText',
            title: '状态'
          },
          {
            key: 'uTime',
            title: '最后更新时间',
            render: (h, params) => {
              return h('div', new Date(Number(params.row.updateTime)).Format('yyyy-MM-dd hh:mm:ss'))
            }
          },
          {
            key: 'action',
            title: '操作',
            width: 200,
            render: (h, params) => {
              var v = false
              if (params.row.audityStatus == 'PASSED'||params.row.audityStatus == 'RETURNED') {
                v = true
              }
              return h('div', [
                h('Button', {
                  props: {
                    type: 'success',
                    size: 'small'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.viewModal = true
                      this.viewDetail(params.row.id)
                    }
                  }
                }, '查看审批记录'),
                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small',
                    icon: 'edit',
                    disabled: v
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.editModal = true
                      this.audity(params.row)
                    }
                  }
                }, '审批')
              ])
            }
          }
        ],
        formValidate: {
          audityComment: [
            {required: false, message: '请输入审批意见', trigger: 'blur'}
          ],
          isPassed: [
            {required: true, message: '请选择审批结果', trigger: 'blur'}
          ]
        },
      }
    },
    methods: {
      init () {
        this.searchParams.pageSize = 10
        this.searchParams.pageNum = 1
        this.initFormatter()
        this.search()
      },
      search () {
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goAudity/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.audityStatus = resp.data.dicts.AUDITY_STATUS
          this.pageList = resp.data.data
        })
        this.$axiosInstance({
          method: 'post',
          url: '/pr/myCatalog/read/catalog',
          data: {type: 'YES_NO'}
        }).then((resp) => {
          this.isPassed = resp.data.dicts.YES_NO
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

      viewDetail (id) {
        var params = {}
        params.audityId = id
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goAudityRecord/selectByCondition',
          data: params
        }).then((resp) => {
          for(let i = 0 ;i<resp.data.data.length;i++){
            if(resp.data.data[i].audityPicture !=null && resp.data.data[i].audityPicture != ''){
              let urls = resp.data.data[i].audityPicture.split(',')
              let links = ''
              for (let j=0 ;j< urls.length;j++) {
                if(urls[j]!=''){
                  links = 'https://tytool.tfzq.com' + urls[j] + ','+links
                }
              }
              resp.data.data[i].audityPicture = links.substring(0,links.length-1);
            }
          }
          this.audityRecordList = resp.data.data
        })
      },

      audity (row) {
        console.log(row)
        this.audityPictureArr=[]
        this.audityLinkArr=[]
        this.audityContent=null
        this.audityItem.id = row.id
        this.audityItem.audityStatus = row.audityStatus
        this.audityItem.userName = row.userName
        this.audityItem.audityTitle = row.audityTitle
        this.audityItem.createTime = row.createTime
        this.audityItem.audityLink = row.audityLink
        this.audityItem.audityPicture = row.audityPicture
        if(this.audityItem.audityPicture!=null && this.audityItem.audityPicture!=''){
          this.audityPictureArr = this.audityItem.audityPicture.split(',')
        }
        if(this.audityItem.audityLink ){
          var textR = this.audityItem.audityLink;
          var reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
          textR = textR.replace(reg, "<a target='_blank' href='$1$2'>$1$2</a>")
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
            params.userName=this.audityItem.userName
            params.audityId = this.audityItem.id
            params.audityStatus = this.audityItem.audityStatus
            params.isPassed = this.record.isPassed
            params.audityComment = this.record.audityComment

            this.$axiosInstance({
              method: 'post',
              url: '/pr/goAudity/modify',
              data: params
            }).then((resp) => {
              this.editModal = false
              if (resp.data.httpCode === 200) {
                this.search()
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
</style>