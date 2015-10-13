'use strict';

angular.module('stockanalyzerApp').controller('StockPriceWeeklyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'StockPriceWeekly', 'StockInfo',
        function($scope, $stateParams, $modalInstance, entity, StockPriceWeekly, StockInfo) {

        $scope.stockPriceWeekly = entity;
        $scope.stockinfos = StockInfo.query();
        $scope.load = function(id) {
            StockPriceWeekly.get({id : id}, function(result) {
                $scope.stockPriceWeekly = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('stockanalyzerApp:stockPriceWeeklyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.stockPriceWeekly.id != null) {
                StockPriceWeekly.update($scope.stockPriceWeekly, onSaveFinished);
            } else {
                StockPriceWeekly.save($scope.stockPriceWeekly, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
