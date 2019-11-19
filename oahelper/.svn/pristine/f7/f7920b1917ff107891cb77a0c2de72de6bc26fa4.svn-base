'use strict';

    angular.module('app')
        .controller('${table.beanObj}UpdateController', ['$scope', '$rootScope', '$state', '$timeout', 'toaster',
                                             function($scope, $rootScope, $state, $timeout, toaster) {
        var title = "";
        if($state.includes('**.${table.beanObj}.update')){
            title="编辑${table.tableDesc}";
            var id = $state.params.id;
            activate(id);
            validate(id);
        }else if($state.includes('**.${table.beanObj}.create')){
            title="添加${table.tableDesc}";
            activate(null);
            validate(null);
            setTimeout(function(){
                !$rootScope.$$phase && $scope.$apply();
            },300);
        }
        $scope.title = title;
        $scope.loading = true;
        <#if table.hasFileField>
        var custFile={};
        initFileInputs();
        </#if>

        //提交表单
        $scope.submit= function(){
            <#list table.filesFields as item>
        	$scope.record.${item.code} = clearComma($scope.record.${item.code})  + custFile['${item.code}'].join(',');
        	$scope.record.${item.ref} = clearComma($scope.record.${item.ref})  + custFile['${item.ref}'].join(',');
		    </#list>

            $scope.loading = true;
            var m = $scope.record;
            if(m){
                $scope.isDisabled = true;//提交disabled
                $.ajax({
                    url : $scope.record.id ? '/${module}/${table.beanObj}/update' : '/${module}/${table.beanObj}/add',
                    data: $scope.record
                }).then(function(result){
                if(result.httpCode==200){//成功
                    toaster.clear('*');
                    toaster.pop('success', '', "保存成功");
                    <#list table.filesFields as item>
		        	custFile['${item.code}'].splice(0,custFile['${item.code}'].length);
		        	custFile['${item.ref}'].splice(0,custFile['${item.ref}'].length);
				    </#list>
                    
                    $timeout(function(){
                        $state.go('main.${module}.${table.beanObj}.list');
                    },2000);
                }else{
                    toaster.clear('*');
                    toaster.pop('error', '', result.msg);
                    $scope.isDisabled = false;
                }
                });
            }
            
        };

        // 初始化页面
        function activate(id) {
            $scope.loading = true;
            $.ajax({
                url : '/${module}/${table.beanObj}/read/detail',
                data: {'id': id}
            }).then(function(result) {
                $scope.loading = false;
                if (result.httpCode == 200) {
                	<#list table.fields as item>
                	<#if item.entityFlag>
                    $scope.${item.refObj}s = result.dicts['${item.ref}s'];
                	<#elseif item.dictFlag>
                    $scope.${item.refObj} = result.dicts['${item.ref}'];
                    </#if>
                    </#list>
                    //先做字典赋值，再延时加载，避免下拉框中的值没有被选中
                    $timeout(function(){
                        $scope.record = result.data;
                    },100);
                } else {
                    $scope.msg = result.msg;
                }
                $scope.$apply();

            });
        }


        //表单验证 
        function validate(userId){
            $scope.validate = function(event) {
                event.preventDefault();
                console.log('validate' + $scope.validator.validate());
                if ($scope.validator.validate()) {
                    $scope.submit();
                }
            }
        }

		<#if table.hasFileField>
        function initFileInputs(){
            <#list table.filesFields as item>
            initFileInput("${item.code}",'${item.code}','${item.ref}');//初始化上传文件控件
		    </#list>
        }
        function initFileInput(ctrlName,fileArray,fileNameArray) {
            var control = $('#' + ctrlName);
            control.attr('fileArray',fileArray);
            control.attr('fileNameArray',fileNameArray);
            custFile[fileArray] = [];
            custFile[fileNameArray] = [];

            control.fileinput({
                uploadUrl:"/uploadfile/file", language: 'zh',showUpload: false,dropZoneEnabled: false,
                showCaption: false, browseClass: "btn btn-info",maxFileCount: 1,showPreview:true,maxFileSize:10000
            })
            .on("filebatchselected", function(event, files) {
                $(this).fileinput("upload");
            })
            .on("fileuploaded", function(event, data) {
                if(data.response.httpCode != 200){
                    alert('上传失败');
                }else{
                    var file = data.response.files[0];
                    custFile[control.attr('fileArray')].push(file.id);
                    custFile[control.attr('fileNameArray')].push(file.actualName);
                    console.log(custFile);
                }
            })
            .on("filecleared", function(event, data) {
                custFile[control.attr('fileArray')].splice(0,control.fileArray.length) ;
                custFile[control.attr('fileNameArray')].splice(0,control.fileNameArray.length) ;
            })
            .on("fileuploaderror", function(event, data) {
                $(".file-preview-error").remove();
            });
        }
        
        $scope.deleteExistSingleFile = function(file,filename,fileid){
            var file_arr = $scope.record[file].split(",");
            var filename_arr = $scope.record[filename].split(",");
            var idx = file_arr.indexOf(fileid) ;
            file_arr.splice(idx,1) ;
            filename_arr.splice(idx,1) ;
            $scope.record[file+'Bean'].splice(idx,1) ;
            $scope.record[file] = file_arr.join(",");
            $scope.record[filename] = filename_arr.join(",");
        }

        function clearComma(str){
            if(str!=null && str.length >0){
                if(str.charAt(str.length -1) == ","){
                    return str;
                }else{
                    return str +",";
                }
            }else{
                return '';
            }
        }
        </#if>
    }]);