<style lang="less">
    @import '../../styles/common.less';
    @import './components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
            <Card>
                <Row>
                    <Col span="22">
                    <Input v-model="searchParams.keyword" placeholder="请输入姓名，联系方式，账户搜索..." style="width: 200px"/>
                    <span @click="userSearch" style="margin: 0 10px;"><Button type="primary"
                                                                              icon="search">搜索</Button></span>
                    <Button @click="searchCancel" type="ghost">取消</Button>
                    </Col>
                    <Col span="2">
                    <span @click="addRecordBtn"><Button type="success"> 添加账户 </Button></span>
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
        <Modal v-model="formModal"
               :title="editMode=='add'?'新增':'编辑'"
               :mask-closable="false">
            <Form ref="record" :model="record" label-position="right" :label-width="100" :rules="formValidate">
                <FormItem class="margin-bottom-10" label="所属机构" prop="deptId">
                    <Select :disabled="saveLoading" v-model="record.deptId" style="width:300px">
                        <Option v-for="(val,key) in deptTypes" :value="key" :key="key">
                            {{ val }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem class="margin-bottom-10" label="姓名" prop="userName"><Input :disabled="saveLoading"
                                                                                     v-model="record.userName"
                                                                                     placeholder="请输入姓名"
                                                                                     style="width: 300px"></Input>
                </FormItem>
                <FormItem class="margin-bottom-10" label="账户" prop="account"><Input :disabled="saveLoading"
                                                                                    v-model="record.account"
                                                                                    placeholder="请输入账户"
                                                                                    style="width: 300px"></Input>
                </FormItem>
                <FormItem class="margin-bottom-10" label="密码" prop="password"><Input type="password"
                                                                                     :disabled="saveLoading"
                                                                                     v-model="record.password"
                                                                                     placeholder="请输入密码"
                                                                                     style="width: 300px"></Input>
                </FormItem>
                <FormItem class="margin-bottom-10" label="联系电话" prop="phone"><Input :disabled="saveLoading"
                                                                                    v-model="record.phone"
                                                                                    placeholder="请输入联系电话"
                                                                                    style="width: 300px"></Input>
                </FormItem>
                <FormItem class="margin-bottom-10" label="微信OpenID" prop="yqAccount"><Input :disabled="saveLoading"
                                                                                        v-model="record.yqAccount"
                                                                                        placeholder="请输入微信OpenID"
                                                                                        style="width: 300px"></Input>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
            </div>
        </Modal>
        <Modal
                v-model="formModal1"
                title="用户角色设置"
                @on-ok="setRole">
            <Scroll height="200">
                <Row v-for="option in roles">
                    <Checkbox v-model="option.isSelected" :value="option.isSelected" :key="option.roleId">
                        {{option.roleName}}
                    </Checkbox>
                </Row>
            </Scroll>
        </Modal>
    </div>
</template>

<script>
  export default {
    name: 'account-manage',
    data () {
      return {
        pageList: {},
        // 编辑
        formModal: false,
        editMode: '',
        record: {},
        deptTypes: {},
        saveLoading: false,
        formValidate: {
          account: [
            {required: true, message: '请输入用户姓名', trigger: 'blur'},
            {type: 'string', max: 16, message: '不能超过16个字符', trigger: 'blur'}
          ],
          userName: [
            {required: true, message: '请输入姓名', trigger: 'blur'},
            {type: 'string', max: 10, message: '不能超过10个字符', trigger: 'blur'}
          ],
          password: [
            {type: 'string', min: 6, max: 16, message: '需要6-16个字符', trigger: 'blur'}
          ],
          phone: [
            {type: 'string', max: 16, message: '不能超过16个字符', trigger: 'blur'}
          ],
          yqAccount: [
            {type: 'string', max: 32, message: '不能超过32个字符', trigger: 'blur'}
          ],
          yqPwd: [
            {type: 'string', max: 16, message: '不能超过16个字符', trigger: 'blur'}
          ],

        },
        searchParams: {},
        columns1: [
          {key: 'account', title: '账户'},
          {key: 'userName', title: '姓名'},
          {key: 'deptName', title: '所属机构'},
          {key: 'phone', title: '联系电话'},
          {
            key: 'locked', title: '冻结', render (h, params) {
              if (params.row.locked === true) {
                return '是'
              } else {
                return '否'
              }
            }
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
                      this.formModal = true
                    }
                  }
                }, '编辑'),
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
                      this.initRoleDetail(params.row.id)
                      this.formModal1 = true
                    }
                  }
                }, '角色设置'),
                h('Button', {
                  props: {
                    type: 'warning',
                    size: 'small'
                  },
                  on: {
                    click: () => {
                      this.lock(params.index)
                    }
                  }
                }, this.lockBtnText(params.row.locked))
              ])
            }
          }
        ],
        formModal1: false,
        roles: [],
        userId: ''
      }
    },
    methods: {
      init () {
        this.searchParams.pageSize = 10
        this.searchParams.pageNum = 1
        this.search()
      },
      initDetail (id) {
        this.record = {}// 表单置空
        if (id === null || id === '') {
          this.editMode = 'add'
        } else {
          this.editMode = 'edit'
        }
        var param = {}
        param.id = id
        this.$axiosInstance({
          method: 'post',
          url: '/sys/user/read/detail',
          data: param
        }).then((resp) => {
          this.deptTypes = resp.data.dicts.DEPTTYPE
          if (this.editMode === 'edit') {
            this.record = resp.data.data
          }
        })
      },
      initRoleDetail (id) {
        this.userId = id
        this.roles = []
        if (id === null || id === '') {
          this.editMode = 'add'
        } else {
          this.editMode = 'edit'
        }
        var param = {}
        param.id = id
        this.$axiosInstance({
          method: 'post',
          url: '/sys/user/read/role',
          data: param
        }).then((resp) => {
          this.roles = resp.data.dicts
          for (let i = 0; i < this.roles.length; i++) {
            if (this.roles[i].isSelected === '1') {
              this.roles[i].isSelected = true
            } else {
              this.roles[i].isSelected = false
            }
          }

        })
      },
      search () {
        this.$axiosInstance({
          method: 'post',
          url: '/sys/user/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.pageList = resp.data.data
        })
      },
      userSearch () {
        this.searchParams.pageNum = 1
        this.search()
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
      lockBtnText (locked) {
        return locked ? '解冻' : '冻结'
      },
      lock (index) {
        this.$axiosInstance({
          method: 'post',
          url: '/sys/user/lock',
          data: {
            id: this.pageList.list[index].id,
            lock: this.pageList.list[index].locked ? '0' : '1'
          }
        }).then((resp) => {
          this.search()
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
      addRecordBtn () {
        this.initDetail('')
        this.formModal = true
      },
      addRecord () {
        // let self = this;
        this.$axiosInstance({
          method: 'post',
          url: '/sys/user/add',
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
          url: '/sys/user/update',
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
      goBackList () {
        this.formModal = false
        this.search()
      },
      cancel () {
        this.formModal = false
        this.$Message.info('返回列表页面')
      },
      setRole () {
        var param = {}
        param.id = this.userId
        param.rloe = []
        for (let i = 0; i < this.roles.length; i++) {
          if (this.roles[i].isSelected === true) {
            param.rloe.push(this.roles[i].roleId)
          }
        }
        this.$axiosInstance({
          method: 'post',
          url: '/sys/user/role/add',
          data: param
        }).then((resp) => {
          this.$Message.info(resp.data.msg)
        })
      },
      removeEmpty (obj) {
        Object.keys(obj).forEach(function (key) {
          (obj[key] && typeof obj[key] === 'object') && this.removeEmpty(obj[key]) ||
          (obj[key] === undefined || obj[key] === null) && delete obj[key]
        })
        return obj
      }
    },
    mounted () {
      this.init()
    }
  }
</script>
