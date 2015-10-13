'use strict';

angular.module('stockanalyzerApp')
    .factory('StockInfo', function ($resource, DateUtils) {
        return $resource('api/stockInfos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
