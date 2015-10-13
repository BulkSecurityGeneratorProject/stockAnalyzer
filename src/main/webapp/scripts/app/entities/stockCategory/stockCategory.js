'use strict';

angular.module('stockanalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('stockCategory', {
                parent: 'entity',
                url: '/stockCategorys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockCategory.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockCategory/stockCategorys.html',
                        controller: 'StockCategoryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockCategory');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stockCategory.detail', {
                parent: 'entity',
                url: '/stockCategory/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'stockanalyzerApp.stockCategory.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/stockCategory/stockCategory-detail.html',
                        controller: 'StockCategoryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stockCategory');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockCategory', function($stateParams, StockCategory) {
                        return StockCategory.get({id : $stateParams.id});
                    }]
                }
            })
            .state('stockCategory.new', {
                parent: 'stockCategory',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockCategory/stockCategory-dialog.html',
                        controller: 'StockCategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('stockCategory', null, { reload: true });
                    }, function() {
                        $state.go('stockCategory');
                    })
                }]
            })
            .state('stockCategory.edit', {
                parent: 'stockCategory',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/stockCategory/stockCategory-dialog.html',
                        controller: 'StockCategoryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['StockCategory', function(StockCategory) {
                                return StockCategory.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('stockCategory', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
