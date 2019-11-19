define(['app'], function (app) {
    app.controller('systemCtrl', ['$scope', '$rootScope', '$state', function ($scope, $rootScope, $state) {
        $scope.currentName = $state.current.menuName;
        $scope.menuBeans = [];
        var parent = $state.current.name.split('.')[1];
        for (var i = 0; i < $rootScope.menuList.length; i++) {
            if ($rootScope.menuList[i].request.indexOf(parent) >= 0)
                $scope.menuBeans = $rootScope.menuList[i].menuBeans;
        }
        var setMenuBeans = function (url) {
            for (var i = 0; i < $scope.menuBeans.length; i++) {
                $scope.menuBeans[i].isActive = $scope.menuBeans[i].request == url;
            }
        };
        $scope.link = function (menu) {
            if (menu.isActive)
                return;
            setMenuBeans(menu.request);
            $rootScope.isScroll = true;
            $state.go(menu.request);
        };
        setMenuBeans($state.current.name);
    }])
});
