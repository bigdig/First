define(['app'], function (app) {
    app.directive('pageNav', function () {
        return {
            restrict: 'EA',
            template: '<div><div class="logo"><img src="{{navPic.length>0 ? scope.navPic : \'../img/logo.png\'}}" /><a href="javascript:void(0)" ng-click="logout()" class="logout text-orange font-16 fl"><sub class="icon icon-switch font-20 text-red"></sub> 退出账户</a></div><h1>{{navName}}</h1><h3 class="text-blue-light">{{navCurrentname}}</h3></div>',
            replace: true,
            scope: {
                navName: '@',
                navPic: '@',
                navCurrentname: '@',
                navHide: '@',
                logout: '='
            }
        }
    }).directive('optionSelect', function () {
        return {
            require: '?ngModel',
            link: function (scope, ele, attrs, c) {
                scope.$watch(attrs.ngModel, function (value) {
                    c.$setValidity('select', !!value);
                });
            }
        };
    }).directive('nodeTree', function () {
        return {
            template: '<node ng-repeat="node in tree"></node>',
            replace: true,
            restrict: 'E',
            scope: {
                tree: '=children',
                callback: '=callfunc'
            }
        };
    }).directive('node', function ($compile) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'view/node.html',
            link: function (scope, element) {
                if (scope.node && scope.node.children && scope.node.children.length > 0) {
                    scope.node.childrenVisibility = true;
                    var childNode = $compile('<ul class="tree"><node-tree children="node.children" callfunc="callbackFunc"></node-tree></ul>')(scope);
                    element.append(childNode);
                } else {
                    scope.node.childrenVisibility = false;
                }
            },
            controller: ['$scope', function ($scope) {
                $scope.checkNode = function (node) {
                    node.checked = !node.checked;

                    function checkChildren(c) {
                        angular.forEach(c.children, function (c) {
                            c.checked = node.checked;
                            checkChildren(c);
                        });
                    }

                    checkChildren(node);
                    if ($scope.$parent.callback)
                        $scope.$parent.callback(node);
                };
            }]
        };
    }).directive('grid', function ($compile) {
        return {
            template: '<row ng-repeat="node in data" id="{{node.id}}"></row>',
            replace: true,
            restrict: 'E',
            scope: {
                data: '=children',
                parent: '@'
            },
            link: function (scope, element) {
                var parentNode = $('#' + scope.parent);
                parentNode.append(element);
            }
        };
    }).directive('row', function ($compile) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'view/row.html',
            link: function (scope, element, attrs) {
                var parentNode = $('#' + attrs.id);
                if (scope.node && scope.node.children && scope.node.children.length > 0) {
                    scope.node.childrenVisibility = true;
                    var childNode = $compile('<grid children="node.children" parent="' + attrs.id + '"></grid>')(scope);
                    parentNode.append(element);
                    element.after(childNode);
                } else {
                    scope.node.childrenVisibility = false;
                }
            },
            controller: ['$scope', '$timeout', function ($scope, $timeout) {
                $scope.toggle = function (node) {
                    node.isCollected = !node.isCollected;
                    node.childCollected = false;
                    $scope.loading.scroll = false;

                    function checkChildren(c) {
                        angular.forEach(c.children, function (c) {
                            c.isCollected = c.childCollected = node.isCollected;
                            checkChildren(c);
                        });
                    }

                    checkChildren(node);
                    $scope.copyDataToTable();
                    $timeout(function () {
                        $scope.loading.scroll = true;
                    }, 50);
                };
            }]
        };
    }).directive('deptList', function () {
        return {
            templateUrl: 'view/deptselect.html',
            replace: true,
            restrict: 'E',
            scope: {
                model: '=',
                value: '=',
                name: '@',
                tag: '@',
                required: '@',
                children: '='
            },
            controller: ['$scope', function ($scope) {
                $scope.openSelect = function () {
                    $('.combo').hide();
                    $('#' + $scope.tag).css({'min-width': $('.select').width() + 32 + 'px'}).toggle();
                }
            }]
        };
    }).directive('selects', function () {
        return {
            template: '<nodes ng-repeat="node in children"></nodes>',
            replace: true,
            restrict: 'E',
            scope: {
                children: '='
            }
        };
    }).directive('nodes', function ($compile) {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'view/option.html',
            link: function (scope, element) {
                if (scope.node && scope.node.children && scope.node.children.length > 0) {
                    var childNode = $compile('<ul><selects children="node.children" current="model"></selects></ul>')(scope);
                    element.append(childNode);
                }
            },
            controller: ['$scope', function ($scope) {
                $scope.select = function (node) {
                    this.model[this.name] = node.deptName;
                    this.model.deptId = node.id;
                    $('#' + $scope.tag).css({'min-width': $('.select').width() + 32 + 'px'}).toggle();
                };
            }]
        };
    }).directive('combobox', function () {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'view/combobox.html',
            scope: {
                id: '@',
                no: '@',
                name: '@',
                placeholder: '@',
                multiple: '@',
                model: '=',
                value: '=',
                servicelist: '=',
                callback: '=',
                disabled: '=',
                required: '='
            },
            controller: ['$scope', function ($scope) {
                $scope.list = [];
                var selectsId = [];
                var selectsName = [];
                var setValue = function () {
                    if ($scope.multiple) {
                        if ($scope.model[$scope.no] && $scope.model[$scope.no].length > 0) {
                            selectsId.length = 0;
                            selectsName.length = 0;
                            var selName = $scope.model[$scope.no].split(',');
                            for (var i = 0; i < selName.length; i++) {
                                for (var j = 0; j < $scope.servicelist.length; j++) {
                                    var index = $scope.servicelist[j].text.indexOf('(');
                                    if (selName[i] == (index >= 0 ? $scope.servicelist[j].text.substr(0, index) : $scope.servicelist[j].text)) {
                                        selectsId.push($scope.servicelist[j].id);
                                        selectsName.push(selName[i]);
                                    }
                                }
                            }
                        } else {
                            selectsId.length = 0;
                            selectsName.length = 0;
                            for (var j = 0; j < $scope.servicelist.length; j++) {
                                $scope.servicelist[j].active = false;
                            }
                        }
                    }
                };
                $scope.selectKeyup = function () {
                    setValue();
                    this.list.length = 0;
                    var ele = $('input[name=' + $scope.name + '] + a + div.combo');
                    if (this.model[this.no] && this.model[this.no].length > 0) {
                        ele.show();
                        ele.css({'min-width': $('input[name=' + $scope.name + ']').width() + 40 + 'px'});
                        var select_Name;
                        if ($scope.multiple) {
                            var selName = this.model[this.no].split(',');
                            select_Name = selName[selName.length - 1];
                        } else
                            select_Name = this.model[this.no];
                        for (var i = 0; i < this.servicelist.length; i++) {
                            this.servicelist[i].active = false;
                            if (this.servicelist[i].text.indexOf(select_Name) >= 0)
                                this.list.push(this.servicelist[i]);
                        }
                    } else {
                        if (this.callback)
                            this.callback(null);
                    }
                    if (this.list.length <= 0)
                        ele.hide();
                };
                $scope.selectBlur = function () {
                    if ($scope.multiple) {
                        var select_Name;
                        if (this.model[this.no] && this.model[this.no].length > 0) {
                            var selName = this.model[this.no].split(',');
                            select_Name = selName[selName.length - 1];
                        } else
                            select_Name = this.model[this.no];
                        for (var i = 0; i < this.servicelist.length; i++) {
                            var index = this.servicelist[i].text.indexOf('(');
                            if ((index >= 0 ? this.servicelist[i].text.substr(0, index) : this.servicelist[i].text) == select_Name) {
                                this.onSelect(this.servicelist[i]);
                                if (this.callback)
                                    this.callback(this.servicelist[i]);
                                break;
                            }
                        }
                    } else {
                        if (this.callback)
                            this.callback(null);
                    }
                };
                $scope.openSelect = function () {
                    if ($scope.disabled)
                        return;
                    setValue();
                    this.list.length = 0;
                    if ($scope.multiple) {
                        for (var i = 0; i < this.servicelist.length; i++) {
                            this.servicelist[i].active = false;
                            for (var j = 0; j < selectsId.length; j++) {
                                if (selectsId[j] == this.servicelist[i].id)
                                    this.servicelist[i].active = true;
                            }
                            this.list.push(this.servicelist[i]);
                        }
                    } else {
                        for (var i = 0; i < this.servicelist.length; i++) {
                            this.servicelist[i].active = this.model[this.id] ? this.servicelist[i].id == this.model[this.id] : false;
                            this.list.push(this.servicelist[i]);
                        }
                    }
                    $('.options').hide();
                    var ele = $('input[name=' + $scope.name + ']');
                    ele.parents('li').siblings().find('.combo').hide();
                    if ($scope.servicelist.length > 0)
                        $('input[name=' + $scope.name + '] + a + div.combo').css({'min-width': ele.width() + 40 + 'px'}).toggle();
                };
                $scope.onSelect = function (data) {
                    if ($scope.multiple) {
                        var index = data.text.indexOf('(');
                        for (var i = 0; i < selectsId.length; i++) {
                            if (selectsId[i] == data.id) {
                                selectsName[i] = index >= 0 ? data.text.substr(0, index) : data.text;
                                this.model[this.id] = selectsId.join(',');
                                this.model[this.no] = selectsName.join(',');
                                return;
                            }
                        }
                        selectsId.push(data.id);
                        selectsName.push(index >= 0 ? data.text.substr(0, index) : data.text);
                        this.model[this.id] = selectsId.join(',');
                        this.model[this.no] = selectsName.join(',');
                        data.active = true;
                    } else {
                        this.model[this.id] = data.id;
                        this.model[this.no] = data.text;
                        $('.combo').hide();
                    }
                    if (this.callback)
                        this.callback(data);
                }
            }]
        }
    }).directive('unique', function () {
        return {
            require: '?ngModel',
            link: function (scope, ele, attrs, ctrl) {
                scope.$watch(attrs.ngModel, function () {
                    if (scope.model[scope.no] && scope.model[scope.no].length > 0 && scope.servicelist && scope.servicelist.length > 0) {
                        var invalid = false;
                        if (scope.multiple) {
                            var cout = 0;
                            var selName = scope.model[scope.no].split(',');
                            for (var i = 0; i < scope.servicelist.length; i++) {
                                var index = scope.servicelist[i].text.indexOf('(');
                                for (var j = 0; j < selName.length; j++) {
                                    if ((index >= 0 ? scope.servicelist[i].text.substr(0, index) : scope.servicelist[i].text) == selName[j])
                                        cout++;
                                }
                            }
                            invalid = cout == selName.length;
                        } else {
                            for (var i = 0; i < scope.servicelist.length; i++) {
                                if (scope.servicelist[i].text == scope.model[scope.no]) {
                                    invalid = true;
                                    break;
                                }
                            }
                        }
                        ctrl.$setValidity('unique', invalid)
                    } else
                        ctrl.$setValidity('unique', true);
                });
            }
        }
    }).directive('search', function () {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'view/search.html',
            scope: {
                id: '@name',
                maxlength: '@',
                placeholder: '@',
                required: '@',
                model: '=',
                value: '=',
                servicelist: '=',
                callback: '='
            },
            controller: ['$scope', '$timeout', function ($scope, $timeout) {
                $scope.dataList = [];
                $scope.selectKeyup = function () {
                    var _this = this;
                    _this.dataList.length = 0;
                    if (_this.value && _this.value.length > 0) {
                        if (_this.callback) {
                            _this.callback(function () {
                                if (_this.servicelist.length > 0) {
                                    for (var i = 0; i < _this.servicelist.length; i++) {
                                        _this.dataList.push(_this.servicelist[i]);
                                    }
                                    $('#' + _this.id).css({'min-width': $('.com-bo').width() + 30 + 'px'}).show();
                                }
                                else
                                    $('#' + _this.id).hide();
                            });
                            return;
                        }
                        $('#' + _this.id).css({'min-width': $('.com-bo').width() + 30 + 'px'}).show();
                        for (var i = 0; i < _this.servicelist.length; i++) {
                            if (_this.servicelist[i].text.indexOf(_this.value) >= 0)
                                _this.dataList.push(_this.servicelist[i]);
                        }
                    }
                    if (_this.dataList.length <= 0)
                        $('#' + _this.id).hide();
                };
                $scope.selectBlur = function () {
                    var _this = this;
                    $timeout(function () {
                        $('#' + _this.id).hide();
                    }, 150);
                };
                $scope.onSelect = function (data) {
                    this.model[this.id] = data.text;
                    if (this.callback)
                        this.callback(data);
                }
            }]
        }
    }).directive('repeatFinish', function () {
        return {
            link: function (scope, element, attr) {
                if (scope.$last == true)
                    scope.$eval(attr.repeatFinish);
            }
        }
    })
});
