'use strict';

angular.module('stockanalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stockPriceWeekly', {
                parent: 'entity',
                url: '/stockPriceWeeklys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockPriceWeekly.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockPriceWeekly/stockPriceWeeklys.html',
                        controller: 'StockPriceWeeklyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockPriceWeekly');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stockPriceWeekly.detail', {
                parent: 'entity',
                url: '/stockPriceWeekly/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockPriceWeekly.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockPriceWeekly/stockPriceWeekly-detail.html',
                        controller: 'StockPriceWeeklyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockPriceWeekly');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockPriceWeekly', function($stateParams, StockPriceWeekly) {
                        return StockPriceWeekly.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stockPriceWeekly.new', {
                parent: 'stockPriceWeekly',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockPriceWeekly/stockPriceWeekly-dialog.html',
                        controller: 'StockPriceWeeklyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {day: null, week: null, year: null, open: null, close: null, high: null, low: null, totalVolume: null, adjustedClose: null, priceChange: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stockPriceWeekly', null, { reload: true });
                    }, function() {
                        $state.go('stockPriceWeekly');
                    })
                }]
            })
            .state('stockPriceWeekly.edit', {
                parent: 'stockPriceWeekly',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockPriceWeekly/stockPriceWeekly-dialog.html',
                        controller: 'StockPriceWeeklyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StockPriceWeekly', function(StockPriceWeekly) {
                                return StockPriceWeekly.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockPriceWeekly', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
