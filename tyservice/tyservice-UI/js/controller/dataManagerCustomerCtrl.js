define(['app'], function (app) {
    app.controller('dataManagerCustomerCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $state, $stateParams, $timeout, ReqServ, appInterface, ngDialog) {
        if (!$stateParams.id) {
            $state.go('master.datamanager.' + $stateParams.model);
            return;
        }
        $scope.customer = {};
        $scope.customerInfo = {selectedCustomers: []};
        $scope.welcome.isBack = !!$stateParams.index;
        var currentName = $state.current.name;
        var _hasCustomer = false, _isLoaded = false;
        var dataModel = {
            toolsBar: [{
                btnName: '保存', bgStyle: 'bg-red-light text-orange', event: function () {
                    var data = {actId: $stateParams.id, custIds: []};
                    var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                    for (var i = 0; i < selectedList.length; i++) {
                        data.custIds.push(selectedList[i].id);
                    }
                    $scope.disabled(false);
                    ReqServ.request('POST', 'ty/tyActivitysign/update', data).success(function (result) {
                        $scope.disabled(true);
                        if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            return;
                        }
                        $scope.showSuccess('保存参与客户信息完成。');
                        _hasCustomer = true;
                    }).error(function (result) {
                        $scope.disabled(true);
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    });
                }, isDisabled: true
            }],
            heads: [{title: '客户名称', dataKey: 'custName', width: 90},
                {title: '所属机构', dataKey: 'orgName', width: 150},
                {title: '手机号', dataKey: 'custMobile', width: 90},
                {title: '邮箱', dataKey: 'custEmail', width: 120},
                {title: '所在地', dataKey: 'area', width: 90},
                {title: '对口销售', dataKey: 'serviceSaler', width: 90}],
            checkFunc: function (customer) {
                var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                if (customer.hasCheckBox.isChecked) {
                    selectedList.push({id: customer.source.id, custName: customer.source.custName});
                } else {
                    for (var i = 0; i < selectedList.length; i++) {
                        if (selectedList[i].id == customer.source.id) {
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
                nameList.push(selectedList[i].custName);
            }
            $scope.customerInfo.selectedCustomers = '已参与客户：' + nameList.join(' ');
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
            if (!$scope.searchOrgs) {
                $scope.searchOrgs = result.dicts['Serviceorgs'];
                $scope.searchStafflist = result.dicts['TyStafflists'];
                ReqServ.removeStore('searchOrgs');
                ReqServ.setStore('searchOrgs', JSON.stringify($scope.searchOrgs));
                ReqServ.removeStore('searchStafflist');
                ReqServ.setStore('searchStafflist', JSON.stringify($scope.searchStafflist));
            }
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
            $scope.customer.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'ty/tyOrgcustomer/read/list', $scope.customer).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                if (!_isLoaded) {
                    ReqServ.request('POST', 'ty/tyActivitysign/read/list', {actId: $stateParams.id}).success(function (res) {
                        if (res.httpCode != appInterface.successCode) {
                            $scope.loading.isShow = false;
                            $scope.showAlert(result.msg ? res.msg : appInterface.alert);
                            return;
                        }
                        _isLoaded = true;
                        for (var i = 0; i < res.data.length; i++) {
                            pushSelectItem(selectedList, {id: res.data[i].custId, custName: res.data[i].custName});
                        }
                        if (selectedList.length > 0) {
                            _hasCustomer = true;
                            ReqServ.setStore('selectedItem', JSON.stringify(selectedList));
                        }
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
            $state.go('master.datamanager.' + $stateParams.model, {index: $stateParams.index, customer: _hasCustomer});
        });
        $scope.$on('loadPageData', function (evt, data) {
            ReqServ.clearObj($scope.customer);
            ReqServ.copyObj($scope.searchModel, $scope.customer);
            if (data)
                ReqServ.removeStore('datas');
            loadData();
        });
        $scope.searchCustomer = function () {
            ReqServ.removeStore('datas');
            $scope.customer.pageNum = 1;
            $scope.clearData();
            $scope.setSelectAll(false);
            ReqServ.copyObj($scope.customer, $scope.searchModel);
            loadData();
        };
        ReqServ.removeStore('datas');
        ReqServ.removeStore('selectedItem');
        $scope.clearData();
        loadData();
    }])
});