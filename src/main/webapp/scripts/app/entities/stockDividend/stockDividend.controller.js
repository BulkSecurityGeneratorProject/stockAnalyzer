'use strict';

angular.module('stockanalyzerApp')
    .controller('StockDividendController', function ($scope, StockDividend, ParseLinks) {
        $scope.stockDividends = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            StockDividend.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.stockDividends = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            StockDividend.get({id: id}, function(result) {
                $scope.stockDividend = result;
                $('#deleteStockDividendConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            StockDividend.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStockDividendConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stockDividend = {day: null, amount: null, id: null};
        };
    });
