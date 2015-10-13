'use strict';

angular.module('stockanalyzerApp').controller('StockPriceDailyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'StockPriceDaily', 'StockInfo',
        function($scope, $stateParams, $modalInstance, entity, StockPriceDaily, StockInfo) {

        $scope.stockPriceDaily = entity;
        $scope.stockinfos = StockInfo.query();
        $scope.load = function(id) {
            StockPriceDaily.get({id : id}, function(result) {
                $scope.stockPriceDaily = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('stockanalyzerApp:stockPriceDailyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.stockPriceDaily.id != null) {
                StockPriceDaily.update($scope.stockPriceDaily, onSaveFinished);
            } else {
                StockPriceDaily.save($scope.stockPriceDaily, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
