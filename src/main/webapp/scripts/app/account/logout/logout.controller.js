'use strict';

angular.module('stockanalyzerApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
