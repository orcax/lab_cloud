'use strict';

/**
 */
angular.module('prjApp')
  .filter('ReservationTypeFilter', function () {
    return function (input) {
       if(input == 'CLASS'){
       		return "班级预约";
       }else if(input == 'STUDENT'){
       		return "自行预约";
       }
    };
  })
  .filter('ReservationTypeClassFilter', function () {
    return function (input) {
       if(input == 'CLASS'){
       		return "label-warning";
       }else if(input == 'STUDENT'){
       		return "label-success";
       }
    };
  });