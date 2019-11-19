define(['app'], function (app) {
    app.controller('loginCtrl', ['$scope', '$rootScope', '$state', 'ReqServ', 'appInterface', function ($scope, $rootScope, $state, ReqServ, appInterface) {
        var login_name = '登录';
        $scope.loginInfo = {
            accountFocus: false,
            passwordFocus: false,
            loading: false,
            btnName: login_name,
            adlogin: true
        };
        $scope.login = function () {
            if (!ReqServ.trim($scope.loginInfo.account) || !ReqServ.trim($scope.loginInfo.password)) {
                $scope.loginInfo.message = '请输入账户和密码';
                return;
            }
            ReqServ.setBtnStatus(true, '登录中...', null, $scope.loginInfo);
            ReqServ.request('POST', 'login', $scope.loginInfo).success(function (data) {
                if (data.httpCode != appInterface.successCode) {
                    ReqServ.setBtnStatus(false, login_name, data.msg, $scope.loginInfo);
                    return;
                }
                ReqServ.request('POST', 'sys/user/read/current', null).success(function (data) {
                    if (data.httpCode != appInterface.successCode) {
                        ReqServ.setBtnStatus(false, login_name, data.msg, $scope.loginInfo);
                        ReqServ.removeUser();
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
                        menus: menuList,
                        staff: data.tyStaff ? {
                            tel: data.tyStaff.tel,
                            staffName: data.tyStaff.staffName,
                            deptName: data.tyStaff.deptName,
                            id: data.tyStaff.id
                        } : null
                    });
                    $rootScope.menuList = data.menus;
                    var hasHomeCust = data.menus.filter(function (value) {
                        return value.request == 'master.home.mycust';
                    });
                    $state.go(hasHomeCust.length ? 'master.home.mycust' : 'master.home');
                }).error(function (data) {
                    ReqServ.setBtnStatus(false, login_name, (data && data.msg) ? data.msg : appInterface.alert, $scope.loginInfo);
                });
            }).error(function (data) {
                ReqServ.setBtnStatus(false, login_name, (data && data.msg) ? data.msg : appInterface.alert, $scope.loginInfo);
            });
        };
        $scope.login_focus = function (tag) {
            tag == 'account' ? $scope.loginInfo.accountFocus = true : $scope.loginInfo.passwordFocus = true;
        };
        $scope.login_blur = function (tag) {
            tag == 'account' ? $scope.loginInfo.accountFocus = false : $scope.loginInfo.passwordFocus = false;
        };
    }]);
});