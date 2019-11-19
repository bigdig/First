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
                    <span @click="exportResult"><Button type="success"> 导出查询结果 </Button></span>
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
                            <img :src="'/'+item.url">
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
                    <div v-model="userSelectedList">
                        <span v-for="item in userSelectedList" style="margin-right: 15px;">{{item.userName}}</span>
                    </div>
                    <Button type="primary" @click="selectUser" style="width:100px;">选择用户</Button>
                    <Button type="error" @click="clearUserSelectedList">清空</Button>
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
            <img :src="'/' + imgUrl + ''" v-if="visible" style="width: 100%;">
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

        <Modal width="700" title="选择用户" v-model="user_visible" class-name="vertical-center-modal">
            <Card>
                <Row>
                    <div v-model="userSelectedList" style="margin-bottom:15px;">
                        已选择人员：<span v-for="item in userSelectedList"
                                    style="margin-right: 15px;">{{item.userName}}</span>
                    </div>
                </Row>
                <Row>
                    <Col span="9" class="padding-left-10">

                    <p>
                        <Icon type="navicon-round"/>
                        人员分组信息
                    </p>
                    <div style="height: 320px;overflow-y: auto">
                        <Menu active-name="1" width="auto" @on-select="loadUsers">
                            <Spin size="large" fix v-if="spinShow1"></Spin>
                            <MenuItem :name="0">
                                <Icon type="ios-people"/>
                                所有人员
                            </MenuItem>
                            <Menu-group title="部门分组">
                                <MenuItem v-for="item in groupListAll" v-if="item.groupType==='DEPARTMENT_GROUP'"
                                          :name="item.id" :key="item.id">
                                    <Icon type="ios-people"/>
                                    {{item.groupName}}
                                </MenuItem>
                            </Menu-group>
                            <Menu-group title="自定义分组">
                                <MenuItem v-for="item in groupListAll" v-if="item.groupType==='CUSTOM_GROUP'"
                                          :name="item.id" :key="item.id">
                                    <Icon type="ios-people"/>
                                    {{item.groupName}}
                                </MenuItem>
                            </Menu-group>
                        </Menu>
                    </div>

                    </Col>

                    <Col span="15" style="padding-left: 10px;">

                    <p>
                        <Icon type="android-contacts"/>
                        成员列表
                    </p>
                    <Table height="330" border size="small" :columns="userCols" :data="userList"></Table>
                    <Spin size="large" fix v-if="spinShow"></Spin>
                    </Col>
                </Row>

            </Card>
        </Modal>


        <Modal width="800" title="查看消息" v-model="viewModal" class-name="vertical-center-modal">
            <Row style="line-height: 35px;">
                <Col span="3" style="text-align: right;font-weight: bold;">
                消息标题</Col>
                <Col span="21" style="padding-left: 20px;">
                {{record.informTitle}}</Col>
            </Row>
            <Row style="line-height: 35px;">
                <Col span="3" style="text-align: right;font-weight: bold;">
                消息来源</Col>
                <Col span="4" style="padding-left: 20px;">
                {{record.srcTypeText}}</Col>
                <Col span="3" style="text-align: right;font-weight: bold;">
                是否需要回应</Col>
                <Col span="3" style="padding-left: 20px;">
                {{record.isReplyText}}</Col>
                <Col span="3" style="text-align: right;font-weight: bold;">
                发送时间</Col>
                <Col span="6" style="padding-left: 20px;">
                {{record.createTime}}</Col>
            </Row>
            <Row style="line-height: 35px;">
                <Col span="3" style="text-align: right;font-weight: bold;">
                已发送到分组</Col>
                <Col span="21" style="padding-left: 20px;">
                {{record.informGroup}}</Col>
            </Row>

            <Row class="margin-top-10 ">
                <Col span="3" style="text-align: right;font-weight: bold;">
                已发送到个人</Col>
                <Col span="21" style="padding-left: 20px;">
                <Table border size="small" height="210" :columns="user_cols" :data="usersPageList"></Table>
                </Col>
            </Row>
            <Row style="padding-top:10px;line-height: 35px;">
                <Col span="3" style="text-align: right;font-weight: bold;">
                消息内容</Col>
                <Col span="21" style="padding-top:10px;padding-left: 20px;line-height: 20px;word-break:break-all;">
                <p>{{record.informContent}}</p>
                </Col>
            </Row>
        </Modal>
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
        spinShow: false,
        spinShow1: false,
        defaultList: [],
        imgUrl: '',
        visible: false,
        group_visible: false,
        user_visible: false,
        uploadList: [],
        groupList: [],
        groupListId: [],
        groupListAll: [],//所有的用户分组列表
        userGroupListAll: [],
        userList: [],
        userSelectedList: [],
        userListAll: [],
        // userList: [{
        //   title: '所有分组',
        //   loading: false,
        //   children: [{title: '部门分组', loading: false, children: []}, {title: '自定义分组', loading: false, children: []}]
        // }],
        loadingStatus: false,
        t_spinShow: true,
        formModal: false,
        viewModal: false,
        pageList: {},
        usersPageList: [],
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
        user_cols: [
          {key: 'userName', title: '用户名'}, {key: 'isRepliedText', title: '是否回应'}, {key: 'remark', title: '用户反馈'}
        ],
        userCols: [
          {title: '用户名', key: 'userName'},
          // {title: '标记', key: 'flag'},
          {
            title: '操作',
            key: 'action',
            // width: 80,
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: params.row.flag ? 'error' : 'success',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.handleClick(params)
                    }
                  }
                }, params.row.flag ? '移除' : '添加')
              ])
            }
          }
        ],
        columns1: [
          {key: 'informTitle', title: '消息标题'},
          {key: 'srcTypeText', title: '来源类型'},
          {key: 'isReplyText', title: '是否需要回应'},
          {key: 'createTime', title: '创建时间'},
          {key: 'remark', title: '备注'},
          {key: 'action', title: '操作',
          width: 200,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    this.initDetail(params.row.id)
                    this.viewModal = true
                  }
                }
              }, '查看'),
              h('Button', {
                props: {
                  type: 'warning',
                  size: 'small',
                  icon: 'forward'
                },
                style: {
                  marginRight: '5px',
                  display: params.row.srcType.indexOf('RMD_')==0?'inline':'none'
                },
                on: {
                  click: () => {
                    this.remind(params.row.id)
                  }
                }
              }, '再次发送提醒')
            ])
          }
        }],

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
      exportResult () {
        let params = '?export=1'
        if (this.searchParams.informTitle) {
          params = params + '&informTitle=' + this.searchParams.informTitle
        }
        if (this.searchParams.srcType) {
          params = params + '&srcType=' + this.searchParams.srcType
        }
        if (this.searchParams.isReply) {
          params = params + '&isReply=' + this.searchParams.isReply
        }
        let url = '/pr/goInform/export' + params
        let eurl = encodeURI(url)
        window.open(eurl)

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
        this.clearUserSelectedList()
        this.clearGroupList()
        this.$refs.upload.fileList.splice(0,this.$refs.upload.fileList.length)
        console.log(this.uploadList)
        this.record = {}//表单置空
        if (id === null || id === '') {
          this.editMode = 'add'
        } else {
          this.usersPageList = []
          var param = {}
          param.id = id
          this.$axiosInstance({
            method: 'post',
            url: '/pr/goInform/read/detail',
            data: param
          }).then((resp) => {
            this.record = resp.data.data
            var t = this.record.informGroup
            if (t) {this.record.informGroup = t.replace(/,/g, '，').substring(1, t.length)}
          })
          var param2 = {}
          param2.informId = id
          this.$axiosInstance({
            method: 'post',
            url: '/pr/goInformUser/selectByCondition',
            data: param2
          }).then((resp) => {
            this.usersPageList = resp.data.data
          })
        }

      },
      remind (id) {
        this.$Modal.confirm({
          content: '确定要再次发送提醒吗？',
          onOk: () => {
            let param = {}
            param.id = id
            this.$axiosInstance({
              method: 'post',
              url: '/pr/goInform/remind',
              data: param
            }).then((resp) => {
              if (resp.data.httpCode === 200) {
                this.$Message.info('再次发送提醒成功！')
                this.goBackList()
              } else {
                this.$Message.info(resp.data.msg)
              }
            })
          },
          onCancel: () => {
          }
        })
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
        for (var i = 0; i < this.userSelectedList.length; i++) {
          userId = userId + ',' + this.userSelectedList[i].id
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
        console.log(this.uploadList)
        return check
      },
      clearGroupList () {
        this.groupList = [], this.groupListId = []
      },
      selectGroup () {
        if (this.groupListAll.length == 0) {
          this.$axiosInstance({
            method: 'post',
            url: '/pr/goGroup/selectByCondition',
            data: {}
          }).then((resp) => {
            this.groupListAll = resp.data.data
          })
        }
        this.group_visible = true
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
        this.userList = []
        this.user_visible = true

        if (this.groupListAll.length == 0) {
          this.spinShow1 = true
          this.$axiosInstance({
            method: 'post',
            url: '/pr/goGroup/selectByCondition',
            data: {}
          }).then((resp) => {
            this.groupListAll = resp.data.data
            this.spinShow1 = false
          })
        }

        if (this.userListAll.length == 0) {
          this.spinShow = true
          this.$axiosInstance({
            method: 'post',
            url: '/pr/goInform/selectUserByCondition',
            data: {}
          }).then((resp) => {
            this.userListAll = resp.data.data
            this.spinShow = false
          })
        }

        if (this.userGroupListAll.length == 0) {
          this.$axiosInstance({
            method: 'post',
            url: '/pr/goUserGroup/selectByCondition',
            data: {}
          }).then((resp) => {
            this.userGroupListAll = resp.data.data
          })
        }
      },
      clearUserSelectedList () {
        this.userSelectedList = []
      },
      ensureUser () {this.user_visible = false},
      setUser () {

      },

      loadUsers (groupId) {
        this.userList = []

        if (groupId == 0) {
          for (var i = 0; i < this.userListAll.length; i++) {
            this.userList.push(this.userListAll[i])
          }
        } else {
          for (var k = 0; k < this.groupListAll.length; k++) {
            var gp = this.groupListAll[k]
            if (gp.id == groupId) {
              //找到分组信息
              if (gp.groupType === 'DEPARTMENT_GROUP') {//部门分组,按照deptId查找
                var deptId = gp.departmentId
                for (var i = 0; i < this.userListAll.length; i++) {
                  if (this.userListAll[i].deptId == deptId) {
                    this.userList.push(this.userListAll[i])
                  }
                }
              } else {//自定义分组,按照userId查找
                for (var j = 0; j < this.userListAll.length; j++) {
                  var user = this.userListAll[j]
                  for (var m = 0; m < this.userGroupListAll.length; m++) {
                    if (this.userGroupListAll[m].userId == user.id && this.userGroupListAll[m].groupId == groupId) {
                      this.userList.push(user)
                    }
                  }
                }
              }
              break
            }
          }
        }

        for (var n = 0; n < this.userList.length; n++) {
          var user = this.userList[n]
          user.flag = false
          for (var i = 0; i < this.userSelectedList.length; i++) {
            if (this.userSelectedList[i].id == user.id) {
              user.flag = true
              break
            }
          }
        }
        this.userList.sort(function (a, b) {
          if (a.flag != b.flag) {
            return a.flag ? 0 : 1
          }
          return 0
        })
      },
      handleClick (params) {
        params.row.flag = !params.row.flag
        if (params.row.flag) {
          this.userSelectedList.push(params.row)
        } else {
          for (var i = 0; i < this.userSelectedList.length; i++) {
            if (this.userSelectedList[i].id == params.row.id) {
              this.userSelectedList.splice(i, 1)
            }
          }
        }
      }
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