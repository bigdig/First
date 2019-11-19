define(['app'], function (app) {
    app.controller('portraitTagCtrl', ['$scope', '$anchorScroll','$location','$state', '$timeout', 'ngDialog', 'ReqServ', 'appInterface', function ($scope,$anchorScroll,$location, $state, $timeout, ngDialog, ReqServ, appInterface) {
        $scope.menuLabel = {
            edit: '保存',
            isShow: false,
            hasEditable: false,
            submit: false
        };
        $scope.menuTags = {
            loadingLv1: false,
            loadingLv2: false,
            loadingLv3: false,
            tagsLv1: [],
            tagsLv2: [],
            tagsLv3: [],
            tagsLv11: [],
            tagsLv21: [],
            tagsLv31: [],
            isExpTest: false,
            submit: false,
            btnText: '保存'
        };
        $scope.tagData = {};
        $scope.menuTitle=''
        $scope.selectLabel1=''
        $scope.selectLabel2=''
        $scope.selectLabel3=''
        $scope.selectLabel4=''
        $scope.selectLabel5=''
        $scope.selectLabel=''
        $scope.selectLabelId1=''
        $scope.selectLabelId2=''
        $scope.selectLabelId3=''
        $scope.selectLabelList=[]
        $scope.relationId1=''
        $scope.relationId2=''
        $scope.relationId3=''
        $scope.relationLabalName1=''
        $scope.relationLabalName2=''
        $scope.relationLabalName3=''
        $scope.addLevel=''
        $scope.replaceLevel=''
        $scope.delLevel=''
        $scope.activeTagData=[] 
        $scope.activeTagData1=[]
        $scope.activeTagData2=[] 
        $scope.activeTagData3=[] 
        $scope.relationLabalShow1=false
        $scope.relationLabalShow2=false
        $scope.relationLabalShow3=false
        $scope.relationLabalShow4=false
        $scope.relationLabalShow5=false
        $scope.delShow1=false
        $scope.delShow2=false
        $scope.delShow3=false
        $scope.replaceSelectShow=false
        $scope.addSelectShow=false
        $scope.searchLabelList=[]
        $scope.searchLabelId=''
        $scope.menuList={}
        $scope.batchLabel=[]
        var _ids = [];
        var _sorts = [];
        var _hasEditable, _menu, _totalPageLv1, _totalPageLv2, _totalPageLv3, _tagLv1, _tagLv2, _tagLv3;
        var _pageIndexLv1 = 1, _pageIndexLv2 = 1, _pageIndexLv3 = 1;
        var _height = 220;
        var showToolBar = function (isShow) {
            isShow ? $('.bottom').fadeIn() : $('.bottom').fadeOut();
            _hasEditable = isShow;
        };
        var loadMenu = function () {
            $scope.relationLabalShow1=false
            $scope.relationLabalShow2=false
            $scope.relationLabalShow3=false
            $scope.relationLabalShow4=false
            $scope.relationLabalShow5=false
            $scope.loading.isShow = true;
            ReqServ.request('POST', 'ty/tyLabel/read/list', {
                catId: 0,
                pageSize: appInterface.pageSize * appInterface.multiple
            }).success(function (result) {
                $scope.loading.isShow = false;
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    $scope.tabMenus = [];
                    return;
                }
                result.data.list.forEach(function (value) {
                    value.active = false;
                });
                $scope.tabMenus = result.data.list;
                $scope.tabMenu($scope.tabMenus[0]);
                $scope.menuTitle=$scope.tabMenus[0].labelName
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                $scope.tabMenus = [];
            })
        };
        var loadData = function (level,dataCatId,searchActive) {
            if(dataCatId==undefined){
                var model = {
                    catId: _menu.id,
                    labelLevel: level,
                    pageSize: appInterface.pageSize * appInterface.multiple
                };
                switch (level) {
                    case 1 :
                        model.pageNum = _pageIndexLv1;
                        model.pid = null;
                        $scope.menuTags.loadingLv1 = true;
                        break;
                    case 2:
                        model.pageNum = _pageIndexLv2;
                        model.pid = _tagLv1 ? _tagLv1.id : null;
                        $scope.menuTags.loadingLv2 = true;
                        break;
                    case 3:
                        model.pageNum = _pageIndexLv3;
                        model.pid = _tagLv2 ? _tagLv2.id : (_tagLv1 ? _tagLv1.id : null);
                        $scope.menuTags.loadingLv3 = true;
                        break;
                }
            }else{
                var model = {
                    catId: dataCatId,
                    labelLevel: level,
                    pageSize: appInterface.pageSize * appInterface.multiple,
                    pageNum:1
                };
            }
            $location.hash(dataCatId);
            $anchorScroll();
            ReqServ.request('POST', 'ty/tyLabel/getSubLabel', model).success(function (result) {
                switch (level) {
                    case 1 :
                        $scope.menuTags.loadingLv1 = false;
                        break;
                    case 2:
                        $scope.menuTags.loadingLv2 = false;
                        break;
                    case 3:
                        $scope.menuTags.loadingLv3 = false;
                        break;
                }
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    $scope.menuTags.tagsLv1 = [];
                    switch (level) {
                        case 1 :
                            $scope.menuTags.tagsLv1 = [];
                            break;
                        case 2:
                            $scope.menuTags.tagsLv2 = [];
                            break;
                        case 3:
                            $scope.menuTags.tagsLv3 = [];
                            break;
                    }
                    return;
                }
                var length;
                switch (level) {
                    case 1 :
                        _totalPageLv1 = result.data.pages;
                        length = $scope.menuTags.tagsLv1.length;
                        result.data.list.forEach(function (value, index) {
                            value.active = false;
                            value.hasEditable = false;
                            value.active1 = false;
                            if(searchActive==value.id){
                                value.active1 = true;
                                $location.hash('lv1_'+searchActive);
                                $anchorScroll();
                            }
                            $scope.menuTags.tagsLv1[length + index] = value;
                        });
                        break;
                    case 2:
                        _totalPageLv2 = result.data.pages;
                        length = $scope.menuTags.tagsLv2.length;
                        result.data.list.forEach(function (value, index) {
                            value.active = false;
                            value.hasEditable = false;
                            value.active1 = false;
                            if(searchActive==value.id){
                                value.active1 = true;
                                $location.hash('lv2_'+searchActive);
                                $anchorScroll();
                            }
                            $scope.menuTags.tagsLv2[length + index] = value;
                        });
                        break;
                    case 3:
                        _totalPageLv3 = result.data.pages;
                        length = $scope.menuTags.tagsLv3.length;
                        result.data.list.forEach(function (value, index) {
                            value.active = false;
                            value.active1 = false;
                            value.hasEditable = false;
                            if(searchActive==value.id){
                                value.active1 = true;
                                $location.hash('lv3_'+searchActive);
                                $anchorScroll();
                            }
                            $scope.menuTags.tagsLv3[length + index] = value;
                        });
                        break;
                }
            }).error(function (result) {
                switch (level) {
                    case 1 :
                        $scope.menuTags.loadingLv1 = false;
                        $scope.menuTags.tagsLv1 = [];
                        break;
                    case 2:
                        $scope.menuTags.loadingLv2 = false;
                        $scope.menuTags.tagsLv2 = [];
                        break;
                    case 3:
                        $scope.menuTags.loadingLv3 = false;
                        $scope.menuTags.tagsLv3 = [];
                        break;
                }
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.searchLabel = function (){
            if($('#searchLabelName').val()==''||$('#searchLabelName').val()==undefined){
                return
            }
            ngDialog.open({
                template: 'searchLabalTemplate',
                className: 'ngdialog-theme-plain',
                onOpenCallback: $scope.openLabelCallback,
                scope: $scope
            });
        }
        $scope.openLabelCallback = function (){
            var openAddMenu={
                labelName:$('#searchLabelName').val(),
                pageSize:100
            }
            ReqServ.request('POST', 'ty/tyLabel/searchLabelByName', openAddMenu).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                        $scope.searchLabelList=[]
                }else{
                    $scope.searchLabelList=result.data.list
                    $scope.searchLabelList.forEach(function (value) {
                        if(value.labelLevel==0){
                            value.labelLevelText='类别'
                        }
                        if(value.labelLevel==1){
                            value.labelLevelText='一级标签'
                        }
                        if(value.labelLevel==2){
                            value.labelLevelText='二级标签'
                        }
                        if(value.labelLevel==3){
                            value.labelLevelText='三级标签'
                        }
                    });
                }
            }).error(function (result) {
                $scope.searchLabelList=[]
            });
        }
        $scope.searchLName = function (data){
            ngDialog.close();
            $scope.menuTitle=data.catName
            $scope.selectLabel4=$scope.menuTitle
            $scope.selectLabel5=$scope.menuTitle
            $scope.selectLabel1=''
            $scope.selectLabel2=''
            $scope.selectLabel3=''
            $scope.menuTags.tagsLv1.length =$scope.menuTags.tagsLv2.length =$scope.menuTags.tagsLv3.length =0
            if(data.labelLevel==0){
                $scope.tabMenus.forEach(function (value) {
                    value.active=false
                    if(value.id==data.id){
                        value.active=true
                    }
                });
                loadData(1,data.id);
                loadData(2,data.id);
                loadData(3,data.id);
            }else{
                $scope.tabMenus.forEach(function (value) {
                    value.active=false
                    if(value.id==data.catId){
                        value.active=true
                    }
                });
            }
            if(data.labelLevel==1){
                loadData(1,data.catId,data.id)
                loadData(2,data.catId);
                loadData(3,data.catId);
            }
            if(data.labelLevel==2){
                loadData(2,data.catId,data.id)
                loadData(1,data.catId);
                loadData(3,data.catId);
                
            }
            if(data.labelLevel==3){
                loadData(3,data.catId,data.id)
                loadData(1,data.catId);
                loadData(2,data.catId);    
            } 
            $('.editLv1').addClass('hide');
            $('.editLv2').addClass('hide');
            $('.plusLv2').addClass('hide');
            $('.editLv3').addClass('hide');
            $('.plusLv3').addClass('hide');
            $scope.relationLabalShow1=false
            $scope.relationLabalShow2=false
            $scope.relationLabalShow3=false
            $scope.relationLabalShow4=false
            $scope.relationLabalShow5=false     
        }
        $scope.btnEvent = function () {
            $('.tag-reference a.fr').off('click');
            $('.tag-reference a.fr').on('click', function (e) {
                if ($(this).attr('class').indexOf('plus') >= 0) {
                    var ele = $(this).parent('div');
                    ele.next().width(ele.outerWidth() - 4);
                    ele.next().removeClass('hide');
                    ele.next().find('input').focus();
                }
            });
            $('#lv1').off('scroll');
            $('#lv1').on('scroll', function () {
                if ($(this)[0].scrollTop + $(this).height() >= $(this)[0].scrollHeight) {
                    if ($scope.menuTags.loadingLv1 || _pageIndexLv1 >= _totalPageLv1)
                        return;
                    ++_pageIndexLv1;
                    loadData(1);
                }
            });
            $('#lv2').off('scroll');
            $('#lv2').on('scroll', function () {
                if ($(this)[0].scrollTop + $(this).height() >= $(this)[0].scrollHeight) {
                    if ($scope.menuTags.loadingLv2 || _pageIndexLv2 >= _totalPageLv2)
                        return;
                    ++_pageIndexLv2;
                    loadData(2);
                }
            });
            $('#lv3').off('scroll');
            $('#lv3').on('scroll', function () {
                if ($(this)[0].scrollTop + $(this).height() >= $(this)[0].scrollHeight) {
                    if ($scope.menuTags.loadingLv3 || _pageIndexLv3 >= _totalPageLv3)
                        return;
                    ++_pageIndexLv3;
                    loadData(3);
                }
            });
        };
        $scope.editMenu = function (kind) {
            if (kind == 'update') {
                var text = $('.tag').find("a[contenteditable='true']").eq(0).text();
                for (var i = 0; i < $scope.tabMenus.length; i++) {
                    if ($scope.tabMenus[i].catName == text) {
                        $scope.showAlert('存在同名的类别。');
                        return;
                    }
                }
            } else {
                if (!confirm('删除此菜单将同时删除其下所有标签，以及与标签相关的信息。确定要删除吗？'))
                    return;
            }
            var dataModel = {};
            ReqServ.copyObj(_menu, dataModel);
            dataModel.catName = dataModel.labelName = text;
            $scope.menuLabel.submit = true;
            $scope.menuLabel.edit = '处理中...';
            ReqServ.request('POST', kind == 'del' ? 'ty/tyLabel/delete' : 'ty/tyLabel/update', dataModel).success(function (data) {
                $scope.menuLabel.submit = false;
                $scope.menuLabel.edit = '保存';
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                _menu.catName = _menu.labelName = text;
                $scope.cancelMenu();
                if (kind == 'del') {
                    $scope.cancel();
                    ReqServ.clearObj($scope.tag);
                    showToolBar(false);
                    loadMenu();
                }
            }).error(function (result) {
                $scope.menuLabel.submit = false;
                $scope.menuLabel.edit = '保存';
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.cancelMenu = function () {
            $scope.menuLabel.hasEditable = $scope.menuLabel.isExpTest = false;
            $('.tag').find("a[contenteditable='true']").eq(0).text(_menu.catName);
        };
        $scope.tabMenu = function (menu) {
            $scope.menuList=menu
            $scope.relationLabalShow1=false
            $scope.relationLabalShow2=false
            $scope.relationLabalShow3=false
            $scope.relationLabalShow4=false
            $scope.relationLabalShow5=false
            $scope.menuTitle=menu.catName
            $scope.selectLabel1=''
            $scope.selectLabel2=''
            $scope.selectLabel3=''
            $scope.selectLabel4=$scope.menuTitle
            $scope.selectLabel5=$scope.menuTitle
            if (menu.active)
                return;
            if ($scope.menuLabel.hasEditable)
                return;
            $scope.cancel();
            _menu = menu;
            $scope.tabMenus.forEach(function (value) {
                value.active=false
                if(_menu.id == value.id){
                    menu.active=true;
                }  
            });
            ReqServ.clearObj($scope.tag);
            $scope.menuTags.tagsLv1.length = $scope.menuTags.tagsLv2.length = $scope.menuTags.tagsLv3.length = 0;
            $scope.menuTags.isExpTest = false;
            showToolBar(false);
            $('.plusLv2').addClass('hide');
            $('.plusLv3').addClass('hide');
            $('.editLv1').addClass('hide');
            $('.editLv2').addClass('hide');
            $('.editLv3').addClass('hide');
            _pageIndexLv1 = _pageIndexLv2 = _pageIndexLv3 = 1;
            _tagLv1 = _tagLv2 = _tagLv3 = null;
            loadData(1);
            loadData(2);
            loadData(3);
        };
        $scope.add = function (e, invalid, kind, level) {
            console.log(e, invalid, kind, level)
            var keyCode = window.event ? e.keyCode : e.which;
            var dataModel;
            if (keyCode != 13 || invalid || $scope.menuTags.submit)
                return;

            if(level==1||level==2||level==3){
               if (kind == 'tag') {
                    if (!$scope.tagData.label || !$scope.tagData.label.replace(/(^\s*)|(\s*$)/g, "").length)
                        return;
                    dataModel = {
                        catId: _menu.id,
                        catName: _menu.catName,
                        labelName: $scope.tagData.label,
                        labelLevel: level
                    };
                    switch (level) {
                        case 1:
                            dataModel.pid = 0;
                            break;
                        case 2:
                            dataModel.pid = _tagLv1.id;
                            break;
                        case 3:
                            dataModel.pid = _tagLv2.id;
                            break;
                    }

                } else {
                    if (!$scope.tagData.name || !$scope.tagData.name.replace(/(^\s*)|(\s*$)/g, "").length)
                        return;
                    dataModel = {
                        catId: 0,
                        catName: $scope.tagData.name,
                        labelName: $scope.tagData.name,
                        labelLevel: level,
                        pid: 0
                    };
                }
                $scope.menuTags.submit = true;
                ReqServ.request('POST', 'ty/tyLabel/add', dataModel).success(function (result) {
                    $scope.menuTags.submit = false;
                    if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                        return;
                    }
                    if (kind == 'tag') {
                        var newTag = {
                            id: result.data.id,
                            labelName: result.data.labelName,
                            labelLevel: result.data.labelLevel,
                            active: false
                        };
                        switch (level) {
                            case 1:
                                $scope.menuTags.tagsLv1.splice(0, 0, newTag);
                                break;
                            case 2:
                                $scope.menuTags.tagsLv2.splice(0, 0, newTag);
                                break;
                            case 3:
                                $scope.menuTags.tagsLv3.splice(0, 0, newTag);
                                break;
                        }
                    } else
                        $scope.tabMenus.push({id: result.data.id, catName: result.data.catName, active: false});
                }).error(function (result) {
                    $scope.menuTags.submit = false;
                    $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                });
                $scope.blur($scope.tagData); 
            }else{
               var dataModel1={
                    catId: 0,
                    catName: $scope.tagData.name,
                    labelName: $scope.tagData.name,
                    labelLevel: level,
                    pid: 0
               }
              ReqServ.request('POST', 'ty/tyLabel/add', dataModel1).success(function (result) {
                    $scope.menuTags.submit = false;
                    if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                        return;
                    }
                    if (kind == 'tag') {
                        var newTag = {
                            id: result.data.id,
                            labelName: result.data.labelName,
                            labelLevel: result.data.labelLevel,
                            active: false
                        };
                        switch (level) {
                            case 1:
                                $scope.menuTags.tagsLv1.splice(0, 0, newTag);
                                break;
                            case 2:
                                $scope.menuTags.tagsLv2.splice(0, 0, newTag);
                                break;
                            case 3:
                                $scope.menuTags.tagsLv3.splice(0, 0, newTag);
                                break;
                        }
                    } else
                        $scope.tabMenus.push({id: result.data.id, catName: result.data.catName, active: false});
                        $scope.tagData.name=''
                }).error(function (result) {
                    $scope.menuTags.submit = false;
                    $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                });  
            }   
        };
        $scope.activeTag = function (e, index, tag, level) {
            console.log(tag)
            if(tag.active!=true){
                if($scope.batchLabel.length>0){
                    $scope.batchLabelRe=false
                    $scope.batchLabel.forEach(function (value) {
                        if(value.id==tag.id){
                            $scope.batchLabelRe=true
                        }
                    });
                    if($scope.batchLabelRe!=true){
                        $scope.batchLabel.push(tag)  
                    }
                }else{
                   $scope.batchLabel.push(tag) 
                }     
            }
            console.log($scope.batchLabel)

            $location.hash('');
            $anchorScroll(); 

            $scope.menuTags.tagsLv1.forEach(function (value) {
                value.active1 = false;
            });
            $scope.menuTags.tagsLv2.forEach(function (value) {
                value.active1 = false;
            });
            $scope.menuTags.tagsLv3.forEach(function (value) {
                value.active1 = false;
            });
            if(level==1&&e!=1){
                $scope.selectLabelId1=tag.id
                $scope.activeTagData1=tag
                $scope.relationLabalShow1=true
                $scope.relationLabalShow2=false
                $scope.relationLabalShow3=false
                $scope.relationLabalShow4=false
                $scope.relationLabalShow5=false
                $scope.delShow1=false
                $scope.delShow2=false
                $scope.delShow3=false
                $scope.selectLabel1=tag.labelName
                $scope.selectLabel2=''
                $scope.selectLabel3=''
                $scope.selectLabel4= $scope.selectLabel1
                $scope.selectLabel5= $scope.selectLabel1
            }
            if(level==2&&e!=1){
                $scope.selectLabelId2=tag.id
                $scope.activeTagData2=tag
                $scope.relationLabalShow2=true
                $scope.relationLabalShow3=false
                if($scope.relationLabalShow2==false){
                    $scope.relationLabalShow4=true
                }else{
                    $scope.relationLabalShow4=false
                }
                if($scope.relationLabalShow1==false){
                    $scope.relationLabalShow5=true
                }else{
                    $scope.relationLabalShow5=false
                }
                $scope.delShow2=false
                $scope.delShow3=false
                $scope.selectLabel2=tag.labelName
                $scope.selectLabel3=''
                $scope.selectLabel5= $scope.selectLabel2
            }
            if(level==3&&e!=1){
                $scope.selectLabelId3=tag.id
                $scope.activeTagData3=tag
                $scope.relationLabalShow3=true
                if($scope.relationLabalShow1==false){
                    $scope.relationLabalShow5=true
                }else{
                    $scope.relationLabalShow5=false
                }
                if($scope.relationLabalShow2==false){
                    $scope.relationLabalShow4=true
                }else{
                    $scope.relationLabalShow4=false
                }
                $scope.delShow3=false
                $scope.selectLabel3=tag.labelName
            }
            if(level==1||level==2||level==3){
                $scope.selectLabel=tag.labelName
                var model1 = {
                    labelId: tag.id,
                    pageSize: appInterface.pageSize * appInterface.multiple,
                    pageNum:1,
                    type:0
                };
                ReqServ.request('POST', 'ty/tyLabelnet/read/detail', model1).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    }
                    if(tag.labelLevel==1){
                        $scope.menuTags.tagsLv11=result.data
                    }
                    if(tag.labelLevel==2){
                        $scope.menuTags.tagsLv21=result.data
                    }
                    if(tag.labelLevel==3){
                        $scope.menuTags.tagsLv31=result.data
                    }
                    
                }).error(function (result) {
                    
                });
                if(tag.active==true){
                    switch (tag.labelLevel) {
                       case '1': 
                            $scope.menuTags.tagsLv2.length=0
                            $scope.menuTags.tagsLv3.length=0
                            loadData(2);
                            loadData(3);
                            $scope.menuTags.tagsLv2.forEach(function (value) {
                                value.active = false;
                            });
                            $scope.menuTags.tagsLv3.forEach(function (value) {
                                value.active = false;
                            });
                            $('.editLv2').addClass('hide');
                            $('.editLv3').addClass('hide');
                            $('.plusLv3').addClass('hide');
                            break;
                       case '2': 
                            $scope.menuTags.tagsLv3.length=0
                            loadData(3);
                            $scope.menuTags.tagsLv3.forEach(function (value) {
                                value.active = false;
                            });
                            $('.editLv3').addClass('hide');
                            break;
                    }
                    return
                }
                if (_hasEditable || tag.active)
                    return;
                $('.form').last().addClass('hide');
                switch (tag.labelLevel) {
                    case '1':
                        $scope.menuTags.tagsLv1.forEach(function (value) {
                            value.active = false;
                            value.hasEditable = false;
                        });
                        _pageIndexLv2 = _pageIndexLv3 = 1;
                        $scope.menuTags.tagsLv2.length = $scope.menuTags.tagsLv3.length = 0;
                        $('.plusLv2').removeClass('hide');
                        $('.plusLv3').addClass('hide');
                        $('.editLv1').removeClass('hide');
                        $('.editLv2').addClass('hide');
                        $('.editLv3').addClass('hide');
                        _tagLv1 = tag;
                        _tagLv2 = _tagLv3 = null;
                        loadData(2);
                        loadData(3);
                        $scope.menuTags.tagsLv2.forEach(function (value) {
                            value.active = false;
                        });
                        $scope.menuTags.tagsLv3.forEach(function (value) {
                            value.active = false;
                        });
                        break;
                    case '2':
                        $scope.menuTags.tagsLv2.forEach(function (value) {
                            value.active = false;
                            value.hasEditable = false;
                        });
                        $scope.menuTags.tagsLv3.forEach(function (value) {
                            value.active = false;
                            value.hasEditable = false;
                        });
                        _pageIndexLv3 = 1;
                        $scope.menuTags.tagsLv3.length = 0;
                        $('.plusLv3').removeClass('hide');
                        $('.editLv2').removeClass('hide');
                        $('.editLv3').addClass('hide');
                        _tagLv2 = tag;
                        _tagLv3 = null;
                        loadData(3);
                        break;
                    case '3':
                        $scope.menuTags.tagsLv3.forEach(function (value) {
                            value.active = false;
                            value.hasEditable = false;
                        });
                        $('.editLv3').removeClass('hide');
                        _tagLv3 = tag;
                        break;
                }
                tag.active = true;
                
            }else{
                if(level==11){
                    $scope.relationId1=tag.id
                    $scope.relationLabalName1=tag.labelName
                    $scope.delShow1=true
                    $scope.menuTags.tagsLv11.forEach(function (value){
                        value.active = false;
                    })
                }
                if(level==21){
                    $scope.relationId2=tag.id
                    $scope.relationLabalName2=tag.labelName
                    $scope.delShow2=true
                    $scope.menuTags.tagsLv21.forEach(function (value){
                        value.active = false;
                    })
                }
                if(level==31){
                    $scope.relationId3=tag.id
                    $scope.relationLabalName3=tag.labelName
                    $scope.delShow3=true
                    $scope.menuTags.tagsLv31.forEach(function (value){
                        value.active = false;
                    })
                }
                tag.active=true
            } 
         
        };
        $scope.modify = function (level) {
            console.log(level)
            $scope.modifyLevel=level
            if (_hasEditable)
                return;
            switch (level) {
                case 1 :
                    _tagLv1.hasEditable = true;
                    break;
                case 2:
                    _tagLv2.hasEditable = true;
                    break;
                case 3:
                    _tagLv3.hasEditable = true;
                    break;
            }
            showToolBar(true);
        };
        $scope.openReplace = function (level){
            $scope.replaceLevel=level
            $scope.selectLabelList=[]
            $scope.replaceSelectShow=false
            ngDialog.open({
                template: 'replaceMenuTemplate',
                className: 'ngdialog-theme-plain',
                //onOpenCallback: $scope.openedMenuCallback,
                scope: $scope
            });
        }
        $scope.replaceSelect= function (event){
            var keycode = event.keyCode;
            if(keycode==13){
                var openAddMenu={
                    labelName:$('#searchInput').val(),
                    pageSize:100
                }
                ReqServ.request('POST', 'ty/tyLabel/searchLabelByName', openAddMenu).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            $scope.selectLabelList=[]
                    }else{
                        $scope.replaceSelectShow=true
                        $scope.selectLabelList=result.data.list
                    }
                }).error(function (result) {
                    $scope.selectLabelList=[]
                });
            }  
        }
        $scope.activeLabel=function(data){
            console.log(data,$scope.selectLabelList)
            $scope.selectLabelList.forEach(function (value) {
                value.active=false
                if(value.id==data.id){
                    value.active=true
                }
            })
            $scope.searchLabelId=data.id
        }
        $scope.replaceLabel= function (){
            if($scope.searchLabelId==undefined||$scope.searchLabelId==''){
                $scope.showAlert('请选择移动标签');
                return
            }
            if($scope.replaceLevel==1){
                var moveMenu={
                    id:$scope.selectLabelId1,
                    pid:$scope.searchLabelId
                }
            }
            if($scope.replaceLevel==2){
                var moveMenu={
                    id:$scope.selectLabelId2,
                    pid:$scope.searchLabelId
                }
            }
            if($scope.replaceLevel==3){
                var moveMenu={
                    id:$scope.selectLabelId3,
                    pid:$scope.searchLabelId
                }
            }
            $scope.searchLabelId=''
            ReqServ.request('POST', 'ty/tyLabel/moveLabels', moveMenu).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    ngDialog.close();
                }else{
                    $scope.showAlert('移动成功')
                    ngDialog.close();
                    _tagLv1 = _tagLv2 = _tagLv3 = null;
                    $scope.menuTags.tagsLv1.length = $scope.menuTags.tagsLv2.length = $scope.menuTags.tagsLv3.length = 0;
                    $scope.menuTags.tagsLv11.length = $scope.menuTags.tagsLv21.length = $scope.menuTags.tagsLv31.length = 0;
                    $scope.relationLabalShow1=false
                    $scope.relationLabalShow2=false
                    $scope.relationLabalShow3=false
                    $scope.relationLabalShow4=false
                    $scope.relationLabalShow5=false
                    $scope.selectLabel1=''
                    $scope.selectLabel2=''
                    $scope.selectLabel3=''
                    $scope.selectLabel4= $scope.menuTitle
                    $scope.selectLabel5= $scope.menuTitle
                    loadData(1);
                    loadData(2);
                    loadData(3);
                }               
            }).error(function (result) {
                $scope.showAlert('移动失败')
                ngDialog.close();
            });
        }
        $scope.openDelete = function (level){
            $scope.delLevel=level
            ngDialog.open({
                template: 'delLabalTemplate',
                className: 'ngdialog-theme-plain',
                //onOpenCallback: $scope.openedMenuCallback,
                scope: $scope
            });
        }
        $scope.deleteLabel = function (){
            if($scope.delLevel==1||$scope.delLevel==2||$scope.delLevel==3){
                if($scope.delLevel==1){
                    var delmodel={
                        id:$scope.selectLabelId1
                    }
                }
                if($scope.delLevel==2){
                    var delmodel={
                        id:$scope.selectLabelId2
                    }
                }
                if($scope.delLevel==3){
                    var delmodel={
                        id:$scope.selectLabelId3
                    }
                }
                ReqServ.request('POST', 'ty/tyLabel/delete', delmodel).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                        ngDialog.close();
                    }else{
                        $scope.showAlert('删除成功');
                        ngDialog.close();
                        _tagLv1 = _tagLv2 = _tagLv3 = null;
                        $scope.menuTags.tagsLv1.length = $scope.menuTags.tagsLv2.length = $scope.menuTags.tagsLv3.length = 0;
                        $scope.menuTags.tagsLv11.length = $scope.menuTags.tagsLv21.length = $scope.menuTags.tagsLv31.length = 0;
                        $scope.relationLabalShow1=false
                        $scope.relationLabalShow2=false
                        $scope.relationLabalShow3=false
                        $scope.relationLabalShow4=false
                        $scope.relationLabalShow5=false
                        $scope.selectLabel1=''
                        $scope.selectLabel2=''
                        $scope.selectLabel3=''
                        $scope.selectLabel4= $scope.menuTitle
                        $scope.selectLabel5= $scope.menuTitle
                        loadData(1);
                        loadData(2);
                        loadData(3);
                    }   
                    $scope.selectLabelId=''
                }).error(function (result) {
                    $scope.showAlert('删除失败');
                    $scope.selectLabelId=''
                    ngDialog.close();
                });
            }else{
                if($scope.delLevel==11){
                    if($scope.relationId1==''||$scope.relationId1==undefined){
                        $scope.showAlert('请选择标签');
                        return
                    }
                    $scope.activeTagData=$scope.activeTagData1
                    var delmodel1={
                        id:$scope.relationId1
                    }
                    $scope.relationId1=''
                }
                if($scope.delLevel==21){
                    if($scope.relationId2==''||$scope.relationId2==undefined){
                        $scope.showAlert('请选择标签');
                        return
                    }
                    $scope.activeTagData=$scope.activeTagData2
                    var delmodel1={
                        id:$scope.relationId2
                    }
                    $scope.relationId2=''
                }
                if($scope.delLevel==31){
                    if($scope.relationId3==''||$scope.relationId3==undefined){
                        $scope.showAlert('请选择标签');
                        return
                    }
                    $scope.activeTagData=$scope.activeTagData3
                    var delmodel1={
                        id:$scope.relationId3
                    }
                    $scope.relationId3=''
                }
                ReqServ.request('POST', 'ty/tyLabelnet/delete', delmodel1).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                        ngDialog.close();
                    }else{
                        $scope.showAlert('删除成功');
                        ngDialog.close();
                        $scope.activeTag(1, 2, $scope.activeTagData, ($scope.delLevel+'').slice(0,1))  
                        if($scope.delLevel==11){
                            $scope.delShow1=false
                        }
                        if($scope.delLevel==21){
                            $scope.delShow2=false
                        }
                        if($scope.delLevel==31){
                            $scope.delShow3=false
                        }
                    }   
                    $scope.relationId=''
                }).error(function (result) {
                    $scope.showAlert('删除失败');
                    ngDialog.close();
                    $scope.relationId=''
                });
            }    
        }
        $scope.openAdd= function (level){
            $scope.addLevel=level
            $scope.selectLabelList=[]
            $scope.addSelectShow=false
            if($scope.selectLabel==''){
                $scope.showAlert('请选择标签');
                return
            }
            ngDialog.open({
                template: 'addMenuTemplate',
                className: 'ngdialog-theme-plain',
                //onOpenCallback: $scope.openedMenuCallback,
                scope: $scope
            });
        }
        $scope.addRelSelect= function (event){
            var keycode = event.keyCode;
            if(keycode==13){
                var openAddMenu={
                    labelName:$('#searchInput').val(),
                    pageSize:100
                }
                ReqServ.request('POST', 'ty/tyLabel/searchLabelByName', openAddMenu).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            $scope.selectLabelList=[]
                    }else{
                        $scope.addSelectShow=true
                        if($scope.addLevel==11){
                            for(var i=0;i<result.data.list.length;i++){
                                for(var j=0;j<$scope.menuTags.tagsLv1.length;j++){
                                    if($scope.menuTags.tagsLv1[j]!=undefined&&result.data.list[i]!=undefined){
                                        if(result.data.list[i].labelName==$scope.menuTags.tagsLv1[j].labelName){
                                            result.data.list.splice(i,1);
                                            i--;
                                        }
                                    }
                                    
                                }
                                   
                            }
                            for(var i=0;i<result.data.list.length;i++){
                                for(var k=0;k<$scope.menuTags.tagsLv11.length;k++){
                                    if($scope.menuTags.tagsLv11[k]!=undefined&&result.data.list[i]!=undefined){
                                        if(result.data.list[i].labelName==$scope.menuTags.tagsLv11[k].labelName){
                                            result.data.list.splice(i,1);
                                            i--;
                                        }
                                    }
                                } 
                            }
                            $scope.selectLabelList=result.data.list
                        }else if($scope.addLevel==21){
                            for(var i=0;i<result.data.list.length;i++){
                                for(var j=0;j<$scope.menuTags.tagsLv2.length;j++){
                                    if($scope.menuTags.tagsLv2[j]!=undefined&&result.data.list[i]!=undefined){
                                        if(result.data.list[i].labelName==$scope.menuTags.tagsLv2[j].labelName){
                                            result.data.list.splice(i,1);
                                            i--;
                                        }
                                    }
                                    
                                }
                                   
                            }
                            for(var i=0;i<result.data.list.length;i++){
                                for(var k=0;k<$scope.menuTags.tagsLv21.length;k++){
                                    if($scope.menuTags.tagsLv21[k]!=undefined&&result.data.list[i]!=undefined){
                                        if(result.data.list[i].labelName==$scope.menuTags.tagsLv21[k].labelName){
                                            result.data.list.splice(i,1);
                                            i--;
                                        }
                                    }
                                } 
                            }
                            $scope.selectLabelList=result.data.list
                        }else if($scope.addLevel==31){
                            for(var i=0;i<result.data.list.length;i++){
                                for(var j=0;j<$scope.menuTags.tagsLv3.length;j++){
                                    if($scope.menuTags.tagsLv3[j]!=undefined&&result.data.list[i]!=undefined){
                                        if(result.data.list[i].labelName==$scope.menuTags.tagsLv3[j].labelName){
                                            result.data.list.splice(i,1);
                                            i--;
                                        }
                                    }
                                    
                                }
                                   
                            }
                            for(var i=0;i<result.data.list.length;i++){
                                for(var k=0;k<$scope.menuTags.tagsLv31.length;k++){
                                    if($scope.menuTags.tagsLv31[k]!=undefined&&result.data.list[i]!=undefined){
                                        if(result.data.list[i].labelName==$scope.menuTags.tagsLv31[k].labelName){
                                            result.data.list.splice(i,1);
                                            i--;
                                        }
                                    }
                                } 
                            }
                            $scope.selectLabelList=result.data.list
                        } 
                    }
                }).error(function (result) {
                    $scope.selectLabelList=[]
                });
            }  
        }
        $scope.addRelLabel= function (){
            if($scope.searchLabelId==undefined||$scope.searchLabelId==''){
                $scope.showAlert('请选择关联标签');
                return
            }
            if($scope.addLevel==11){
                $scope.activeTagData=$scope.activeTagData1
                var addMenu={
                    labelIdFrom:$scope.selectLabelId1,
                    labelIdTo:$scope.searchLabelId
                }
            }
            if($scope.addLevel==21){
                $scope.activeTagData=$scope.activeTagData2
                var addMenu={
                    labelIdFrom:$scope.selectLabelId2,
                    labelIdTo:$scope.searchLabelId
                }
            }
            if($scope.addLevel==31){
                $scope.activeTagData=$scope.activeTagData3
                var addMenu={
                    labelIdFrom:$scope.selectLabelId3,
                    labelIdTo:$scope.searchLabelId
                }
            }
            $scope.searchLabelId=''
            ReqServ.request('POST', 'ty/tyLabelnet/add', addMenu).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    ngDialog.close();
                }else{
                    $scope.showAlert('添加成功')
                    ngDialog.close();
                    $scope.activeTag(1, 2, $scope.activeTagData,($scope.addLevel+'').slice(0,1)) 
                }    
            }).error(function (result) {
                $scope.showAlert('添加失败')
                ngDialog.close();
            });
        }
        $scope.edit = function () {
            $scope.menuTags.submit = true;
            $scope.menuTags.btnText = '处理中...';
            if(_tagLv1!=null){
                _tagLv1 && _tagLv1.hasEditable ? _tagLv1.labelName = $('a[contenteditable=true]').text() : null;
            }
            if(_tagLv2!=null){
                _tagLv2 && _tagLv2.hasEditable ? _tagLv2.labelName = $('a[contenteditable=true]').text() : null;
            }
            if(_tagLv3!=null){
               _tagLv3 && _tagLv3.hasEditable ? _tagLv3.labelName = $('a[contenteditable=true]').text() : null;
            }
            if($scope.modifyLevel==1){
                var updateTag=_tagLv1
            }
            if($scope.modifyLevel==2){
                var updateTag=_tagLv2
            }
            if($scope.modifyLevel==3){
                var updateTag=_tagLv3
            }
            $scope.modifyLevel=''
            ReqServ.request('POST', 'ty/tyLabel/update', updateTag).success(function (result) {
                $scope.menuTags.submit = false;
                $scope.menuTags.btnText = '保存';
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                $scope.cancel();
            }).error(function (result) {
                $scope.menuTags.submit = false;
                $scope.menuTags.btnText = '保存';
                $scope.showAlert(result.msg ? result.msg : appInterface.alert);
            });
        };
        $scope.cancel = function () {
            var val;
            _tagLv1 && _tagLv1.hasEditable ? val = _tagLv1.labelName : null;
            _tagLv2 && _tagLv2.hasEditable ? val = _tagLv2.labelName : null;
            _tagLv3 && _tagLv3.hasEditable ? val = _tagLv3.labelName : null;
            $('a[contenteditable=true]').text(val);
            _tagLv1 ? _tagLv1.hasEditable = false : null;
            _tagLv2 ? _tagLv2.hasEditable = false : null;
            _tagLv3 ? _tagLv3.hasEditable = false : null;
            $scope.menuTags.isExpTest = false;
            showToolBar(false);
        };
        $scope.keyup = function (e, kind) {
            var pattern;
            if (kind == 'tag') {
                pattern = /^[^,].*[^,]$/;
                $scope.menuTags.isExpTest = !pattern.test($(e.target).text());
            } else {
                pattern = /^(\w|[\u4e00-\u9fa5]){1,16}$/;
                $scope.menuLabel.isExpTest = !pattern.test($(e.target).text());
            }
        };
        $scope.openedMenuCallback = function () {
            $timeout(function () {
                $('.wrap').DDSort({
                    target: 'li',		// 示例而用，默认即 li，
                    delay: 100,         // 延时处理，默认为 50 ms，防止手抖点击 A 链接无效
                    floatStyle: {
                        'border': '1px solid #ccc',
                        'background-color': '#fff'
                    },
                    up: function () {
                        _ids.length = 0;
                        _sorts.length = 0;
                        $('.tag-List').children('li').each(function (index) {
                            _ids.push($(this).attr('data'));
                            _sorts.push(index);
                        });
                    }
                });
            }, 1000);
        };
        $scope.modifyMenu = function () {
            $scope.toggleMenu();
            $scope.menuLabel.hasEditable = true;
            $timeout(function () {
                $('.tag').find("a[contenteditable='true']").focus();
            }, 300);
        };
        $scope.openSortMenus = function () {
            $scope.toggleMenu();
            ngDialog.open({
                template: 'sortMenuTemplate',
                className: 'ngdialog-theme-plain',
                onOpenCallback: $scope.openedMenuCallback,
                scope: $scope
            });
        };
        $scope.sortMenu = function () {
            $scope.menuTags.submit = true;
            $scope.menuTags.btnText = '处理中...';
            ReqServ.request('POST', 'ty/tyLabel/batchUpdateOrder', {ids: _ids, sorts: _sorts}).success(function (data) {
                $scope.menuTags.submit = false;
                $scope.menuTags.btnText = '确定';
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                $scope.cancelMenu();
                $scope.cancel();
                showToolBar(false);
                loadMenu();
                ngDialog.close();
            }).error(function (result) {
                $scope.menuTags.btnText = '确定';
                $scope.menuTags.submit = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.finish = function () {
            var result = $('.tags');
            //result.height(($(document).height() - $('.menu-head').height() - _height)/3);
            result.css({'padding-top': '5px', 'padding-bottom': '40px','height':'130px'});
        };
        $scope.openBatchReplace = function () {
            $scope.batchLabelList=[]
            $scope.selectLabelList=[]
            $scope.batchLabel.forEach(function (value) {
                value.active2 = false;
                $scope.batchLabelList.push(value.id)
            });
            ngDialog.open({
                template: 'batchReplaceTemplate',
                className: 'ngdialog-theme-default',
                //onOpenCallback: $scope.openedBatchCallback,
                scope: $scope
            });
            console.log($scope.batchLabelList)
        }
        $scope.replaceClearClick= function (){
            $scope.batchLabel=[] 
            $scope.batchLabelList=[]
        }
        $scope.enterReplaceLabel= function (data){
            data.active2=true
        }
        $scope.leaveReplaceLabel= function (data){
            data.active2=false
        }
        $scope.deleteReplaceLabel= function (data){
            $scope.batchLabelList=[]
            $scope.batchLabel.forEach(function (value,index) {
                if(data.id==value.id){
                    $scope.batchLabel.splice(index,1)
                }
            })
            $scope.batchLabel.forEach(function (value) {
                $scope.batchLabelList.push(value.id) 
            })
        }
        $scope.batchReplace = function (){
            if($scope.searchLabelId==undefined||$scope.searchLabelId==''){
                $scope.showAlert('请选择移动标签');
                return
            }
            if($scope.batchLabelList.length==0){
                $scope.showAlert('请选择需要批量移动的标签');
                return
            }
            
            $scope.errorData=[]
            $scope.successCodeData=[]
            $scope.errorData1=[]
            $scope.successCodeData1=[]
            for(var i=0;i<$scope.batchLabelList.length;i++){
                var moveMenu={
                    id:$scope.batchLabelList[i],
                    pid:$scope.searchLabelId
                }
                ReqServ.request('POST', 'ty/tyLabel/moveLabels', moveMenu).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        $scope.successCodeData.push({labelName:$scope.batchLabelList[i].labelName,msg:result.msg ? result.msg : appInterface.alert})
                    }else{

                    }               
                }).error(function (result) {
                    $scope.errorData.push($scope.batchLabelList[i].labelName)  
                });
            }
            $timeout(function(){
                ngDialog.close();
                _tagLv1 = _tagLv2 = _tagLv3 = null;
                $scope.menuTags.tagsLv1.length = $scope.menuTags.tagsLv2.length = $scope.menuTags.tagsLv3.length = 0;
                $scope.menuTags.tagsLv11.length = $scope.menuTags.tagsLv21.length = $scope.menuTags.tagsLv31.length = 0;
                $scope.relationLabalShow1=false
                $scope.relationLabalShow2=false
                $scope.relationLabalShow3=false
                $scope.relationLabalShow4=false
                $scope.relationLabalShow5=false
                $scope.selectLabel1=''
                $scope.selectLabel2=''
                $scope.selectLabel3=''
                $scope.selectLabel4= $scope.menuTitle
                $scope.selectLabel5= $scope.menuTitle
                loadData(1);
                loadData(2);
                loadData(3);
                $scope.batchLabel=[]
                $scope.searchLabelId=''
            },1000)
            if($scope.errorData.length==0&&$scope.successCodeData.length==0){
                $scope.showAlert('批量移动成功')
            }else {
                ngDialog.open({
                    template: 'promptTemplate',
                    className: 'ngdialog-theme-plain',
                    scope: $scope
                });
            }
            
        }
        $scope.openBatchDelete = function () {
            $scope.batchLabelList=[]
            $scope.batchLabel.forEach(function (value) {
                value.active2 = false;
                $scope.batchLabelList.push(value.id)
            });
            ngDialog.open({
                template: 'batchDeleteTemplate',
                className: 'ngdialog-theme-plain',
                //onOpenCallback: $scope.openedMenuCallback,
                scope: $scope
            });
        }
        $scope.batchDelete = function (){
            if($scope.batchLabelList.length==0){
                $scope.showAlert('请选择需要批量删除的标签');
                return
            }
            $scope.errorData=[]
            $scope.successCodeData=[]
            $scope.errorData1=[]
            $scope.successCodeData1=[]
            for(var i=0;i<$scope.batchLabelList.length;i++){
                var delmodel={
                    id:$scope.batchLabelList[i]
                }
                ReqServ.request('POST', 'ty/tyLabel/delete', delmodel).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        $scope.successCodeData1.push({labelName:$scope.batchLabelList[i].labelName,msg:result.msg ? result.msg : appInterface.alert})
                    }else{
                        
                    }   
                }).error(function (result) {
                    $scope.errorData1.push($scope.batchLabelList[i].labelName)  
                });
            }
            $timeout(function(){
                ngDialog.close();
                _tagLv1 = _tagLv2 = _tagLv3 = null;
                $scope.menuTags.tagsLv1.length = $scope.menuTags.tagsLv2.length = $scope.menuTags.tagsLv3.length = 0;
                $scope.menuTags.tagsLv11.length = $scope.menuTags.tagsLv21.length = $scope.menuTags.tagsLv31.length = 0;
                $scope.relationLabalShow1=false
                $scope.relationLabalShow2=false
                $scope.relationLabalShow3=false
                $scope.relationLabalShow4=false
                $scope.relationLabalShow5=false
                $scope.selectLabel1=''
                $scope.selectLabel2=''
                $scope.selectLabel3=''
                $scope.selectLabel4= $scope.menuTitle
                $scope.selectLabel5= $scope.menuTitle
                loadData(1);
                loadData(2);
                loadData(3);
                $scope.batchLabel=[]
            },1000)
            if($scope.errorData.length==0&&$scope.successCodeData.length==0){
                $scope.showAlert('批量删除成功')
            }else {
                ngDialog.open({
                    template: 'promptTemplate',
                    className: 'ngdialog-theme-plain',
                    scope: $scope
                });
            }
        }
        loadMenu();
    }])
});