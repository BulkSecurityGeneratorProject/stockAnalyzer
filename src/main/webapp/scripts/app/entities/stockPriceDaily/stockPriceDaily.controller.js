'use strict';

angular.module('stockanalyzerApp')
    .controller('StockPriceDailyController', function ($scope, StockPriceDaily, ParseLinks) {
        $scope.stockPriceDailys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            StockPriceDaily.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.stockPriceDailys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            StockPriceDaily.get({id: id}, function(result) {
                $scope.stockPriceDaily = result;
                $('#deleteStockPriceDailyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            StockPriceDaily.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStockPriceDailyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stockPriceDaily = {day: null, open: null, close: null, high: null, low: null, volume: null, adjustedClose: null, id: null};
        };
    });
