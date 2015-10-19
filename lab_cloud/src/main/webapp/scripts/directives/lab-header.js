'use strict';

/**
 * @ngdoc directive
 * @name prjApp.directive:labHeader
 * @description
 * # labHeader
 */
angular.module('prjApp')
  .directive('labHeader', function () {
    return {
      templateUrl: 'template/lab-header.html',
      restrict: 'E',
      replace: true,
      controller: function ($scope, $rootScope, $location, LoginService) {
      	$scope.user = LoginService.getLoginUser();
      	if(!$scope.user){
      		$location.path('/login');
      	}
      	$scope.logout = function(){
      		var promise = LoginService.logout();
      		promise.then(function(data){
      			LoginService.clearSessionStorage();
      		},function(data){
      			//console.log("logout failed!");
      		});
      	}
      },
      link: function postLink(scope, element, attrs) {

      }
    };
  });

