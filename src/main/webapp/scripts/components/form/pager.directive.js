/* globals $ */
'use strict';

angular.module('stockanalyzerApp')
    .directive('stockanalyzerAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
