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
                    <row><h2 style="margin-bottom:20px">文件库</h2></row>
                    <Col span="22">
                    <span>文件名：</span>
                    <Input v-model="searchParams.fileName" placeholder="文件名" style="width: 200px"/>
                    <span>文件分类：</span>
                    <Select v-model="searchParams.fileCatalog" style="width:150px">
                        <Option v-for="item in file_catalog" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                    <span @click="search" style="margin: 0 10px;"><Button type="primary"
                                                                          icon="search">搜索</Button></span>
                    <Button @click="searchCancel" type="ghost">清空条件</Button>
                    </Col>

                    <Col span="2" class="">
                    <span @click="addRecordBtn"><Button type="success"> 上传新文件 </Button></span>
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
                <FormItem label="文件名" prop="fileName">
                    <Input v-model="record.fileName" placeholder=""></Input>
                </FormItem>

                <Row>
                    <Col span="8">
                    <FormItem label="文件分类" prop="fileCatalog">
                        <Select v-model="record.fileCatalog" style="width:150px">
                            <Option v-for="item in file_catalog" :value="item.id" :key="item.id">{{ item.text }}
                            </Option>
                        </Select></FormItem>
                    </Col>
                    <Col span="14">
                    <FormItem label="排序权重">
                        <Tooltip placement="top-start" content="权重范围1~10，权重越大，排序越靠前">
                            <InputNumber value="1" :max="10" :min="1" v-model="record.fileWeight"></InputNumber>
                        </Tooltip>
                    </FormItem>
                    </Col>
                </Row>

                <FormItem label="文件描述">
                    <Input v-model="record.fileDescription" type="textarea" :autosize="{minRows: 4,maxRows: 4}"maxlength="150"
                           placeholder=""></Input>
                </FormItem>
                <FormItem label="文件路径">
                    <Row>
                        <Col span="4">
                        <Upload ref="upload" :show-upload-list="true" :on-success="handleSuccess"
                                :before-upload="clearFiles"
                                action="pr/goUpload/fileArchive">
                            <Button icon="ios-cloud-upload-outline">上传文件</Button>
                        </Upload>
                        </Col>
                        <Col span="4">
                        <a v-if="record.filePath" :href="record.filePath">下载查看</a>
                        </Col>
                    </Row>
                </FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
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
        //编辑
        saveLoading: false,
        editMode: '',
        record: {
          fileWeight: 1
        },
        uploadFile: {},
        formValidate: {
          fileName: [
            {required: true, message: '请输入文件名', trigger: 'blur'}
          ],
          fileCatalog: [
            {required: true, message: '请选择文件类型', trigger: 'blur'}
          ],
          filePath: [
            {required: true, message: '请上传文件', trigger: 'blur'}
          ]
        },
        //列表

        columns1: [
          {
            key: 'fileName',
            title: '文件名',
            render: (h, params) => {
              return h('div', [
                h('a', {
                  domProps: {
                    href: params.row.filePath,
                  },
                  style: {
                    marginRight: '5px'
                  },
                  on: {}
                }, params.row.fileName)])

            }
          },
          {
            key: 'fileCatalogText',
            title: '文件分类'
          },
          {
            key: 'fileWeight',
            title: '权重'
          },
          {
            key: 'createTime',
            title: '创建时间'
          },
          {
            key: 'action',
            title: '操作',
            align: 'center',
            render: (h, params) => {
              var name = ''
              var ic = ''
              var func = new function () {

              }
              if (params.row.fileWeight == 10) {
                name = '取消置顶'
                ic = 'arrow-down-c'
                func = this.down
              } else {
                name = '置顶'
                ic = 'arrow-up-c'
                func = this.up
              }
              return h('div', [
                h('Button', {
                  props: {
                    type: 'primary',
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
                }, '编辑'),
                h('Button', {
                    props: {
                      type: 'success',
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
                ),
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
        file_catalog: [],

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
          url: '/pr/goFileArchive/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.file_catalog = resp.data.dicts.FILE_CATALOG
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
          url: '/pr/goFileArchive/read/detail',
          data: param
        }).then((resp) => {
          //this.PrDatatypes = resp.data.dicts.PrDatatypes;
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
      handleSuccess (res, file) {
        // console.log(file)
        file.url = res.files[0].url.replace(/\\/g, '/')
        //console.log(file)
        //file.url = 'https://o5wwk8baw.qnssl.com/7eb99afb9d5f317c912f08b5212fd69a/avatar'
        file.name = res.files[0].name

        this.uploadFile.url = file.url
        this.uploadFile.name = file.name
        this.record.filePath = file.url
      },
      clearFiles () {
        this.$refs['upload'].clearFiles()
      },
      addRecord () {

        if (JSON.stringify(this.uploadFile) == '{}') {
          this.$Message.info('请上传文件')
        } else {
          this.record.filePath = this.uploadFile.url
          console.log(this.record.fileWeight)
          this.$axiosInstance({
            method: 'post',
            url: '/pr/goFileArchive/add',
            data: this.removeEmpty(this.record)
          }).then((resp) => {
            this.saveLoading = false
            if (resp.data.httpCode === 200) {
              this.goBackList()
            } else {
              this.$Message.info(resp.data.msg)
            }
          })
        }
      },
      updateRecord () {
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goFileArchive/update',
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
              url: '/pr/goFileArchive/delete',
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
      //置顶
      up (row) {
        this.updateWeight(row, 10)
      },
      //取消置顶
      down (row) {
        this.updateWeight(row, 1)
      },
      updateWeight (row, num) {
        var w = row
        w.fileWeight = num
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goFileArchive/update',
          data: this.removeEmpty(w)
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
      this.uploadList = this.$refs.upload.fileList
    }
  }
</script>
