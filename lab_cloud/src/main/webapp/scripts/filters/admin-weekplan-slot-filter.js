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
    .filter('adminSlot1Style', function () {
        return function (dayResult) {
            if(dayResult.slot1OpenStatus != 'OPEN'  && dayResult.slot1OpenStatus != null) {
                    return "danger";
            }
            return "success";
        };
    })
    .filter('adminSlot2Style', function () {
        return function (dayResult) {
            if(dayResult.slot2OpenStatus != 'OPEN'  && dayResult.slot2OpenStatus != null) {
                    return "danger";
            }
            return "success";
        };
    })
    .filter('adminSlot3Style', function () {
        return function (dayResult) {
            if(dayResult.slot3OpenStatus != 'OPEN'  && dayResult.slot3OpenStatus != null) {
                    return "danger";
            }
            return "success";
        };
    })
    .filter('adminSlot4Style', function () {
        return function (dayResult) {
            if(dayResult.slot4OpenStatus != 'OPEN'  && dayResult.slot4OpenStatus != null) {
                    return "danger";
            }
            return "success";
        };
    })
    .filter('adminSlot5Style', function () {
        return function (dayResult) {
            if(dayResult.slot5OpenStatus != 'OPEN'  && dayResult.slot5OpenStatus != null) {
                    return "danger";
            }
            return "open";
        };
    })
    .filter('adminSlot6Style', function () {
        return function (dayResult) {
            if(dayResult.slot6OpenStatus != 'OPEN'  && dayResult.slot6OpenStatus != null) {
                    return "danger";
            }
            return "open";
        };
    });
