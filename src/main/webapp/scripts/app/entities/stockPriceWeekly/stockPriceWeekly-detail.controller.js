'use strict';

angular.module('stockanalyzerApp')
    .controller('StockPriceWeeklyDetailController', function ($scope, $rootScope, $stateParams, entity, StockPriceWeekly, StockInfo) {
        $scope.stockPriceWeekly = entity;
        $scope.load = function (id) {
            StockPriceWeekly.get({id: id}, function(result) {
                $scope.stockPriceWeekly = result;
            });
        };
        $rootScope.$on('stockanalyzerApp:stockPriceWeeklyUpdate', function(event, result) {
            $scope.stockPriceWeekly = result;
        });
    });
