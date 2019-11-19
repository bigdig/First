<style lang="less">
    @import '../../styles/common.less';
    @import '../../components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
                <Card>
                    <Row>
                        <Col span="22">
                            <Input v-model="searchParams.keyword" placeholder="请输入合作内容搜索..." style="width: 200px"/>
                            <span @click="userSearch" style="margin: 0 10px;"><Button type="primary" icon="search">搜索</Button></span>
                            <!--<Button @click="searchCancel" type="ghost">取消</Button>-->
                        </Col>
                        <Col span="2" class="">
                            <span @click="goBackBtn"><Button icon="arrow-left-a"> 返回 </Button></span>
                        </Col>
                    </Row>
                    <!--<Row class="margin-top-10 ">-->
                        <!--<Table :columns="columns1" :data="pageList.list" size="small"></Table>-->
                    <!--</Row>-->
                    <Row class="margin-top-10 " v-for="item in pageList.list">
                        <Card>
                            <p style="text-indent:30px;"><span style="font-weight:bold">合作内容：</span>{{item.coopContent}}</p>
                            <p style="text-align: right"><span style="font-weight:bold">
                                <Tooltip content="编辑" v-if="userInfo.id == item.createBy">
                                    <Button type="ghost" icon="edit" shape="circle" size="small" @click="updateClick(item.id)"></Button>
                                </Tooltip>
                                <Tooltip content="删除" v-if="userInfo.id == item.createBy">
                                    <Button type="ghost" icon="android-delete" shape="circle" size="small" @click="deleteClick(item.id)"></Button>
                                </Tooltip>
                                合作时间：</span>{{item.coopDate.substring(0,10)}}
                            </p>

                        </Card>
                    </Row>
                    <Row class="margin-top-10 ">
                        <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage"
                              @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                    </Row>
                </Card>
            </Col>
        </Row>

        <Modal v-model="formModal"  title="修改" :mask-closable="false" >
            <Form ref="record" :model="record" label-position="right" :label-width="100" :rules="formValidate">
                <FormItem class="margin-bottom-10" label="合作时间">
                    <DatePicker type="date" :disabled="saveLoading" v-model="record.coopDate" placeholder="请输入合作时间" format="yyyy-MM-dd" style="width: 200px"></DatePicker>

                </FormItem>
                <FormItem class="margin-bottom-10" label="合作内容" prop="coopContent">
                    <Input v-model="record.coopContent" placeholder="请输入合作内容" :disabled="saveLoading" style="width: 300px"></Input>
                </FormItem>
                <!--<FormItem class="margin-bottom-10" label="备注" style="width: 500px">-->
                    <!--<Input :disabled="saveLoading" v-model="record.remark" type="textarea" placeholder="备注"></Input>-->
                <!--</FormItem>-->

            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
            </div>
        </Modal>
        <!--删除Modal-->
        <Modal v-model="delModal" width="360">
            <p slot="header" style="color:#f60;text-align:center">
                <Icon type="information-circled"></Icon>
                <span>删除确认窗口</span>
            </p>
            <div style="text-align:center">
                <p>您正在删除该条记录.</p>
                <p>确认删除吗?</p>
            </div>
            <div slot="footer">
                <Button type="error" size="large" long :loading="saveLoading" @click="delRecord">删除</Button>
            </div>
        </Modal>

    </div>
</template>

