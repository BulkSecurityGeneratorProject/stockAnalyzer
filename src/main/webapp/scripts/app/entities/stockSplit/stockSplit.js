'use strict';

angular.module('stockanalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stockSplit', {
                parent: 'entity',
                url: '/stockSplits',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockSplit.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockSplit/stockSplits.html',
                        controller: 'StockSplitController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockSplit');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stockSplit.detail', {
                parent: 'entity',
                url: '/stockSplit/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockSplit.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockSplit/stockSplit-detail.html',
                        controller: 'StockSplitDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockSplit');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockSplit', function($stateParams, StockSplit) {
                        return StockSplit.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stockSplit.new', {
                parent: 'stockSplit',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockSplit/stockSplit-dialog.html',
                        controller: 'StockSplitDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {day: null, splitRatio: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stockSplit', null, { reload: true });
                    }, function() {
                        $state.go('stockSplit');
                    })
                }]
            })
            .state('stockSplit.edit', {
                parent: 'stockSplit',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockSplit/stockSplit-dialog.html',
                        controller: 'StockSplitDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StockSplit', function(StockSplit) {
                                return StockSplit.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockSplit', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
