/**
 * Created by developer on 18/10/2015.
 */

angular.module('app', ['ngRoute'])
    .config(['$routeProvider', '$locationProvider',
        function ($routeProvider, $locationProvider) {
            $routeProvider.
                when('/', {
                    templateUrl: '/assets/templates/dashboard.html',
                    controller: 'DashboardController'
                }).
                when('/products/list/:filter', {
                    templateUrl: '/assets/templates/product_list.html',
                    controller: 'ProductListController'
                }).
                when('/products/list', {
                    templateUrl: '/assets/templates/product_list.html',
                    controller: 'ProductListController'
                }).
                when('/products/tree/:id', {
                    templateUrl: '/assets/templates/product_tree.html',
                    controller: 'ProductTreeController'
                }).
                when('/products/new', {
                    templateUrl: '/assets/templates/product_new.html',
                    controller: 'ProductNewController'
                }).
                when('/products/edit', {
                    templateUrl: '/assets/templates/product_edit.html',
                    controller: 'ProductEditController'
                }).
                otherwise({
                    redirectTo: '/'
                });
            $locationProvider.html5Mode(
                {
                    enabled: true,
                    requireBase: false
                });
        }
    ])

    .controller('DashboardController', function ($scope) {
        $scope.title = "Dashboard";
        $scope.subtitle = "Principal";
        $scope.message ='This is the dashboard';
    })

    .controller('ProductTreeController', function ($scope, $http) {
        $scope.title = "Producto";
        $scope.subtitle = "Estructura";

        $scope.drawCircular = function () {
            circular('#diagram','/api/products/tree/1770');
            //$http.get('/api/products/tree/1770').then(function (res))
        };

        $scope.drawCartesian = function () {
            cartesian('#diagram', '/api/products/tree/1770');
        };

        $scope.drawCircular();
        //$scope.drawCartesian();
    });