<script>

    export default {
        name: 'media-manage',
        data () {
            return {
                coopId:null,
                //
                delModal:false,
                delId:'',
                //编辑
                saveLoading:false,
                formModal: false,
                editMode: '',
                record: {},
                formValidate: {
                    coopContent: [
                        { required: true, message: '请输入合作内容', trigger: 'blur' }
                    ]},
                //列表
                pageList: {},
                searchParams: {},
                columns1: [
                    // {key: 'coopId', title: '供应商编号', width: 100,ellipsis:true},
                    // {key: 'coopType', title: '合作类型', width: 100,ellipsis:true},
                    {key: 'coopDate', title: '合作时间', width: 200},
                    {key: 'coopContent', title: '合作内容'},
                    {
                        key: 'action',
                        title: '操作',
                        width: 150,
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
                                            this.initDetail(params.row.id);
                                            this.formModal = true;
                                        }
                                    }
                                }, '编辑'),
                                h('Button', {
                                    props: {
                                        type: 'warning',
                                        size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.delModal = true;
                                            this.delId=params.row.id;
                                        }
                                    }
                                }, '删除')
                            ]);
                        }
                    }
                ],
              userInfo:null

            }
        },
        methods: {
            init () {
                this.coopId = this.$route.params.coopId;
                this.searchParams.pageSize = 10;
                this.searchParams.pageNum = 1;
                this.search();
                let userInfo = JSON.parse(localStorage.userInfo);
                this.userInfo = userInfo;
            },
            search () {
                this.searchParams.coopId=this.coopId;
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prCoop/read/list',
                    data: this.searchParams
                }).then((resp) => {
                    this.pageList = resp.data.data;
                })
            },
            userSearch () {
                this.searchParams.pageNum = 1;
                this.search();
            },
            searchCancel () {
                this.searchParams = {};
                this.search();
            },
            changePage (currentPage) {
                this.searchParams.pageNum = currentPage;
                this.search();
            },
            changePageNum(pageSize){
                this.searchParams.pageSize = pageSize;
                this.search();
            },
            addRecordBtn () {
                this.initDetail('')
                this.formModal = true;
            },
            initDetail (id) {
                this.record={};//表单置空
                if (id === null || id === '') {
                    this.editMode = 'add';
                } else {
                    this.editMode = 'edit';
                }
                var param = {};
                param.id = id;
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prCoop/read/detail',
                    data: param
                }).then((resp) => {
                    this.PrDatatypes = resp.data.dicts.PrDatatypes;
                    this.record = resp.data.data;
                })
            },
          updateClick (id) {
            this.initDetail(id);
            this.formModal = true;
          },
          deleteClick (id) {
            this.delModal = true;
            this.delId=id;
          },
            handleSubmit () {
                this.$refs['record'].validate((valid) => {

                    if (valid) {
                        this.saveLoading = true;
                        if (this.editMode === 'edit') {
                            console.log('edit submit');
                            this.updateRecord();
                        }
                    }
                })
            },
            addRecord(){
                // let self = this;
                // this.$axiosInstance({
                //     method: 'post',
                //     url: '/pr/prCoop/add',
                //     data: this.removeEmpty(this.record)
                // }).then((resp) => {
                //     this.saveLoading = false;
                //     if (resp.data.httpCode === 200) {
                //         this.goBackList();
                //     }else{
                //         this.$Message.info(resp.data.msg);
                //     }
                // })
            },
            updateRecord(){
              console.log('updateRecord');
              console.log(this.record);
              this.record.coopDate = this.dateFormat(this.record.coopDate);
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prCoop/update',
                    data: this.removeEmpty(this.record)
                }).then((resp) => {
                    this.saveLoading = false;
                    if (resp.data.httpCode === 200) {
                        this.goBackList();
                    }else{
                        this.$Message.info(resp.data.msg);
                    }
                })
            },
            delRecord(){
                var param = {};
                param.id = this.delId;
                this.saveLoading = true;
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prCoop/delete',
                    data: param
                }).then((resp) => {
                    this.saveLoading = false;
                    if (resp.data.httpCode === 200) {
                        this.goBackList();
                    }else{
                        this.$Message.info(resp.data.msg);
                    }
                })
            },
            goBackList(){
                this.formModal = false;
                this.delModal = false;
                this.search();
            },
            cancel () {
                this.formModal = false;
                this.$Message.info('返回列表页面');
            },
              goBackBtn(){
                this.$router.go(-1)
              },
            removeEmpty(obj) {
                Object.keys(obj).forEach(function(key) {
                    (obj[key] && typeof obj[key] === 'object') && removeEmpty(obj[key]) ||
                    (obj[key] === undefined || obj[key] === null) && delete obj[key]
                });
                return obj;
            },
              dateFormat(val) {
                let year = val.getFullYear().toString();
                let month = val.getMonth() >= 9
                  ? (val.getMonth() + 1).toString()
                  : "0" + (val.getMonth() + 1);
                let date = val.getDate() >= 9
                  ? val.getDate().toString()
                  : "0" + val.getDate();
                return year + "-" + month + "-" + date;
              },
        },
        mounted () {
            this.init();
        }
    };
</script>
