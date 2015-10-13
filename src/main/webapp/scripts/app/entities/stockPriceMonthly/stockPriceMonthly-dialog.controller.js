'use strict';

angular.module('stockanalyzerApp').controller('StockPriceMonthlyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'StockPriceMonthly', 'StockInfo',
        function($scope, $stateParams, $modalInstance, entity, StockPriceMonthly, StockInfo) {

        $scope.stockPriceMonthly = entity;
        $scope.stockinfos = StockInfo.query();
        $scope.load = function(id) {
            StockPriceMonthly.get({id : id}, function(result) {
                $scope.stockPriceMonthly = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('stockanalyzerApp:stockPriceMonthlyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.stockPriceMonthly.id != null) {
                StockPriceMonthly.update($scope.stockPriceMonthly, onSaveFinished);
            } else {
                StockPriceMonthly.save($scope.stockPriceMonthly, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
