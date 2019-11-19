define(["moment"], function (moment) {
    angular.module('jkuri.datepicker', []).directive('ngDatepicker', ['$timeout', function ($timeout) {
        'use strict';
        var setScopeValues = function (scope, attrs) {
            scope.format = attrs.format || 'YYYY-MM-DD';
            scope.viewFormat = attrs.viewFormat || 'Do MMMM YYYY';
            scope.locale = attrs.locale || 'en';
            scope.firstWeekDaySunday = scope.$eval(attrs.firstWeekDaySunday) || false;
            scope.placeholder = attrs.placeholder || '';
            scope.name = attrs.name ? attrs.name : '';
        };
        var setLimitDate = function (scope, attrs) {
            scope.min = attrs.min == 'true';
            scope.limitDate = attrs.limitdate;
        };
        return {
            restrict: 'EA',
            // require: '?ngModel',
            scope: {
                dirty: '=',
                required: '=',
                value: '='
            },
            link: function (scope, element, attrs, ngModel) {
                setScopeValues(scope, attrs);
                scope.calendarOpened = false;
                scope.days = [];
                scope.dayNames = [];
                scope.viewValue = null;
                scope.dateValue = null;
                moment.locale(scope.locale);
                var date = moment();
                var generateCalendar = function (date) {
                    setLimitDate(scope, attrs);
                    var lastDayOfMonth = date.endOf('month').date(),
                        month = date.month(),
                        year = date.year(),
                        limitDate = scope.limitDate ? scope.limitDate.split('-') : null,
                        n = 1;
                    var firstWeekDay = scope.firstWeekDaySunday === true ? date.set('date', 2).day() : date.set('date', 1).day();
                    if (firstWeekDay !== 1) {
                        n -= firstWeekDay - 1;
                    }
                    //Code to fix date issue
                    if (n == 2)
                        n = -5;
                    scope.dateValue = date.format('MMMM YYYY');
                    scope.days = [];
                    if (limitDate)
                        limitDate = new Date(parseInt(limitDate[0]), parseInt(limitDate[1]) - 1, parseInt(limitDate[2]));
                    var today = new Date();
                    for (var i = n; i <= lastDayOfMonth; i += 1) {
                        if (i > 0) {
                            var currentDate = new Date(year, month, i);
                            scope.days.push({
                                day: i,
                                month: month + 1,
                                year: year,
                                enabled: limitDate ? (scope.min ? currentDate >= limitDate : currentDate <= limitDate) : true,
                                today: currentDate - new Date(today.getFullYear(), today.getMonth(), today.getDate()) == 0
                            });
                        } else {
                            scope.days.push({day: null, month: null, year: null, enabled: false});
                        }
                    }
                };
                var generateDayNames = function () {
                    var date = scope.firstWeekDaySunday === true ? moment('2015-06-07') : moment('2015-06-01');
                    for (var i = 0; i < 7; i += 1) {
                        scope.dayNames.push(date.format('ddd'));
                        date.add('1', 'd');
                    }
                };
                generateDayNames();
                scope.showCalendar = function () {
                    if (scope.calendarOpened)
                        return;
                    $('input[name=' + scope.name + ']').focus();
                    scope.calendarOpened = true;
                    generateCalendar(date);
                };
                scope.closeCalendar = function () {
                    scope.calendarOpened = false;
                };
                scope.prevYear = function () {
                    date.subtract(1, 'Y');
                    generateCalendar(date);
                };
                scope.prevMonth = function () {
                    date.subtract(1, 'M');
                    generateCalendar(date);
                };
                scope.nextMonth = function () {
                    date.add(1, 'M');
                    generateCalendar(date);
                };
                scope.nextYear = function () {
                    date.add(1, 'Y');
                    generateCalendar(date);
                };
                scope.clear = function () {
                    //ngModel.$setViewValue('');
                    scope.value = null;
                    if (scope.dirty !== undefined)
                        scope.dirty = true;
                    scope.closeCalendar();
                };
                scope.selectDate = function (event, date) {
                    event.preventDefault();
                    if (!date.enabled)
                        return;
                    var selectedDate = moment(date.day + '.' + date.month + '.' + date.year, 'DD.MM.YYYY');
                    //ngModel.$setViewValue(selectedDate.format(scope.format));
                    scope.value = selectedDate.format(scope.viewFormat);
                    if (scope.dirty !== undefined)
                        scope.dirty = true;
                    scope.closeCalendar();
                };
                scope.blurEventHandler = function () {
                    var pickerBox = element[0].querySelector('.ng-datepicker'),
                        input = element[0].querySelector('.ng-datepicker-input');
                    $timeout(function () {
                        var activeElement = document.activeElement,
                            datepickerElementActive = activeElement.isEqualNode(input) || activeElement.isEqualNode(pickerBox);
                        return datepickerElementActive || scope.closeCalendar()
                    });
                };
                scope.$on('blurEvent', scope.blurEventHandler);
                scope.blurHandler = function () {
                    scope.$emit('blurEvent');
                };
                // ngModel.$render = function () {
                //     var newValue = ngModel.$viewValue;
                //     if (newValue) {
                //         scope.value = moment(newValue).format(attrs.viewFormat);
                //         scope.dateValue = newValue;
                //     }
                // };
            },
            template: '<div>' +
            '	<input name="{{ name }}" ng-required="{{ required }}" readonly type="search" tabindex="1" ng-click="showCalendar()" ng-model="value" ng-blur="blurHandler()" class="ng-datepicker-input" placeholder="{{ placeholder }}"><i ng-click="showCalendar()" class="font-16 icon icon-calendar" style="position: absolute; line-height: 36px; margin-left: -26px"></i>'
            + '</div>' +
            '<div id="datepickerBox-{{$id}}" class="ng-datepicker" tabindex="1" ng-show="calendarOpened" ng-blur="blurHandler()" >' +
            '  <div class="controls">' +
            '    <div class="left">' +
            '      <i class="icon-chevron-thin-left prev-year-btn" ng-click="prevYear()"></i>' +
            '      <i class="icon-chevron-left  prev-month-btn" ng-click="prevMonth()"></i>' +
            '    </div>' +
            '    <span class="date" ng-bind="dateValue"></span>' +
            '    <div class="right">' +
            '      <i class="icon-chevron-right next-month-btn" ng-click="nextMonth()"></i>' +
            '      <i class="icon-chevron-thin-right next-year-btn" ng-click="nextYear()"></i>' +
            '    </div>' +
            '  </div>' +
            '  <div class="day-names">' +
            '    <span ng-repeat="dn in dayNames">' +
            '      <span>{{ dn }}</span>' +
            '    </span>' +
            '  </div>' +
            '  <div class="calendar">' +
            '    <span ng-repeat="d in days">' +
            '      <span class="day" ng-click="selectDate($event, d)" ng-class="{dayL: $last, disabled: !d.enabled, today: d.today}">{{ d.day }}</span>' +
            '    </span>' +
            '    <span><div ng-click="clear()">清除</div></span>' +
            '  </div>' +
            '</div>'
        };
    }]);
});

