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
                        <Col span="16">
                        <Select v-model="searchParams.dataType" placeholder="供应商类别" style="width:200px">
                            <Option  value="" >所有类别</Option>
                            <Option v-for="option in PrDatatypes" :value="option.id" :key="option.id">
                                {{ option.text }}
                            </Option>
                        </Select>

                        <Input v-model="searchParams.keyword" placeholder="请输入供应商城市、单位、联系人、电话、邮箱、微信号、对接公司、对接人" style="width: 300px;margin-bottom: 5px"/>
                            <!--<Input v-model="searchParams.unit" placeholder="请输入供应商单位" style="width: 200px"/>-->
                            <!--<Input v-model="searchParams.linkmanName" placeholder="请输入联系人姓名" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.tel " placeholder="请输入电话" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.mail" placeholder="请输入邮箱" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.wechat " placeholder="请输入微信号" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.borkerCompany" placeholder="请输入对接公司" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.brokerName" placeholder="请输入对接人姓名" style="width: 200px;margin-bottom: 5px;margin-bottom: 5px"/>-->
                            <!--<Button @click="searchCancel" type="ghost">取消</Button>-->
                            <span @click="userSearch" ><Button type="primary" icon="search">搜索</Button></span>
                        </Col>
                        <Col span="2" style="padding-top:6px">
                            <a target="_blank" href="/excel/prsupplier.xlsx">导入模板</a>
                        </Col>
                        <Col span="2" >
                            <Upload action="/pr/prSupplier/batchImport" :format="['xls','xlsx']" :show-upload-list="false" :on-success="uploadSucc" :on-error="uploadFail" :on-format-error="handleFormatError">
                                <Button type="ghost" icon="ios-cloud-upload-outline">导入</Button>
                            </Upload>
                        </Col>
                        <Col span="4" >
                                <Button type="primary" @click="exportData()"><Icon type="ios-download-outline"></Icon>导出</Button>
                                <span @click="addRecordBtn"><Button type="success"> 添加供应商 </Button></span>
                        </Col>
                    </Row>
                    <Row class="margin-top-10 " style="display: none;" v-if="pageList">
                        <Table :columns="columns1" :data="pageList.list" ref="table" size="small"></Table>
                    </Row>
                    <Row class="margin-top-10 " v-if="pageList==null || (pageList.list && pageList.list.length==0)">
                        <Card>
                            <p style="text-align: center">暂无数据</p>
                        </Card>
                    </Row>
                    <Row v-if="pageList">
                        <Row class="margin-top-10 " v-for="item in pageList.list">
                            <Card>
                                <p slot="title" style="height:28px;line-height: 28px;">
                                    [{{item.dataText}}]{{item.unit}}
                                    <Tooltip content="编辑" v-if="userInfo.deptId == item.createUserDept">
                                        <Button type="ghost" icon="edit" shape="circle" size="small" @click="updateClick(item.id)"></Button>
                                    </Tooltip>
                                    <Tooltip content="删除" v-if="userInfo.deptId == item.createUserDept">
                                        <Button type="ghost" icon="android-delete" shape="circle" size="small" @click="deleteClick(item.id)"></Button>
                                    </Tooltip>
                                </p>
                                <a href="#" size="28" slot="extra" @click.prevent="addCoopClick(item.id)" style="margin-right: 20px;">
                                    <!--<Tooltip content="新增合作记录">-->
                                    <Icon type="android-add" style="margin-right: 5px;"></Icon>新增合作记录
                                    <!--</Tooltip>-->
                                </a>
                                <a href="#" size="28" slot="extra" @click.prevent="showCoopClick(item.id)">
                                    <!--<Tooltip content="合作记录">-->
                                    <Icon type="android-menu" style="margin-right: 5px;"></Icon>合作列表查询
                                    <!--</Tooltip>-->
                                </a>
                                <div>
                                    <row>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">城市：</span>{{item.city}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">联系人姓名：</span>{{item.linkmanName}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">联系电话：</span>{{item.tel}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">联系邮箱：</span>{{item.mail}}</p>
                                        </Col>
                                    </row>
                                    <row>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">微信号：</span>{{item.wechat}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">地址：</span>{{item.address}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">对接公司：</span>{{item.borkerCompany}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">对接人：</span>{{item.brokerName}}</p>
                                        </Col>
                                    </row>
                                    <row>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">对接公司：</span>{{item.borkerCompany}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">对接人：</span>{{item.brokerName}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">对接人电话：</span>{{item.brokerUid}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">提交人：</span>{{item.createByName}}</p>
                                        </Col>
                                    </row>
                                    <row>
                                        <Col span="24">
                                        <p ><span style="font-weight:bold">服务简介：</span>{{item.remark}}</p>
                                        </Col>
                                    </row>
                                </div>
                            </Card>
                        </Row>
                    </Row>

                    <Row class="margin-top-10 " v-if="pageList">
                        <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage"
                              @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                    </Row>
                </Card>
            </Col>
        </Row>

        <Modal v-model="formModal"  :title="editMode=='add'?'新增':'编辑'" :mask-closable="false" >
            <Form ref="record" :model="record" label-position="right" :label-width="100" :rules="formValidate">
                <FormItem class="margin-bottom-10" label="服务类别" prop="dataType">
                    <Select :disabled="saveLoading" v-model="record.dataType" style="width:300px">
                        <Option v-for="option in PrDatatypes" :value="option.id" :key="option.id">
                            {{ option.text }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem class="margin-bottom-10"prop="unit" label="单位">
                    <Input :disabled="saveLoading" v-model="record.unit" placeholder="请输入单位名称" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10"prop="linkmanName" label="联系人姓名">
                    <Input :disabled="saveLoading" v-model="record.linkmanName" placeholder="请输入联系人姓名" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10"prop="city" label="城市">
                    <Input :disabled="saveLoading" v-model="record.city" placeholder="请输入城市" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10"  prop="position" label="职位">
                    <Input :disabled="saveLoading" v-model="record.position" placeholder="请输入职位" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" prop="tel"label="联系电话">
                    <Input :disabled="saveLoading" v-model="record.tel" placeholder="请输入联系电话" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10"prop="wechat" label="微信号">
                    <Input :disabled="saveLoading" v-model="record.wechat" placeholder="请输入微信号" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" prop="mail"label="联系邮箱">
                    <Input :disabled="saveLoading" v-model="record.mail" placeholder="请输入联系邮箱" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" prop="address" label="地址">
                    <Input :disabled="saveLoading" v-model="record.address" placeholder="请输入地址" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10"prop="borkerCompany" label="对接公司">
                    <Input :disabled="saveLoading" v-model="record.borkerCompany" placeholder="请输入对接公司" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10"prop="brokerName" label="对接人">
                    <Input :disabled="saveLoading" v-model="record.brokerName" placeholder="请输入对接人" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="对接人电话">
                    <Input :disabled="saveLoading" v-model="record.brokerUid" placeholder="请输入对接人电话" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="服务简介" prop="remark">
                    <Input type="textarea" :row="5" :disabled="saveLoading" v-model="record.remark"  placeholder="服务简介"></Input>
                </FormItem>

            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
            </div>
        </Modal>
        <Modal v-model="formModal1"  title="新增" :mask-closable="false" >
            <Form ref="coopRecord" :model="coopRecord" label-position="right" :label-width="100" :rules="coopFormValidate">

               <Input v-if="false" v-model="coopRecord.coopId" placeholder="请输入供应商编号"
                      style="width: 300px"></Input>
              <Input  v-if="false" v-model="coopRecord.coopType" placeholder="请输入合作类型"
                      style="width: 300px"></Input>
                <FormItem class="margin-bottom-10" label="合作时间" prop="coopDate">
                    <DatePicker type="date" :disabled="saveLoading" v-model="coopRecord.coopDate" placeholder="请输入合作时间"
                                format="yyyy-MM-dd" style="width: 300px"></DatePicker></FormItem>
                <FormItem class="margin-bottom-10" label="合作内容" prop="coopContent">
                    <Input type="textarea" :rows="5" :disabled="saveLoading" v-model="coopRecord.coopContent" placeholder="请输入合作内容"
                                                                       ></Input></FormItem>
                <!--<FormItem class="margin-bottom-10" label="备注" style="width: 500px">-->
                    <!--<Input :disabled="saveLoading" v-model="coopRecord.remark" type="textarea" placeholder="备注"></Input>-->
                <!--</FormItem>-->

            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleCoopSubmit">保存</Button>
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
                <Button type="error" size="large"long :loading="saveLoading"  @click="delRecord">删除</Button>
            </div>
        </Modal>

    </div>
</template>

<script>

    export default {
        name: 'media-manage',
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
                PrDatatypes:[],
                formValidate: {
                  dataType:[
                    { required: true, message: '请选择数据类型', trigger: 'change' }
                  ],
                  unit: [
                    { required: true, message: '请输入单位', trigger: 'blur' },
                    {type: 'string', max: 60, message: '不能超过60个字符', trigger: 'blur'}
                  ],
                  linkmanName: [
                    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
                    {type: 'string', max: 32, message: '不能超过32个字符', trigger: 'blur'}
                  ],
                    city: [
                        {type: 'string', max: 16, message: '不能超过16个字符', trigger: 'blur'}
                    ],
                    position: [
                      {type: 'string', max: 16, message: '不能超过16个字符', trigger: 'blur'}
                    ],
                    tel: [
                      {type: 'string', max: 16, message: '不能超过16个字符', trigger: 'blur'}
                    ],
                    wechat: [
                      {type: 'string', max: 16, message: '不能超过16个字符', trigger: 'blur'}
                    ],
                    mail: [
                      {type: 'string', max: 16, message: '不能超过16个字符', trigger: 'blur'}
                    ],
                    address: [
                      {type: 'string', max: 120, message: '不能超过120个字符', trigger: 'blur'}
                    ],
                    borkerCompany: [
                      {type: 'string', max: 120, message: '不能超过120个字符', trigger: 'blur'}
                    ],
                    brokerName: [
                      {type: 'string', max: 32, message: '不能超过32个字符', trigger: 'blur'}
                    ],
                    brokerUid: [
                      {type: 'string', max: 18, message: '不能超过18个字符', trigger: 'blur'}
                    ],
                    remark: [
                      {type: 'string', max: 500, message: '不能超过500个字符', trigger: 'blur'}
                    ]
                },
              coopFormValidate: {
                  coopDate: [
                    { required: true,type: 'date', message: '请输入合作时间', trigger: 'change' }
                  ],
                  coopContent: [
                    { required: true, message: '请输入合作内容', trigger: 'blur' },
                    {type: 'string', max: 2000, message: '不能超过2000个字符', trigger: 'blur'}
                  ],
                },
               // 列表
                pageList: {},
                searchParams: {},
                columns1: [
                    {key: 'dataText', title: '服务类别', width: 100},
                    {key: 'unit', title: '单位', width: 100},
                    {key: 'city', title: '城市', width: 100},
                    {key: 'position', title: '职位', width: 100},
                    {key: 'tel', title: '联系电话', width: 100},
                    {key: 'wechat', title: '微信号', width: 100},
                    {key: 'mail', title: '联系邮箱', width: 100},
                    {key: 'address', title: '地址', width: 100},
                    {key: 'linkmanName', title: '联系人姓名', width: 100},
                    {key: 'borkerCompany', title: '对接公司', width: 100},
                    {key: 'brokerName', title: '对接人', width: 100},
//                    {key: 'brokerUid', title: '对接人uid', width: 100},
                    {key: 'remark', title: '备注', width: 100},
                ],
                formModal1:false,
                coopRecord:{},
              userInfo:null

            }
        },
        methods: {
            init () {
                this.searchParams.pageSize = 10;
                this.searchParams.pageNum = 1;
                this.search();

              let userInfo = JSON.parse(localStorage.userInfo);
              this.userInfo = userInfo;

            },
            search () {
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prSupplier/read/list',
                    data: this.searchParams
                }).then((resp) => {
                    this.pageList = resp.data.data;
                    this.PrDatatypes = resp.data.dicts.PrDatatypes;
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
                    url: '/pr/prSupplier/read/detail',
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
              showCoopClick(id)  {
                let argu = { coopId: id };
                this.$router.push({
                  name: 'coop-manage',
                  params: argu
                });
              },
              addCoopClick(id)  {
                this.formModal1 = true;
                this.initCoopModal(id);
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
                    url: '/pr/prSupplier/add',
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
                    url: '/pr/prSupplier/update',
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
                    url: '/pr/prSupplier/delete',
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
                this.formModal1 = false;
                this.delModal = false;
                this.search();
            },
            cancel () {
                this.formModal = false;
                this.formModal1 = false;
                this.$Message.info('返回列表页面');
            },
            removeEmpty(obj) {
                Object.keys(obj).forEach(function(key) {
                    (obj[key] && typeof obj[key] === 'object') && removeEmpty(obj[key]) ||
                    (obj[key] === undefined || obj[key] === null) && delete obj[key]
                });
                return obj;
            },
            initCoopModal (id){
                this.coopRecord={}
                this.coopRecord.coopId = id;
                this.coopRecord.coopType='1';//供应商类型
            },
            handleCoopSubmit () {
                this.$refs['coopRecord'].validate((valid) => {
                    if (valid) {
                        this.addCoopRecord();
                    }
                })
            },
            addCoopRecord(){
                let self = this;
                this.coopRecord.coopDate = this.dateFormat(this.coopRecord.coopDate);
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prCoop/add',
                    data: this.coopRecord
                }).then((resp) => {
                    this.saveLoading = false;
                    if (resp.data.httpCode === 200) {
                        this.goBackList();
                    }else{
                        this.$Message.info(resp.data.msg);
                    }
                })
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
            exportData () {
                this.$refs.table.exportCsv({
                    filename: 'data'
                });
            },
            uploadSucc (res, file) {
                let tips = '';
                for (let i = 0; i < res.data.errorLogList.length; i++) {
                    tips = tips+ res.data.errorLogList[i] +"<br/>";
                }
                this.$Notice.info({
                    title:res.msg,
                    desc:tips
                });
                this.search();
            },
            uploadFail (){
                this.$Notice.info({
                    title:'上传文件失败'
                });
            },
            handleFormatError (file) {
                this.$Notice.warning({
                    title: '不支持该文件格式'
                });
            },
        },
        mounted () {
            this.init();
        }
    };
</script>
