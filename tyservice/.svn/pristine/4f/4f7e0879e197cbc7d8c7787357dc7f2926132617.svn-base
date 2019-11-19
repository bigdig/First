define(['app'], function (app) {
    app.controller('portraitCtrl', ['$scope', '$state', 'ReqServ', '$timeout', 'appInterface', function ($scope, $state, ReqServ, $timeout, appInterface) {
        $scope.currentName = $state.current.menuName;
        // $scope.search = {more: false};
        // $scope.searchList = {zero: [], one: [], two: [], three: []};
        // $scope.tagList = {zero: [], one: [], two: [], three: []};
        // $scope.isSystemManager = ReqServ.isSysManager($scope.userInfo.role);
        // var setSearchToolbarHeight = function () {
        //     $('.search-Result').css('max-height', ($('.col-24').height() - 340) + 'px');
        // };
        $scope.focus = function (tag) {
            $('.tag li').last().removeClass('hide');
            $('.search li').last().removeClass('hide');
            $("input[name='" + tag + "']").focus();
        };
        $scope.blur = function (dataModel) {
            $('.tag li').last().addClass('hide');
            $('.search li').last().addClass('hide');
            $('.form').addClass('hide');
            ReqServ.clearObj(dataModel);
        };
        // $scope.showMore = function () {
        //     $scope.search.more = !$scope.search.more;
        //     setTimeout(function () {
        //         setSearchToolbarHeight();
        //     }, 100);
        // };
        // $scope.setMark = function (mark, level) {
        //     switch (level) {
        //         case 0:
        //             $scope.search.zeroLabel = null;
        //             if ($scope.tagList.zero.join(',').indexOf(mark) >= 0)
        //                 return;
        //             $scope.tagList.zero.push(mark);
        //             break;
        //         case 1:
        //             $scope.search.oneLabel = null;
        //             if ($scope.tagList.one.join(',').indexOf(mark) >= 0)
        //                 return;
        //             $scope.tagList.one.push(mark);
        //             break;
        //         case 2:
        //             $scope.search.twoLabel = null;
        //             if ($scope.tagList.two.join(',').indexOf(mark) >= 0)
        //                 return;
        //             $scope.tagList.two.push(mark);
        //             break;
        //         case 3:
        //             $scope.search.threeLabel = null;
        //             if ($scope.tagList.three.join(',').indexOf(mark) >= 0)
        //                 return;
        //             $scope.tagList.three.push(mark);
        //             break;
        //     }
        // };
        // $scope.removeMark = function (index, level) {
        //     switch (level) {
        //         case 0:
        //             $scope.tagList.zero.splice(index, 1);
        //             break;
        //         case 1:
        //             $scope.tagList.one.splice(index, 1);
        //             break;
        //         case 2:
        //             $scope.tagList.two.splice(index, 1);
        //             break;
        //         case 3:
        //             $scope.tagList.three.splice(index, 1);
        //             break;
        //     }
        // };
        // $scope.searchTag = function (level) {
        //     var data = {labelLevel: level, pageSize: 30};
        //     switch (level) {
        //         case 0:
        //             data.labelLevel = null;
        //             data.labelName = $scope.search.zeroLabel;
        //             break;
        //         case 1:
        //             data.labelName = $scope.search.oneLabel;
        //             break;
        //         case 2:
        //             data.labelName = $scope.search.twoLabel;
        //             data.pids = $scope.tagList.one.length ? $scope.tagList.one.join(',') : null;
        //             break;
        //         case 3:
        //             data.labelName = $scope.search.threeLabel;
        //             data.pids = $scope.tagList.two.length ? $scope.tagList.two.join(',') : null;
        //             break;
        //     }
        //     if (!data.labelName || !data.labelName.replace(/(^\s*)|(\s*$)/g, "").length) {
        //         switch (level) {
        //             case 0:
        //                 $scope.searchList.zero.length = 0;
        //                 break;
        //             case 1:
        //                 $scope.searchList.one.length = 0;
        //                 break;
        //             case 2:
        //                 $scope.searchList.two.length = 0;
        //                 break;
        //             case 3:
        //                 $scope.searchList.three.length = 0;
        //                 break;
        //         }
        //         return;
        //     }
        //     ReqServ.request('POST', 'ty/tyLabel/read/list', data).success(function (result) {
        //         if (result.httpCode != appInterface.successCode) {
        //             switch (level) {
        //                 case 0:
        //                     $scope.searchList.zero.length = 0;
        //                     break;
        //                 case 1:
        //                     $scope.searchList.one.length = 0;
        //                     break;
        //                 case 2:
        //                     $scope.searchList.two.length = 0;
        //                     break;
        //                 case 3:
        //                     $scope.searchList.three.length = 0;
        //                     break;
        //             }
        //             return;
        //         }
        //         switch (level) {
        //             case 0:
        //                 $scope.searchList.zero.length = 0;
        //                 result.data.list.forEach(function (value) {
        //                     $scope.searchList.zero.push(value);
        //                 });
        //                 break;
        //             case 1:
        //                 $scope.searchList.one.length = 0;
        //                 result.data.list.forEach(function (value) {
        //                     $scope.searchList.one.push(value);
        //                 });
        //                 break;
        //             case 2:
        //                 $scope.searchList.two.length = 0;
        //                 result.data.list.forEach(function (value) {
        //                     $scope.searchList.two.push(value);
        //                 });
        //                 break;
        //             case 3:
        //                 $scope.searchList.three.length = 0;
        //                 result.data.list.forEach(function (value) {
        //                     $scope.searchList.three.push(value);
        //                 });
        //                 break;
        //         }
        //     }).error(function (result) {
        //         switch (level) {
        //             case 0:
        //                 $scope.searchList.zero.length = 0;
        //                 break;
        //             case 1:
        //                 $scope.searchList.one.length = 0;
        //                 break;
        //             case 2:
        //                 $scope.searchList.two.length = 0;
        //                 break;
        //             case 3:
        //                 $scope.searchList.three.length = 0;
        //                 break;
        //         }
        //     })
        // };
        // $scope.clearTag = function () {
        //     $timeout(function () {
        //         $scope.searchList.zero.length = $scope.searchList.one.length = $scope.searchList.two.length = $scope.searchList.three.length = 0;
        //     }, 150);
        // };
        // $scope.searchUser = function () {
        //     $state.current.name.indexOf('searchex') >= 0 ? $scope.$broadcast('loadSearch') : $state.go('master.portrait.searchex');
        // };
        //setSearchToolbarHeight();
    }])
});
