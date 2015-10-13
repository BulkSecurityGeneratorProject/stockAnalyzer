'use strict';

angular.module('stockanalyzerApp')
    .factory('StockExchange', function ($resource, DateUtils) {
        return $resource('api/stockExchanges/:id', {}, {
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
