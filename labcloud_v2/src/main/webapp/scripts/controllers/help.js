'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:HelpCtrl
 * @description
 * # HelpCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('HelpCtrl', function ($scope, $rootScope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
