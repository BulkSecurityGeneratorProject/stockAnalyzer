'use strict';

angular.module('stockanalyzerApp').controller('StockDividendDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'StockDividend', 'StockInfo', 'StockPriceDaily',
        function($scope, $stateParams, $modalInstance, $q, entity, StockDividend, StockInfo, StockPriceDaily) {

        $scope.stockDividend = entity;
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
            StockDividend.get({id : id}, function(result) {
                $scope.stockDividend = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('stockanalyzerApp:stockDividendUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.stockDividend.id != null) {
                StockDividend.update($scope.stockDividend, onSaveFinished);
            } else {
                StockDividend.save($scope.stockDividend, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
