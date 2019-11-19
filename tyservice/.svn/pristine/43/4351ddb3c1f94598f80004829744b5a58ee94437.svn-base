define(['app'], function (app) {
    app.controller('dataManagerMyServiceCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, $stateParams, $timeout, ReqServ, ngDialog, appInterface) {
        $scope.myservice = {activityType: ['9', '10', '11', '12']};
        $scope.myserviceInfo = {};
        // $scope.uploadDataModel.value = '单个添加个人服务信息';
        // $scope.uploadDataModel.template = 'editMyServiceTemplate';
        // $scope.uploadDataModel.pageTitle = '批量添加个人服务信息';
        // $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'activity_qt.xlsx';
        // $scope.uploadDataModel.uploadUrl = 'ty/tyActivity/batchImport';
        $scope.uploadDataModel.scope = $scope;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            // toolsBar: [{
            //     btnName: '添加个人服务信息', bgStyle: 'bg-purple-light text-purple', event: function () {
            //         if ($scope.hasTaskRun($scope.myserviceInfo.loading))
            //             return;
            //         ReqServ.clearObj($scope.myserviceInfo);
            //         $scope.myserviceInfo.activityType = '8';
            //         $scope.myserviceInfo.pageTitle = '添加个人服务信息';
            //         $scope.myserviceInfo.btnName = '添加';
            //         ngDialog.open({
            //             template: 'editMyServiceTemplate',
            //             className: 'ngdialog-theme-default',
            //             onOpenCallback: $scope.editOpenedCallback,
            //             scope: $scope
            //         });
            //     }
            // }],
            controls: [{
                name: '客户', event: function (item) {
                    if ($scope.hasTaskRun($scope.myserviceInfo.loading))
                        return;
                    _item = item;
                    ngDialog.open({
                        template: 'meetingCustomerTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.custOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }
                // , {
                //     name: '编辑', event: function (item) {
                //         if ($scope.hasTaskRun($scope.myserviceInfo.loading))
                //             return;
                //         _item = item;
                //         ReqServ.copyObj(_item.source, $scope.myserviceInfo);
                //         $scope.myserviceInfo.pageTitle = '编辑个人服务信息';
                //         $scope.myserviceInfo.btnName = '保存';
                //         ngDialog.open({
                //             template: 'editMyServiceTemplate',
                //             className: 'ngdialog-theme-default',
                //             onOpenCallback: $scope.editOpenedCallback,
                //             scope: $scope
                //         });
                //     },
                //     isShow: true
                // }, {
                //     name: '删除', event: function (item) {
                //         if ($scope.hasTaskRun($scope.myserviceInfo.loading) || !window.confirm('确定要删除？'))
                //             return;
                //         $scope.myserviceInfo.loading = true;
                //         ReqServ.request('POST', 'ty/tyActivity/delete', {id: item.source.id}).success(function (data) {
                //             $scope.myserviceInfo.loading = false;
                //             if (data.httpCode != appInterface.successCode) {
                //                 $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                //                 return;
                //             }
                //             $scope.searchMyService();
                //         }).error(function (result) {
                //             $scope.myserviceInfo.loading = false;
                //             $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                //         })
                //     },
                //     isShow: true
                // }
            ],
            heads: [{title: '日期', dataKey: 'myserviceDate', width: 90},
                {title: '服务类型', dataKey: 'activityTypeText', width: 90},
                {title: '服务内容', dataKey: 'title', width: 200},
                {title: '研究员', dataKey: 'researchers', width: 150},
                {title: '研究团队', dataKey: 'teams', width: 150},
                {title: '地点', dataKey: 'locale', width: 90},
                {title: '参与客户', dataKey: 'customer', width: 90}],
            isScrollLoaded: true,
            controlWidth: 40
        };
        var loadLocalData = function () {
            $scope.loading.isShow = true;
            $scope.clearData();
            ReqServ.copyObj(JSON.parse(ReqServ.getStore('searchModel')), $scope.searchModel);
            ReqServ.copyObj($scope.searchModel, $scope.myservice);
            $scope.myserviceTeams = JSON.parse(ReqServ.getStore('Researchdept'));
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
            $scope.myservice.pageSize = $scope.searchModel.pageSize;
            $scope.myservice.beginDate = ReqServ.getDateNum($scope.myservice.startDateTime);
            $scope.myservice.endDate = ReqServ.getDateNum($scope.myservice.endDateTime);
            ReqServ.request('POST', 'ty/tyActivity/read/list', $scope.myservice).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.myserviceTeams) {
                    $scope.myserviceTeams = result.dicts['Researchdept'];
                    $scope.researchers = result.dicts['Researchers'];
                    $scope.salResearchers = result.dicts['ResearchersEx'];
                    ReqServ.removeStore('Researchdept');
                    ReqServ.setStore('Researchdept', JSON.stringify($scope.myserviceTeams));
                    ReqServ.removeStore('Researchers');
                    ReqServ.setStore('Researchers', JSON.stringify($scope.researchers));
                    ReqServ.removeStore('ResearchersEx');
                    ReqServ.setStore('ResearchersEx', JSON.stringify($scope.salResearchers));
                }
                for (var i = 0; i < result.data.list.length; i++) {
                    result.data.list[i].myserviceDate = ReqServ.getDateString(result.data.list[i].beginDate + '');
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
            ReqServ.copyObj($scope.searchModel, $scope.myservice);
            if (data)
                ReqServ.removeStore('data');
            loadData();
        });
        $scope.searchMyService = function () {
            ReqServ.removeStore('data');
            $scope.myservice.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.myservice, $scope.searchModel);
            loadData();
        };
        // $scope.editMyService = function () {
        //     ReqServ.setBtnStatus(true, '处理中...', null, $scope.myserviceInfo);
        //     $scope.myserviceInfo.beginDate = ReqServ.getDateNum($scope.myserviceInfo.myserviceDate);
        //     $scope.myserviceInfo.userIds = $scope.myserviceInfo.researcherId.split(',');
        //     ReqServ.request('POST', $scope.myserviceInfo.id ? 'ty/tyActivity/update' : 'ty/tyActivity/add', $scope.myserviceInfo).success(function (result) {
        //         ReqServ.setBtnStatus(false, $scope.myserviceInfo.id ? '保存' : '添加', null, $scope.myserviceInfo);
        //         if (result.httpCode != appInterface.successCode) {
        //             ReqServ.setBtnStatus(false, $scope.myserviceInfo.id ? '保存' : '添加', null, $scope.myserviceInfo);
        //             $scope.showAlert(result.msg ? result.msg : appInterface.alert);
        //             return;
        //         }
        //         if ($scope.myserviceInfo.id) {
        //             var teams = [], list = $scope.myserviceInfo.researcherId.split(',');
        //             for (var i = 0; i < $scope.salResearchers.length; i++) {
        //                 for (var j = 0; j < list.length; j++) {
        //                     if ($scope.salResearchers[i].id == list[j])
        //                         teams.push($scope.salResearchers[i].text.substring($scope.salResearchers[i].text.indexOf('(') + 1, $scope.salResearchers[i].text.length - 1));
        //                 }
        //             }
        //             $scope.myserviceInfo.teams = teams.join(',');
        //             ReqServ.copyObj($scope.myserviceInfo, _item.source);
        //             _item.item = [{text: _item.source.myserviceDate},
        //                 {text: _item.source.activityTypeText},
        //                 {text: _item.source.title},
        //                 {text: _item.source.researchers},
        //                 {text: _item.source.teams},
        //                 {text: _item.source.locale},
        //                 {text: _item.source.customer}];
        //             ngDialog.close();
        //             return;
        //         }
        //         ngDialog.close();
        //         $scope.clearData();
        //         loadData();
        //     }).error(function (result) {
        //         ReqServ.setBtnStatus(false, $scope.myserviceInfo.id ? '保存' : '添加', null, $scope.myserviceInfo);
        //         $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
        //     });
        // };
        // $scope.editOpenedCallback = function () {
        //     $scope.myserviceInfo.dirty = false;
        // };
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
        // $scope.addCustomers = function () {
        //     $state.go('master.datamanager.customer', {id: _item.source.id, index: _item.index, model: 'myservice'});
        // };
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