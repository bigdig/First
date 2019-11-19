'use strict';

    angular.module('app')
        .controller('${table.beanObj}ViewController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
                                             function($scope, $rootScope, $state, $timeout, toaster) {
        var title="查看${table.tableDesc}";
        var id = $state.params.id;
        activate(id);

        $scope.title = title;
        $scope.loading = true;
        //初始化验证

        // 初始化页面
        function activate(id) {
            $scope.loading = true;
            $.ajax({
                url : '/${module}/${table.beanObj}/read/detail',
                data: {'id': id}
            }).then(function(result) {
                $scope.loading = false;
                if (result.httpCode == 200) {
                    $scope.record = result.data;
                } else {
                    $scope.msg = result.msg;
                }
                $scope.$apply();

            });
        }
    }]);