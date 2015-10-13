'use strict';

angular.module('stockanalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stockPriceMonthly', {
                parent: 'entity',
                url: '/stockPriceMonthlys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockPriceMonthly.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockPriceMonthly/stockPriceMonthlys.html',
                        controller: 'StockPriceMonthlyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockPriceMonthly');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stockPriceMonthly.detail', {
                parent: 'entity',
                url: '/stockPriceMonthly/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockPriceMonthly.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockPriceMonthly/stockPriceMonthly-detail.html',
                        controller: 'StockPriceMonthlyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockPriceMonthly');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockPriceMonthly', function($stateParams, StockPriceMonthly) {
                        return StockPriceMonthly.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stockPriceMonthly.new', {
                parent: 'stockPriceMonthly',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockPriceMonthly/stockPriceMonthly-dialog.html',
                        controller: 'StockPriceMonthlyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {day: null, month: null, year: null, open: null, close: null, high: null, low: null, totalVolume: null, adjustedClose: null, priceChange: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stockPriceMonthly', null, { reload: true });
                    }, function() {
                        $state.go('stockPriceMonthly');
                    })
                }]
            })
            .state('stockPriceMonthly.edit', {
                parent: 'stockPriceMonthly',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockPriceMonthly/stockPriceMonthly-dialog.html',
                        controller: 'StockPriceMonthlyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StockPriceMonthly', function(StockPriceMonthly) {
                                return StockPriceMonthly.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockPriceMonthly', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
