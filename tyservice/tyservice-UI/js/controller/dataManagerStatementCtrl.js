define(['app'], function (app) {
    app.controller('dataManagerStatementCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, $stateParams, $timeout, ReqServ, ngDialog, appInterface) {
        $scope.statement = {};
        $scope.statementInfo = {};
        $scope.uploadDataModel.value = '单个添加晨会发言信息';
        $scope.uploadDataModel.template = 'editStatementTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加晨会发言信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_ch.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyActivity/batchImport';
        $scope.uploadDataModel.scope = $scope;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加晨会发言信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.statementInfo.loading))
                        return;
                    ReqServ.clearObj($scope.statementInfo);
                    $scope.statementInfo.pageTitle = '添加晨会发言信息';
                    $scope.statementInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editStatementTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '推荐股票', event: function (item) {
                    if ($scope.hasTaskRun($scope.statementInfo.loading))
                        return;
                    $state.go('master.datamanager.stock', {id: item.source.id, index: item.index, model: 'statement'});
                }, isShow: true
            }, {
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.statementInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.statementInfo);
                    $scope.statementInfo.pageTitle = '编辑晨会发言信息';
                    $scope.statementInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editStatementTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.statementInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.statementInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyDcmorningcon/delete', {id: item.source.id}).success(function (data) {
                        $scope.statementInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchStatement();
                    }).error(function (result) {
                        $scope.statementInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                }, isShow: true
            }],
            heads: [{title: '日期', dataKey: 'setDate', width: 90},
                {title: '发言概要', dataKey: 'title', width: 200},
                {title: '研究员', dataKey: 'researcherName', width: 150},
                {title: '研究团队', dataKey: 'teams', width: 150},
                {title: '股票推荐', dataKey: 'stock', width: 90}],
            isScrollLoaded: true,
            controlWidth: 140
        };
        var loadLocalData = function () {
            $scope.loading.isShow = true;
            $scope.clearData();
            ReqServ.copyObj(JSON.parse(ReqServ.getStore('searchModel')), $scope.searchModel);
            ReqServ.copyObj($scope.searchModel, $scope.statement);
            $scope.researchTeams = JSON.parse(ReqServ.getStore('Researchdept'));
            $scope.researchers = JSON.parse(ReqServ.getStore('Researchers'));
            $scope.stResearchers = JSON.parse(ReqServ.getStore('ResearchersEx'));
            dataModel.pageInfo = JSON.parse(ReqServ.getStore('pageInfo'));
            dataModel.data = JSON.parse(ReqServ.getStore('data'));
            dataModel.data[parseInt($stateParams.index)].stock = $stateParams.stock == 'true' ? '已推荐' : '未推荐';
            ReqServ.removeStore('data');
            ReqServ.setStore('data', JSON.stringify(dataModel.data));
            $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.statement.pageSize = $scope.searchModel.pageSize;
            $scope.statement.beginDate = ReqServ.getDateNum($scope.statement.startDateTime);
            $scope.statement.endDate = ReqServ.getDateNum($scope.statement.endDateTime);
            ReqServ.request('POST', 'ty/tyDcmorningcon/read/list', $scope.statement).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.researchTeams) {
                    $scope.researchTeams = result.dicts['Researchdept'];
                    $scope.researchers = result.dicts['Researchers'];
                    $scope.stResearchers = result.dicts['ResearchersEx'];
                    ReqServ.removeStore('Researchdept');
                    ReqServ.setStore('Researchdept', JSON.stringify($scope.researchTeams));
                    ReqServ.removeStore('Researchers');
                    ReqServ.setStore('Researchers', JSON.stringify($scope.researchers));
                    ReqServ.removeStore('ResearchersEx');
                    ReqServ.setStore('ResearchersEx', JSON.stringify($scope.stResearchers));
                }
                for (var i = 0; i < result.data.list.length; i++) {
                    result.data.list[i].stock = result.data.list[i].stockList ? '已推荐' : '未推荐';
                    result.data.list[i].setDate = ReqServ.getDateString(result.data.list[i].beginDate + '');
                    var researcher = '', team = '', userIds = '';
                    if (result.data.list[i].staffList) {
                        for (var j = 0; j < result.data.list[i].staffList.length; j++) {
                            researcher += result.data.list[i].staffList[j].staffName + ',';
                            userIds += result.data.list[i].staffList[j].id + ',';
                            if (team != '' && team.indexOf(result.data.list[i].staffList[j].deptName) >= 0)
                                continue;
                            team += result.data.list[i].staffList[j].deptName + ',';
                        }
                    }
                    result.data.list[i].researcherName = researcher != '' ? researcher.substring(0, researcher.length - 1) : null;
                    result.data.list[i].researcherId = userIds != '' ? userIds.substring(0, userIds.length - 1) : null;
                    result.data.list[i].teams = team != '' ? team.substring(0, team.length - 1) : null;
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
            ReqServ.copyObj($scope.searchModel, $scope.statement);
            if (data)
                ReqServ.removeStore('data');
            loadData();
        });
        $scope.searchStatement = function () {
            ReqServ.removeStore('data');
            $scope.statement.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.statement, $scope.searchModel);
            loadData();
        };
        $scope.editStatement = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.statementInfo);
            $scope.statementInfo.beginDate = ReqServ.getDateNum($scope.statementInfo.setDate);
            $scope.statementInfo.userIds = $scope.statementInfo.researcherId.split(',');
            ReqServ.request('POST', $scope.statementInfo.id ? 'ty/tyDcmorningcon/update' : 'ty/tyDcmorningcon/add', $scope.statementInfo).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.statementInfo.id ? '保存' : '添加', null, $scope.statementInfo);
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.statementInfo.id) {
                    var team = '';
                    for (var j = 0; j < result.data.staffList.length; j++) {
                        if (team != '' && team.indexOf(result.data.staffList[j].deptName) >= 0)
                            continue;
                        team += result.data.staffList[j].deptName + ',';
                    }
                    $scope.statementInfo.teams = team != '' ? team.substring(0, team.length - 1) : null;
                    ReqServ.copyObj($scope.statementInfo, _item.source);
                    _item.item = [{text: _item.source.setDate},
                        {text: _item.source.title},
                        {text: _item.source.researcherName},
                        {text: _item.source.teams},
                        {text: _item.source.stock}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.statementInfo.id ? '保存' : '添加', null, $scope.statementInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            $scope.statementInfo.dirty = false;
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