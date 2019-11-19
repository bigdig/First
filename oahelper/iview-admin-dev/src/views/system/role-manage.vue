<style lang="less">
    @import '../../styles/common.less';
    @import './components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="30">
                <Card>
                    <Row>
                        <Col span="22">
                            <Input v-model="searchParams.keyword" placeholder="角色名称" style="width: 200px" />
                            <span @click="roleSearch" style="margin: 0 10px;"><Button type="primary" icon="search">搜索</Button></span>
                            <Button @click="searchCancel" type="ghost" >取消</Button>
                        </Col>
                        <Col span="2">
                            <span @click="addRecordBtn"><Button type="success" >添加角色</Button></span>
                        </Col>
                    </Row>
                    <Row class="margin-top-10">
                        <Table :columns="columns4" :data="pageList.list"></Table>
                    </Row>
                    <Row class="margin-top-10">
                        <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage" @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                    </Row>
                </Card>
            </Col>
        </Row>
        <Modal v-model="formModal"
               :title="editMode=='add'?'新增':'编辑'"
               :mask-closable="false">
            <Form ref="record" :model="record" label-position="right" :label-width="100" :rules="formValidate">
                <FormItem class="margin-bottom-10" label="机构" prop="deptId">
                    <Select :disabled="saveLoading" v-model="record.deptId" style="width:300px">
                        <Option v-for="(val,key) in deptTypes" :value="key" :key="key">
                            {{ val }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem class="margin-bottom-10" label="角色名" prop="roleName">
                    <Input :disabled="saveLoading" v-model="record.roleName" placeholder="请输入角色名"
                                                                       style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="角色编码" v-if="editMode ==='add'" prop="roleCode">
                    <Input :disabled="saveLoading" v-model="record.roleCode" placeholder="请输入角色编码"
                                                                     style="width: 300px"></Input></FormItem>
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
            </div>
        </Modal>
        <Modal
                v-model="modal2"
                title="角色功能设置"
                @on-ok="setMenu">
            <Scroll height="200">
                <Tree :data="treeNodelist" show-checkbox></Tree>
            </Scroll>
        </Modal>
        <Modal
                v-model="modal3"
                title="数据权限管理"
                width="800"
                @on-ok="setDataauth">
            <Row style="background:#eee;padding:20px">
                <Col span="7">
                    <Card :bordered="false" >
                        <p slot="title">媒体类别</p>
                        <Scroll height="250">
                            <Row v-for="option in dataType1"><Checkbox  v-model="option.isSelected" :value="option.isSelected" :key="option.id">{{option.dataText}}</Checkbox></Row>
                        </Scroll>
                    </Card>
                </Col>
                <Col span="7" offset="1">
                    <Card :bordered="false">
                        <p slot="title">供应商</p>
                        <Scroll height="250">
                            <Row v-for="option in dataType2"><Checkbox  v-model="option.isSelected" :value="option.isSelected" :key="option.id">{{option.dataText}}</Checkbox></Row>
                        </Scroll>
                    </Card>
                </Col>
                <Col span="7" offset="1">
                    <Card :bordered="false">
                        <p slot="title">监测方案</p>
                        <Scroll height="250">
                            <Row v-for="option in dataType3"><Checkbox  v-model="option.isSelected" :value="option.isSelected" :key="option.id">{{option.dataText}}</Checkbox></Row>
                        </Scroll>
                    </Card>
                </Col>
            </Row>
        </Modal>
    </div>
</template>

<script>
    export default {
        name: 'searchable-table',
        data () {
            return {
                editMode: '',
                record: {},
                deptTypes: [],
                saveLoading: false,
                formValidate: {
                  deptId: [
                    { required: true, message: '请选择机构', trigger: 'change' },
                  ],
                  roleName: [
                    { required: true, message: '请输入角色名称', trigger: 'blur' }
                  ],
                  roleCode: [
                    { required: true, message: '请输入角色编码', trigger: 'blur' }
                  ]
                },
                pageList: {},
                menuList: [],
                searchParams: {},
                formModal: false,
                modal2: false,
                modal3: false,
                roleParams: {},
                treeNodelist: [],
                datatypeList: [],
                dataType1: [],
                dataType2: [],
                dataType3: [],
                chooseList: [],
                chooseId: '',
                columns4: [
                    {key: 'roleName', title: '角色名称'},
                    {key: 'roleCode', title: '角色编码'},
                    {key: 'deptName', title: '机构'},
                    {
                        key: 'action',
                        title: '操作',
                        width: 250,
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
                                        type: 'primary',
                                        size: 'small'
                                    },
                                    style: {
                                        marginRight: '5px'
                                    },
                                    on: {
                                        click: () => {
                                            this.menu(params.index);
                                        }
                                    }
                                }, '功能')

                            ]);
                        }
                    }
                ]
            };
        },
        methods: {
            init () {
                this.searchParams.pageSize = 10;
                this.dataType1.title = '媒体类别';
                this.dataType2.title = '供应商';
                this.dataType3.title = '监测方案';
                this.search();
            },
            search () {
                this.$axiosInstance({
                    method: 'post',
                    url: '/sys/role/read/list',
                    data: this.searchParams
                }).then((resp) => {
                    this.pageList = resp.data.data;
                });
            },
            initDetail (id) {
                this.record = {}; // 表单置空
                if (id === null || id === '') {
                    this.editMode = 'add';
                } else {
                    this.editMode = 'edit';
                }
                var param = {};
                param.id = id;
                param.pagesize = '1000'
                this.$axiosInstance({
                    method: 'post',
                    url: '/sys/role/read/detail',
                    data: param
                }).then((resp) => {
                    this.deptTypes = resp.data.dicts.DEPTTYPE;
                    if (this.editMode === 'edit') {
                        this.record = resp.data.data;
                    }
                });
            },
            roleSearch () {
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
            changePageNum (pageSize) {
                this.searchParams.pageSize = pageSize;
                this.search();
            },
            menu (index) {
                this.menuList=[];
                this.treeNodelist=[];
                this.modal2 = true;
                this.roleParams = this.pageList.list[index];
                this.chooseId = this.roleParams.id;
                this.$axiosInstance({
                    method: 'post',
                    url: '/sys/role/read/menu',
                    data: {
                        id: this.roleParams.id,
                        pageSize: '10'
                    }
                }).then((resp) => {
//                    this.menuList = resp.data.dicts;
//                    this.$axiosInstance({
//                        method: 'post',
//                        url: '/sys/menu/read/list',
//                        data: {
//                            pageSize: '1000'
//                        }
//                    }).then((resp) => {
//
//                    });

                  let menus = resp.data.dicts;
                  //debugger;
                  let authMenuIds = JSON.parse(localStorage.menus);
                  //临时数组存放
                  var tempArray1 = [];//临时数组1
                  let authMenus = [];//临时数组2

                  for(var i=0;i<authMenuIds.length;i++){
                    tempArray1[authMenuIds[i]]=true;//将数array2 中的元素值作为tempArray1 中的键，值为true；
                  }
                  for(var i=0;i<menus.length;i++){
                    if(tempArray1[menus[i].menuId]){
                      authMenus.push(menus[i]);//过滤array1 中与array2 相同的元素；
                    }
                  }

                  console.log(authMenus);
                  //console.log(this.menuList);
                  this.getTreeNode(authMenus, this.treeNodelist);

                });
            },
            dataAuth (id) {
              this.dataType1 = [];
              this.dataType2 = [];
              this.dataType3 = [];
              this.modal3 = true;
                this.chooseId = id;
                var param = {};
                param.roleId = id;
                param.pageSize=1000;
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prDataauth/read/list',
                    data: param
                }).then((resp) => {
                    this.chooseList = resp.data.data.list;
                    this.$axiosInstance({
                        method: 'post',
                        url: '/pr/prDatatype/read/list',
                        data: {
                            pageSize: '1000'
                        }
                }).then((res) => {
                    if(res.data.data){
                        this.datatypeList = res.data.data.list;
                        for (let i = 0; i < this.datatypeList.length; i++) {
                            this.datatypeList[i].isSelected = false;
                        }
                        for (let i = 0; i < this.chooseList.length; i++) {
                            for (let j = 0; j < this.datatypeList.length; j++) {
                                if (this.chooseList[i].datatypeId === this.datatypeList[j].id) {
                                    this.datatypeList[j].isSelected = true;
                                }
                            }
                        }

                        for (let i = 0; i < this.datatypeList.length; i++) {
                            if (this.datatypeList[i].dataType === '1') {
                                this.datatypeList[i].title = this.datatypeList[i].dataText;
                                this.dataType1.push(this.datatypeList[i]);
                            } else if (this.datatypeList[i].dataType === '2') {
                                this.datatypeList[i].title = this.datatypeList[i].dataText;
                                this.dataType2.push(this.datatypeList[i]);
                            } else if (this.datatypeList[i].dataType === '3') {
                                this.datatypeList[i].title = this.datatypeList[i].dataText;
                                this.dataType3.push(this.datatypeList[i]);
                            }
                        }
                    }

                    });
                });
            },
            getTreeNode: function (list, treeNodelist) {
                if (treeNodelist.length === 0) {
                    var j = 0;
                    for (var i = 0; i < list.length; i++) {
                        list[i].title = list[i].menuName;
                        list[i].expand = true;
                        if ( list[i].parentId !='0' && list[i].isSelected === '1') {
                           list[i].checked = true;
                        }else{
                          list[i].checked = false;
                        }
                    }

                    for (var k = 0; k < list.length; k++) {
                        if (list[k].parentId === '0') {
                            treeNodelist.push(list[k]);
                            getChildNode(treeNodelist[j], list);
                            j++;
                        }
                    }
                    console.log(treeNodelist);
                }
                function getChildNode (item, _list) {
                    item.children = [];
                    for (var i = 0; i < _list.length; i++) {
                        if (_list[i].parentId === item.menuId) {
                            item.children.push(_list[i]);
                        }
                    }
                }
            },
