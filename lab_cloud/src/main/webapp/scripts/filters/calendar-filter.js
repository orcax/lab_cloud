'use strict';

angular.module('prjApp')
  .filter('calendarTdClass', function () {
    return function (input) {
    	if(input == 'PENDING'){
            return 'warning';
        }else if(input == 'OPEN'){
            return 'default';
        }else if(input == 'APPROVED'){
            return 'danger';
        }else if(input == 'CLOSED'){
            return 'closed';
        }else{
            return '';
        }
    };
  })
  .filter('stCalendarTdClass', function(){
    return function(lst){
        if(lst==null || lst.length <=0){
            return '';
        }else if(lst.length > 0){
            return 'warning';
        }
    };
  })
  .filter('weekDayFilter', function(){
    return function(input){
        switch(input){
            case 1:
                return '周一';
                break;
            case 2:
                return '周二';
                break;
            case 3:
                return '周三';
                break;
            case 4:
                return '周四';
                break;
            case 5:
                return '周五';
                break;
            case 6:
                return '周六';
                break;
            case 0:
                return '周日';
                break;
        };
    };
  });