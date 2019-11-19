angular.module('myApp').controller('clientCtrl', function ($scope, $state, $rootScope, storage, req, orgNameList) {
//显示那一部分
    if ($rootScope.clientHeaderClass) {
        $scope.clientInfo = true;
        $scope.typeInfo = false;
    } else if ($rootScope.TwoclientHeaderClass) {
        $scope.clientInfo = false;
        $scope.typeInfo = true;
    }
//开始读取数据
    if(storage.getData().id&&storage.getData().orgId){
        req.getDate("/bizspace/read/custdetail",{"id":storage.getData().id,"token":token}).then(function (result) {
                    if (result.status == 200) {
                        $scope.custDetail = result.data.data;
                        var len=result.data.data.labelCatList;
                        $scope.len=len;
                    }
                }, function (error) {
                });
        req.getDate("/bizspace/read/orgdetail",{"id":storage.getData().orgId,"token":token}).then(function (result) {
            if (result.status == 200) {
                $scope.orgData = result.data.data;
            }
        }, function (error) {
        });
    }else if(storage.getData().id&&!storage.getData().orgId){
        req.getDate("/bizspace/read/orgdetail",{"id":storage.getData().id,"token":token}).then(function (result) {
            if (result.status == 200) {
                $scope.orgData = result.data.data;
            }
        }, function (error) {
        });
    }

    $scope.tab = function (index) {
        if (index == 1) {
            $rootScope.clientHeaderClass = true;
            $rootScope.TwoclientHeaderClass = false;
            $scope.clientInfo = true;
            $scope.typeInfo = false;
        } else if (index == 2) {
            $rootScope.clientHeaderClass = false;
            $rootScope.TwoclientHeaderClass = true;
            $scope.clientInfo = false;
            $scope.typeInfo = true;
        }
    }

    $scope.detail = function (id) {
        var obj = {
            "id": id || '',
            "token": token
        }
        req.getDate("/bizspace/read/custdetail", obj).then(function (result) {
            if (result.status == 200) {
                $scope.custDetail = result.data.data;
            }
        }, function (error) {
        })

    }
    //跳转到所有联系人
    $scope.state = function () {
        var obj = {
            "orgName": $scope.orgData.orgName || '',
            // "id":$scope.orgData.id,
            "token": token
        }
        req.getDate("/bizspace/read/custlist", obj).then(function (result) {
            if (result.status == 200) {
                orgNameList.setData(result);
                $state.go("allContact");
            }
        }, function (error) {
        })

    }
    if ($rootScope.TabHeader) {
        document.getElementById('clientTop').style.top = "49px";
        $scope.clientInfo = true;
        $scope.typeInfo = false;
        var list = document.getElementsByClassName('active');
        for (var i = 0; i < list.length; i++) {
            angular.element(list[i]).removeClass('active');
        }
        angular.element(document.getElementsByClassName('first')[0]).addClass('active')
    } else {
        document.getElementById('clientTop').style.top = "0px";
        $scope.clientInfo = false;
        $scope.typeInfo = true;
    }

})