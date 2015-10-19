'use strict';

/**
 */
angular.module('prjApp')
  .filter('FileNameFromPath', function () {
    return function (input) {
       var filename = input.replace(/^.*[\\\/]/, '')
       return filename;
    };
  });