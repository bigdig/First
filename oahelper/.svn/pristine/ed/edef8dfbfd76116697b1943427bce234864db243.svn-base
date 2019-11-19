<style lang="less">
    @import '../../styles/common.less';
    @import '../system/components/table.less';
</style>

<template>
    <div>

        <Row :gutter="10">
            <Col span="24">
            <Card>
                <Row>
                    <row><h2 style="margin-bottom:20px">通知管理</h2></row>
                    <Col span="22">
                    <span>标题：</span>
                    <Input v-model="searchParams.noticeTitle" placeholder="标题" style="width: 200px"/>
                    <span>所属分类：</span>
                    <Select v-model="searchParams.noticeType" filterable style="width:150px">
                        <Option v-for="item in notice_type" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                    <span>是否发送消息：</span>
                    <Select v-model="searchParams.isInform" style="width:100px">
                        <Option v-for="item in yes_no" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                    <span @click="search" style="margin: 0 10px;"><Button type="primary"
                                                                          icon="search">搜索</Button></span>
                    <Button @click="searchCancel" type="ghost">清空条件</Button>
                    </Col>

                    <Col span="2" class="">
                    <span @click="addRecordBtn"><Button type="success"> 新建通知 </Button></span>
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
                <FormItem label="标题" prop="noticeTitle">
                    <Input v-model="record.noticeTitle" placeholder=""></Input>
                </FormItem>

                <Row>
                    <Col span="3">
                    <FormItem label="所属分类" prop="noticeType">
                        <Select v-model="record.noticeType" style="width:120px">
                            <Option v-for="item in notice_type" :value="item.id" :key="item.id">{{ item.text }}
                            </Option>
                        </Select>
                    </FormItem>
                    </Col>
                    <Col span="5">
                    <span style="margin-left: 50px"></span>
                    </Col>
                    <Col span="3">
                    <FormItem label="是否发送消息" prop="isInform">
                        <Select v-model="record.isInform" style="width:80px">
                            <Option v-for="item in yes_no" :value="item.id" :key="item.id">{{ item.text }}
                            </Option>
                        </Select>
                    </FormItem>
                    </Col>

                </Row>
                <FormItem label="链接地址：" prop="noticeUrl">
                    <Input v-model="record.noticeUrl" placeholder="" type="textarea"
                           :autosize="{minRows: 1,maxRows:3}"></Input>
                </FormItem>

                <FormItem v-if="record.isInform=='YES'" label="发送到组">
                    <div v-model="groupList">
                        <span v-for="item in groupList" style="margin-right: 15px;">{{item}}</span>
                    </div>
                    <Button type="primary" @click="selectGroup" style="width:100px;">选择用户分组</Button>
                    <Button type="error" @click="clearGroupList">清空</Button>
                </FormItem>
                <Form-item label="内容" prop="noticeContent">
                    <Input v-model="record.noticeContent" type="textarea" :autosize="{minRows: 10,maxRows: 10}"
                           placeholder=""></Input>
                </Form-item>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
            </div>
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


        <Modal width="800" title="查看通知" v-model="viewModal" class-name="vertical-center-modal">
            <Row style="line-height: 35px;">
                <Col span="3" style="text-align: right;font-weight: bold;">
                通知标题</Col>
                <Col span="21" style="padding-left: 20px;">
                {{record.noticeTitle}}</Col>
            </Row>
            <Row style="line-height: 35px;">
                <Col span="3" style="text-align: right;font-weight: bold;">
                通知类型</Col>
                <Col span="5" style="padding-left: 20px;">
                {{record.noticeTypeText}}</Col>
                <Col span="3" style="text-align: right;font-weight: bold;">
                是否发消息</Col>
                <Col span="3" style="padding-left: 20px;">
                {{record.isInformText}}</Col>
                <Col span="3" style="text-align: right;font-weight: bold;">
                发送时间</Col>
                <Col span="6" style="padding-left: 20px;">
                {{record.createTime}}</Col>
            </Row>
            <Row v-if="record.isInform=='YES'" style="line-height: 35px;">
                <Col span="3" style="text-align: right;font-weight: bold;">
                已发送到分组</Col>
                <Col span="21" style="padding-left: 20px;">
                {{record.informGroup}}</Col>
            </Row>
            <Row style="padding-top:10px;line-height: 35px;">
                <Col span="3" style="text-align: right;font-weight: bold;">链接地址</Col>
                <Col span="21" style="padding-top:7px;padding-left: 20px;line-height: 20px;">
                    <p>{{record.noticeUrl}}</p>
                </Col>
            </Row>
            <Row style="padding-top:10px;line-height: 35px;">
                <Col span="3" style="text-align: right;font-weight: bold;">
                通知内容</Col>
                <Col span="21" style="padding-top:7px;padding-left: 20px;line-height: 20px;">
                <p>{{record.noticeContent}}</p>
                </Col>
            </Row>

        </Modal>

    </div>
