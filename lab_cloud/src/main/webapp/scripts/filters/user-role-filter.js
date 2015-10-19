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
  .filter('userRole', function (DictService) {
    return function (input) {
    	var roles = DictService.getRoleDict();
    	var name = '';
    	for(var i in roles){
    		var role = roles[i];
    		if(role.id == input){
    			name = role.name;
    		}
    	}
    	return name;
    };
  })
  .filter('teacherRole', function(){
    return function(input){
        if(input=='ALL'){
            return '教师兼实验教师';
        }else if(input=='NORMAL'){
            return '普通教师';
        }else if(input=='LAB'){
            return '实验教师';
        }
    };
  })
  .filter('genderFilter', function(){
    return function(input){
        if(input=='MALE'){
            return '男';
        }else if(input=='FEMALE'){
            return '女';
        }
    };
  })
  .filter('isLabTeacher', function(){
    return function(input){
        if(input=='LAB'|| input =='ALL'){
            return true;
        }else{
            return false;
        }
    };
  })
  .filter('isNormalTeacher', function(){
    return function(input){
        if(input=='ALL'|| input =='NORMAL'){
            return true;
        }else{
            return false;
        }
    };
  });