//          isNodeSelected (currNode, list) {
//                currNode.checked = false;
//                if(currNode.parentId ==='0'){
//                  return;
//                }
//                for (let i = 0; i < list.length; i++) {
//                    if (currNode.id === list[i].menuId) {
//                        if (list[i].isSelected === '1') {
//                          currNode.checked = true;
//                        } else {
//                          currNode.checked = false;
//                        }
//                        currNode.menuId = list[i].menuId;
//                        break;
//                    }
//                }
//            },
            setMenu () {
                var param = {};
                param.menu = [];
                for (let i = 0; i < this.treeNodelist.length; i++) {
                    if (this.treeNodelist[i].checked === true) {
                          param.menu.push(this.treeNodelist[i].menuId);
                    }
                    for (let j = 0; j < this.treeNodelist[i].children.length; j++) {
                        if (this.treeNodelist[i].children[j].checked === true) {
                            param.menu.push(this.treeNodelist[i].children[j].menuId);
                            // add parentId
                          if(param.menu.indexOf(this.treeNodelist[i].children[j].parentId) < 0){
                            param.menu.push(this.treeNodelist[i].children[j].parentId);
                          }
                        }
                    }
                }
                param.roleId = this.chooseId;
                this.$axiosInstance({
                    method: 'post',
                    url: '/sys/role/menu/update',
                    data: param
                }).then((resp) => {
                  this.$Message.info(resp.data.msg);
                });
            },
            setDataauth () {
                var param = {};
                param.datatypeId = [];
                for (let i = 0; i < this.dataType1.length; i++) {
                    console.log(this.dataType1[i].isSelected);
                    if (this.dataType1[i].isSelected === true) {
                        param.datatypeId.push(this.dataType1[i].id);
                    }
                }
                for (let i = 0; i < this.dataType2.length; i++) {
                    if (this.dataType2[i].isSelected === true) {
                        param.datatypeId.push(this.dataType2[i].id);
                    }
                }
                for (let i = 0; i < this.dataType3.length; i++) {
                    if (this.dataType3[i].isSelected === true) {
                        param.datatypeId.push(this.dataType3[i].id);
                    }
                }
                param.roleId = this.chooseId;
                this.$axiosInstance({
                    method: 'post',
                    url: '/pr/prDataauth/update',
                    data: param
                }).then((resp) => {
                    this.$Notice.info({
                        title: resp.data.msg
                    });
                });
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
            addRecordBtn () {
                this.initDetail('');
                this.formModal = true;
            },
            addRecord () {
                this.$axiosInstance({
                    method: 'post',
                    url: '/sys/role/add',
                    data: this.removeEmpty(this.record)
                }).then((resp) => {
                    this.saveLoading = false;
                    if (resp.data.httpCode === 200) {
                        this.goBackList();
                    } else {
                        this.$Message.info(resp.data.msg);
                    }
                });
            },
            updateRecord () {
                this.$axiosInstance({
                    method: 'post',
                    url: '/sys/role/update',
                    data: this.removeEmpty(this.record)
                }).then((resp) => {
                    this.saveLoading = false;
                    if (resp.data.httpCode === 200) {
                        this.goBackList();
                    } else {
                        this.$Message.info(resp.data.msg);
                    }
                });
            },
            goBackList () {
                this.formModal = false;
                this.modal2 = false;
                this.modal3 = false;
                this.search();
            },
            cancel () {
              this.formModal = false;

              this.$Message.info('返回列表页面');
            },
            removeEmpty (obj) {
                Object.keys(obj).forEach(function (key) {
                    (obj[key] && typeof obj[key] === 'object') && this.removeEmpty(obj[key]) ||
                    (obj[key] === undefined || obj[key] === null) && delete obj[key];
                });
                return obj;
            }
        },
        mounted () {
            this.init();
        }
    };
</script>
