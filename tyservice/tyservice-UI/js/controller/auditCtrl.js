define(['app'], function (app) {
    app.controller('auditCtrl', ['$scope', '$sce', '$state', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $sce, $state, ReqServ, appInterface, ngDialog) {
        $scope.audit = {};
        $scope.auditInfo = {loading: false};
        $scope.msgType = appInterface.msgType;
        $scope.msgFlag = appInterface.msgFlag;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            controls: [{
                name: '审核', event: function (item) {
                    if ($scope.hasTaskRun($scope.auditInfo.loading))
                        return;
                    _item = item;
                    ReqServ.clearObj($scope.auditInfo);
                    ReqServ.copyObj(_item.source, $scope.auditInfo);
                    if ($scope.auditInfo.filePath) {
                        var fileUrl = $scope.auditInfo.filePath.split(',');
                        var fileName = $scope.auditInfo.fileName.split(',');
                        $scope.auditInfo.files = [];
                        for (var i = 0; i < fileUrl.length; i++) {
                            var fileDownload = "ty/tyOrgcustomer/maildown?fileName=" + fileName[i] + '&filePath=' + fileUrl[i];
                            $scope.auditInfo.files.push({
                                fileName: fileName[i],
                                fileDownload: fileDownload
                            })
                        }
                    }
                    restore($scope.auditInfo);
                    ngDialog.open({
                        template: 'editAuditTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.auditOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }],
            heads: [{title: '类型', dataKey: 'msgType', width:70},
                {title: '主题', dataKey: 'title', width:150},
                {title: '内容', dataKey: 'content', width: 200},
                {title: '状态', dataKey: 'auditFlag', width: 70},
                {title: '发起人', dataKey: 'createName', width: 90},
                {title: '发起时间', dataKey: 'createTime', width: 90},
                {title: '审核人', dataKey: 'auditBy', width: 70},
                {title: '审核时间', dataKey: 'auditTime', width: 90},
                {title: '备注', dataKey: 'remark', width: 150}],
            isScrollLoaded: true
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
            obj.htmlContent = $sce.trustAsHtml(obj.msgContent);
        };
        var formatDate = function (date) {
            function format(value) {
                return value < 10 ? '0' + value : value;
            }

            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            var day = date.getDate();
            var hour = date.getHours();
            var minute = date.getMinutes();
            var second = date.getSeconds();
            return year + '-' + format(month) + '-' + format(day) + ' ' + format(hour) + ':' + format(minute) + ':' + format(second);
        };
        var restore = function (obj) {
            obj.msgType = obj.msgType == '短信' ? 'sms' : 'email';
            switch (obj.auditFlag) {
                case '未审核' :
                    obj.auditFlag = '0';
                    break;
                case '已通过' :
                    obj.auditFlag = '1';
                    break;
                case '未通过' :
                    obj.auditFlag = '2';
                    break;
            }
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
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.audit);
            ReqServ.copyObj($scope.searchModel, $scope.audit);
            loadData();
        });
        $scope.searchAudit = function () {
            $scope.audit.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.audit, $scope.searchModel);
            loadData();
        };
        $scope.messageAudit = function (submitType) {
            $scope.auditInfo.loading = true;
            ReqServ.request('POST', submitType == 'pass' ? 'ty/tyMessageaudit/pass' : 'ty/tyMessageaudit/reject', {
                id: $scope.auditInfo.id,
                remark: $scope.auditInfo.remark
            }).success(function (data) {
                $scope.auditInfo.loading = false;
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                $scope.auditInfo.auditFlag = submitType == 'pass' ? '1' : '2';
                $scope.auditInfo.auditTime = formatDate(new Date());
                ReqServ.copyObj($scope.auditInfo, _item.source);
                transfer(_item.source);
                _item.item = [{text: _item.source.msgType, width: 70},
                    {text: _item.source.title, width: 150},
                    {text: _item.source.content, width: 200},
                    {text: _item.source.auditFlag, width: 70},
                    {text: _item.source.createName, width: 90},
                    {text: _item.source.createTime, width: 90},
                    {text: ReqServ.getUser().userName, width: 70},
                    {text: _item.source.auditTime, width: 90},
                    {text: _item.source.remark ? _item.source.remark : '-', width: 150}];
                ngDialog.close();
            }).error(function (result) {
                $scope.auditInfo.loading = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.auditOpenedCallback = function () {
            ReqServ.request('POST', 'ty/tyMessageaudit/read/detail', {id: $scope.auditInfo.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                $scope.auditInfo.customers = {
                    num: result.data.receiverNames.length,
                    text: result.data.receiverNames.join(' ')
                };
            }).error(function (result) {
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            })
        };
        $scope.clearData();
        loadData();
    }])
});