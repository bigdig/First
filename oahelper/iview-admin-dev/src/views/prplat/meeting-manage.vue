<style lang="less">
    @import '../../styles/common.less';
    @import '../system/components/table.less';
</style>

<template>
    <div>
        <Row :gutter="10">
            <Col span="24">
            <Card>
                <row><h2 style="margin-bottom:20px">晨会</h2></row>
                <Row>
                    <Col span="14">
                    <span>晨会日期：</span>
                    <DatePicker v-model="searchParams.meetingDate" format="yyyy-MM-dd"
                                style="width: 150px"/>
                    <span>参会方式：</span>
                    <Select v-model="searchParams.attendenceWay" style="width:100px">
                        <Option v-for="item in meetingWay" :value="item.id" :key="item.id">{{ item.text }}
                        </Option>
                    </Select>
                    <!--<span>是否已结束：</span>-->
                    <!--<Select v-model="searchParams.isClosed" style="width:100px">-->
                    <!--<Option v-for="item in yes_no" :value="item.id" :key="item.id">{{ item.text }}-->
                    <!--</Option>-->
                    <!--</Select>-->
                    <span @click="search" style="margin: 0 10px;"><Button type="primary"
                                                                          icon="search">搜索</Button></span>
                    <Button @click="searchCancel" type="ghost">清空条件</Button>
                    </Col>
                    <Col span="10">
                    <Date-picker placeholder="导出开始日期" type="datetime"
                                v-model="bDate" @on-change="chgBegin" format="yyyy-MM-dd" style="width: 150px"></Date-picker>
                    <Date-picker placeholder="导出结束日期" type="datetime"
                                v-model="eDate"  @on-change="chgEnd" format="yyyy-MM-dd" style="width: 150px"></Date-picker>
                    <span @click="exportMonth" style="margin: 0 10px;">
                        <Button type="success">导出</Button>
                    </span>
                    <!--<Button @click="exportAll" type="ghost">导出所有</Button>-->
                    </Col>
                </Row>
                <Row class="margin-top-10 ">
                    <Table :columns="columns1" :data="pageList.list"></Table>
                </Row>
                <Row class="margin-top-10 ">
                    <Page :total="pageList.total" :page-size="pageList.pageSize" @on-change="changePage"
                          @on-page-size-change="changePageNum" show-elevator show-sizer></Page>
                </Row>
            </Card>
            </Col>
        </Row>
        <Modal width="600" v-model="picModal" title="审批" :mask-closable="false">
            <div>
                <img style="width: 100%" v-bind:src="this.picUrl"/>
            </div>
        </Modal>

    </div>

</template>

<script>

  export default {
    name: 'searchable-table',
    data () {
      return {
        bDate:null,
        eDate:null,
        begin_date:'',
        end_date:'',
        pageList: {},
        searchParams: {meetingDate: '', attendenceWay: ''},
        userParams: {},
        meetingWay: {},
        absenceWay: {},
        presentWay: {},
        allDicts: {},
        picUrl:'',
        picModal:false,
        columns1: [
          {
            key: 'meetingDate',
            title: '晨会日期'
          },
          {
            key: 'userName',
            title: '参会人'
          },
          {
            key: 'attendenceWayText',
            title: '出席方式'
          },
          {
            key: 'presentWayText',
            title: '现场参加地点'
          },
          {
            key: 'absenceWayText',
            title: '请假原因'
          },
          {
            key: 'absencePicture', title: '请假申请凭证',
            render: (h, params) => {
              if (params.row.absencePicture == null || params.row.absencePicture === '') {
                return
              } else {
                return h('div', [
                 /* h('a', {
                    domProps: {
                      href: params.row.absencePicture
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {}*/
                  h('Button', {
                    props: {
                      type: 'success',
                      size: 'small'
                    },
                    style: {
                      marginRight: '5px'
                    },
                    on: {
                      click: () => {
                        this.picModal = true
                        this.picUrl=params.row.absencePicture
                        //this.viewDetail(params.row.id)
                      }
                    }
                  }, '查看')])
              }
            }
          }
        ]
      }
    },
    methods: {
      init () {
        this.searchParams.pageSize = 10
        this.searchParams.pageNum = 1
        this.search()
      }
      ,
      search () {
        if (!(this.searchParams.meetingDate === '' || this.searchParams.meetingDate == null)) {
          var t = new Date(this.searchParams.meetingDate)
          var year = t.getFullYear()
          var month = (t.getMonth() + 1) < 10 ? '0' + (t.getMonth() + 1) : (t.getMonth() + 1)
          var day = t.getDate()
          var n = year + '-' + month + '-' + day
          this.searchParams.meetingDate = n
        }
        this.$axiosInstance({
          method: 'post',
          url: '/pr/goUserMorningMeeting/read/list',
          data: this.searchParams
        }).then((resp) => {
          this.meetingWay = resp.data.dicts.MEETING_WAY
          this.absenceWay = resp.data.dicts.ABSENCE_WAY
          this.presentWay = resp.data.dicts.PRESENT_WAY
          this.pageList = resp.data.data
        })
      }
      ,
      searchCancel () {
        this.searchParams = {}
        this.search()
      }
      ,
      changePage (currentPage) {
        this.searchParams.pageNum = currentPage
        this.search()
      }
      ,
      changePageNum (pageSize) {
        this.searchParams.pageSize = pageSize
        this.search()
      },
      chgBegin(e){
        console.log(e)
        this.begin_date=e
      },
      chgEnd(e){
        console.log(e)
        this.end_date=e
      },
      exportMonth () {
        debugger
        console.log(this.begin_date)
        console.log(this.end_date)
        if (this.begin_date === '' || this.end_date === '') {
          this.$Message.info('请选择开始日期和结束日期！')
          return
        }
        if (this.bDate > this.eDate ) {
          this.$Message.info('开始日期不能大于结束日期！')
          return
        }
//          var t = new Date(this.begin_date)
//          var year = t.getFullYear()
//          var month = (t.getMonth() + 1) < 10 ? '0' + (t.getMonth() + 1) : (t.getMonth() + 1)
//          var n = year + '-' + month
          var url = '/pr/goUserMorningMeeting/export' + '?beginDate=' + this.begin_date +'&endDate=' + this.end_date
          window.open(url)


      }
      ,
      exportAll () {
        window.open('/pr/goUserMorningMeeting/export')
      }
      ,

      removeEmpty (obj) {
        Object.keys(obj).forEach(function (key) {
          (obj[key] && typeof obj[key] === 'object') && this.removeEmpty(obj[key]) ||
          (obj[key] === undefined || obj[key] === null) && delete obj[key]
        })
        return obj
      }
      ,
    }
    ,
    mounted () {
      this.init()
    }
  }
</script>
