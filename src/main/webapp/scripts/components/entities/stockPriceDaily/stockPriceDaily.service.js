'use strict';

angular.module('stockanalyzerApp')
    .factory('StockPriceDaily', function ($resource, DateUtils) {
        return $resource('api/stockPriceDailys/:id', {}, {
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
