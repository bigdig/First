var app = angular.module('myApp', ['ionic']);
var token = '';
app.run(['$location',function($location,$rootScope){
    var url=$location.absUrl();
    if(url.split('?').length!==1){
        url=url.split('?')[1];
        var len=url.split('&');
        var obj={};
        for(var i=0;i<len.length;i++){
            var arr=len[i].split('=');
            obj[arr[0]]=arr[1];
        }
        token=obj.token;
    }

}])
app.config(["$stateProvider", "$urlRouterProvider",
    "$ionicConfigProvider", "$httpProvider", "$locationProvider",
    function ($stateProvider, $urlRouterProvider, $ionicConfigProvider, $httpProvider, $locationProvider) {
        $ionicConfigProvider.views.maxCache(0);

        $httpProvider.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8";
        $httpProvider.defaults.transformRequest = function (obj) {
            var str = [];
            for (var p in obj) {
                str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
            }
            return str.join("&");
        }
        //隐藏ion-nav-back-button文字
        $ionicConfigProvider.backButton.text("");
        $ionicConfigProvider.backButton.previousTitleText(false);

        $stateProvider
            .state('main', {
                url: '/main',
                templateUrl: 'src/template/main.html',
                controller: 'mainCtrl'
            })
            .state('client', {
                url: '/client',
                templateUrl: 'src/template/client.html',
                controller: 'clientCtrl'
            })
            .state('allContact', {
                url: '/allContact',
                templateUrl: 'src/template/allContact.html',
                controller: 'allContactCtrl'
            })

        $urlRouterProvider.otherwise('/main');
    }]);
app.service("req", function ($http, $q) {
    this.getDate = function (url, data) {
        var d = $q.defer();
        // document.getElementsByClassName('load')[0].style="block";
        $http.post(url, data).then(function (response) {
            d.resolve(response)
        }, function (error) {
            d.reject("error");
        })
        // document.getElementsByClassName('load')[0].style="none";
        return d.promise;
    }
})
app.factory('storage', function () {
    var service = {};
    var data;
    service.setData = function (newData) {
        data = newData;
    }
    service.getData = function () {
        return data;
    }
    return service;
}).factory('orgNameList', function () {
    var service = {};
    var data;
    service.setData = function (newData) {
        data = newData;
    }
    service.getData = function () {
        return data;
    }
    return service;
})





