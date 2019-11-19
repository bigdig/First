define(['app'], function (app) {
    app.controller('homeCustCtrl', ['$scope', '$state', 'ReqServ', 'appInterface', function ($scope, $state, ReqServ, appInterface) {
        $scope.welcome.isHide = true;
        if (!$scope.tabMenus[1].active)
            $scope.tabMenus[1].active = true;
        var currentName = $state.current.name;
        var dataModel = {
            heads: [{title: '客户名称', dataKey: 'custName', width: 90},
                {title: '所属机构', dataKey: 'orgName', width: 200},
                {title: '职位', dataKey: 'title', width: 120},
                {title: '手机号', dataKey: 'custMobile', width: 90},
                {title: '邮箱', dataKey: 'custEmail', width: 120},
                {title: '最近活跃日期', dataKey: 'activeDatetime', width: 120}],
            isScrollLoaded: true
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            ReqServ.request('POST', 'ty/tyOrgcustomer/read/list', null).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                result.data.list.forEach(function (value) {
                    value.activeDatetime = ReqServ.getDateString(value.activeDatetime + '');
                });
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
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