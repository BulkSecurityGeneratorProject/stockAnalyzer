'use strict';

angular.module('stockanalyzerApp')
    .controller('StockSplitController', function ($scope, StockSplit, ParseLinks) {
        $scope.stockSplits = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            StockSplit.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.stockSplits = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            StockSplit.get({id: id}, function(result) {
                $scope.stockSplit = result;
                $('#deleteStockSplitConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            StockSplit.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStockSplitConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stockSplit = {day: null, splitRatio: null, id: null};
        };
    });
