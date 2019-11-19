define(['app'], function (app) {
    app.controller('jourCtrl', ['$scope', '$timeout', '$state', '$stateParams', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $timeout, $state, $stateParams, ReqServ, appInterface, ngDialog) {
        if (!$scope.tabMenus[1].active)
            $scope.tabMenus[1].active = true;
        $scope.welcome.isBack = true;
        var currentName = $state.current.name;
        var dataModel = {
            heads: [{title: '项目类型', dataKey: 'projectTypeText', width: 90},
                {title: '项目角色', dataKey: 'projectRoleText', width: 90},
                {title: '项目主体', dataKey: 'projectName', width: 120},
                {title: '需求及进展', dataKey: 'projectDemand', width: 200},
                {title: '所属团队', dataKey: 'deptName', width: 150},
                {title: '关联专家', dataKey: 'expertStaffName', width: 150},
                {title: '关联客户', dataKey: 'orgCustName', width: 150},
                {title: '关联公司', dataKey: 'companyName', width: 200},
                {title: '附件', dataKey: 'attachmentFlag', width: 70},
                {title: '创建日期', dataKey: 'createTime', width: 90},
                {title: '截止日期', dataKey: 'endTime', width: 90},
                {title: '更新日期', dataKey: 'updateTime', width: 90},
                {title: '提供者', dataKey: 'brokerName', width: 90}],
            isScrollLoaded: true
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            ReqServ.request('POST', 'ty/tyProject/read/jourlist', {projectId: $stateParams.id}).success(function (result) {
                $scope.loading.isShow = false;
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                result.data.list.forEach(function (value) {
                    value.endTime = value.endTime ? ReqServ.getDateString(value.endTime + '') : '-';
                    value.brokerName = value.deleteFlag == '1' ? '<span class="text-gray">' + value.brokerName + '</span>' : value.brokerName;
                    value.attachmentFlag = value.attachmentFlag == '1' ? '有' : '无';
                });
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert(result.msg ? result.msg : appInterface.alert);
            })
        };
        $scope.$on('loadPageData', function (evt) {
            loadData();
        });
        $scope.clearData();
        loadData();
    }])
});