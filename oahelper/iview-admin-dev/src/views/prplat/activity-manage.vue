<style lang="less">
    @import '../../styles/common.less';
    @import '../system/components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
            <Card>
                <row><h2 style="margin-bottom:20px">活动征集</h2></row>
                <Row>
                    <Col span="22">
                    <span>活动名：</span>
                    <Input v-model="searchParams.voteGroupName" placeholder="活动名" style="width: 200px"/>
                    <span>发布状态：</span>
                    <Select v-model="searchParams.isOpen" style="width:100px">
                        <Option v-for="item in is_open" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                    <!--<span>是否已结束：</span>-->
                    <!--<Select v-model="searchParams.isClosed" style="width:100px">-->
                    <!--<Option v-for="item in yes_no" :value="item.id" :key="item.id">{{ item.text }}-->
                    <!--</Option>-->
                    <!--</Select>-->
                    <span @click="search" style="margin: 0 10px;"><Button type="primary"
                                                                          icon="search">搜索</Button></span>
                    <Button @click="searchCancel" type="ghost">清空条件</Button>
                    </Col>

                    <Col span="2" class="">
                    <span @click="addRecordBtn"><Button type="success"> 发起活动 </Button></span>
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
                <FormItem label="活动名" prop="voteGroupName">
                    <Input v-model="record.voteGroupName" placeholder=""></Input>
                </FormItem>

                <Row>
                    <Col span="8">
                    <FormItem label="报名开始时间">
                        <DatePicker v-model="record.startTime" type="date" placeholder="报名开始时间"></DatePicker>
                    </FormItem>
                    </Col>
                    <Col span="8">
                    <FormItem label="报名截止时间">
                        <DatePicker v-model="record.endTime" type="date" placeholder="报名截止时间"></DatePicker>
                    </FormItem>
                    </Col>
                </Row>
                <FormItem label="活动描述">
                    <Input v-model="record.voteGroupDescription" type="textarea" :autosize="{minRows: 4,maxRows: 4}"
                           placeholder=""></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
            </div>
        </Modal>

        <Modal width="800" v-model="voteModal" title="编辑活动内容" :mask-closable="false">
            <div>
                <Row>
                    <Col span="16">
                    <h2 v-model="record" style="margin-bottom: 10px;">{{record.voteGroupName}}</h2>
                    </Col>
                    <Col span="8">
                    <span style="float: right;" @click="editVote(0)">
                        <Button type="success"> 新增活动内容 </Button>
                    </span>
                    </Col>
                </Row>

            </div>
            <div style="height:390px;overflow: hidden;">
                <Table height="390" border size="small" :columns="voteColumn"
                       :data="voteList"></Table>
            </div>
            <div slot="footer">
                <Button type="text" @click="cancelVoteModal">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="saveVoteModal">保存</Button>
            </div>
        </Modal>

        <Modal width="600" v-model="voteItemModal" title="编辑活动项" :mask-closable="false">
            <div>
                <Row>
                    <Col span="16">
                    <h2 v-model="vote" style="margin-bottom: 10px;">{{vote.voteName}}</h2>
                    </Col>
                    <Col span="8">
                    <span style="float: right;" @click="editVoteItem(0)">
                        <Button type="success"> 新增活动项 </Button>
                    </span>
                    </Col>
                </Row>
            </div>
            <div style="height:290px;overflow: hidden;">
                <Table height="290" border size="small" :columns="voteItemColumn"
                       :data="voteItemList"></Table>
            </div>
            <div slot="footer">
                <Button type="text" @click="cancelVoteItemModal">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="saveVoteItemModal">保存</Button>
            </div>
        </Modal>

        <Modal width="400" v-model="voteEditModal" title="活动内容" :mask-closable="false">
            <Form ref="vote" :model="vote" label-position="right" :label-width="90" :rules="voteValidate">
                <FormItem label="名称" prop="voteName">
                    <Input v-model="vote.voteName" placeholder=""></Input>
                </FormItem>
                <FormItem label="是否可填写" prop="isCustomizable">
                    <Select v-model="vote.isCustomizable">
                        <Option v-for="item in yes_no" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="是否多选" prop="isMulti">
                    <Select v-model="vote.isMulti">
                        <Option v-for="item in yes_no" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="排序号" prop="sortNo">
                    <InputNumber value="1" :max="100" :min="1" v-model="vote.sortNo"></InputNumber>
                </FormItem>
                <FormItem label="描述">
                    <Input v-model="vote.voteDescription" type="textarea" :autosize="{minRows: 4,maxRows: 4}"
                           placeholder=""></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancelVoteEditModal">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="saveVoteEditModal">保存</Button>
            </div>
        </Modal>


        <Modal width="400" v-model="voteItemEditModal" title="活动项" :mask-closable="false">
            <Form ref="voteItem" :model="voteItem" label-position="right" :label-width="90" :rules="voteItemValidate">
                <FormItem label="名称" prop="voteItemContent">
                    <Input v-model="voteItem.voteItemContent" placeholder=""></Input>
                </FormItem>
                <FormItem label="排序号" prop="sortNo">
                    <InputNumber value="1" :max="100" :min="1" v-model="voteItem.sortNo"></InputNumber>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancelVoteItemEditModal">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="saveVoteItemEditModal">保存</Button>
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
        formModal: false,
        voteModal: false,
        voteEditModal: false,
        voteItemModal: false,
        voteItemEditModal: false,
        pageList: {},
        searchParams: {isActivity: 'YES'},
        userParams: {},
        allDicts: {},
        //编辑
        saveLoading: false,
        editMode: '',
        record: {},

        vote: {},
        voteItem: {},

        voteList: [],
        voteItemList: [],

        formValidate: {
          voteGroupName: [
            {required: true, message: '请输入活动名称', trigger: 'blur'}
          ]
        },
        voteValidate: {
          voteName: [
            {required: true, message: '请输入名称', trigger: 'blur'}
          ],
          isCustomizable: [
            {required: true, message: '请选择', trigger: 'blur'}
          ],
          isMulti: [
            {required: true, message: '请选择', trigger: 'blur'}
          ]
        },
        voteItemValidate: {
          voteItemContent: [
            {required: true, message: '请输入选项内容', trigger: 'blur'}
          ],
        },

        //列表
        columns1: [
          {
            key: 'voteGroupName', title: '活动名'
          },
          {
            key: 'startTime', title: '报名开始时间',
            render: (h, params) => {
              var t = new Date(params.row.startTime)
              var n = t.getFullYear() + '年' + (t.getMonth() + 1) + '月' + t.getDate() + '日'
              return h('div', [
                h('div', {}, n)
              ])
            }
          },
          {
            key: 'endTime', title: '报名截止时间',
            render: (h, params) => {
              var t = new Date(params.row.endTime)
              var n = t.getFullYear() + '年' + (t.getMonth() + 1) + '月' + t.getDate() + '日'
              return h('div', [
                h('div', {}, n)
              ])
            }
          },
          {
            key: 'isOpenText', title: '发布状态',
            render: (h, params) => {
              var name = ''
              var ic = ''
              var tp = ''
              var func = new function () {

              }
              if (params.row.isOpen == 'CLOSED') {
                name = '发布'
                tp = 'success'
                ic = 'forward'
                func = this.open
              } else {
                name = '撤回'
                tp = 'error'
                ic = 'reply'
                func = this.close
              }

              return h('div', [h('span', {
                style: {
                  marginRight: '10px'
                }
              }, params.row.isOpenText),
                h('Button', {
                    props: {
                      type: tp,
                      size: 'small',
                      icon: ic
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        func(params.row)
                      }
                    }
                  },
                  name
                )
              ])
            }
          },
          {
            key: 'isClosed',
            title: '是否已结束',
            render: (h, params) => {
              var t = new Date(new Date(params.row.endTime).getTime() + 24 * 60 * 60 * 1000)
              var now = new Date()
              var n = t > now ? '未结束' : '已结束'
              return h('div', [
                h('div', {}, n)
              ])
            }
          },
          {
            key: 'createTime', title: '创建时间'
          },
          {
            key: 'createTime', title: '报名情况', width: 100,
            render: (h, params) => {
              return h('div', [
                h('a', {
                  domProps: {
                    href: '/pr/goVoteGroup/export?id=' + params.row.id,
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {}
                }, '导出结果')])

            }
          },
          {
            key: 'action',
            title: '操作',
            width: 230,
            render: (h, params) => {

              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small',
                    icon: 'settings'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.initVoteModal(params.row)
                    }
                  }
                }, '配置'),
                h('Button', {
                  props: {
                    type: 'ghost',
                    size: 'small',
                    icon: 'edit'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.initDetail(params.row.id)
                      this.formModal = true
                    }
                  }
                }, '修改'),
                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small',
                    icon: 'ios-trash'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.delRecord(params.row.id)
                    }
                  }
                }, '删除')
              ])
            }
          }
        ],
        voteColumn: [
          {
            key: 'voteName', title: '名称', width: 300
          },
          {
            key: 'isCustomizableText', title: '可填写'
          },
          {
            key: 'isMultiText', title: '多选'
          },
          {
            key: 'sortNo', title: '排序号'
          },
          {
            key: 'action', title: '操作', width: 230,
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small',
                    icon: 'settings'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.initVoteItemModal(params.row)
                    }
                  }
                }, '配置'),
                h('Button', {
                  props: {
                    type: 'ghost',
                    size: 'small',
                    icon: 'edit'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.vote = params.row
                      this.editVote(1)
                    }
                  }
                }, '修改'),

                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small',
                    icon: 'ios-trash'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.deleteVote(params.row.id)
                    }
                  }
                }, '删除')
              ])
            }
          }
        ],
        voteItemColumn: [
          {
            key: 'voteItemContent', title: '选项名'
          },
          {
            key: 'sortNo', title: '排序号', width: 100
          },
          {
            key: 'action', title: '操作', width: 180,
            render: (h, params) => {
              return h('div', [
                h('Button', {
                  props: {
                    type: 'ghost',
                    size: 'small',
                    icon: 'edit'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.voteItem = params.row
                      this.editVoteItem(1)
                    }
                  }
                }, '修改'),

                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small',
                    icon: 'ios-trash'
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.deleteVoteItem(params.row.id)
                    }
                  }
                }, '删除')
              ])
            }
          }
        ],
        yes_no: [],
        is_open: []
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
          url: '/pr/goVoteGroup/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.yes_no = resp.data.dicts.YES_NO
          this.is_open = resp.data.dicts.OPEN_CLOSE
          this.pageList = resp.data.data
        })
      },
      searchCancel () {
        this.searchParams = {isActivity: 'YES'}
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
          url: '/pr/goVoteGroup/read/detail',
          data: param
        }).then((resp) => {
          //this.PrDatatypes = resp.data.dicts.PrDatatypes;
          this.record = resp.data.data
        })
      },
      handleSubmit () {
        if (this.record.startTime == null || this.record.startTime === '') {
          this.$Message.info('请设置报名开始时间')
        } else if (this.record.endTime == null || this.record.endTime === '') {
          this.$Message.info('请设置报名截止时间')
        } else {
          var tmp = this.record.startTime
          var year = tmp.getFullYear()
          var month = tmp.getMonth() + 1
          if (month < 10) {
            month = '0' + month
          }
          var day = tmp.getDate()
          this.record.startTime = year + '-' + month + '-' + day

          tmp = this.record.endTime
          year = tmp.getFullYear()
          month = tmp.getMonth() + 1
          if (month < 10) {
            month = '0' + month
          }
          var day = tmp.getDate()
          this.record.endTime = year + '-' + month + '-' + day

          this.$refs['record'].validate((valid) => {
            if (valid) {
              if (this.editMode === 'edit') {
                this.updateRecord()
              } else if (this.editMode === 'add') {

                this.addRecord()
              }
            }
          })
        }
      },
      addRecord () {
        this.record.isOpen = 'CLOSED'
        this.record.isActivity = 'YES'

        this.$axiosInstance({
          method: 'post',
          url: '/pr/goVoteGroup/add',
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
          url: '/pr/goVoteGroup/update',
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
      open (row) {
        this.open_close(row, 'OPEN')
      },
      close (row) {
        this.open_close(row, 'CLOSED')
      },
      open_close (row, status) {

        var w = row
        w.isOpen = status
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goVoteGroup/update',
          data: this.removeEmpty(w)
        }).then((resp) => {
          this.saveLoading = false
          if (resp.data.httpCode === 200) {
            this.goBackList()
          } else {
            this.goBackList()
            this.$Message.info(resp.data.msg)
          }
        })
      },
      delRecord (id) {

        this.$Modal.confirm({
          content: '确定要删除这条记录吗？',
          onOk: () => {
            var param = {}
            param.id = id
            this.$axiosInstance({
              method: 'post',
              url: '/pr/goVoteGroup/delete',
              data: param
            }).then((resp) => {
              this.saveLoading = false
              if (resp.data.httpCode === 200) {
                this.goBackList()
                this.$Message.info('删除成功！')
              } else {
                this.$Message.info(resp.data.msg)
              }
            })
          },
          onCancel: () => {
          }
        })
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

      editVote (n) {
        this.voteEditModal = true
        if (n == 0) {
          this.vote = {}
          this.vote.voteGroupId = this.record.id
        } else {
          var voteId = this.vote.id
          if (voteId != null && voteId != '') {
            var param = {}
            param.id = voteId
            this.$axiosInstance({
              method: 'post',
              url: '/pr/goVote/read/detail',
              data: param
            }).then((resp) => {
              this.vote = resp.data.data
            })
          }
        }
      },

      editVoteItem (n) {
        if (n == 0) {
          this.voteItem = {}
          this.voteItem.voteId = this.vote.id
        }
        this.voteItemEditModal = true
      },

      cancelVoteModal () {
        this.voteModal = false
      },
      cancelVoteItemModal () {
        this.voteItemModal = false
      },
      cancelVoteEditModal () {
        this.voteEditModal = false
      },
      cancelVoteItemEditModal () {
        this.voteItemEditModal = false
      },
      saveVoteModal () {
        this.cancelVoteModal()
      },
      saveVoteItemModal () {
        this.cancelVoteItemModal()
      },

      saveVoteEditModal () {
        this.$refs['vote'].validate((valid) => {
          if (valid) {

            var url = ''
            var id = this.vote.id
            var sortNo = this.vote.sortNo
            if (sortNo == null || sortNo === '') {
              this.vote.sortNo = 1
            }

            if (id == null || id === '') {
              url = '/pr/goVote/add'
            } else {
              url = '/pr/goVote/update'
            }

            this.$axiosInstance({
              method: 'post',
              url: url,
              data: this.removeEmpty(this.vote)
            }).then((resp) => {
              if (resp.data.httpCode === 200) {
                this.voteEditModal = false
                this.initVoteModal(this.record)
              } else {
                this.$Message.info(resp.data.msg)
              }
            })
          }
        })

      },

      saveVoteItemEditModal () {
        this.$refs['voteItem'].validate((valid) => {
          if (valid) {

            var url = ''
            var id = this.voteItem.id
            var sortNo = this.voteItem.sortNo
            if (sortNo == null || sortNo === '') {
              this.voteItem.sortNo = 1
            }

            if (id == null || id === '') {
              url = '/pr/goVoteItem/add'
            } else {
              url = '/pr/goVoteItem/update'
            }

            this.$axiosInstance({
              method: 'post',
              url: url,
              data: this.removeEmpty(this.voteItem)
            }).then((resp) => {
              if (resp.data.httpCode === 200) {
                this.voteItemEditModal = false
                this.initVoteItemModal(this.vote)
              } else {
                this.$Message.info(resp.data.msg)
              }
            })
          }
        })

      },

      initVoteModal (row) {
        this.voteModal = true
        this.record = row
        this.vote = {}
        this.vote.voteGroupId = row.id
        // this.vote.pageSize = 1000
        // this.vote.pageNum = 1
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goVote/selectByCondition',
          data: this.vote
        }).then((resp) => {
          this.yes_no = resp.data.dicts.YES_NO
          this.voteList = resp.data.data
        })

      },

      initVoteItemModal (row) {
        this.voteItemModal = true
        this.vote = row
        this.voteItem = {}
        this.voteItem.voteId = row.id
        // this.voteItem.pageSize = 1000
        // this.voteItem.pageNum = 1
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goVoteItem/selectByCondition',
          data: this.voteItem
        }).then((resp) => {
          this.voteItemList = resp.data.data
        })
      },

      deleteVote (id) {
        this.$Modal.confirm({
          content: '确定要删除这条记录吗？',
          onOk: () => {
            var param = {}
            param.id = id
            this.$axiosInstance({
              method: 'post',
              url: '/pr/goVote/delete',
              data: param
            }).then((resp) => {
              this.saveLoading = false
              if (resp.data.httpCode === 200) {
                this.initVoteModal(this.record)
                this.$Message.info('删除成功！')
              } else {
                this.$Message.info(resp.data.msg)
              }
            })
          },
          onCancel: () => {
          }
        })
      },
      // getAllVotes (id) {
      //   var params = {}
      //   params.id = id
      //   this.$axiosInstance({
      //     method: 'post',
      //     url: '/pr/goVoteGroup/export',
      //     data: params,
      //     responseType: 'blob'
      //   }).then((resp) => {
      //     this.download(resp)
      //   })
      // },
      // download (data) {
      //   if (!data) {
      //     return
      //   }
      //   let url = window.URL.createObjectURL(new Blob([data]))
      //   let link = document.createElement('a')
      //   link.style.display = 'none'
      //   link.href = url
      //   link.setAttribute('download', 'vote_detail.xlsx')
      //
      //   document.body.appendChild(link)
      //   link.click()
      // },

      deleteVoteItem (id) {
        this.$Modal.confirm({
          content: '确定要删除这条记录吗？',
          onOk: () => {
            var param = {}
            param.id = id
            this.$axiosInstance({
              method: 'post',
              url: '/pr/goVoteItem/delete',
              data: param
            }).then((resp) => {
              this.saveLoading = false
              if (resp.data.httpCode === 200) {
                this.initVoteItemModal(this.vote)
                this.$Message.info('删除成功！')
              } else {
                this.$Message.info(resp.data.msg)
              }
            })
          },
          onCancel: () => {
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
