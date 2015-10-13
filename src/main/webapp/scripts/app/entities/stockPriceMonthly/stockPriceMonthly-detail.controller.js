'use strict';

angular.module('stockanalyzerApp')
    .controller('StockPriceMonthlyDetailController', function ($scope, $rootScope, $stateParams, entity, StockPriceMonthly, StockInfo) {
        $scope.stockPriceMonthly = entity;
        $scope.load = function (id) {
            StockPriceMonthly.get({id: id}, function(result) {
                $scope.stockPriceMonthly = result;
            });
        };
        $rootScope.$on('stockanalyzerApp:stockPriceMonthlyUpdate', function(event, result) {
            $scope.stockPriceMonthly = result;
        });
    });
