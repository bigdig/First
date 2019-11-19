define(['app', 'echarts'], function (app, echarts) {
    app.controller('portraitOverviewCtrl', ['$scope', '$state', '$timeout', 'ReqServ', function ($scope, $state, $timeout, ReqServ) {
        var areaChart, saleChart, tradeChart, activeChart;
        var width;
        var height = 300, top = '35%', right = '10%', center = '55%';
        var itemStyle = {
            normal: {},
            emphasis: {
                barBorderWidth: 1,
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowColor: 'rgba(0,0,0,0.5)'
            }
        };
        var areaOption = {
            title: {text: '客户地域分布'},
            tooltip: {
                trigger: 'item',
                formatter: "{b}:{c}({d}%)"
            },
            legend: {
                orient: 'vertical',
                right: right,
                top: top,
                data: []
            },
            series: [{
                type: 'pie',
                center: [top, center],
                data: [],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }]
        };
        var saleOption = {
            title: {text: '客户销售潜力分布'},
            tooltip: {
                trigger: 'item',
                formatter: "{b}:{c}({d}%)"
            },
            legend: {
                orient: 'vertical',
                right: right,
                top: top,
                data: []
            },
            series: [{
                type: 'pie',
                center: [top, center],
                data: [],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }]
        };
        var tradeOption = {
            title: {text: '客户行业分布'},
            tooltip: {
                trigger: 'item',
                formatter: "{b}:{c}({d}%)"
            },
            legend: {
                orient: 'vertical',
                right: right,
                top: top,
                data: []
            },
            series: [{
                type: 'pie',
                center: [top, center],
                data: [],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }]
        };
        var activeOption = {
            title: {text: '客户活跃趋势'},
            toolbox: {
                feature: {
                    magicType: {
                        type: ['stack', 'tiled']
                    },
                    dataView: {}
                }
            },
            tooltip: {
            },
            legend: {
                data: ['活跃客户', '非活跃客户']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [{
                type: 'category',
                data: [],
                axisTick: {
                    alignWithLabel: true
                }
            }],
            yAxis: [{
                type: 'value'
            }],
            series: [{
                name: '活跃客户',
                type: 'bar',
                stack: '客户',
                barWidth: top,
                itemStyle: itemStyle,
                data: []
            }, {
                name: '非活跃客户',
                type: 'bar',
                stack: '客户',
                barWidth: top,
                itemStyle: itemStyle,
                data: []
            }]
        };
        var loadAreaChart = function () {
            $('#area_chart').width(width).height(height);
            areaChart = echarts.init($('#area_chart').get(0));
            areaChart.showLoading();
            ReqServ.request('POST', 'ty/tyReport/read/custAreaDist', null).success(function (result) {
                areaChart.hideLoading();
                if (result.data) {
                    areaOption.legend.data.length = 0;
                    areaOption.series[0].data.length = 0;
                    result.data.forEach(function (value) {
                        areaOption.legend.data.push(value.groupName);
                        areaOption.series[0].data.push({value: value.groupNum, name: value.groupName})
                    });
                    areaChart.setOption(areaOption);
                }
            }).error(function () {
                areaChart.hideLoading();
            });
        };
        var loadSaleChart = function () {
            $('#sale_chart').width(width).height(height);
            saleChart = echarts.init($('#sale_chart').get(0));
            saleChart.showLoading();
            ReqServ.request('POST', 'ty/tyReport/read/custSaleDist', null).success(function (result) {
                saleChart.hideLoading();
                if (result.data) {
                    saleOption.legend.data.length = 0;
                    saleOption.series[0].data.length = 0;
                    result.data.forEach(function (value) {
                        saleOption.legend.data.push(value.groupName);
                        saleOption.series[0].data.push({value: value.groupNum, name: value.groupName})
                    });
                    saleChart.setOption(saleOption);
                }
            }).error(function () {
                saleChart.hideLoading();
            });
        };
        var loadTradeChart = function () {
            $('#trade_chart').width(width).height(height);
            tradeChart = echarts.init($('#trade_chart').get(0));
            tradeChart.showLoading();
            ReqServ.request('POST', 'ty/tyReport/read/custTradeDist', null).success(function (result) {
                tradeChart.hideLoading();
                if (result.data) {
                    tradeOption.legend.data.length = 0;
                    tradeOption.series[0].data.length = 0;
                    result.data.forEach(function (value) {
                        tradeOption.legend.data.push(value.groupName);
                        tradeOption.series[0].data.push({value: value.groupNum, name: value.groupName})
                    });
                    tradeChart.setOption(tradeOption);
                }
            }).error(function () {
                tradeChart.hideLoading();
            });
        };
        var loadActiveChart = function () {
            $('#active_chart').width(width).height(height);
            activeChart = echarts.init($('#active_chart').get(0));
            activeChart.showLoading();
            ReqServ.request('POST', 'ty/tyReport/read/custActiveTend', null).success(function (result) {
                activeChart.hideLoading();
                if (result.data) {
                    activeOption.series[0].data.length = activeOption.series[1].data.length = 0;
                    activeOption.xAxis[0].data.length = 0;
                    result.data.forEach(function (value) {
                        activeOption.xAxis[0].data.push(value.groupName);
                        activeOption.series[0].data.push(value.activeCustNum);
                        activeOption.series[1].data.push(value.noactiveCustNum);
                    });
                    activeChart.setOption(activeOption);
                }
            }).error(function () {
                activeChart.hideLoading();
            });
        };
        $timeout(function () {
            width = $('.report').width();
            loadAreaChart();
            loadSaleChart();
            loadTradeChart();
            loadActiveChart();
        },500);

    }])
});