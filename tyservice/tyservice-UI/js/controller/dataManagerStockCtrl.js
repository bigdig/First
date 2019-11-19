define(['app'], function (app) {
    app.controller('dataManagerStockCtrl', ['$scope', '$state', '$stateParams', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, $stateParams, ReqServ, ngDialog, appInterface) {
        if (!$stateParams.id) {
            $state.go('master.datamanager.' + $stateParams.model);
            return;
        }
        $scope.welcome.isBack = !!$stateParams.index;
        $scope.stockInfo = {};
        var _item, _hasStock;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加推荐股票', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.stockInfo.loading))
                        return;
                    ReqServ.clearObj($scope.stockInfo);
                    $scope.stockInfo.pageTitle = '添加推荐股票';
                    $scope.stockInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editStockTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.stockInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.stockInfo);
                    $scope.stockInfo.pageTitle = '编辑推荐股票';
                    $scope.stockInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editStockTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.stockInfo.loading) || !window.confirm('确定要删除？'))
                        return;
                    $scope.stockInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyDcrecostock/delete', {
                        id: item.source.id,
                        speakId: $stateParams.id
                    }).success(function (data) {
                        $scope.stockInfo.loading = false;
                        if (data.httpCode != appInterface.successCode) {
                            $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchModel.pageNum = 1;
                        $scope.clearData();
                        loadData();
                    }).error(function (result) {
                        $scope.stockInfo.loading = false;
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    })
                }, isShow: true
            }],
            heads: [{title: '股票代码', dataKey: 'stockCode', width: 90},
                {title: '股票名称', dataKey: 'stockName', width: 90},
                // {title: '行业', dataKey: 'firstindustryname'},
                {title: '市场', dataKey: 'marketNo', width: 90},
                {title: '推荐日期', dataKey: 'recommendDate', width: 90},
                {title: '推荐团队', dataKey: 'team', width: 90},
                {title: '推荐类型', dataKey: 'recommendTypeName', width: 90},
                {title: '在股票池', dataKey: 'deleteFlag', width: 90}
            ],
            isScrollLoaded: true,
            hidePages: true,
            controlWidth: 80
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            ReqServ.request('POST', 'ty/tyDcrecostock/read/list', {actId: $stateParams.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.teams) {
                    $scope.markets = ['CN', 'HK', 'US'];
                    $scope.teams = result.dicts['Researchdept'];
                    $scope.recommendTypes = result.dicts['recommendTypes'];
                }
                _hasStock = result.data.length > 0;
                for (var i = 0; i < result.data.length; i++) {
                    result.data[i].recommendDate = ReqServ.getDateString(result.data[i].recommendTime);
                }
                dataModel.data = result.data;
                dataModel.pageInfo = {size: result.data.length, total: result.data.length, pages: 1};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('goBack', function (evt) {
            $scope.loading.isShow = true;
            $state.go('master.datamanager.' + $stateParams.model, {index: $stateParams.index, stock: _hasStock});
        });
        $scope.editStock = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.stockInfo);
            $scope.stockInfo.recommendTime = ReqServ.getDateNum($scope.stockInfo.recommendDate);
            $scope.stockInfo.actId = $stateParams.id;
            for (var i = 0; i < $scope.recommendTypes.length; i++) {
                if ($scope.recommendTypes[i].id == $scope.stockInfo.recommendType) {
                    $scope.stockInfo.recommendTypeName = $scope.recommendTypes[i].text;
                    break;
                }
            }
            ReqServ.request('POST', $scope.stockInfo.id ? 'ty/tyDcrecostock/update' : 'ty/tyDcrecostock/add', $scope.stockInfo).success(function (result) {
                    ReqServ.setBtnStatus(false, $scope.stockInfo.id ? '保存' : '添加', null, $scope.stockInfo);
                    if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                        return;
                    }
                    if ($scope.stockInfo.id) {
                        ReqServ.copyObj($scope.stockInfo, _item.source);
                        _item.item = [{text: _item.source.stockCode},
                            {text: _item.source.stockName},
                            // {text: _item.source.firstindustryname ? _item.source.firstindustryname : '-'},
                            {text: _item.source.marketNo},
                            {text: _item.source.recommendDate},
                            {text: _item.source.team},
                            {text: _item.source.recommendTypeName}];
                        ngDialog.close();
                        return;
                    }
                    ngDialog.close();
                    $scope.clearData();
                    loadData();
                }
            ).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.stockInfo.id ? '保存' : '添加', null, $scope.stockInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            $scope.stockInfo.dirty = false;
        };
        $scope.clearData();
        loadData();
    }])
});