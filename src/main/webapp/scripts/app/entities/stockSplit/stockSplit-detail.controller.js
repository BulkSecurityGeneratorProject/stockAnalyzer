'use strict';

angular.module('stockanalyzerApp')
    .controller('StockSplitDetailController', function ($scope, $rootScope, $stateParams, entity, StockSplit, StockInfo, StockPriceDaily) {
        $scope.stockSplit = entity;
        $scope.load = function (id) {
            StockSplit.get({id: id}, function(result) {
                $scope.stockSplit = result;
            });
        };
        $rootScope.$on('stockanalyzerApp:stockSplitUpdate', function(event, result) {
            $scope.stockSplit = result;
        });
    });
