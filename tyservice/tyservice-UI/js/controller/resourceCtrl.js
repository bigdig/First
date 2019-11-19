define(['app'], function (app) {
    app.controller('resourceCtrl', ['$scope', function ($scope) {
        $scope.tabBar = {isShow: true};
        $scope.tabMenus.length = 0;
        $scope.tabMenus.push({id: 1, name: '上市公司', url: 'master.resource.company', active: false});
        $scope.tabMenus.push({id: 2, name: '专家库', url: 'master.resource.expert', active: false});
        $scope.tabMenus.push({id: 3, name: '项目库', url: 'master.resource.project', active: false});
    }])
});