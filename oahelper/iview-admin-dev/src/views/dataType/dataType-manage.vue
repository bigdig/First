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
                            <!--<Input v-model="searchParams.keyword" placeholder="请输入姓名，联系方式，账户搜索..." style="width: 200px"/>-->
                            <!--<span @click="userSearch" style="margin: 0 10px;"><Button type="primary" icon="search">搜索</Button></span>-->
                            <!--<Button @click="searchCancel" type="ghost">取消</Button>-->
                        </Col>
                        <Col span="2" class="">
                            <span @click="addRecordBtn"><Button type="success"> 添加数据类型 </Button></span>
                        </Col>
                    </Row>
                    <Row class="margin-top-10 ">
                        <Table :columns="columns1" :data="pageList.list" size="small"></Table>
                    </Row>
                    <Row class="margin-top-10 ">
                        <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage"
                              @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                    </Row>
                </Card>
            </Col>
        </Row>

        <Modal v-model="formModal" :title="editMode=='add'?'新增':'编辑'" :mask-closable="false" >
            <Form ref="record" :model="record" label-position="right" :label-width="100" :rules="formValidate">
                <FormItem class="margin-bottom-10" label="服务类别"prop="dataType">
                <Select v-model="record.dataType" style="width:200px">
                    <Option v-for="item in dataType" :value="item.value" :key="item.value":disabled="saveLoading">{{ item.label }}</Option>
                </Select></FormItem>
                <FormItem class="margin-bottom-10" label="类别名称">
                    <Input :disabled="saveLoading" v-model="record.dataText" placeholder="请输入类别名称" style="width: 300px"></Input></FormItem>

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
                <Button type="error" size="large" long :loading="true" @click="delRecord">删除</Button>
            </div>
        </Modal>

    </div>
</template>

<script>

    export default {
        name: 'dataType',
        data () {
            return {
                //
                dataType: [
                    {
                        value: '1',
                        label: '媒体类别'
                    },
                    {
                        value: '2',
                        label: '供应商类别'
                    },
                    ],
                delModal:false,
                delId:'',
                //编辑
                saveLoading:false,
                formModal: false,
                editMode: '',
                record: {},
                formValidate: {
                    dataType: [
                        { required: true, message: '请输入媒体类别，供应商服务类别', trigger: 'blur' }
                    ]},
                //列表
                pageList: {},
                searchParams: {},
                columns1: [
                    // {key: 'id', title: '编号'},
                    {key: 'dataType', title: '数据类别', width:200,render (h, params) {
                        if(params.row.dataType === '1') {
                            return '媒体类别';
                        }else if(params.row.dataType === '2'){
                            return '供应商类别';
                        }else if(params.row.dataType === '3'){
                            return '监测文案';
                        }else {
                          return '-';
                        }
                    } },
                    {key: 'dataText', title: '类别名称', width: 200},
                    {
                        key: 'action',
                        title: '操作',
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
                            ]);
                        }
                    }
                ]
            }
        },
        methods: {
            init () {
                this.searchParams.pageSize = 10;
                this.searchParams.pageNum = 1;
                this.search();
            },
            search () {
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prDatatype/read/list',
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
                    url: '/pr/prDatatype/read/detail',
                    data: param
                }).then((resp) => {
                    //this.PrDatatypes = resp.data.dicts.PrDatatypes;
                    this.record = resp.data.data;
                })
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
                    url: '/pr/prDatatype/add',
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
                    url: '/pr/prDatatype/update',
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
                    url: '/pr/prDatatype/delete',
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
