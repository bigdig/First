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
                    <row><h2 style="margin-bottom:20px">流程提醒</h2></row>
                    <Col span="22">
                    <span>标题：</span>
                    <Input v-model="searchParams.remindTitle" placeholder="标题" style="width: 200px"/>
                    <span>所属类型：</span>
                    <Select v-model="searchParams.remindType" style="width:150px">
                        <Option v-for="item in remind_type" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                    <span>是否已发送通知：</span>
                    <Select v-model="searchParams.isInformed" style="width:100px">
                        <Option v-for="item in yes_no" :value="item.id" :key="item.id">{{ item.text }}</Option>
                    </Select>
                    <span @click="search" style="margin: 0 10px;"><Button type="primary"
                                                                          icon="search">搜索</Button></span>
                    <Button @click="searchCancel" type="ghost">清空条件</Button>
                    </Col>

                    <Col span="2" class="">
                    <span @click="addRecordBtn"><Button type="success"> 新建提醒 </Button></span>
                    </Col>
                </Row>
                <Row class="margin-top-10 ">
                    <Table :loading="saveLoading" stripe border :columns="columns1" :data="pageList.list"></Table>
                </Row>
                <Row class="margin-top-10 ">
                    <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage"
                          @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                </Row>
            </Card>
            </Col>
        </Row>

        <Modal width="600" v-model="formModal" :title="editMode=='add'?'新增':'编辑'" :mask-closable="false">
            <Form ref="record" :model="record" label-position="right" :label-width="100" :rules="formValidate">
                <FormItem label="标题" prop="remindTitle">
                    <Input v-model="record.remindTitle" placeholder=""></Input>
                </FormItem>
                <FormItem label="流程提醒类型" prop="remindType">
                    <Select v-model="record.remindType" style="width:150px">
                        <Option v-for="item in remind_type" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select><br/>请选择对应类型的文件上传，文件按照模板准备，否则将造成解析错误
                </FormItem>
                <FormItem v-if="record.remindType" label="上传文件">
                    <Row>
                        <Col span="16">
                        <Upload ref="uploadFile"
                                action="/pr/goRemind/add"
                                :data="record"
                                :before-upload="validateForm"
                                :on-success="handleSuccess"
                                :format="['xlsx','xls']">
                            <Button icon="ios-cloud-upload-outline" style="width:150px">上传文件</Button>
                        </Upload>
                        </Col>
                    </Row>
                </FormItem>
            </Form>
            <div slot="footer" style="color:red">
                选择上传文件即会提交相关数据
            </div>
        </Modal>


        <Modal width="1000" v-model="viewModal" title="查看详情" :mask-closable="false">
            <Row>
                <Table height="600" stripe border :columns="remindInformColumn" :data="remindInformList"></Table>
            </Row>
            <div slot="footer">
            </div>
        </Modal>
    </div>
</template>

<script>

  export default {
    components: {},
    name: 'searchable-table',
    data () {
      return {
        saveLoading: false,
        t_spinShow: true,
        formModal: false,
        viewModal: false,
        pageList: {},
        remindInformList: [],
        searchParams: {},
        userParams: {},
        allDicts: {},
        //编辑
        saveLoading: false,
        editMode: '',
        record: {},
        formValidate: {
          remindTitle: [
            {required: true, message: '请输入标题', trigger: 'blur'}
          ],
          remindType: [
            {required: true, message: '请选择类型', trigger: 'blur'}
          ]
        },

        remindInformColumn: [
          {
            key: 'remindTitle',
            title: '标题',
            width: 200
          },
          {
            key: 'userName',
            title: '被提醒人',
            width: 90
          },
          // {
          //   key: 'remindStatusText',
          //   title: '报销单状态',
          //   width: 100
          // },
          {
            key: 'remark',
            title: '流程状态',
            width: 100
          },
          {
            key: 'remindContent',
            title: '通知内容'
          },
          {
            key: 'createTime',
            title: '创建时间',
            width: 150
          },
        ],
        //列表
        columns1: [
          {
            key: 'remindTitle',
            title: '标题'
          },
          {
            key: 'remindTypeText',
            title: '所属类型'
          },
          {
            key: 'isInformedText',
            title: '是否已发送提醒',
            render: (h, params) => {

              if (params.row.isInformed == 'NO') {
                return h('div', [h('span', {
                  style: {
                    marginRight: '10px'
                  }
                }, params.row.isInformedText),
                  h('Button', {
                      props: {
                        type: 'success',
                        size: 'small',
                        icon: 'forward'
                      },
                      on: {
                        click: () => {
                          this.inform(params.row.id)
                        }
                      }
                    },
                    '发送提醒'
                  )
                ])
              } else {
                return h('div', [h('span', {}, params.row.isInformedText)
                ])
              }
            }
          },
          {
            key: 'createTime',
            title: '创建时间'
          },
          {
            key: 'action',
            title: '操作',
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
                      this.viewModal = true
                      this.viewRemindInform(params.row.id)
                    }
                  }
                }, '查看')
              ])
            }
          }
        ],
        remind_type: [],
        remind_status: [],
        yes_no: []
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
          url: '/pr/goRemind/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.remind_type = resp.data.dicts.REMIND_TYPE
          this.yes_no = resp.data.dicts.YES_NO
          this.pageList = resp.data.data
        })
      },

      viewRemindInform (id) {
        var params = {}
        // params.pageSize = 1000
        // params.pageNum = 1
        params.remindId = id
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goRemindInform/selectByCondition',
          data: params
        }).then((resp) => {
          this.remindInformList = resp.data.data
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
          url: '/pr/goRemind/read/detail',
          data: param
        }).then((resp) => {
          this.record = resp.data.data
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
        this.record.isInformed = 'NO'
        let self = this
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goRemind/add',
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
        this.formModal = false
        this.$Message.info('返回列表页面')
      },
      validateForm () {
        this.record.isInformed = 'NO'
        var v = false
        this.$refs['record'].validate((valid) => {
          v = valid
        })
        return v
      },
      handleSuccess (resp) {
        this.formModal = false
        this.$Message.info('处理成功')
        this.goBackList()
      },
      inform (id) {
        this.saveLoading = true
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goRemind/remind',
          data: {id: id}
        }).then((resp) => {
          this.saveLoading = false
          if (resp.data.httpCode === 200) {
            this.$Message.info('成功发送提醒')
            this.goBackList()
          } else {
            this.$Message.info(resp.data.msg)
          }
        })
      },
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
