/**
 * Created by developer on 18/10/2015.
 */
angular.module('app')
    .controller('ProductListController', function ($scope, $http, $log, $location, $routeParams) {

        var isRawMaterialListing = function () {
            return $routeParams.filter;
        };

        $scope.title = isRawMaterialListing()? 'Materiales' : 'Producto';
        $scope.subtitle = 'Listado';

        $scope.products = [];
        $scope.loading = true;

        var path = isRawMaterialListing()? '/api/products' : '/api/products?all=true';

        $http.get(path)
            .then(function (res) {
                $scope.products = res.data;
                $scope.loading = false;
            }
        );
})
    .controller('ProductViewController', function ($scope, $http, $log, $location, $routeParams) {
        $scope.title = 'Producto';
        $scope.subtitle = 'Detalle';
        $scope.product = {};

        $scope.load = function () {
           var id = $routeParams.id;
           $http.get('/api/products/get/' + id).then(function (response) {
               $scope.product = response.data;
           }, function (response) {

           });
        };

        $scope.load()
    });
