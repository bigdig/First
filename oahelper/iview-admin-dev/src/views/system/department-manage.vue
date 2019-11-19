<style lang="less">
    @import '../../styles/common.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
                <Card>
                    <Row>
                        <!--<Col span="22">-->
                            <!--<Input v-model="searchParams.keyword" placeholder="机构名称" style="width: 200px" />-->
                            <!--<span @click="deptSearch" style="margin: 0 10px;"><Button type="primary" icon="search">搜索</Button></span>-->
                            <!--<Button @click="searchCancel" type="ghost" >取消</Button>-->
                        <!--</Col>-->
                        <Col span="2" offset="22">
                            <span @click="addRecordBtn"><Button type="success" >添加机构</Button></span>
                        </Col>
                    </Row>
                    <Row class="margin-top-10 ">
                        <tree-grid
                                :items='nodeList'
                                :columns='columns2'
                                @on-row-click='rowClick'
                                @on-selection-change='selectionClick'></tree-grid>
                    </Row>
                    <Row class="margin-top-10 ">
                        <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage" @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                    </Row>
                </Card>
            </Col>
        </Row>
        <Modal v-model="formModal"
               :title="editMode=='add'?'新增':'编辑'"
               :mask-closable="false">
            <Form ref="record" :model="record" label-position="right" :label-width="100" :rules="formValidate">
                <FormItem class="margin-bottom-10" label="上级机构" prop="parentId" v-if="record.parentId != '0'">
                    <Select :disabled="saveLoading" v-model="record.parentId" style="width:300px">
                        <Option v-for="option in PrDatatypes" :value="option.id" :key="option.id">
                            {{ option.deptName }}
                        </Option>
                    </Select>
                </FormItem>
                <FormItem class="margin-bottom-10" label="机构名称" prop="deptName"><Input :disabled="saveLoading" v-model="record.deptName" placeholder="请输入机构名称"
                                                                     style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="机构编码" prop="deptNo" v-if="editMode === 'add'"><Input :disabled="saveLoading" v-model="record.deptNo" placeholder="请输入机构编码"
                                                                     style="width: 300px"></Input></FormItem>
                <FormItem class="margin-bottom-10" label="描述" prop="deptDesc"><Input :disabled="saveLoading" v-model="record.deptDesc" placeholder="请输入机构描述"
                                                                       style="width: 300px"></Input></FormItem>
                <!--<FormItem class="margin-bottom-10" label="备注" style="width: 500px">-->
                    <!--<Input :disabled="saveLoading" v-model="record.remark" type="textarea" placeholder="备注"></Input>-->
                <!--</FormItem>-->
            </Form>
            <div slot="footer">
                <Button type="text" @click="cancel">取消</Button>
                <Button type="primary" :loading="saveLoading" @click="handleSubmit">保存</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
import TreeGrid from './components/treeGrid2.0';

