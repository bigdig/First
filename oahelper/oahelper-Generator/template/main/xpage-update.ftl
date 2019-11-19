<!-- toaster directive -->
<toaster-container toaster-options="{'position-class': 'toast-bottom-right', 'close-button':true}"></toaster-container>
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
<div class="wrapper-md content">
    <div class="panel tf-biaoGe">
        <!--表单-->
            <div class="panel-body">
                <form class="form-horizontal" kendo-validator="validator" ng-submit="validate($event)" name="user_form">
                    <#list table.fields as item>
                    <#if (item.code=="id" || item.code=="enable"|| item.code=="createBy"|| item.code=="createTime"
                    || item.code=="updateBy"|| item.code=="updateTime"|| item.code=="deleteFlag"|| item.skipInputFlag)>
				    <#else>
                	<!--${item.name}-->
                    <div class="form-group">
                        <label class="col-lg-2 col-sm-3 control-label" for="${item.code}">
                        <span class="text-danger wrapper-sm">*</span>${item.name}</label>
                        <div class="col-sm-6 ">
                        	<#if item.entityFlag>
                            <select kendo-combo-box id="${item.code}" name="${item.code}" k-data-text-field="'text'" k-data-value-field="'id'" 
                           		k-data-source="${item.refObj}s" ng-model="record.${item.code}" required >
                            </select>
                        	<#elseif item.dictFlag >
                            <select kendo-combo-box id="${item.code}" name="${item.code}" k-data-text-field="'text'" k-data-value-field="'id'" 
                           		k-data-source="${item.refObj}" ng-model="record.${item.code}" required >
                            </select>
                        	<#elseif (item.javaType=="Integer" || item.javaType=="Long")>
                        	<input kendo-numeric-text-box id="${item.code}" name="${item.code}" k-min="0" k-max="100" 
                        		k-options="{format:'#',decimals:'0'}" k-ng-model="record.${item.code}" required />
                        	<#elseif (item.javaType=="Double")>
                        	<input kendo-numeric-text-box id="${item.code}" name="${item.code}" k-min="0" k-max="100" 
                        		k-options="{format:'#.00  元',decimals:'2'}" k-ng-model="record.${item.code}" required/>
                        	<#elseif (item.javaType=="Date")>
							<input kendo-date-picker id="${item.code}" name="${item.code}" k-options="{format:'yyyy-MM-dd'}" 
								ng-model="record.${item.code}" required/>
             				<#elseif (item.javaType=="String" && item.fileFlag==false)>
                            <#if (item.length<1000)>
                            <input type="text" class="k-textbox" name="${item.code}" id="${item.code}"  ng-model="record.${item.code}"
                                   required maxlength="${(item.length/2)?floor}" />
                            <#else>
                            <textarea class="form-control" name="${item.code}" id="${item.code}"  ng-model="record.${item.code}"></textarea>
                            </#if>
             				<#elseif (item.fileFlag==true)>
             				    <div>
	                                <input id="${item.code}" name="${item.code}" type="file" class="file">
	                            </div>
	                            <div class="file-list" ng-repeat="file in record.${item.code}Bean">
	                                <span class=" control-label" >
	                                    <a ng-href="/uploadfile/down?fileId={{file.id}}">
	                                        <i class="fa fa-file-text-o m-r-xs text-sm"></i>{{file.actualName}}
	                                    </a>
	                                </span>
	                                <button type="button" id="{{file.id}}" ng-click="deleteExistSingleFile('${item.code}','${item.ref}',file.id)" class="btn btn-sm btn-default">
	                                    <i class="fa fa-times m-r-xs text-sm"></i>删除</button>
	                            </div>
                        	</#if>
                        </div>
                    </div>
				    </#if>
				    </#list>

                    <!--btn-->
                    <div class="form-group">
                        <div class="tf-bottomFixfd">
                            <a ui-sref="main.${module}.${table.beanObj}.list" class="btn tf-btn-white shanchu-color">返回</a>
                            <button type="submit" class="btn tf-btn-red" ng-disabled="isDisabled">提交</button>
                        </div>
                    </div>
                </form>
            </div>
    </div>
</div>