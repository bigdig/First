define(['app'], function (app) {
    app.controller('dataManagerMeetingCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, $stateParams, $timeout, ReqServ, ngDialog, appInterface) {
        $scope.meeting = {activityType: '1'};
        $scope.meetingInfo = {};
        $scope.uploadDataModel.scope = $scope;
        $scope.custList = [];
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加电话会议信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.meetingInfo.loading))
                        return;
                    ReqServ.clearObj($scope.meetingInfo);
                    $scope.meetingInfo.activityType = '1';
                    $scope.meetingInfo.pageTitle = '添加电话会议信息';
                    $scope.meetingInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editMeetingTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '客户', event: function (item) {
                    if ($scope.hasTaskRun($scope.meetingInfo.loading))
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
                    if ($scope.hasTaskRun($scope.meetingInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.meetingInfo);

                    $scope.meetingInfo.pageTitle = '编辑电话会议信息';
                    $scope.meetingInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editMeetingTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                },
                isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.meetingInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.meetingInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyActivity/delete', {id: item.source.id}).success(function (data) {
                        $scope.meetingInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchMeeting();
                    }).error(function (result) {
                        $scope.meetingInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                },
                isShow: true
            }],
            heads: [{title: '日期', dataKey: 'meetingDate', width: 90},
                {title: '服务内容', dataKey: 'title', width: 200},
                {title: '研究员', dataKey: 'researchers', width: 150},
                {title: '研究团队', dataKey: 'teams', width: 150},
                {title: '会议时间', dataKey: 'meetingTime', width: 150},
                {title: '地点', dataKey: 'locale', width: 90},
                {title: '参与客户', dataKey: 'customer', width: 90}],
            isScrollLoaded: true,
            controlWidth: 120
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.meeting.pageSize = $scope.searchModel.pageSize;
            $scope.meeting.beginDate = ReqServ.getDateNum($scope.meeting.startDateTime);
            $scope.meeting.endDate = ReqServ.getDateNum($scope.meeting.endDateTime);
            ReqServ.request('POST', 'ty/tyActivity/read/list', $scope.meeting).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.researchTeams) {
                    $scope.researchTeams = result.dicts['Researchdept'];
                    $scope.researchers = result.dicts['Researchers'];
                    $scope.salResearchers = result.dicts['ResearchersEx'];
                }
                for (var i = 0; i < result.data.list.length; i++) {
                    result.data.list[i].meetingDate = ReqServ.getDateString(result.data.list[i].beginDate + '');
                    result.data.list[i].metStartTime = result.data.list[i].beginTime ? ReqServ.getTimeString(result.data.list[i].beginTime + '') : '';
                    result.data.list[i].metEndTime = result.data.list[i].endTime ? ReqServ.getTimeString(result.data.list[i].endTime + '') : '';
                    result.data.list[i].meetingTime = (result.data.list[i].beginTime ? result.data.list[i].metStartTime : '') + '-' + (result.data.list[i].endTime ? result.data.list[i].metEndTime : '');
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
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('loadPageData', function (evt, data) {
            ReqServ.clearObj($scope.statement);
            ReqServ.copyObj($scope.searchModel, $scope.meeting);
            if (data)
                ReqServ.removeStore('data');
            loadData();
        });
        $scope.searchMeeting = function () {
            ReqServ.removeStore('data');
            $scope.meeting.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.meeting, $scope.searchModel);
            loadData();
        };
        $scope.editMeeting = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.meetingInfo);
            $scope.meetingInfo.beginDate = ReqServ.getDateNum($scope.meetingInfo.meetingDate);
            $scope.meetingInfo.beginTime = ReqServ.getTimeNum($scope.meetingInfo.metStartTime);
            $scope.meetingInfo.endTime = ReqServ.getTimeNum($scope.meetingInfo.metEndTime);
            $scope.meetingInfo.userIds = $scope.meetingInfo.researcherId.split(',');
            ReqServ.request('POST', $scope.meetingInfo.id ? 'ty/tyActivity/update' : 'ty/tyActivity/add', $scope.meetingInfo).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.meetingInfo.id ? '保存' : '添加', null, $scope.meetingInfo);
                if (result.httpCode != appInterface.successCode) {
                    ReqServ.setBtnStatus(false, $scope.meetingInfo.id ? '保存' : '添加', null, $scope.meetingInfo);
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.meetingInfo.id) {
                    var teams = [], list = $scope.meetingInfo.researcherId.split(',');
                    for (var i = 0; i < $scope.salResearchers.length; i++) {
                        for (var j = 0; j < list.length; j++) {
                            if ($scope.salResearchers[i].id == list[j])
                                teams.push($scope.salResearchers[i].text.substring($scope.salResearchers[i].text.indexOf('(') + 1, $scope.salResearchers[i].text.length - 1));
                        }
                    }
                    $scope.meetingInfo.teams = teams.join(',');
                    ReqServ.copyObj($scope.meetingInfo, _item.source);
                    _item.item = [{text: _item.source.meetingDate},
                        {text: _item.source.title},
                        {text: _item.source.researchers},
                        {text: _item.source.teams},
                        {text: _item.source.metStartTime + '-' + _item.source.metEndTime},
                        {text: _item.source.locale},
                        {text: _item.source.customer}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.meetingInfo.id ? '保存' : '添加', null, $scope.meetingInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            $scope.uploadDataModel.value = '单个添加电话会议信息';
            $scope.uploadDataModel.template = 'editMeetingTemplate';
            $scope.uploadDataModel.pageTitle = '批量添加电话会议信息';
            $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_dh.xlsx';
            $scope.uploadDataModel.uploadUrl = 'ty/tyActivity/batchImport';
            $scope.meetingInfo.dirty = false;
        };
        $scope.custOpenedCallback = function () {
            //$scope.uploadDataModel.value = '已参与客户';
            //$scope.uploadDataModel.template = 'meetingCustomerTemplate';
            $scope.uploadDataModel.pageTitle = '批量添加参与客户';
            $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_dh_cust.xlsx';
            $scope.uploadDataModel.uploadUrl = 'ty/tyActivity/batchImport';
            $scope.uploadDataModel.actId = _item.source.id;
            $scope.custList.length = 0;
            ReqServ.request('POST', 'ty/tyActivitysign/read/list', {actId: _item.source.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (result.data)
                    result.data.forEach(function (value) {
                        value.arriveTime = ReqServ.getTimeString(value.arriveTime + '');
                        value.leaveTime = ReqServ.getTimeString(value.leaveTime + '');
                        value.totalDuration = ReqServ.getTotalTime(value.totalDuration);
                        value.custMobile = value.custMobile ? value.custMobile : '-';
                        $scope.custList.push(value);
                    })
            }).error(function (result) {
                $scope.showAlert(result.msg ? result.msg : appInterface.alert);
            })
        };
        ReqServ.removeStore('data');
        $scope.clearData();
        loadData();
    }])
});