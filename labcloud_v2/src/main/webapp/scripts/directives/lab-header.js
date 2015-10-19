'use strict';

/**
 * @ngdoc directive
 * @name morningStudioApp.directive:labHeader
 * @description
 * # labHeader
 */
angular.module('labcloud')
  .directive('labHeader', function () {
    return {
      templateUrl: 'templates/lab-header.html',
      restrict: 'E',
      controller: function ($scope, $rootScope, sessionService) {
        $scope.logout = function() {
          sessionService.delToken();
        };
      },
      link: function postLink(scope, element, attrs) {

      }
    };
  });
