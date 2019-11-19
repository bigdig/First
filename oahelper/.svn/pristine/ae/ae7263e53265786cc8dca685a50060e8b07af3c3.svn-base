<style lang="less">
    @import '../../styles/common.less';
    @import '../system/components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
            <Card>
                <row><h2 style="margin-bottom:20px">提交审批</h2></row>
                <Row>
                    <Col span="22">
                    <span>标题：</span>
                    <Input v-model="searchParams.audityTitle" placeholder="标题" style="width: 100px"/>
                    <span>审批状态：</span>
                    <Select v-model="searchParams.audityStatus" style="width:100px">
                        <Option v-for="item in audityStatus" :value="item.id" :key="item.id">{{ item.text }}</Option>
                    </Select>
                    <Date-picker placeholder="开始日期" type="datetime"
                                 v-model="bDate" @on-change="chgBegin" format="yyyy-MM-dd" style="width: 150px"></Date-picker>
                    <Date-picker placeholder="结束日期" type="datetime"
                                 v-model="eDate"  @on-change="chgEnd" format="yyyy-MM-dd" style="width: 150px"></Date-picker>

                    <span @click="search" style="margin: 0 10px;">
                        <Button type="primary" icon="search">搜索</Button>
                    </span>
                    <!--<Button @click="searchCancel" type="ghost">取消</Button>-->
                    <Button @click="exportFile" type="ghost">导出</Button>
                    </Col>
                    <Col span="2" class="">
                    <span @click="addRecordBtn"><Button type="success"> 新建审批 </Button></span>
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

        <Modal width="600" v-model="editModal" :title="editMode=='add'?'新建':'编辑'" :mask-closable="false">

            <Form ref="record" :model="record" label-position="right" :label-width="100" style="margin-top:20px;"
                  :rules="formValidate">
                <FormItem label="标题：" prop="audityTitle">
                    <Input v-model="record.audityTitle" placeholder=""></Input>
                </FormItem>
                <FormItem label="链接地址：" prop="audityLink">
                    <Input v-model="record.audityLink" placeholder="" type="textarea"
                           :autosize="{minRows: 3,maxRows:3}"></Input>
                    <br>
                    <span style="color:red">多个链接请以英文逗号,做分隔</span>
                </FormItem>
                <FormItem label="上传图片">
                    <div v-if="audityPictureArr!=''" class="demo-upload-list" v-for="item in audityPictureArr">
                        <template v-if="item != ''">
                            <img v-if="validateImg(item)" :src="item">
                            <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='pdf'" :src="pdfImg">
                            <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='docx'" :src="docxImg">
                            <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='doc'" :src="docxImg">
                            <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='xlsx'" :src="xlsxImg">
                            <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='xls'" :src="xlsxImg">
                            <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='pptx'" :src="pptxImg">
                            <img v-if="item.substring((item.lastIndexOf('.')+1),item.length).toLocaleLowerCase()=='ppt'" :src="pptxImg">
                            <div class="demo-upload-list-cover">
                                <Icon type="ios-eye-outline" @click.native="handleView(item)"></Icon>
                                <Icon type="ios-trash-outline" @click.native=" deletePIc(item)"></Icon>
                            </div>
                        </template>
                        <template v-else>
                            <Progress v-if="item.showProgress" :percent="item.percentage" hide-info></Progress>
                        </template>
                    </div>
                    <Upload
                            ref="upload"
                            :show-upload-list="false"
                            :default-file-list="defaultList"
                            :on-success="handleSuccess"
                            :format="['jpg','jpeg','png','bmp','gif','docx','doc','xlsx','xls','pptx','ppt','pdf']"
                            :max-size="20480"
                            :on-format-error="handleFormatError"
                            :on-exceeded-size="handleMaxSize"
                            :before-upload="handleBeforeUpload"
                            multiple
                            type="drag"
                            action="pr/goUpload/fileArchive"
                            style="display: inline-block;width:58px;">
                        <div style="width: 58px;height:58px;line-height: 58px;">
                            <Icon type="ios-camera" size="20"></Icon>
                        </div>
                    </Upload>
                </FormItem>
               <!-- <FormItem label="图片：">
                    <div>
                        <Row class="text-center">
                            <Col span="8">
                                <Upload ref="upload" :show-upload-list="false" :on-success="handleSuccess"
                                        action="pr/goUpload/fileArchive">
                                    <Button icon="ios-cloud-upload-outline">上传文件</Button>
                                </Upload>
                                <div v-for="item in audityPictureArr">
                                    <a target="_blank" :href="item">下载查看</a>
                                    <Button type="ghost" shape="circle" @click="deletePIc(item) "  style="width: 55px; height: 30px;" >删除</Button>
                                </div>
                                &lt;!&ndash;<Input v-show="false" v-model="record.audityPicture1"/>&ndash;&gt;
                            </Col>
                        </Row>
                    </div>
                </FormItem>-->
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" @click="handleSubmit">提交</Button>
            </div>
        </Modal>
        <Modal title="图片预览" v-model="visible">
            <img :src="imgUrl + ''" v-if="visible" style="width: 100%;">
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
        record: {
          audityPicture1: null
        },
        bDate:null,
        eDate:null,
        audityPictureArr:[],
        imgUrl: '',
        visible: false,
        audityItem: {},
        viewModal: false,
        editModal: false,
        editMode: '',
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
                  links = links + '<a href="' + urls[i] + '" target="_blank">附件' + (i+1) + '</a>' + '<br>'
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
            key: 'audityLink',
            title: '链接地址',
            width: 300,
            render: (h, params) => {
              if(params.row.audityLink.indexOf("http") == 0){
                var urls = params.row.audityLink.split(',')
                var links = ''
                for (var i in urls) {
                  links = links + '<a href="' + urls[i] + '" target="_blank">' + urls[i] + '</a>' + '<br>'
                }
              }else{
                links = '<span>' + params.row.audityLink + '</span>'
              }
              return h('div', {
                domProps: {
                  innerHTML: links
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
            width: 250,
            render: (h, params) => {
              var v = false
              if (params.row.audityStatus == 'PASSED' || params.row.audityStatus == 'COMMITED' || params.row.audityStatus == 'RECOMMITED') {
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
                      this.initDetail(params.row.id)
                    }
                  }
                }, '重新提交')
              ])
            }
          }
        ],
        formValidate: {
          audityTitle: [
            {required: true, message: '请输入标题', trigger: 'blur'}
          ],
          audityLink: [
            {required: true, message: '请选择链接地址', trigger: 'blur'}
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
          url: '/pr/goAudity/read/mylist',
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
      addRecordBtn () {
        this.initDetail('')
        this.editModal = true
      },
      initDetail (id) {
        this.audityPictureArr=[]
        console.log(this.uploadList)
        this.record = {}//表单置空
        this.record.fileWeight = 1
        this.clearFiles()
        if (id === null || id === '') {
          this.editMode = 'add'
        } else {
          this.editMode = 'edit'
        }
        var param = {}
        param.id = id
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goAudity/read/detail',
          data: param
        }).then((resp) => {
          //this.PrDatatypes = resp.data.dicts.PrDatatypes;
          this.record = resp.data.data
          if(resp.data.data.audityPicture!=''){
            let arr = resp.data.data.audityPicture.split(',');
            for (let i = 0; i < arr.length; i++) {
              if (arr[i].indexOf('/') == 0) {
              } else {
                arr[i] = '/' + arr[i]
              }
            }
            this.audityPictureArr=arr;
          }

        })
      },

      handleSubmit () {
        this.record.audityPicture = this.audityPictureArr.join(',')
        this.$refs['record'].validate((valid) => {
          if (valid) {

            if (this.editMode === 'edit') {
              this.updateRecord()
            } else if (this.editMode === 'add') {
              this.addRecord()
            }
          }
        })
      },

      updateRecord () {
        this.record.audityStatus = 'RECOMMITED'
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goAudity/update',
          data: this.removeEmpty(this.record)
        }).then((resp) => {
          this.editModal = false
          if (resp.data.httpCode === 200) {
            this.search()
            this.$Message.info('更新成功')
          } else {
            this.$Message.info('更新失败')
          }
        })
      },
      addRecord () {
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goAudity/add',
          data: this.removeEmpty(this.record)
        }).then((resp) => {
          this.editModal = false
          if (resp.data.httpCode === 200) {
            this.search()
            this.$Message.info('提交成功')
          } else {
            this.$Message.info('提交失败')
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
        let userInfo = JSON.parse(localStorage.userInfo);
        var url = '/pr/goAudityRecord/export'
          + '?beginDate=' + this.searchParams.beginDate
          + '&endDate=' + this.searchParams.endDate
          + '&userId=' + userInfo.id
          + ((this.searchParams.audityTitle!=undefined &&this.searchParams.audityTitle!='')?'&audityTitle=' + this.searchParams.audityTitle:'')
          + ((this.searchParams.audityStatus!=undefined &&this.searchParams.audityStatus!='')?'&audityStatus=' + this.searchParams.audityStatus:'')
        console.log(url)
        window.open(url)
      },
      handleSuccess (res, file) {
        file.url = res.files[0].url.replace(/\\/g, '/')
        file.name = res.files[0].name
        if (file.url.indexOf('/') == 0) {
        } else {
          file.url = '/' + file.url
        }

        this.audityPictureArr.push(file.url);
        console.log(this.audityPictureArr)
      },

      handleView (name) {
        if(this.validateImg(name)){
          this.imgUrl = name
          this.visible = true
        }else{
          window.open(name);
        }
      },
     /* handleRemove (file) {
        const fileList = this.$refs.upload.fileList
        this.$refs.upload.fileList.splice(fileList.indexOf(file), 1)
      },*/
      handleFormatError (file) {
        this.$Notice.warning({
          title: '文件格式不正确',
          desc: '文件 ' + file.name + ' 格式不正确, 仅支持jpg、jpeg、png、bmp、gif格式。'
        })
      },
      handleMaxSize (file) {
        this.$Notice.warning({
          title: '文件过大',
          desc: '文件  ' + file.name + ' 过大，允许的文件不超过2M。'
        })
      },
      handleBeforeUpload () {
        const check = this.audityPictureArr.length < 5
        if (!check) {
          this.$Notice.warning({
            title: '只能上传不超过5张照片。'
          })
        }
        console.log(this.uploadList)
        return check
      },

      deletePIc(fileUrl){
        var index = this.audityPictureArr.indexOf(fileUrl);
        if (index > -1) {
          this.audityPictureArr.splice(index, 1);
        }
      },
      clearFiles () {
        this.$refs['upload'].clearFiles()
        //this.record.audityPicture1 = ''
        //console.log(this.record.audityPicture1)
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
      //this.audityPictureArr = this.$refs.upload.fileList
    }
  }
</script>

<style>
.demo-upload-list {
display: inline-block;
width: 60px;
height: 60px;
text-align: center;
line-height: 60px;
border: 1px solid transparent;
border-radius: 4px;
overflow: hidden;
background: #fff;
position: relative;
box-shadow: 0 1px 1px rgba(0, 0, 0, .2);
margin-right: 4px;
}

.demo-upload-list img {
width: 100%;
height: 100%;
}

.demo-upload-list-cover {
display: none;
position: absolute;
top: 0;
bottom: 0;
left: 0;
right: 0;
background: rgba(0, 0, 0, .6);
}

.demo-upload-list:hover .demo-upload-list-cover {
display: block;
}

.demo-upload-list-cover i {
color: #fff;
font-size: 20px;
cursor: pointer;
margin: 0 2px;
}
</style>