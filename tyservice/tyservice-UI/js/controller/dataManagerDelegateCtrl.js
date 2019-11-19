define(['app'], function (app) {
    app.controller('dataManagerDelegateCtrl', ['$scope', '$state', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, ReqServ, ngDialog, appInterface) {
        $scope.delegate = {};
        $scope.delegateInfo = {};
        $scope.uploadDataModel.value = '单个添加委托课题';
        $scope.uploadDataModel.template = 'editDelegateTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加委托课题';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_wt.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyActivity/batchImport';
        $scope.uploadDataModel.scope = $scope;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加委托课题', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.delegateInfo.loading))
                        return;
                    ReqServ.clearObj($scope.delegateInfo);
                    $scope.delegateInfo.pageTitle = '添加委托课题';
                    $scope.delegateInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editDelegateTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.delegateInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.delegateInfo);
                    $scope.delegateInfo.pageTitle = '编辑委托课题';
                    $scope.delegateInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editDelegateTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                },
                isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.delegateInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.delegateInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyDctopic/delete', {id: item.source.id}).success(function (data) {
                        $scope.delegateInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchDelegate();
                    }).error(function (result) {
                        $scope.delegateInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                },
                isShow: true
            }],
            heads: [{title: '日期', dataKey: 'topicDate', width: 90},
                //{title: '服务类型', dataKey: 'topicTypeText', width: 120},
                {title: '服务内容', dataKey: 'title', width: 200},
                {title: '研究员', dataKey: 'researchers', width: 120},
                {title: '研究团队', dataKey: 'teams', width: 120},
                {title: '机构名称', dataKey: 'orgName', width: 200},
                {title: '对口销售', dataKey: 'serviceSaler', width: 90},
                {title: '参与客户', dataKey: 'customers', width: 200},
                {title: '所属区域', dataKey: 'teamCat', width: 90}],
            isScrollLoaded: true,
            controlWidth: 80
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.delegate.pageSize = $scope.searchModel.pageSize;
            $scope.delegate.beginDate = ReqServ.getDateNum($scope.delegate.startDateTime);
            $scope.delegate.endDate = ReqServ.getDateNum($scope.delegate.endDateTime);
            ReqServ.request('POST', 'ty/tyDctopic/read/list', $scope.delegate).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.serviceTypes) {
                    $scope.serviceTypes = result.dicts['TOPICTYPE'];
                    $scope.researchTeams = result.dicts['Researchdept'];
                    $scope.researchers = result.dicts['Researchers'];
                    $scope.areas = result.dicts['SysWorkAreas'];
                    $scope.institutionals = result.dictsEx['TyServiceorgs'];
                    $scope.salers = result.dictsEx['TyStafflists'];
                    $scope.delResearchers = result.dicts['ResearchersEx'];
                    $scope.delCustomers = [];
                }
                for (var i = 0; i < result.data.list.length; i++) {
                    result.data.list[i].topicDate = ReqServ.getDateString(result.data.list[i].beginDate + '');
                    var researchers = [], researcherIds = [], teams = [], customers = [], custIds = [];
                    if (result.data.list[i].staffList) {
                        for (var r = 0; r < result.data.list[i].staffList.length; r++) {
                            researchers.push(result.data.list[i].staffList[r].staffName);
                            researcherIds.push(result.data.list[i].staffList[r].id);
                            teams.push(result.data.list[i].staffList[r].deptName);
                        }
                    }
                    result.data.list[i].researchers = researchers.join(',');
                    result.data.list[i].researcherId = researcherIds.join(',');
                    result.data.list[i].teams = teams.join(',');
                    if (result.data.list[i].custList) {
                        for (var c = 0; c < result.data.list[i].custList.length; c++) {
                            customers.push(result.data.list[i].custList[c].custName);
                            custIds.push(result.data.list[i].custList[c].custId);
                        }
                        result.data.list[i].custId = custIds.join(',');
                        result.data.list[i].customers = customers.join(',');
                    }
                }
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.delegate);
            ReqServ.copyObj($scope.searchModel, $scope.delegate);
            loadData();
        });
        $scope.setSaler = function (data) {
            if ($scope.delegateInfo.orgId) {
                $scope.delCustomers.length = 0;
                ReqServ.request('POST', 'ty/tyOrgcustomer/read/custdict', {orgId: $scope.delegateInfo.orgId}).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        $('input[name=del_customer]').attr('readonly', true);
                        return;
                    }
                    for (var i = 0; i < result.data.custdicts.length; i++) {
                        $scope.delCustomers.push(result.data.custdicts[i]);
                    }
                    $('input[name=del_customer]').attr('readonly', $scope.delCustomers.length <= 0);
                }).error(function () {
                    $('input[name=del_customer]').attr('readonly', true);
                });
            }
            if (data) {
                $scope.delegateInfo.customers = null;
                var list = data.remark.split(','), salers = [], areas = [];
                for (var i = 0; i < list.length; i++) {
                    for (var j = 0; j < $scope.salers.length; j++) {
                        if (list[i] == $scope.salers[j].id) {
                            salers.push($scope.salers[j].text);
                            for (var m = 0; m < $scope.areas.length; m++) {
                                if ($scope.salers[j].remark == $scope.areas[m].id)
                                    areas.push($scope.areas[m].text);
                            }
                        }
                    }
                }
                $scope.delegateInfo.serviceSaler = salers.join(',');
                $scope.delegateInfo.teamCat = areas.join(',');
            }
        };
        $scope.searchDelegate = function () {
            $scope.delegate.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.delegate, $scope.searchModel);
            loadData();
        };
        $scope.editDelegate = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.delegateInfo);
            ReqServ.request('POST', $scope.delegateInfo.id ? 'ty/tyDctopic/update' : 'ty/tyDctopic/add', {
                id: $scope.delegateInfo.id,
                orgId: $scope.delegateInfo.orgId,
                beginDate: ReqServ.getDateNum($scope.delegateInfo.topicDate),
                // topicType: $scope.delegateInfo.topicType,
                title: $scope.delegateInfo.title,
                custIds: $scope.delegateInfo.custId.split(','),
                userIds: $scope.delegateInfo.researcherId.split(',')
            }).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.delegateInfo.id ? '保存' : '添加', null, $scope.delegateInfo);
                if (result.httpCode != appInterface.successCode) {
                    ReqServ.setBtnStatus(false, $scope.delegateInfo.id ? '保存' : '添加', null, $scope.delegateInfo);
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.delegateInfo.id) {
                    var teams = [], list = $scope.delegateInfo.researcherId.split(',');
                    for (var i = 0; i < $scope.delResearchers.length; i++) {
                        for (var j = 0; j < list.length; j++) {
                            if ($scope.delResearchers[i].id == list[j])
                                teams.push($scope.delResearchers[i].text.substring($scope.delResearchers[i].text.indexOf('(') + 1, $scope.delResearchers[i].text.length - 1));
                        }
                    }
                    $scope.delegateInfo.teams = teams.join(',');
                    ReqServ.copyObj($scope.delegateInfo, _item.source);
                    ReqServ.getTextValue($scope.serviceTypes, _item.source, 'topicType', 'topicTypeText');
                    _item.item = [{text: _item.source.topicDate},
                        // {text: _item.source.topicTypeText},
                        {text: _item.source.title},
                        {text: _item.source.researchers},
                        {text: _item.source.teams},
                        {text: _item.source.orgName},
                        {text: _item.source.serviceSaler},
                        {text: _item.source.customers},
                        {text: _item.source.teamCat}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.delegateInfo.id ? '保存' : '添加', null, $scope.delegateInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            $scope.delCustomers.length = 0;
            $scope.delegateInfo.dirty = false;
            $scope.setSaler();
        };
        $scope.clearData();
        loadData();
    }])
});