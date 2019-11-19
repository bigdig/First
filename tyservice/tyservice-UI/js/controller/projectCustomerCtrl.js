define(['app'], function (app) {
    app.controller('projectCustomerCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $state, $stateParams, $timeout, ReqServ, appInterface, ngDialog) {
        if (!$stateParams.id) {
            $state.go('master.resource.project');
            return;
        }
        $scope.tabBar.isShow = false;
        $scope.customer = {};
        $scope.customerInfo = {selectedCustomers: []};
        $scope.welcome.isBack = !!$stateParams.index;
        var currentName = $state.current.name;
        var _isLoaded = false;
        var _item;
        var dataModel = {
            toolsBar: [{
                btnName: '保存', bgStyle: 'bg-red-light text-orange', event: function () {
                    var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                    var orgCustIds = [];
                    var orgCustNames = [];
                    for (var i = 0; i < selectedList.length; i++) {
                        orgCustIds.push(selectedList[i].id);
                        orgCustNames.push(selectedList[i].custName);
                    }
                    _item.orgCustId = orgCustIds.join(',');
                    _item.orgCustName = orgCustNames.join(',');
                    $scope.disabled(false);
                    ReqServ.request('POST', 'ty/tyProject/update', _item).success(function (result) {
                        $scope.disabled(true);
                        if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            return;
                        }
                        $scope.showSuccess('保存参与客户信息完成。');
                    }).error(function (result) {
                        $scope.disabled(true);
                        $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    });
                }, isDisabled: true
            }],
            heads: [{title: '客户名称', dataKey: 'custName', width: 90},
                {title: '所属机构', dataKey: 'orgName', width: 200},
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
                    ReqServ.request('POST', 'ty/tyProject/read/detail', {id: $stateParams.id}).success(function (res) {
                        if (res.httpCode != appInterface.successCode) {
                            $scope.loading.isShow = false;
                            $scope.showAlert(result.msg ? res.msg : appInterface.alert);
                            return;
                        }
                        _isLoaded = true;
                        _item = res.data;
                        if (res.data.orgCustId) {
                            var custIds = res.data.orgCustId.split(',');
                            var custNames = res.data.orgCustName.split(',');
                            for (var i = 0; i < custIds.length; i++) {
                                pushSelectItem(selectedList, {id: custIds[i], custName: custNames[i]});
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