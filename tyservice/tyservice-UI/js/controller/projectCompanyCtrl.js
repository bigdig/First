define(['app'], function (app) {
    app.controller('projectCompanyCtrl', ['$scope', '$state', '$stateParams', 'ReqServ', 'appInterface', function ($scope, $state, $stateParams, ReqServ, appInterface) {
        if (!$stateParams.id) {
            $state.go('master.resource.project');
            return;
        }
        $scope.tabBar.isShow = false;
        $scope.company = {};
        $scope.companyInfo = {selectedCompanies: []};
        $scope.welcome.isBack = !!$stateParams.index;
        var currentName = $state.current.name;
        var _isLoaded = false;
        var _item;
        var dataModel = {
            toolsBar: [{
                btnName: '保存', bgStyle: 'bg-red-light text-orange', event: function () {
                    var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                    var companyIds = [];
                    var companyNames = [];
                    for (var i = 0; i < selectedList.length; i++) {
                        companyIds.push(selectedList[i].id);
                        companyNames.push(selectedList[i].stockName);
                    }
                    _item.companyId = companyIds.join(',');
                    _item.companyName = companyNames.join(',');
                    $scope.disabled(false);
                    ReqServ.request('POST', 'ty/tyProject/update', _item).success(function (result) {
                        $scope.disabled(true);
                        if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            return;
                        }
                        $scope.showSuccess('保存参与上市公司信息完成。');
                    }).error(function (result) {
                        $scope.disabled(true);
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    });
                }, isDisabled: true
            }],
            heads: [{title: '证券类别', dataKey: 'marketKind', width: 90},
                {title: '交易代码', dataKey: 'stockCode', width: 90},
                {title: '证券简称', dataKey: 'stockShortname', width: 90},
                {title: '中文名称', dataKey: 'stockName', width: 150},
                {title: '交易市场', dataKey: 'marketName', width: 120}],
            checkFunc: function (company) {
                var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                if (company.hasCheckBox.isChecked) {
                    selectedList.push({id: company.source.id, stockName: company.source.stockName});
                } else {
                    for (var i = 0; i < selectedList.length; i++) {
                        if (selectedList[i].id == company.source.id) {
                            selectedList.splice(i, 1);
                            break;
                        }
                    }
                    $scope.setSelectAll(false);
                }
                if (selectedList.length <= 0)
                    ReqServ.removeStore('selectedItem');
                else
                    ReqServ.setStore('selectedItem', JSON.stringify(selectedList));
                $scope.disabled();
                getSelectedName();
            },
            isScrollLoaded: true
        };
        var getSelectedName = function () {
            var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
            var nameList = [];
            for (var i = 0; i < selectedList.length; i++) {
                nameList.push(selectedList[i].stockName);
            }
            $scope.companyInfo.selectedCompanies = '参与的上市公司：' + nameList.join(' ');
        };
        var pushSelectItem = function (list, item) {
            for (var i = 0; i < list.length; i++) {
                if (item.id == list[i].id)
                    return;
            }
            list.push(item);
        };
        var setSelectedItem = function () {
            var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
            if (dataModel.data) {
                for (var j = 0; j < selectedList.length; j++) {
                    for (var i = 0; i < dataModel.data.length; i++) {
                        if (selectedList[j].id == dataModel.data[i].id) {
                            dataModel.data[i].isChecked = true;
                            break;
                        }
                    }
                }
            }
        };
        var loadedData = function (result) {
            dataModel.data = result.data.list;
            dataModel.pageInfo = {
                size: result.data.size,
                total: result.data.total,
                pages: result.data.pages
            };
            ReqServ.removeStore('searchModels');
            ReqServ.setStore('searchModels', JSON.stringify($scope.searchModel));
            ReqServ.removeStore('pageInfos');
            ReqServ.setStore('pageInfos', JSON.stringify(dataModel.pageInfo));
            if (ReqServ.getStore('datas')) {
                var _data = JSON.parse(ReqServ.getStore('datas'));
                ReqServ.removeStore('datas');
                ReqServ.setStore('datas', JSON.stringify(_data.concat(dataModel.data)));
            } else
                ReqServ.setStore('datas', JSON.stringify(dataModel.data));
            getSelectedName();
            setSelectedItem();
            $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            $scope.disabled();
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
                var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                if (!_isLoaded) {
                    ReqServ.request('POST', 'ty/tyProject/read/detail', {id: $stateParams.id}).success(function (res) {
                        if (res.httpCode != appInterface.successCode) {
                            $scope.loading.isShow = false;
                            $scope.showAlert(result.msg ? res.msg : appInterface.alert);
                            return;
                        }
                        _isLoaded = true;
                        _item = res.data;
                        if (res.data.companyId) {
                            var companyIds = res.data.companyId.split(',');
                            var companyNames = res.data.companyName.split(',');
                            for (var i = 0; i < companyIds.length; i++) {
                                pushSelectItem(selectedList, {id: companyIds[i], stockName: companyNames[i]});
                            }
                        }
                        if (selectedList.length > 0)
                            ReqServ.setStore('selectedItem', JSON.stringify(selectedList));
                        loadedData(result);
                    }).error(function (res) {
                        $scope.loading.isShow = false;
                        $scope.showAlert((res && res.msg) ? result.msg : appInterface.alert);
                    });
                    return;
                }
                loadedData(result);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('goBack', function (evt) {
            $scope.loading.isShow = true;
            $state.go('master.resource.project', $stateParams);
        });
        $scope.$on('loadPageData', function (evt, data) {
            ReqServ.clearObj($scope.customer);
            ReqServ.copyObj($scope.searchModel, $scope.customer);
            if (data)
                ReqServ.removeStore('datas');
            loadData();
        });
        $scope.searchCompany = function () {
            ReqServ.removeStore('datas');
            $scope.company.pageNum = 1;
            $scope.clearData();
            $scope.setSelectAll(false);
            ReqServ.copyObj($scope.company, $scope.searchModel);
            loadData();
        };
        ReqServ.removeStore('datas');
        ReqServ.removeStore('selectedItem');
        $scope.clearData();
        loadData();
    }])
});