define(['app'], function (app) {
    app.controller('sessionCtrl', ['$scope', '$state', 'ReqServ', 'appInterface', function ($scope, $state, ReqServ, appInterface) {
        $scope.session = {};
        $scope.clearData();
        var currentName = $state.current.name;
        var dataModel = {
            controls: [{
                name: '下线', event: function (item) {
                    if (!confirm('此操作将影响到该账户的登录状态。确定要这么做？'))
                        return;
                    ReqServ.request('POST', 'sys/session/delete', {id: item.source.id}).success(function (data) {
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.clearData();
                        loadData();
                    }).error(function (result) {
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                }, isShow: true
            }],
            heads: [{title: '账户', dataKey: 'account', width: 90},
                {title: '会话', dataKey: 'sessionId', width: 100},
                {title: 'IP地址', dataKey: 'ip', width: 100}],
            isScrollLoaded: true,
            controlWidth: 40
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.session.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'sys/session/read/list', $scope.session).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
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
            ReqServ.clearObj($scope.session);
            ReqServ.copyObj($scope.searchModel, $scope.session);
            loadData();
        });
        $scope.searchSession = function () {
            $scope.session.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.session, $scope.searchModel);
            loadData();
        };
        $scope.clearData();
        loadData();
    }])
});