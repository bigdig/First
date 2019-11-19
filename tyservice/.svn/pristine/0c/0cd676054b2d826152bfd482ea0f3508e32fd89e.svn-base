define(['app'], function (app) {
    app.controller('logCtrl', ['$scope', '$state', 'ReqServ', 'appInterface', function ($scope, $state, ReqServ, appInterface) {
        $scope.log = {};
        $scope.welcome.isHide = true;
        $scope.clearData();
        var currentName = $state.current.name;
        var dataModel = {
            heads: [{title: '请求地址', dataKey: 'requestUri', width: 120},
                {title: '姓名', dataKey: 'createBy', width: 90},
                {title: 'IP地址', dataKey: 'clientHost', width: 90},
                {title: '操作时间', dataKey: 'createTime', width: 120}],
            isScrollLoaded: true
        };
        var loadSearch = function () {
            ReqServ.request('POST', 'sys/event/read/dics', null).success(function (data) {
                if (data.httpCode != appInterface.successCode)
                    return;
                $scope.requestUriDic = data.dicts['REQUESTURI'];
            }).error(function () {

            })
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.log.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'sys/event/read/list', $scope.log).success(function (result) {
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
            ReqServ.clearObj($scope.log);
            ReqServ.copyObj($scope.searchModel, $scope.log);
            loadData();
        });
        $scope.searchLog = function () {
            $scope.log.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.log, $scope.searchModel);
            loadData();
        };
        loadSearch();
        $scope.clearData();
        loadData();
    }])
});