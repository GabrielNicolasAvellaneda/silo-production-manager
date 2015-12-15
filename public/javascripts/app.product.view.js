angular.module('app')
    .controller('CostsDetailsController', function ($scope, $http, $modalInstance, product) {

        $scope.product = product;

        $scope.ok = function() {
            $modalInstance.close();
        };
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

    })
    .controller('ProductViewController', function ($scope, $http, $log, $location, $routeParams, $uibModal) {
        $scope.title = 'Producto';
        $scope.subtitle = 'Detalle';
        $scope.product = {};
        $scope.items = [];

        $scope.load = function () {
            var id = $routeParams.id;
            $http.get('/api/products/get/' + id).then(function (response) {
                $scope.product = response.data;
                $log.info($scope.product);
            }, function (response) {

            });

            $http.get('/api/products/get-items/' + id).then(function (response) {
                $scope.items = response.data;
            });
        };

        $scope.showCostsDetails = function () {
            var modalInstance = $uibModal.open({
                size: 'md',
                animation: true,
                templateUrl: 'costsDetailsModal.html',
                controller: 'CostsDetailsController',
                resolve: {
                    product: function () {
                        return $scope.product;
                    }
                }
            });
        };

        $scope.load();
    });
