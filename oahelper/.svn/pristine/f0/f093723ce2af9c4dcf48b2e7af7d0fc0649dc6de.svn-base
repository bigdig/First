'use strict';

angular.module('app')
	.controller('${table.beanObj}Controller', [ '$rootScope', '$scope', '$http','$timeout', '$state',
	                                function($rootScope, $scope, $http,$timeout, $state) {
		$scope.title = '${table.tableDesc}管理';
		//返回到当前列表时，注入上次查询参数
		$rootScope.fillPageParam($scope,$state);
        $scope.loading = false;

		$scope.mainGridOptions ={
			scrollable: true,
			resizable:true,
			columns: [
		    <#list table.fields as item>
		    <#if !item.fileFlag && !item.skipViewFlag && item.code!="createBy" && item.code!="createTime"
                    && item.code!="updateBy" && item.code!="updateTime" && item.code!="deleteFlag">
		    	<#if item.entityFlag || item.dictFlag>
					{ field: '${item.nameField}', title: '${item.name}', width: '120px'},
				<#else>
					{ field: '${item.code}', title: '${item.name}', width: '120px'},
				</#if>
			</#if>
		    </#list>
					{ title: '操作',template:kendo.template($("#handle-template").html()), width: '200px'}
			]
		};


		$scope.pageInfo = {};
		$scope.search = function () {
			//采用深复制，避免searchParams和param引用同一对象，造成渲染下拉框选中值时被清除
			$rootScope.searchParams = JSON.parse(JSON.stringify($scope.param));
	        $scope.loading = true;
			$scope.pageInfo = {};
			$.ajax({
				url : '/${module}/${table.beanObj}/read/list',
				data: $scope.param
			}).then(function(result) {
		        $scope.loading = false;
				if (result.httpCode == 200) {
					$scope.pageInfo = result.data;
					<#list table.searchFields as item>
                	<#if item.dictFlag>
                    $scope.${item.refObj} = result.dicts['${item.ref}'];
                    </#if>
                    </#list>
                    //延时重新赋值避免下拉框动态选项未被选中
					$timeout(function(){
						$scope.param = $rootScope.searchParams;
					},100);
					
				} else {
					$scope.msg = result.msg;
				}
				$scope.$apply();
			});
		}
		
		$scope.search();
		
		$scope.clearSearch = function() {
			$scope.param.keyword= null;
			$scope.search();
		}
		
		$scope.disableItem = function(id, enable) {
			$scope.loading = true;
			$.ajax({
				url : '/${module}/${table.beanObj}/delete',
				data: {id:id}
			}).then(function(result) {
		        $scope.loading = false;
				if (result.httpCode == 200) {
					$scope.search();
				} else {
					
				}
				$scope.$apply();
			});
			
		}
		
		// 翻页
        $scope.pagination = function (page) {
            $scope.param.pageNum=page;
            $scope.search();
        };
} ]);