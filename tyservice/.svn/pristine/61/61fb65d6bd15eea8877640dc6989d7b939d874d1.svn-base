define(['app'], function (app) {
    app.controller('masterCtrl', ['$scope','$location' ,'$rootScope', '$state', 'ReqServ', '$sce', '$stateParams', '$timeout', 'uiUploader', 'ngDialog', 'appInterface',
        function ($scope,$location, $rootScope, $state, ReqServ, $sce, $stateParams, $timeout, uiUploader, ngDialog, appInterface) {
            var loadDataCount = 0;
            var logout_name = '退出账户';
            var uploadFiles = [];
            var timer;
            $scope.isFromPortrait = {isYes: false};
            $scope.userInfo = ReqServ.getUser();
            $scope.logoutInfo = {isLoading: false, btnName: logout_name};
            $scope.loading = {isShow: false, scroll: false};
            $scope.uploadDataModel = {};
            $scope.alert = {};
            $scope.uploadFile = {files: [], isHide: false, isDisabled: false};
            $scope.selectedCustomers = [];
            $scope.messageInfo = {};
            $scope.pageNo = {pageNum: 1, pages: appInterface.pages};
            $scope.page = {pageNum: 1, pages: appInterface.pages};
            $scope.searchModel = {pageSize: appInterface.pageSize, pageNum: 1};
            $scope.labelList = [];
            $scope.tabMenus = [];

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
                    setChildren(obj[length + i], dataModel);
                }
            };
            
            var setChildren = function (dataItem, dataModel) {
                if (dataItem.source.children && dataItem.source.children.length > 0) {
                    dataModel.level += 1;
                    getAllData(dataItem.source.children, dataModel, dataItem.children);
                } else
                    dataItem.children = [];
            };
            var showMessage = function (message, flag) {
                window.clearTimeout(timer);
                $scope.alert.message = message;
                var dialog = $('.alert');
                switch (flag) {
                    case 'alert' :
                        dialog.removeClass('active bg-success').children('i').removeClass('icon-check-circle');
                        dialog.addClass('active bg-warring').children('i').addClass('icon-warning2');
                        break;
                    case 'success' :
                        dialog.removeClass('active bg-warring').children('i').removeClass('icon-warning2');
                        dialog.addClass('active bg-success').children('i').addClass('icon-check-circle');
                        break;
                }
                timer = window.setTimeout(function () {
                    $scope.alert.message=''
                    message=''
                    dialog.removeClass('active bg-success bg-warring').children('i').removeClass('icon-check-circle icon-warning2');
                    console.log($scope.alert.message,message)
                }, 3000);
            };
            var bytesToSize = function (bytes) {
                if (bytes === 0)
                    return '0 B';
                var k = 1024;
                var sizes = ['B', 'K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y'];
                var i = Math.floor(Math.log(bytes) / Math.log(k));
                return (bytes / Math.pow(k, i)).toPrecision(3) + sizes[i];
            };
            var hasFiles = function (fileName) {
                for (var i = 0; i < uploadFiles.length; i++) {
                    if (uploadFiles[i].key == fileName)
                        return true;
                }
                return false;
            };
            var hasMaxSize = function (files) {
                var bytes = 0;
                var k = 1024;
                var M = 2;
                for (var i = 0; i < files.length; i++) {
                    bytes += files[i].size;
                }
                if (uploadFiles.length > 0) {
                    for (var j = 0; j < uploadFiles.length; j++) {
                        bytes += uploadFiles[j].file.size;
                    }
                }
                var size = Math.floor(Math.log(bytes) / Math.log(k));
                return size > M || (size == M && (bytes / Math.pow(k, size)).toPrecision(3) > appInterface.fileSize);

            };
            var setDataTableWidth = function () {
                $('.data').width($('.panel-head').width());
            };
            $scope.logout = function () {
                ReqServ.setBtnStatus(true, '登出中...', null, $scope.logoutInfo);
                ReqServ.request('POST', 'logout', null).success(function (data) {
                    ReqServ.removeUser();
                    $state.go('login');
                }).error(function () {
                    ReqServ.removeUser();
                    $state.go('login');
                });
            };
            $scope.rebuildData = function (dataModel, stateName) {
                if (stateName != $state.current.name || loadDataCount > 0){
                    return false;
                }
                loadDataCount = stateName == $state.current.name ? 1 : 0;
                $scope.loading.scroll = dataModel.isScrollLoaded;
                $scope.dataModel.charts = dataModel.charts;
                $scope.dataModel.toolsBar = dataModel.toolsBar;
                $scope.dataModel.menu = dataModel.menu;
                $scope.dataModel.checkAll = dataModel.checkAll;
                $scope.dataModel.hasAllChecked = !!dataModel.checkFunc;
                $scope.dataModel.selectedAll = dataModel.selectAll;
                $scope.dataModel.hasControl = !!dataModel.controls;
                $scope.dataModel.dataHead = dataModel.heads;
                $scope.dataModel.pageInfo = dataModel.pageInfo ? dataModel.pageInfo : {pages: 1};
                $scope.dataModel.hidePages = dataModel.hidePages;
                $scope.dataModel.controlAsMenu = dataModel.controlAsMenu;
                $scope.dataModel.controlWidth = dataModel.controlWidth;
                if (dataModel.data) {
                    getAllData(dataModel.data, {
                        isList: dataModel.isList,
                        heads: dataModel.heads,
                        controls: dataModel.controls,
                        isCopyControls: dataModel.isCopyControls,
                        checkFunc: dataModel.checkFunc,
                        level: 0
                    }, $scope.dataModel.data, false);
                }
                return false;
            };
            $scope.selectAll = function () {
                if ($scope.dataModel.selectedAll)
                    $scope.dataModel.selectedAll($scope.dataModel.data, $scope.dataModel.selectAll);
                $scope.disabled($scope.dataModel.selectAll);
            };
            $scope.clearData = function () {
                $(".dataCp").html('');
                ReqServ.clearObj($scope.searchModel);
                $scope.dataModel.data.length = $scope.dataModel.selectedCount = loadDataCount = 0;
                $scope.searchModel.pageNum = 1;
            };
            $scope.uploadOpenedCallback = function () {
                $('.file').on('change', function (e) {
                    var percent;
                    var files = e.target.files;
                    var fileExtenArray = files[0].name.split('.');
                    var fileExten = fileExtenArray[fileExtenArray.length - 1].toLowerCase();
                    if (fileExten != 'xls' && fileExten != 'xlsx') {
                        return;
                    }
                    uiUploader.addFiles(files);
                    $scope.uploadFile.files = uiUploader.getFiles();
                    $scope.uploadFile.isHide = $scope.uploadDataModel.isDisabled = true;
                    $scope.uploadFile.logs = [];
                    $scope.$apply();
                    uiUploader.startUpload({
                        url: $scope.uploadDataModel.uploadUrl,
                        data: {actId: $scope.uploadDataModel.actId ? $scope.uploadDataModel.actId : ''},
                        concurrency: 2,
                        onProgress: function (file) {
                            percent = Math.round((file.loaded / file.size) * 100);
                            file.percentage = (percent > 100 ? 100 : percent) + '%';
                            $scope.$apply();
                        },
                        onUploadSuccess: function (file) {
                            file.humanSize = bytesToSize(file.size);
                            file.percentage = '100%';
                            file.message = '处理中...';
                            $scope.$apply();
                        },
                        onCompleted: function (file, responseText) {
                            $scope.uploadDataModel.isDisabled = false;
                            var result = JSON.parse(responseText);
                            $scope.uploadFile.logs = (result.data && result.data.errorLogList) ? result.data.errorLogList : [];
                            if (result.httpCode != appInterface.successCode) {
                                $scope.uploadFile.isError = true;
                                file.message = '批量添加失败';
                                $scope.$apply();
                                return;
                            }
                            $scope.uploadFile.isError = false;
                            file.message = '处理完成';
                            if (result.data.successNum > 0)
                                $scope.broadcast(result.data.successNum, $scope.uploadDataModel.actId);
                            $scope.$apply();
                        },
                        onError: function (e) {
                            $scope.uploadDataModel.isDisabled = false;
                            $scope.showAlert(e.message ? e.message : '批量添加失败');
                            $scope.$apply();
                        }
                    });
                });
            };
            $scope.uploadPreCloseCallback = function () {
                $('.file').off('change');
                uiUploader.removeAll();
                $scope.uploadFile.files.length = 0;
                $scope.uploadFile.isHide = $scope.uploadFile.isDisabled = false;
            };
            $scope.go = function (menu) {
                if (menu.isActive && menu.menuName == $state.current.menuName)
                    return;
                for (var i = 0; i < $rootScope.menuList.length; i++) {
                    $rootScope.menuList[i].isActive = $rootScope.menuList[i].menuName == menu.menuName;
                }
                $rootScope.isScroll = true;
                $state.go(menu.request, arguments, {reload: true});
            };
            $scope.showSuccess = function (message) {
                showMessage(message, 'success');
            };
            $scope.showAlert = function (message) {
                showMessage(message, 'alert');
            };
            //打开邮件客户端
            // $scope.openAllEmailClient = function (){
            //     var customers = [];
            //     var data = {
            //         msgType: 'email',
            //         title: $scope.messageInfo.title,
            //         isAll: !!$scope.messageInfo.isAll,
            //         groupId: $scope.searchModel.groupId
            //     };
            //     for (var i = 0; i < $scope.selectedCustomers.length; i++) {
            //         customers[i] = $scope.selectedCustomers[i].id;
            //     }
            //     data.custListStr = customers.join(',');
            //     ReqServ.request('POST', 'ty/tyOrgcustomer/getAlllEmailList', data).success(function (result) {
            //         var mto = '';
            //         var emailList = result.data;
            //         for (var i = 0; i < emailList.length; i++) {
            //             mto = mto +  emailList[i] ;
            //             if(i < emailList.length-1){
            //                 mto = mto +';';
            //             }
            //         }
            //         $('#mailList').val(mto);
            //         location="mailto:" + mto;
            //     }).error(function () {
            //
            //     })
            // };
            $scope.openEmailClient = function (emailList) {
                var mto = '';
                for (var i = 0; i < emailList.length; i++) {
                    mto = mto + (emailList[i].custEmail ? emailList[i].custEmail : '');
                    if (i < emailList.length - 1 && emailList[i].custEmail) {
                        mto = mto + ';';
                    }
                }
                $('#mailList').val(mto);
                location = "mailto:" + mto;
            };
            $scope.sendMessage = function (send_type) {
                $('.attachment').find('li').removeClass('cur');
                $('.customer').find('i').removeClass('icon');
                $('.attachment_input').attr('disabled', true);
                var customers = [];
                var url = 'ty/tyOrgcustomer/sendMsg';
                var data = {
                    msgType: send_type == 'sms' ? 'sms' : 'email',
                    title: send_type == 'email' ? $scope.messageInfo.title : null,
                    isAll: !!$scope.messageInfo.isAll,
                    groupId: $scope.searchModel.groupId
                };
                for (var i = 0; i < $scope.selectedCustomers.length; i++) {
                    customers[i] = $scope.selectedCustomers[i].id;
                }
                data.custListStr = customers.join(',');
                if (send_type == 'email') {
                    $scope.messageInfo.content = $('#mail_content').val();
                    if (uploadFiles.length > 0) {
                        for (var m = 0; m < uploadFiles.length; m++) {
                            data[uploadFiles[m].key] = uploadFiles[m].file
                        }
                    }
                }
                data.msgContent = $scope.messageInfo.content;

                function successFunc(data) {
                    ReqServ.setBtnStatus(false, '发送', null, $scope.messageInfo);
                    if (data.httpCode != appInterface.successCode) {
                        $('.attachment').find('li').removeClass('cur');
                        $('.customer').find('i').removeClass('icon');
                        $('.attachment_input').attr('disabled', true);
                        $scope.showAlert(data.msg);
                        if (send_type == 'sms') {
                            editSmsTab.opener = null;
                            editSmsTab.close();
                        }
                        return;
                    }
                    ngDialog.close();
                    $scope.showSuccess((send_type == 'sms' ? '短信' : '邮件') + '发送提交成功，正在审核请耐心等待！');
                    if (send_type == 'sms') {
                        var url = data.data;   //后台传入地址
                        //window.open(url,'_blank');
                        editSmsTab.location.href = url;
                    }
                }

                function errorFunc(result) {
                    ReqServ.setBtnStatus(false, '发送', null, $scope.messageInfo);
                    $('.attachment').find('li').removeClass('cur');
                    $('.customer').find('i').removeClass('icon');
                    $('.attachment_input').attr('disabled', false);
                    $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    editSmsTab.opener = null;
                    editSmsTab.close();
                }

                ReqServ.setBtnStatus(true, '发送中...', null, $scope.messageInfo);
                var editSmsTab;//研究管理平台
                if (send_type == 'email') {
                    ReqServ.upload(url, data).success(successFunc).error(errorFunc);
                } else {
                    editSmsTab = window.open('/smsloading.html');
                    ReqServ.request('POST', url, data).success(successFunc).error(errorFunc);
                }
            };
            $scope.openedEmailCallback = function () {
                $('#mailList').val('加载中...');
                $timeout(function () {
                    if ($scope.dataModel.selectedAll) {
                        var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                        for (var i = 0; i < selectedList.length; i++) {
                            $scope.selectedCustomers.push({
                                id: selectedList[i].id,
                                name: selectedList[i].custName,
                                custEmail: selectedList[i].custEmail
                            });
                        }
                    } else {
                        for (var i = 0; i < $scope.dataModel.data.length; i++) {
                            if ($scope.dataModel.data[i].hasCheckBox.isChecked) {
                                $scope.selectedCustomers.push({
                                    id: $scope.dataModel.data[i].source.id,
                                    name: $scope.dataModel.data[i].source.custName,
                                    custEmail: $scope.dataModel.data[i].source.custEmail,
                                    isChecked: $scope.dataModel.data[i].hasCheckBox.isChecked
                                });
                            }
                        }
                    }
                    $scope.messageInfo.total = $scope.selectedCustomers.length;
                    $scope.openEmailClient($scope.selectedCustomers);
                }, 100);
            };
            $scope.openedCallback = function () {
                $('.attachment_input').off('change');
                $('.attachment_input').on('change', function () {
                    if (this.files.length <= 0)
                        return;
                    if (hasMaxSize(this.files)) {
                        $('#msg').text('附件总容量不超过' + appInterface.fileSize + 'M');
                        return;
                    }
                    $('#msg').text('');
                    for (var i = 0; i < this.files.length; i++) {
                        if (hasFiles(this.files[i].name))
                            continue;
                        uploadFiles.push({key: this.files[i].name, file: this.files[i]});
                        $('.attachment').append('<li class="cur"><small>(' + bytesToSize(this.files[i].size) + ')</small>' + this.files[i].name + '&nbsp;<a href="javascript:void(0)" data-key="' + this.files[i].name + '"><i class="icon icon-cross"></i></a></li>');
                    }
                });
                $('.attachment').off('click', 'a');
                $('.attachment').on('click', 'a', function () {
                    for (var i = 0; i < uploadFiles.length; i++) {
                        if (uploadFiles[i].key == $(this).attr('data-key')) {
                            uploadFiles.splice(i, 1);
                            break;
                        }
                    }
                    $(this).parent().remove();
                    $('.attachment_input').val('');
                    $('#msg').text('');
                });
                // var xheditor = $('#mail_content').xheditor({
                //     tools: 'Cut,Copy,Paste,Pastetext,|,Blocktag,Fontface,FontSize,Bold,Italic,Underline,Strikethrough,FontColor,BackColor,SelectAll,Removeformat,|,Align,List,Outdent,Indent,|,Link,Unlink,Anchor,Hr,Table,|,Source,Preview',
                //     width: '100%',
                //     height: '180px'
                // });
                // if (xheditor)
                //     xheditor.setSource($scope.messageInfo.content);
                // if ($scope.messageInfo.isAll) {
                //     $scope.messageInfo.total = $scope.dataModel.pageInfo.total;
                //
                //     return;
                // }
                $scope.messageInfo.total = '加载中...';
                $timeout(function () {
                    if ($scope.dataModel.selectedAll) {
                        var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                        for (var i = 0; i < selectedList.length; i++) {
                            $scope.selectedCustomers.push({
                                id: selectedList[i].id,
                                name: selectedList[i].custName,
                                custEmail: selectedList[i].custEmail
                            });
                        }
                    } else {
                        for (var i = 0; i < $scope.dataModel.data.length; i++) {
                            if ($scope.dataModel.data[i].hasCheckBox.isChecked) {
                                $scope.selectedCustomers.push({
                                    id: $scope.dataModel.data[i].source.id,
                                    name: $scope.dataModel.data[i].source.custName,
                                    custEmail: $scope.dataModel.data[i].source.custEmail,
                                    isChecked: $scope.dataModel.data[i].hasCheckBox.isChecked
                                });
                            }
                        }
                    }
                    $scope.messageInfo.total = $scope.selectedCustomers.length;
                }, 100);
            };
            $scope.preCloseCallback = function (value) {
                if (value == 'sms' || value == 'mail') {
                    if (value == 'mail')
                        $scope.messageInfo.content = $('#mail_content').val();
                    if ($scope.isFromPortrait.isYes)
                        return;
                    $scope.selectedCustomers.length = 0;
                    return;
                }
                uploadFiles.length = 0;
                $('.attachment_input').off('change');
                $('.attachment').off('click', 'a');
                $('.attachment_input').val('');
                var isAll = !!$scope.messageInfo.isAll;
                ReqServ.clearObj($scope.messageInfo);
                $scope.messageInfo.isAll = isAll;
                $scope.isFromPortrait.isYes = false;
                $scope.selectedCustomers.length = 0;
            };
            $scope.delete = function (customer) {
                for (var i = 0; i < $scope.selectedCustomers.length; i++) {
                    if (customer.id == $scope.selectedCustomers[i].id) {
                        $scope.selectedCustomers.splice(i, 1);
                        break;
                    }
                }
                $scope.messageInfo.total = $scope.selectedCustomers.length;
            };
            $scope.disabled = function (disabled) {
                var isChecked = false;
                if (disabled != null)
                    isChecked = disabled;
                else {
                    if (ReqServ.getStore('selectedItem')) {
                        isChecked = true;
                    } else {
                        for (var i = 0; i < $scope.dataModel.data.length; i++) {
                            if ($scope.dataModel.data[i].hasCheckBox.isChecked) {
                                isChecked = true;
                                break;
                            }
                        }
                    }
                }
                if ($scope.dataModel.toolsBar)
                    for (var j = 0; j < $scope.dataModel.toolsBar.length; j++) {
                        if ($scope.dataModel.toolsBar[j].isDisabled != undefined)
                            $scope.dataModel.toolsBar[j].isDisabled = !isChecked;
                    }
            };
            $scope.setSelectedCount = function (count) {
                $scope.dataModel.selectedCount = count;
            };
            $scope.setSelectAll = function (isAll) {
                $scope.dataModel.selectAll = isAll;
            };
            $scope.isSelectedAll = function () {
                return $scope.dataModel.selectAll;
            };
            $scope.remove = function (index) {
                if ($scope.dataModel.data.length > 0)
                    $scope.dataModel.data.splice(index, 1);
            };
            $scope.searchLabel = function (key, callback) {
                ReqServ.request('POST', 'ty/tyLabel/read/list', {
                    labelName: key,
                    pageSize: 30
                }).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        callback([]);
                        return;
                    }
                    callback(result.data.list);
                }).error(function () {
                    callback([]);
                })
            };
            $scope.searchKey = function (e, obj) {
                e.stopPropagation();
                e.preventDefault();
                var keyCode = window.event ? e.keyCode : e.which;
                if (!obj.label || obj.label.length <= 0) {
                    $scope.closeSearch();
                    return;
                }
                if (keyCode == 13) {
                    if (obj.labels)
                        obj.labels.push(obj.label);
                    obj.label = null;
                    return;
                }
                $scope.searchLabel(obj.label, function (list) {
                    $scope.labelList.length = 0;
                    list.forEach(function (value) {
                        $scope.labelList.push(value);
                    });
                });
            };
            $scope.setLabel = function (label, obj, ele) {
                obj.label = label;
                $scope.closeSearch();
                $("input[name='" + ele + "']").focus();
            };
            $scope.closeSearch = function () {
                $scope.labelList.length = 0;
            };
            $scope.backTop = function () {
                var scrollTop = $(document).scrollTop();
                var i = 1;
                var gotTop = function () {
                    scrollTop -= i;
                    i *= 2;
                    if (scrollTop < 0) {
                        $(document).scrollTop(0);
                        return;
                    } else
                        $(document).scrollTop(scrollTop);
                    $timeout(arguments.callee, 1);
                };
                gotTop();
            };
            $scope.broadcast = function () {
                $scope.dataModel.data.length = loadDataCount = 0;
                $scope.searchModel.pageNum = 1;
                arguments.length ? $scope.$broadcast('loadPageData', arguments) : $scope.$broadcast('loadPageData');
            };
            $scope.hide = function (e) {
                if ($(e.target).hasClass('select') || $(e.target).hasClass('node')) {
                    e.preventDefault();
                    e.stopPropagation();
                    return;
                }
                $('.options').hide();
                $('.combo').hide();
            };
            $scope.back = function () {
                if(ReqServ.getStore('currentName')=='master.home.cust'){
                    $state.go('master.home.cust')
                }else if(ReqServ.getStore('currentName')=='master.customer'){
                    $state.go('master.customer')
                }else if(ReqServ.getStore('currentName')=='master.institutional'){
                    $state.go('master.institutional')
                }else{
                    $scope.$broadcast('goBack');
                }      
            };
            $scope.showTemplateList = function (msgType) {
                ngDialog.close(null, msgType);
                ngDialog.open({
                    template: 'msgTempTemplate',
                    className: 'ngdialog-theme-default',
                    closeByEscape: false,
                    closeByDocument: false,
                    scope: $scope
                });
                $scope.template = msgType == 'sms' ? 'sendMessageTemplate' : 'sendEmailTemplate';
                var data = {pageSize: 100};
                msgType == 'sms' ? data.sendShortmsg = '1' : data.sendMail = '1';
                ReqServ.request('POST', 'ty/tyMsgtemplate/read/list', data).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        $scope.templateList = [];
                        return;
                    }
                    for (var i = 0; i < result.data.list.length; i++) {
                        result.data.list[i].msgType = msgType;
                    }
                    $scope.templateList = result.data.list;
                }).error(function () {
                    $scope.templateList = [];
                });
            };
            $scope.chooseTemplate = function (template) {
                ngDialog.close();
                if (template.msgType == 'sms') {
                    $scope.messageInfo.content = template.tmpContent;
                } else {
                    $scope.messageInfo.title = template.tmpTitle;
                    $scope.messageInfo.content = template.tmpContent;
                }
                $scope.backDialog(template.msgType == 'sms' ? 'sendMessageTemplate' : 'sendEmailTemplate');
            };
            $scope.backDialog = function (template) {
                ngDialog.open({
                    template: template,
                    className: 'ngdialog-theme-default',
                    preCloseCallback: $scope.preCloseCallback,
                    onOpenCallback: $scope.openedCallback,
                    scope: $scope
                });
            };
            $scope.toggleMenu = function () {
                $('#tag_menu').toggle();
                $(document).off('click');
                $(document).on('click', function (e) {
                    if ($(e.target).hasClass('icon-menu')) {
                        e.stopPropagation();
                        e.preventDefault();
                        return;
                    }
                    $('#tag_menu').hide();
                });
            };
            $scope.toggleMenuById = function (id) {
                $('#' + id).toggle();
                $(document).off('click');
                $(document).on('click', function (e) {
                    if ($(e.target).hasClass('icon-menu')) {
                        e.stopPropagation();
                        e.preventDefault();
                        return;
                    }
                    $('#' + id).hide();
                });
            };
            $scope.keydown = function (e) {
                var keyCode = window.event ? e.keyCode : e.which;
                if (keyCode == 13) {
                    e.preventDefault();
                    e.stopPropagation();
                }
            };
            $scope.hasTaskRun = function (flag) {
                if (flag)
                    $scope.showAlert('无法执行，有未完成的服务请求。请稍后再试！');
                return flag;
            };
            $scope.tabMenu = function (menu) {
                if (menu.active)
                    return;
                $scope.tabMenus.forEach(function (value) {
                    value.active = value.id == menu.id;
                });
                $state.go(menu.url);
            };
            $scope.clearFiles = function () {
                $scope.uploadFile.files.length = 0;
            };
            $scope.filesPreCloseCallback = function () {
                $('#fileBtn').off('change');
                $scope.uploadFile.isDisabled = false;
            };
            $scope.filesOpenedCallback = function () {
                $('#fileBtn').on('change', function (e) {
                    var files = e.target.files;
                    uiUploader.addFiles(files);
                    $scope.uploadFile.files = uiUploader.getFiles();
                    $scope.uploadDataModel.isDisabled = true;
                    $scope.$apply();
                    uiUploader.startUpload({
                        url: $scope.uploadDataModel.uploadUrl,
                        concurrency: 4,
                        onProgress: function (file) {
                            $scope.$apply();
                        },
                        onCompleted: function (file, response) {
                            var result = JSON.parse(response);
                            if (result.httpCode != appInterface.successCode) {
                                $scope.showAlert('批量上传失败');
                                $scope.$apply();
                                return;
                            }
                            file.addAttachs = result.data.addAttachs;
                            file.addAttachNames = result.data.addAttachNames;
                            file.humanSize = bytesToSize(file.size);
                            $scope.$apply();
                        },
                        onCompletedAll: function (files) {
                            $scope.uploadDataModel.isDisabled = false;
                            $scope.$apply();
                        },
                        onError: function (e) {
                            $scope.uploadDataModel.isDisabled = false;
                            $scope.showAlert(e.message ? e.message : '批量上传失败');
                            $scope.$apply();
                        }
                    });
                });
            };
            $scope.downloadFile = function (file) {
                window.open('uploadfile/downActFile?fileUrl=' + file.addAttachs + '&fileName=' + file.addAttachNames)
            };
            $scope.fileRemove = function (file) {
                for (var i = 0; i < $scope.uploadFile.files.length; i++) {
                    if ($scope.uploadFile.files[i].addAttachNames == file.addAttachNames) {
                        $scope.uploadFile.files.splice(i, 1);
                        break;
                    }
                }
                $('#fileBtn').val('');
            };
            var translate = function () {
                var translateX = $('.data').width() - $('.data')[0].scrollWidth + $('.data').scrollLeft();
                $('.ctl').css({'transform': 'translateX(' + (translateX >= 0 ? 0 : translateX) + 'px)'});
                if (translateX >= 0)
                    $('.ctl').css({'box-shadow': 'none'});
                else
                    $('.ctl').css({'box-shadow': '-2px 0 5px -2px #cccccc'});
            };
            var translateCtlH = function () {
                if ($('.data')[0]) {
                    var translateX = $('.data').width() - $('.data')[0].scrollWidth + $('.data').scrollLeft();
                    var scrollTop = $(window).scrollTop();
                    var offsetTop = $('.data').offset() ? $('.data').offset().top : 0;
                    var height = $('.dataCp') ? $('.dataCp').outerHeight() : 0;
                    $('.ctl_h').css({'transform': 'translate(' + (translateX >= 0 ? 0 : translateX) + 'px,' + (scrollTop > offsetTop ? (scrollTop - offsetTop + height) : 0) + 'px)'});
                    if (translateX >= 0)
                        $(".ctl_h").css({'box-shadow': 'none'});
                    else
                        $(".ctl_h").css({'box-shadow': '-2px 0 5px -2px #cccccc'});
                }
            };
            var translateTh = function () {
                var scrollTop = $(this).scrollTop();
                var offsetTop = $('.data').offset() ? $('.data').offset().top : 0;
                var height = $('.dataCp') ? $('.dataCp').outerHeight() : 0;
                if (scrollTop > offsetTop) {
                    $('.dataCp').css({'transform': 'translateY(' + (scrollTop - offsetTop + height) + 'px)'});
                    $('.th').css({'transform': 'translateY(' + (scrollTop - offsetTop + height) + 'px)'});
                }
                else {
                    $('.dataCp').css({'transform': 'translateY(0px)'});
                    $('.th').css({'transform': 'translateY(0px)'});
                }
            };
            $scope.copyDataToTable = function () {
                $timeout(function () {
                    setDataTableWidth();
                    $(".dataCp").off('scroll');
                    $(".dataCp").html($('.data').html());
                    $(".dataCp").on('scroll', function () {
                        var scrollLeft = $(this).scrollLeft();
                        $(".data").scrollLeft(scrollLeft);
                        translate();
                        translateCtlH();

                    });
                    translate();
                    translateTh();
                    translateCtlH();
                    var translateX = $('.data').width() - $('.data')[0].scrollWidth + $('.data').scrollLeft();
                    $('.dataCp').css({'max-height': translateX >= 0 ? '0px' : '14px'});
                }, 300);
            };
            $scope.finish = function () {
                $('ul.groups li').off('mouseup');
                $('ul.groups li').on('mouseup', function () {
                    var offsetL = $(this).offset().left + $('ul.groups').scrollLeft() - $('ul.groups').offset().left;
                    var widthP = $('ul.groups').width();
                    var widthC = $(this).outerWidth(true);
                    var sub = offsetL + widthC - widthP;
                    if (offsetL == 0)
                        $('ul.groups').scrollLeft(0);
                    else if ((offsetL + widthC) == widthP)
                        $('ul.groups').scrollLeft(sub);
                    else if (sub > 0)
                        $('ul.groups').scrollLeft(sub + 40);
                    else
                        $('ul.groups').scrollLeft(offsetL - 40);
                })
            };
            $(function () {
                $(window).on('scroll', function () {
                    translateTh();
                    translateCtlH();
                    if ($(document).scrollTop() >= $(this).height())
                        $('.back').fadeIn();
                    else
                        $('.back').fadeOut();
                    if ($(document).scrollTop() + $(this).height() >= $(document).height()) {
                        if ($scope.loading.isShow || !$scope.loading.scroll || $scope.searchModel.pageNum >= $scope.dataModel.pageInfo.pages || $rootScope.isScroll)
                            return;
                        loadDataCount = 0;
                        $scope.searchModel.pageNum += 1;
                        $scope.$broadcast('loadPageData');
                    }
                });
            });
        }
    ])
});