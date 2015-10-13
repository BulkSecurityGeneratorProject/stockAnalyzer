'use strict';

angular.module('stockanalyzerApp')
    .controller('StockExchangeDetailController', function ($scope, $rootScope, $stateParams, entity, StockExchange) {
        $scope.stockExchange = entity;
        $scope.load = function (id) {
            StockExchange.get({id: id}, function(result) {
                $scope.stockExchange = result;
            });
        };
        $rootScope.$on('stockanalyzerApp:stockExchangeUpdate', function(event, result) {
            $scope.stockExchange = result;
        });
    });
