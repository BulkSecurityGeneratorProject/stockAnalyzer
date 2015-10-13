'use strict';

angular.module('stockanalyzerApp').controller('StockInfoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'StockInfo', 'StockExchange', 'StockCategory',
        function($scope, $stateParams, $modalInstance, entity, StockInfo, StockExchange, StockCategory) {

        $scope.stockInfo = entity;
        $scope.stockexchanges = StockExchange.query();
        $scope.stockcategorys = StockCategory.query();
        $scope.load = function(id) {
            StockInfo.get({id : id}, function(result) {
                $scope.stockInfo = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('stockanalyzerApp:stockInfoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.stockInfo.id != null) {
                StockInfo.update($scope.stockInfo, onSaveFinished);
            } else {
                StockInfo.save($scope.stockInfo, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
