define(['app', 'echarts', 'echartsWordcloud'], function (app, echarts,echartsWordcloud) {
    app.controller('portraitSearchCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ngDialog', 'appInterface', 'ReqServ', function ($scope, $state, $stateParams, $timeout, ngDialog, appInterface, ReqServ) {
        if (!$stateParams.custId) {
            $state.go($stateParams.from);
            return;
        }
        $scope.welcome.isBack = !!$stateParams.index;
        $scope.tagData = {submit: false};
        $scope.searchCust = {};
        var currentName = $state.current.name;
        var wordoption = {
            title: {
                text: '标签'
            },
            tooltip: {
                show: true
            },
            series: [{
                name: '',
                type: 'wordCloud',
                sizeRange: [16, 30],
                rotationRange: [0, 0],
                gridSize : 20,
                textPadding: 0,
                autoSize: {
                    enable: true,
                    minSize: 6
                },
                textStyle: {
                    normal: {
                        color: function() {
                            return 'rgb(' + [
                                Math.round(Math.random() * 160),
                                Math.round(Math.random() * 160),
                                Math.round(Math.random() * 160)
                            ].join(',') + ')';
                        }
                    },
                    emphasis: {
                        shadowBlur: 3,
                        shadowColor: '#ccc'
                    }
                },
                data: [
                    {
                        name: "Planet Fitness",
                        value: 4
                    },
                    {
                        name: "Pitch Perfect",
                        value: 3
                    },
                    {
                        name: "Express5555555",
                        value: 5
                    },
                    {
                        name: "Express4444444",
                        value: 6
                    },
                    {
                        name: "Express233333",
                        value: 7
                    },
                    {
                        name: "Express22222222",
                        value: 8
                    },
                    {
                        name: "Express1111111",
                        value: 9
                    },
                    {
                        name: "Sam S Club",
                        value: 1
                    },
                ]
            }]
        };
        var dataModel = {
            isList: true,
            heads: [],
            isScrollLoaded: true,
            charts: {
                event: function () {
                    ngDialog.open({
                        template: 'counterTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.counterChartOpenedCallback,
                        scope: $scope
                    });
                }
            },
        };
        $scope.counterChartOpenedCallback = function () {
            loadData();
            wordoption.series[0].data=[]
            for(var i=0;i<$scope.customerInfo.custLabelList.length;i++){
                var labelCat=$scope.customerInfo.custLabelList[i]
                wordoption.series[0].data.push({name:labelCat.labelName,value:(labelCat.validCount)+''})
            }
            width = $('.ngdialog-message').width();
            $('#counter_chart').width(width).height(parseFloat(width)/2.5);
            // 基于准备好的dom，初始化echarts图表 
            var myChart = echarts.init(document.getElementById('counter_chart')); 
            // 为echarts对象加载数据 
            myChart.setOption(wordoption);  
        };

        var datalist = [];
        var getType = function (name) {
            var type;
            switch (name) {
                case '电话会议' :
                    type = 1;
                    break;
                case '委托课题' :
                    type = 2;
                    break;
                case '调研' :
                    type = 5;
                    break;
                case '路演反路演' :
                    type = 6;
                    break;
                case '沙龙' :
                    type = 7;
                    break;
                case '其他' :
                    type = 0;
                    break;
                case '午/晚餐交流' :
                    type = 8;
                    break;
                case '专项服务' :
                    type = 9;
                    break;
            }
            return type;
        };
        var loadData = function () {
            ReqServ.request('POST', 'ty/tyOrgcustomer/read/detail', {id: $stateParams.custId}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                $scope.customerInfo = result.data;
            }).error(function (result) {
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        var loadActivityRecord = function () {
            $scope.loading.isShow = true;
            $scope.searchCust.custId = $stateParams.custId;
            $scope.searchCust.beginDateStart = ReqServ.getDateNum($scope.searchCust.startDateTime);
            $scope.searchCust.beginDateEnd = ReqServ.getDateNum($scope.searchCust.endDateTime);
            ReqServ.request('POST', 'ty/tyReport/read/activityList', $scope.searchCust).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!dataModel.heads.length) {
                    var headlast;
                    for (var property in result.dicts) {
                        if (property == '其他') {
                            headlast = {title: property + '(' + result.dicts[property] + ')', type: getType(property)};
                            continue;
                        }
                        dataModel.heads.push({
                            title: property + '(' + result.dicts[property] + ')',
                            type: getType(property)
                        })
                    }
                    if (headlast)
                        dataModel.heads.push(headlast);
                }
                datalist.length = 0;
                result.data.list.forEach(function (value) {
                    for (var property in value) {
                        datalist.push({text: value[property], type: getType((property + '').split(',')[0])});
                    }
                });
                dataModel.data = datalist;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('goBack', function (evt) {
            $state.go($stateParams.from, {index: $stateParams.index});
        });
        $scope.sendToCustomer = function (kind) {
            $scope.isFromPortrait.isYes = true;
            $scope.selectedCustomers.push({
                id: $scope.customerInfo.id,
                name: $scope.customerInfo.custName,
                custEmail: $scope.customerInfo.custEmail
            });
            $scope.messageInfo.total = $scope.selectedCustomers.length;
            $scope.messageInfo.isAll = false;
            ngDialog.open({
                template: kind == 'message' ? 'sendMessageTemplate' : 'sendEmailTemplate',
                className: 'ngdialog-theme-default',
                scope: $scope.$parent,
                onOpenCallback: kind == 'message' ? $scope.$parent.openedCallback : $scope.$parent.openedEmailCallback,
                preCloseCallback: $scope.$parent.preCloseCallback,
                data: 'detail'
            });
        };
        $scope.addTag = function (e, invalid) {
            var keyCode = window.event ? e.keyCode : e.which;
            if (keyCode != 13 || !$scope.tagData.label.replace(/(^s*)|(s*$)/g, "").length || invalid || $scope.tagData.submit) {
                $scope.searchKey(e, $scope.tagData);
                return;
            }
            $scope.tagData.submit = true;
            ReqServ.request('POST', 'ty/tyLabel/addCustLabel', {
                custId: $scope.customerInfo.id,
                labelName: $scope.tagData.label
            }).success(function (result) {
                $scope.tagData.submit = false;
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg);
                    return;
                }
                if (result.data.isExists == '1') {
                    $scope.blur($scope.tagData);
                    return;
                }
                var hsaCatId;
                for (var i = 0; i < $scope.customerInfo.labelCatList.length; i++) {
                    if ($scope.customerInfo.labelCatList[i].catId == result.data.catId) {
                        $scope.customerInfo.labelCatList[i].subList.push({
                            id: result.data.id,
                            labelName: result.data.labelName
                        });
                        hsaCatId = true;
                        break;
                    }
                }
                if (!hsaCatId)
                    $scope.customerInfo.labelCatList.push({
                        catId: result.data.catId,
                        catName: result.data.catName,
                        subList: [{id: result.data.id, labelName: result.data.labelName}]
                    });
                $scope.blur($scope.tagData);
            }).error(function (result) {
                $scope.tagData.submit = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.onBlur = function () {
            $timeout(function () {
                $scope.closeSearch();
            }, 150);
            if ($scope.tagData.label)
                return;
            $scope.blur($scope.tagData);
        };
        $scope.searchActivity = function () {
            $scope.searchCust.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.searchCust, $scope.searchModel);
            loadActivityRecord();
        };
        $scope.focus = function (tag) {
            $('.tag li').last().removeClass('hide');
            $('.search li').last().removeClass('hide');
            $("input[name='" + tag + "']").focus();
        };
        $scope.blur = function (dataModel) {
            $('.tag li').last().addClass('hide');
            $('.search li').last().addClass('hide');
            $('.form').addClass('hide');
            ReqServ.clearObj(dataModel);
        };
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.searchCust);
            ReqServ.copyObj($scope.searchModel, $scope.searchCust);
            loadActivityRecord();
        });
        $scope.clearData();
        loadData();
        loadActivityRecord();
    }])
});