/**
 * Created by developer on 18/10/2015.
 */
angular.module('app')
    .controller('ProductListController', function ($scope, $http, $log, $location) {

        var isRawMaterialListing = function () {
            return ($location.path().indexOf('raw-material') !== -1);
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
});
