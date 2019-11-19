define(['app'], function (app) {
    app.controller('projectExpertCtrl', ['$scope', '$state', '$stateParams', 'ReqServ', 'appInterface', function ($scope, $state, $stateParams, ReqServ, appInterface) {
        if (!$stateParams.id) {
            $state.go('master.resource.project');
            return;
        }
        $scope.tabBar.isShow = false;
        $scope.expert = {};
        $scope.expertInfo = {selectedExperts: []};
        $scope.welcome.isBack = !!$stateParams.index;
        $scope.expertNames = [];
        var currentName = $state.current.name;
        var _isLoaded = false;
        var _item;
        var dataModel = {
            toolsBar: [{
                btnName: '保存', bgStyle: 'bg-red-light text-orange', event: function () {
                    var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                    var staffIds = [];
                    var staffNames = [];
                    for (var i = 0; i < selectedList.length; i++) {
                        staffIds.push(selectedList[i].id);
                        staffNames.push(selectedList[i].expertName);
                    }
                    _item.expertStaffId = staffIds.join(',');
                    _item.expertStaffName = staffNames.join(',');
                    $scope.disabled(false);
                    ReqServ.request('POST', 'ty/tyProject/update', _item).success(function (result) {
                        $scope.disabled(true);
                        if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            return;
                        }
                        $scope.showSuccess('保存参与专家信息完成。');
                    }).error(function (result) {
                        $scope.disabled(true);
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    });
                }, isDisabled: true
            }],
            heads: [{title: '类别', dataKey: 'expertTypeText', width: 90},
                {title: '联系人', dataKey: 'expertName', width: 90},
                {title: '所属公司', dataKey: 'orgName', width: 200},
                {title: '职位', dataKey: 'title', width: 120},
                {title: '手机', dataKey: 'expertTel', width: 90},
                {title: '项目角色', dataKey: 'expertProjectRoleText', width: 90}],
            checkFunc: function (expert) {
                var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                if (expert.hasCheckBox.isChecked) {
                    selectedList.push({id: expert.source.id, expertName: expert.source.expertName});
                } else {
                    for (var i = 0; i < selectedList.length; i++) {
                        if (selectedList[i].id == expert.source.id) {
                            selectedList.splice(i, 1);
                            break;
                        }
                    }
                    $scope.setSelectAll(false);
                }
                if (selectedList.length <= 0)
                    ReqServ.removeStore('selectedItem');
                else
                    ReqServ.setStore('selectedItem', JSON.stringify(selectedList));
                $scope.disabled();
                getSelectedName();
            },
            isScrollLoaded: true
        };
        var getSelectedName = function () {
            var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
            var nameList = [];
            for (var i = 0; i < selectedList.length; i++) {
                nameList.push(selectedList[i].expertName);
            }
            $scope.expertInfo.selectedExperts = '已参与专家：' + nameList.join(' ');
        };
        var pushSelectItem = function (list, item) {
            for (var i = 0; i < list.length; i++) {
                if (item.id == list[i].id)
                    return;
            }
            list.push(item);
        };
        var setSelectedItem = function () {
            var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
            if (dataModel.data) {
                for (var j = 0; j < selectedList.length; j++) {
                    for (var i = 0; i < dataModel.data.length; i++) {
                        if (selectedList[j].id == dataModel.data[i].id) {
                            dataModel.data[i].isChecked = true;
                            break;
                        }
                    }
                }
            }
        };
        var loadedData = function (result) {
            if (!$scope.expertTypes) {
                $scope.expertType = result.dicts['EXPERTTYPE'];
                $scope.closeLevel = result.dicts['CLOSELEVEL'];
                $scope.projectRole = result.dicts['EXPERT_PROJECTROLE'];
                ReqServ.removeStore('EXPERTTYPE');
                ReqServ.setStore('EXPERTTYPE', JSON.stringify($scope.expertType));
                ReqServ.removeStore('CLOSELEVEL');
                ReqServ.setStore('CLOSELEVEL', JSON.stringify($scope.closeLevel));
                ReqServ.removeStore('EXPERT_PROJECTROLE');
                ReqServ.setStore('EXPERT_PROJECTROLE', JSON.stringify($scope.projectRole));
            }
            dataModel.data = result.data.list;
            dataModel.pageInfo = {
                size: result.data.size,
                total: result.data.total,
                pages: result.data.pages
            };
            ReqServ.removeStore('searchModels');
            ReqServ.setStore('searchModels', JSON.stringify($scope.searchModel));
            ReqServ.removeStore('pageInfos');
            ReqServ.setStore('pageInfos', JSON.stringify(dataModel.pageInfo));
            if (ReqServ.getStore('datas')) {
                var _data = JSON.parse(ReqServ.getStore('datas'));
                ReqServ.removeStore('datas');
                ReqServ.setStore('datas', JSON.stringify(_data.concat(dataModel.data)));
            } else
                ReqServ.setStore('datas', JSON.stringify(dataModel.data));
            getSelectedName();
            setSelectedItem();
            $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            $scope.disabled();
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
                var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                if (!_isLoaded) {
                    ReqServ.request('POST', 'ty/tyProject/read/detail', {id: $stateParams.id}).success(function (res) {
                        if (res.httpCode != appInterface.successCode) {
                            $scope.loading.isShow = false;
                            $scope.showAlert(result.msg ? res.msg : appInterface.alert);
                            return;
                        }
                        _isLoaded = true;
                        _item = res.data;
                        if (res.data.orgCustId) {
                            var staffIds = res.data.expertStaffId ? res.data.expertStaffId.split(',') : [];
                            var staffNames = res.data.expertStaffName ? res.data.expertStaffName.split(',') : [];
                            for (var i = 0; i < staffIds.length; i++) {
                                pushSelectItem(selectedList, {id: staffIds[i], expertName: staffNames[i]});
                            }
                        }
                        if (selectedList.length > 0)
                            ReqServ.setStore('selectedItem', JSON.stringify(selectedList));
                        loadedData(result);
                    }).error(function (res) {
                        $scope.loading.isShow = false;
                        $scope.showAlert((res && res.msg) ? result.msg : appInterface.alert);
                    });
                    return;
                }
                loadedData(result);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('goBack', function (evt) {
            $scope.loading.isShow = true;
            $state.go('master.resource.project', $stateParams);
        });
        $scope.$on('loadPageData', function (evt, data) {
            ReqServ.clearObj($scope.expert);
            ReqServ.copyObj($scope.searchModel, $scope.expert);
            if (data)
                ReqServ.removeStore('datas');
            loadData();
        });

        $scope.searchExpert = function () {
            ReqServ.removeStore('datas');
            $scope.expert.pageNum = 1;
            $scope.clearData();
            $scope.setSelectAll(false);
            ReqServ.copyObj($scope.expert, $scope.searchModel);
            loadData();
        };
        ReqServ.removeStore('datas');
        ReqServ.removeStore('selectedItem');
        $scope.clearData();
        loadData();
    }])
});