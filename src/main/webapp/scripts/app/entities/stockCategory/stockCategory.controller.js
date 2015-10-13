'use strict';

angular.module('stockanalyzerApp')
    .controller('StockCategoryController', function ($scope, StockCategory, ParseLinks) {
        $scope.stockCategorys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            StockCategory.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.stockCategorys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            StockCategory.get({id: id}, function(result) {
                $scope.stockCategory = result;
                $('#deleteStockCategoryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            StockCategory.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStockCategoryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.stockCategory = {name: null, id: null};
        };
    });
