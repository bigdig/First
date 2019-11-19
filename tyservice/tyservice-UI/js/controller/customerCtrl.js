define(['app', 'echarts', 'echartsWordcloud'], function (app, echarts,echartsWordcloud) {
    app.controller('customerCtrl', ['$scope', '$sce', '$state', '$stateParams', '$timeout', 'ReqServ', 'appInterface', 'ngDialog', function ($scope, $sce, $state, $stateParams, $timeout, ReqServ, appInterface, ngDialog) {
        $scope.customer = {};
        $scope.customer.markCal = 'or';
        $scope.customerInfo = {};
        $scope.uploadDataModel.value = '单个添加客户信息';
        $scope.uploadDataModel.template = 'editCustomerTemplate';
        $scope.uploadDataModel.pageTitle = '批量添加客户信息';
        $scope.uploadDataModel.templateFile = appInterface.fileUrl + 'ty_orgcustomer_temp.xlsx';
        $scope.uploadDataModel.uploadUrl = 'ty/tyOrgcustomer/batchImport';
        $scope.uploadDataModel.scope = $scope;
        $scope.welcome.isHide = true;
        $scope.servieSalers = [];
        $scope.searchTags = [];
        $scope.tagList = [];
        $scope.tagList1= [];
        $scope.tagList2= [];
        $scope.tagList3= [];
        $scope.tagList4= [];
        $scope.tagData=[]
        $scope.lastLabelName1= [];
        $scope.first = '';
        $scope.customer.pageNum =1
        $scope.groupTools = {isShow: false, isEdit: true, disabled: false, isSubmit: false};
        $scope.newGroup = {};
        $scope.welcome.isHide = true;
        $scope.reportMenu = [{name: '相关标签', active: false},
            {name: '地域分布', active: false},
            {name: '销售潜力分布', active: false},
            {name: '行业分布', active: false},
            {name: '活跃趋势', active: false},
            {name: '活动喜好分布', active: false}];
        $scope.searchT = {more: false};
        $scope.meetingCust = {};

        var _tempUpdateCustomerOrgName = '';

        var _item;
        var currentName = $state.current.name;
        if (currentName.indexOf('home') >= 0) {
            if (!$scope.tabMenus[1].active)
                $scope.tabMenus[1].active = true;
            $('.out').removeClass('padding20');
            $scope.meetingCust.isShow = false;

        } else {
            $('.out').addClass('padding20');
            $scope.meetingCust.isShow = true;
        }
        var currentGroup, width, reqUrl, reportChart, chartOption;
        var itemStyle = {
            normal: {},
            emphasis: {
                barBorderWidth: 1,
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowOffsetY: 0,
                shadowColor: 'rgba(0,0,0,0.5)'
            }
        };
        var pieOption = {
            tooltip: {
                trigger: 'item',
                formatter: "{b}:{c}({d}%)"
            },
            legend: {
                orient: 'vertical',
                right: '15%',
                top: '25%',
                data: []
            },
            series: [{
                type: 'pie',
                center: ['35%', '50%'],
                data: [],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }]
        };
        var barOption = {
            title: {text: '客户活跃趋势'},
            toolbox: {
                feature: {
                    magicType: {
                        type: ['stack', 'tiled']
                    },
                    dataView: {}
                }
            },
            tooltip: {},
            legend: {
                data: ['活跃客户', '非活跃客户']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [{
                type: 'category',
                data: [],
                axisTick: {
                    alignWithLabel: true
                }
            }],
            yAxis: [{
                type: 'value'
            }],
            series: [{
                name: '活跃客户',
                type: 'bar',
                stack: '客户',
                barWidth: '30%',
                itemStyle: itemStyle,
                data: []
            }, {
                name: '非活跃客户',
                type: 'bar',
                stack: '客户',
                barWidth: '30%',
                itemStyle: itemStyle,
                data: []
            }]
        };
        $scope.markChart  = {
            icon: 'icon icon-filter',
            event:function(){
                ngDialog.open({
                    template: 'countermarkTemplate',
                    className: 'ngdialog-theme-default',
                    onOpenCallback: $scope.countermarkChartOpenedCallback,
                    scope: $scope
                });
            }
        }
        $scope.markData=[]
        $scope.markData1=[]
        var dataModel = {
            charts: [{
                title: '统计图',
                icon: 'icon icon-pie-chart',
                event: function () {
                    ngDialog.open({
                        template: 'reportTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.reportChartOpenedCallback,
                        scope: $scope
                    });
                }
            }],
            toolsBar: [{
                btnName: '发送短信', bgStyle: 'bg-cyan text-cyan-light', event: function () {
                    ngDialog.open({
                        template: 'sendMessageTemplate',
                        className: 'ngdialog-theme-default',
                        preCloseCallback: $scope.$parent.preCloseCallback,
                        onOpenCallback: $scope.$parent.openedCallback,
                        scope: $scope.$parent
                    });
                }, isDisabled: true
            }, {
                btnName: '发送邮件', bgStyle: 'bg-cyan-dark text-blue-lit', event: function () {
                    ngDialog.open({
                        template: 'sendEmailTemplate',
                        className: 'ngdialog-theme-default',
                        preCloseCallback: $scope.$parent.preCloseCallback,
                        onOpenCallback: $scope.$parent.openedEmailCallback,
                        scope: $scope.$parent
                    });
                }, isDisabled: true
            }, {
                btnName: '添加客户信息', bgStyle: 'bg-purple-light text-purple', event: function () {
                    if ($scope.hasTaskRun($scope.customerInfo.loading))
                        return;
                    ReqServ.clearObj($scope.customerInfo);
                    $scope.customerInfo.labels = [];
                    $scope.customerInfo.pageTitle = '添加客户信息';
                    $scope.customerInfo.btnName = '添加';
                    ngDialog.open({
                        template: 'editCustomerTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editCustomerOpenedCallback,
                        scope: $scope
                    });
                }, isHide: false
            }, {
                btnName: '批量导出客户', bgStyle: 'bg-flow-opacity text-blue-lit', event: function () {
                    var params = '';
                    for (var propert in $scope.searchModel) {
                        params += propert + '=' + $scope.searchModel[propert] + '&'
                    }
                    window.open('ty/tyOrgcustomer/batchExport?' + params.substring(0, params.length - 1));
                }, isHide: false
            }, {
                btnName: '添加分组', bgStyle: 'bg-red-light text-orange', event: function () {
                    if ($scope.hasTaskRun($scope.newGroup.loading))
                        return;
                    showTemplate(true, 'addGroupTemplate')
                }, isHide: false, isDisabled: true, title: '最多可添加' + appInterface.groupNum + '个分组'
            }],
            menu: {
                isEnable: false, isShow: false, title: '菜单', list: [{
                    name: '移组', callback: function () {
                        if ($scope.hasTaskRun($scope.newGroup.loading))
                            return;
                        showTemplate(false, 'moveGroupTemplate');
                    }
                }, {
                    name: '移除客户', callback: function () {
                        if ($scope.hasTaskRun($scope.customerInfo.loading))
                            return;
                        if (!confirm('确定要删除吗？'))
                            return;
                        var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                        var data = {groupId: currentGroup.id, custIds: []};
                        for (var i = 0; i < selectedList.length; i++) {
                            data.custIds.push(selectedList[i].id);
                        }
                        ngDialog.open({
                            template: 'maskTemplate',
                            className: 'ngdialog-theme-plain',
                            closeByEscape: false,
                            closeByDocument: false,
                            showClose: false
                        });
                        ReqServ.request('POST', 'ty/tyCustgroup/batchDeleteDetail', data).success(function (data) {
                            ngDialog.close();
                            if (data.httpCode != appInterface.successCode) {
                                $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                                return;
                            }
                            $scope.searchCustomer();
                        }).error(function (result) {
                            ngDialog.close();
                            $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                        })
                    }
                }]
            },
            controls: [{
                name: '查看', event: function (item) {
                    if ($scope.hasTaskRun($scope.customerInfo.loading))
                        return;
                    ReqServ.setStore('itemLook', 'true');
                    ReqServ.setStore('currentName', $state.current.name);
                    $state.go('master.portrait.search', {custId: item.source.id, index: item.index, from: currentName});
                }, isShow: true
            }, {
                name: '编辑', event: function (item) {
                    if ($scope.hasTaskRun($scope.customerInfo.loading))
                        return;
                    _item = item;
                    ReqServ.copyObj(_item.source, $scope.customerInfo);
                    $scope.customerInfo.labels = $scope.customerInfo.mark ? $scope.customerInfo.mark.split(' ') : [];
                    $scope.customerInfo.pageTitle = '编辑客户信息';
                    $scope.customerInfo.btnName = '保存';
                    ngDialog.open({
                        template: 'editCustomerTemplate',
                        className: 'ngdialog-theme-default',
                        onOpenCallback: $scope.editCustomerOpenedCallback,
                        scope: $scope
                    });
                }, isShow: true
            }, {
                name: '删除', event: function (item) {
                    if ($scope.hasTaskRun($scope.customerInfo.loading) || !window.confirm('确定要删除?'))
                        return;
                    $scope.customerInfo.loading = true;
                    ReqServ.request('POST', 'ty/tyOrgcustomer/delete', {id: item.source.id}).success(function (result) {
                        $scope.customerInfo.loading = false;
                        if (result.httpCode != appInterface.successCode) {
                            $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                            return;
                        }
                        $scope.searchCustomer();
                    }).error(function (result) {
                        $scope.customerInfo.loading = false;
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    });
                }, isShow: true
            }],
            heads: [{title: '客户名称', dataKey: 'custNameLink', width: 50},
                {title: '所属机构', dataKey: 'orgNameLink', width: 200},
                {title: '职位', dataKey: 'title', width: 90},
                {title: '研究行业', dataKey: 'industry', width: 150},
                {title: '手机号', dataKey: 'custMobile', width: 90},
                {title: '邮箱', dataKey: 'custEmail', width: 150},
                {title: '所在地', dataKey: 'area', width: 90},
                {title: '对口销售', dataKey: 'serviceSaler', width: 80},
                {title: '状态', dataKey: 'custStatusName', width: 90},
                {title: '白名单到期日', dataKey: 'whiteDeadline', width: 150},
                {title: '标签', dataKey: 'mark', width: 150},
                {title: '创建时间', dataKey: 'createTime', width: 120},
                {title: '更新时间', dataKey: 'updateTime', width: 120}],
            checkFunc: function (customer) {
                var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                if (customer.hasCheckBox.isChecked) {
                    selectedList.push({
                        id: customer.source.id,
                        custName: customer.source.custName,
                        custEmail: customer.source.custEmail
                    });
                } else {
                    for (var i = 0; i < selectedList.length; i++) {
                        if (selectedList[i].id == customer.source.id) {
                            selectedList.splice(i, 1);
                            break;
                        }
                    }
                    $scope.setSelectAll(false);
                }
                $scope.setSelectedCount(selectedList.length);
                if (selectedList.length <= 0)
                    ReqServ.removeStore('selectedItem');
                else
                    ReqServ.setStore('selectedItem', JSON.stringify(selectedList));
                dataModel.menu.isShow = showMenu();
                $scope.disabled();
            },
            selectAll: function (data, isAll) {
                var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                ngDialog.open({
                    template: 'maskTemplate',
                    className: 'ngdialog-theme-plain',
                    closeByEscape: false,
                    closeByDocument: false,
                    showClose: false
                });
                ReqServ.request('POST', 'ty/tyOrgcustomer/read/allList', $scope.customer).success(function (result) {
                    ngDialog.close();
                    if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                        return;
                    }
                    var i;
                    for (i = 0; i < data.length; i++) {
                        data[i].hasCheckBox.isChecked = isAll;
                    }
                    if (isAll) {
                        if (selectedList.length > 0) {
                            for (i = 0; i < result.data.length; i++) {
                                var isExists = false;
                                for (var j = 0; j < selectedList.length; j++) {
                                    if (selectedList[j].id == result.data[i].id) {
                                        isExists = true;
                                        break;
                                    }
                                }
                                if (!isExists)
                                    selectedList.push({
                                        id: result.data[i].id,
                                        custName: result.data[i].custName,
                                        custEmail: result.data[i].custEmail
                                    });
                            }
                        } else {
                            for (i = 0; i < result.data.length; i++) {
                                selectedList.push({
                                    id: result.data[i].id,
                                    custName: result.data[i].custName,
                                    custEmail: result.data[i].custEmail
                                });
                            }
                        }
                    } else {
                        for (i = 0; i < result.data.length; i++) {
                            for (var j = 0; j < selectedList.length; j++) {
                                if (selectedList[j].id == result.data[i].id) {
                                    selectedList.splice(j, 1);
                                    break;
                                }
                            }
                        }
                    }
                    $scope.setSelectedCount(selectedList.length);
                    if (selectedList.length <= 0)
                        ReqServ.removeStore('selectedItem');
                    else
                        ReqServ.setStore('selectedItem', JSON.stringify(selectedList));
                    dataModel.menu.isShow = showMenu();
                }).error(function (result) {
                    ngDialog.close();
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                });
            },
            isScrollLoaded: true,
            controlWidth: 120
        };
        var countermarkData=function(){
            $scope.custData1=[]
            $scope.custData2=[]
            $scope.custData3=[]
            $scope.custData4=[]
            $scope.custFilterMarks=[]
            if($scope.tagList1.length!=0){
                var getSubLabels={
                    labelIds:$scope.tagList1
                }
                ReqServ.request('POST', 'ty/tyLabel/getSubLabelsAndBindLabels', getSubLabels).success(function (result) {
                    if (result.httpCode != appInterface.successCode) {
                        $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    }else{
                        $scope.markData=result.data.subLabels
                        $scope.markData1=result.data.bindLabels
                        for(var i=0;i<$scope.markData.length;i++){
                            $scope.markData[i].check=true
                            $scope.custData1.push($scope.markData[i].labelName)
                        }
                        for(var j=0;j<$scope.markData1.length;j++){
                            $scope.markData1[j].check=true
                            $scope.custData2.push($scope.markData1[j].labelName)
                        }
                        for(var k=0;k<$scope.tagData.length;k++){
                            $scope.tagData[k].check=true
                            $scope.custData3.push($scope.tagData[k].text)
                        }
                        $scope.custData4=$scope.custData1.concat($scope.custData2.concat($scope.custData3))
                        $scope.custFilterMarks=$scope.custData4.join(',');
                        console.log($scope.custFilterMarks)
                    }
                }).error(function (result) {

                });
            }
        }
        $scope.countermarkChartOpenedCallback = function () {
            if($scope.looktag==true){
                console.log($scope.tagList.join(','),ReqServ.getStore('tagList').join(','))
                if($scope.tagList.join(',')!=ReqServ.getStore('tagList').join(',')){
                   $scope.looktag=false
                   $scope.tagList1=[]
                   for(var i=0;i<$scope.tagData.length;i++){
                    console.log($scope.tagData[i])
                        $scope.tagList1.push($scope.tagData[i].id)
                    }
                    countermarkData()
                }else{
                    $scope.markData=ReqServ.getStore('markData')
                    $scope.markData1=ReqServ.getStore('markData1')
                    $scope.tagData=ReqServ.getStore('tagData');
                    console.log(ReqServ.getStore('tagData'))
                }

            }else{
                console.log($scope.tagList.join(','),$scope.lastLabelName1.join(','))
                if($scope.lastLabelName1.join(',')==$scope.tagList.join(',')){
                    $scope.markData=ReqServ.getStore('markData')
                    $scope.markData1=ReqServ.getStore('markData1')
                    $scope.tagData=ReqServ.getStore('tagData');
                    console.log(ReqServ.getStore('tagData'))
                }else{
                    console.log(ReqServ.getStore('tagData'))

                    $scope.tagList1=[]
                    for(var i=0;i<$scope.tagData.length;i++){
                        console.log($scope.tagData[i])
                        $scope.tagList1.push($scope.tagData[i].id)
                    }
                    countermarkData()
                }
            }
        };
        $scope.clearmark= function (){
            ngDialog.close();
            $scope.customer.markCal = 'or';
            for(var i=0;i<$scope.markData.length;i++){
                $scope.markData[i].check=true
            }
            for(var j=0;j<$scope.markData1.length;j++){
                $scope.markData1[j].check=true
            }
            for(var o=0;o<$scope.tagData.length;o++){
                $scope.tagData[o].check=true
            }
            ReqServ.setStore('markData',$scope.markData)
            ReqServ.setStore('markData1',$scope.markData1)
            ReqServ.setStore('tagData',$scope.tagData)
            ReqServ.removeStore('data');
            $scope.customer.pageNum = 1;
            $scope.clearData();
            $scope.setSelectAll(false);
            $scope.customer.marks = $scope.tagList.join(',');
            $scope.customer.filterMarks = ''
            ReqServ.copyObj($scope.customer, $scope.searchModel);
            loadData();
        }
        $scope.marksub= function (){
            ngDialog.close();
            $scope.lastLabelName1=[]
            for(var k=0;k<$scope.tagList.length;k++){
                $scope.lastLabelName1.push($scope.tagList[k])
            }
            $scope.tagList2=[]
            $scope.tagList3=[]
            $scope.tagData1=[]
            for(var i=0;i<$scope.markData.length;i++){
                if($scope.markData[i].check==true){
                    $scope.tagList2.push($scope.markData[i].labelName)
                }
            }
            for(var j=0;j<$scope.markData1.length;j++){
                if($scope.markData1[j].check==true){
                    $scope.tagList3.push($scope.markData1[j].labelName)
                }
            }
            for(var o=0;o<$scope.tagData.length;o++){
                if($scope.tagData[o].check==true){
                    $scope.tagData1.push($scope.tagData[o].text)
                }
            }
            ReqServ.setStore('markData',$scope.markData)
            ReqServ.setStore('markData1',$scope.markData1)
            ReqServ.setStore('tagData',$scope.tagData)
            ReqServ.setStore('tagList',$scope.tagList)
            $scope.tagList4=$scope.tagData1.concat($scope.tagList2.concat($scope.tagList3))
            ReqServ.removeStore('data');
            $scope.customer.pageNum = 1;
            $scope.clearData();
            $scope.setSelectAll(false);
            $scope.customer.marks = $scope.tagList.join(',');
            $scope.customer.filterMarks = $scope.tagList4.join(',');
            ReqServ.copyObj($scope.customer, $scope.searchModel);
            loadData();
        }
        var showMenu = function () {
            var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
            if (dataModel.data) {
                for (var j = 0; j < selectedList.length; j++) {
                    for (var i = 0; i < dataModel.data.length; i++) {
                        if (selectedList[j].id == dataModel.data[i].id) {
                            return currentGroup.id !== '0';
                        }
                    }
                }
            }
            return false;
        };
        var setSelectedItem = function () {
            var isShow = false;
            var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
            if (dataModel.data) {
                for (var j = 0; j < selectedList.length; j++) {
                    for (var i = 0; i < dataModel.data.length; i++) {
                        if (selectedList[j].id == dataModel.data[i].id) {
                            dataModel.data[i].isChecked = true;
                            isShow = currentGroup.id !== '0';
                            break;
                        }
                    }
                }
            }
            dataModel.menu.isShow = isShow;
            $scope.setSelectedCount(selectedList.length);
        };
        var loadLocalData = function () {
            $scope.loading.isShow = true;
            $scope.clearData();
            ReqServ.copyObj(JSON.parse(ReqServ.getStore('searchModel')), $scope.searchModel);
            ReqServ.copyObj($scope.searchModel, $scope.customer);
            $scope.tagList = $scope.customer.marks ? $scope.customer.marks.split(',') : [];
            $scope.searchT.more = $scope.tagList.length > 0;
            $scope.searchOrgs = JSON.parse(ReqServ.getStore('searchOrgs'));
            $scope.searchArea = JSON.parse(ReqServ.getStore('searchArea'));
            $scope.searchStafflist = JSON.parse(ReqServ.getStore('searchStafflist'));
            $scope.searchIndustry = JSON.parse(ReqServ.getStore('searchIndustry'));
            $scope.searchIsAcceptRpt = JSON.parse(ReqServ.getStore('searchIsAcceptRpt'));
            $scope.searchRptSendGroup = JSON.parse(ReqServ.getStore('searchRptSendGroup'));
            $scope.groups = JSON.parse(ReqServ.getStore('groups'));
            for (var i = 0; i < $scope.groups.length; i++) {
                if ($scope.groups[i].active) {
                    currentGroup = $scope.groups[i];
                    break;
                }
            }
            dataModel.pageInfo = JSON.parse(ReqServ.getStore('pageInfo'));
            dataModel.data = JSON.parse(ReqServ.getStore('data'));
            dataModel.menu.isEnable = dataModel.toolsBar[dataModel.toolsBar.length - 1].isHide = dataModel.toolsBar[dataModel.toolsBar.length - 2].isHide = $scope.groupTools.isShow = currentGroup.id !== '0';
            setSelectedItem();
            $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
            $scope.disabled();
        };
        var loadGroup = function () {
            $scope.groups = [{id: '0', custGroupname: '全部', active: true, isEditable: false}];
            currentGroup = $scope.groups[0];
            ReqServ.request('POST', 'ty/tyCustgroup/read/list', null).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
                    return;
                }
                for (var i = 0; i < result.data.list.length; i++) {
                    result.data.list[i].active = false;
                    result.data.list[i].isEditable = false;
                    $scope.groups.push(result.data.list[i]);
                }
            }).error(function (result) {
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            })
        };
        var loadData = function () {
            $scope.customer.pageSize = $scope.searchModel.pageSize;
            if(ReqServ.getStore('itemLook')=='true'){
                $scope.look=true
                $scope.looktag=true
                ReqServ.removeStore('itemLook');
                $scope.customer=ReqServ.getStore('customer')
                if($scope.customer.groupId==undefined||$scope.customer.groupId==''){

                }else{
                    setTimeout(function(){
                       for (var i = 0; i < $scope.groups.length; i++) {
                            if($scope.groups[i].id==$scope.customer.groupId){
                                $scope.groups[i].active = true;
                            }else{
                                $scope.groups[i].active = false;
                            }
                        }
                   },1000)
                }
                if($scope.customer.marks==undefined||$scope.customer.marks==''){
                    $scope.searchT.more=false
                }else{
                    $scope.searchT.more=true
                    $scope.tagList=ReqServ.getStore('tagList')
                }
            }
            $scope.loading.isShow = true;
            ReqServ.removeStore('customer');
            ReqServ.setStore('customer',$scope.customer)
            ReqServ.request('POST', 'ty/tyOrgcustomer/read/list', $scope.customer).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.loading.isShow = false;
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if (!$scope.searchOrgs) {
                    $scope.searchOrgs = result.dicts['Serviceorgs'];
                    $scope.searchArea = result.dicts['SysAreas'];
                    $scope.searchStafflist = result.dicts['TyStafflists'];
                    $scope.searchIndustry = result.dicts['industry'];
                    $scope.searchIsAcceptRpt = result.dicts['isAcceptRpt'];
                    $scope.searchRptSendGroup = result.dicts['rptSendGroup'];
                    ReqServ.removeStore('searchOrgs');
                    ReqServ.setStore('searchOrgs', JSON.stringify($scope.searchOrgs));
                    ReqServ.removeStore('searchArea');
                    ReqServ.setStore('searchArea', JSON.stringify($scope.searchArea));
                    ReqServ.removeStore('searchStafflist');
                    ReqServ.setStore('searchStafflist', JSON.stringify($scope.searchStafflist));
                    ReqServ.removeStore('searchIndustry');
                    ReqServ.setStore('searchIndustry', JSON.stringify($scope.searchIndustry));
                    ReqServ.removeStore('searchIsAcceptRpt');
                    ReqServ.setStore('searchIsAcceptRpt', JSON.stringify($scope.searchIsAcceptRpt));
                    ReqServ.removeStore('searchRptSendGroup');
                    ReqServ.setStore('searchRptSendGroup', JSON.stringify($scope.searchRptSendGroup));
                }
                result.data.list.forEach(function (value, index) {
                    value.whiteDeadline = ReqServ.getDateString(value.whiteDeadline + '');
                    value.custNameLink = '<a class="text-cyan-light" href="#/master/portrait/search?custId=' + value.id + '&index=' + index + '&from=' + currentName + '">' + value.custName + '</a>';
                    value.orgNameLink = '<a class="text-cyan-light" href="#/master/institutionaldetail?id=' + value.orgId + '&index=' + index + '&from=' + currentName + '">' + value.orgName + '</a>';
                });
                dataModel.data = result.data.list;
                dataModel.pageInfo = {size: result.data.size, total: result.data.total, pages: result.data.pages};
                if($scope.first==''){
                    if($scope.look==true){
                        $scope.look=false
                        $scope.sub($scope.customer.pageNum)
                    }else{
                        $scope.sub(1)
                    }
                }
                ReqServ.removeStore('searchModel');
                ReqServ.setStore('searchModel', JSON.stringify($scope.searchModel));
                ReqServ.removeStore('groups');
                ReqServ.setStore('groups', JSON.stringify($scope.groups));
                ReqServ.removeStore('pageInfo');
                ReqServ.setStore('pageInfo', JSON.stringify(dataModel.pageInfo));
                if (ReqServ.getStore('data')) {
                    var _data = JSON.parse(ReqServ.getStore('data'));
                    ReqServ.removeStore('data');
                    ReqServ.setStore('data', JSON.stringify(_data.concat(dataModel.data)));
                } else
                    ReqServ.setStore('data', JSON.stringify(dataModel.data));
                if ($scope.isSelectedAll()) {
                    var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
                    for (var i = 0; i < dataModel.data.length; i++) {
                        var isExists = false;
                        for (var j = 0; j < selectedList.length; j++) {
                            if (selectedList[j].id == dataModel.data[i].id) {
                                isExists = true;
                                break;
                            }
                        }
                        if (!isExists)
                            selectedList.push({
                                id: dataModel.data[i].id,
                                custName: dataModel.data[i].custName,
                                custEmail: dataModel.data[i].custEmail
                            });
                    }
                    ReqServ.setStore('selectedItem', JSON.stringify(selectedList));
                }
                setSelectedItem();
                $scope.clearData()
                $scope.loading.isShow = $scope.rebuildData(dataModel, currentName);
                $scope.disabled();
            }).error(function (result) {
                $scope.loading.isShow = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        function setPage(length, amount, num, first) {//创建保存页码数组的函数
             //length数据总条数
             //amount每页数据条数
             //num保留的页码数
             //first第一页的页码
             var pages = []; //创建分页数组
             var page = Math.ceil(length / amount);
             if (page <= num) {
               for (var i = 1; i <= page; i++) {
                 pages.push(i);
               }
             }
             if (page > num) {
               for (var i = first; i < first + num; i++) {
                 pages.push(i);
               }
             }
             return pages;
        }

        $scope.firstPage = 1;
        $scope.pageNum = 5;
        $scope.page = 1;
        var amount = 100;//数据总条数
        var each = 15;//每页显示的条数
        $scope.sub = function(page) {
           amount=dataModel.pageInfo.total
           each=dataModel.pageInfo.size
           $scope.lastPage = Math.ceil(amount / each);
           if (page >= $scope.pageNum) {
             $scope.firstPage = page - Math.floor($scope.pageNum / 2);
           } else {
             $scope.firstPage = 1;
           }
           if ($scope.firstPage > $scope.lastPage - $scope.pageNum) {
             $scope.firstPage = $scope.lastPage - $scope.pageNum + 1;
           }
           $scope.pages = setPage(amount, each, $scope.pageNum, $scope.firstPage);
           $scope.page = page;
           if($scope.first==1){
                $scope.customer.pageNum = page;
                $scope.clearData();
                $scope.setSelectAll(false);
                $scope.customer.marks = $scope.tagList.join(',');
                ReqServ.copyObj($scope.customer, $scope.searchModel);
                loadData();
            }
            $scope.first=1
        }
        $scope.changePageSize= function(data) {
            if(data=='refresh'){
                $scope.clearData();
                loadData();
            }else{
                $scope.clearData();
                $scope.setSelectAll(false);
                $scope.customer.marks = $scope.tagList.join(',');
                ReqServ.copyObj($scope.customer, $scope.searchModel);
                $scope.customer.pageSize = $scope.searchModel.pageSize
                $scope.customer.pageNum = 1;
                $scope.first=''
                loadData();
            }
        }
        var showTemplate = function (isAdd, templateId) {
            ReqServ.clearObj($scope.newGroup);
            if ($scope.messageInfo.isAll)
                $scope.messageInfo.total = dataModel.pageInfo.total;
            $scope.newGroup.groups = [];
            $scope.newGroup.new = isAdd;
            $scope.newGroup.loading = false;
            $scope.newGroup.btnName = '确定';
            for (var i = 0; i < $scope.groups.length; i++) {
                if ($scope.groups[i].id != '0') {
                    if (!isAdd && $scope.groups[i].id == currentGroup.id)
                        continue;
                    $scope.newGroup.groups.push({
                        id: $scope.groups[i].id,
                        custGroupname: $scope.groups[i].custGroupname,
                        active: false
                    })
                }
            }
            ngDialog.open({
                template: templateId,
                className: 'ngdialog-theme-plain',
                scope: $scope
            });
        };
        // $scope.$on('loadPageData', function (evt, data) {
        //     ReqServ.clearObj($scope.customer);
        //     ReqServ.copyObj($scope.searchModel, $scope.customer);
        //     if (data) {
        //         ReqServ.removeStore('data');
        //         ReqServ.removeStore('selectedItem');
        //         $scope.setSelectAll(false);
        //         setSelectedItem();
        //         $scope.disabled();
        //     }
        //     loadData();
        // });
        $scope.searchCustomer = function () {
            ReqServ.removeStore('data');
            $scope.customer.pageNum = 1;
            $scope.clearData();
            $scope.setSelectAll(false);
            $scope.customer.marks = $scope.tagList.join(',');
            ReqServ.copyObj($scope.customer, $scope.searchModel);
            $scope.first=''
            loadData();
        };
        $scope.editCustomer = function () {
            if($scope.customerInfo.isAcceptRpt=='1' &&
                ($scope.customerInfo.rptSendGroupName==null||$scope.customerInfo.rptSendGroupName=='')){
                alert("接受报告时【配置报告发送组】字段不能为空！");
                return;
            }
            ReqServ.setBtnStatus(true, '处理中...', null, $scope.customerInfo);
            $scope.customerInfo.recEmail = $scope.customerInfo.recEmail ? 1 : 0;
            $scope.customerInfo.recSms = $scope.customerInfo.recSms ? 1 : 0;
            $scope.customerInfo.mark = ($scope.customerInfo.labels && $scope.customerInfo.labels.length > 0) ? $scope.customerInfo.labels.join(' ') : null;
            ReqServ.request('POST', $scope.customerInfo.id ? 'ty/tyOrgcustomer/update' : 'ty/tyOrgcustomer/add', $scope.customerInfo).success(function (result) {
                ReqServ.setBtnStatus(false, $scope.customerInfo.id ? '保存' : '添加', null, $scope.customerInfo);
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.customerInfo.id) {
                    ReqServ.getTextValue($scope.servieSalers, $scope.customerInfo, 'salerId', 'serviceSaler');
                    $scope.customerInfo.updateTime = result.data.updateTime;
                    ReqServ.copyObj($scope.customerInfo, _item.source);
                    _item.item = [{text: '<a class="text-cyan-light" href="#/master/portrait/search?custId=' + _item.source.id + '&index=' + _item.index + '&from=' + currentName + ')">' + _item.source.custName + '</a>'},
                        {text: '<a class="text-cyan-light" href="#/master/institutionaldetail?id=' + _item.source.orgId + '&index=' + _item.index + '&from=' + currentName + ')">' + _item.source.orgName + '</a>'},
                        {text: _item.source.title},
                        {text: _item.source.industry ? _item.source.industry : '-'},
                        {text: _item.source.custMobile},
                        {text: _item.source.custEmail},
                        {text: _item.source.area ? _item.source.area : '-'},
                        {text: _item.source.serviceSaler},
                        {text: _item.source.custStatusName ? _item.source.custStatusName : '-'},
                        {text: _item.source.whiteDeadline ? _item.source.whiteDeadline : '-'},
                        {text: _item.source.mark ? _item.source.mark : '-'},
                        {text: _item.source.createTime},
                        {text: _item.source.updateTime}];
                    ReqServ.removeStore('data');
                    ReqServ.setStore('data', JSON.stringify(dataModel.data));
                    ngDialog.close();
                    return;
                }
                ngDialog.close();
                $scope.clearData();
                loadData();
            }).error(function (result) {
                ReqServ.setBtnStatus(false, $scope.customerInfo.id ? '保存' : '添加', null, $scope.customerInfo);
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            });
        };
        $scope.editCustomerOpenedCallback = function () {
            function init() {
                $scope.servieSalers.length = 0;
                for (var i = 0; i < $scope.tyServiceorgs.length; i++) {
                    if ($scope.customerInfo.id && $scope.tyServiceorgs[i].id == $scope.customerInfo.orgId) {
                        var list = $scope.tyServiceorgs[i].remark.split(',');
                        for (var j = 0; j < list.length; j++) {
                            for (var m = 0; m < $scope.searchStafflist.length; m++) {
                                $scope.searchStafflist[m].selected = $scope.searchStafflist[m].id == $scope.customerInfo.salerId;
                                if ($scope.searchStafflist[m].id == list[j]) {
                                    $scope.servieSalers.push($scope.searchStafflist[m]);
                                }
                            }
                        }
                    }
                }
            }

            $scope.customerInfo.recEmail = $scope.customerInfo.recEmail == '1';
            $scope.customerInfo.recSms = $scope.customerInfo.recSms == '1';
            if ($scope.tyServiceorgs) {
                init();
                return;
            }
            ReqServ.request('POST', 'ty/tyOrgcustomer/read/detail', {id: $scope.customerInfo.id}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.tyServiceorgs = [];
                    return;
                }
                $scope.tyServiceorgs = result.dicts['TyServiceorgs'];
                init();
            }).error(function () {
                $scope.tyServiceorgs = [];
            })
        };
        $scope.removeMark = function (index) {
            $scope.customerInfo.labels.splice(index, 1);
        };
        $scope.removeTag = function (index) {
            $scope.tagList.splice(index, 1);
            $scope.tagData.splice(index, 1);
        };
        $scope.setSaler = function (data) {
            /* 保证编辑时不会点击机构选择框，对口销售销售 */
            if(_tempUpdateCustomerOrgName == ''){
                _tempUpdateCustomerOrgName = $scope.customerInfo.orgName
            }

            if(_tempUpdateCustomerOrgName == $scope.customerInfo.orgName && data == null){
                return;
            }
            /****************************************/


            $scope.servieSalers.length = 0;
            if (data) {
                var list = data.remark.split(',');
                for (var j = 0; j < list.length; j++) {
                    for (var i = 0; i < $scope.searchStafflist.length; i++) {
                        if ($scope.searchStafflist[i].id == list[j])
                            $scope.servieSalers.push($scope.searchStafflist[i]);
                    }
                }
                if ($scope.servieSalers.length > 0) {
                    $scope.servieSalers[0].selected = true;
                    $scope.customerInfo.salerId = $scope.servieSalers[0].id;
                }
            } else
                $scope.customerInfo.salerId = null;
        };
        $scope.searchTag = function (event) {
            if (typeof event == 'function')
                $scope.searchLabel($scope.customer.mark, function (data) {
                    $scope.searchTags.length = 0;
                    for (var i = 0; i < data.length; i++) {
                        $scope.searchTags.push({text: data[i].labelName,id: data[i].id});
                    }
                    event();
                });
            else {
                $scope.customer.mark = null;
                if ($scope.tagList.join(',').indexOf(event.text) >= 0)
                    return;

                $scope.tagList.push(event.text);
                $scope.tagData.push({text:event.text,id:event.id,check:true});
            }
        };
        $scope.showGroup = function (group) {
            $scope.tagData=[]
            if (currentGroup.isEditable)
                return;
            currentGroup = group;
            ReqServ.clearObj($scope.customer);
            $scope.tagList.length = 0;
            $scope.searchT.more = false;
            $scope.customer.groupId = currentGroup.id !== '0' ? currentGroup.id : null;
            $scope.searchCustomer();
            for (var i = 0; i < $scope.groups.length; i++) {
                $scope.groups[i].active = false;
            }
            group.active = true;
            dataModel.menu.isEnable = dataModel.toolsBar[dataModel.toolsBar.length - 1].isHide = dataModel.toolsBar[dataModel.toolsBar.length - 2].isHide = $scope.groupTools.isShow = group.id !== '0';
        };
        $scope.editGroup = function () {
            currentGroup.isEditable = true;
            $scope.groupTools.isEdit = false;
            $timeout(function () {
                $('.groups').find("li[contenteditable='true']").focus();
            }, 300);
        };
        $scope.deleteGroup = function () {
            if ($scope.hasTaskRun($scope.newGroup.loading))
                return;
            if (!confirm('确定要删除吗？'))
                return;
            $scope.groupTools.isSubmit = true;
            ngDialog.open({
                template: 'maskTemplate',
                className: 'ngdialog-theme-plain',
                closeByEscape: false,
                closeByDocument: false,
                showClose: false
            });
            ReqServ.request('POST', 'ty/tyCustgroup/delete', {id: currentGroup.id}).success(function (data) {
                ngDialog.close();
                $scope.groupTools.isSubmit = false;
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                for (var i = 0; i < $scope.groups.length; i++) {
                    if ($scope.groups[i].id == currentGroup.id) {
                        $scope.groups.splice(i, 1);
                        break;
                    }
                }
                $scope.showGroup($scope.groups[0]);
            }).error(function (result) {
                ngDialog.close();
                $scope.groupTools.isSubmit = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            })
        };
        $scope.saveGroup = function () {
            currentGroup.custGroupname = $('.groups').find("li[contenteditable='true']").eq(0).text();
            $scope.groupTools.isSubmit = true;
            ReqServ.request('POST', 'ty/tyCustgroup/update', currentGroup).success(function (data) {
                $scope.groupTools.isSubmit = false;
                if (data.httpCode != appInterface.successCode) {
                    $scope.showAlert(data.msg ? data.msg : appInterface.alert);
                    return;
                }
                $scope.cancelGroup();
            }).error(function (result) {
                $scope.groupTools.isSubmit = false;
                $scope.showAlert((result && result.msg) ? result.msg : appInterface.alert);
            })
        };
        $scope.cancelGroup = function () {
            $('.groups').find("li[contenteditable='true']").eq(0).text(currentGroup.custGroupname);
            currentGroup.isEditable = false;
            $scope.groupTools.isEdit = true;
        };
        $scope.addGroup = function () {
            var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
            var data = {custIds: []};
            if ($scope.messageInfo.isAll) {
                ReqServ.copyObj($scope.searchModel, data);
                data.isAll = $scope.messageInfo.isAll;
            } else {
                for (var i = 0; i < selectedList.length; i++) {
                    data.custIds.push(selectedList[i].id);
                }
            }
            if ($scope.newGroup.new)
                data.groupName = $scope.newGroup.custGroupname;
            else
                data.groupId = $scope.newGroup.selectedGroup.id;
            ReqServ.setBtnStatus(true, '提交中...', null, $scope.newGroup);
            ReqServ.request('POST', 'ty/tyCustgroup/batchUpdateGroupDetail', data).success(function (result) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.newGroup);
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                if ($scope.newGroup.new) {
                    result.data.active = false;
                    result.data.isEditable = false;
                    $scope.groups.push(result.data);
                }
                ngDialog.close();
                $scope.showSuccess('完成添加分组！');
            }).error(function (restult) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.newGroup);
                $scope.showAlert((restult && restult.msg) ? restult.msg : appInterface.alert);
            })
        };
        $scope.moveGroup = function () {
            var selectedList = ReqServ.getStore('selectedItem') ? JSON.parse(ReqServ.getStore('selectedItem')) : [];
            var data = {custIds: [], srcGroupId: currentGroup.id, destGroupId: $scope.newGroup.selectedGroup.id};
            for (var i = 0; i < selectedList.length; i++) {
                data.custIds.push(selectedList[i].id);
            }
            ReqServ.setBtnStatus(true, '提交中...', null, $scope.newGroup);
            ReqServ.request('POST', 'ty/tyCustgroup/batchRemoveDetail', data).success(function (result) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.newGroup);
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert(result.msg ? result.msg : appInterface.alert);
                    return;
                }
                ngDialog.close();
                $scope.searchCustomer();
            }).error(function (restult) {
                ReqServ.setBtnStatus(false, '确定', null, $scope.newGroup);
                $scope.showAlert((restult && restult.msg) ? restult.msg : appInterface.alert);
            })
        };
        $scope.setGroup = function (group) {
            if ($scope.newGroup.new)
                return;
            for (var i = 0; i < $scope.newGroup.groups.length; i++) {
                $scope.newGroup.groups[i].active = false;
            }
            group.active = true;
            $scope.newGroup.selectedGroup = group;
        };
        $scope.keyup = function (e) {
            var pattern = /^(\w|[\u4e00-\u9fa5]){1,8}$/;
            $scope.groupTools.disabled = !pattern.test($(e.target)[0].innerText);
        };
        $scope.reportChartOpenedCallback = function () {
            width = $('.ngdialog-message').width();
            $('#report_chart').width(width).height(parseFloat(width) / 2.5);
            reportChart = echarts.init($('#report_chart').get(0));
            $scope.tabReport($scope.reportMenu[0].name);
        };
        $scope.tabReport = function (menu) {
            $scope.tagList1=[]
            for(var k=0;k<$scope.tagData.length;k++){
                $scope.tagList1.push($scope.tagData[k].id)
            }
            countermarkData()
            setTimeout(function(){
                console.log($scope.custFilterMarks)
                if($scope.searchModel.filterMarks==undefined){
                    $scope.searchModel.filterMarks=$scope.custFilterMarks
                }
                if($scope.searchModel.filterMarks!=undefined&&$scope.searchModel.filterMarks.length==0){
                    console.log($scope.searchModel.filterMarks==[])
                    $scope.searchModel.filterMarks=$scope.custFilterMarks
                }
                console.log($scope.searchModel.filterMarks)
                $scope.searchModel.markCal = 'or';
                $scope.reportMenu.forEach(function (value) {
                    value.active = value.name == menu;
                });
                switch (menu) {
                    case '相关标签':
                        reqUrl = 'ty/tyReport/read/custLabelDist';
                        chartOption = pieOption;
                        break;
                    case '地域分布':
                        reqUrl = 'ty/tyReport/read/custAreaDist';
                        chartOption = pieOption;
                        break;
                    case '销售潜力分布':
                        reqUrl = 'ty/tyReport/read/custSaleDist';
                        chartOption = pieOption;
                        break;
                    case '行业分布':
                        reqUrl = 'ty/tyReport/read/custTradeDist';
                        chartOption = pieOption;
                        break;
                    case '活跃趋势':
                        reqUrl = 'ty/tyReport/read/custActiveTend';
                        chartOption = barOption;
                        break;
                    case '活动喜好分布':
                        reqUrl = 'ty/tyReport/read/activityLike';
                        chartOption = pieOption;
                        break;
                }
                reportChart.showLoading();
                ReqServ.request('POST', reqUrl, $scope.searchModel).success(function (result) {
                    reportChart.hideLoading();
                    if (result.data) {
                        if (menu == '活跃趋势') {
                            chartOption.series[0].data.length = chartOption.series[1].data.length = 0;
                            chartOption.xAxis[0].data.length = 0;
                            result.data.forEach(function (value) {
                                chartOption.xAxis[0].data.push(value.groupName);
                                chartOption.series[0].data.push(value.activeCustNum);
                                chartOption.series[1].data.push(value.noactiveCustNum);
                            });
                        } else {
                            chartOption.legend.data.length = 0;
                            chartOption.series[0].data.length = 0;
                            result.data.forEach(function (value) {
                                chartOption.legend.data.push(value.groupName);
                                chartOption.series[0].data.push({value: value.groupNum, name: value.groupName})
                            });
                        }
                        reportChart.clear();
                        reportChart.setOption(chartOption);
                    }
                }).error(function () {
                    reportChart.hideLoading();
                });
            },1000)
        };
        $scope.showMore = function () {
            $scope.searchT.more = !$scope.searchT.more;
            if (!$scope.searchT.more)
                $scope.tagList.length = 0;
        };
        $scope.prepCust = function () {
            if ($scope.loading.isShow)
                return;
            $state.go('master.meetingcust');
        };
        if ($stateParams.index) {
            loadLocalData();
            $timeout(function () {
                var tr = $('.data').find('tr').eq(parseInt($stateParams.index) + appInterface.add);
                tr.addClass('active');
                $(document).scrollTop(tr.position().top - appInterface.height);
            }, 500);
            return;
        }
        ReqServ.removeStore('data');
        ReqServ.removeStore('selectedItem');
        $scope.clearData();
        $scope.setSelectAll(false);
        loadGroup();
        loadData();
    }])
});

