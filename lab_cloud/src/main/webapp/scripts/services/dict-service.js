'use strict';

/**
 * @ngdoc service
 * @name prjApp.DictService
 * @description
 * # DictService
 * Service in the prjApp.
 */
angular.module('prjApp')
  .service('DictService', function DictService() {

    this.getRoleDict = function(){
    	var userRoles = [
            {
                name: "学生",
                id: 'STUDENT'
            },
            {
                name: "管理员",
                id: 'ADMINISTRATOR'
            }, {
                name: "教师",
                id: 'TEACHER'
            } 
        ];
        return userRoles;
    };

    this.getLabStatusDict = function(){
        var labStatuses = [
            {
                name:"开放",
                id:"OPEN"
            },
            {
                name:"关闭",
                id:"CLOSED"
            }
        ];
        return labStatuses;
    };

    this.getSlotKey = function(slot_num){
        var slotKeyMap = {
            'slot':'',
            'value':''
        };
        switch(slot_num) {
            case 1:
                slotKeyMap.slot = 'ONE';
                slotKeyMap.value = '1,2 节';
                break;
            case 2:
                slotKeyMap.slot = 'TWO';
                slotKeyMap.value = '3,4 节';
                break;
            case 3:
                slotKeyMap.slot = 'THREE';
                slotKeyMap.value = '中午';
                break;
            case 4:
                slotKeyMap.slot = 'FOUR';
                slotKeyMap.value = '5,6 节';
                break;
            case 5:
                slotKeyMap.slot = 'FIVE';
                slotKeyMap.value = '7,8 节';
                break;
            case 6:
                slotKeyMap.slot = 'SIX';
                slotKeyMap.value = '晚上(上)';
                break;
            case 7:
                slotKeyMap.slot = 'SEVEEN';
                slotKeyMap.value = '晚上(下)';
                break;
        };
        return slotKeyMap;
    };

  });
