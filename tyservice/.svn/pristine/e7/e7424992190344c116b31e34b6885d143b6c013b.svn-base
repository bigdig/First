define(['app'], function (app) {
    app.controller('headCtrl', ['$scope', '$rootScope', '$state', function ($scope, $rootScope, $state) {
        $scope.router = {showSubTitle: !($state.current.name.indexOf('master.home') >= 0)};
        var pageName = $state.current.name.split('.');
        if (pageName.length > 2) {
            for (var i = 0; i < $rootScope.menuList.length; i++) {
                if (pageName[0] + '.' + pageName[1] == $rootScope.menuList[i].request) {
                    $scope.router.previous = $rootScope.menuList[i];
                    break;
                }
            }
            $scope.router.current = $state.current;
        } else {
            $scope.router.previous = $state.current;
            $scope.router.current = null;
        }
    }])
});
