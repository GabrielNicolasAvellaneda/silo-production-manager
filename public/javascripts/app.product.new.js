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
    .controller('ProductEditController', function ($scope, $http, $location, $log, $routeParams, $uibModal, Data) {
        $scope.title = 'Producto';
        $scope.subtitle = "Editar";
        $scope.product = {};
        $scope.items = {};

        $scope.load = function (id) {
            Data.getProduct({productId:id}, function (data) {
                $scope.product = data
            });
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

        $scope.selectProduct = false;
        $scope.items = [];

        $scope.removeItem = function(item) {
            var index = $scope.items.indexOf(item);
            $scope.items.splice(index, 1);
        };

        $scope.itemExists = function(searchItem) {
            for (var i=0; i < $scope.items.length; i++) {
                var item = $scope.items[i];
                if (item.product == searchItem.product) {
                    return true;
                }
            }
            return false;
        };

        $scope.editItem = function (item) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'editItemModal.html',
                controller: 'EditItemController',
                resolve: {
                    item: function () {
                        return item;
                    }
                }
            });
        };

        $scope.addItemAndEdit = function (item) {
            if ($scope.addItem(item)) {
                $scope.editItem(item);
            }
        };

        $scope.addItem = function(item) {
            if (!$scope.itemExists(item)) {
                $scope.items.push(item);
                return true;
            }
            return false;
        };

        $scope.showAddItemModal = function () {
            var modalInstance = $uibModal.open({
                size: 'lg',
                animation: true,
                templateUrl: 'selectProductModal.html',
                controller: 'SelectProductController'
            });
            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
                $scope.addItemAndEdit(selectedItem);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
            $scope.selectProduct = false;
        }
    })
    .controller('SelectProductController', function ($scope, $http, $modalInstance) {
        $scope.items = [];
        $scope.firstTime = true;

        $scope.query = { text: ''};

        $scope.selected = {};

        $scope.ok = function() {
            if ($scope.selected.item) {
                $modalInstance.close({quantity: 1, product: $scope.selected.item});
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
