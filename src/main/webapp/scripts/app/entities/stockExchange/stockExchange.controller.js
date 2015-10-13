'use strict';

angular.module('stockanalyzerApp')
    .controller('StockExchangeController', function ($scope, StockExchange, ParseLinks) {
        $scope.stockExchanges = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            StockExchange.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.stockExchanges = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            StockExchange.get({id: id}, function(result) {
                $scope.stockExchange = result;
                $('#deleteStockExchangeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            StockExchange.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStockExchangeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stockExchange = {name: null, id: null};
        };
    });
