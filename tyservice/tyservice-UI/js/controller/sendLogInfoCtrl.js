define(['app'], function (app) {
    app.controller('sendLogInfoCtrl', ['$scope', '$sce', '$state', '$stateParams', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $sce, $state, $stateParams, ReqServ, appInterface, ngDialog) {
        if (!$stateParams.id) {
            $state.go('master.send');
            return;
        }
        $scope.welcome.isBack = true;
        $scope.send = {msgAuditid: $stateParams.id};
        $scope.sendInfo = {iconTitle: '展开'};
        $scope.msgType = appInterface.msgType;
        $scope.msgFlag = appInterface.sendFlag;
        var currentName = $state.current.name;
        var dataModel = {
            controls: [{
                name: '流程', event: function (item) {
                    ReqServ.copyObj(item.source, $scope.sendInfo);
                    ngDialog.open({
                        template: 'editSendTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }, isShow: true
            }],
            heads: [{title: '发送标志', dataKey: 'sendFlag', width: 70},
                {title: '接收人', dataKey: 'receiverName', width: 90},
                {title: '接收人手机', dataKey: 'receiverTel', width: 90},
                {title: '接收人邮箱', dataKey: 'receiverEmail', width: 120}],
            isScrollLoaded: true,
            controlWidth: 40
        };
        var transfer = function (obj) {
            obj.htmlContent = $sce.trustAsHtml(obj.msgContent);
            obj.msgContent = ReqServ.delHtmlTag(obj.msgContent);
            obj.sendFlag = obj.sendFlag == '0' ? '未发送' : '已发送';
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.send.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'ty/tyMessagelog/read/list', $scope.send).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                for (var i = 0; i < result.data.list.length; i++) {
                    transfer(result.data.list[i]);
                }
                if (result.data.list.length > 0)
                    ReqServ.copyObj(result.data.list[0], $scope.sendInfo);
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('goBack', function (evt) {
            $scope.loading.isShow = true;
            $state.go('master.send', {index: $stateParams.index});
        });
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.send);
            ReqServ.copyObj($scope.searchModel, $scope.send);
            loadData();
        });
        $scope.searchSend = function () {
            $scope.send.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.send, $scope.searchModel);
            loadData();
        };
        $scope.openEditor = function () {
            if ($scope.sendInfo.isEditor)
                $scope.sendInfo.iconTitle = '展开';
            else
                $scope.sendInfo.iconTitle = '收起';
            $scope.sendInfo.isEditor = !$scope.sendInfo.isEditor;
        };
        $scope.clearData();
        loadData();
    }])
});