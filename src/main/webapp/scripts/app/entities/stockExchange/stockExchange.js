'use strict';

angular.module('stockanalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stockExchange', {
                parent: 'entity',
                url: '/stockExchanges',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockExchange.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockExchange/stockExchanges.html',
                        controller: 'StockExchangeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockExchange');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stockExchange.detail', {
                parent: 'entity',
                url: '/stockExchange/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockExchange.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockExchange/stockExchange-detail.html',
                        controller: 'StockExchangeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockExchange');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockExchange', function($stateParams, StockExchange) {
                        return StockExchange.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stockExchange.new', {
                parent: 'stockExchange',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockExchange/stockExchange-dialog.html',
                        controller: 'StockExchangeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stockExchange', null, { reload: true });
                    }, function() {
                        $state.go('stockExchange');
                    })
                }]
            })
            .state('stockExchange.edit', {
                parent: 'stockExchange',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockExchange/stockExchange-dialog.html',
                        controller: 'StockExchangeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StockExchange', function(StockExchange) {
                                return StockExchange.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockExchange', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
