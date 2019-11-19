define(['app'], function (app) {
    app.controller('roleCtrl', ['$scope', '$state', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, ReqServ, ngDialog, appInterface) {
        $scope.role = {};
        $scope.permission = {};
        $scope.roleInfo = {pageTitle: '添加角色信息', btnName: '添加'};
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加角色信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.roleInfo.loading))
                        return;
                    ReqServ.clearObj($scope.roleInfo);
                    $scope.roleInfo.pageTitle = '添加角色信息';
                    $scope.roleInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editRoleTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.roleInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.roleInfo);
                    $scope.roleInfo.pageTitle = '编辑角色信息';
                    $scope.roleInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editRoleTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '菜单', event: function (item) {
                    $scope.roleInfo.id = item.source.id;
                    $scope.roleInfo.roleName = item.source.roleName;
                    $scope.roleInfo.btnName = '确定';
                    ngDialog.open({
                        template: 'editMenuTemplate',
                        className: 'ngdialog-theme-plain',
                        onOpenCallback: $scope.editMenuOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }],
            heads: [{title: '角色名称', dataKey: 'roleName', width: 100},
                {title: '角色编码', dataKey: 'roleCode', width: 100},
                {title: '公司', dataKey: 'deptName', width: 100}],
            isScrollLoaded: true,
            controlWidth: 80
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.role.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'sys/role/read/list', $scope.role).success(function (result) {
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
        var getMenuId = function (menu, menus) {
            for (var i = 0; i < menu.children.length; i++) {
                if (menu.children[i].checked) {
                    menus.push(menu.children[i].id);
                    arguments.callee(menu.children[i], menus);
                }
            }
        };
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.role);
            ReqServ.copyObj($scope.searchModel, $scope.role);
            loadData();
        });
        $scope.searchRole = function () {
            $scope.role.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.role, $scope.searchModel);
            loadData();
        };
        $scope.editRole = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.roleInfo);
            ReqServ.request('POST', $scope.roleInfo.id ? 'sys/role/update' : 'sys/role/add', $scope.roleInfo).success(function (data) {
                ReqServ.setBtnStatus(false, $scope.roleInfo.id ? '保存' : '添加', null, $scope.roleInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                if ($scope.roleInfo.id) {
                    for (var i = 0; i < $scope.companies.length; i++) {
                        if ($scope.roleInfo.deptId == $scope.companies[i].id) {
                            _item.source.deptName = $scope.companies[i].text;
                            break;
                        }
                    }
                    ReqServ.copyObj($scope.roleInfo, _item.source);
                    _item.item = [{text: _item.source.roleName},
                        {text: _item.source.roleCode},
                        {text: _item.source.deptName}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.roleInfo.id ? '保存' : '添加', null, $scope.roleInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editOpenedCallback = function () {
            var setCompanies = function () {
                for (var i = 0; i < $scope.companies.length; i++) {
                    $scope.companies[i].selected = $scope.companies[i].id == $scope.roleInfo.deptId;
                }
            };
            if ($scope.companies) {
                setCompanies();
                return;
            }
            ReqServ.request('POST', 'sys/role/read/detail', {id: $scope.roleInfo.id}).success(function (data) {
                if (data.httpCode != appInterface.successCode) {
                    $scope.companies = [];
                    return;
                }
                $scope.companies = data.dicts['SysDepts'];
                setCompanies();
            }).error(function () {
                $scope.companies = [];
            })
        };
        $scope.menuSave = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.roleInfo);
            var menu = [];
            for (var i = 0; i < $scope.tree.length; i++) {
                if ($scope.tree[i].checked) {
                    menu.push($scope.tree[i].id);
                    getMenuId($scope.tree[i], menu);
                }
            }
            ReqServ.request('POST', 'sys/role/menu/update', {
                menu: menu,
                roleId: $scope.roleInfo.id
            }).success(function (data) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.roleInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                ngDialog.close();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.roleInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            })
        };
        $scope.editMenuOpenedCallback = function () {
            ReqServ.request('POST', 'sys/menu/read/list', {pageSize: 1000}).success(function (result) {
                $scope.tree = [];
                if (result.httpCode != appInterface.successCode)
                    return;
                ReqServ.request('POST', 'sys/role/read/menu', {id: $scope.roleInfo.id}).success(function (data) {
                    if (data.httpCode != appInterface.successCode)
                        return;
                    for (var m = 0; m < result.data.list.length; m++) {
                        for (var n = 0; n < data.dicts.length; n++) {
                            if (result.data.list[m].id == data.dicts[n].menuId) {
                                result.data.list[m].checked = data.dicts[n].isSelected == '1';
                                break;
                            }
                        }
                    }
                    ReqServ.getTreeNode(result.data.list, $scope.tree, '0', 'menuName');
                });
            }).error(function () {
                $scope.tree = [];
            });
        };
        $scope.callbackFunc = function (node) {
            for (var i = 0; i < $scope.tree.length; i++) {
                if ($scope.tree[i].id == node.parentId) {
                    $scope.tree[i].checked = true;
                    break;
                }
            }
        };
        $scope.clearData();
        loadData();
    }])
});