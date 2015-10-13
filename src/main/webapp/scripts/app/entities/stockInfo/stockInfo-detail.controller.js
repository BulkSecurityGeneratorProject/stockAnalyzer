'use strict';

angular.module('stockanalyzerApp')
    .controller('StockInfoDetailController', function ($scope, $rootScope, $stateParams, entity, StockInfo, StockExchange, StockCategory) {
        $scope.stockInfo = entity;
        $scope.load = function (id) {
            StockInfo.get({id: id}, function(result) {
                $scope.stockInfo = result;
            });
        };
        $rootScope.$on('stockanalyzerApp:stockInfoUpdate', function(event, result) {
            $scope.stockInfo = result;
        });
    });
