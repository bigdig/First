define(['app'], function (app) {
    app.controller('departmentCtrl', ['$scope', '$state', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, ReqServ, ngDialog, appInterface) {
        $scope.department = {parentId: $scope.userInfo.dept.id};
        $scope.departmentInfo = {};
        var _item;
        var _list = [];
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加部门信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    ReqServ.clearObj($scope.departmentInfo);
                    $scope.departmentInfo.parentId = '1001';
                    $scope.departmentInfo.pageTitle = '添加部门信息';
                    $scope.departmentInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editDepartmentTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editDepartmentOpened,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '编辑', event: function (item) {
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.departmentInfo);
                    $scope.departmentInfo.deptId = $scope.departmentInfo.parentId;
                    $scope.departmentInfo.pageTitle = '编辑部门信息';
                    $scope.departmentInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editDepartmentTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editDepartmentOpened,
                        scope: $scope
                    });
                }, isShow: true
            }],
            heads: [{title: '部门名称', dataKey: 'deptName', width:200},
                {title: '部门代码', dataKey: 'deptNo', width:100},
                {title: '描述', dataKey: 'deptDesc', width:150}],
            isScrollLoaded: true,
            controlWidth: 40
        };
        var getParentData = function (keyList, list, tree) {
            function getParent(parentId, list, tree) {
                for (var j = 0; j < list.length; j++) {
                    if (list[j].id == parentId) {
                        var isExisist = false;
                        for (var m = 0; m < tree.length; m++) {
                            if (tree[m].id == parentId) {
                                isExisist = true;
                                break;
                            }
                        }
                        if (!isExisist) {
                            tree.push(list[j]);
                            if (list[j].parentId != '1001')
                                arguments.callee(list[j].parentId, list, tree);
                        }
                    }
                }
            }

            for (var i = 0; i < keyList.length; i++) {
                tree.push(keyList[i]);
                if (keyList[i].parentId != '1001')
                    getParent(keyList[i].parentId, list, tree);
            }
        };
        var getAllData = function (tree, list) {
            function getTreeNode(children, list) {
                for (var j = 0; j < children.length; j++) {
                    var _children = children[j].children ? children[j].children : null;
                    children[j].children = null;
                    list.push(children[j]);
                    if (_children)
                        arguments.callee(_children, list);
                }
            }

            list.length = 0;
            for (var i = 0; i < tree.length; i++) {
                var children = tree[i].children ? tree[i].children : null;
                tree[i].children = null;
                list.push(tree[i]);
                if (children)
                    getTreeNode(children, list);
            }
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.department.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'sys/dept/read/deptlist', $scope.department).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
                getAllData(result.data.list, _list);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.department);
            ReqServ.copyObj($scope.searchModel, $scope.department);
            loadData();
        });
        $scope.searchDepartment = function () {
            $scope.department.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.department, $scope.searchModel);
            if ($scope.department.deptName && $scope.department.deptName.length > 0) {
                $scope.loading.isShow = true;
                var _searchList = [];
                var _tree = [];
                for (var i = 0; i < _list.length; i++) {
                    if (_list[i].deptName.indexOf($scope.department.deptName) >= 0) {
                        _searchList.push(_list[i]);
                    }
                }
                getParentData(_searchList, _list, _tree);
                dataModel.data = [];
                ReqServ.getTreeNode(_tree, dataModel.data, '1001', 'deptName');
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            } else
                loadData();
        };
        $scope.editDepartmentOpened = function () {
            ReqServ.request('POST', 'sys/dept/read/allDept', null).success(function (result) {
                $scope.deptList = [];
                if (result.httpCode != appInterface.successCode)
                    return;
                if ($scope.departmentInfo.id) {
                    for (var i = 0; i < result.data.length; i++) {
                        if (result.data[i].id == $scope.departmentInfo.deptId)
                            $scope.departmentInfo.deptParentName = result.data[i].deptName;
                    }
                }
                ReqServ.getTreeNode(result.data, $scope.deptList, '0', 'deptName');
            }).error(function () {
                $scope.deptList = [];
            });
        };
        $scope.editDepartment = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.departmentInfo);
            $scope.departmentInfo.parentId = $scope.departmentInfo.deptId;
            ReqServ.request('POST', $scope.departmentInfo.id ? 'sys/dept/update' : 'sys/dept/add', $scope.departmentInfo).success(function (data) {
                ReqServ.setBtnStatus(false, $scope.departmentInfo.id ? '保存' : '添加', null, $scope.departmentInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.departmentInfo.id ? '保存' : '添加', null, $scope.departmentInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.clearData();
        loadData();
    }])
});