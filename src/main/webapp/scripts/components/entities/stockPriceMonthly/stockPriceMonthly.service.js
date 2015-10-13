'use strict';

angular.module('stockanalyzerApp')
    .factory('StockPriceMonthly', function ($resource, DateUtils) {
        return $resource('api/stockPriceMonthlys/:id', {}, {
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
