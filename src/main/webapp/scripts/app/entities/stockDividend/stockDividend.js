'use strict';

angular.module('stockanalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stockDividend', {
                parent: 'entity',
                url: '/stockDividends',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockDividend.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockDividend/stockDividends.html',
                        controller: 'StockDividendController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockDividend');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stockDividend.detail', {
                parent: 'entity',
                url: '/stockDividend/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockDividend.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockDividend/stockDividend-detail.html',
                        controller: 'StockDividendDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockDividend');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockDividend', function($stateParams, StockDividend) {
                        return StockDividend.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stockDividend.new', {
                parent: 'stockDividend',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockDividend/stockDividend-dialog.html',
                        controller: 'StockDividendDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {day: null, amount: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stockDividend', null, { reload: true });
                    }, function() {
                        $state.go('stockDividend');
                    })
                }]
            })
            .state('stockDividend.edit', {
                parent: 'stockDividend',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockDividend/stockDividend-dialog.html',
                        controller: 'StockDividendDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StockDividend', function(StockDividend) {
                                return StockDividend.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockDividend', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
