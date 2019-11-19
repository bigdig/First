define(['app'], function (app) {
    app.controller('institutionalCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $state, $stateParams, $timeout, ReqServ, appInterface, ngDialog) {
        $scope.institutional = {};
        $scope.institutionalInfo = {};
        $scope.whiteDeadlineFlags = [{value: 1, text: '7天后到期'}, {value: 2, text: '15天后到期'}];
        $scope.uploadDataModel.value = '单个添加机构信息';
        $scope.uploadDataModel.template = 'editInstitutionalTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加机构信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'ty_serviceorg_temp.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyServiceorg/batchImport';
        $scope.uploadDataModel.scope = $scope;
        $scope.first = '';
        if ($stateParams.flag)
            $scope.institutional.whiteDeadline = $stateParams.flag;
        $scope.clearData();
        var _item;
        var currentName = $state.current.name;
        var isSysManager = ReqServ.isSysManager($scope.userInfo.role);
        var dataModel = {
            toolsBar: [{
                btnName: '添加机构信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.institutionalInfo.loading))
                        return;
                    ReqServ.clearObj($scope.institutionalInfo);
                    $scope.institutionalInfo.pageTitle = '添加机构信息';
                    $scope.institutionalInfo.btnName = '添加';
                    $scope.institutionalInfo.import = isSysManager;
                    ngDialog.open({
                        template: 'editInstitutionalTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editInsOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            controls: [{
                name: '查看', event: function (item) {
                    ReqServ.setStore('institutionalLook', 'true');
                    ReqServ.setStore('currentName', $state.current.name);
                    $state.go('master.institutionaldetail', {id: item.source.id, index: item.index, from: currentName});
                }, isShow: true
            }, {
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.institutionalInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.institutionalInfo);
                    $scope.institutionalInfo.pageTitle = '编辑机构信息';
                    $scope.institutionalInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editInstitutionalTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editInsOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.institutionalInfo.loading) || !window.confirm('确定要删除?'))
                        return;
                    $scope.institutionalInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyServiceorg/delete', {id: item.source.id}).success(function (result) {
                        $scope.institutionalInfo.loading = false;
                        if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchInstitutional();
                    }).error(function (result) {
                        $scope.institutionalInfo.loading = false;
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    });
                }, isShow: true
            }],
            heads: [{title: '机构名称', dataKey: 'orgNameLink', width: 200},
                {title: '机构类型', dataKey: 'custCatName', width: 90},
                {title: '对口销售', dataKey: 'serviceSaler', width: 120},
                {title: '机构级别', dataKey: 'orgLevelName', width: 90},
                {title: '机构状态', dataKey: 'custStatusName', width: 90},
                {title: '白名单到期日', dataKey: 'whiteDeadline', width: 120},
                {title: '区域', dataKey: 'teamCat', width: 120},
                {title: '更新时间', dataKey: 'updateTime', width: 120}],
            isScrollLoaded: true,
            controlWidth: 120
        };
        var loadLocalData = function () {
            $scope.loading.isShow = true;
            $scope.clearData();
            ReqServ.copyObj(JSON.parse(ReqServ.getStore('searchModel')), $scope.searchModel);
            ReqServ.copyObj($scope.searchModel, $scope.institutional);
            $scope.searchLevels = JSON.parse(ReqServ.getStore('searchLevels'));
            $scope.searchStatuss = JSON.parse(ReqServ.getStore('searchStatuss'));
            $scope.searchArea = JSON.parse(ReqServ.getStore('searchArea'));
            $scope.searchCats = JSON.parse(ReqServ.getStore('searchCats'));
            $scope.searchOrgs = JSON.parse(ReqServ.getStore('searchOrgs'));
            $scope.searchStafflist = JSON.parse(ReqServ.getStore('searchStafflist'));
            dataModel.pageInfo = JSON.parse(ReqServ.getStore('pageInfo'));
            dataModel.data = JSON.parse(ReqServ.getStore('data'));
            $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
        };
        var loadData = function () {
            $scope.loading.isShow = true;
            $scope.institutional.pageSize = $scope.searchModel.pageSize;
            if(ReqServ.getStore('institutionalLook')=='true'){
                $scope.look=true
                ReqServ.removeStore('institutionalLook');
                $scope.institutional=ReqServ.getStore('institutional')
            }
            ReqServ.removeStore('institutional');
            ReqServ.setStore('institutional',$scope.institutional)
            if($scope.institutional.custStatus == '3'){
                $scope.institutional.whiteDeadline = ''
            }
            ReqServ.request('POST', 'ty/tyServiceorg/read/list', $scope.institutional).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.searchLevels) {
                    $scope.searchLevels = result.dicts['OrgLevels'];
                    $scope.searchStatuss = result.dicts['CustStatuss'];
                    $scope.searchAreaCode = result.dicts['AreaCode'];
                    $scope.searchArea = result.dicts['SysWorkAreas'];
                    $scope.searchCats = result.dicts['CustCats'];
                    $scope.searchOrgs = result.dicts['Serviceorgs'];
                    $scope.searchStafflist = result.dicts['TyStafflists'];
                    ReqServ.removeStore('searchLevels');
                    ReqServ.setStore('searchLevels', JSON.stringify($scope.searchLevels));
                    ReqServ.removeStore('searchStatuss');
                    ReqServ.setStore('searchStatuss', JSON.stringify($scope.searchStatuss));
                    ReqServ.removeStore('searchArea');
                    ReqServ.setStore('searchArea', JSON.stringify($scope.searchArea));
                    ReqServ.removeStore('searchCats');
                    ReqServ.setStore('searchCats', JSON.stringify($scope.searchCats));
                    ReqServ.removeStore('searchOrgs');
                    ReqServ.setStore('searchOrgs', JSON.stringify($scope.searchOrgs));
                    ReqServ.removeStore('searchStafflist');
                    ReqServ.setStore('searchStafflist', JSON.stringify($scope.searchStafflist));
                }
                result.data.list.forEach(function (value, index) {
                    value.whiteDeadline = value.custStatus=='3'?'-':ReqServ.getDateString(value.whiteDeadline + '');
                    value.orgNameLink = '<a class="text-cyan-light" href="#/master/institutionaldetail?id=' + value.id + '&index=' + index + '&from=' + currentName + '">' + value.orgName + '</a>';
                });
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                if($scope.first==''){
                    if($scope.look==true){
                        $scope.look=false
                        $scope.sub($scope.institutional.pageNum)
                    }else{
                        $scope.sub(1)
                    }
                }
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
                $scope.clearData()
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        function setPage(length, amount, num, first) {//创建保存页码数组的函数
             //length数据总条数
             //amount每页数据条数
             //num保留的页码数
             //first第一页的页码
             var pages = []; //创建分页数组
             var page = Math.ceil(length / amount);
             if (page <= num) {
               for (var i = 1; i <= page; i++) {
                 pages.push(i);
               }
             }
             if (page > num) {
               for (var i = first; i < first + num; i++) {
                 pages.push(i);
               }
             }
             return pages;
        }

        $scope.firstPage = 1;
        $scope.pageNum = 5;
        $scope.page = 1;
        var amount = 100;//数据总条数
        var each = 15;//每页显示的条数
        $scope.sub = function(page) {
           amount=dataModel.pageInfo.total
           each=dataModel.pageInfo.size
           $scope.lastPage = Math.ceil(amount / each);
           if (page >= $scope.pageNum) {
             $scope.firstPage = page - Math.floor($scope.pageNum / 2);
           } else {
             $scope.firstPage = 1;
           }
           if ($scope.firstPage > $scope.lastPage - $scope.pageNum) {
             $scope.firstPage = $scope.lastPage - $scope.pageNum + 1;
           }
           $scope.pages = setPage(amount, each, $scope.pageNum, $scope.firstPage);
           $scope.page = page;
           if($scope.first==1){
                ReqServ.removeStore('data');
                $scope.institutional.pageNum = page;
                $scope.clearData();
                ReqServ.copyObj($scope.institutional, $scope.searchModel);
                loadData();
            }
            $scope.first=1
        }
        // $scope.$on('loadPageData', function (evt, data) {
        //     ReqServ.clearObj($scope.institutional);
        //     ReqServ.copyObj($scope.searchModel, $scope.institutional);
        //     if (data)
        //         ReqServ.removeStore('data');
        //     loadData();
        // });
        $scope.changePageSize= function(data) {
            if(data=='refresh'){
                $scope.clearData();
                loadData();
            }else{
                $scope.institutional.pageNum = 1;
                $scope.clearData();
                ReqServ.copyObj($scope.institutional, $scope.searchModel);
                $scope.first=''
                loadData();
            }
        }
        $scope.searchInstitutional = function () {
            ReqServ.removeStore('data');
            $scope.institutional.pageNum = 1;
            $scope.clearData();
            ReqServ.copyObj($scope.institutional, $scope.searchModel);
            $scope.first=''
            loadData();
        };
        $scope.editInsOpenedCallback = function () {
            $scope.institutionalInfo.recEmail = $scope.institutionalInfo.recEmail == '1';
            $scope.institutionalInfo.recSms = $scope.institutionalInfo.recSms == '1';
            if ($scope.tyStafflists)
                return;
            ReqServ.request('POST', 'ty/tyServiceorg/read/detail', {id: $scope.institutionalInfo.id}).success(function (data) {
                if (data.httpCode != appInterface.successCode) {
                    $scope.tyStafflists = [];
                    return;
                }
                $scope.tyStafflists = data.dicts['TyStafflists'];
            }).error(function () {
                $scope.tyStafflists = [];
            });
        };
        $scope.editInstitutional = function () {
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.institutionalInfo);
            $scope.institutionalInfo.recEmail = $scope.institutionalInfo.recEmail ? 1 : 0;
            $scope.institutionalInfo.recSms = $scope.institutionalInfo.recSms ? 1 : 0;
            $scope.institutionalInfo.whiteDeadline = $scope.institutionalInfo.custStatus == '3' ? null : ReqServ.getDateNum($scope.institutionalInfo.whiteDeadline);
            ReqServ.request('POST', $scope.institutionalInfo.id ? 'ty/tyServiceorg/update' : 'ty/tyServiceorg/add', $scope.institutionalInfo).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.institutionalInfo.id ? '保存' : '添加', null, $scope.institutionalInfo);
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.institutionalInfo.id) {
                    result.data.whiteDeadline = result.data.whiteDeadline ? ReqServ.getDateString(result.data.whiteDeadline + '') : '-';
                    ReqServ.copyObj(result.data, _item.source);
                    ReqServ.getTextValue($scope.searchLevels, _item.source, 'orgLevel', 'orgLevelName');
                    ReqServ.getTextValue($scope.searchStatuss, _item.source, 'custStatus', 'custStatusName');
                    ReqServ.getTextValue($scope.searchCats, _item.source, 'custCat', 'custCatName');
                    _item.item = [{
                        text: '<a class="text-cyan-light" href="#/master/institutionaldetail?id=' + _item.source.id + '&index=' + _item.index + '&from=' + currentName + ')">' + _item.source.orgName + '</a>',
                        width: 200
                    },
                        {text: _item.source.custCatName},
                        {text: _item.source.serviceSaler},
                        {text: _item.source.orgLevelName},
                        {text: _item.source.custStatusName},
                        {text: _item.source.whiteDeadline ? _item.source.whiteDeadline : '-'},
                        {text: _item.source.teamCat},
                        {text: _item.source.updateTime}];
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.institutionalInfo.id ? '保存' : '添加', null, $scope.institutionalInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.prepInstitutional = function () {
            $state.go('master.preinst');
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
        $scope.clearData();
        loadData();
    }])
});
