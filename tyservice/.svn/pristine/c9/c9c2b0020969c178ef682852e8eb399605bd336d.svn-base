define(['app'], function (app) {
    app.controller('personCtrl', ['$scope', '$state', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, ReqServ, ngDialog, appInterface) {
        $scope.person = {};
        $scope.personInfo = {};
        var _item, _daptList;
        var currentName = $state.current.name;
        var lock = function (data_item, locked) {
            ReqServ.request('POST', 'sys/user/lock', {id: data_item.source.id, lock: locked}).success(function (data) {
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg);
                    return;
                }
                data_item.item[4].text = locked ? '是' : '否';
                if (data_item.controls[1].name == '冻结')
                    data_item.controls[1].isShow = !locked;
                if (data_item.controls[2].name == '解冻')
                    data_item.controls[2].isShow = locked;
            }).error(function (result) {
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        var dataModel = {
            toolsBar: [{
                btnName: '添加账户信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.personInfo.loading))
                        return;
                    ReqServ.clearObj($scope.personInfo);
                    $scope.personInfo.pageTitle = '添加账户信息';
                    $scope.personInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editPersonTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.personInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.personInfo);
                    $scope.personInfo.password = null;
                    $scope.personInfo.pageTitle = '编辑账户信息';
                    $scope.personInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editPersonTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }, {
                name: '冻结', event: function (item) {
                    lock(item, 1);
                }, locked: 'locked'
            }, {
                name: '解冻', event: function (item) {
                    lock(item, 0);
                }, unlocked: 'locked'
            }, {
                name: '角色', event: function (item) {
                    $scope.personInfo.id = item.source.id;
                    $scope.personInfo.account = item.source.account;
                    $scope.personInfo.btnName = '确定';
                    ngDialog.open({
                        template: 'editPersonRoleTemplate',
                        className: 'ngdialog-theme-plain',
                        onOpenCallback: $scope.editRoleOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            heads: [{title: '账户', dataKey: 'account', width: 100},
                {title: '姓名', dataKey: 'userName', width: 90},
                {title: '所属部门', dataKey: 'deptName', width: 100},
                {title: '联系电话', dataKey: 'phone', width: 100},
                {title: '冻结', dataKey: 'locked', width: 70}],
            isScrollLoaded: true,
            isCopyControls: true,
            controlWidth: 120
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.person.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'sys/user/read/list', $scope.person).success(function (result) {
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
            ReqServ.clearObj($scope.person);
            ReqServ.copyObj($scope.searchModel, $scope.person);
            loadData();
        });
        $scope.searchPerson = function () {
            $scope.person.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.person, $scope.searchModel);
            loadData();
        };
        $scope.editPerson = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.personInfo);
            ReqServ.request('POST', $scope.personInfo.id ? 'sys/user/update' : 'sys/user/add', $scope.personInfo).success(function (data) {
                ReqServ.setBtnStatus(false, $scope.personInfo.id ? '保存' : '添加', null, $scope.personInfo);
                if (data.httpCode != appInterface.successCode) {
                    ReqServ.setBtnStatus(false, $scope.personInfo.id ? '保存' : '添加', null, $scope.personInfo);
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                if ($scope.personInfo.id) {
                    ReqServ.copyObj($scope.personInfo, _item.source);
                    _item.item = [{text: _item.source.account},
                        {text: _item.source.userName},
                        {text: _item.source.deptName},
                        {text: _item.source.phone ? _item.source.phone : '-'},
                        {text: _item.source.locked ? '是' : '否'}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.personInfo.id ? '保存' : '添加', null, $scope.personInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            if ($scope.deptList)
                return;
            ReqServ.request('POST', 'sys/dept/read/allDept', null).success(function (result) {
                $scope.deptList = [];
                if (result.httpCode != appInterface.successCode)
                    return;
                _daptList = result.data;
                ReqServ.getTreeNode(_daptList, $scope.deptList, '0', 'deptName');
            }).error(function () {
                $scope.deptList = [];
            });
        };
        $scope.roleSave = function () {
            var ids = [];
            for (var i = 0; i < $scope.roles.length; i++) {
                if ($scope.roles[i].isSelected)
                    ids.push($scope.roles[i].roleId);
            }
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.personInfo);
            ReqServ.request('POST', 'sys/user/role/add', {
                id: $scope.personInfo.id,
                rloe: ids
            }).success(function (data) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.personInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                ngDialog.close();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.personInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editRoleOpenedCallback = function () {
            ReqServ.request('POST', 'sys/user/read/teamrole', {id: $scope.personInfo.id}).success(function (data) {
                if (data.httpCode != appInterface.successCode) {
                    $scope.roles = [];
                    return;
                }
                for (var i = 0; i < data.dicts.length; i++) {
                    data.dicts[i].isSelected = data.dicts[i].isSelected == '1';
                }
                $scope.roles = data.dicts;
            }).error(function () {
                $scope.roles = [];
            })
        };
        $scope.clearData();
        loadData();
    }])
});