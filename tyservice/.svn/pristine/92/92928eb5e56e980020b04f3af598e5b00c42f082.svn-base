define(['jquery', 'angular', 'asyncLoader', 'ddsort', 'xheditor', 'zh_cn', 'qtip', 'uiCustom', 'jsHash', 'cal', 'uiRouter', 'bindOnce', 'sanitize', 'ngDialog', 'uiUploader', 'ngDatepicker'],
    function ($, angular, asyncLoader) {
        var app = angular.module('app', ['ui.router', 'pasvaz.bindonce', 'ngSanitize', 'ngDialog', 'ui.uploader', 'jkuri.datepicker']);
        app.run(['$state', '$rootScope', 'ReqServ', 'appInterface', function ($state, $rootScope, ReqServ, appInterface) {
            $rootScope.system = {name: appInterface.sysname};
            $rootScope.menuList = ReqServ.getUser() ? ReqServ.getUser().menus : [];
            $rootScope.dataModel = {selectAll: false, data: [], selectedCount: 0};
            $rootScope.welcome = {isHide: false, isBack: false};
            $rootScope._state = $state;
            $rootScope.$on('$stateChangeStart', function (event, toState, toParams, fromState, fromParams) {
                if (toState.isLogin && !ReqServ.getUser()) {
                    event.preventDefault();
                    if (!toParams.from) {
                        $state.go('login');
                        return;
                    }
                    ReqServ.request('POST', 'sys/user/read/current', null).success(function (data) {
                        if (data.httpCode != appInterface.successCode) {
                            $state.go('login');
                            return;
                        }
                        var roleList = [];
                        var menuList = [];
                        for (var i = 0; i < data.userrole.length; i++) {
                            roleList[i] = {roleId: data.userrole[i].roleId};
                        }
                        for (var m = 0; m < data.menus.length; m++) {
                            menuList[m] = {
                                menuName: data.menus[m].menuName,
                                iconcls: data.menus[m].iconcls,
                                request: data.menus[m].request
                            };
                            if (data.menus[m].menuBeans) {
                                menuList[m].menuBeans = [];
                                for (var n = 0; n < data.menus[m].menuBeans.length; n++) {
                                    menuList[m].menuBeans[n] = {
                                        menuName: data.menus[m].menuBeans[n].menuName,
                                        iconcls: data.menus[m].menuBeans[n].iconcls,
                                        request: data.menus[m].menuBeans[n].request
                                    }
                                }
                            }
                        }
                        ReqServ.setUser({
                            id: data.user.id,
                            userName: data.user.userName,
                            account: data.user.account,
                            position: data.tyStaff ? data.tyStaff.position : '',
                            dept: {id: data.dept.id, pic: data.dept.pic},
                            role: roleList,
                            menus: menuList
                        });
                        $rootScope.menuList = data.menus;
                        $state.go(toState.name, toParams);
                    }).error(function (data) {
                        $state.go('login');
                    });
                }
                $rootScope.welcome.isHide = !(toState.name == 'master.system' || toState.name == 'master.datamanager' || toState.name == 'master.resource' || toState.name == 'master.home');
                $rootScope.welcome.isBack = false;
                $rootScope.dataModel.toolsBar = null;
                // if (toState.name.indexOf('portrait') >= 0)
                //     $('.col-76').hide();
                if (toState.name.indexOf('system') >= 0 || toState.name.indexOf('datamanager') >= 0)
                    $('.col-88').hide();
                else
                    $('.col-84').hide();
            });
            $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {
                var currentUrl = toState.name.split('.')[1];
                for (var i = 0; i < $rootScope.menuList.length; i++) {
                    $rootScope.menuList[i].isActive = $rootScope.menuList[i].request.indexOf(currentUrl) >= 0;
                }
                $rootScope.isScroll = false;
            });
            $rootScope.$on('$viewContentLoaded', function (event, viewConfig) {
                // if (viewConfig == 'main@master')
                //     $('.col-84').slideDown();
                // else if (viewConfig == 'portrait-main@master.portrait')
                //     $('.col-76').slideDown();
                if (viewConfig == 'system-main@master.system' || viewConfig == 'datamanager-main@master.datamanager')
                    $('.col-88').show();
                else
                    $('.col-84').show();
                if (viewConfig == 'home-main@master.home')
                    $rootScope.$broadcast('contentLoaded');
            });
        }]);
        asyncLoader.configure(app);
        return app;
    }
);