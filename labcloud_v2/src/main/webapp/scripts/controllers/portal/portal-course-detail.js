'use strict';

angular.module('labcloud')
  .controller('PortalCourseDetailCtrl', function($scope, $rootScope, $localStorage ) {
    $scope.userType = $localStorage.currentUser.show_role;;
  });