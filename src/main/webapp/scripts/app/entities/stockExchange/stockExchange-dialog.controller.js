'use strict';

angular.module('stockanalyzerApp').controller('StockExchangeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'StockExchange',
        function($scope, $stateParams, $modalInstance, entity, StockExchange) {

        $scope.stockExchange = entity;
        $scope.load = function(id) {
            StockExchange.get({id : id}, function(result) {
                $scope.stockExchange = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('stockanalyzerApp:stockExchangeUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.stockExchange.id != null) {
                StockExchange.update($scope.stockExchange, onSaveFinished);
            } else {
                StockExchange.save($scope.stockExchange, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
