define(['app'], function (app) {
    app.controller('meetingCustomerCtrl', ['$scope', '$state', '$timeout', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $state, $timeout, ReqServ, appInterface, ngDialog) {
        $scope.customer = {};
        $scope.customerInfo = {};
        $scope.uploadDataModel.value = null;
        $scope.uploadDataModel.template = null;
        $scope.uploadDataModel.pageTitle = '批量导入客户信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'ty_prepcust_temp.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyOrgprepcust/batchImport';
        $scope.uploadDataModel.scope = null;
        $scope.welcome.isBack = true;
        $scope.servieSalers = [];
        var _item, serviceStaffs;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '批量导入客户信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    ngDialog.open({
                        template: 'uploadExcelTemplate',
                        scope: $scope.$parent,
                        preCloseCallback: $scope.$parent.uploadPreCloseCallback,
                        onOpenCallback: $scope.$parent.uploadOpenedCallback
                    })
                }
            }],
            controls: [{
                name: '审核', event: function (item) {
                    if ($scope.hasTaskRun($scope.customerInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.customerInfo);
                    $scope.customerInfo.labels = $scope.customerInfo.mark ? $scope.customerInfo.mark.split(' ') : [];
                    $scope.customerInfo.pageTitle = '客户信息';
                    ngDialog.open({
                        template: 'editMeetCustomerTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editCustomerOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }],
            heads: [{title: '客户名称', dataKey: 'custName', width: 90},
                {title: '所属机构', dataKey: 'orgName', width: 200},
                {title: '职位', dataKey: 'title', width: 120},
                {title: '研究行业', dataKey: 'industry', width: 120},
                {title: '手机号', dataKey: 'custMobile', width: 90},
                {title: '邮箱', dataKey: 'custEmail', width: 120},
                {title: '所在地', dataKey: 'area', width: 90},
                {title: '对口销售', dataKey: 'serviceSaler', width: 90},
                {title: '手机存在', dataKey: 'existMobileFlagText', width: 90},
                {title: '状态', dataKey: 'prepcustStatusText', width: 110},
                {title: '标签', dataKey: 'mark', width: 150}],
            isScrollLoaded: true,
            controlWidth: 40
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.customer.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'ty/tyOrgprepcust/read/list', $scope.customer).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.prepCustStatus) {
                    $scope.prepCustStatus = result.dicts['PREPCUSTSTATUS'];
                    $scope.existMobileFlag = result.dicts['EXISTMOBILEFLAG'];
                    $scope.searchIndustry = result.dicts['industry'];
                }
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
            $state.go('master.customer');
        });
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.customer);
            ReqServ.copyObj($scope.searchModel, $scope.customer);
            loadData();
        });
        $scope.searchCustomer = function () {
            $scope.customer.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.customer, $scope.searchModel);
            loadData();
        };
        $scope.editCustomer = function (flag) {
            if (flag == 1) {
                $scope.customerInfo.prepcustId = _item.source.id;
                $scope.customerInfo.id = null;
            } else {
                $scope.customerInfo.prepcustStatus = '2';
            }
            $scope.customerInfo.mark = ($scope.customerInfo.labels && $scope.customerInfo.labels.length > 0) ? $scope.customerInfo.labels.join(' ') : null;
            $scope.customerInfo.loading = true;
            ReqServ.request('POST', flag == 1 ? 'ty/tyOrgprepcust/saveCust' : 'ty/tyOrgprepcust/update', $scope.customerInfo).success(function (data) {
                $scope.customerInfo.loading = false;
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                ngDialog.close();
                ReqServ.copyObj($scope.customerInfo, _item.source);
                _item.source.prepcustStatusText = flag == 1 ? '审核通过' : '审核不通过';
                _item.item = [{text: _item.source.custName, width: 90},
                    {text: _item.source.orgName, width: 200},
                    {text: _item.source.title, width: 120},
                    {text: _item.source.industry ? _item.source.industry : '-', width: 120},
                    {text: _item.source.custMobile, width: 90},
                    {text: _item.source.custEmail ? _item.source.custEmail : '-', width: 120},
                    {text: _item.source.area ? _item.source.area : '-', width: 90},
                    {text: _item.source.serviceSaler, width: 90},
                    {text: _item.source.existMobileFlagText, width: 90},
                    {text: _item.source.prepcustStatusText, width: 110},
                    {text: _item.source.mark ? _item.source.mark : '-', width: 150}];
            }).error(function (result) {
                $scope.customerInfo.loading = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editCustomerOpenedCallback = function () {
            function init() {
                $scope.servieSalers.length = 0;
                var hasOrg = false, hasSaler = false;
                for (var i = 0; i < $scope.tyServiceorgs.length; i++) {
                    if ($scope.tyServiceorgs[i].text.indexOf($scope.customerInfo.orgName) >= 0) {
                        $scope.customerInfo.orgId = $scope.tyServiceorgs[i].id;
                        $scope.customerInfo.orgName = $scope.tyServiceorgs[i].text;
                        hasOrg = true;
                        var list = $scope.tyServiceorgs[i].remark.split(',');
                        for (var j = 0; j < list.length; j++) {
                            for (var k = 0; k < serviceStaffs.length; k++) {
                                if (serviceStaffs[k].id == list[j]) {
                                    serviceStaffs[k].selected = hasSaler = serviceStaffs[k].text.indexOf($scope.customerInfo.serviceSaler) >= 0;
                                    if (hasSaler)
                                        $scope.customerInfo.salerId = serviceStaffs[k].id;
                                    $scope.servieSalers.push(serviceStaffs[k]);
                                }
                            }
                        }
                    }
                }
                if (!hasSaler)
                    $scope.customerInfo.salerId = '';
                if (!hasOrg) {
                    $scope.customerInfo.orgName = null;
                    $scope.customerInfo.serviceSaler = null;
                }
            }

            $scope.removeMark = function (index) {
                $scope.customerInfo.labels.splice(index, 1);
            };
            if ($scope.tyServiceorgs) {
                init();
                return;
            }
            $scope.customerInfo.pattern = /^([a-zA-Z0-9\(\)（）_-]|[\u4e00-\u9fa5]){1,64}$/;
            ReqServ.request('POST', 'ty/tyOrgprepcust/read/detail', {id: $scope.customerInfo.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.tyServiceorgs = [];
                    serviceStaffs = [];
                    return;
                }
                $scope.tyServiceorgs = result.dicts['TyServiceorgs'];
                serviceStaffs = result.dicts['TyStafflists'];
                init();
            }).error(function () {
                $scope.tyServiceorgs = [];
                serviceStaffs = [];
            })
        };
        $scope.setSaler = function (data) {
            $scope.servieSalers.length = 0;
            if (data) {
                var list = data.remark.split(',');
                for (var j = 0; j < list.length; j++) {
                    for (var i = 0; i < serviceStaffs.length; i++) {
                        if (serviceStaffs[i].id == list[j])
                            $scope.servieSalers.push(serviceStaffs[i]);
                    }
                }
                if ($scope.servieSalers.length > 0) {
                    $scope.servieSalers[0].selected = true;
                    $scope.customerInfo.salerId = $scope.servieSalers[0].id;
                }
            } else
                $scope.customerInfo.salerId = null;
        };
        $scope.clearData();
        loadData();
    }])
});