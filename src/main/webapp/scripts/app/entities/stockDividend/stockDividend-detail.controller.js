'use strict';

angular.module('stockanalyzerApp')
    .controller('StockDividendDetailController', function ($scope, $rootScope, $stateParams, entity, StockDividend, StockInfo, StockPriceDaily) {
        $scope.stockDividend = entity;
        $scope.load = function (id) {
            StockDividend.get({id: id}, function(result) {
                $scope.stockDividend = result;
            });
        };
        $rootScope.$on('stockanalyzerApp:stockDividendUpdate', function(event, result) {
            $scope.stockDividend = result;
        });
    });
