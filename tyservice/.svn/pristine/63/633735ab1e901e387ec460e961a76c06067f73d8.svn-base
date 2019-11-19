define(['app'], function (app) {
    app.controller('dataManagerLanguageCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, $stateParams, $timeout, ReqServ, ngDialog, appInterface) {
        $scope.language = {activityType: '4'};
        $scope.languageInfo = {};
        $scope.uploadDataModel.value = '单个添加语音微路演信息';
        $scope.uploadDataModel.template = 'editLanguageTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加语音微路演信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_yy.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyActivity/batchImport';
        $scope.uploadDataModel.scope = $scope;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加语音微路演信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.languageInfo.loading))
                        return;
                    ReqServ.clearObj($scope.languageInfo);
                    $scope.languageInfo.activityType = '4';
                    $scope.languageInfo.pageTitle = '添加语音微路演信息';
                    $scope.languageInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editLanguageTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '推荐股票', event: function (item) {
                    if ($scope.hasTaskRun($scope.languageInfo.loading))
                        return;
                    $state.go('master.datamanager.stock', {id: item.source.id, index: item.index, model: 'language'});
                }, isShow: true
            }, {
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.languageInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.languageInfo);
                    $scope.languageInfo.pageTitle = '编辑语音微路演信息';
                    $scope.languageInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editLanguageTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.languageInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.languageInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyActivity/delete', {id: item.source.id}).success(function (data) {
                        $scope.languageInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchLanguage();
                    }).error(function (result) {
                        $scope.languageInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                }, isShow: true
            }],
            heads: [{title: '日期', dataKey: 'setDate', width: 90},
                {title: '服务内容', dataKey: 'title', width: 200},
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
            ReqServ.copyObj($scope.searchModel, $scope.language);
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
            $scope.language.pageSize = $scope.searchModel.pageSize;
            $scope.language.beginDate = ReqServ.getDateNum($scope.language.startDateTime);
            $scope.language.endDate = ReqServ.getDateNum($scope.language.endDateTime);
            ReqServ.request('POST', 'ty/tyActivity/read/list', $scope.language).success(function (result) {
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
            ReqServ.clearObj($scope.language);
            ReqServ.copyObj($scope.searchModel, $scope.language);
            if (data)
                ReqServ.removeStore('data');
            loadData();
        });
        $scope.searchLanguage = function () {
            ReqServ.removeStore('data');
            $scope.language.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.language, $scope.searchModel);
            loadData();
        };
        $scope.editLanguage = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.languageInfo);
            $scope.languageInfo.beginDate = ReqServ.getDateNum($scope.languageInfo.setDate);
            $scope.languageInfo.userIds = $scope.languageInfo.researcherId.split(',');
            ReqServ.request('POST', $scope.languageInfo.id ? 'ty/tyActivity/update' : 'ty/tyActivity/add', $scope.languageInfo).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.languageInfo.id ? '保存' : '添加', null, $scope.languageInfo);
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.languageInfo.id) {
                    var team = '';
                    for (var j = 0; j < result.data.staffList.length; j++) {
                        if (team != '' && team.indexOf(result.data.staffList[j].deptName) >= 0)
                            continue;
                        team += result.data.staffList[j].deptName + ',';
                    }
                    $scope.languageInfo.teams = team != '' ? team.substring(0, team.length - 1) : null;
                    ReqServ.copyObj($scope.languageInfo, _item.source);
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
                ReqServ.setBtnStatus(false, $scope.languageInfo.id ? '保存' : '添加', null, $scope.languageInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            $scope.languageInfo.dirty = false;
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