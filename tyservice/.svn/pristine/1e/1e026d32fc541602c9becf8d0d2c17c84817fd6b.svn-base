define(['app'], function (app) {
    app.controller('sendCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'appInterface', function ($scope, $state, $stateParams, $timeout, ReqServ, appInterface) {
        $scope.audit = {};
        $scope.auditInfo = {loading: false};
        $scope.msgType = appInterface.msgType;
        $scope.msgFlag = appInterface.msgFlag;
        var currentName = $state.current.name;
        var dataModel = {
            controls: [{
                name: '查看详情', event: function (item) {
                    $scope.loading.isShow = true;
                    $state.go('master.sendloginfo', {id: item.source.id, index: item.index});
                }, isShow: true
            }],
            heads: [{title: '类型', dataKey: 'msgType', width: 70},
                {title: '主题', dataKey: 'title', width: 100},
                {title: '内容', dataKey: 'content', width: 200},
                {title: '状态', dataKey: 'auditFlag', width: 70},
                {title: '发起人', dataKey: 'createName', width: 90},
                {title: '发起时间', dataKey: 'createTime', width: 120}],
            isScrollLoaded: true,
            controlWidth: 70
        };
        var transfer = function (obj) {
            obj.msgType = obj.msgType == 'sms' ? '短信' : '邮件';
            switch (obj.auditFlag) {
                case '0' :
                    obj.auditFlag = '未审核';
                    break;
                case '1' :
                    obj.auditFlag = '已通过';
                    break;
                case '2' :
                    obj.auditFlag = '未通过';
                    break;
            }
            obj.content = ReqServ.delHtmlTag(obj.msgContent);
        };
        var loadLocalData = function () {
            $scope.loading.isShow = true;
            $scope.clearData();
            ReqServ.copyObj(JSON.parse(ReqServ.getStore('searchModel')), $scope.searchModel);
            ReqServ.copyObj($scope.searchModel, $scope.audit);
            dataModel.pageInfo = JSON.parse(ReqServ.getStore('pageInfo'));
            dataModel.data = JSON.parse(ReqServ.getStore('data'));
            $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.audit.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'ty/tyMessageaudit/read/list', $scope.audit).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                for (var i = 0; i < result.data.list.length; i++) {
                    transfer(result.data.list[i]);
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
            ReqServ.clearObj($scope.audit);
            ReqServ.copyObj($scope.searchModel, $scope.audit);
            if (data)
                ReqServ.removeStore('data');
            loadData();
        });
        $scope.searchAudit = function () {
            ReqServ.removeStore('data');
            $scope.audit.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.audit, $scope.searchModel);
            loadData();
        };
        if ($stateParams.index) {
            loadLocalData();
            $timeout(function () {
                var tr = $('table.data').find('tr').eq(parseInt($stateParams.index) + appInterface.add);
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