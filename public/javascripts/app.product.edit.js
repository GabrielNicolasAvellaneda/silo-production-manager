angular.module('app')
    .controller('ProductEditController', function ($scope, $http, $location, $log, $routeParams, $uibModal, Data, $q, $timeout) {
        $scope.title = 'Producto';
        $scope.subtitle = "Editar";
        $scope.product = {};
        $scope.alerts = [];

        $scope.load = function (id) {
            $http.get('/api/products/get/' + id).then(function (result) {
                angular.extend($scope.product, result.data);
            });
        };

        $scope.load($routeParams.id);

        $scope.productKinds = [];
        $scope.productUnits = [];

        $scope.getProductKinds = function () {
            $http.get('/api/products/kinds').then(function (result) {
                angular.extend($scope.productKinds, result.data);
            });
        };
        $scope.getProductKinds();

        $scope.getProductUnits = function () {
            $http.get('/api/products/units').then(function (result) {
                angular.extend($scope.productUnits, result.data);
            });
        };

        $scope.getProductUnits();

        $scope.selectProduct = false;

        $scope.removeItem = function(item) {
            var index = $scope.product.items.indexOf(item);
            $scope.product.items.splice(index, 1);
        };

        $scope.itemExists = function(searchItemProductId) {
            for (var i=0; i < $scope.product.items.length; i++) {
                var item = $scope.product.items[i];
                if (item.productId == searchItemProductId) {
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
            console.log('item', item);
            if ($scope.addItem(item)) {
                $scope.editItem(item);
            } else {
                alert('Can not add this item!');
            }
        };


        $scope.addItem = function(item) {
            if (!$scope.itemExists(item)) {
                $scope.product.items.push(item);
                return true;
            }
            return false;
        };

        var UI = {
            openShowAddItemModal: function () {
                return $uibModal.open({
                    size: 'lg',
                    animation: true,
                    templateUrl: 'selectProductModal.html',
                    controller: 'SelectProductController'
                });
            }
        };

        $scope.showAddItemModal = function () {
            var modalInstance = UI.openShowAddItemModal();
            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
                $scope.addItemAndEdit(selectedItem);
            }, function () {
                $log.info('Modal dismissed at: ' + new Date());
            });
            $scope.selectProduct = false;
        };

        $scope.closeAlert = function(index) {
            $scope.alerts.splice(index, 1);
        };

        $scope.save = function () {
            $log.info($scope.product);
            $http.post('/api/products/update', $scope.product).then(function (response) {
                $scope.alerts.push({msg: "Se han actualizado los datos del producto y se generó un proceso de actualización de costos."});
            }, function (response) {
                console.log(response);})
        };

    });
