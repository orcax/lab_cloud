'use strict';

angular.module('labcloud')
  .controller('PortalClassDetailCtrl', function($scope, $location) {
  	$scope.onClickCourseItem = function (index) {
  		$location.path('/portal/course/' + index);
  	};
  });