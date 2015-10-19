'use strict'

angular.module('labcloud').config(
  ['$ocLazyLoadProvider', function($ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
      debug:  false,
      events: true,
      modules: [
        {
            name: 'oitozero.ngSweetAlert',
            files: [
                'bower_components/angular-sweetalert/SweetAlert.js',
                'bower_components/sweetalert/lib/sweet-alert.css'
            ]
        },
        {
            name:'angularFileUpload',
            files: [
              'bower_components/ng-file-upload/angular-file-upload.js',
              'bower_components/ng-file-upload-shim/angular-file-upload-shim.js'
            ]
        }
      ]
    });
}]);