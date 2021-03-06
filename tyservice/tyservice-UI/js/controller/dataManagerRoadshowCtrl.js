define(['app'], function (app) {
    app.controller('dataManagerRoadshowCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, $stateParams, $timeout, ReqServ, ngDialog, appInterface) {
        $scope.roadshow = {activityType: '6'};
        $scope.roadshowInfo = {};
        $scope.uploadDataModel.value = '单个添加路演反路演信息';
        $scope.uploadDataModel.template = 'editRoadshowTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加路演反路演信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_ly.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyActivity/batchImport';
        $scope.uploadDataModel.scope = $scope;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加路演反路演信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.roadshowInfo.loading))
                        return;
                    ReqServ.clearObj($scope.roadshowInfo);
                    $scope.roadshowInfo.activityType = '6';
                    $scope.roadshowInfo.pageTitle = '添加路演反路演信息';
                    $scope.roadshowInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editRoadshowTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '客户', event: function (item) {
                    if ($scope.hasTaskRun($scope.roadshowInfo.loading))
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
                    if ($scope.hasTaskRun($scope.roadshowInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.roadshowInfo);
                    $scope.roadshowInfo.pageTitle = '编辑路演反路演信息';
                    $scope.roadshowInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editRoadshowTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                },
                isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.roadshowInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.roadshowInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyActivity/delete', {id: item.source.id}).success(function (data) {
                        $scope.roadshowInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchRoadshow();
                    }).error(function (result) {
                        $scope.roadshowInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                },
                isShow: true
            }],
            heads: [{title: '日期', dataKey: 'roadshowDate', width: 90},
                //{title: '服务内容', dataKey: 'title', width: 90},
                {title: '研究员', dataKey: 'roadshowers', width: 100},
                {title: '研究团队', dataKey: 'teams', width: 100},
                {title: '地点', dataKey: 'locale', width: 90},
                {title: '参与客户', dataKey: 'customer', width: 90},
                {title: '机构名称', dataKey: 'orgName', width: 90}],
            isScrollLoaded: true,
            controlWidth: 120
        };
        var loadLocalData = function () {
            $scope.loading.isShow = true;
            $scope.clearData();
            ReqServ.copyObj(JSON.parse(ReqServ.getStore('searchModel')), $scope.searchModel);
            ReqServ.copyObj($scope.searchModel, $scope.roadshow);
            $scope.researchTeams = JSON.parse(ReqServ.getStore('Researchdept'));
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
            $scope.roadshow.pageSize = $scope.searchModel.pageSize;
            $scope.roadshow.beginDate = ReqServ.getDateNum($scope.roadshow.startDateTime);
            $scope.roadshow.endDate = ReqServ.getDateNum($scope.roadshow.endDateTime);
            ReqServ.request('POST', 'ty/tyActivity/read/list', $scope.roadshow).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.researchTeams) {
                    $scope.researchTeams = result.dicts['Researchdept'];
                    $scope.researchers = result.dicts['Researchers'];
                    $scope.salResearchers = result.dicts['ResearchersEx'];
                    ReqServ.removeStore('Researchdept');
                    ReqServ.setStore('Researchdept', JSON.stringify($scope.researchTeams));
                    ReqServ.removeStore('Researchers');
                    ReqServ.setStore('Researchers', JSON.stringify($scope.researchers));
                    ReqServ.removeStore('ResearchersEx');
                    ReqServ.setStore('ResearchersEx', JSON.stringify($scope.salResearchers));
                }
                for (var i = 0; i < result.data.list.length; i++) {
                    result.data.list[i].roadshowDate = ReqServ.getDateString(result.data.list[i].beginDate + '');
                    //result.data.list[i].customer = result.data.list[i].custList && result.data.list[i].custList.length ? '已录入' : '未录入';
                    if(result.data.list[i].custList.length){
                        for(let cust of result.data.list[i].custList){
                            if(result.data.list[i].customer == undefined){
                                result.data.list[i].customer = cust.custName;
                            }else{
                                result.data.list[i].customer += "," + cust.custName;
                            }
                        }
                        result.data.list[i].orgName = result.data.list[i].custList[0].orgName;
                    }else{
                        result.data.list[i].customer = '未录入';
                    }
                    var roadshowers = [], roadshowerIds = [], teams = [];
                    if (result.data.list[i].staffList) {
                        for (var r = 0; r < result.data.list[i].staffList.length; r++) {
                            roadshowers.push(result.data.list[i].staffList[r].staffName);
                            roadshowerIds.push(result.data.list[i].staffList[r].id);
                            teams.push(result.data.list[i].staffList[r].deptName);
                        }
                    }
                    result.data.list[i].roadshowers = roadshowers.join(',');
                    result.data.list[i].roadshowerId = roadshowerIds.join(',');
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
            ReqServ.copyObj($scope.searchModel, $scope.roadshow);
            if (data)
                ReqServ.removeStore('data');
            loadData();
        });
        $scope.searchRoadshow = function () {
            ReqServ.removeStore('data');
            $scope.roadshow.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.roadshow, $scope.searchModel);
            loadData();
        };
        $scope.editRoadshow = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.roadshowInfo);
            $scope.roadshowInfo.beginDate = ReqServ.getDateNum($scope.roadshowInfo.roadshowDate);
            $scope.roadshowInfo.userIds = $scope.roadshowInfo.researchersId.split(',');
            ReqServ.request('POST', $scope.roadshowInfo.id ? 'ty/tyActivity/update' : 'ty/tyActivity/add', $scope.roadshowInfo).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.roadshowInfo.id ? '保存' : '添加', null, $scope.roadshowInfo);
                if (result.httpCode != appInterface.successCode) {
                    ReqServ.setBtnStatus(false, $scope.roadshowInfo.id ? '保存' : '添加', null, $scope.roadshowInfo);
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.roadshowInfo.id) {
                    var teams = [], list = $scope.roadshowInfo.researchersId.split(',');
                    for (var i = 0; i < $scope.salRoadshowers.length; i++) {
                        for (var j = 0; j < list.length; j++) {
                            if ($scope.salRoadshowers[i].id == list[j])
                                teams.push($scope.salRoadshowers[i].text.substring($scope.salRoadshowers[i].text.indexOf('(') + 1, $scope.salRoadshowers[i].text.length - 1));
                        }
                    }
                    $scope.roadshowInfo.teams = teams.join(',');
                    ReqServ.copyObj($scope.roadshowInfo, _item.source);
                    _item.item = [{text: _item.source.roadshowDate},
                        {text: _item.source.title},
                        {text: _item.source.roadshowers},
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
                ReqServ.setBtnStatus(false, $scope.roadshowInfo.id ? '保存' : '添加', null, $scope.roadshowInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            $scope.roadshowInfo.dirty = false;
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
            $state.go('master.datamanager.customer', {id: _item.source.id, index: _item.index, model: 'roadshow'});
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