<!-- toaster directive -->
<toaster-container toaster-options="{'position-class': 'toast-top-right', 'close-button':true}"></toaster-container>
<!-- / toaster directive -->

<!-- 导航条 -->
<div class="bg-white bread-crumb">
    <ul class="breadcrumb m-b-n-xs wrapper-md tf-DHpadding">
        <li><a ui-sref="main.sys.user.welcome">首页</a></li>
        <li><a ui-sref="main.${module}.${table.beanObj}.list">${table.tableDesc}管理</a></li>
        <li class="active" ng-bind="title"></li>
        <a ui-sref="main.${module}.${table.beanObj}.list" class="goFor">返回</a>
    </ul>
</div>

<!-- 内容区域 -->
<div class="wrapper-md content tf-leiRong">
    <div class="panel tf-biaoGe">
        <!--表单-->
        <div class="panel-body">
        	<form class="form-horizontal"  name="user_form">
            <#list table.fields as item>
            <#if !item.skipViewFlag && item.code!="createBy" && item.code!="createTime"
                    && item.code!="updateBy" && item.code!="updateTime">
                <!--${item.name}-->
               <label class="col-lg-2 col-sm-3 control-label">
               <span class="text-danger wrapper-sm"></span>
               ${item.name}
               </label>
               <div class="col-sm-9"> 
               <#if (item.fileFlag==true)>
               <div class="file-list" ng-repeat="file in record.${item.code}Bean">
                   <span class=" control-label" >
                       <a ng-href="/uploadfile/down?fileId={{file.id}}">
                           <i class="fa fa-file-text-o m-r-xs text-sm"></i>{{file.actualName}}
                       </a>
                   </span>
               </div>
               <#elseif item.entityFlag || item.dictFlag>
                   <label class="control-label control-content">&nbsp;{{record.${item.nameField}}}</label>
               <#else>
                   <label class="control-label control-content">&nbsp;{{record.${item.code}}}</label>
               </#if>
               </div>
            </#if>
            </#list>
            </form>
        </div>
    </div>
</div>
