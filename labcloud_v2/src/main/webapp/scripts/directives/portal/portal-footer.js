'use strict';

angular.module('labcloud')
  .directive('portalFooter', function () {
    return {
      templateUrl: 'templates/portal/portal-footer.html',
      restrict: 'E'
    };
  });
