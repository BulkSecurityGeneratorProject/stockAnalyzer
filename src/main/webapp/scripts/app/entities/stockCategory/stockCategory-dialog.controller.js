'use strict';

angular.module('stockanalyzerApp').controller('StockCategoryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'StockCategory',
        function($scope, $stateParams, $modalInstance, entity, StockCategory) {

        $scope.stockCategory = entity;
        $scope.load = function(id) {
            StockCategory.get({id : id}, function(result) {
                $scope.stockCategory = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('stockanalyzerApp:stockCategoryUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.stockCategory.id != null) {
                StockCategory.update($scope.stockCategory, onSaveFinished);
            } else {
                StockCategory.save($scope.stockCategory, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
