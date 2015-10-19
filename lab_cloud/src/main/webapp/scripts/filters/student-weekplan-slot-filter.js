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
  .filter('stExpStatus', function () {
    return function (exp) {
        if(exp.fileRecords.length > 0 && exp.experimentRecord !=null){
            return '教师已评';
        }else if(exp.experimentRecord !=null){
            return '教师已评';
        }
        else if(exp.fileRecords.length > 0){
            return '实验已做';
        }else{
            return '待完成';
        }
    };
  }).filter('stExpStatusClass', function () {
    return function (exp) {
        if(exp.fileRecords.length > 0 && exp.experimentRecord !=null){
            return 'label-success';
        }else if(exp.experimentRecord !=null){
            return 'label-success';
        }
        else if(exp.fileRecords.length > 0){
            return 'label-warning';
        }else{
            return 'label-default';
        }
    };
  }).filter('stExpStatusAlready',function(){
    return function (exp) {
        var count = 0;
        for (var i = 0; i < exp.fileRecords.length; i++) {
            if(exp.fileRecords[i].type == 'expData'){
                count++;
            }
        };
        if(count > 0){
            return true;
        }else{
            return false;
        }
    };
  });