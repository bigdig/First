define(['app'], function (app) {
    app.controller('dataManagerOtherCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, $stateParams, $timeout, ReqServ, ngDialog, appInterface) {
        $scope.other = {activityType: '0'};
        $scope.otherInfo = {};
        $scope.uploadDataModel.value = '单个添加其他信息';
        $scope.uploadDataModel.template = 'editOtherTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加其他信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_qt.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyActivity/batchImport';
        $scope.uploadDataModel.scope = $scope;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加其他信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.otherInfo.loading))
                        return;
                    ReqServ.clearObj($scope.otherInfo);
                    $scope.otherInfo.activityType = '0';
                    $scope.otherInfo.pageTitle = '添加其他信息';
                    $scope.otherInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editOtherTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '客户', event: function (item) {
                    if ($scope.hasTaskRun($scope.otherInfo.loading))
                        return;
                    _item = item;
                    ngDialog.open({
                        template: 'meetingCustomerTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.custOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.otherInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.otherInfo);
                    $scope.otherInfo.pageTitle = '编辑其他信息';
                    $scope.otherInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editOtherTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                },
                isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.otherInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.otherInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyActivity/delete', {id: item.source.id}).success(function (data) {
                        $scope.otherInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchOther();
                    }).error(function (result) {
                        $scope.otherInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                },
                isShow: true
            }],
            heads: [{title: '日期', dataKey: 'otherDate', width: 90},
                {title: '服务内容', dataKey: 'title', width: 200},
                {title: '研究员', dataKey: 'researchers', width: 150},
                {title: '研究团队', dataKey: 'teams', width: 150},
                {title: '地点', dataKey: 'locale', width: 90},
                {title: '参与客户', dataKey: 'customer', width: 90}],
            isScrollLoaded: true,
            controlWidth: 120
        };
        var loadLocalData = function () {
            $scope.loading.isShow = true;
            $scope.clearData();
            ReqServ.copyObj(JSON.parse(ReqServ.getStore('searchModel')), $scope.searchModel);
            ReqServ.copyObj($scope.searchModel, $scope.other);
            $scope.otherTeams = JSON.parse(ReqServ.getStore('Researchdept'));
            $scope.researchers = JSON.parse(ReqServ.getStore('Researchers'));
            $scope.salResearchers = JSON.parse(ReqServ.getStore('ResearchersEx'));
            dataModel.pageInfo = JSON.parse(ReqServ.getStore('pageInfo'));
            dataModel.data = JSON.parse(ReqServ.getStore('data'));
            dataModel.data[parseInt($stateParams.index)].customer = $stateParams.customer == 'true' ? '已录入' : '未录入';
            ReqServ.removeStore('data');
            ReqServ.setStore('data', JSON.stringify(dataModel.data));
            $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.other.pageSize = $scope.searchModel.pageSize;
            $scope.other.beginDate = ReqServ.getDateNum($scope.other.startDateTime);
            $scope.other.endDate = ReqServ.getDateNum($scope.other.endDateTime);
            ReqServ.request('POST', 'ty/tyActivity/read/list', $scope.other).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.otherTeams) {
                    $scope.otherTeams = result.dicts['Researchdept'];
                    $scope.researchers = result.dicts['Researchers'];
                    $scope.salResearchers = result.dicts['ResearchersEx'];
                    ReqServ.removeStore('Researchdept');
                    ReqServ.setStore('Researchdept', JSON.stringify($scope.otherTeams));
                    ReqServ.removeStore('Researchers');
                    ReqServ.setStore('Researchers', JSON.stringify($scope.researchers));
                    ReqServ.removeStore('ResearchersEx');
                    ReqServ.setStore('ResearchersEx', JSON.stringify($scope.salResearchers));
                }
                for (var i = 0; i < result.data.list.length; i++) {
                    result.data.list[i].otherDate = ReqServ.getDateString(result.data.list[i].beginDate + '');
                    result.data.list[i].customer = result.data.list[i].custList && result.data.list[i].custList.length ? '已录入' : '未录入';
                    var researchers = [], researcherIds = [], teams = [];
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
                }
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                ReqServ.removeStore('searchModel');
                ReqServ.setStore('searchModel', JSON.stringify($scope.searchModel));
                ReqServ.removeStore('pageInfo');
                ReqServ.setStore('pageInfo', JSON.stringify(dataModel.pageInfo));
                if (ReqServ.getStore('data')) {
                    var _data = JSON.parse(ReqServ.getStore('data'));
                    ReqServ.removeStore('data');
                    ReqServ.setStore('data', JSON.stringify(_data.concat(dataModel.data)));
                } else
                    ReqServ.setStore('data', JSON.stringify(dataModel.data));
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('loadPageData', function (evt, data) {
            ReqServ.clearObj($scope.statement);
            ReqServ.copyObj($scope.searchModel, $scope.other);
            if (data)
                ReqServ.removeStore('data');
            loadData();
        });
        $scope.searchOther = function () {
            ReqServ.removeStore('data');
            $scope.other.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.other, $scope.searchModel);
            loadData();
        };
        $scope.editOther = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.otherInfo);
            $scope.otherInfo.beginDate = ReqServ.getDateNum($scope.otherInfo.otherDate);
            $scope.otherInfo.userIds = $scope.otherInfo.researcherId.split(',');
            ReqServ.request('POST', $scope.otherInfo.id ? 'ty/tyActivity/update' : 'ty/tyActivity/add', $scope.otherInfo).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.otherInfo.id ? '保存' : '添加', null, $scope.otherInfo);
                if (result.httpCode != appInterface.successCode) {
                    ReqServ.setBtnStatus(false, $scope.otherInfo.id ? '保存' : '添加', null, $scope.otherInfo);
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.otherInfo.id) {
                    var teams = [], list = $scope.otherInfo.researcherId.split(',');
                    for (var i = 0; i < $scope.salResearchers.length; i++) {
                        for (var j = 0; j < list.length; j++) {
                            if ($scope.salResearchers[i].id == list[j])
                                teams.push($scope.salResearchers[i].text.substring($scope.salResearchers[i].text.indexOf('(') + 1, $scope.salResearchers[i].text.length - 1));
                        }
                    }
                    $scope.otherInfo.teams = teams.join(',');
                    ReqServ.copyObj($scope.otherInfo, _item.source);
                    _item.item = [{text: _item.source.otherDate},
                        {text: _item.source.title},
                        {text: _item.source.researchers},
                        {text: _item.source.teams},
                        {text: _item.source.locale},
                        {text: _item.source.customer}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.otherInfo.id ? '保存' : '添加', null, $scope.otherInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            $scope.otherInfo.dirty = false;
        };
        $scope.custOpenedCallback = function () {
            ReqServ.request('POST', 'ty/tyActivitysign/read/list', {actId: _item.source.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    $scope.custList = [];
                    return;
                }
                $scope.custList = result.data ? result.data : [];
            }).error(function (result) {
                $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                $scope.custList = [];
            })
        };
        $scope.addCustomers = function () {
            $state.go('master.datamanager.customer', {id: _item.source.id, index: _item.index, model: 'other'});
        };
        if ($stateParams.index) {
            loadLocalData();
            $timeout(function () {
                var tr = $('.data').find('tr').eq(parseInt($stateParams.index) + appInterface.add);
                tr.addClass('active');
                $(document).scrollTop(tr.position().top - appInterface.height);
            }, 500);
            return;
        }
        ReqServ.removeStore('data');
        $scope.clearData();
        loadData();
    }])
});