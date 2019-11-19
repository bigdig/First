define(['app', 'routers', 'configs', 'services', 'directives', 'filters'], function (app) {
        angular.element(document).ready(function () {
            angular.bootstrap(document, [app.name]);
            angular.element(document).find('html').addClass('ng-app');
        });
    }
);