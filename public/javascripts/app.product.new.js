/**
 * Created by developer on 20/10/2015.
 */
angular.module('app')
    .controller('ProductNewController', function ($scope, $http, $location, $log) {
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
            $http.post('/api/products/new', $scope.product).then(function (response) {
                $location.path('/products/view/' + response.data.id);
            }, function (response) {
                console.log(response);
            })
        };

        $scope.getProductKinds();
        $scope.getProductUnits();

    })
    .controller('ProductEditController', function ($scope, $http, $location, $log, $routeParams) {
        $scope.title = 'Producto';
        $scope.subtitle = "Editar";
        $scope.product = {};

        $scope.load = function (id) {

           $http.get('/api/products/get/' + id).then(function (response) {
              $scope.product = response.data
           })
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

        $scope.getProductKinds();
        $scope.getProductUnits();
        $scope.load($routeParams.id);
    });


