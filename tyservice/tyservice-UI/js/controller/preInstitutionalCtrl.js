define(['app'], function (app) {
    app.controller('preInstitutionalCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $state, $stateParams, $timeout, ReqServ, appInterface, ngDialog) {
        $scope.institutional = {};
        $scope.institutionalInfo = {};
        $scope.uploadDataModel.value = null;
        $scope.uploadDataModel.template = null;
        $scope.uploadDataModel.pageTitle = '批量导入机构信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'ty_prepinst_temp.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyPreserviceorg/batchImport';
        $scope.uploadDataModel.scope = null;
        $scope.welcome.isBack = true;
        $scope.clearData();
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '批量导入机构信息', bgStyle: 'bg-purple-light text-purple', event: function () {
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
                    if ($scope.hasTaskRun($scope.institutionalInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.institutionalInfo);
                    $scope.institutionalInfo.labels = $scope.institutionalInfo.mark ? $scope.institutionalInfo.mark.split(' ') : [];
                    $scope.institutionalInfo.pageTitle = '客户信息';
                    ngDialog.open({
                        template: 'editInstitutionalTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editInsOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }],
            heads: [{title: '机构名称', dataKey: 'orgName', width: 200},
                {title: '机构类型', dataKey: 'custCatName', width: 90},
                {title: '对口销售', dataKey: 'serviceSaler', width: 120},
                {title: '机构级别', dataKey: 'orgLevelName', width: 90},
                {title: '机构状态', dataKey: 'custStatusName', width: 90},
                {title: '白名单到期日', dataKey: 'whiteDeadline', width: 120},
                {title: '区域', dataKey: 'teamCat', width: 120},
                {title: '机构存在', dataKey: 'orgExistsText', width: 90},
                {title: '状态', dataKey: 'preporgStatusText', width: 110},
                {title: '标签', dataKey: 'mark', width: 150}],
            isScrollLoaded: true,
            controlWidth: 40
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.institutional.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'ty/tyPreserviceorg/read/list', $scope.institutional).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.searchLevels) {
                    $scope.searchLevels = result.dicts['OrgLevels'];
                    $scope.searchStatuss = result.dicts['CustStatuss'];
                    $scope.searchArea = result.dicts['SysWorkAreas'];
                    $scope.searchCats = result.dicts['CustCats'];
                    $scope.searchOrgs = result.dicts['Serviceorgs'];
                    $scope.searchStafflist = result.dicts['TyStafflists'];
                    $scope.preporgStatus = result.dicts['PREPORGSTATUS'];
                    $scope.orgExists = result.dicts['ORGEXISTS'];
                }
                result.data.list.forEach(function (value) {
                    value.whiteDeadline = ReqServ.getDateString(value.whiteDeadline + '');
                });
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
            $state.go('master.institutional');
        });
        $scope.$on('loadPageData', function (evt, data) {
            ReqServ.clearObj($scope.institutional);
            ReqServ.copyObj($scope.searchModel, $scope.institutional);
            loadData();
        });
        $scope.searchInstitutional = function () {
            $scope.institutional.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.institutional, $scope.searchModel);
            loadData();
        };
        $scope.editInsOpenedCallback = function () {
            $scope.institutionalInfo.recEmail = $scope.institutionalInfo.recEmail == '1';
            $scope.institutionalInfo.recSms = $scope.institutionalInfo.recSms == '1';
            if ($scope.tyStafflists)
                return;
            ReqServ.request('POST', 'ty/tyPreserviceorg/read/detail', {id: $scope.institutionalInfo.id}).success(function (data) {
                if (data.httpCode != appInterface.successCode) {
                    $scope.tyStafflists = [];
                    return;
                }
                $scope.tyStafflists = data.dicts['TyStafflists'];
            }).error(function () {
                $scope.tyStafflists = [];
            });
        };
        $scope.editInstitutional = function (flag) {
            if (flag == 1)
                $scope.institutionalInfo.id = _item.source.id;
            else
                $scope.institutionalInfo.preporgStatus = '2';
            $scope.institutionalInfo.recEmail = $scope.institutionalInfo.recEmail ? 1 : 0;
            $scope.institutionalInfo.recSms = $scope.institutionalInfo.recSms ? 1 : 0;
            $scope.institutionalInfo.whiteDeadline = $scope.institutionalInfo.custStatus == '3' ? null : ReqServ.getDateNum($scope.institutionalInfo.whiteDeadline);
            $scope.institutionalInfo.loading = true;
            ReqServ.request('POST', flag == 1 ? 'ty/tyPreserviceorg/saveOrg' : 'ty/tyPreserviceorg/update', $scope.institutionalInfo).success(function (result) {
                $scope.institutionalInfo.loading = false;
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result ? result.msg : appInterface.alert);
                    return;
                }
                ngDialog.close();
                $scope.institutionalInfo.whiteDeadline = $scope.institutionalInfo.whiteDeadline ? ReqServ.getDateString($scope.institutionalInfo.whiteDeadline + '') : '-';
                ReqServ.copyObj($scope.institutionalInfo, _item.source);
                ReqServ.getTextValue($scope.searchLevels, _item.source, 'orgLevel', 'orgLevelName');
                ReqServ.getTextValue($scope.searchStatuss, _item.source, 'custStatus', 'custStatusName');
                ReqServ.getTextValue($scope.searchCats, _item.source, 'custCat', 'custCatName');
                _item.source.preporgStatusText = flag == 1 ? '审核通过' : '审核不通过';
                _item.source.preporgStatus = flag;
                _item.item = [{text: _item.source.orgName, width: 200},
                    {text: _item.source.custCatName, width: 90},
                    {text: _item.source.serviceSaler, width: 120},
                    {text: _item.source.orgLevelName, width: 90},
                    {text: _item.source.custStatusName, width: 90},
                    {text: _item.source.whiteDeadline ? _item.source.whiteDeadline : '-', width: 120},
                    {text: _item.source.teamCat, width: 120},
                    {text: _item.source.orgExistsText, width: 90},
                    {text: _item.source.preporgStatusText, width: 110},
                    {text: _item.source.mark ? _item.source.mark : '-', width: 150}];
            }).error(function (result) {
                $scope.institutionalInfo.loading = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
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