/**
 * Created by developer on 18/10/2015.
 */
angular.module('app')
    .controller('ProductListCtrl', function ($scope, $http, $log) {

        $scope.products = [];
        $scope.loading = true;

        $log.info('hello, world');

        $http.get('/api/products')
            .then(function (res) {
                $scope.products = res.data;
                $scope.loading = false;
            }
        );
});
