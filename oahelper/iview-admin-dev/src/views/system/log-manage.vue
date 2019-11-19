<style lang="less">
    @import '../../styles/common.less';
    @import './components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
                <Card>
                    <Row>
                        <Col span="22">
                            <Input v-model="searchParams.account" placeholder="账户名" style="width: 200px" />
                            <Select v-model="searchParams.requestUri" style="width:200px">
                                <Option v-for="(val, key) in allDicts" :value="key" :key="key">{{ val }}</Option>
                            </Select>
                            <span @click="search" style="margin: 0 10px;"><Button type="primary" icon="search">搜索</Button></span>
                            <Button @click="searchCancel" type="ghost" >取消</Button>
                        </Col>
                    </Row>
                    <Row class="margin-top-10 ">
                        <Table :columns="columns1" :data="pageList.list"></Table>
                    </Row>
                    <Row class="margin-top-10 ">
                        <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage" @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                    </Row>
                </Card>
            </Col>
        </Row>
    </div>
</template>

<script>

    export default {
        name: 'searchable-table',
        data () {
            return {
                pageList: {},
                searchParams:{},
                userParams:{},
                allDicts:{},
                columns1: [
                    {
                        key: 'requestUri',
                        title: '请求地址'
                    },
                    {
                        key: 'createBy',
                        title: '姓名'
                    },
                    {
                        key: 'clientHost',
                        title: 'IP地址'
                    },
                    {
                        key: 'createTime',
                        title: '操作时间'
                    }
                ]
            }
        },
        methods: {
            init () {
                this.searchParams.pageSize=10;
                this.searchParams.pageNum=1;
                this.$axiosInstance({
                    method: 'post',
                    url: '/sys/event/read/dics'
                }).then((resp) => {
                    this.allDicts = resp.data.dicts.REQUESTURI;
                })
                this.search ();
            },
            search () {
                this.$axiosInstance({
                    method: 'post',
                    url: '/sys/event/read/list',
                    data: this.searchParams
                }).then((resp) => {
                    this.pageList = resp.data.data;
                })
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
                this.searchParams.pageSize=pageSize;
                this.search();
            }
        },
        mounted () {
            this.init();
        }
    };
</script>
