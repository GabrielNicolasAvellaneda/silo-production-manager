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
                console.log(response);})
        };

        $scope.getProductKinds();
        $scope.getProductUnits();

    })
   .controller('SelectProductController', function ($scope, $http, $modalInstance) {
        $scope.items = [];
        $scope.firstTime = true;

        $scope.query = { text: ''};

        $scope.selected = {};

        var createProductItemFromProduct = function (product) {
           return {
                quantity: 1,
                id: -1,
                productId: product.id,
                code1: product.code1,
                description: product.description,
                unitCost: product.calculatedCost
           }
        };

        $scope.ok = function() {
            if ($scope.selected.item) {
                $modalInstance.close(createProductItemFromProduct($scope.selected.item));
            }
        };
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.hasResults = function () {
            return $scope.items && $scope.items.length > 0;
        };

        $scope.search = function () {
            $scope.firstTime = false;
            $http.post('/api/products/search', $scope.query).then(function (response) {
                var data = response.data.slice(0, 10);
                $scope.items = data;
            });
        };

    })
    .controller('EditItemController', function ($scope, $modalInstance, item) {
        $scope.item = item;
        $scope.ok = function() {
            $modalInstance.close();
        };
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };


    });
