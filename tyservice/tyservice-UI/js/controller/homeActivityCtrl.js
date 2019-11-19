define(['app'], function (app) {
    app.controller('homeActivityCtrl', ['$scope', '$timeout', '$state', '$stateParams', 'ReqServ', 'ngDialog', 'appInterface', function ($scope, $timeout, $state, $stateParams, ReqServ, ngDialog, appInterface) {
        $scope.welcome.isHide = true;
        $scope.currentType = undefined;
        $scope.currentType1=undefined
        if (!$scope.tabMenus[3].active)
            $scope.tabMenus[3].active = true;
        var currentName = $state.current.name;
        var _item;
        var datalist = [];
        var datalist1 = [];
        $scope.custList=[]
        $scope.searchModel = {};
        $scope.searchModel.pageSize=15
        $scope.searchModel.activityTypes=[]
        $scope.searchModel1 = {};
        $scope.searchModel1.pageSize=15
        $scope.searchModel1.activityTypes=[]
        $scope.dataModel.heads1=[]
        $scope.first=''
        $scope.first1=''
        var getType = function (name) {
            var type;
            switch (name) {
                case '电话会议' :
                    type = 1;
                    break;
                case '委托课题' :
                    type = 2;
                    break;
                case '调研' :
                    type = 5;
                    break;
                case '路演反路演' :
                    type = 6;
                    break;
                case '沙龙' :
                    type = 7;
                    break;
                case '其他' :
                    type = 0;
                    break;
                case '午/晚餐交流' :
                    type = 8;
                    break;
                case '专项服务' :
                    type = 9;
                    break;
            }
            return type;
        };
        $scope.service=true;
        $scope.customer=false;
        var dataModel = {
            controls: [{
                name: '参与客户', event: function (item) {
                    _item = item;
                    ngDialog.open({
                        template: 'activityCustomerTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.actOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true,isShow1: true
            }],
            isList: true,
            heads: [],
            data: [],
            data1: [],
            isScrollLoaded: true
        };
        $scope.servicebtn =  function(){
            $scope.service=true
            $scope.customer=false
            $('#service').attr("class","tabactive")
            $('#customer').attr("class","")
        }
        $scope.customerbtn =  function(){
            $scope.customer=true
            $scope.service=false
            $('#service').attr("class","")
            $('#customer').attr("class","tabactive")
        }
        var serviceData=function(){
            $scope.loading.isShow = true;
            ReqServ.request('POST', 'ty/tyReport/read/activityList', $scope.searchModel).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!dataModel.heads.length) {
                    var headlast;
                    for (var property in result.dicts) {
                        if (property == '其他') {
                            headlast = {title: property + '(' + result.dicts[property] + ')', type: getType(property)};
                            continue;
                        }
                        dataModel.heads.push({
                            title: property + '(' + result.dicts[property] + ')',
                            type: getType(property)
                        })
                    }
                    if (headlast)
                        dataModel.heads.push(headlast);
                }
                datalist=[]
                $scope.dataModel.data=[]
                result.data.list.forEach(function (value) {
                    for (var property in value) {
                        var attr = (property + '').split(',');
                        datalist.push({id: attr[1], text: value[property], type: getType(attr[0])});
                    }
                });
                dataModel.data = datalist;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                if($scope.first==''){
                   $scope.sub(1) 
                }
                $scope.clearData()
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        }
        var customerData=function(){
            //客户维度
            //ty/tyReport/read/customerActivityList
            //ty/tyActivitysign/read/pagelist
            $scope.loading.isShow1 = true;
            ReqServ.request('POST', 'ty/tyReport/read/customerActivityList', $scope.searchModel1).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow1 = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                //$scope.dataModel.heads1=[{title:1,type:6},{title:2,type:2},{title:3,type:7},{title:4,type:5}]
                if ($scope.dataModel.heads1==[]) {
                    var headlast1;
                    for (var property in result.dicts) {
                        if (property == '其他') {
                            headlast1 = {title: property + '(' + result.dicts[property] + ')', type: getType(property)};
                            continue;
                        }
                        $scope.dataModel.heads1.push({
                            title: property + '(' + result.dicts[property] + ')',
                            type: getType(property)
                        })
                    }
                    if (headlast1)
                        $scope.dataModel.heads1.push(headlast1);
                }
                datalist1=[]
                result.data.list.forEach(function (value) {
                    for (var property in value) {
                        var attr = (property + '').split(',');
                        datalist1.push({id: attr[1], text: value[property], type:getType(attr[0])})
                    }
                });
                $scope.dataModel.data1 = datalist1;
                $scope.dataModel.pageInfo1 = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                if($scope.first1==''){
                   $scope.sub1(1) 
                }
                $scope.loading.isShow1 = $scope.rebuildData(dataModel, currentName);
            }).error(function (result) {
                $scope.loading.isShow1 = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        }
        var loadActivityRecord = function () {
            ReqServ.removeStore('activityTypes')
            ReqServ.removeStore('activityTypes1')
            serviceData()
            customerData()
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
                $scope.searchModel.pageNum = page;
                $scope.searchModel.activityTypes=ReqServ.getStore('activityTypes')
                serviceData()
            }
            $scope.first=1
        }

        $scope.firstPage1 = 1;
        $scope.pageNum1 = 5;
        $scope.page1 = 1;
        var amount1 = 100;//数据总条数
        var each1 = 15;//每页显示的条数
        $scope.sub1 = function(page) {
           amount1=$scope.dataModel.pageInfo1.total
           each1=$scope.dataModel.pageInfo1.size
           $scope.lastPage1 = Math.ceil(amount1 / each1);
           if (page >= $scope.pageNum1) {
             $scope.firstPage1 = page - Math.floor($scope.pageNum1 / 2);
           } else {
             $scope.firstPage1 = 1;
           }
           if ($scope.firstPage1 > $scope.lastPage1 - $scope.pageNum1) {
             $scope.firstPage1 = $scope.lastPage1 - $scope.pageNum1 + 1;
           }
           $scope.pages1 = setPage(amount1, each1, $scope.pageNum1, $scope.firstPage1);
           $scope.page1 = page;
           if($scope.first1==1){
                $scope.searchModel1.pageNum = page;
                $scope.searchModel1.activityTypes=ReqServ.getStore('activityTypes1')
                 customerData()
            }
            $scope.first1=1
        }
        var setChildren = function (dataItem, dataModel) {
            if (dataItem.source.children && dataItem.source.children.length > 0) {
                dataModel.level += 1;
                getAllData(dataItem.source.children, dataModel, dataItem.children);
            } else
                dataItem.children = [];
        };
        var getAllData = function (dataSource, dataModel, obj) {
            var length = obj.length;
            var level = dataModel.level;
            for (var i = 0; i < dataSource.length; i++) {
                var datas = [];
                var controls = [];
                if (dataModel.controls && dataModel.isCopyControls)
                    ReqServ.copyArray(dataModel.controls, controls);
                if (dataModel.isList) {
                    datas.length = 0;
                } else {
                    for (var j = 0; j < dataModel.heads.length; j++) {
                        for (var property in dataSource[i]) {
                            if (dataModel.heads[j].dataKey == property) {
                                var textData = '';
                                if (typeof(dataSource[i][property]) == 'boolean')
                                    textData = dataSource[i][property] ? '是' : '否';
                                else if (typeof(dataSource[i][property]) == 'number')
                                    textData = dataSource[i][property];
                                else
                                    textData = dataSource[i][property] && dataSource[i][property].length ? dataSource[i][property] : '-';
                                datas[j] = {
                                    text: textData,
                                    hide: dataModel.heads[j].hide
                                };
                                for (var c = 0; c < controls.length; c++) {
                                    if (controls[c].locked == property)
                                        controls[c].isShow = !dataSource[i][property] && typeof dataSource[i][property] == 'boolean';
                                    else if (controls[c].unlocked == property)
                                        controls[c].isShow = dataSource[i][property] && typeof dataSource[i][property] == 'boolean';
                                    else if (controls[c].isCheck && controls[c].isCheck.key == property)
                                        controls[c].isShow = controls[c].isCheck.text == dataSource[i][property];
                                    else
                                        controls[c].isShow = true;
                                    if (controls[c].noSpaceLeft && controls[c].noSpaceLeft.key == property)
                                        controls[c].noSpaceLeft = controls[c].noSpaceLeft.text == dataSource[i][property];
                                }
                                break;
                            }
                        }
                    }
                }
                obj[length + i] = {
                    source: dataSource[i],
                    item: datas,
                    index: length + i,
                    controls: dataModel.isCopyControls ? controls : dataModel.controls,
                    hasCheckBox: dataModel.checkFunc ? {
                        isChecked: $scope.dataModel.selectAll || dataSource[i].isChecked,
                        check: dataModel.checkFunc
                    } : false,
                    children: [],
                    level: level,
                    style: {'padding-left': level * 33 + 'px'}
                };
                dataModel.level = level;
                //setChildren(obj[length + i], dataModel);
            }
        };
        // $scope.$on('loadPageData', function (evt) {
        //     ReqServ.clearObj(searchModel);
        //     ReqServ.copyObj($scope.searchModel, searchModel);
        //     loadActivityRecord();
        // });
        $scope.actOpenedCallback = function () {
            $scope.custList.length = 0;
            ReqServ.request('POST', 'ty/tyReport/read/queryActivityCust', {id: _item.source.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                result.data.forEach(function (value) {
                    $scope.custList.push(value);
                })
            }).error(function (result) {
                $scope.showAlert(result.msg ? result.msg : appInterface.alert);
            })
        };
        $scope.chooseAct =  function(type){
            $scope.currentType=type
            $scope.searchModel.activityTypes=[]
            $scope.searchModel.pageNum = 1;
            $scope.loading.isShow = true;
            if(type == '9'){
                $scope.searchModel.activityTypes = [9,10,11,12];
            }else{
                $scope.searchModel.activityTypes.push(type);
            }
            ReqServ.setStore('activityTypes',$scope.searchModel.activityTypes)
            $scope.first=''
            serviceData()                
            // ReqServ.request('POST', 'ty/tyReport/read/activityList', {activityTypes : activityTypes}).success(function (result) {
            //     if (result.httpCode != appInterface.successCode) {
            //         $scope.loading.isShow = false;
            //         $scope.showAlert(result.msg ? result.msg : appInterface.alert);
            //         return;
            //     }
            //     datalist.length = 0;
            //     result.data.list.forEach(function (value) {
            //         for (var property in value) {
            //             var attr = (property + '').split(',');
            //             datalist.push({id: attr[1], text: value[property], type: getType(attr[0])});
            //         }
            //     });
            //     dataModel.data = datalist;
            //     // $scope.dataModel.data = datalist;
            //     $scope.dataModel.data = [];
            //     dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};

            //     $scope.loading.scroll = dataModel.isScrollLoaded;
            //     $scope.dataModel.charts = dataModel.charts;
            //     $scope.dataModel.toolsBar = dataModel.toolsBar;
            //     $scope.dataModel.menu = dataModel.menu;
            //     $scope.dataModel.checkAll = dataModel.checkAll;
            //     $scope.dataModel.hasAllChecked = !!dataModel.checkFunc;
            //     $scope.dataModel.selectedAll = dataModel.selectAll;
            //     $scope.dataModel.hasControl = !!dataModel.controls;
            //     $scope.dataModel.dataHead = dataModel.heads;
            //     $scope.dataModel.pageInfo = dataModel.pageInfo ? dataModel.pageInfo : {pages: 1};
            //     $scope.dataModel.hidePages = dataModel.hidePages;
            //     $scope.dataModel.controlAsMenu = dataModel.controlAsMenu;
            //     $scope.dataModel.controlWidth = dataModel.controlWidth;
            //     if (dataModel.data) {
            //         getAllData(dataModel.data, {
            //             isList: dataModel.isList,
            //             heads: dataModel.heads,
            //             controls: dataModel.controls,
            //             isCopyControls: dataModel.isCopyControls,
            //             checkFunc: dataModel.checkFunc,
            //             level: 0
            //         }, $scope.dataModel.data, false);
            //     }
            //     $scope.loading.isShow = false;
            // }).error(function (result) {
            //     $scope.loading.isShow = false;
            //     $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            // });

            //服务维度
            // ReqServ.request('POST', 'ty/tyActivitysign/read/pagelist', {activityTypes : activityTypes1}).success(function (result) {
            //     if (result.httpCode != appInterface.successCode) {
            //         $scope.loading.isShow1 = false;
            //         $scope.showAlert(result.msg ? result.msg : appInterface.alert);
            //         return;
            //     }
            //     datalist1.length = 0;
            //     result.data.list.forEach(function (value) {
            //         value.createTime=value.createTime.substring(0,10);
            //         if(value.createTime.indexOf('-')!=-1){
            //             value.createTime=value.createTime.replace('-','年')
            //         }
            //         if(value.createTime.lastIndexOf('-')!=-1){
            //             value.createTime=value.createTime.replace('-','月')  
            //         }
            //         value.createTime=value.createTime+'日'
            //         if(value.orgSimpleName==null){
            //             datalist1.push({id:value.id,text:value.createTime+': '+value.custName
            //             +'参与了'+value.staffList[0].deptName+'行业下'+value.staffList[0].staffName+'组织的'+
            //             value.tyActivity.title,type:Math.floor(Math.random()*9+1)})
            //         }else{
            //             datalist1.push({id:value.id,text:value.createTime+': '+value.orgSimpleName+'的'+value.custName
            //             +'参与了'+value.staffList[0].deptName+'行业下'+value.staffList[0].staffName+'组织的'+
            //             value.tyActivity.title,type:Math.floor(Math.random()*9+1)})
            //         }
            //     });

            //     dataModel.data1 = datalist1;
            //     dataModel.pageInfo1 = {size: result.data.size, total: result.data.total, pages: result.data.pages};
            //     $scope.dataModel.data1 = [];
            //     $scope.dataModel.pageInfo1 = dataModel.pageInfo1 ? dataModel.pageInfo1 : {pages: 1};
            //     $scope.dataModel.data1=dataModel.data1
            //     $scope.loading.isShow1 = $scope.rebuildData(dataModel, currentName);
            // }).error(function (result) {
            //     $scope.loading.isShow1 = false;
            //     $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            // });
        };
        $scope.chooseAct1 =  function(type){
            $scope.currentType1=type
            $scope.searchModel1.activityTypes=[]
            $scope.searchModel1.pageNum = 1;
            $scope.loading.isShow1 = true;
            if(type == '9'){
                $scope.searchModel1.activityTypes = [9,10,11,12];
            }else{
                $scope.searchModel1.activityTypes.push(type);
            }
            ReqServ.setStore('activityTypes1',$scope.searchModel1.activityTypes)
            $scope.first1=''
            customerData()
        }
        $scope.clearData();
        loadActivityRecord();
    }])
});