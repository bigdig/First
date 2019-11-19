define(['app'], function (app) {
    app.controller('companyCtrl', ['$scope', '$state', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $state, ReqServ, appInterface, ngDialog) {
        $scope.company = {};
        $scope.companyInfo = {};
        $scope.uploadDataModel.value = '单个添加上市公司信息';
        $scope.uploadDataModel.template = 'editCompanyTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加上市公司信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'ty_listedcomp_temp.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyListedcompany/batchImport';
        $scope.uploadDataModel.scope = $scope;
        if (!$scope.tabMenus[0].active)
            $scope.tabMenus[0].active = true;
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加上市公司信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.companyInfo.loading))
                        return;
                    ReqServ.clearObj($scope.companyInfo);
                    $scope.companyInfo.pageTitle = '添加上市公司信息';
                    $scope.companyInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editCompanyTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.companyInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.companyInfo);
                    $scope.companyInfo.pageTitle = '编辑上市公司信息';
                    $scope.companyInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editCompanyTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '查看', event: function (item) {
                    ReqServ.copyObj(item.source, $scope.companyInfo);
                    ngDialog.open({
                        template: 'companyTemplate',
                        className: 'ngdialog-theme-plain',
                        scope: $scope
                    });
                }, isShow: true
            }],
            heads: [{title: '证券类别', dataKey: 'marketKind', width: 90},
                {title: '交易代码', dataKey: 'stockCode', width: 90},
                {title: '证券简称', dataKey: 'stockShortname', width: 90},
                {title: '中文名称', dataKey: 'stockName', width: 150},
                {title: '中文缩写', dataKey: 'companyShortname', width: 90},
                {title: '交易代码', dataKey: 'marketNo', width: 90},
                {title: '交易市场', dataKey: 'marketName', width: 120}],
            isScrollLoaded: true,
            controlWidth: 80
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.company.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'ty/tyListedcompany/read/list', $scope.company).success(function (result) {
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
            ReqServ.clearObj($scope.company);
            ReqServ.copyObj($scope.searchModel, $scope.company);
            loadData();
        });
        $scope.searchCompany = function () {
            $scope.company.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.company, $scope.searchModel);
            loadData();
        };
        $scope.editCompany = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.companyInfo);
            ReqServ.request('POST', $scope.companyInfo.id ? 'ty/tyListedcompany/update' : 'ty/tyListedcompany/add', $scope.companyInfo).success(function (data) {
                ReqServ.setBtnStatus(false, $scope.companyInfo.id ? '保存' : '添加', null, $scope.companyInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                if ($scope.companyInfo.id) {
                    ReqServ.copyObj($scope.companyInfo, _item.source);
                    _item.item = [{text: _item.source.marketKind},
                        {text: _item.source.stockCode},
                        {text: _item.source.stockShortname},
                        {text: _item.source.stockName},
                        {text: _item.source.companyShortname ? _item.source.companyShortname : '-'},
                        {text: _item.source.marketNo},
                        {text: _item.source.marketName}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.companyInfo.id ? '保存' : '添加', null, $scope.companyInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.clearData();
        loadData();
    }])
});