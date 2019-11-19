<style lang="less">
    @import "../../styles/common.less";
</style>
<template>
    <div class="home-main">
        <row>
            <Col :md="6" :lg="6">
                <Select v-model="companyId" @on-change="searchTrend()" :transfer=true>
                    <Option v-for="item in datatype3" :value="item.id" :key="item.id">{{ item.dataText }}</Option>
                </Select>
            </Col>
        </row>
        <Row >
            <Col :md="10" :lg="10">
            <Row class-name="" :gutter="10">
                <Card>
                    <p slot="title" class="card-title">
                        <Icon type="android-checkbox-outline"></Icon>
                        舆论统计表
                    </p>
                    <div class="">
                        <Table :columns="columns1" :data="pageList.list" size="small"></Table>
                    </div>
                </Card>
            </Row>
            </Col>
            <Col :md="6" :lg="6">
                <Row class-name="" :gutter="10">
                    <Card>
                        <p slot="title" class="card-title">
                            <Icon type="android-checkbox-outline"></Icon>
                            情感属性
                        </p>

                        <div class="">
                        </div>
                    </Card>
                </Row>
            </Col>

            <Col :md="6" :lg="6">
                <Row class-name="" :gutter="10">
                    <Card>
                        <p slot="title" class="card-title">
                            <Icon type="android-checkbox-outline"></Icon>
                            媒体活跃度
                        </p>

                        <div class="">
                        </div>
                    </Card>
                </Row>
            </Col>
        </Row>
    </div>
</template>

<script>

export default {
    name: 'yq-manage',
    components: {
    },
    data () {
        return {
          datatype3: [],

          columns1: [
            {key: 'keyWords', title: '监测主体', width: 100,},
            {key: 'catAmount1', title: '舆情声量', width: 100,},
            {key: 'catAmount16', title: '敏感', width: 100,},
            {key: 'catAmount45', title: '正面', width: 100},
            {key: 'catAmount46', title: '负面', width: 100},
          ]

        };
    },
    computed: {

    },
    methods: {
        init () {
            this.$axiosInstance({
                method: 'post',
                url: '/pr/prDatatype/read/list',
                data: {
                    pageSize: '1000'
                }
            }).then((res) => {
                for (let i = 0; i < res.data.data.list.length ; i++) {
                    if(res.data.data.list[i].dataType === '3'){
                        this.datatype3.push(res.data.data.list[i]);
                        if(this.companyId === ''){
                            this.companyId = res.data.data.list[i].id;
                        }
                    }
                }
                this.searchTrend();
            });
        },
        searchTrend () {
          this.$axiosInstance({
            method: 'post',
            url: '/pr/prLeader/read/list',
            data: {
              pageSize: '10'
            }
          }).then((res) => {
            for (let i = 0; i < res.data.data.list.length; i++) {
              this.toDoList.push(res.data.data.list[i]);
            }
          });
          this.$axiosInstance({
            method: 'post',
            url: '/pr/prRealpublicopinion/read/list'
          }).then((res) => {
            for (let i = 0; i < res.data.data.list.length; i++) {
              this.alerNotices.push(res.data.data.list[i]);
            }
          });
        }
    },
    mounted () {
        this.init();
    }
};
</script>
