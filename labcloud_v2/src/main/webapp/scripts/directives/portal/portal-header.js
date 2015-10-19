'use strict';

angular.module('labcloud')
  .directive('portalHeader', function () {
  	var pathHash = {
  		"home": "/portal/home",
  		"course": "/portal/course",
      "class": "/portal/class",
  		"calendar": "/portal/calendar",
  		"notification": "/portal/notification"
  	};

  	function getPath(url) {
  		for(var path in pathHash) {
    		if(url.indexOf(pathHash[path]) > -1) {
    			return path;
    		}
    	}
    	return null;
  	}

    return {
      templateUrl: 'templates/portal/portal-header.html',
      restrict: 'E',
      controller: function ($scope, $localStorage, $rootScope, $location, sessionService) {
        $scope.userType = $localStorage.currentUser.show_role;

      	$scope.path = getPath($location.path());

        $rootScope.$on("$locationChangeStart", function(event, next, current) {
        	$scope.path = getPath(next);
	      });

        $scope.toOld = function(){
          if($scope.userType == 'TEACHER'){
            $location.path('/teacher/reservation');
          }else{
            $location.path('/student/reservation');
          }
        }

        $scope.logout = function() {
          sessionService.delToken();
        };
      }
    };
  });
