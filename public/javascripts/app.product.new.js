/**
 * Created by developer on 20/10/2015.
 */
angular.module('app')
    .controller('ProductNewController', function ($scope, $http, $log) {
        $scope.title = "Producto";
        $scope.subtitle = "Nuevo";
        $scope.product = {};
        $scope.productKinds = [];
        $scope.productUnits = [
            {id: 1, name: "M2"},
            {id: 2, name: "Unidad"}
        ];

        $scope.save = function () {
            $log.info("Saving...");
        };

        $scope.getProductKinds = function () {
            $http.get('/api/products/kinds').success(function (res) {
                $scope.productKinds = $scope.productKinds.concat(res);
            });
        };

        $scope.getProductKinds();
    });
