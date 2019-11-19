.state('main.${module}.${table.beanObj}', {
    url: '/${table.beanObj}',
    template: '<div ui-view class="fade-in-right-big smooth"></div>'
})
.state('main.${module}.${table.beanObj}.list', {
    url: '/list',
    templateUrl: 'mng/app/${module}/${table.beanObj}/list.html',
    controller: '${table.beanObj}Controller',
    resolve: {
    	deps: ['uiLoad', '$ocLazyLoad', function(uiLoad, $ocLazyLoad) {
            return uiLoad.load('mng/app/${module}/${table.beanObj}/listController.js').then(function() {
                return $ocLazyLoad.load('toaster');
            });
        }]
      }
})
 .state('main.${module}.${table.beanObj}.view', {
    url: '/view/{id}',
    templateUrl: 'mng/app/${module}/${table.beanObj}/view.html',
    controller: '${table.beanObj}ViewController',
    resolve: {
        deps: ['uiLoad', '$ocLazyLoad', function(uiLoad, $ocLazyLoad) {
            return uiLoad.load('mng/app/${module}/${table.beanObj}/viewController.js').then(function() {
                return $ocLazyLoad.load('toaster');
            });
        }]
      }
}) 
.state('main.${module}.${table.beanObj}.create', {
    url: '/create',
    templateUrl: 'mng/app/${module}/${table.beanObj}/update.html',
    controller: '${table.beanObj}UpdateController',
    resolve: {
    	deps: ['uiLoad', '$ocLazyLoad', function(uiLoad, $ocLazyLoad) {
            return uiLoad.load('mng/app/${module}/${table.beanObj}/updateController.js').then(function() {
                return $ocLazyLoad.load('toaster');
            });
        }]
      }
})
.state('main.${module}.${table.beanObj}.update', {
    url: '/update/{id}',
    templateUrl: 'mng/app/${module}/${table.beanObj}/update.html',
    controller: '${table.beanObj}UpdateController',
    resolve: {
    	deps: ['uiLoad', '$ocLazyLoad', function(uiLoad, $ocLazyLoad) {
            return uiLoad.load('mng/app/${module}/${table.beanObj}/updateController.js').then(function() {
                return $ocLazyLoad.load('toaster');
            });
        }]
      }
})