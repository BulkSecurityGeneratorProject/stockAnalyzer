'use strict';

angular.module('stockanalyzerApp')
    .controller('StockCategoryDetailController', function ($scope, $rootScope, $stateParams, entity, StockCategory) {
        $scope.stockCategory = entity;
        $scope.load = function (id) {
            StockCategory.get({id: id}, function(result) {
                $scope.stockCategory = result;
            });
        };
        $rootScope.$on('stockanalyzerApp:stockCategoryUpdate', function(event, result) {
            $scope.stockCategory = result;
        });
    });
