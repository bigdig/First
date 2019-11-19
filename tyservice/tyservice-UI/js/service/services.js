define(['app', 'store', 'jqueryCookie'], function (app, store) {
    app.service('ReqServ', ['$http', 'appInterface', function ($http, appInterface) {
        return {
            request: function (method, url, data) {
                if (data)
                    data.pageSize = data.pageSize ? data.pageSize : appInterface.pageSize;
                return $http({
                    method: method,
                    url: appInterface.reqUrl + url,
                    data: data,
                    timeout: 60000
                });
            },
            upload: function (url, data) {
                return $http({
                    method: 'POST',
                    headers: {'Content-Type': undefined},
                    url: appInterface.reqUrl + url,
                    data: data,
                    transformRequest: function (data) {
                        var formData = new FormData();
                        angular.forEach(data, function (value, key) {
                            key.indexOf('.') >= 0 ? formData.append('file', value) : formData.append(key, value);
                        });
                        return formData;
                    }
                })
            },
            setUser: function (user) {
                store.set('menus', JSON.stringify(user.menus));
                user.menus = null;
                $.cookie('user', JSON.stringify(user));
            },
            getUser: function () {
                var user = $.cookie('user') ? JSON.parse($.cookie('user')) : null;
                var menus = store.get('menus') ? JSON.parse(store.get('menus')) : [];
                if (user)
                    user.menus = menus;
                return user;
            },
            removeUser: function () {
                if ($.cookie('user'))
                    $.cookie('user', null);
                if (store.get('menus'))
                    store.remove('menus');
            },
            setStore: function (name, data) {
                store.set(name, data);
            },
            getStore: function (name) {
                return store.get(name);
            },
            removeStore: function (name) {
                store.remove(name);
            },
            trim: function (str) {
                var regExp = new RegExp(" ", "g");
                if (!str) {
                    return null;
                }
                if (angular.isString(str)) {
                    return str.replace(regExp, "");
                } else {
                    return str.toString().replace(regExp, "");
                }
            },
            setBtnStatus: function (isLoading, btnName, message, obj) {
                obj.loading = isLoading;
                obj.btnName = btnName;
                if (message)
                    obj.message = message;
            },
            copyObj: function (source, target) {
                for (var property in source) {
                    if (property == 'pageTitle' || property == 'btnName' || property == 'pageSize')
                        continue;
                    target[property] = source[property];
                }
            },
            copyArray: function (source, target) {
                for (var i = 0; i < source.length; i++) {
                    target[i] = {};
                    this.copyObj(source[i], target[i]);
                }
            },
            clearObj: function (obj) {
                for (var property in obj) {
                    if (property == 'pageSize')
                        continue;
                    obj[property] = null;
                }
            },
            isSysManager: function (roleList) {
                for (var i = 0; i < roleList.length; i++) {
                    if (roleList[i].roleId == 'sysmng' || roleList[i].roleId == 'guanliyuan')
                        return true;
                }
                return false;
            },
            getTreeNode: function (source, list, key, attr) {
                function getChildNode(item, _list, _attr) {
                    item.children = [];
                    for (var i = 0; i < _list.length; i++) {
                        if (_list[i].parentId == item.id) {
                            _list[i].name = _list[i][_attr];
                            item.children.push(_list[i]);
                            arguments.callee(_list[i], _list);
                        }
                    }
                }

                var j = 0;
                for (var i = 0; i < source.length; i++) {
                    if (source[i].parentId == key) {
                        list[j] = source[i];
                        getChildNode(list[j], source, attr);
                        j++;
                    }
                }
            },
            getTextValue: function (list, obj, id, name) {
                for (var i = 0; i < list.length; i++) {
                    if (list[i].id == obj[id]) {
                        obj[name] = list[i].text;
                        break;
                    }
                }
            },
            delHtmlTag: function (str) {
                return str.replace(/<[^>]+>/g, "");
            },
            getDateString: function (dataNum) {
                if (!dataNum || dataNum.length < 8)
                    return null;
                var year = dataNum.substr(0, 4);
                var month = dataNum.substr(4, 2);
                var day = dataNum.substr(6, 2);
                return year + '-' + month + '-' + day;
            },
            getTimeString: function (timeNum) {
                if (!timeNum || timeNum.length < 5)
                    return null;
                if (timeNum.length == 5) {
                    var hour = timeNum.substr(0, 1);
                    var minute = timeNum.substr(1, 2);
                    var second = timeNum.substr(3, 2);
                    return hour + ':' + minute + ':' + second;
                } else {
                    var hour = timeNum.substr(0, 2);
                    var minute = timeNum.substr(2, 2);
                    var second = timeNum.substr(4, 2);
                    return hour + ':' + minute + ':' + second;
                }
            },
            getDateNum: function (dateString) {
                if (!dateString || dateString.length < 10)
                    return null;
                var date = dateString.split('-');
                return date[0] + date[1] + date[2];
            },
            getTimeNum: function (timeString) {
                if (!timeString || timeString.length < 8)
                    return null;
                var date = timeString.split(':');
                return date[0] + date[1] + date[2];
            },
            dateFormat: function (date, fmt) {
                var o = {
                    "M+": date.getMonth() + 1,                 //月份
                    "d+": date.getDate(),                    //日
                    "h+": date.getHours(),                   //小时
                    "m+": date.getMinutes(),                 //分
                    "s+": date.getSeconds(),                 //秒
                    "q+": Math.floor((date.getMonth() + 3) / 3), //季度
                    "S": date.getMilliseconds()             //毫秒
                };
                if (/(y+)/.test(fmt)) {
                    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
                }
                for (var k in o) {
                    if (new RegExp("(" + k + ")").test(fmt)) {
                        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
                    }
                }
                return fmt;
            },
            getMonth: function (date) {
                var dateString = '';
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                if (month > 9)
                    dateString += year + month;
                else
                    dateString += year + '0' + month;
                return dateString
            },
            getMonthDay: function (date) {
                var year = date.getFullYear();
                var month = date.getMonth() + 1;
                var day = date.getDate();
                month = month > 9 ? month : '0' + month;
                day = day > 9 ? day : '0' + day;
                return year + '-' + month + '-' + day;
            },
            getTotalTime: function (sec) {
                var timeFormat = '';

                function timeFilter(secdons) {
                    if (secdons < 60) {
                        var sec;
                        sec = (secdons > 0) ? secdons + '秒' : '';
                        timeFormat = timeFormat + sec;
                        return;
                    }
                    else if (secdons < 3600) {
                        var min;
                        min = Math.floor(secdons / 60);
                        timeFormat = timeFormat + min + '分钟';
                        timeFilter(secdons - min * 60)
                    }
                    else {
                        var hour;
                        hour = Math.floor(secdons / 3600);
                        timeFormat = timeFormat + hour + '小时';
                        timeFilter(secdons - hour * 3600);
                    }
                }

                timeFilter(sec);
                return timeFormat;
            }
        }
    }]);
});
