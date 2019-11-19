define(['app'], function (app) {
    app.controller('staffCtrl', ['$scope', '$state', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $state, ReqServ, appInterface, ngDialog) {
        $scope.staff = {};
        $scope.staffInfo = {};
        $scope.staffStatus = [{id: 0, text: '在职'}, {id: 1, text: '离职'}];
        var _item, _daptList;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: ReqServ.isSysManager($scope.userInfo.role) ? [{
                btnName: '添加员工信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.staffInfo.loading))
                        return;
                    ReqServ.clearObj($scope.staffInfo);
                    $scope.staffInfo.pageTitle = '添加员工信息';
                    $scope.staffInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editStaffTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editStaffOpenedCallback,
                        scope: $scope
                    });
                }
            }] : null,
            controls: [{
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.staffInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.staffInfo);
                    $scope.staffInfo.pageTitle = '编辑员工信息';
                    $scope.staffInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editStaffTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editStaffOpenedCallback,
                        scope: $scope
                    });
                }, isCheck: {key: 'deleteFlag', text: '0'}
            }, {
                name: '冻结', event: function (item) {
                    if ($scope.hasTaskRun($scope.staffInfo.loading) || !window.confirm('确定要冻结？'))
                        return;
                    $scope.staffInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyStafflist/delete', {id: item.source.id}).success(function (result) {
                        $scope.staffInfo.loading = false;
                        if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchStaff();
                    }).error(function (result) {
                        $scope.staffInfo.loading = false;
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    });
                }, isCheck: {key: 'deleteFlag', text: '0'}
            }],
            heads: [{title: '所属部门', dataKey: 'deptName', width: 150},
                {title: '姓名', dataKey: 'staffName', width: 90},
                {title: '职位', dataKey: 'position', width: 120},
                {title: '手机号', dataKey: 'tel', width: 90},
                {title: '区域', dataKey: 'workAreaname', width: 70},
                {title: '邮箱', dataKey: 'email', width: 120},
                {title: '离职', dataKey: 'deleteFlagText', width: 70},
                {dataKey: 'deleteFlag', hide: true}],
            isScrollLoaded: true,
            isCopyControls: true,
            controlWidth: 40
        };
        var loadDic = function () {
            ReqServ.request('POST', 'sys/dept/read/allDept', null).success(function (result) {
                $scope.deptList = [];
                $scope.deptListSearch = [];
                if (result.httpCode != appInterface.successCode)
                    return;
                _daptList = result.data;
                ReqServ.getTreeNode(_daptList, $scope.deptList, '0', 'deptName');
                ReqServ.getTreeNode(_daptList, $scope.deptListSearch, '0', 'deptName');
            }).error(function () {
                $scope.deptList = [];
                $scope.deptListSearch = [];
            });
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.staff.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'ty/tyStafflist/read/list', $scope.staff).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.sysWorkAreaList)
                    $scope.sysWorkAreaList = result.dicts['SysWorkAreas'];
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.staff);
            ReqServ.copyObj($scope.searchModel, $scope.staff);
            loadData();
        });
        $scope.searchStaff = function () {
            $scope.staff.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.staff, $scope.searchModel);
            loadData();
        };
        $scope.editStaffOpenedCallback = function () {
            if ($scope.sysPositions)
                return;
            ReqServ.request('POST', 'ty/tyStafflist/read/detail', {id: $scope.staffInfo.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.sysPositions = [];
                    return;
                }
                $scope.sysPositions = result.dicts['SysPositions'];
            }).error(function () {
                $scope.sysPositions = [];
            });
        };
        $scope.editStaff = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.staffInfo);
            ReqServ.request('POST', $scope.staffInfo.id ? 'ty/tyStafflist/update' : 'ty/tyStafflist/add', $scope.staffInfo).success(function (data) {
                ReqServ.setBtnStatus(false, $scope.staffInfo.id ? '保存' : '添加', null, $scope.staffInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                if ($scope.staffInfo.id) {
                    ReqServ.copyObj($scope.staffInfo, _item.source);
                    ReqServ.getTextValue($scope.sysWorkAreaList, _item.source, 'workAreaid', 'workAreaname');
                    ReqServ.getTextValue($scope.sysPositions, _item.source, 'positionId', 'position');
                    _item.item = [{text: _item.source.deptName},
                        {text: _item.source.staffName},
                        {text: _item.source.position},
                        {text: _item.source.tel},
                        {text: _item.source.workAreaname},
                        {text: _item.source.email},
                        {text: _item.source.deleteFlagText}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.staffInfo.id ? '保存' : '添加', null, $scope.staffInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.clearData();
        loadData();
        loadDic();
    }])
});