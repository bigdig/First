define(['app'], function (app) {
    app.controller('trackCtrl', ['$scope', '$timeout', '$state', '$stateParams', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $timeout, $state, $stateParams, ReqServ, appInterface, ngDialog) {
        if (!$scope.tabMenus[0].active)
            $scope.tabMenus[0].active = true;
        $scope.welcome.isBack = true;
        $scope.detailInfo = {};
        var currentName = $state.current.name;
        var _item;
        var dataModel = {
            toolsBar: [{
                btnName: '添加追踪信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.detailInfo.loading))
                        return;
                    ReqServ.clearObj($scope.detailInfo);
                    $scope.detailInfo.pageTitle = '添加追踪信息';
                    $scope.detailInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editDetailTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.detailInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.detailInfo);
                    $scope.detailInfo.pageTitle = '编辑跟踪信息';
                    $scope.detailInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editDetailTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }, isCheck: {key: 'createBy', text: $scope.userInfo.id}
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.detailInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.detailInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyProject/deleteTrack', {id: item.source.id}).success(function (result) {
                        $scope.detailInfo.loading = false;
                        if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            return;
                        }
                        $scope.clearData();
                        loadData();
                    }).error(function (result) {
                        $scope.detailInfo.loading = false;
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    })

                }, isCheck: {key: 'createBy', text: $scope.userInfo.id}
            }],
            heads: [{title: '追踪信息', dataKey: 'projectTrack', width: 500},
                {title: '创建日期', dataKey: 'createTime', width: 70},
                {title: '更新日期', dataKey: 'updateTime', width: 70},
                {title: '创建者', dataKey: 'createByName', width: 90},
                {dataKey: 'createBy', hide: true}],
            isScrollLoaded: true,
            isCopyControls: true
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            ReqServ.request('POST', 'ty/tyProject/read/tracklist', {projectId: $stateParams.id}).success(function (result) {
                $scope.loading.isShow = false;
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                result.data.list.forEach(function (value) {
                    value.updateTime = ReqServ.getMonthDay(new Date(value.updateTime));
                });
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert(result.msg ? result.msg : appInterface.alert);
            })
        };
        $scope.editProjectTrack = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.detailInfo);
            $scope.detailInfo.projectId = $stateParams.id;
            ReqServ.request('POST', $scope.detailInfo.id ? 'ty/tyProject/updateTrack' : 'ty/tyProject/addTrack', $scope.detailInfo).success(function (data) {
                ReqServ.setBtnStatus(false, $scope.detailInfo.id ? '保存' : '添加', null, $scope.detailInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                if ($scope.detailInfo.id) {
                    ReqServ.copyObj($scope.detailInfo, _item.source);
                    _item.item = [{text: _item.source.projectTrack, width: 500},
                        {text: _item.source.createTime, width: 70},
                        {text: _item.source.updateTime, width: 70},
                        {text: _item.source.createByName, width: 90},
                        {text: _item.source.createBy, hide: true}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.detailInfo.id ? '保存' : '添加', null, $scope.detailInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('loadPageData', function (evt) {
            loadData();
        });
        $scope.clearData();
        loadData();
    }])
});