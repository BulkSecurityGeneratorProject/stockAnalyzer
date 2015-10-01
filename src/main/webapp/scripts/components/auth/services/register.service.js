'use strict';

angular.module('stockanalyzerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


