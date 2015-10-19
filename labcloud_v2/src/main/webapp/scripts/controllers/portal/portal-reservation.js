'use strict';

angular.module('labcloud')
  .controller('PortalReservationCtrl', function($scope, $rootScope, $localStorage) {
    $scope.userType = $localStorage.currentUser.show_role;

    $scope.reservation = {
      date: new Date(),
      time: new Date()
    };

  	$scope.resList = [
  		{
        name: '扭转实验',
        items: [
          {
            date: new Date(),
            place: '本部L218',
            classTime: '3,4节课程',
            status: 0 //0:已被预约 1:可以预约
          },
          {
            date: new Date(),
            place: '本部L218',
            classTime: '3,4节课程',
            status: 1 //0:已被预约 1:可以预约
          }
        ]
  		},
      {
        name: '扭转实验',
        items: [
          {
            date: new Date(),
            place: '本部L218',
            classTime: '3,4节课程',
            status: 0 //0:已被预约 1:可以预约
          },
          {
            date: new Date(),
            place: '本部L218',
            classTime: '3,4节课程',
            status: 1 //0:已被预约 1:可以预约
          }
        ]
      }
  	];
  });