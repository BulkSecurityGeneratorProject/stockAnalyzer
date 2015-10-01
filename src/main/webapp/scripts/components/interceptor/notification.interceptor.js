 'use strict';

angular.module('stockanalyzerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-stockanalyzerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-stockanalyzerApp-params')});
                }
                return response;
            },
        };
    });