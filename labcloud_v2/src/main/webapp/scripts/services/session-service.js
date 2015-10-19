'use strict';

angular.module('labcloud').service('sessionService',
	function categoryService ($localStorage, $location, 
		$rootScope, tokenFactory, qService, $route) {

		function gCurrSemester(){
			if($localStorage.semester){
				$rootScope.semester = $localStorage.semester;
				return $localStorage.semester;
			}else{
				return null;
			}
		};

		function checkLocalToken(){
			//这边比较token用$localstorage,因为$rootScope一刷新页面就清空了
			if (!$localStorage.token  || !$localStorage.currentUser) {
				
				$location.path('/login');
				return false;
			}else{
				$rootScope.currentUser = $localStorage.currentUser;
				$rootScope.token = $localStorage.token;
			}
		};

		this.checkToken = function() {
			return checkLocalToken();
		};

		this.getCurrSemeter = function(){
			return gCurrSemester();
		};

		this.storageChecking = function(){
			 checkLocalToken();
			 checkLocalToken();
		};

		this.saveCurrSemeter = function(semester){
			$localStorage.semester = semester;
			$rootScope.semester = semester;
		};

		this.saveToken = function(user, token) {
			var user = wrapperUser(user);
			$localStorage.currentUser = user;
			$localStorage.token = token;
			$rootScope.currentUser = user;
			$rootScope.token = token;
			if(user.show_role == 'ADMINISTRATOR'){
				$location.path('/usermanage');	
			}else if(user.show_role == 'TEACHER'){
				$location.path('/teacher/reservation');	
			}else{
				$location.path('/student/reservation');	
			}
		};


		function wrapperUser(user){
			if(user.role == 'STUDENT' || user.role =='ADMINISTRATOR'){
				user.show_role = user.role;
			}else{
				user.show_role = 'TEACHER';
			}
			return user;
		};

		this.delToken = function() {
			delete $localStorage.currentUser;
			delete $localStorage.token;
			delete $rootScope.currentUser;
			delete $rootScope.token;
			delete $localStorage.semester;
			delete $rootScope.semester;
			$location.path('/login');
		};
		this.headers = function(){
			return {
      	'x-auth-token': $localStorage.token
    	};
		};
	}
);