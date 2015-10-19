'use strict';

/**
 * @ngdoc directive
 * @name prjApp.directive:tabHeading
 * @description
 * # tabHeading
 */
angular.module('prjApp')
  .directive('tabHeading', function () {
    return {
      template: '<div></div>',
      restrict: 'E',
      link: function postLink(scope, element, attrs) {
        element.text('this is the tabHeading directive');
      }
    };
  });
