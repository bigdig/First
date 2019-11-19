define(['app'], function (app) {
    app.controller('projectCtrl', ['$scope', '$timeout', '$state', '$stateParams', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $timeout, $state, $stateParams, ReqServ, appInterface, ngDialog) {
        $scope.tabBar.isShow = true;
        $scope.uploadDataModel.uploadUrl = 'uploadfile/activityfile';
        $scope.uploadDataModel.value = '返回';
        $scope.uploadDataModel.template = 'editProjectTemplate';
        $scope.uploadDataModel.scope = $scope;
        $scope.project = {};
        $scope.projectInfo = {required: true};
        $scope.projectNames = [];
        if (!$scope.tabMenus[2].active)
            $scope.tabMenus[2].active = true;
        var _item;
        var currentName = $state.current.name;
        var addFiles = function () {
            $scope.uploadFile.files.length = 0;
            var attachment = $scope.projectInfo.attachment ? $scope.projectInfo.attachment.split(',') : [];
            var attachName = $scope.projectInfo.attachName ? $scope.projectInfo.attachName.split(',') : [];
            for (var i = 0; i < attachment.length; i++) {
                $scope.uploadFile.files.push({addAttachs: attachment[i], addAttachNames: attachName[i], loaded: true});
            }
        };
        var dataModel = {
            toolsBar: [{
                btnName: '添加项目库信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.projectInfo.loading))
                        return;
                    ReqServ.clearObj($scope.projectInfo);
                    if ($scope.userInfo.staff) {
                        $scope.projectInfo.deptName = $scope.userInfo.staff.deptName;
                        $scope.projectInfo.brokerName = $scope.userInfo.staff.staffName;
                    }
                    $scope.projectInfo.createTimeText = ReqServ.getMonthDay(new Date());
                    $scope.projectInfo.pageTitle = '添加项目库信息';
                    $scope.projectInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editProjectTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.clearFiles,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '关联客户', event: function (item) {
                    if ($scope.hasTaskRun($scope.projectInfo.loading))
                        return;
                    $state.go('master.resource.customer', {id: item.source.id, index: item.index});
                }, isShow: true
            }, {
                name: '关联公司', event: function (item) {
                    if ($scope.hasTaskRun($scope.projectInfo.loading))
                        return;
                    $state.go('master.resource.pjcompany', {id: item.source.id, index: item.index});
                }, isShow: true
            }, {
                name: '关联专家', event: function (item) {
                    if ($scope.hasTaskRun($scope.projectInfo.loading))
                        return;
                    $state.go('master.resource.pjexpert', {id: item.source.id, index: item.index});
                }, isShow: true
            }, {
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.projectInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.projectInfo);
                    $scope.projectInfo.createTimeText = ReqServ.getMonthDay(new Date());
                    $scope.projectInfo.endTime = $scope.projectInfo.endTime != '-' ? $scope.projectInfo.endTime : null;
                    $scope.projectInfo.pageTitle = '编辑项目库信息';
                    $scope.projectInfo.btnName = '保存';
                    addFiles();
                    ngDialog.open({
                        template: 'editProjectTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '查看', event: function (item) {
                    $state.go('master.projectdetail.track', {id: item.source.id, index: item.index});
                    // ReqServ.copyObj(item.source, $scope.projectInfo);
                    // addFiles();
                    // ngDialog.open({
                    //     template: 'projectTemplate',
                    //     className: 'ngdialog-theme-plain',
                    //     scope: $scope
                    // });
                }, isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.projectInfo.loading) || !window.confirm('确定要删除吗?'))
                        return;
                    $scope.projectInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyProject/delete', {id: item.source.id}).success(function (result) {
                        $scope.projectInfo.loading = false;
                        if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchProject();
                    }).error(function (result) {
                        $scope.projectInfo.loading = false;
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    });
                }, isShow: true
            }],
            heads: [{title: '项目类型', dataKey: 'projectTypeText', width: 90},
                {title: '项目角色', dataKey: 'projectRoleText', width: 90},
                {title: '项目主体', dataKey: 'projectName', width: 120},
                {title: '需求及进展', dataKey: 'projectDemand', width: 200},
                {title: '提供者', dataKey: 'brokerName', width: 90},
                {title: '所属团队', dataKey: 'deptName', width: 150},
                {title: '创建日期', dataKey: 'createTime', width: 90},
                {title: '截止日期', dataKey: 'endTime', width: 90},
                {title: '更新日期', dataKey: 'updateTime', width: 90},
                {title: '关联专家', dataKey: 'expertStaffName', width: 150},
                {title: '关联客户', dataKey: 'orgCustName', width: 150},
                {title: '关联公司', dataKey: 'companyName', width: 200},
                {title: '附件', dataKey: 'attachmentFlag', width: 70}],
            isScrollLoaded: true,
            controlWidth: 310
        };
        var loadLocalData = function () {
            $scope.loading.isShow = true;
            $scope.clearData();
            ReqServ.copyObj(JSON.parse(ReqServ.getStore('searchModel')), $scope.searchModel);
            ReqServ.copyObj($scope.searchModel, $scope.project);
            $scope.projectTypes = JSON.parse(ReqServ.getStore('PROJECTTYPE'));
            $scope.projectRoles = JSON.parse(ReqServ.getStore('EXPERT_PROJECTROLE'));
            dataModel.pageInfo = JSON.parse(ReqServ.getStore('pageInfo'));
            dataModel.data = JSON.parse(ReqServ.getStore('data'));
            ReqServ.request('POST', 'ty/tyProject/read/detail', {id: $stateParams.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                for (var i = 0; i < dataModel.data.length; i++) {
                    if (dataModel.data[i].id == $stateParams.id) {
                        dataModel.data[i] = result.data;
                        break;
                    }
                }
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
                ReqServ.removeStore('data');
                ReqServ.setStore('data', JSON.stringify(dataModel.data));
                $scope.disabled();

            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert(result.msg ? result.msg : appInterface.alert);
            });
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.project.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'ty/tyProject/read/list', $scope.project).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.projectType) {
                    $scope.projectType = result.dicts['PROJECTTYPE'];
                    $scope.projectTypes = result.dicts['PROJECTTYPE'].concat();
                    $scope.projectRole = result.dicts['EXPERT_PROJECTROLE'];
                    $scope.projectRoles = result.dicts['EXPERT_PROJECTROLE'].concat();
                    ReqServ.removeStore('PROJECTTYPE');
                    ReqServ.setStore('PROJECTTYPE', JSON.stringify($scope.projectTypes));
                    ReqServ.removeStore('EXPERT_PROJECTROLE');
                    ReqServ.setStore('EXPERT_PROJECTROLE', JSON.stringify($scope.projectRoles));
                }
                result.data.list.forEach(function (value) {
                    value.endTime = value.endTime ? ReqServ.getDateString(value.endTime + '') : '-';
                    value.brokerName = value.deleteFlag == '1' ? '<span class="text-gray">' + value.brokerName + '</span>' : value.brokerName;
                    value.attachmentFlag = value.attachmentFlag == '1' ? '有' : '无';
                });
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                ReqServ.removeStore('searchModel');
                ReqServ.setStore('searchModel', JSON.stringify($scope.searchModel));
                ReqServ.removeStore('pageInfo');
                ReqServ.setStore('pageInfo', JSON.stringify(dataModel.pageInfo));
                if (ReqServ.getStore('data')) {
                    var _data = JSON.parse(ReqServ.getStore('data'));
                    ReqServ.removeStore('data');
                    ReqServ.setStore('data', JSON.stringify(_data.concat(dataModel.data)));
                } else
                    ReqServ.setStore('data', JSON.stringify(dataModel.data));
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.$on('loadPageData', function (evt) {
            ReqServ.clearObj($scope.project);
            ReqServ.copyObj($scope.searchModel, $scope.project);
            loadData();
        });
        $scope.searchProject = function () {
            ReqServ.removeStore('data');
            $scope.project.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.project, $scope.searchModel);
            loadData();
        };
        $scope.editProject = function () {
            var attachment = [];
            var attachName = [];
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.projectInfo);
            $scope.uploadFile.files.forEach(function (value) {
                attachment.push(value.addAttachs);
                attachName.push(value.addAttachNames);
            });
            $scope.projectInfo.endTime = ReqServ.getDateNum($scope.projectInfo.endTime);
            $scope.projectInfo.attachment = attachment.join(',');
            $scope.projectInfo.attachName = attachName.join(',');
            if (attachment.length > 0)
                $scope.projectInfo.attachmentFlag = '有';
            ReqServ.request('POST', $scope.projectInfo.id ? 'ty/tyProject/update' : 'ty/tyProject/add', $scope.projectInfo).success(function (data) {
                ReqServ.setBtnStatus(false, $scope.projectInfo.id ? '保存' : '添加', null, $scope.projectInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                if ($scope.projectInfo.id) {
                    var i;
                    for (i = 0; $scope.projectTypes.length; i++) {
                        if ($scope.projectTypes[i].id == $scope.projectInfo.projectType) {
                            $scope.projectInfo.projectTypeText = $scope.projectTypes[i].text;
                            break;
                        }
                    }
                    for (i = 0; $scope.projectRoles.length; i++) {
                        if ($scope.projectRoles[i].id == $scope.projectInfo.projectRole) {
                            $scope.projectInfo.projectRoleText = $scope.projectRoles[i].text;
                            break;
                        }
                    }
                    $scope.projectInfo.endTime = ReqServ.getDateString($scope.projectInfo.endTime);
                    ReqServ.copyObj($scope.projectInfo, _item.source);
                    _item.item = [{text: _item.source.projectTypeText},
                        {text: _item.source.projectRoleText},
                        {text: _item.source.projectName ? _item.source.projectName : '-'},
                        {text: _item.source.projectDemand},
                        {text: _item.source.brokerName},
                        {text: _item.source.deptName},
                        {text: _item.source.createTime},
                        {text: _item.source.endTime ? _item.source.endTime : '-'},
                        {text: _item.source.updateTime},
                        {text: _item.source.expertStaffName ? _item.source.expertStaffName : '-'},
                        {text: _item.source.orgCustName ? _item.source.orgCustName : '-'},
                        {text: _item.source.companyName ? _item.source.companyName : '-'},
                        {text: _item.source.attachmentFlag ? _item.source.attachmentFlag : '-'}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.projectInfo.id ? '保存' : '添加', null, $scope.projectInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        if ($stateParams.index) {
            loadLocalData();
            $timeout(function () {
                var tr = $('.data').find('tr').eq(parseInt($stateParams.index) + appInterface.add);
                tr.addClass('active');
                $(document).scrollTop(tr.position().top - appInterface.height);
            }, 500);
            return;
        }
        ReqServ.removeStore('data');
        ReqServ.removeStore('selectedItem');
        $scope.clearData();
        loadData();
    }])
});