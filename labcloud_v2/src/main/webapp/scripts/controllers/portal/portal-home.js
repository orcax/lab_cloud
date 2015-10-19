'use strict';

angular.module('labcloud')
  .controller('PortalHomeCtrl', function($scope, $location, $rootScope, $localStorage) {
  	$scope.userType = $localStorage.currentUser.show_role;

    // 测试数据
    $scope.expList = [
	    {
	    	title: '扭转实验',
	    	date: new Date(),
	    	place: '本部L218',
	    	classTime: '3,4节课程',
	    	classNumber: '班级A编号',
	    	teacher: '教师名称'
	    },
	    {
	    	title: '扭转实验',
	    	date: new Date(),
	    	place: '本部L218',
	    	classTime: '3,4节课程',
	    	classNumber: '班级A编号',
	    	teacher: '教师名称'
	    },
	    {
	    	title: '扭转实验',
	    	date: new Date(),
	    	place: '本部L218',
	    	classTime: '3,4节课程',
	    	classNumber: '班级A编号',
	    	teacher: '教师名称'
	    }
    ];

    $scope.sortTypeList = [
    	{
    		'value': 1,
    		'name': '按照实验日期排列'
    	},
    	{
    		'value': 2,
    		'name': '按照实验名称排列'
    	},
    	{
    		'value': 3,
    		'name': '按照班级排列'
    	}
    ];
    $scope.sortType = $scope.sortTypeList[0];

    // 点击整个实验tile触发
    $scope.onClickExp = function (index) {
    	$location.path('/portal/experiment/' + index);
    };
    // 点击查看虚拟实验出发
    $scope.onClickView = function (event) {
    	event.stopPropagation();
    	event.preventDefault();
    };
    // 点击取消预约出发
    $scope.onClickCancel = function (event) {
    	event.stopPropagation();
    	event.preventDefault();
    };
  });