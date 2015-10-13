'use strict';

angular.module('stockanalyzerApp')
    .controller('StockPriceWeeklyController', function ($scope, StockPriceWeekly, ParseLinks) {
        $scope.stockPriceWeeklys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            StockPriceWeekly.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.stockPriceWeeklys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            StockPriceWeekly.get({id: id}, function(result) {
                $scope.stockPriceWeekly = result;
                $('#deleteStockPriceWeeklyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            StockPriceWeekly.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStockPriceWeeklyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stockPriceWeekly = {day: null, week: null, year: null, open: null, close: null, high: null, low: null, totalVolume: null, adjustedClose: null, priceChange: null, id: null};
        };
    });
