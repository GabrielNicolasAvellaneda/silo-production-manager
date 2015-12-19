/**
 * Created by developer on 18/10/2015.
 */

angular.module('app', ['ngRoute', 'ngAnimate', 'ui.bootstrap', 'dataServices'])
    .directive('onEnterKey', function () {
        return function (scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if(event.which === 13) {
                    scope.$apply(function (){
                        scope.$eval(attrs.onEnterKey);
                    });

                    event.preventDefault();
                }
            });
        };
    })
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
                when('/products/view/:id', {
                    templateUrl: '/assets/templates/product_view.html',
                    controller: 'ProductViewController'
                }).
                when('/products/new', {
                    templateUrl: '/assets/templates/product_new.html',
                    controller: 'ProductNewController'
                }).
                when('/products/edit/:id', {
                    templateUrl: '/assets/templates/product_edit.html',
                    controller: 'ProductEditController'
                }).
                otherwise({
                    redirectTo: '/'
                });
/*            $locationProvider.html5Mode({
                enabled: true,
                requireBase: false
            });*/
        }
    ])

    .controller('DashboardController', function ($scope, $http) {
        $scope.title = "Dashboard";
        $scope.subtitle = "Principal";
        $scope.message ='This is the dashboard';

        $http.get('/api/products/dashboard').then(function (result) {
           $scope.dashboard = result.data;
        });
    })

    .controller('ProductTreeController', function ($scope, $http, $routeParams) {
        $scope.title = "Producto";
        $scope.subtitle = "Estructura";

        $scope.drawTree = function () {
            drawTree('#diagram', '/api/products/tree/' + $routeParams.id);
            //drawTree('#diagram','/api/products/tree/1770ll');
        };

        $scope.drawTree();
    });

