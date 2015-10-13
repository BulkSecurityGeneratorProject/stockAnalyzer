'use strict';

angular.module('stockanalyzerApp')
    .controller('StockInfoController', function ($scope, StockInfo, ParseLinks) {
        $scope.stockInfos = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            StockInfo.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.stockInfos = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            StockInfo.get({id: id}, function(result) {
                $scope.stockInfo = result;
                $('#deleteStockInfoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            StockInfo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStockInfoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stockInfo = {ticker: null, companyName: null, id: null};
        };
    });
