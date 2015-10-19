'use strict';

angular.module('labcloud')
  .controller('PortalClassCtrl', function($scope, $location) {

  	$scope.onClickClassItem = function (index) {
  		$location.path('/portal/class/' + index);
  	};

  });