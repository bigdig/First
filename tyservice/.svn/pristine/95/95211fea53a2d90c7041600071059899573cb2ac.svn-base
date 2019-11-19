define(['app'], function (app) {
    app.controller('dataManagerSalonCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, $stateParams, $timeout, ReqServ, ngDialog, appInterface) {
        $scope.salon = {};
        $scope.salonInfo = {};
        $scope.subActFlags = [{id: '1', text: '独立子项'}, {id: '2', text: '分时子项'}];
        $scope.uploadDataModel.value = '单个添加沙龙信息';
        $scope.uploadDataModel.template = 'editSalonTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加沙龙信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_sl.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyActivity/batchImport';
        $scope.uploadDataModel.scope = $scope;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加沙龙信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.salonInfo.loading))
                        return;
                    ReqServ.clearObj($scope.salonInfo);
                    $scope.salonInfo.pageTitle = '添加沙龙信息';
                    $scope.salonInfo.btnName = '添加';
                    $scope.salonInfo.isAdd = true;
                    $scope.salonInfo.subActFlag = '0';
                    ngDialog.open({
                        template: 'editSalonTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '客户', event: function (item) {
                    if ($scope.hasTaskRun($scope.salonInfo.loading))
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
                    if ($scope.hasTaskRun($scope.salonInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.salonInfo);
                    $scope.salonInfo.pageTitle = '编辑沙龙信息';
                    $scope.salonInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editSalonTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                },
                isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.salonInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.salonInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyDcsalon/delete', {id: item.source.id}).success(function (data) {
                        $scope.salonInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchSalon();
                    }).error(function (result) {
                        $scope.salonInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                },
                isShow: true
            }, {
                name: '添加子项', event: function (item) {
                    if ($scope.hasTaskRun($scope.salonInfo.loading))
                        return;
                    _item = item;
                    _item.source.parentActId = _item.source.id;
                    _item.source.id = _item.source.subActFlag = null;
                    ReqServ.copyObj(_item.source, $scope.salonInfo);
                    $scope.salonInfo.pageTitle = '添加子项';
                    $scope.salonInfo.btnName = '添加';
                    $scope.salonInfo.title = null;
                    ngDialog.open({
                        template: 'editSalonTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                },
                isShow: true
            }],
            heads: [{title: '日期', dataKey: 'salonDate', width: 90},
                {title: '时间', dataKey: 'meetingTime', width: 150},
                {title: '服务内容', dataKey: 'title', width: 200},
                {title: '研究员', dataKey: 'researchers', width: 150},
                {title: '研究团队', dataKey: 'teams', width: 150},
                {title: '地点', dataKey: 'locale', width: 90},
                {title: '参与客户', dataKey: 'customer', width: 90}],
            isScrollLoaded: true,
            controlWidth: 180
        };
        var getChildren = function (obj) {
            obj.salonDate = ReqServ.getDateString(obj.beginDate + '');
            obj.customer = obj.custList && obj.custList.length ? '已录入' : '未录入';
            obj.slStartTime = obj.beginTime ? ReqServ.getTimeString(obj.beginTime + '') : '';
            obj.slEndTime = obj.endTime ? ReqServ.getTimeString(obj.endTime + '') : '';
            obj.meetingTime = (obj.beginTime ? obj.slStartTime : '') + '-' + (obj.endTime ? obj.slEndTime : '');
            var researchers = [], researcherIds = [], teams = [];
            if (obj.staffList) {
                for (var r = 0; r < obj.staffList.length; r++) {
                    researchers.push(obj.staffList[r].staffName);
                    researcherIds.push(obj.staffList[r].id);
                    teams.push(obj.staffList[r].deptName);
                }
            }
            obj.researchers = researchers.join(',');
            obj.researcherId = researcherIds.join(',');
            obj.teams = teams.join(',');
            obj.children = obj.subActList ? obj.subActList : [];
            obj.children.forEach(function (item) {
                getChildren(item);
            });
        };
        var loadLocalData = function () {
            $scope.loading.isShow = true;
            $scope.clearData();
            ReqServ.copyObj(JSON.parse(ReqServ.getStore('searchModel')), $scope.searchModel);
            ReqServ.copyObj($scope.searchModel, $scope.salon);
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
            $scope.salon.pageSize = $scope.searchModel.pageSize;
            $scope.salon.beginDate = ReqServ.getDateNum($scope.salon.startDateTime);
            $scope.salon.endDate = ReqServ.getDateNum($scope.salon.endDateTime);
            ReqServ.request('POST', 'ty/tyDcsalon/read/list', $scope.salon).success(function (result) {
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
                if (result.data.list)
                    result.data.list.forEach(function (value) {
                        getChildren(value);
                    });
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
            ReqServ.copyObj($scope.searchModel, $scope.salon);
            if (data)
                ReqServ.removeStore('data');
            loadData();
        });
        $scope.searchSalon = function () {
            ReqServ.removeStore('data');
            $scope.salon.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.salon, $scope.searchModel);
            loadData();
        };
        $scope.editSalon = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.salonInfo);
            ReqServ.request('POST', $scope.salonInfo.id ? 'ty/tyDcsalon/update' : 'ty/tyDcsalon/add', {
                id: $scope.salonInfo.id,
                beginDate: ReqServ.getDateNum($scope.salonInfo.salonDate),
                beginTime: ReqServ.getTimeNum($scope.salonInfo.slStartTime),
                endTime: ReqServ.getTimeNum($scope.salonInfo.slEndTime),
                title: $scope.salonInfo.title,
                locale: $scope.salonInfo.locale,
                userIds: $scope.salonInfo.researcherId.split(','),
                subActFlag: $scope.salonInfo.subActFlag,
                parentActId: $scope.salonInfo.parentActId
            }).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.salonInfo.id ? '保存' : '添加', null, $scope.salonInfo);
                if (result.httpCode != appInterface.successCode) {
                    ReqServ.setBtnStatus(false, $scope.salonInfo.id ? '保存' : '添加', null, $scope.salonInfo);
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.salonInfo.id) {
                    var teams = [], list = $scope.salonInfo.researcherId.split(',');
                    for (var i = 0; i < $scope.salResearchers.length; i++) {
                        for (var j = 0; j < list.length; j++) {
                            if ($scope.salResearchers[i].id == list[j])
                                teams.push($scope.salResearchers[i].text.substring($scope.salResearchers[i].text.indexOf('(') + 1, $scope.salResearchers[i].text.length - 1));
                        }
                    }
                    $scope.salonInfo.teams = teams.join(',');
                    ReqServ.copyObj($scope.salonInfo, _item.source);
                    _item.item = [{text: _item.source.salonDate},
                        {text: _item.source.slStartTime + '-' + _item.source.slEndTime},
                        {text: _item.source.title},
                        // {text: _item.source.content},
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
                ReqServ.setBtnStatus(false, $scope.salonInfo.id ? '保存' : '添加', null, $scope.salonInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            $scope.salonInfo.dirty = false;
        };
        $scope.custOpenedCallback = function () {
            ReqServ.request('POST', 'ty/tyActivitysign/read/list', {actId: _item.source.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    $scope.custList=[];
                    return;
                }
                $scope.custList = result.data ? result.data : [];
            }).error(function (result) {
                $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                $scope.custList=[];
            })
        };
        $scope.addCustomers = function () {
            $state.go('master.datamanager.customer', {id: _item.source.id, index: _item.index, model: 'salon'});
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