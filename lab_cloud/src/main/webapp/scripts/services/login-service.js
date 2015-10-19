'use strict';

/**
 * @ngdoc service
 * @name prjApp.LoginService
 * @description
 * # LoginService
 * Service in the prjApp.
 */
angular.module('prjApp')
    .service('LoginService', function LoginService($http, $q, $rootScope, $location, $localStorage, $sessionStorage) {
        
        this.saveCurrentUser = function (user, password, isKeepLogin) {
            $location.path('/');
        };

        this.login = function (userName, userPWD, character) {
            var deferred = $q.defer();
             $http.post('/Account/login',{
                                "accountCharacter": character,
                                "data": {
                "number": userName,
                "password": userPWD
            }
         }) .success(function (data, status, headers, config) {
                    deferred.resolve(data);
                    //console.log(data);
                })
                .error(function (data, status, headers, config) {
                    deferred.reject(data);
                });
            return deferred.promise;
        };

        this.logout = function () {
            var deferred = $q.defer();
            $http.post('/Account/logout', {'token': $rootScope.token, "accountCharacter":$rootScope.character})
                .success(function (data, status, headers, config) {
                    deferred.resolve(data);
                    $rootScope.message = '';
                })
                .error(function (data, status, headers, config) {
                    deferred.reject(data);
                });
            return deferred.promise;
        };

        this.clearSessionStorage = function(){
            delete $rootScope.loginUser;
            delete $sessionStorage.loginUser;
            delete $rootScope.token;
            delete $sessionStorage.token;
            delete $sessionStorage.character;
            delete $rootScope.character;
            $location.path('/');
        }

        this.persitentLogin = function(user, token, character){
            $rootScope.loginUser = user;
            $rootScope.token = token;
            if(user){
                $rootScope.character = character;
            }
            $sessionStorage.loginUser = user;
            $sessionStorage.token = token;
            $sessionStorage.character = character;
        };

        this.getLoginUser = function(){
            if($rootScope.loginUser != undefined && $rootScope.token != undefined){
                return $rootScope.loginUser;
            }else if($sessionStorage.loginUser != undefined){
                $rootScope.loginUser = $sessionStorage.loginUser;
                $rootScope.token = $sessionStorage.token;
                $rootScope.character = $sessionStorage.character;
                return $sessionStorage.loginUser;
            }else{
                return false;
            }
        };
    });
