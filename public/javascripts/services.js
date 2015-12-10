var dataServices = angular.module('dataServices', ['ngResource']);

dataServices.factory('Data', ['$resource',
    function ($resource) {
        return $resource('api/products/get/:productId', {}, {
            getProduct: {method: 'GET', params:{productId:'productId'}}
        });
    }]);