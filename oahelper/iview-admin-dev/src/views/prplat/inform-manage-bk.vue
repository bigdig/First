<style lang="less">
    @import '../../styles/common.less';
    @import '../system/components/table.less';
</style>

<template>
    <div>

        <Row :gutter="10">
            <Col span="24">
            <Card>
                <row><h2 style="margin-bottom:20px">消息管理</h2></row>
                <Row>
                    <Col span="22">
                    <span>消息标题：</span>
                    <Input v-model="searchParams.informTitle" placeholder="消息标题" style="width: 200px"/>
                    <span>来源类型：</span>
                    <Select v-model="searchParams.srcType" filterable style="width:150px">
                        <Option v-for="item in src_type" :value="item.id" :key="item.id">{{ item.text }}</Option>
                    </Select>
                    <span>是否需要回应：</span>
                    <Select v-model="searchParams.isReply" style="width:100px">
                        <Option v-for="item in yes_no" :value="item.id" :key="item.id">{{ item.text }}</Option>
                    </Select>
                    <span @click="search" style="margin: 0 10px;"><Button type="primary"
                                                                          icon="search">搜索</Button></span>
                    <Button @click="searchCancel" type="ghost">清空条件</Button>
                    </Col>

                    <Col span="2" class="">
                    <span @click="addRecordBtn"><Button type="success"> 新建消息 </Button></span>
                    </Col>
                </Row>
                <Row class="margin-top-10 ">
                    <Table stripe border :columns="columns1" :data="pageList.list"></Table>
                </Row>
                <Row class="margin-top-10 ">
                    <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage"
                          @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                </Row>
            </Card>
            </Col>
        </Row>

        <Modal width="800" v-model="formModal" :title="editMode=='add'?'新增':'编辑'" :mask-closable="false">
            <Form ref="record" :model="record" label-position="right" :label-width="100" :rules="formValidate">
                <FormItem label="消息标题" prop="informTitle">
                    <Input v-model="record.informTitle" placeholder=""></Input>
                </FormItem>
                <FormItem label="上传图片">
                    <div class="demo-upload-list" v-for="item in uploadList">
                        <template v-if="item.status === 'finished'">
                            <img :src="'http://127.0.0.1:8088/pr'+item.url">
                            <div class="demo-upload-list-cover">
                                <Icon type="ios-eye-outline" @click.native="handleView(item.url)"></Icon>
                                <Icon type="ios-trash-outline" @click.native="handleRemove(item)"></Icon>
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
                            :format="['jpg','jpeg','png','bmp','gif']"
                            :max-size="2048"
                            :on-format-error="handleFormatError"
                            :on-exceeded-size="handleMaxSize"
                            :before-upload="handleBeforeUpload"
                            multiple
                            type="drag"
                            action="pr/goUpload/inform"
                            style="display: inline-block;width:58px;">
                        <div style="width: 58px;height:58px;line-height: 58px;">
                            <Icon type="ios-camera" size="20"></Icon>
                        </div>
                    </Upload>
                    <!--<Upload multiple :before-upload="handleUpload"-->
                    <!--action="">-->
                    <!--<Button icon="ios-cloud-upload-outline">选择文件开始上传（可选多个文件）</Button>-->
                    <!--</Upload>-->

                    <!--<div v-model="files">-->
                    <!--<span style="margin-right: 10px;" v-for="item in files">{{item.name}}</span>-->
                    <!--<Button type="success" @click="upload" :loading="loadingStatus">{{ loadingStatus ? 'Uploading' :-->
                    <!--'点击开始上传' }}-->
                    <!--</Button>-->
                    <!--</div>-->

                </FormItem>
                <FormItem label="发送到组">
                    <div v-model="groupList">
                        <span v-for="item in groupList" style="margin-right: 15px;">{{item}}</span>
                    </div>
                    <Button type="primary" @click="selectGroup" style="width:100px;">选择用户分组</Button>
                    <Button type="error" @click="clearGroupList">清空</Button>
                </FormItem>
                <FormItem label="发送到个人">
                    <div v-model="userListAll">
                        <span v-for="item in userListAll" v-if="item.isSelected" style="margin-right: 15px;">{{item.userName}}</span>
                    </div>
                    <Button type="primary" @click="selectUser" style="width:100px;">选择用户</Button>
                    <Button type="error" @click="clearUserList">清空</Button>
                </FormItem>
                <FormItem label="是否需要回应" prop="isReply">

                    <Select v-model="record.isReply" style="width:100px">
                        <Option v-for="item in yes_no" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>

                </FormItem>
                <Form-item label="消息内容" prop="informContent">
                    <Input v-model="record.informContent" type="textarea" :autosize="{minRows: 6,maxRows:6}"
                           placeholder=""></Input>
                </Form-item>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存并发送</Button>
            </div>
        </Modal>

        <Modal title="图片预览" v-model="visible">
            <img :src="'http://127.0.0.1:8088/pr' + imgUrl + ''" v-if="visible" style="width: 100%;">
        </Modal>
        <Modal title="选择用户分组" v-model="group_visible">
            <Checkbox-Group v-model="groupListId" @on-change="translateGroup()">
                <div style="font-weight:bold;font-size: 14px;margin:10px 0 10px 0">部门分组</div>
                <checkbox style="width: 150px;height: 30px;" v-for="item in groupListAll"
                          v-if="item.groupType==='DEPARTMENT_GROUP'"
                          :label="item.id">{{item.groupName}}
                </checkbox>
                <div style="font-weight:bold;font-size: 14px;margin:10px 0 10px 0">自定义分组</div>
                <checkbox style="width: 150px;height: 30px;" v-for="item in groupListAll"
                          v-if="item.groupType==='CUSTOM_GROUP'"
                          :label="item.id">{{item.groupName}}
                </checkbox>
            </Checkbox-Group>
            <div slot="footer">
                <Button type="primary" :loading="saveLoading" @click="ensureGroup">确定</Button>
            </div>
        </Modal>

        <Modal title="选择用户" v-model="user_visible" class-name="vertical-center-modal">
            <scroll height="300">
                <span v-for="option in userListAll">
                    <Checkbox v-model="option.isSelected" :value="option.isSelected" :key="option.id"
                              style="width: 120px;line-height: 40px;">
                        {{option.userName}}
                    </Checkbox>
                </span>
            </scroll>
        </Modal>

        <!--<Modal title="选择用户" v-model="user_visible">-->
        <!--&lt;!&ndash;<Tree :data="userList" :load-data="loadUser" show-checkbox></Tree>&ndash;&gt;-->
        <!--<div slot="footer">-->
        <!--<Button type="primary" :loading="saveLoading" @click="ensureUser">确定</Button>-->
        <!--</div>-->
        <!--</Modal>-->
    </div>
