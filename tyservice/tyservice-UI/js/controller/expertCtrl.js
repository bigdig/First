define(['app', 'echarts'], function (app, echarts) {
    app.controller('expertCtrl', ['$scope', '$state', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $state, ReqServ, appInterface, ngDialog) {
        $scope.expert = {};
        $scope.expertInfo = {required: true};
        $scope.uploadDataModel.value = '单个添加专家库信息';
        $scope.uploadDataModel.template = 'editExpertTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加专家库信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'ty_expert_temp.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyExpert/batchImport';
        $scope.uploadDataModel.scope = $scope;
        $scope.expertNames = [];
        if (!$scope.tabMenus[1].active)
            $scope.tabMenus[1].active = true;
        var closeOption = {
            title: {
                text: '关系程度分析图',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{b}:({d}%)"
            },
            legend: {
                left: 'center',
                bottom: 0,
                data: []
            },
            series: [{
                type: 'pie',
                radius: ['60%', '70%'],
                avoidLabelOverlap: false,
                label: {
                    normal: {
                        show: false,
                        position: 'center',
                        formatter: '{b|{b}}\n{c|{c}位}',
                        rich: {
                            b: {
                                lineHeight: 26
                            },
                            c: {
                                fontSize: 26,
                                fontWeight: 'bold'
                            }
                        }
                    },
                    emphasis: {
                        show: true
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                data: []
            }]
        };
        var industryOption = {
            title: {
                text: '对应行业紧密度统计图',
                x: 'center'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{b}:({d}%)"
            },
            legend: {
                left: 'center',
                bottom: 0,
                data: []
            },
            series: [{
                type: 'pie',
                radius: [0, '70%'],
                avoidLabelOverlap: false,
                label: {
                    normal: {
                        position: 'inner'
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                data: []
            }]
        };
        var _item, closeChart, industryChart, close_ele, industry_ele;
        var currentName = $state.current.name;
        var dataModel = {
            charts: [{
                title: '统计图',
                icon: 'icon icon-pie-chart',
                event: function () {
                    if ($scope.hasTaskRun($scope.expertInfo.loading))
                        return;
                    ngDialog.open({
                        template: 'chartTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.chartsOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            toolsBar: [{
                btnName: '添加专家库信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.expertInfo.loading))
                        return;
                    ReqServ.clearObj($scope.expertInfo);
                    if ($scope.userInfo.staff) {
                        $scope.expertInfo.industry = $scope.userInfo.staff.deptName;
                        $scope.expertInfo.brokerName = $scope.userInfo.staff.staffName;
                        $scope.expertInfo.brokerTel = $scope.userInfo.staff.tel;
                        $scope.expertInfo.brokerId = $scope.userInfo.staff.id;
                    }
                    $scope.expertInfo.pageTitle = '添加专家库信息';
                    $scope.expertInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editExpertTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.expertInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.expertInfo);
                    $scope.expertInfo.pageTitle = '编辑专家库信息';
                    $scope.expertInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editExpertTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '查看', event: function (item) {
                    ReqServ.copyObj(item.source, $scope.expertInfo);
                    ngDialog.open({
                        template: 'expertTemplate',
                        className: 'ngdialog-theme-plain',
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.expertInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.expertInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyExpert/delete', {id: item.source.id}).success(function (data) {
                        $scope.expertInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchExpert();
                    }).error(function (result) {
                        $scope.expertInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                }, isShow: ReqServ.isSysManager($scope.userInfo.role)
            }],
            heads: [{title: '类别', dataKey: 'expertTypeText', width: 90},
                {title: '联系人', dataKey: 'expertName', width: 90},
                {title: '所属公司', dataKey: 'orgName', width: 200},
                {title: '职位', dataKey: 'title', width: 120},
                {title: '手机', dataKey: 'expertTel', width: 90},
                {title: '所属行业', dataKey: 'industry', width: 12},
                {title: '对接人', dataKey: 'brokerName', width: 90},
                {title: '对接人电话', dataKey: 'brokerTel', width: 90},
                {title: '关系程度', dataKey: 'closeLevelText', width: 90},
                {title: '项目角色', dataKey: 'expertProjectRoleText', width: 120},
                {title: '来源', dataKey: 'source', width: 120}],
            isScrollLoaded: true,
            controlWidth: 80
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.expert.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'ty/tyExpert/read/list', $scope.expert).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.expertType) {
                    $scope.expertType = result.dicts['EXPERTTYPE'];
                    $scope.expertTypes = result.dicts['EXPERTTYPE'].concat();
                    $scope.closeLevel = result.dicts['CLOSELEVEL'];
                    $scope.closeLevels = result.dicts['CLOSELEVEL'].concat();
                    $scope.projectRole = result.dicts['EXPERT_PROJECTROLE'];
                    $scope.projectRoles = result.dicts['EXPERT_PROJECTROLE'].concat()
                }
                result.data.list.forEach(function (value) {
                    value.brokerName = value.deleteFlag == '1' ? '<span class="text-gray">' + value.brokerName + '</span>' : value.brokerName;
                });
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.expert);
            ReqServ.copyObj($scope.searchModel, $scope.expert);
            loadData();
        });
        $scope.chartsOpenedCallback = function () {
            close_ele = $('#close_chart');
            industry_ele = $('#industry_chart');
            var width = ($('.ngdialog-message').width() - 60) / 2;
            close_ele.width(width).height(width);
            industry_ele.width(width).height(width);
            closeChart = echarts.init(close_ele.get(0));
            industryChart = echarts.init(industry_ele.get(0));
            closeChart.showLoading();
            ReqServ.request('POST', 'ty/tyExpert/read/closeLevelNum', $scope.searchModel).success(function (result) {
                closeChart.hideLoading();
                if (result.data) {
                    closeOption.legend.data.length = 0;
                    closeOption.series[0].data.length = 0;
                    result.data.forEach(function (value) {
                        closeOption.legend.data.push(value.closeLevelText);
                        closeOption.series[0].data.push({value: value.closeLevelNum, name: value.closeLevelText})
                    });
                    closeChart.setOption(closeOption);
                }
            }).error(function () {
                closeChart.hideLoading();
            });
            industryChart.showLoading();
            ReqServ.request('POST', 'ty/tyExpert/read/industryNum', $scope.searchModel).success(function (result) {
                industryChart.hideLoading();
                if (result.data && result.data.list) {
                    industryOption.legend.data.length = 0;
                    industryOption.series[0].data.length = 0;
                    result.data.list.forEach(function (value, index) {
                        industryOption.legend.data.push(value.industry);
                        industryOption.series[0].data.push({
                            value: value.industryNum,
                            name: value.industry
                        })
                    });
                    industryChart.setOption(industryOption);
                }
            }).error(function () {
                industryChart.hideLoading();
            });
        };
        $scope.searchExpert = function () {
            $scope.expert.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.expert, $scope.searchModel);
            loadData();
        };
        $scope.setExpert = function (event) {
            if (typeof event == 'function') {
                ReqServ.request('POST', 'ty/tyOrgcustomer/read/searchByCondition', {custName: $scope.expertInfo.expertName}).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                        return;
                    }
                    $scope.expertNames.length = 0;
                    result.data.forEach(function (value) {
                        value.text = value.custName;
                        $scope.expertNames.push(value);
                    });
                    event();
                }).error(function (result) {
                    ngDialog.close();
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                });
            }
            else {
                $scope.expertInfo.expertId = event;
                for (var i = 0; i < $scope.expertNames.length; i++) {
                    if ($scope.expertNames[i].id == event) {
                        $scope.expertInfo.orgName = $scope.expertNames[i].orgName;
                        $scope.expertInfo.orgId = $scope.expertNames[i].orgId;
                        $scope.expertInfo.expertTel = $scope.expertNames[i].custMobile;
                        $scope.expertInfo.title = $scope.expertNames[i].title;
                        $scope.expertInfo.industry = $scope.expertNames[i].industry;
                        break;
                    }
                }
            }
        };
        $scope.editExpert = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.expertInfo);
            ReqServ.request('POST', $scope.expertInfo.id ? 'ty/tyExpert/update' : 'ty/tyExpert/add', $scope.expertInfo).success(function (data) {
                ReqServ.setBtnStatus(false, $scope.expertInfo.id ? '保存' : '添加', null, $scope.expertInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                if ($scope.expertInfo.id) {
                    var i;
                    for (i = 0; $scope.expertTypes.length; i++) {
                        if ($scope.expertTypes[i].id == $scope.expertInfo.expertType) {
                            $scope.expertInfo.expertTypeText = $scope.expertTypes[i].text;
                            break;
                        }
                    }
                    for (i = 0; $scope.closeLevels.length; i++) {
                        if ($scope.closeLevels[i].id == $scope.expertInfo.closeLevel) {
                            $scope.expertInfo.closeLevelText = $scope.closeLevels[i].text;
                            break;
                        }
                    }
                    for (i = 0; $scope.projectRole.length; i++) {
                        if ($scope.projectRole[i].id == $scope.expertInfo.expertProjectRole) {
                            $scope.expertInfo.expertProjectRoleText = $scope.projectRole[i].text;
                            break;
                        }
                    }
                    ReqServ.copyObj($scope.expertInfo, _item.source);
                    _item.item = [{text: _item.source.expertTypeText},
                        {text: _item.source.expertName},
                        {text: _item.source.orgName},
                        {text: _item.source.title ? _item.source.title : '-'},
                        {text: _item.source.expertTel},
                        {text: _item.source.industry},
                        {text: _item.source.brokerName},
                        {text: _item.source.brokerTel},
                        {text: _item.source.closeLevelText},
                        {text: _item.source.expertProjectRoleText},
                        {text: _item.source.source ? _item.source.source : '-'}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.expertInfo.id ? '保存' : '添加', null, $scope.expertInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.clearData();
        loadData();
    }])
});