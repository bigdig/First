define(['app', 'echarts'], function (app, echarts) {
    app.controller('homeMyCustCtrl', ['$scope', '$timeout', '$state', 'ReqServ', 'appInterface', function ($scope, $timeout, $state, ReqServ, appInterface) {
        $scope.welcome.isHide = true;
        if (!$scope.tabMenus[0].active)
            $scope.tabMenus[0].active = true;
        $scope.myCust = {};
        var saleChart, activeChart, tradeChart, areaChart;
        var width;
        var height = 300, top = '35%', right = '5%', center = '60%';
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
            tooltip: {},
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
        $scope.timespan = [{name: '一个月', active: false, value: -1},
            {name: '二个月', active: false, value: -2},
            {name: '三个月', active: false, value: -3},
            {name: '半年', active: false, value: -6},
            {name: '一年', active: false, value: -12}];
        $scope.whiteDeadline = [{name: '7天后到期', active: false, value: 1},
            {name: '15天后到期', active: false, value: 2}];
        $scope.getCustByTimespan = function (timespan) {
            $scope.timespan.forEach(function (value) {
                value.active = value.name == timespan.name;
            });
            ReqServ.request('POST', 'ty/tyReport/read/myCust', {month: timespan.value}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert((result && result.message) ? result.message : appInterface.alert);
                    return;
                }
                ReqServ.copyObj(result, $scope.myCust);
            }).error(function (result) {
                $scope.showAlert((result && result.message) ? result.message : appInterface.alert);
            })
        };
        $scope.getWhiteDeadLine = function (flag) {
            $scope.whiteDeadline.forEach(function (value) {
                value.active = value.name == flag.name;
            });
            ReqServ.request('POST', 'ty/tyReport/read/getDeadlineOrgs', {whiteDeadline: flag.value}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert((result && result.message) ? result.message : appInterface.alert);
                    return;
                }
                $scope.myCust.whileDeadLineNumber = result.data ? result.data : 0;
            }).error(function (result) {
                $scope.showAlert((result && result.message) ? result.message : appInterface.alert);
            })
        };
        $scope.setWhiteDeadLine = function () {
            var flag = $scope.whiteDeadline.filter(function (value) {
                return value.active == true;
            });
            $state.go('master.institutional', {flag: flag[0].value});
        };
        $scope.getCustByTimespan($scope.timespan[0]);
        $scope.getWhiteDeadLine($scope.whiteDeadline[0]);
        $timeout(function () {
            width = ($('.res-head').width() - 30) / 2;
            loadAreaChart();
            loadSaleChart();
            loadTradeChart();
            loadActiveChart();
        }, 500);
    }])
});