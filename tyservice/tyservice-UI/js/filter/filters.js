define(['app'], function (app) {
    app.filter('deleteHtml', function () {
        return function (value) {
            return value ? value.replace(/<[^>]+>/g,"") : null;
        };
    });
});