</template>

<script>
  import Checkbox from 'iview/src/components/checkbox/checkbox'

  export default {
    components: {Checkbox},
    name: 'searchable-table',
    data () {
      return {
        // files: [],
        defaultList: [],
        imgUrl: '',
        visible: false,
        group_visible: false,
        user_visible: false,
        uploadList: [],
        groupList: [],
        groupListId: [],
        groupListAll: [],//所有的用户分组列表
        userList: [],
        userListAll: [],
        // userList: [{
        //   title: '所有分组',
        //   loading: false,
        //   children: [{title: '部门分组', loading: false, children: []}, {title: '自定义分组', loading: false, children: []}]
        // }],
        loadingStatus: false,
        t_spinShow: true,
        formModal: false,
        pageList: {},
        searchParams: {},
        userParams: {},
        allDicts: {},
        src_type: [],
        yes_no: [],
        //编辑
        saveLoading: false,
        editMode: '',
        record: {},
        formValidate: {
          informTitle: [
            {required: true, message: '请输入信息标题', trigger: 'blur'}
          ],
          isReply: [
            {required: true, message: '请选择', trigger: 'blur'}
          ],
          informContent: [
            {required: true, message: '请输入消息内容', trigger: 'blur'}
          ],
        },
        //列表

        columns1: [{key: 'informTitle', title: '消息标题'}, {key: 'srcTypeText', title: '来源类型'}, {
          key: 'isReplyText',
          title: '是否需要回应'
        }, {key: 'createTime', title: '创建时间'}, {key: 'operation', title: '操作'}],
        // src_type: [{value: 'INFO', label: '消息通知'}, {value: 'NOTICE', label: '公告指南'}, {value: 'MEETING', label: '晨会'}],
        // yes_no: [{value: 'YES', label: '是'}, {value: 'NO', label: '否'}]
      }
    },
    methods: {
      init () {
        this.searchParams.pageSize = 10
        this.searchParams.pageNum = 1
        // this.$axiosInstance({
        //   method: 'post',
        //   url: '/pr/goInform/read/dics'
        // }).then((resp) => {
        //   this.allDicts = resp.data.dicts.REQUESTURI
        // })
        this.search()
      },
      search () {
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goInform/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.src_type = resp.data.dicts.INFORM_TYPE
          this.yes_no = resp.data.dicts.YES_NO
          this.pageList = resp.data.data
        })
      },
      searchCancel () {
        this.searchParams = {}
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
      addRecordBtn () {
        // this.$refs.upload.clearFiles()
        this.initDetail('')
        this.formModal = true
      },
      initDetail (id) {
        this.record = {}//表单置空
        if (id === null || id === '') {
          this.editMode = 'add'
        } else {
          this.editMode = 'edit'
        }
        var param = {}
        param.id = id
        // this.$axiosInstance({
        //   method: 'post',
        //   url: '/pr/prDatatype/read/detail',
        //   data: param
        // }).then((resp) => {
        //   //this.PrDatatypes = resp.data.dicts.PrDatatypes;
        //   this.record = resp.data.data;
        // })
      },
      handleSubmit () {
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
      addRecord () {

        var informGroup = ''
        var informGroupId = ''
        var externalLink = ''
        var userId = ''

        for (var i = 0; i < this.groupList.length; i++) {
          informGroup = informGroup + ',' + this.groupList[i]
        }
        for (var i = 0; i < this.userListAll.length; i++) {
          if (this.userListAll[i].isSelected) {
            userId = userId + ',' + this.userListAll[i].id
          }
        }
        for (var i = 0; i < this.groupListId.length; i++) {
          informGroupId = informGroupId + ',' + this.groupListId[i]
        }
        for (var i = 0; i < this.uploadList.length; i++) {
          externalLink = externalLink + ',' + this.uploadList[i].url
        }

        this.record.informGroup = informGroup
        this.record.users = userId
        this.record.informGroupId = informGroupId
        this.record.externalLink = externalLink

        //
        // let self = this
        // this.groupList = []
        // this.groupListId = []
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goInform/add',
          data: this.removeEmpty(this.record)
        }).then((resp) => {
          this.saveLoading = false
          if (resp.data.httpCode === 200) {
            this.goBackList()
          } else {
            this.$Message.info(resp.data.msg)
          }
        })
      },
      updateRecord () {
        // this.$axiosInstance({
        //   method: 'post',
        //   url: '/pr/prDatatype/update',
        //   data: this.removeEmpty(this.record)
        // }).then((resp) => {
        //   this.saveLoading = false
        //   if (resp.data.httpCode === 200) {
        //     this.goBackList()
        //   } else {
        //     this.$Message.info(resp.data.msg)
        //   }
        // })
      },
      delRecord () {
        var param = {}
        param.id = this.delId
        // this.$axiosInstance({
        //   method: 'post',
        //   url: '/pr/prDatatype/delete',
        //   data: param
        // }).then((resp) => {
        //   this.saveLoading = false
        //   if (resp.data.httpCode === 200) {
        //     this.goBackList()
        //   } else {
        //     this.$Message.info(resp.data.msg)
        //   }
        // })
      },
      goBackList () {
        this.formModal = false
        this.delModal = false
        this.search()
      },
      cancel () {
        // this.$refs.upload.clearFiles()
        this.formModal = false
        this.$Message.info('返回列表页面')
      },
      removeEmpty (obj) {
        Object.keys(obj).forEach(function (key) {
          (obj[key] && typeof obj[key] === 'object') && this.removeEmpty(obj[key]) ||
          (obj[key] === undefined || obj[key] === null) && delete obj[key]
        })
        return obj
      },
      handleView (name) {
        this.imgUrl = name
        this.visible = true
      },
      handleRemove (file) {
        const fileList = this.$refs.upload.fileList
        this.$refs.upload.fileList.splice(fileList.indexOf(file), 1)
      },
      handleSuccess (res, file) {
        // console.log(file)
        file.url = res.files[0].url.replace(/\\/g, '/')
        //console.log(file)
        //file.url = 'https://o5wwk8baw.qnssl.com/7eb99afb9d5f317c912f08b5212fd69a/avatar'
        file.name = res.files[0].name
      },
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
        const check = this.uploadList.length < 5
        if (!check) {
          this.$Notice.warning({
            title: '只能上传不超过5张照片。'
          })
        }
        return check
      },
      // handleUpload (file) {
      //   this.files.push(file)
      //   return false
      // },
      // upload () {
      //   this.loadingStatus = true
      //   setTimeout(() => {
      //     this.files = []
      //     this.loadingStatus = false
      //     this.$Message.success('Success')
      //   }, 1500)
      // }
      clearGroupList () { this.groupList = [], this.groupListId = [] },
      selectGroup () {
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goGroup/read/list',
          data: {pageSize: 10000, pageNum: 1}
        }).then((resp) => {
          this.groupListAll = resp.data.data.list
          this.group_visible = true
        })

      },
      ensureGroup () {this.group_visible = false},
      translateGroup () {
        this.groupList = []
        var gli = this.groupListId
        var gla = this.groupListAll

        for (var i = 0; i < gli.length; i++) {
          var id1 = gli[i]
          for (var j = 0; j < gla.length; j++) {
            var id2 = gla[j].id
            if (id1 == id2) {
              this.groupList.push(gla[j].groupName)
            }//end if
          }//end for
        }//end for
      },
      selectUser () {
        this.userListAll = []
        this.user_visible = true
        this.$axiosInstance({
          method: 'post',
          url: '/sys/user/read/list',
          data: {pageSize: 10000, pageNum: 1}
        }).then((resp) => {
          this.userListAll = resp.data.data.list
          this.user_visible = true
        })
      },
      clearUserList () {
        for (var i = 0; i < this.userListAll.length; i++) {
          this.userListAll[i].isSelected = false
        }
      },
      ensureUser () {this.user_visible = false},
      setUser () {

      },
      loadUser (item, callback) {
        setTimeout(() => {
          const data = [
            {
              title: 'children',
              loading: false,
              children: []
            },
            {
              title: 'children_nochild'
            }
          ]
          callback(data)
        }, 1000)
      },
    },

    mounted () {
      this.init()
      this.uploadList = this.$refs.upload.fileList
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