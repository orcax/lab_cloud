'use strict';

/**
 * @ngdoc function
 * @name morningStudioApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the morningStudioApp
 */
angular.module('labcloud')
  .controller('MainController', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
