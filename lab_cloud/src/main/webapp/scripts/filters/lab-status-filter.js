'use strict';

/**
 * @ngdoc filter
 * @name prjApp.filter:labStatusFilter
 * @function
 * @description
 * # labStatusFilter
 * Filter in the prjApp.
 */
angular.module('prjApp')
  .filter('labStatusFilter', function (DictService) {
    return function (input) {
    	var dict = DictService.getLabStatusDict();
    	var name = '';
    	for(var i in dict){
    		var node = dict[i];
    		if(node.id == input){
    			name = node.name;
    		}
    	}
    	return name;
    };
  })
  .filter('labStatusClassFilter', function(){
    return function (input){
        if(input=='CLOSED' || input == false){
            return 'default';
        }else{
            return 'success';
        }
    };
  })
  .filter('labOpenFilter', function(){
    return function (input){
        if(input==false){
            return '关闭';
        }else{
            return '开放';
        }
    };
  })
  .filter('labSlotFilter', function(){
    return function(input){
        var value = "";
        switch(input){
            case 'ONE':
                value = "1,2节";
                break;
            case 'TWO':
                value = "3,4节";
                break;
            case 'THREE':
                value = "中午";
                break;
            case 'FOUR':
                value = "7,8节";
                break;
            case 'FIVE':
                value = "8,9节";
                break;
            case 'SIX':
                value = "晚上(上)";
                break;
            case 'SEVEN':
                value = "晚上(下)";
                break;
            default:
                value = "";
        }
        return value;
    };
  })
  .filter('reservationStatusFilter', function(){
    return function(input){
        if(input==null || input==undefined){
            return '未预约';
        }else{
            return '已预约';
        }
    };
  });
