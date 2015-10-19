'use strict';

/**
 * @ngdoc filter
 * @name prjApp.filter:userRoleFilter
 * @function
 * @description
 * # userRoleFilter
 * Filter in the prjApp.
 */
angular.module('prjApp')
  .filter('teacherSlot1Style', function () {
    return function (dayResult) {
        for(var i = 0;i != dayResult.length;i++) {
            if(dayResult[i].slot1OpenStatus != 'OPEN' && dayResult[i].slot1OpenStatus != null) {
                return "danger";
            }
        }
        return "success";
    };
  })
  .filter('teacherSlot2Style', function () {
    return function (dayResult) {
        for(var i = 0;i != dayResult.length;i++) {
            if(dayResult[i].slot2OpenStatus != 'OPEN' && dayResult[i].slot2OpenStatus != null) {
                return "danger";
            }
        }
        return "success";
    };
  })
  .filter('teacherSlot3Style', function () {
    return function (dayResult) {
        for(var i = 0;i != dayResult.length;i++) {
            if(dayResult[i].slot3OpenStatus != 'OPEN'  && dayResult[i].slot3OpenStatus != null) {
                return "danger";
            }
        }
        return "success";
    };
  })
  .filter('teacherSlot4Style', function () {
    return function (dayResult) {
        for(var i = 0;i != dayResult.length;i++) {
            if(dayResult[i].slot4OpenStatus != 'OPEN'  && dayResult[i].slot4OpenStatus != null) {
                return "danger";
            }
        }
        return "success";
    };
  })
  .filter('teacherSlot5Style', function () {
    return function (dayResult) {
        for(var i = 0;i != dayResult.length;i++) {
            if(dayResult[i].slot5OpenStatus != 'OPEN'  && dayResult[i].slot5OpenStatus != null) {
                return "danger";
            }
        }
        return "open";
    };
  })
  .filter('teacherSlot6Style', function () {
    return function (dayResult) {
        for(var i = 0;i != dayResult.length;i++) {
            if(dayResult[i].slot6OpenStatus != 'OPEN'  && dayResult[i].slot6OpenStatus != null) {
                return "danger";
            }
        }
        return "open";
    };
  });
