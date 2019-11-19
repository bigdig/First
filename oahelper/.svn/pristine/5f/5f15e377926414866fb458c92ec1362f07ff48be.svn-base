<style lang="less">
    @import '../../styles/common.less';
    @import '../system/components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
            <Card>
                <row><h2 style="margin-bottom:20px">用户分组</h2></row>
                <Row>
                    <Col span="22">
                    <span>分组名称：</span>
                    <Input v-model="searchParams.groupName" placeholder="" style="width: 200px"/>
                    <span>分组类型</span>
                    <Select v-model="searchParams.groupType" filterable style="width:150px">
                        <Option v-for="item in group_type" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                    <span @click="search" style="margin: 0 10px;"><Button type="primary"
                                                                          icon="search">搜索</Button></span>
                    <Button @click="searchCancel" type="ghost">清空条件</Button>
                    </Col>

                    <Col span="2" class="">
                    <span @click="addRecordBtn"><Button type="success"> 新建分组 </Button></span>
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

        <Modal width="600" v-model="formModal" :title="editMode=='add'?'新增':'编辑'" :mask-closable="false">

            <Form ref="record" :model="record" label-position="right" :label-width="100" style="margin-top:20px;"
                  :rules="formValidate">
                <FormItem label="分组名" prop="groupName">
                    <Input v-model="record.groupName"></Input>
                </FormItem>
                <FormItem label="分组类型">
                    <Row>
                        <Col span="8">
                        <FormItem prop="groupType">
                            <Select v-model="record.groupType" style="width:150px">
                                <Option v-for="item in group_type" :value="item.id" :key="item.id">{{ item.text }}
                                </Option>
                            </Select>
                        </FormItem>
                        </Col>
                        <Col v-if="record.groupType=='DEPARTMENT_GROUP'" span="6" style="text-align: center">
                        选择部门</Col>
                        <Col span="10">
                        <FormItem v-if="record.groupType=='DEPARTMENT_GROUP'" prop="groupType">
                            <Select v-model="record.departmentId"
                                    style="width:195px">
                                <Option v-for="item in deptListAll" :value="item.id"
                                        :key="item.id">{{ item.deptName }}
                                </Option>
                            </Select>
                        </FormItem>
                        </Col>
                    </Row>

                </FormItem>
                <FormItem label="备注" props="groupRemark">
                    <Input v-model="record.groupRemark" placeholder=""></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
            </div>
        </Modal>

        <Modal width="800" title="选择用户" v-model="user_visible" class-name="vertical-center-modal">
            <Card>
                <Row class="margin-top-10">
                    <Col span="6" class="padding-left-10">
                    <p>
                        <Icon type="navicon-round"></Icon>
                        部门列表
                    </p>
                    <div style="height:290px;overflow: hidden;margin-top: 20px;">
                        <ul class="iview-admin-draggable-list">
                            <li v-for="(item,index) in deptList" :key="item.id" class="notwrap"
                                :data-index="index" @click="changeDept(item.id)">
                                {{ item.deptName }}
                            </li>
                        </ul>
                    </div>
                    </Col>

                    <Col span="18" style="padding-left: 10px;">

                    <p>
                        <Icon type="android-contacts"></Icon>
                        所有成员列表
                    </p>
                    <div style="height:290px;overflow: hidden;margin-top: 20px;">
                        <Table ref="groupUserTable" height="290" border size="small" :columns="userCols"
                               :data="userListAll"></Table>
                        <Spin size="large" fix v-if="spinShow"></Spin>
                    </div>
                    </Col>
                </Row>

            </Card>

        </Modal>

    </div>
</template>