export default {

    data () {
          const valideParentId = (rule, value, callback) => {
            console.log('valideParentId:'+value +','+ this.record.id);
            if (value === this.record.id) {
              callback(new Error('上级机构不能选择自己'));
            } else {
              callback();
            }
          };
        return {
            // 编辑
            formModal: false,
            editMode: '',
            record: {},
            PrDatatypes: [],
            saveLoading: false,
            formValidate: {
              parentId: [
                { required: true, message: '请输入上级机构', trigger: 'change' },
                { validator: valideParentId, trigger: 'change' }
              ],
              deptName: [
                { required: true, message: '请输入机构名称', trigger: 'blur' },
                {type: 'string', max: 32, message: '不能超过32个字符', trigger: 'blur'}

              ],
              deptNo: [
                { required: true, message: '请输入机构代码', trigger: 'blur' },
                {type: 'string', max: 32, message: '不能超过32个字符', trigger: 'blur'}

              ],
              deptDesc: [
                {type: 'string', max: 20, message: '不能超过20个字符', trigger: 'blur'}
              ]
            },

            pageList: {},
            nodeList: [],
            searchParams: {},
            deptParams: {},
            findDept: [],
            childrenList: [],
            parentNode: [],
            node: {},
            sampleNodelist: [],

            columns2: [
                {key: 'deptName',title: '机构名称'},
                {key: 'deptNo',title: '机构代码'},
                {key: 'deptDesc', title: '描述'},
                {type: 'action', title: '操作', align: 'center', actions: [{type: 'primary', text: '编辑'}]
                }
            ]
        };
    },
    components: {
        TreeGrid
    },
    methods: {
        init () {
            this.searchParams.pageSize = 100;
            this.$axiosInstance({
                method: 'post',
                url: '/sys/dept/read/deptlist',
                data: this.searchParams
            }).then((resp) => {
                this.nodeList = resp.data.data.list;
                this.sampleNodelist = JSON.parse(JSON.stringify(this.nodeList));
            });
        },
        search () {
            this.$axiosInstance({
                method: 'post',
                url: '/sys/dept/read/deptlist',
                data: this.searchParams
            }).then((resp) => {
                this.pageList = resp.data.data;
            });
        },
        initDetail (id) {
            this.$axiosInstance({
                method: 'post',
                url: '/sys/dept/read/allDept'
            }).then((resp) => {
                this.PrDatatypes = resp.data.data;
                this.record = {};// 表单置空
                if (id === null || id === '') {
                    this.editMode = 'add';
                } else {
                    this.editMode = 'edit';
                }
                var param = {};
                param.id = id;
                this.$axiosInstance({
                    method: 'post',
                    url: '/sys/dept/read/detail',
                    data: param
                }).then((resp) => {
                    this.record = resp.data.data;
                });
            });
        },
        deptSearch () {
            this.nodeList = JSON.parse(JSON.stringify(this.sampleNodelist));
            for (let i = 0; i < this.nodeList.length; i++) {
                this.searchEach(this.nodeList[i], this.searchParams.keyword);
            }
            let length = this.nodeList.length;
            for (let i = length - 1; i >= 0; i--) {
                let e2 = this.nodeList[i];
                if (!this.isHasChildren(e2) && e2.deptName.indexOf(this.searchParams.keyword) <= -1) {
                    this.nodeList.splice(i, 1);
                }
            }
        },
        isHasChildren (node) {
            let flag = false;
            if (node.children && node.children.length > 0) {
                flag = true;
            }
            return flag;
        },
        searchEach (node, value) {
            let depth = this.getTreeDepth(node);
            let self = this;
            for (let i = 0; i < depth - 1; i++) {
                // 记录【删除不匹配搜索内容的叶子节点】操作的次数。
                // 如果这个变量记录的操作次数为0，表示树形结构中，所有的
                // 叶子节点(不包含只有根节点的情况)都匹配搜索内容。那么就没有必要再
                // 在循环体里面遍历树了.
                let spliceCounter = 0;
                // 遍历树形结构
                this.traverseTree(node, n => {
                    if (self.isHasChildren(n)) {
                        let children = n.children;
                        let length = children.length;

                        // 找到不匹配搜索内容的叶子节点并删除。为了避免要删除的元素在数组中的索引改变，从后向前循环,
                        // 找到匹配的元素就删除。
                        for (let j = length - 1; j >= 0; j--) {
                            let e3 = children[j];
                            if (!self.isHasChildren(e3) && e3.deptName.indexOf(value) <= -1) {
                                children.splice(j, 1);
                                spliceCounter++;
                            }
                        } // end for (let j = length - 1; j >= 0; j--)
                    }
                }); // end this.traverseTree(node, n=>{

                // 所有的叶子节点都匹配搜索内容，没必要再执行循环体了。
                if (spliceCounter === 0) {
                    break;
                }
            }
        },
        getTreeDepth (node) {
            if (undefined === node || null == node) {
                return 0;
            }
            // 返回结果
            let r = 0;
            // 树中当前层节点的集合。
            let currentLevelNodes = [node];
            // 判断当前层是否有节点
            while (currentLevelNodes.length > 0){
                // 当前层有节点，深度可以加一。
                r++;
                // 下一层节点的集合。
                let nextLevelNodes = new Array();
                // 找到树中所有的下一层节点，并把这些节点放到 nextLevelNodes 中。
                for (let i = 0; i < currentLevelNodes.length; i++) {
                    let e = currentLevelNodes[i];
                    if (this.isHasChildren(e)) {
                        nextLevelNodes = nextLevelNodes.concat(e.children);
                    }
                }
                // 令当前层节点集合的引用指向下一层节点的集合。
                currentLevelNodes = nextLevelNodes;
            }
            return r;
        },
        traverseTree (node, callback) {
            if (!node) {
                return;
            }
            var stack = [];
            stack.push(node);
            var tmpNode;
            while (stack.length > 0) {
                tmpNode = stack.pop();
                callback(tmpNode);
                if (tmpNode.children && tmpNode.children.length > 0) {
                    for (let i = tmpNode.children.length - 1; i >= 0; i--) {
                        stack.push(tmpNode.children[i]);
                    }
                }
            }
        },
        searchCancel () {
            this.searchParams = {};
            this.init();
        },
        changePage (currentPage) {
            this.searchParams.pageNum = currentPage;
            this.search();
        },
        changePageNum (pageSize) {
            this.searchParams.pageSize = pageSize;
            this.search();
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
            // let self = this;
            this.$axiosInstance({
                method: 'post',
                url: '/sys/dept/add',
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
                url: '/sys/dept/update',
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
            this.init();
        },
        removeEmpty (obj) {
            Object.keys(obj).forEach(function (key) {
                (obj[key] && typeof obj[key] === 'object') && this.removeEmpty(obj[key]) ||
                (obj[key] === undefined || obj[key] === null) && delete obj[key]
            });
            return obj;
        },
        cancel () {
          this.formModal = false;
          this.$Message.info('返回列表页面');
        },
        rowClick (data, event, index, actionText) {
//            console.log('当前行数据:' + data)
//            console.log('点击事件:' + event)
//            console.log('点击行号:' + index)
//            console.log('点击actionText:' + actionText)
            this.initDetail(data.id);
            this.formModal = true;
        },
        selectionClick (arr) {
            console.log('选中数据id数组:' + arr);
        },
        sortClick (key, type) {
            console.log('排序字段:' + key)
            console.log('排序规则:' + type);
        }
    },
    mounted () {
        this.init();
    }
};
</script>
