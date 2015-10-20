/**
 * Created by developer on 20/10/2015.
 */
angular.module('app')
    .controller('ProductNewController', function ($scope, $http, $log) {
        $scope.title = "Producto";
        $scope.subtitle = "Nuevo";
        $scope.product = {
            specificCost: 0,
            specificWorkmanHours: 0
        };
        $scope.productKinds = [];
        $scope.productUnits = [];


        $scope.getProductKinds = function () {
            $http.get('/api/products/kinds').success(function (res) {
                $scope.productKinds = $scope.productKinds.concat(res);
            });
        };

        $scope.getProductUnits = function () {
            $http.get('/api/products/units').success(function (data) {
               $scope.productUnits = $scope.productUnits.concat(data);
            })
        };

        $scope.save = function () {
            $log.info($scope.product);
            $http.post('/api/products/new', $scope.product).success(function (data, status) {
                console.log("Saved!");
                $log.info("Yes, was saved!");
            })
        };

        $scope.getProductKinds();
        $scope.getProductUnits();

    });
