define(['app'], function (app) {
    app.controller('institutionalDetailCtrl', ['$scope', '$state', '$stateParams', '$timeout', 'ngDialog', 'appInterface', 'ReqServ', function ($scope, $state, $stateParams, $timeout, ngDialog, appInterface, ReqServ) {
        if (!$stateParams.id) {
            $state.go('master.institutional');
            return;
        }
        $scope.welcome.isBack = !!$stateParams.index;
        var loadCustList = function (orgName) {
            ReqServ.request('POST', 'ty/tyOrgcustomer/read/list', {orgName: orgName}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result ? result.msg : appInterface.alert);
                    return;
                }
                $scope.custList = result.data.list;
                if ($scope.custList && $scope.custList.length) {
                    $scope.custList[0].active = true;
                    $state.go('master.institutionaldetail.cust', {custId: $scope.custList[0].id});
                }
            }).error(function (result) {
                $scope.showAlert(result ? result.msg : appInterface.alert);
            });
        };
        var loadData = function () {
            ReqServ.request('POST', 'ty/tyServiceorg/read/detail', {id: $stateParams.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result ? result.msg : appInterface.alert);
                    return;
                }
                $scope.institutionalInfo = result.data;
                if ($scope.institutionalInfo)
                    $scope.institutionalInfo.up = false;
                loadCustList($scope.institutionalInfo.orgName);
            }).error(function (result) {
                $scope.showAlert(result ? result.msg : appInterface.alert);
            });
        };
        $scope.showCust = function (cust) {
            $scope.custList.forEach(function (value) {
                value.active = cust.id == value.id;
            });
            $state.go('master.institutionaldetail.cust', {custId: cust.id});
        };
        $scope.showOrHide = function () {
            $scope.institutionalInfo.up = !$scope.institutionalInfo.up;
        };
        $scope.$on('goBack', function (evt) {
            $state.go($stateParams.from, {index: $stateParams.index});
        });
        $scope.clearData();
        loadData();
    }])
});