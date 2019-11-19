define(['app'], function (app) {
    app.controller('templateCtrl', ['$scope', '$state', 'ReqServ', 'appInterface', function ($scope, $state, ReqServ, appInterface) {
        $scope.templatForm = {isEditor: true, title: '收起'};
        $scope.templateInfo = {btnName: '创建', sms: false, mail: false};
        $scope.loading.scroll = true;
        $scope.templateList = [];
        var _tempInfo = {};
        var _tempTemplate = {};
        var loadData = function () {
            $scope.loading.isShow = true;
            ReqServ.request('POST', 'ty/tyMsgtemplate/read/list', {
                pageNum: $scope.searchModel.pageNum
            }).success(function (result) {
                $scope.loading.isShow = false;
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                var length = $scope.templateList.length;
                for (var i = 0; i < result.data.list.length; i++) {
                    $scope.templateList[length + i] = {
                        id: result.data.list[i].id,
                        sms: result.data.list[i].sendShortmsg == '1',
                        mail: result.data.list[i].sendMail == '1',
                        disabled: true,
                        showEditor: true,
                        loading: false,
                        disabledButton: false,
                        tmpContent: result.data.list[i].tmpContent,
                        tmpTitle: result.data.list[i].tmpTitle,
                        btnName: '保存'
                    }
                }
                $scope.dataModel.pageInfo = {pages: result.data.pages};
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            })
        };
        var setButtonStatus = function (template, flag) {
            for (var i = 0; i < $scope.templateList.length; i++) {
                if ($scope.templateList[i].id != template.id) {
                    $scope.templateList[i].disabledButton = flag;
                }
            }
        };
        $scope.$on('loadPageData', function (evt) {
            loadData();
        });
        $scope.openEditor = function () {
            if ($scope.templatForm.isEditor) {
                ReqServ.copyObj($scope.templateInfo, _tempInfo);
                $scope.templateInfo.sms = $scope.templateInfo.mail = false;
                $scope.templatForm.title = '展开';
            } else {
                ReqServ.copyObj(_tempInfo, $scope.templateInfo);
                $scope.templatForm.title = '收起';
            }
            $scope.templatForm.isEditor = !$scope.templatForm.isEditor;
        };
        $scope.addTemplate = function () {
            $scope.templateInfo.sendShortmsg = $scope.templateInfo.sms ? '1' : '0';
            $scope.templateInfo.sendMail = $scope.templateInfo.mail ? '1' : '0';
            ReqServ.setBtnStatus(true, '提交中...', null, $scope.templateInfo);
            ReqServ.request('POST', 'ty/tyMsgtemplate/add', $scope.templateInfo).success(function (data) {
                ReqServ.clearObj($scope.templateInfo);
                ReqServ.setBtnStatus(false, '创建', null, $scope.templateInfo);
                $scope.templateInfo.sms = $scope.templateInfo.mail = false;
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                $scope.templateList.length = 0;
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, '创建', null, $scope.templateInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            })
        };
        $scope.showEditor = function (template, flag) {
            if (flag) {
                ReqServ.copyObj(template, _tempTemplate);
                template.showEditor = !template.showEditor;
                template.disabled = !template.disabled;
            } else
                ReqServ.copyObj(_tempTemplate, template);
            setButtonStatus(template, flag);
        };
        $scope.modifyTemplate = function (template) {
            template.sendShortmsg = template.sms ? '1' : '0';
            template.sendMail = template.mail ? '1' : '0';
            ReqServ.setBtnStatus(true, '提交中...', null, template);
            ReqServ.request('POST', 'ty/tyMsgtemplate/update', template).success(function (data) {
                ReqServ.setBtnStatus(false, '保存', null, template);
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                template.showEditor = template.disabled = true;
                setButtonStatus(template, false);
            }).error(function (result) {
                ReqServ.setBtnStatus(false, '保存', null, template);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.deleteTemplate = function (template) {
            if (!confirm('确定要删除吗？'))
                return;
            template.loading = true;
            ReqServ.request('POST', 'ty/tyMsgtemplate/delete', template).success(function (data) {
                template.loading = false;
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                $scope.templateList.length = 0;
                $scope.clearData();
                loadData();
            }).error(function (result) {
                template.loading = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.insertFlag = function (id, text, attr, obj) {
            $('#' + id).insertContent(appInterface.templateFlag + text + appInterface.templateFlag);
            obj[attr] = $('#' + id).val();
        };
        (function ($) {
            $.fn.extend({
                insertContent: function (myValue, t) {
                    var $t = $(this)[0];
                    if (document.selection) { // ie
                        this.focus();
                        var sel = document.selection.createRange();
                        sel.text = myValue;
                        this.focus();
                        sel.moveStart('character', -l);
                        var wee = sel.text.length;
                        if (arguments.length == 2) {
                            var l = $t.value.length;
                            sel.moveEnd("character", wee + t);
                            t <= 0 ? sel.moveStart("character", wee - 2 * t
                                - myValue.length) : sel.moveStart(
                                "character", wee - t - myValue.length);
                            sel.select();
                        }
                    } else if ($t.selectionStart
                        || $t.selectionStart == '0') {
                        var startPos = $t.selectionStart;
                        var endPos = $t.selectionEnd;
                        var scrollTop = $t.scrollTop;
                        $t.value = $t.value.substring(0, startPos)
                            + myValue
                            + $t.value.substring(endPos,
                                $t.value.length);
                        this.focus();
                        $t.selectionStart = startPos + myValue.length;
                        $t.selectionEnd = startPos + myValue.length;
                        $t.scrollTop = scrollTop;
                        if (arguments.length == 2) {
                            $t.setSelectionRange(startPos - t,
                                $t.selectionEnd + t);
                            this.focus();
                        }
                    } else {
                        this.value += myValue;
                        this.focus();
                    }
                }
            })
        })(jQuery);
        $scope.clearData();
        loadData();
    }])
});