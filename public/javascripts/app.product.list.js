/**
 * Created by developer on 18/10/2015.
 */
angular.module('app')
    .config(function($locationProvider) {
        $locationProvider.html5Mode({
            enabled: false,
            requireBase: false
        }).hashPrefix('!');
    })
    .controller('ProductListCtrl', function ($scope, $http, $log, $location) {

        var getUrlParameter = function getUrlParameter(sParam) {
            var sPageURL = decodeURIComponent(window.location.search.substring(1)),
                sURLVariables = sPageURL.split('&'),
                sParameterName,
                i;

            for (i = 0; i < sURLVariables.length; i++) {
                sParameterName = sURLVariables[i].split('=');

                if (sParameterName[0] === sParam) {
                    return sParameterName[1] === undefined ? true : sParameterName[1];
                }
            }
        };

        $scope.products = [];
        $scope.loading = true;

        var path = '/api/products';
        if (getUrlParameter("all")) {
            path += '?all=true';
        }

        $http.get(path)
            .then(function (res) {
                $scope.products = res.data;
                $scope.loading = false;
            }
        );
});
