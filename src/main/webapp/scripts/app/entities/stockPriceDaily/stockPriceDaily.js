'use strict';

angular.module('stockanalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stockPriceDaily', {
                parent: 'entity',
                url: '/stockPriceDailys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockPriceDaily.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockPriceDaily/stockPriceDailys.html',
                        controller: 'StockPriceDailyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockPriceDaily');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stockPriceDaily.detail', {
                parent: 'entity',
                url: '/stockPriceDaily/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockPriceDaily.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockPriceDaily/stockPriceDaily-detail.html',
                        controller: 'StockPriceDailyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockPriceDaily');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockPriceDaily', function($stateParams, StockPriceDaily) {
                        return StockPriceDaily.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stockPriceDaily.new', {
                parent: 'stockPriceDaily',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockPriceDaily/stockPriceDaily-dialog.html',
                        controller: 'StockPriceDailyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {day: null, open: null, close: null, high: null, low: null, volume: null, adjustedClose: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stockPriceDaily', null, { reload: true });
                    }, function() {
                        $state.go('stockPriceDaily');
                    })
                }]
            })
            .state('stockPriceDaily.edit', {
                parent: 'stockPriceDaily',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockPriceDaily/stockPriceDaily-dialog.html',
                        controller: 'StockPriceDailyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StockPriceDaily', function(StockPriceDaily) {
                                return StockPriceDaily.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockPriceDaily', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
