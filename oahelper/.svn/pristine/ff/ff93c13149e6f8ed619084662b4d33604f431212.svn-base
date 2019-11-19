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
                        <Col span="21">
                            <Input v-model="searchParams.keyword" placeholder="请输入企业名称，企业简介，领导简介搜索..." style="width: 300px"/>
                            <span @click="userSearch" style="margin: 0 10px;"><Button type="primary" icon="search">搜索</Button></span>
                            <!--<Button @click="searchCancel" type="ghost">取消</Button>-->
                        </Col>
                        <Col span="3" class="" v-if="showAddBtn">
                            <span @click="addRecordBtn"><Button type="success"> 添加传播要素 </Button></span>
                        </Col>
                    </Row>
                    <Row class="margin-top-10 " style="display: none;">
                        <!--<Table :columns="columns1" :data="pageList.list" size="small"></Table>-->
                    </Row>
                    <Row class="margin-top-10 " v-if="pageList.list && pageList.list.length==0">
                        <Card>
                            <p style="text-align: center">暂无数据</p>
                        </Card>
                    </Row>
                    <Row class="margin-top-10 " v-for="item in pageList.list">
                        <Card>
                            <p slot="title" style="height:28px;line-height: 28px;">
                                {{item.companyName}}
                                <Tooltip content="编辑" v-if="userInfo.deptId == item.createUserDept">
                                    <Button type="ghost" icon="edit" shape="circle" size="small" @click="updateClick(item.id)"></Button>
                                </Tooltip>
                                <Tooltip content="删除" v-if="userInfo.deptId == item.createUserDept">
                                    <Button type="ghost" icon="android-delete" shape="circle" size="small" @click="deleteClick(item.id)"></Button>
                                </Tooltip>
                            </p>
                            <p style="text-indent:30px;"><span style="font-weight:bold">企业介绍：</span>{{item.companyAbstract}}</p>
                            <p style="text-indent:30px;"><span style="font-weight:bold">领导介绍：</span>{{item.leaderAbstract}}</p>
                            <p style="text-indent:30px; margin-top: 15px;">
                                <span v-if="item.viFile" style="font-weight:bold"><a target="_blank"  :href="item.viFile">VI使用规范下载</a></span>
                                <span v-if="item.introFile" style="font-weight:bold"><a target="_blank"  :href="item.introFile">公司简介下载</a></span>
                            </p>
                            <p style="text-align: right"><span style="font-weight:bold">提交人：</span>{{item.createByName}}
                                <!--<span style="font-weight:bold">审定人：</span>{{item.auditedName}}-->
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

        <Modal v-model="formModal"  :title="editMode=='add'?'新增':'编辑'" :mask-closable="false" width="800">
            <Form ref="record" :model="record" label-position="right" :label-width="100" :rules="formValidate">

                <FormItem class="margin-bottom-10" label="企业名称" prop="companyName">
                    <Input :rows="5" :disabled="saveLoading" v-model="record.companyName" placeholder="请输入企业名称"
                                                                     style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="企业简介" prop="companyAbstract">
                    <Input :rows="5" :disabled="saveLoading" type="textarea" v-model="record.companyAbstract" placeholder="请输入企业简介"
                                                                     style="width: 600px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="领导简介" prop="leaderAbstract">
                    <Input :rows="5" :disabled="saveLoading" type="textarea"  v-model="record.leaderAbstract" placeholder="请输入领导简介"
                                                                       style="width: 600px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="VI介绍" prop="viFile">
                    <Upload action="/uploadfile/toLocal" :show-upload-list="false" :on-success="uploadViSucc" :on-error="uploadFail" >
                        <Button type="ghost" icon="ios-cloud-upload-outline">上传</Button>
                    </Upload>
                    <a target="_blank" v-if="record.viFile" :href="record.viFile">VI使用规范下载</a>
                    <Input v-show="false" v-model="record.viFile"/>
                </FormItem>

                <FormItem class="margin-bottom-10" label="企业简介" prop="viFile">
                    <Upload action="/uploadfile/toLocal" :show-upload-list="false" :on-success="uploadIntroSucc" :on-error="uploadFail" >
                        <Button type="ghost" icon="ios-cloud-upload-outline">上传</Button>
                    </Upload>
                    <a target="_blank" v-if="record.introFile" :href="record.introFile">企业简介下载</a>
                    <Input v-show="false" v-model="record.introFile"/>
                </FormItem>

                <!--<FormItem class="margin-bottom-10" label="提交人"><Input :disabled="saveLoading" v-model="record.presentBy" placeholder="请输入提交人"-->
                                                                      <!--style="width: 300px"></Input></FormItem>-->
                <!--<FormItem class="margin-bottom-10" label="提交人UID"><Input :disabled="saveLoading" v-model="record.presentUid" placeholder="请输入提交UID"-->
                                                                       <!--style="width: 300px"></Input></FormItem>-->
                <!--<FormItem class="margin-bottom-10" label="审定人"><Input :disabled="saveLoading" v-model="record.auditedName" placeholder="请输入审定人"-->
                                                                     <!--style="width: 300px"></Input></FormItem>-->
                <!--<FormItem class="margin-bottom-10" label="审定人UID"><Input :disabled="saveLoading" v-model="record.auditedUid" placeholder="请输入审定人UID"-->
                                                                         <!--style="width: 300px"></Input></FormItem>-->
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
        name: 'leader-manage',
        data () {
            return {
                //
                delModal:false,
                delId:'',
                //编辑
                saveLoading:false,
                formModal: false,
                editMode: '',
                record: {},
                formValidate: {
                    companyName: [
                        { required: true, message: '请输入公司名称', trigger: 'blur' },
                      {type: 'string', max: 60, message: '不能超过60个字符', trigger: 'blur'}
                    ],
                    companyAbstract: [
                              { required: true, message: '请输入企业简介', trigger: 'blur' },
                    ],
                    leaderAbstract: [
                             { required: true, message: '请输入领导简介', trigger: 'blur' },
                    ],
                  viFile: [
                             { required: true, message: '请上传VI文件', trigger: 'blur' },
                    ],
                },
                //列表
                pageList: {},
                searchParams: {},
//                columns1: [
//                    {key: 'companyName', title: '企业名称', width: 100,ellipsis:true},
//                    {key: 'companyAbstract', title: '企业简介', width: 300,ellipsis:true},
//                    {key: 'leaderAbstract', title: '领导简介', width: 300,ellipsis:true},
//                    {key: 'presentBy', title: '提交人', width: 100},
//                    {key: 'presentUid', title: '提交人UID', width: 100},
//                    {key: 'auditedName', title: '审定人', width: 100},
//                    {key: 'auditedUid', title: '审定人UID', width: 100},
//                    {
//                        key: 'action',
//                        title: '操作',
//                        align: 'center',
//                        width: 160,
//                        render: (h, params) => {
//                            return h('div', [
//                                h('Button', {
//                                    props: {
//                                        type: 'primary',
//                                        size: 'small'
//                                    },
//                                    style: {
//                                        marginRight: '5px'
//                                    },
//                                    on: {
//                                        click: () => {
//                                            this.initDetail(params.row.id);
//                                            this.formModal = true;
//                                        }
//                                    }
//                                }, '编辑'),
//                                h('Button', {
//                                    props: {
//                                        type: 'warning',
//                                        size: 'small'
//                                    },
//                                    on: {
//                                        click: () => {
//                                            this.delModal = true;
//                                            this.delId=params.row.id;
//                                        }
//                                    }
//                                }, '删除')
//                            ]);
//                        }
//                    }
//                ]
              userInfo:null,
              showAddBtn:false,
            }
        },
        methods: {
            init () {
                this.searchParams.pageSize = 10;
                this.searchParams.pageNum = 1;
                this.search();

              let userInfo = JSON.parse(localStorage.userInfo);
              this.userInfo = userInfo;
              let authMenuIds = JSON.parse(localStorage.menus);
              for(var i=0;i<authMenuIds.length;i++){
                if(authMenuIds[i] == '21'){
                  this.showAddBtn = true;
                }
              }
            },
            search () {
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prLeader/read/list',
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
                    url: '/pr/prLeader/read/detail',
                    data: param
                }).then((resp) => {
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
                    console.log(valid)
                    if (valid) {
                        this.saveLoading = true;
                        if (this.editMode === 'edit') {
                            this.updateRecord();
                        } else if (this.editMode === 'add') {
                            this.addRecord();
                        }
                    }
                })
            },
            addRecord(){
                let self = this;
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prLeader/add',
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
            updateRecord(){
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prLeader/update',
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
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prLeader/delete',
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
          uploadViSucc (res, file) {
            this.record.viFile = res.files[0];
          },
          uploadIntroSucc (res, file) {
            this.record.introFile = res.files[0];
          },
          uploadFail (){
            this.$Notice.info({
              title:'上传文件失败'
            });
          },
            removeEmpty(obj) {
                Object.keys(obj).forEach(function(key) {
                    (obj[key] && typeof obj[key] === 'object') && removeEmpty(obj[key]) ||
                    (obj[key] === undefined || obj[key] === null) && delete obj[key]
                });
                return obj;
            }
        },
        mounted () {
            this.init();
        }
    };
</script>
