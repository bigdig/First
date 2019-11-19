define(['app'], function (app) {
    app.controller('projectDetailCtrl', ['$scope', '$state', '$stateParams', 'ReqServ', 'appInterface', function ($scope, $state, $stateParams, ReqServ, appInterface) {
        if (!$stateParams.id)
            $state.go('master.resource.project');
        $scope.tabMenus.length = 0;
        $scope.tabMenus.push({id: 1, name: '跟踪', url: 'master.projectdetail.track', active: false});
        $scope.tabMenus.push({id: 2, name: '日志', url: 'master.projectdetail.jour', active: false});
        var loadProjectInfo = function () {
            ReqServ.request('POST', 'ty/tyProject/read/detail', {id: $stateParams.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                $scope.uploadFile.files.length = 0;
                var attachment = result.data.attachment ? result.data.attachment.split(',') : [];
                var attachName = result.data.attachName ? result.data.attachName.split(',') : [];
                for (var i = 0; i < attachment.length; i++) {
                    $scope.uploadFile.files.push({
                        addAttachs: attachment[i],
                        addAttachNames: attachName[i],
                        loaded: true
                    });
                }
                result.data.endTime = ReqServ.getDateString(result.data.endTime + '');
                for (var propert in result.data) {
                    result.data[propert] = result.data[propert] ? result.data[propert] : '-'
                }
                $scope.projectDetail = result.data;
            }).error(function (result) {
                $scope.showAlert(result.msg ? result.msg : appInterface.alert);
            })
        };
        $scope.$on('goBack', function (evt) {
            $scope.loading.isShow = true;
            $state.go('master.resource.project', {index: $stateParams.index});
        });
        loadProjectInfo();
    }])
});