define(['app'], function (app) {
    app.controller('customerActivitySignCtrl', ['$scope', '$state', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, ReqServ, ngDialog, appInterface) {
        $scope.activitySign = {};
        $scope.activitySignInfo = {};
        $scope.uploadDataModel.value = '单个添加服务参与记录';
        $scope.uploadDataModel.template = 'editactivitySignTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加服务参与记录';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_wt.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyActivitySign/batchImport';
        $scope.uploadDataModel.scope = $scope;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            // toolsBar: [{
            //     btnName: '添加服务记录', bgStyle: 'bg-purple-light text-purple', event: function () {
            //         if ($scope.hasTaskRun($scope.activitySignInfo.loading))
            //             return;
            //         ReqServ.clearObj($scope.activitySignInfo);
            //         $scope.activitySignInfo.pageTitle = '添加服务记录';
            //         $scope.activitySignInfo.btnName = '添加';
            //         ngDialog.open({
            //             template: 'editactivitySignTemplate',
            //             className: 'ngdialog-theme-default',
            //             onOpenCallback: $scope.editOpenedCallback,
            //             scope: $scope
            //         });
            //     }
            // }],
            controls: [
                // {
                // name: '编辑', event: function (item) {
                //     if ($scope.hasTaskRun($scope.activitySignInfo.loading))
                //         return;
                //     _item = item;
                //     ReqServ.copyObj(_item.source, $scope.activitySignInfo);
                //     $scope.activitySignInfo.pageTitle = '编辑服务记录了';
                //     $scope.activitySignInfo.btnName = '保存';
                //     ngDialog.open({
                //         template: 'editactivitySignTemplate',
                //         className: 'ngdialog-theme-default',
                //         onOpenCallback: $scope.editOpenedCallback,
                //         scope: $scope
                //     });
                // },
                // isShow: true},
                 {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.activitySignInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.activitySignInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyActivitysign/delete', {id: item.source.id}).success(function (data) {
                        $scope.activitySignInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchactivitySign();
                    }).error(function (result) {
                        $scope.activitySignInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                },
                isShow: true
            }],
            heads: [{title: '日期', dataKey: 'createTime', width: 90},
                //{title: '服务类型', dataKey: 'topicTypeText', width: 120},
                {title: '参与客户', dataKey: 'custName', width: 90},
                {title: '服务内容', dataKey: 'title', width: 200},
                {title: '研究员', dataKey: 'researchers', width: 120},
                {title: '研究团队', dataKey: 'teams', width: 120},
                {title: '机构名称', dataKey: 'orgName', width: 200},
                //{title: '对口销售', dataKey: 'serviceSaler', width: 90},
                {title: '报名状态', dataKey: 'signStatusText', width: 200},
                {title: '地点', dataKey: 'locale', width: 90}],
            isScrollLoaded: true,
            controlWidth: 50
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.activitySign.pageSize = $scope.searchModel.pageSize;
            // $scope.activitySign.beginDate = ReqServ.getDateNum($scope.activitySign.startDateTime);
            // $scope.activitySign.endDate = ReqServ.getDateNum($scope.activitySign.endDateTime);
            ReqServ.request('POST', 'ty/tyActivitysign/read/pagelist', $scope.activitySign).success(function (result) {
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
                    //创建时间
                    result.data.list[i].createTime = result.data.list[i].createTime.substring(0,10);
                    var researchers = [], researcherIds = [], teams = [], customers = [], custIds = [];
                    //服务内容
                    if(!result.data.list[i].title){
                        result.data.list[i].title = result.data.list[i].tyActivity.title;
                    }
                    //所属区域
                    if(!result.data.list[i].locale){
                        result.data.list[i].locale  = result.data.list[i].tyActivity.locale;
                    }

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
                    // if (result.data.list[i].custList) {
                    //     for (var c = 0; c < result.data.list[i].custList.length; c++) {
                    //         customers.push(result.data.list[i].custList[c].custName);
                    //         custIds.push(result.data.list[i].custList[c].custId);
                    //     }
                    //     result.data.list[i].custId = custIds.join(',');
                    //     result.data.list[i].customers = customers.join(',');
                    // }
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
            ReqServ.clearObj($scope.activitySign);
            ReqServ.copyObj($scope.searchModel, $scope.activitySign);
            loadData();
        });
        $scope.setSaler = function (data) {
            if ($scope.activitySignInfo.orgId) {
                $scope.delCustomers.length = 0;
                ReqServ.request('POST', 'ty/tyOrgcustomer/read/custdict', {orgId: $scope.activitySignInfo.orgId}).success(function (result) {
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
                $scope.activitySignInfo.customers = null;
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
                $scope.activitySignInfo.serviceSaler = salers.join(',');
                $scope.activitySignInfo.teamCat = areas.join(',');
            }
        };
        $scope.searchactivitySign = function () {
            $scope.activitySign.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.activitySign, $scope.searchModel);
            loadData();
        };
        $scope.editactivitySign = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.activitySignInfo);
            ReqServ.request('POST', $scope.activitySignInfo.id ? 'ty/tyActivitysign/update' : 'ty/tyActivitysign/add', {
                id: $scope.activitySignInfo.id,
                orgId: $scope.activitySignInfo.orgId,
                beginDate: ReqServ.getDateNum($scope.activitySignInfo.createTime),
                // topicType: $scope.activitySignInfo.topicType,
                title: $scope.activitySignInfo.title,
                custIds: $scope.activitySignInfo.custId.split(','),
                userIds: $scope.activitySignInfo.researcherId.split(',')
            }).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.activitySignInfo.id ? '保存' : '添加', null, $scope.activitySignInfo);
                if (result.httpCode != appInterface.successCode) {
                    ReqServ.setBtnStatus(false, $scope.activitySignInfo.id ? '保存' : '添加', null, $scope.activitySignInfo);
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.activitySignInfo.id) {
                    var teams = [], list = $scope.activitySignInfo.researcherId.split(',');
                    for (var i = 0; i < $scope.delResearchers.length; i++) {
                        for (var j = 0; j < list.length; j++) {
                            if ($scope.delResearchers[i].id == list[j])
                                teams.push($scope.delResearchers[i].text.substring($scope.delResearchers[i].text.indexOf('(') + 1, $scope.delResearchers[i].text.length - 1));
                        }
                    }
                    $scope.activitySignInfo.teams = teams.join(',');
                    ReqServ.copyObj($scope.activitySignInfo, _item.source);
                    ReqServ.getTextValue($scope.serviceTypes, _item.source, 'topicType', 'topicTypeText');
                    _item.item = [{text: _item.source.createTime},
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
                ReqServ.setBtnStatus(false, $scope.activitySignInfo.id ? '保存' : '添加', null, $scope.activitySignInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            $scope.delCustomers.length = 0;
            $scope.activitySignInfo.dirty = false;
            $scope.setSaler();
        };
        $scope.clearData();
        loadData();
    }])
});