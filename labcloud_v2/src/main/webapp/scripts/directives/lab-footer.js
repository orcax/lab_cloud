'use strict';

/**
 * @ngdoc directive
 * @name labcloud.directive:labfooter
 * @description
 * # labfooter
 */
angular.module('labcloud')
  .directive('labFooter', function () {
    return {
      templateUrl: 'templates/lab-footer.html',
      restrict: 'E',
      controller: function ($scope, $rootScope, $location) {
        
      },
      link: function postLink(scope, element, attrs) {

      }
    };
  });