</template>

<script>
  export default {
    components: {},
    name: 'searchable-table',
    data () {
      return {
        t_spinShow: true,
        formModal: false,
        viewModal: false,
        pageList: {},
        searchParams: {},
        userParams: {},
        allDicts: {},
        groupList: [],
        groupListAll: [],
        groupListId: [],
        group_visible: false,
        //编辑
        saveLoading: false,
        editMode: '',
        record: {},
        notice_type: [],
        yes_no: [],
        formValidate: {
          noticeTitle: [
            {required: true, message: '请输入通知标题', trigger: 'blur'}
          ],
          noticeType: [
            {required: true, message: '请选择通知类型', trigger: 'blur'}
          ],
          isInform: [
            {required: true, message: '请选择是否发送消息', trigger: 'blur'}
          ],
          noticeContent: [
            {required: true, message: '请输入通知内容', trigger: 'blur'}
          ]
        },

        //列表

        columns1: [
          {
            key: 'noticeTitle',
            title: '标题'
          },
          {
            key: 'noticeTypeText',
            title: '所属分类'
          },
          {
            key: 'noticeUrl',
            title: '链接地址'
          },
          {
            key: 'isInformText',
            title: '是否发送消息'
          },
          {
            key: 'createTime',
            title: '创建时间'
          },
          {
            key: 'action',
            title: '操作',
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
                }, '查看')
              ])
            }
          }
        ]
      }
    },
    methods: {
      init () {
        this.searchParams.pageSize = 10
        this.searchParams.pageNum = 1
        // this.$axiosInstance({
        //   method: 'post',
        //   url: '/sys/event/read/dics'
        // }).then((resp) => {
        //   this.allDicts = resp.data.dicts.REQUESTURI
        // })
        this.search()
      },
      search () {
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goNotice/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.notice_type = resp.data.dicts.NOTICE_TYPE
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
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goNotice/read/detail',
          data: param
        }).then((resp) => {
          //this.PrDatatypes = resp.data.dicts.PrDatatypes;
          this.record = resp.data.data;
          var t = this.record.informGroup;
          this.record.informGroup = t.replace(/,/g, '，').substring(1, t.length)
        })
      },

      clearGroupList () { this.groupList = [], this.groupListId = [] },
      selectGroup () {
        this.group_visible = true
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goGroup/selectByCondition',
          data: {}
        }).then((resp) => {
          this.groupListAll = resp.data.data
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
      handleSubmit () {
        this.$refs['record'].validate((valid) => {
          if (valid) {
            this.spinShow = true
            if (this.editMode === 'edit') {
              //this.updateRecord()
            } else if (this.editMode === 'add') {
              this.addRecord()
            }
          }
        })
      },
      addRecord () {

        var informGroup = ''
        var informGroupId = ''

        for (var i = 0; i < this.groupList.length; i++) {
          informGroup = informGroup + ',' + this.groupList[i]
        }
        for (var i = 0; i < this.groupListId.length; i++) {
          informGroupId = informGroupId + ',' + this.groupListId[i]
        }

        this.record.informGroup = informGroup
        this.record.informGroupId = informGroupId

        let self = this
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goNotice/add',
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
        this.$axiosInstance({
          method: 'post',
          url: '/pr/prDatatype/update',
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
        this.formModal = false
        this.$Message.info('返回列表页面')
      }
      ,
      removeEmpty (obj) {
        Object.keys(obj).forEach(function (key) {
          (obj[key] && typeof obj[key] === 'object') && this.removeEmpty(obj[key]) ||
          (obj[key] === undefined || obj[key] === null) && delete obj[key]
        })
        return obj
      },
    },
    mounted () {
      this.init()
    }
  }
</script>
