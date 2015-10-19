'use strict';

/**
 * @ngdoc service
 * @name iocUiApp.loading
 * @description
 * # loading
 * Service in the iocUiApp.
 */
angular.module('prjApp')
  .factory('loadingHttpInterceptor', function loadingHttpInterceptor($q, $timeout) {
    return {
      'request': function(config) {
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