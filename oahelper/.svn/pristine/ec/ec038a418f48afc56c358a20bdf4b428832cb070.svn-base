<template>
    <div style="width:100%;height:100%;" id="service_request_con"></div>
</template>

<script>
import echarts from 'echarts';

export default {
    name: 'serviceRequests',
    props: {
//        yqDateList: Array,
//        yqAmountList: Array
        xyData:Object
    },
    mounted () {
        //this.drawLine();
    },
    methods: {
        drawLine(){
          console.log('begin drawline');
          if(this.xyData === null){
            console.log('finish drawline:data is null');
            return;
          }
            let option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        label: {
                            backgroundColor: '#6a7985'
                        }
                    }
                },
                legend: {
                    y: 'top',
                    data: ['中性', '敏感', '正面', '负面'],
                },
                color: ['#2b85e4', '#c09853', '#183691','#b94a48'],
                grid: {
                    top: '10%',
                    left: '1.2%',
                    right: '1%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: [
                    {
                        type: 'category',
                        boundaryGap: false,
                        data: this.xyData.dateList
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: '中性',
                        type: 'line',
                        smooth: true,
                        stack: '中性',
                        data: this.xyData.yqAmountList[0]
                    },
                    {
                        name: '敏感',
                        type: 'line',
                        smooth: true,
                        stack: '敏感',
                        data: this.xyData.yqAmountList[1]
                    },
                    {
                        name: '正面',
                        type: 'line',
                        smooth: true,
                        stack: '正面',
                        data: this.xyData.yqAmountList[2]
                    },
                    {
                        name: '负面',
                        type: 'line',
                        smooth: true,
                        stack: '负面',
                        data: this.xyData.yqAmountList[3]
                    }
                ]
            };
          console.log(this.xyData.dateList);
          console.log(this.xyData.yqAmountList);
//            if(this.xyData.dateList && this.xyData.yqAmountList && this.xyData.dateList.length>0 && this.xyData.yqAmountList>0){
              let serviceRequestCharts = echarts.init(document.getElementById('service_request_con'));
              serviceRequestCharts.setOption(option);

              window.addEventListener('resize', function () {
                serviceRequestCharts.resize();
              });
//            }
            console.log('finish drawline');
        }
    }
};
</script>