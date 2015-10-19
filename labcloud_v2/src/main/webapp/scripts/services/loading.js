'use strict';

/**
 * @ngdoc service
 * @name iocUiApp.loading
 * @description
 * # loading
 * Service in the iocUiApp.
 */
angular.module('labcloud')
  .factory('loadingHttpInterceptor', function loadingHttpInterceptor ($q, 
    $timeout, $localStorage, $location) {
    return {
      'request': function(config) {
        if($localStorage.currentUser == null || $localStorage.token == undefined){
          $location.path('/login');
        }
        $.isLoading();
        return config || $q.when(config);
      },
      'requestError': function(config) {
        $timeout(function() {
          $.isLoading('hide');
        }, 300);
        // do something on error
        // if (canRecover(rejection)) {
        //   return responseOrNewPromise
        // }
        return $q.reject(rejection);
      },
      'response': function(response) {
        $timeout(function() {
          $.isLoading('hide');
        }, 300);
        // do something on success
        return response || $q.when(response);
      },
      'responseError': function(rejection) {
        $timeout(function() {
          $.isLoading('hide');
        }, 300);
        // do something on error
        // if (canRecover(rejection)) {
        //   return responseOrNewPromise
        // }
        return $q.reject(rejection);
      }
    };
  });