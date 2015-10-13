'use strict';

angular.module('stockanalyzerApp')
    .controller('StockPriceMonthlyController', function ($scope, StockPriceMonthly, ParseLinks) {
        $scope.stockPriceMonthlys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            StockPriceMonthly.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.stockPriceMonthlys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            StockPriceMonthly.get({id: id}, function(result) {
                $scope.stockPriceMonthly = result;
                $('#deleteStockPriceMonthlyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            StockPriceMonthly.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStockPriceMonthlyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stockPriceMonthly = {day: null, month: null, year: null, open: null, close: null, high: null, low: null, totalVolume: null, adjustedClose: null, priceChange: null, id: null};
        };
    });
