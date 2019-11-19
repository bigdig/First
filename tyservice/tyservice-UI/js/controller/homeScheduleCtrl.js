define(['app'], function (app) {
    app.controller('homeScheduleCtrl', ['$scope', '$timeout', 'ReqServ', 'appInterface', function ($scope, $timeout, ReqServ, appInterface) {
        $scope.welcome.isHide = true;
        $scope.currentDate = new Date().toDateString();
        if (!$scope.tabMenus[2].active)
            $scope.tabMenus[2].active = true;
        var myApplyTooltip = function (divElm, agendaItem) {
            // Destroy currrent tooltip if present
            if (divElm.data("qtip"))
                divElm.qtip("destroy");
            var displayData = "";
            var data = agendaItem.data;
            var startDate = ReqServ.dateFormat(agendaItem.startDate, 'yyyy-MM-dd');
            displayData += '<p class="text-blue-lit" style="text-indent: 5px; border-left: solid 4px ' + data.color + '">' + startDate + ' ';
            if (data.activityType == '1')
                displayData += '<span class="text-gray">' + ReqServ.getTimeString(data.beginTime) + '-' + ReqServ.getTimeString(data.endTime) + '</p>';
            else
                displayData += '</p>';
            displayData += '<p>服务内容：<br>' + data.content + '</p>';
            displayData += '服务来源：' + data.activityTypeText;
            // use the user specified colors from the agenda item.
            var myStyle = {
                border: {
                    width: 1,
                    radius: 2,
                    color: '#eaeaea'
                },
                backgroundColor: '#ffffff',
                color: '#666666',
                padding: 10,
                textAlign: "left",
                tip: true
            };
            // apply tooltip
            divElm.qtip({
                content: displayData,
                position: {
                    corner: {
                        tooltip: "bottomMiddle",
                        target: "topMiddle"
                    },
                    adjust: {
                        mouse: true,
                        x: 0,
                        y: -15
                    },
                    target: "mouse"
                },
                show: {
                    when: {
                        event: 'mouseover'
                    }
                },
                style: myStyle
            });

        };
        var getActivityTypeColor = function (activityType) {
            var color;
            switch (activityType) {
                case '0':
                    color = '#FB8967';
                    break;
                case '1':
                    color = '#5686FF';
                    break;
                case '2':
                    color = '#74D20D';
                    break;
                case '3':
                    color = '#806FBD';
                    break;
                case '4':
                    color = '#f77412';
                    break;
                case '5':
                    color = '#FECB2F';
                    break;
                case '6':
                    color = '#FC297D';
                    break;
                case '7':
                    color = '#00F5FF';
                    break;
                case '8':
                    color = '#47A6BF';
                    break;
                default:
                    color = '#4D5AFF';
                    break;
            }
            return color;
        };
        var getCurrentDate = function (dateNum) {
            var beginDate = ReqServ.getDateString(dateNum + '').split('-');
            var year = parseInt(beginDate[0]);
            var month = parseInt(beginDate[1]) - 1;
            var day = parseInt(beginDate[2]);
            return new Date(year, month, day);
        };
        var loadData = function (plugin, month) {
            ReqServ.request('POST', 'ty/tyReport/read/activitySchedule', {acticityMonth: month}).success(function (result) {
                if (result.httpCode != appInterface.successCode) {
                    $scope.showAlert((result && result.message) ? result.message : appInterface.alert);
                    return;
                }
                if (result.data && result.data.length)
                    result.data.forEach(function (value) {
                        var date = getCurrentDate(value.beginDate);
                        var color = getActivityTypeColor(value.activityType);
                        if (date)
                            plugin.addAgendaItem("#mycal", '&nbsp;' + value.activityTypeText, date, date, value.activityType != '1', {
                                activityType: value.activityType,
                                content: value.title ? value.title : '',
                                activityTypeText: value.activityTypeText,
                                beginTime: value.beginTime,
                                endTime: value.endTime,
                                color: color
                            }, {
                                backgroundColor: color,
                                foregroundColor: "#FFFFFF"
                            })

                    });
            }).error(function (result) {
                $scope.showAlert((result && result.message) ? result.message : appInterface.alert);
            });
        };

        $scope.$on('contentLoaded', function (evt) {
            $timeout(function () {
                var currentDate = new Date();
                var jfcalplugin = $("#mycal").jFrontierCal({
                    date: currentDate,
                    applyAgendaTooltipCallback: myApplyTooltip,
                    dragAndDropEnabled: false
                }).data("plugin");
                jfcalplugin.setAspectRatio("#mycal", 0.75);
                loadData(jfcalplugin, ReqServ.getMonth(currentDate));
            }, 500);
        })
    }])
});