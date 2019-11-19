<!-- toaster directive -->
<toaster-container toaster-options="{'position-class': 'toast-top-right', 'close-button':true}"></toaster-container>
<!-- / toaster directive -->

<!-- 导航条 -->
<div class="bg-white bread-crumb">
    <ul class="breadcrumb m-b-n-xs wrapper-md tf-DHpadding">
        <li><a ui-sref="main.sys.user.welcome">首页</a></li>
        <li class="active" ng-bind="title"></li>
    </ul>
</div>

<!-- 加载提醒 -->
<div ng-show="loading" class="text-center m-t-md text-lg"><i class="fa fa-spin fa-spinner"></i> 数据加载中...</div>

<!-- 内容区域 -->
<div ng-hide="loading" class="wrapper-md content tf-leiRong">
    <!-- 数据显示 -->
    <div class="wrapper-data tf-biaoGe">
            <!-- 头部 -->
            <div class="panel-heading clearfix">
                <div class="col-sm-10 search-panel">
                    <form ng-submit="search()" class="form-inline">
                        <div class="form-group">
                        <#list table.searchFields as item>
                        	<#if item.dictFlag >
                            <select kendo-combo-box k-data-text-field="'text'" k-data-value-field="'id'" k-placeholder="'${item.name}'"
                           		k-data-source="${item.refObj}" ng-model="param.${item.code}" ></select>
             				<#elseif (item.javaType=="String")>
                            <input type="text" class="k-textbox tf-input"  ng-model="param.${item.code}" placeholder="${item.name}"
                                   maxlength="${(item.length/2)?floor}"  >
                            </#if>
                        </#list>
                            <button type="submit" class="btn btn-submit tf-chaXun">查询</button>
                        </div>
                    </form>
                </div>
                <div class="col-sm-2 form-group" style="padding:0;">
                    <a ui-sref="main.${module}.${table.beanObj}.create" class="btn btn-info tf-tianJia">添加${table.tableDesc}</a>
                </div>
            </div>
            <kendo-grid k-options="mainGridOptions" k-data-source="pageInfo.list">
            </kendo-grid>

            <script id="handle-template" type="text/x-kendo-template">
                <a ui-sref="main.${module}.${table.beanObj}.view({id: '#=id#'})" class="hrefbtn">
                    <i class="fa fa-book m-r-xs text-sm"></i>查看</a>
                <a ng-click="disableItem('#=id#',0)" class="hrefbtn">
                    <i class="fa fa-trash-o m-r-xs text-sm"></i>删除</a>
                <a ui-sref="main.${module}.${table.beanObj}.update({id: '#=id#'})" class="hrefbtn">
                    <i class="fa fa-edit m-r-xs text-sm"></i>编辑</a>
            </script>

            <!-- 底部分页 -->
			<span data-ng-include=" 'mng/tpl/pageInfo.html' "></span>
    </div><!-- /.wrapper-data -->

</div>