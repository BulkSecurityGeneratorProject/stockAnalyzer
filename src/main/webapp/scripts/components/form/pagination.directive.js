/* globals $ */
'use strict';

angular.module('stockanalyzerApp')
    .directive('stockanalyzerAppPagination', function() {
        return {
            templateUrl: 'scripts/components/form/pagination.html'
        };
    });