<script>

  export default {
    components: {},
    name: 'searchable-table',
    data () {
      return {
        spinShow: false,
        formModal: false,
        pageList: {},
        searchParams: {},
        userParams: {},
        allDicts: {},
        deptListAll: [],
        userListAll: [],
        groupUsers: [],
        nowGroup: {},
        user_visible: false,
        //编辑
        saveLoading: false,
        editMode: '',
        record: {},
        formValidate: {
          groupName: [
            {required: true, message: '请填写分组名称', trigger: 'blur'}
          ],
          groupType: [
            {required: true, message: '请选择分组类别', trigger: 'blur'}
          ],
        },

        //列表
        columns1: [
          {key: 'groupName', title: '分组名称'}, {key: 'groupTypeText', title: '分组类型'}, {
            key: 'groupRemark',
            title: '备注'
          }, {key: 'createTime', title: '创建时间'},
          {
            key: 'action',
            title: '操作',
            width: 200,
            align: 'center',
            render: (h, params) => {
              var v = false
              if (params.row.groupType == 'DEPARTMENT_GROUP') {
                v = true
              }
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
                    size: 'small',
                    disabled: v
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {
                    click: () => {
                      this.selectUser(params.row)
                    }
                  }
                }, '修改成员'),
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
                      this.formModal = true
                    }
                  }
                }, '编辑'),
                h('Button', {
                  props: {
                    type: 'error',
                    size: 'small'
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
        group_type: [],
        deptList: [],
        userCols: [
          {
            title: '用户名',
            key: 'userName'
          },
          // {
          //   title: '所在部门',
          //   key: 'deptName'
          // },
          {
            title: '操作',
            key: 'action',
            sortable: true,
            // width: 80,
            render: (h, params) => {
              params.row.isContained = false
              for (var i = 0; i < this.groupUsers.length; i++) {
                if (this.groupUsers[i].userId === params.row.id) {
                  params.row.isContained = true
                  params.row.userId = params.row.id
                }

              }
              if (params.row.isContained) {
                return h('div', [
                  h('Button', {
                    props: {
                      type: 'error',
                      size: 'small'
                    }, on: {
                      click: () => {
                        this.removeUser(params)
                      }
                    }
                  }, '移除')
                ])
              } else {
                return h('div', [
                  h('Button', {
                    props: {
                      type: 'success',
                      size: 'small'
                    }, on: {
                      click: () => {
                        this.addUser(params)
                      }
                    }
                  }, '添加')])
              }

            }
          }
        ],
        // yes_no: [
        //   {value: 'YES', label: '是'}, {value: 'NO', label: '否'}
        // ]
      }
    },
    methods: {
      init () {
        this.searchParams.pageSize = 10
        this.searchParams.pageNum = 1
        // this.$axiosInstance({
        //   method: 'post',
        //   url: '/pr/goGroup/read/dics'
        // }).then((resp) => {
        //   this.allDicts = resp.data.dicts.REQUESTURI
        // })
        this.search()
      },
      search () {
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goGroup/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.group_type = resp.data.dicts.GROUP_TYPE
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
      selectUser (group) {
        this.user_visible = true
        this.groupUsers = []
        if (group.groupType == 'DEPARTMENT_GROUP') {
          this.$Modal.info({
            content: '自定义分组才可以修改成员！'
          })
        } else {
          this.$axiosInstance({
            method: 'post',
            url: '/sys/dept/read/allDept',
            data: {}
          }).then((resp) => {
            this.deptList = resp.data.data
          })

          this.spinShow = true
          this.nowGroup = group

          var s_param = {}
          s_param.groupId = group.id
          this.$axiosInstance({
            method: 'post',
            url: '/pr/goUserGroup/selectByCondition',
            data: s_param
          }).then((resp) => {
            this.groupUsers = resp.data.data
            this.userListAll = []
            this.$axiosInstance({
              method: 'post',
              url: '/pr/goInform/selectUserByCondition',
              data: {}
            }).then((resp) => {
              this.userListAll = resp.data.data
              this.spinShow = false
            })
          })

        }
      },
      changeDept (deptId) {
        this.spinShow = true
        this.userListAll = []
        var s_params = {}
        s_params.deptId = deptId
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goInform/selectUserByCondition',
          data: s_params
        }).then((resp) => {
          var tmp = resp.data.data
          for (var i = 0; i < tmp.length; i++) {
            if (tmp[i].deptId === deptId) {
              this.userListAll.push(tmp[i])
            }
          }
          this.spinShow = false
        })
      },
      ensureUser () {this.user_visible = false},

      removeUser (params) {
        var pa = params.row
        pa.userId = pa.id
        pa.groupId = this.nowGroup.id
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goUserGroup/delete',
          data: params.row
        }).then((resp) => {
          if (resp.data.httpCode === 200) {
            params.row.isContained = false
            this.selectUser(this.nowGroup)
            this.$Modal.info({
              content: '成功移除'
            })
          }
        })
      },
      addUser (params) {
        var pa = params.row
        pa.userId = pa.id
        pa.groupId = this.nowGroup.id
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goUserGroup/add',
          data: params.row
        }).then((resp) => {
          if (resp.data.httpCode === 200) {
            params.row.isContained = true
            this.selectUser(this.nowGroup)
            this.$Modal.info({
              content: '成功添加'
            })
          }
        })
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
          url: '/pr/goGroup/read/detail',
          data: param
        }).then((resp) => {
          //this.PrDatatypes = resp.data.dicts.PrDatatypes;
          this.record = resp.data.data
        })

        this.$axiosInstance({
          method: 'post',
          url: '/sys/dept/read/allDept',
          data: {}
        }).then((resp) => {
          this.deptListAll = resp.data.data
        })
      },
      handleSubmit () {
        this.$refs['record'].validate((valid) => {
          if (valid) {
            this.saveLoading = true
            if (this.editMode === 'edit') {
              this.updateRecord()
            } else if (this.editMode === 'add') {
              this.addRecord()
            }
          }
        })
      },
      addRecord () {
        if (this.record.groupType == 'CUSTOM_GROUP') {
          this.record.departmentId = ''
        }
        let self = this
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goGroup/add',
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
        if (this.record.groupType == 'CUSTOM_GROUP') {
          this.record.departmentId = ''
        }
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goGroup/update',
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
      delRecord (delId) {
        this.$Modal.confirm({
          content: '确定要删除这条记录吗？',
          onOk: () => {
            var param = {}
            param.id = delId
            this.$axiosInstance({
              method: 'post',
              url: '/pr/goGroup/delete',
              data: param
            }).then((resp) => {
              this.saveLoading = false
              if (resp.data.httpCode === 200) {
                this.$Message.info('删除成功！')
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
      goBackList () {
        this.formModal = false
        this.delModal = false
        this.search()
      },
      cancel () {
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

    },
    mounted () {
      this.init()
    }
  }
</script>
<style>

    .iview-admin-draggable-list {
        height: 100%;
    }

    .iview-admin-draggable-list li {
        padding: 9px;
        border: 1px solid #e7ebee;
        border-radius: 3px;
        margin-bottom: 5px;
        cursor: pointer;
        position: relative;
        transition: all .2s;
    }

    .iview-admin-draggable-list li:hover {
        color: #87b4ee;
        border-color: #87b4ee;
        transition: all .2s;
    }

    .iview-admin-draggable-delete {
        height: 100%;
        position: absolute;
        right: -8px;
        top: 0px;
        display: none;
    }

    .iview-admin-draggable-list li:hover .iview-admin-draggable-delete {
        display: block;
    }

    .placeholder-style {
        display: block !important;
        color: transparent;
        border-style: dashed !important;
    }

    .delte-item-animation {
        opacity: 0;
        transition: all .2s;
    }

    .iview-admin-draggable-list {
        overflow: auto
    }

</style>
