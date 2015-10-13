'use strict';

angular.module('stockanalyzerApp')
    .controller('StockPriceDailyDetailController', function ($scope, $rootScope, $stateParams, entity, StockPriceDaily, StockInfo) {
        $scope.stockPriceDaily = entity;
        $scope.load = function (id) {
            StockPriceDaily.get({id: id}, function(result) {
                $scope.stockPriceDaily = result;
            });
        };
        $rootScope.$on('stockanalyzerApp:stockPriceDailyUpdate', function(event, result) {
            $scope.stockPriceDaily = result;
        });
    });
