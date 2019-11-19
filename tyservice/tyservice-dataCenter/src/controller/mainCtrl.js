angular.module('myApp').controller('mainCtrl', ["$scope", "req", "$state", "$rootScope", "storage", "$timeout",
    function ($scope, req, $state, $rootScope, storage, $timeout) {
        var area, custName, orgName, mark, requestObj,requestObj2,custOrgName;
        $scope.write = '';
        $scope.result = false;
        $scope.num = 0;
        $scope.panel = false;
        //初始化 page打开 page2影藏
        $scope.page1 = true;
        $scope.page2 = false;
        $scope.header_bar = true;
        // 默认加载第一页
        $scope.pageSize = 15;
        $scope.pageNum = 1;
        //是否加载更多数据
        $scope.moredata = true;
        $scope.moredata2 = true;
        $scope.getOrg = function () {
            req.getDate("/bizspace/read/custlist", {
                "token": token, "pageSize": $scope.pageSize,
                "pageNum": $scope.pageNum
            }).then(function (result) {
                if (result.status == 200 && result.data.httpCode == 200) {
                    $scope.resultLength = false;
                    $scope.response = result.data.data.list || [];
                }
            }, function (error) {
            })
        }
        $scope.getOrg2 = function () {
            req.getDate("/bizspace/read/orglist", {
                "token": token, "pageSize": $scope.pageSize,
                "pageNum": $scope.pageNum
            }).then(function (result) {
                if (result.status == 200 && result.data.httpCode == 200) {
                    $scope.resultLength = false;
                    $scope.response2 = result.data.data.list || [];
                }
            }, function (error) {
            })
        }
        // $scope.getOrg();
        // 所有的地名
        $scope.allArea = ["全部", "北京", "上海", "广州", "深圳", "创新团队"];
        // 加载默认请求 org接口
        //搜索的时候 默认清空公共对象的值
        $scope.$watch('write', function (newValue, oldValue) {
            // 用户输入的时候  初始化页数 和数组response
            $scope.pageNum = 1;
            $scope.response = [];
            $scope.resultLength = false;
            $scope.num = null;
            requestObj2.orgName = '';
            requestObj.orgName='';
            requestObj.custName = '';
            requestObj2.area = '';
            requestObj.mark = '';
            requestObj.pageNum=1;
            $scope.moredata = true;
            // 当用户没有任何输入时候
            if (!newValue) {
                $scope.getOrg();
                return;
            }
            // 值有变化并且值存在
            if (newValue !== oldValue) {
                requestObj.custName = newValue;
                req.getDate("/bizspace/read/custlist", requestObj).then(function (result) {
                    if (result.status == 200) {
                        $scope.response = result.data.data.list;
                        $scope.num = result.data.data.total;
                        $scope.resultLength = true;
                    }
                }, function (error) {
                })
            }
        })
        $scope.$watch('write2', function (newValue, oldValue) {
            $scope.pageNum = 1;
            requestObj2.pageNum=1;
            $scope.response2 = [];
            $scope.resultLength = false;
            $scope.num = null;
            requestObj2.orgName = '';
            requestObj.custName = '';
            requestObj2.area = '';
            requestObj.mark = '';
            requestObj.orgName='';
            $scope.moredata2 = true;
            // document.getElementById('offset').style.top="82px";
            // document.getElementById('offset2').style.top="82px";
            // 当用户没有任何输入时候
            if (!newValue) {
                $scope.getOrg2();
                return;
            }
            if (newValue !== oldValue) {
                // document.getElementById('offset').style.top="102px";
                // document.getElementById('offset2').style.top="102px";
                requestObj2.orgName = newValue;
                req.getDate("/bizspace/read/orglist", requestObj2).then(function (result) {
                    if (result.status == 200) {
                        $scope.response2 = result.data.data.list;
                        $scope.num = result.data.data.total;
                        $scope.resultLength = true;
                    }
                }, function (error) {
                })
            }
        })
        // 打开面板时
        $scope.pop = function (id) {
            $scope.panel = true;
            // document.getElementById(id).style.top = "29px";
        }
        $scope.cancel = function () {
            $scope.panel = false;
            $scope.header_bar = true;
            // document.getElementById('offset').style.top = "29px";
            $scope.resize();
        }
        $scope.cancel2 = function () {
            $scope.panel = false;
            $scope.header_bar = true;
            // document.getElementById('offset').style.top = "29px";
        }
        //清空数据
        $scope.resize = function () {
            var list = document.getElementById('Area').getElementsByTagName('div');
            for (var i = 0; i < list.length; i++) {
                angular.element(list[i]).removeClass('active');
            }
            angular.element(list[0]).addClass('active');
            document.getElementById('custName').value = '';
            document.getElementById('orgName').value = '';
            document.getElementById('mark').value = '';
            document.getElementById('custOrgName').value='';
            requestObj.custOrgName='';
            requestObj2.orgName = ''
            requestObj.area = '';
            requestObj.custName = '';
            requestObj2.mark = '';
        }
        //一个公共的对象 保存结果
        requestObj = {
            "custName": custName || '',
            "orgName":  orgName||'',
            "pageNum": $scope.pageNum,
            "token": token,
            "pageSize": $scope.pageSize,
            "mark": mark || ''
        }
        requestObj2 = {
            "orgName": orgName || '',
            "pageNum": $scope.pageNum,
            "token": token,
            "pageSize": $scope.pageSize,
            "area": area || ''
        }
        $scope.right=function(index){
            if(index==1){
                $scope.page1=true;
                $scope.page2=false;
            }else if(index==2){
                $scope.page1=false;
                $scope.page2=true;
            }
        }

        //搜索结果
        $scope.search = function () {
            requestObj2.area = document.getElementsByClassName('active')[0].innerText == "全部" ? '' : document.getElementsByClassName('active')[0].innerText;
            requestObj.custName = document.getElementById('custName').value;
            requestObj2.orgName = document.getElementById('orgName').value;
            requestObj.mark = document.getElementById('mark').value;
            requestObj.orgName=document.getElementById('custOrgName').value;
            //初始化
            $scope.pageNum = 1;
            requestObj2.pageNum=1;
            requestObj.pageNum=1;
            if ($scope.page1) {
                // requestObj.custName=$scope.write;
                $scope.moredata = true;
                req.getDate("/bizspace/read/custlist", requestObj).then(function (result) {
                    if (result.status == 200) {
                        if (result.data.data) {
                            $scope.response = result.data.data.list;
                            $scope.num = result.data.data.total;
                            $scope.resultLength = true;
                        }
                    }
                }, function (error) {
                })
            } else if ($scope.page2) {
                // requestObj2.orgName=$scope.write2;
                $scope.moredata2 = false;
                req.getDate("/bizspace/read/orglist", requestObj2).then(function (result) {
                    if (result.status == 200) {
                        if (result.data.data) {
                            $scope.response2 = result.data.data.list;
                            $scope.num = result.data.data.total;
                            $scope.resultLength = true;
                        }
                    }
                }, function (error) {
                })
            }
            //搜索后 隐藏 面板
            $scope.cancel2();
        }
        $scope.activation = function (even) {
            var list = document.getElementsByClassName('active');
            for (var i = 0; i < list.length; i++) {
                angular.element(list[i]).removeClass('active');
            }
            $scope.area = angular.element(even.target).text();
            angular.element(even.target).addClass('active');
        };
        //头部切换
        $scope.tabTab = function (index, event) {
            //影藏搜索数量
            $scope.resultLength = false;
            var list = document.getElementsByClassName('tab');
            for (var i = 0; i < list.length; i++) {
                angular.element(list[i]).removeClass('tab-active');
            }
            angular.element(event.target).addClass('tab-active');
            if (index === 1) {
                $scope.page1 = true;
                $scope.page2 = false;
            } else if (index === 2) {
                $scope.page1 = false;
                $scope.page2 = true;
            }
        }
        $scope.detail = function (id, orgId) {
            console.log(id, orgId);
            var obj = {
                "id": id,
                "orgId": orgId
            }
            storage.setData(obj);
            if (id && orgId) {
                $rootScope.TabHeader = true;
                $rootScope.clientHeaderClass = true;
                $rootScope.TwoclientHeaderClass = false;
                $state.go("client");
            }else if(id){
                $rootScope.TabHeader = false;
                $rootScope.clientHeaderClass = true;
                $rootScope.TwoclientHeaderClass = false;
                $state.go("client");
            }

        }
// 加载更多
        $scope.loadMore = function () {
            $scope.pageNum = $scope.pageNum + 1;
            if ($scope.page1) {
                requestObj.pageNum= $scope.pageNum;
                if (!$scope.write&&requestObj.custName&&!requestObj.mark&&!requestObj.orgName) {
                    req.getDate("/bizspace/read/orglist", {
                        "token": token, "pageSize": $scope.pageSize,
                        "pageNum": $scope.pageNum
                    }).then(function (result) {
                        if (result.status == 200 && result.data.data) {
                            if(requestObj.pageNum*$scope.pageSize<=result.data.data.total){
                                $scope.response = $scope.response.concat(result.data.data.list);
                                $scope.num = result.data.data.total;
                                $scope.$broadcast('scroll.infiniteScrollComplete');
                            }else {
                                $scope.moredata = false;
                                $scope.$broadcast('scroll.infiniteScrollComplete');
                            }
                        }
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    }, function (error) {
                    })
                } else {
                    req.getDate("/bizspace/read/custlist", requestObj).then(function (result) {
                        if (result.status == 200 && result.data.data) {
                                if(requestObj.pageNum*$scope.pageSize<=result.data.data.total){
                                    $scope.response = $scope.response.concat(result.data.data.list);
                                    $scope.num = result.data.data.total;
                                    $scope.$broadcast('scroll.infiniteScrollComplete');
                                }else {
                                    $scope.moredata = false;
                                    $scope.$broadcast('scroll.infiniteScrollComplete');
                                }
                        }
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    }, function (error) {
                    })
                }
            } else if ($scope.page2) {
                requestObj2.pageNum= $scope.pageNum;
                if (!$scope.write2&&!requestObj2.orgName&&!requestObj2.area) {
                    req.getDate("/bizspace/read/orglist", {
                        "token": token, "pageSize": $scope.pageSize,
                        "pageNum": $scope.pageNum
                    }).then(function (result) {
                        if (result.status == 200 && result.data.data) {
                                if(requestObj2.pageNum*$scope.pageSize<=result.data.data.total) {
                                    $scope.response2 = $scope.response2.concat(result.data.data.list);
                                    $scope.num = result.data.data.total;
                                    $scope.$broadcast('scroll.infiniteScrollComplete');
                                }else {
                                    $scope.moredata2 = false;
                                    $scope.$broadcast('scroll.infiniteScrollComplete');
                                }
                            }

                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    }, function (error) {
                    })
                } else {
                    req.getDate("/bizspace/read/orglist", requestObj2).then(function (result) {
                        if (result.status == 200 && result.data.data) {
                                if(requestObj2.pageNum*$scope.pageSize<=result.data.data.total) {
                                    $scope.response2 = $scope.response2.concat(result.data.data.list);
                                    $scope.num = result.data.data.total;
                                    $scope.$broadcast('scroll.infiniteScrollComplete');
                                }else {
                                    $scope.moredata2 = false;
                                    $scope.$broadcast('scroll.infiniteScrollComplete');
                                }

                        }
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    }, function (error) {
                    })
                }
            }
        }
    }])