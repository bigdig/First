<template>
    <div>
        <Card>
            <Row>
                <div v-model="userGroupList">
                    <span v-for="item in userSelectedList" style="margin-right: 15px;">{{item.userName}}</span>
                </div>
            </Row>
            <Row>
                <Col span="4" class="padding-left-10">
                <Card>
                    <p slot="title">
                        <Icon type="navicon-round"/>
                        人员分组信息 </p>
                    <div style="height: 320px;overflow-y: auto">
                        <!--<ul class="iview-admin-draggable-list">-->
                        <!--<li v-for="(item,index) in userGroupList" :key="item.groupId" class="notwrap"-->
                        <!--:data-index="index"> {{ item.groupName }}</li>-->
                        <!--</ul>-->
                        <Menu active-name="1" width="auto" @on-select="loadUsers">
                            <MenuItem :name="0">
                                <Icon type="ios-people"/>
                                所有人员 </MenuItem>
                            <MenuGroup title="部门分组">
                                <MenuItem v-for="item in userGroupList" v-if="item.groupType==='DEPARTMENT'"
                                          :name="item.groupId">
                                    <Icon type="ios-people"/>
                                    {{item.groupName}}</MenuItem>
                            </MenuGroup>
                            <MenuGroup title="自定义分组">
                                <MenuItem v-for="item in userGroupList" v-if="item.groupType==='CUSTOM'"
                                          :name="item.groupId">
                                    <Icon type="ios-people"/>
                                    {{item.groupName}} </MenuItem>
                            </MenuGroup>

                        </Menu>
                    </div>
                </Card>
                </Col>

                <Col span="10" style="padding-left: 10px;">
                <Card style="height: 400px;">
                    <p slot="title">
                        <Icon type="android-contacts"/>
                        成员列表</p>
                    <Table height="330" border size="small" :columns="userCols" :data="userList"></Table>
                </Card>
                </Col>
                <Col span="10" style="padding-left: 10px;">
                <Card style="height: 400px;">
                    <p slot="title">
                        <Icon type="android-contacts"/>
                        已选择人员</p>
                    <Table height="330" border size="small" :columns="userSelectedCols" :data="userSelectedList"></Table>
                </Card>
                </Col>
            </Row>

        </Card>


    </div>
</template>

<script>
    export default {
        data () {
            return {
                userGroupList: [
                    {groupId: 1, groupName: '自定义分组1', groupType: 'CUSTOM'},
                    {groupId: 2, groupName: '部门分组1', groupType: 'DEPARTMENT'},
                    {groupId: 3, groupName: '自定义分组2', groupType: 'CUSTOM'},
                    {groupId: 4, groupName: '自定义分组3', groupType: 'CUSTOM'},
                    {groupId: 5, groupName: '部门分组2', groupType: 'DEPARTMENT'},
                    {groupId: 6, groupName: '部门分组3', groupType: 'DEPARTMENT'},
                    {groupId: 7, groupName: '部门分组4', groupType: 'DEPARTMENT'}
                ],
                userCols: [
                    {
                        title: '用户名',
                        key: 'userName'
                    },
                    {
                        title: '所在分组',
                        key: 'groupName'
                    },
                    {
                        title: '标记',
                        key: 'flag'
                    },
                    {
                        title: '操作',
                        key: 'action',
                        width: 80,
                        render: (h, params) => {
                            return h('div', [
                                h('Button', {
                                    props: {
                                        type: 'ghost',
                                        size: 'small'
                                    },
                                    on: {
                                        click: () => {
                                            this.handleClick(params);
                                        }
                                    }
                                }, params.row.flag ? '移除' : '添加')
                            ]);
                        }
                    }
                ],
                userSelectedCols: [
                    {
                        title: '用户名',
                        key: 'userName'
                    },
                    {
                        title: '所在分组',
                        key: 'groupName'
                    }
                ],
                userSelectedList: [],
                userList: [],
                userListAll: [
                    {
                        userId: '1',
                        userName: '1John Brown',
                        groupId: '1',
                        flag: false,
                        groupName: 'New York No. 1 Lake Park'
                    },
                    {
                        userId: '2',
                        userName: '2John Brown',
                        groupId: '2',
                        flag: true,
                        groupName: 'New York No. 1 Lake Park'
                    },
                    {
                        userId: '3',
                        userName: '3John Brown',
                        groupId: '5',
                        flag: false,
                        groupName: 'New York No. 1 Lake Park'
                    },
                    {
                        userId: '4',
                        userName: '4John Brown',
                        groupId: '4',
                        flag: false,
                        groupName: 'New York No. 1 Lake Park'
                    },
                    {
                        userId: '5',
                        userName: '5John Brown',
                        groupId: '1',
                        flag: false,
                        groupName: 'New York No. 1 Lake Park'
                    },
                    {
                        userId: '6',
                        userName: '6John Brown',
                        groupId: '3',
                        flag: true,
                        groupName: 'New York No. 1 Lake Park'
                    },
                    {
                        userId: '7',
                        userName: '7John Brown',
                        groupId: '5',
                        flag: false,
                        groupName: 'New York No. 1 Lake Park'
                    },
                    {
                        userId: '8',
                        userName: '8John Brown',
                        groupId: '2',
                        flag: true,
                        groupName: 'New York No. 1 Lake Park'
                    },
                    {
                        userId: '9',
                        userName: '9John Brown',
                        groupId: '2',
                        flag: false,
                        groupName: 'New York No. 1 Lake Park'
                    }
                ]
            };
        },
        methods: {
            init () {
            },
            loadUsers(groupId){
                this.userList = []

                if (groupId == 0) {
                    for (var i = 0; i < this.userListAll.length; i++) {
                        this.userList.push(this.userListAll[i])
                    }
                } else {
                    for (var i = 0; i < this.userListAll.length; i++) {
                        var user = this.userListAll[i]
                        if (user.flags) {
                          //---------------------------------------------------------------------------
                            this.userSelectedList.push(user)
                        }
                        if (user.groupId == groupId) {
                            this.userList.push(user)
                        }
                    }
                }
                this.userList.sort(function (a, b) {
                    if (a.flag !== b.flag) {
                        return a.flag ? 0 : 1;
                    }
                    return 0;
                });
            },
            handleClick (params) {
                params.row.flag = !params.row.flag;
                if (params.row.flag) {
                    this.userSelectedList.push(params.row)
                } else {
                    for (var i = 0; i < this.userSelectedList.length; i++) {
                        if (this.userSelectedList[i].userId === params.row.userId) {
                            this.userSelectedList.splice(i, 1)
                        }
                    }
                }
            }
        }

    };
</script>
<style>


</style>
