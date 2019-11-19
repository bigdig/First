define(['app'], function (app) {
    app.factory('timestampMarker', ['$q', '$injector', function ($q, $injector) {
        return {
            response: function (response) {
                var scope = $injector.get('$rootScope');
                if (response.data.httpCode == 401)
                    scope._state.go('login');
                return response;
            }
        };
    }]).config(['$httpProvider', 'ngDialogProvider', function ($httpProvider, ngDialogProvider) {
        function param(obj) {
            var query = '', name, value, fullSubName, subName, subValue, innerObj, i;
            for (name in obj) {
                value = obj[name];
                if (value instanceof Array) {
                    for (i = 0; i < value.length; ++i) {
                        subValue = value[i];
                        fullSubName = name + '[]';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                }
                else if (value instanceof Object) {
                    for (subName in value) {
                        subValue = value[subName];
                        fullSubName = name + '[' + subName + ']';
                        innerObj = {};
                        innerObj[fullSubName] = subValue;
                        query += param(innerObj) + '&';
                    }
                }
                else if (value !== undefined && value !== null)
                    query += encodeURIComponent(name) + '=' + encodeURIComponent(value) + '&';
            }
            return query.length ? query.substr(0, query.length - 1) : query;
        }

        $httpProvider.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
        $httpProvider.defaults.transformRequest = [function (data) {
            return angular.isObject(data) && String(data) !== '[object File]' ? param(data) : data;
        }];
        $httpProvider.interceptors.push('timestampMarker');
        ngDialogProvider.setDefaults({
            className: 'ngdialog-theme-default',
            plain: false,
            showClose: true,
            closeByDocument: true,
            closeByEscape: true,
            closeByNavigation: true,
            appendTo: false
        });

    }]).constant('appInterface', {
        version: (new Date).getTime(),
        sysname: '客户服务管理平台',
        reqUrl: '/',
        fileUrl: 'files/',
        alert: '服务器出错，请求无法响应。',
        msgType: [{id: 'sms', text: '短信'}, {id: 'email', text: '邮件'}],
        msgFlag: [{id: 0, text: '未审核'}, {id: 1, text: '已通过'}, {id: 2, text: '未通过'}],
        sendFlag: [{id: 0, text: '未发送'}, {id: 1, text: '已发送'}],
        pageSize: 15,
        pages: [15, 20, 30, 50],
        successCode: 200,
        multiple: 7,
        fileSize: 20,
        templateFlag: '#',
        groupNum: 5,
        add: 1,
        height: 160
    });
});
