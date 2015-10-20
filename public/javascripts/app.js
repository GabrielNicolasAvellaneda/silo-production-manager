/**
 * Created by developer on 18/10/2015.
 */

angular.module('app', [])
    .controller('DiagramCtrl', function ($scope, $http) {

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

