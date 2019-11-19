require.config({
    baseUrl: 'js',
    config: {moment: {noGlobal: true}},
    paths: {
        'jquery': 'lib/jquery-1.11.3.min',
        'jqueryCookie': 'lib/jquery.cookie',
        'angular': 'lib/angular.min',
        'asyncLoader': 'lib/angular-async-loader.min',
        'uiRouter': 'lib/angular-ui-router.min',
        'sanitize': 'lib/angular-sanitize.min',
        'ngDialog': 'lib/ngDialog.min',
        'uiUploader': 'lib/uploader.min',
        'store': 'lib/store.min',
        'bindOnce': 'lib/bindonce.min',
        'bootstrap': 'bootstrap/bootstrap',
        'app': 'bootstrap/app',
        'routers': 'router/routers',
        'configs': 'config/configs',
        'services': 'service/services',
        'directives': 'directive/directives',
        'filters': 'filter/filters',
        'moment': 'lib/moment-with-locales.min',
        'ngDatepicker': 'lib/ngDatepicker',
        'ddsort': 'lib/ddsort',
        'xheditor': 'lib/xheditor-1.2.2.min',
        'zh_cn': 'lib/zh-cn',
        'echarts': 'lib/echarts.min',
        'echartsWordcloud': 'lib/echarts-wordcloud.min',
        'uiCustom': 'lib/jquery-ui-1.8.1.custom.min',
        'jsHash': 'lib/jshashtable-2.1',
        'cal': 'lib/jquery-frontier-cal-1.3.2.min',
        'qtip': 'lib/jquery.qtip-1.0.min'
    },
    shim: {
        'angular': {exports: 'angular'},
        'jquery': {exports: '$'},
        'jqueryCookie': {deps: ['jquery']},
        'store': {deps: ['jquery']},
        'qtip': {deps: ['jquery']},
        'uiCustom': {deps: ['jquery']},
        'cal': {deps: ['jsHash', 'jquery']},
        'uiRouter': {deps: ['angular']},
        'bindOnce': {deps: ['angular']},
        'sanitize': {deps: ['angular']},
        'ngDialog': {deps: ['angular']},
        'uiUploader': {deps: ['angular']},
        'ngDatepicker': {deps: ['angular', 'moment']},
        'ddsort': {deps: ['jquery']},
        'xheditor': {deps: ['jquery']},
        'zh_cn': {deps: ['xheditor']}
    },
    deps: ['bootstrap'],
    urlArgs: 'v=' + (new Date).getTime()
});