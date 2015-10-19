'use strict';

/**
 * @ngdoc function
 * @name srcApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the srcApp
 */
angular.module('prjApp')
  .controller('LoginCtrl', function ($scope, $rootScope, $location, LoginService,$sessionStorage) {
        $scope.accountNumber= '';
        $scope.accountPassword = '';
        $scope.isKeepLogin = true;
        //$rootScope.message = '';
        $scope.accountCharacter = 'TEACHER';
        $scope.user = LoginService.getLoginUser();

        $scope.login = function(){
        	////console.log('login...');
        	if($scope.accountNumber.length > 0 &&  $scope.accountPassword.length >0){
        		var promise = LoginService.login($scope.accountNumber, $scope.accountPassword,$scope.accountCharacter);
        		promise.then(function(data){
        			LoginService.persitentLogin(data.data, data.token, $scope.accountCharacter);
                    if(data.errorCode == "Account_Not_Exist"){
                        $rootScope.message = "账号不存在";
                    }else if(data.errorCode == "Password_Wrong"){
                         $rootScope.message = "密码错误";
                    }else if(data.errorCode == "Account_Not_Active"){
                         $rootScope.message = "账户已注销";
                    }
                    if(data.errorCode == "No_Error"){
                         $rootScope.message = '';
                        if($rootScope.character == "STUDENT"){
                            $location.path('/my-experiment');
                        }else if($rootScope.character == "TEACHER"){
                             $location.path('/my-class');
                        }else if($rootScope.character == "ADMINISTRATOR"){//console.log( $sessionStorage.loginUser);
                             $location.path('/lab-user-manage');
                        }
                    }
        		},function(data){
        			$rootScope.message = data.errorCode;
        		});
        	}else{
        		$rootScope.message = "账号和密码不能为空";
        	}
        };
  }); 
