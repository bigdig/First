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
                            <Select v-model="searchParams.dataType" placeholder="媒体类别" style="width:200px">
                                <Option  value="" >所有类别</Option>
                                <Option v-for="option in PrDatatypes" :value="option.id" :key="option.id">
                                    {{ option.text }}
                                </Option>
                            </Select>

                            <Input v-model="searchParams.keyword" placeholder="请输入姓名、媒体名称、联系电话、微信号、联系邮箱、上级单位、领导名称" style="width: 300px;"/>
                            <!--<Input v-model="searchParams.tel" placeholder="请输入联系电话" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.wechat" placeholder="请输入微信号" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.mail" placeholder="请输入联系邮箱" style="width: 200px;margin-bottom: 5px"/>-->

                            <!--<Input v-model="searchParams.superiorUnit" placeholder="请输入上级单位" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.leaderName" placeholder="请输入领导名称" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.brokerCompany" placeholder="请输入对接公司" style="width: 200px;margin-bottom: 5px"/>-->
                            <!--<Input v-model="searchParams.brokerName" placeholder="请输入对接人" style="width: 200px;margin-bottom: 5px"/>-->
                            <span @click="userSearch"><Button type="primary" icon="search">搜索</Button></span>
                        </Col>
                        <Col span="2" style="padding-top:6px">
                        <a target="_blank" href="/excel/prmedia.xlsx">导入模板</a>
                        </Col>
                        <Col span="2" >
                        <Upload action="/pr/prMedia/batchImport":format="['xls','xlsx']" :show-upload-list="false" :on-success="uploadSucc" :on-error="uploadFail" :on-format-error="handleFormatError">
                            <Button type="ghost" icon="ios-cloud-upload-outline">导入</Button>
                        </Upload>
                        </Col>
                        <Col span="4" >
                                <Button type="primary" @click="exportData()"><Icon type="ios-download-outline"></Icon>导出</Button>
                                <span  @click="addRecordBtn"><Button type="success"> 添加媒体 </Button></span>
                        </Col>
                    </Row>
                    <Row class="margin-top-10 " style="display: none;" v-if="pageList">
                        <Table :columns="columns1" :data="pageList.list"  ref="table" size="small"></Table>
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
                                    [{{item.dataText}}]{{item.mediaName}}：{{item.userName}}
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
                                        <p ><span style="font-weight:bold">部门：</span>{{item.department}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">职位：</span>{{item.position}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">地址：</span>{{item.address}}</p>
                                        </Col>
                                    </row>
                                    <row>
                                        <Col span="6" v-if="item.tel!='无权限查看'">
                                        <p ><span style="font-weight:bold">联系电话：</span>{{item.tel}}</p>
                                        </Col>
                                        <Col span="6" v-if="item.mail!='无权限查看'">
                                        <p ><span style="font-weight:bold">联系邮箱：</span>{{item.mail}}</p>
                                        </Col>
                                        <Col span="6" v-if="item.wechat!='无权限查看'">
                                        <p ><span style="font-weight:bold">微信号：</span>{{item.wechat}}</p>
                                        </Col>
                                    </row>
                                    <row>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">上级单位：</span>{{item.superiorUnit}}</p>
                                        </Col>
                                        <Col span="6">
                                        <p ><span style="font-weight:bold">领导：</span>{{item.leaderName}}</p>
                                        </Col>
                                        <Col span="6" v-if="item.leaderTel!='无权限查看'">
                                        <p ><span style="font-weight:bold">领导电话：</span>{{item.leaderTel}}</p>
                                        </Col>
                                        <Col span="6" v-if="item.leaderMail!='无权限查看'">
                                        <p ><span style="font-weight:bold">领导邮箱：</span>{{item.leaderMail}}</p>
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
                                    </row>
                                    <row>
                                        <Col span="6">
                                        <p><span style="font-weight:bold">提交人：</span>{{item.createByName}}</p>
                                        </Col>
                                        <Col span="18">
                                        <p ><span style="font-weight:bold">备注：</span>{{item.remark}}</p>
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
                <FormItem class="margin-bottom-10" label="媒体类别" prop="dataType">
                    <Select :disabled="saveLoading" v-model="record.dataType" style="width:300px">
                        <Option v-for="option in PrDatatypes" :value="option.id" :key="option.id">
                            {{ option.text }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem label="媒体名称" prop="mediaName" class="margin-bottom-10" >
                    <Input v-model="record.mediaName" placeholder="请输入媒体名称" :disabled="saveLoading" style="width: 300px"></Input>
                </FormItem>
                <FormItem label="姓名" prop="userName" class="margin-bottom-10" >
                    <Input v-model="record.userName" placeholder="请输入姓名" :disabled="saveLoading" style="width: 300px"></Input>
                </FormItem>
                <FormItem class="margin-bottom-10" label="部门">
                    <Input :disabled="saveLoading" v-model="record.department" placeholder="请输入部门" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="职位">
                    <Input :disabled="saveLoading" v-model="record.position" placeholder="请输入职位" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="联系电话" v-if="record.tel!='无权限查看'">
                    <Input :disabled="saveLoading" v-model="record.tel" placeholder="请输入联系电话" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="微信号">
                    <Input :disabled="saveLoading" v-model="record.wechat" placeholder="请输入微信号" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="联系邮箱" v-if="record.mail!='无权限查看'">
                    <Input :disabled="saveLoading" v-model="record.mail" placeholder="请输入联系邮箱" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="地址">
                    <Input :disabled="saveLoading" v-model="record.address" placeholder="请输入地址" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="上级单位">
                    <Input :disabled="saveLoading" v-model="record.superiorUnit" placeholder="请输入上级单位" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="领导">
                    <Input :disabled="saveLoading" v-model="record.leaderName" placeholder="请输入领导" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="领导电话" v-if="record.leaderTel!='无权限查看'">
                    <Input :disabled="saveLoading" v-model="record.leaderTel" placeholder="请输入领导电话" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="领导邮箱" v-if="record.leaderMail!='无权限查看'">
                    <Input :disabled="saveLoading" v-model="record.leaderMail" placeholder="请输入领导邮箱" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="对接公司">
                    <Input :disabled="saveLoading" v-model="record.borkerCompany" placeholder="请输入对接公司" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="对接人">
                    <Input :disabled="saveLoading" v-model="record.brokerName" placeholder="请输入对接人" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="对接人电话">
                    <Input :disabled="saveLoading" v-model="record.brokerUid" placeholder="请输入对接人电话" style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="备注" prop="remark">
                    <Input type="textarea" :row="5" :disabled="saveLoading" v-model="record.remark"  placeholder="备注"></Input>
                </FormItem>

            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
            </div>
        </Modal>
        <Modal v-model="formModal1"  title="新增" :mask-closable="false" >
            <Form ref="coopRecord" :model="coopRecord" label-position="right" :label-width="100" :rules="coopFormValidate">
                <Input v-if="false"  v-model="coopRecord.coopId" placeholder="请输入媒体编号"
                       style="width: 300px"></Input>
                <Input v-if="false" v-model="coopRecord.coopType" placeholder="请输入合作类型"
                       style="width: 300px"></Input>
                <FormItem class="margin-bottom-10" label="合作时间" prop="coopDate">
                    <DatePicker type="date" :disabled="saveLoading" v-model="coopRecord.coopDate" placeholder="请输入合作时间" format="yyyy-MM-dd" style="width: 200px"></DatePicker>
                </FormItem>
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
                <Button type="error" size="large" long :loading="saveLoading" @click="delRecord">删除</Button>
            </div>
        </Modal>

    </div>
</template>

<script>

  //import Row from "iview/src/components/grid/row";

  export default {
      //components: {Row},
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
        PrDatatypes: [],
        formValidate: {
          dataType:[
            { required: true, message: '请选择数据类型', trigger: 'change' }
          ],
          mediaName: [
          { required: true, message: '请输入媒体名称', trigger: 'blur' },
            {type: 'string', max: 32, message: '不能超过32个字符', trigger: 'blur'}
          ],
          userName: [
          { required: true, message: '请输入用户姓名', trigger: 'blur' },
            {type: 'string', max: 32, message: '不能超过32个字符', trigger: 'blur'}
          ],
          department: [
            {type: 'string', max: 120, message: '不能超过120个字符', trigger: 'blur'}
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
          superiorUnit: [
            {type: 'string', max: 120, message: '不能超过120个字符', trigger: 'blur'}
            ],
          leaderTel: [
            {type: 'string', max: 16, message: '不能超过16个字符', trigger: 'blur'}
            ],
          leaderName: [
                {type: 'string', max: 32, message: '不能超过32个字符', trigger: 'blur'}
            ],
          leaderMail: [
                {type: 'string', max: 32, message: '不能超过32个字符', trigger: 'blur'}
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
            {type: 'string', max: 1000, message: '不能超过1000个字符', trigger: 'blur'}
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
        //列表
        pageList: {},
        searchParams: {},
        columns1: [
          {key: 'dataText', title: '媒体类别', width: 100,ellipsis:true},
          {key: 'userName', title: '姓名', width: 100,ellipsis:true},
          {key: 'mediaName', title: '媒体名称', width: 100},
          {key: 'department', title: '部门', width: 100},
          {key: 'position', title: '职位', width: 100},
          {key: 'tel', title: '联系电话', width: 100},
          {key: 'wechat', title: '微信号', width: 100},
          {key: 'mail', title: '联系邮箱', width: 100},
          {key: 'address', title: '地址', width: 100},
          {key: 'superiorUnit', title: '上级单位', width: 100},
          {key: 'leaderName', title: '领导', width: 100},
          {key: 'leaderTel', title: '领导电话', width: 100},
          {key: 'leaderMail', title: '领导邮箱', width: 100},
          {key: 'borkerCompany', title: '对接公司', width: 100},
          {key: 'brokerName', title: '对接人', width: 100},
//          {key: 'brokerUid', title: '对接人uid', width: 100},
          {key: 'remark', title: '备注', width: 100},
//          {key: 'createBy', title: '创建人', width: 100},
          {
            key: 'action',
            title: '操作',
            width: 300,
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
                    style: {
                        marginRight: '5px'
                    },
                  on: {
                    click: () => {
                      this.delModal = true;
                      this.delId=params.row.id;
                    }
                  }
                }, '删除'),
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
                              let argu = { coopId: params.row.id };
                              this.$router.push({
                                  name: 'coop-manage',
                                  params: argu
                              });
                          }
                      }
                  }, '合作纪录'),
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
                              this.formModal1 = true;
                              this.initCoopModal(params.row.id);
                          }
                      }
                  }, '添加合作纪录'),
              ]);
            }
          }
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
          url: '/pr/prMedia/read/list',
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
        this.initDetail('');
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
          url: '/pr/prMedia/read/detail',
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
          url: '/pr/prMedia/add',
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
          url: '/pr/prMedia/update',
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
          url: '/pr/prMedia/delete',
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
          (obj[key] && typeof obj[key] === 'object') && this.removeEmpty(obj[key]) ||
          (obj[key] === undefined || obj[key] === null) && delete obj[key]
        });
        return obj;
      },
      initCoopModal (id){
          this.coopRecord={}
            this.coopRecord.coopId = id;
            this.coopRecord.coopType='0';//媒体类型
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
            this.coopRecord.coopDate = this.dateFormat(this.coopRecord.coopDate)
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
