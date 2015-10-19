'use strict';

/**
 * @ngdoc directive
 * @name prjApp.directive:labFooter
 * @description
 * # labFooter
 */
angular.module('prjApp')
  .directive('labFooter', function () {
    return {
      templateUrl: 'template/lab-footer.html',
      restrict: 'E',
      link: function postLink(scope, element, attrs) {

      }
    };
  });
