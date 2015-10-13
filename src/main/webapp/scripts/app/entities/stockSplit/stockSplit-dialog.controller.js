'use strict';

angular.module('stockanalyzerApp').controller('StockSplitDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'StockSplit', 'StockInfo', 'StockPriceDaily',
        function($scope, $stateParams, $modalInstance, $q, entity, StockSplit, StockInfo, StockPriceDaily) {

        $scope.stockSplit = entity;
        $scope.stockinfos = StockInfo.query();
        $scope.stockprices = StockPriceDaily.query({filter: 'stockdividend-is-null'});
        $q.all([$scope.stockDividend.$promise, $scope.stockprices.$promise]).then(function() {
            if (!$scope.stockDividend.stockPriceId) {
                return $q.reject();
            }
            return StockPriceDaily.get({id : $scope.stockDividend.stockPriceId}).$promise;
        }).then(function(stockPrice) {
            $scope.stockprices.push(stockPrice);
        });
        $scope.load = function(id) {
            StockSplit.get({id : id}, function(result) {
                $scope.stockSplit = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('stockanalyzerApp:stockSplitUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.stockSplit.id != null) {
                StockSplit.update($scope.stockSplit, onSaveFinished);
            } else {
                StockSplit.save($scope.stockSplit, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
