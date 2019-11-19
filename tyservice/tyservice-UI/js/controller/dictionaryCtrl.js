define(['app'], function (app) {
    app.controller('dictionaryCtrl', ['$scope', '$state', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $state, ReqServ, ngDialog, appInterface) {
        $scope.dictionary = {};
        $scope.dictionaryInfo = {};
        $scope.childList = [];
        $scope.clearData();
        var _item;
        var currentName = $state.current.name;
        var dataModel = {
            toolsBar: [{
                btnName: '添加字典信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.dictionaryInfo.loading))
                        return;
                    ReqServ.clearObj($scope.dictionaryInfo);
                    $scope.dictionaryInfo.pageTitle = '添加字典信息';
                    $scope.dictionaryInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editDictionaryTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.dictionaryInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.dictionaryInfo);
                    $scope.dictionaryInfo.pageTitle = '编辑字典信息';
                    $scope.dictionaryInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editDictionaryTemplate',
                        className: 'ngdialog-theme-default',
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '添加子项', event: function (item) {
                    ReqServ.copyObj(item.source, $scope.dictionaryInfo);
                    $scope.dictionaryInfo.btnName = '确定';
                    $scope.childList.length = 0;
                    ngDialog.open({
                        template: 'editDictionaryChildTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editChildOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }],
            heads: [{title: '字典名称', dataKey: 'name', width: 120},
                {title: '关键字', dataKey: 'key', width: 150}],
            isScrollLoaded: true,
            controlWidth: 100
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.dictionary.pageSize = $scope.searchModel.pageSize;
            ReqServ.request('POST', 'sys/dicIndex/read/list', $scope.dictionary).success(function (result) {
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
            ReqServ.clearObj($scope.dictionary);
            ReqServ.copyObj($scope.searchModel, $scope.dictionary);
            loadData();
        });
        $scope.searchDictionary = function () {
            $scope.dictionary.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.dictionary, $scope.searchModel);
            loadData();
        };
        $scope.editDictionary = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.dictionaryInfo);
            ReqServ.request('POST', $scope.dictionaryInfo.id ? 'sys/dicIndex/update' : 'sys/dicIndex/add', $scope.dictionaryInfo).success(function (data) {
                ReqServ.setBtnStatus(false, $scope.dictionaryInfo.id ? '保存' : '添加', null, $scope.dictionaryInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                if ($scope.dictionaryInfo.id) {
                    ReqServ.copyObj($scope.dictionaryInfo, _item.source);
                    _item.item = [{text: _item.source.name},
                        {text: _item.source.key}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.dictionaryInfo.id ? '保存' : '添加', null, $scope.dictionaryInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editDictionaryChild = function () {
            var list = [];
            var msg = '请填写编码和名称等必要的信息！';
            for (var i = 0; i < $scope.childList.length; i++) {
                if ($scope.childList[0].id == '' && ($scope.childList[0].code.length <= 0 || $scope.childList[0].codeText.length <= 0)) {
                    $scope.showAlert(msg);
                    return;
                } else if ($scope.childList[i].id.length > 0 && ($scope.childList[i].code.length <= 0 || $scope.childList[i].codeText.length <= 0)) {
                    $scope.showAlert(msg);
                    return;
                } else if ($scope.childList[i].id == '') {
                    if (($scope.childList[i].code.length > 0 && $scope.childList[i].codeText.length <= 0) || ($scope.childList[i].code.length <= 0 && $scope.childList[i].codeText.length > 0)) {
                        $scope.showAlert(msg);
                        return;
                    }
                }
                list[i] = {
                    id: $scope.childList[i].id,
                    code: $scope.childList[i].code,
                    CodeText: $scope.childList[i].codeText
                };
            }
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.dictionaryInfo);
            ReqServ.request('POST', 'sys/dic/update', {
                id: $scope.dictionaryInfo.id,
                data: JSON.stringify(list)
            }).success(function (data) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.dictionaryInfo);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                $scope.editChildOpenedCallback();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.dictionaryInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editChildOpenedCallback = function () {
            ReqServ.request('POST', 'sys/dic/list', {id: $scope.dictionaryInfo.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    if ($scope.childList.length <= 0)
                        $scope.childList.push({id: '', codeText: '', code: ''});
                    return;
                }
                $scope.childList = result.data;
                $scope.childList.push({id: '', codeText: '', code: ''});
            }).error(function () {
                if ($scope.childList.length <= 0)
                    $scope.childList.push({id: '', codeText: '', code: ''});
            })
        };
        $scope.clearData();
        loadData();
    }])
});