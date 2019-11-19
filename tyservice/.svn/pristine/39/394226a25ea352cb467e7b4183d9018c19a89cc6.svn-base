angular.module('myApp').controller('allContactCtrl', function ($scope, $state, $rootScope, storage, orgNameList) {
    $scope.AllContent = function (id,orgId) {
        $rootScope.TabHeader = true;
        $rootScope.clientHeaderClass = true;
        $rootScope.TwoclientHeaderClass = false;
       var obj={
           "id":id,
           "orgId":orgId
       }
        storage.setData(obj);
        $state.go('client');
    }
    if (orgNameList.getData()) {
        $scope.allList = orgNameList.getData().data.data.list;
    }
})