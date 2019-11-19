<style lang="less">
    @import '../../styles/common.less';
    @import '../system/components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
            <Card>
                <row><h2 style="margin-bottom:20px">名片管理</h2></row>
                <Row>
                    <Col span="22">
                    <span>处理状态：</span>
                    <Select v-model="searchParams.cardStatus" filterable style="width:150px">
                        <Option v-for="item in card_status" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                    <span @click="search" style="margin: 0 10px;"><Button type="primary"
                                                                          icon="search">搜索</Button></span>
                    <Button @click="searchCancel" type="ghost">清空条件</Button>
                    </Col>
                    <!--<Col span="2" class="">-->
                    <!--<span @click="addRecordBtn"><Button type="success"> 处理 </Button></span>-->
                    <!--</Col>-->
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

            <Form ref="record" :model="record" label-position="right" :label-width="100" style="margin-top:20px;"
                  :rules="formValidate">
                <table style="line-height: 32px;">
                    <tr>
                        <td width="100px" style="text-align: right;">姓名：</td>
                        <td width="170px">{{record.cardName}}</td>
                        <td width="100px" style="text-align: right;">姓名(en)：</td>
                        <td width="220px">{{record.cardNameEn}}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">部门：</td>
                        <td>{{record.cardDepartment}}</td>
                        <td style="text-align: right;">部门(en)：</td>
                        <td>{{record.cardDepartmentEn}}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">行业：</td>
                        <td>{{record.cardProfession}}</td>
                        <td style="text-align: right;">行业(en)：</td>
                        <td>{{record.cardProfessionEn}}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">职务：</td>
                        <td>{{record.cardPost}}</td>
                        <td style="text-align: right;">职务(en)：</td>
                        <td>{{record.cardPostEn}}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">手机号：</td>
                        <td>{{record.cardMobile}}</td>
                        <td style="text-align: right;">邮箱：</td>
                        <td>{{record.cardEmail}}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">电话：</td>
                        <td>{{record.cardPhone}}</td>
                        <td style="text-align: right;">传真：</td>
                        <td>{{record.cardFax}}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">地址：</td>
                        <td colspan="3">{{record.cardAddress}}</td>
                    </tr>
                    <tr>
                        <td style="text-align: right;">数量：</td>
                        <td>{{record.cardQuantity}}</td>
                    </tr>
                </table>
                <FormItem label="处理状态" prop="cardStatus">
                    <Select :disabled="saveLoading" v-model="record.cardStatus" style="width:150px">
                        <Option v-for="item in card_status" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">处理</Button>
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
        t_spinShow: true,
        formModal: false,
        pageList: {},
        searchParams: {},
        userParams: {},
        allDicts: {},
        card_status: [],
        //编辑
        saveLoading: false,
        editMode: '',
        record: {},
        formValidate: {
          cardStatus: [
            {required: true, message: '请选择状态', trigger: 'change'}
          ],
        },

        //列表
        columns1: [
          {key: 'userName', title: '申请人'}, {key: 'cardName', title: '名片姓名'}, {
            key: 'cardDepartment',
            title: '名片部门'
          }, {key: 'cardQuantity', title: '申请数量'}, {key: 'cardStatusText', title: '当前状态'}, {
            key: 'createTime',
            title: '申请时间'
          },
          {
            key: 'action',
            title: '操作',
            width: 200,
            align: 'center',
            render: (h, params) => {
              var v = false
              if (params.row.cardStatus == 'FINISHED' || params.row.cardStatus == 'FINISHED2') {
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
                      this.initDetail(params.row.id)
                      this.formModal = true
                    }
                  }
                }, '处理')
              ])
            }
          }
        ],
        // card_status: [
        //   {value: 'WAITING', label: '待处理'}, {value: 'PROCESSING', label: '制卡中'}, {value: 'FINISHED', label: '制卡完成'}
        // ],
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
        //   url: '/sys/event/read/dics'
        // }).then((resp) => {
        //   this.allDicts = resp.data.dicts.REQUESTURI
        // })

        this.search()
      },
      search () {
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goCardApply/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.card_status = resp.data.dicts.CARD_STATUS
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
          url: '/pr/goCardApply/read/detail',
          data: param
        }).then((resp) => {
          this.card_status = resp.data.dicts.CARD_STATUS
          this.record = resp.data.data
        })
      },
      handleSubmit () {
        let self = this
        this.$refs['record'].validate((valid) => {
          if (valid) {
            this.saveLoading = true
            if (this.editMode === 'edit') {
              this.updateRecord()
            } else if (this.editMode === 'add') {
            }
          }
        })
      },
      addRecord () {
        let self = this
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goCardApply/add',
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
          url: '/pr/goCardApply/update',
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
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goCardApply/delete',
          data: param
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
