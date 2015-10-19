'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:TeacherReservationCtrl
 * @description
 * # TeacherReservationCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('TeacherReservationCtrl', function($scope, $rootScope, modalService,
    qService, Reservation, Clazz, Semester, sessionService, generalService, $modal,
    uiCalendarConfig, $timeout, $compile) {
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    sessionService.storageChecking();
    var semester = sessionService.getCurrSemeter();

    $scope.map = {
      Reservations: {
        'PENDING': {
          data: {
            curPageNum: 1,
            totalItemNum: 0,
            totalPageNum:0,
            data: []
          }
        },
        'APPROVED': {
          data: {
            curPageNum: 1,
            totalItemNum: 0,
            totalPageNum:0,
            data: []
          }
        },
        'REJECTED': {
          data: {
            curPageNum: 1,
            totalItemNum: 0,
            totalPageNum:0,
            data: []
          }
        }
      },
      Calender: {

      }
    };
    var loadFactory = {};
    loadFactory.Reservations = function(status) {
      qService.tokenHttpGet(Reservation.teacherResByStatusPage, {
        'semester': semester.id,
        'accountId': $rootScope.currentUser.id,
        'pageSize': generalService.pageSize(),
        'pageNumber': $scope.map.Reservations[status].data.curPageNum,
        'status': status
      }).then(function(rc) {
        $scope.map.Reservations[status].data = rc;
      })
    };

    loadFactory.Calender = function(status) {

    };

    $scope.load = function(type, status) {
      if (type == 'Reservations' && $scope.map.Reservations[status].data.totalItemNum > 0) {
        return;
      } else {
        loadFactory[type](status);
      }
    };

    $scope.form = function(reservation) {
      var dialog = modalService.mdDialog(
        '/templates/common/reservation-details-dialog.html',
        'ReservationDetailModalCtrl', reservation);
    };
    $scope.cancel = function(reservation) {
      modalService.deleteConfirmInform(function() {
        alert("这个功能未开放，请联系管理员拒绝该预约!");
      });
    };
    $scope.addReservation = function() {
      var dialog = $modal.open({
        templateUrl: '/templates/common/teacher-reservation-dialog.html',
        controller: 'ClazzReservationModalCtrl',
        resolve: {
          data: function() {
            return {};
          },
          clazzs: function(qService, Clazz) {
            return qService.tokenHttpGet(Clazz.teacherClazzs, {
              'teacherId':$rootScope.currentUser.id,
              'semesterId':sessionService.getCurrSemeter().id
            }); //获取教师本人的clazz
          },
          semester: function(sessionService) {
            return sessionService.getCurrSemeter();
          },
          slots: function(qService, Semester) {
            return qService.tokenHttpGet(Semester.slots, {});
          }
        }
      });

      dialog.result.then(function(rc) {
        $scope.refreshCalendar();
      });
    };


    /**************************************
     * 日历定义
     ***************************************/
    $scope.events = [];
    $scope.eventSources = [$scope.events];

    $scope.labTeacherSources = [];
    $scope.timeSpan = {
      'changeCount': 0,
      'start': null,
      'end': null,
      'type': 'agendaWeek'
    };
    $scope.refreshCalendar = function() {
      $scope.events.splice(0, $scope.events.length);
      $scope.loadCalendar();//todate
    };
    $scope.add = function(res) {

    };
    $scope.loadCalendar = function() {
      qService.tokenHttpGet(Reservation.allTeacherRes, {
        "semesterId": semester.id,
        "teacherId": $rootScope.currentUser.id
      }).then(function(rc) {
        var list = rc.data;
        var rcList = [];
        for (var i = 0; i < list.length; i++) {
          var res = list[i];
          var type = "";
          if (res.clazz == undefined) {
            type = 'student';
          } else {
            type = 'clazz';
          }
          var map = {
            'id': res.id,
            'title': res.lab.name + '-' + res.experiment.name,
            'start': new Date(res.applyDate + ' ' + res.slot.startTime),
            'end': new Date(res.applyDate + ' ' + res.slot.endTime),
            'color': generalService.getReservationColor(res),
            'status': res.status,
            'type': type,
            'data':res
          };
          rcList.push(map);
        };
        $scope.eventSources.splice(0, $scope.eventSources.length);
        $scope.addRemoveEventSource($scope.eventSources, rcList);
        $scope.renderCalender('myCalendar');
      });
    };

    $scope.loadLabTeacherCalendarData = function(){
      qService.tokenHttpGet(Reservation.allLabTeacherRes, {
        "semesterId": semester.id,
        "teacherId": $rootScope.currentUser.id
      }).then(function(rc) {
        var list = rc.data;
        var rcList = [];
        for (var i = 0; i < list.length; i++) {
          var res = list[i];
          var type = "";
          if (res.clazz == undefined) {
            type = 'student';
          } else {
            type = 'clazz';
          }
          var map = {
            'id': res.id,
            'title': res.lab.name + '-' + res.experiment.name,
            'start': new Date(res.applyDate + ' ' + res.slot.startTime),
            'end': new Date(res.applyDate + ' ' + res.slot.endTime),
            'color': generalService.getReservationColor(res),
            'status': res.status,
            'type': type,
            'data':res
          };
          rcList.push(map);
        };
        $scope.labTeacherSources.splice(0, $scope.labTeacherSources.length);
        $scope.addRemoveEventSource($scope.labTeacherSources, rcList);
        $scope.renderCalender('myCalendar2');
      });
    };


    /* alert on Drop */
    $scope.alertOnDrop = function(event, delta, revertFunc, jsEvent, ui, view) {
      $scope.alertMessage = ('Event Droped to make dayDelta ' + delta);
    };
    /* alert on Resize */
    $scope.alertOnResize = function(event, delta, revertFunc, jsEvent, ui, view) {
      $scope.alertMessage = ('Event Resized to make dayDelta ' + delta);
    };
    /* add and removes an event source of choice */
    $scope.addRemoveEventSource = function(sources, source) {
      var canAdd = 0;
      angular.forEach(sources, function(value, key) {
        if (sources[key] === source) {
          sources.splice(key, 1);
          canAdd = 1;
        }
      });
      if (canAdd === 0) {
        sources.push(source);
      }
    };


    /* remove event */
    $scope.remove = function(index) {
      $scope.events.splice(index, 1);
    };
    /* Change View */
    $scope.changeView = function(view, calendar) {
      uiCalendarConfig.calendars[calendar].fullCalendar('changeView', view);
    };
    $scope.alertOnEventClick = function(data, jsEvent, view) {
       var dialog = modalService.mdDialog('/templates/common/reservation-details-dialog.html',
          'ReservationDetailModalCtrl',data.data);
       dialog.result.then(function(rc){
        if(rc=='refresh'){
          $scope.refreshCalendar();
        }

       });
    };
    $scope.renderCalender = function(calendar) {
      if (uiCalendarConfig.calendars[calendar]) {
        uiCalendarConfig.calendars[calendar].fullCalendar('render');
      }
    };
    $scope.eventRender = function(event, element, view) {
      element.attr({
        'tooltip': event.title,
        'tooltip-append-to-body': true
      });
      $compile(element)($scope);
    };


    $scope.select = function(start, end, jsEvent, view) {
      dialogFactory.class();
    };

    $scope.uiConfig = {
      calendar: {
        height: 900,
        // editable: true,
        header: {
          left: 'month,agendaWeek,agendaDay',
          center: 'title',
          right: 'today prev,next'
        },
        lang: 'zh-cn',
        selectable: false,
        slotDuration: '01:00:00',
        allDaySlot: false,
        minTime: "08:00:00",
        maxTime: "23:00:00",
        defaultView: 'agendaWeek',
        select: $scope.select,
        eventClick: $scope.alertOnEventClick,
        eventDrop: $scope.alertOnDrop,
        eventResize: $scope.alertOnResize,
        eventRender: $scope.eventRender,
        viewRender: function(view, element) {
          var start = view.start.subtract(8, 'hours');
          var end = view.end.subtract(8, 'hours').subtract(10, 'seconds');
          var needRefresh = false;
          console.log('load res..');
          $scope.loadCalendar();
          console.log('finish load res..');
        }
      },
      labTeacherCalendar: {
        height: 900,
        // editable: true,
        header: {
          left: 'month,agendaWeek,agendaDay',
          center: 'title',
          right: 'today prev,next'
        },
        lang: 'zh-cn',
        selectable: false,
        slotDuration: '01:00:00',
        allDaySlot: false,
        minTime: "08:00:00",
        maxTime: "23:00:00",
        defaultView: 'agendaWeek',
        eventClick: $scope.alertOnEventClick,
        viewRender: function(view, element) {
          var start = view.start.subtract(8, 'hours');
          var end = view.end.subtract(8, 'hours').subtract(10, 'seconds');
          var needRefresh = false;
          console.log('load res..');
          $scope.loadLabTeacherCalendarData();
          console.log('finish load res..');
        }
      }
    };
    $scope.loadLabTeacherCalendar = function(){
      if($rootScope.currentUser.role == 'ALL_TEACHER' || $rootScope.currentUser.role == 'LAB_TEACHER'){
        $timeout(function() {
          $scope.renderCalender('myCalendar2');
        }, 1000);
      }else{
        modalService.signleConfirmInform('你不是实验教师','无权限访问这个页面','warning', function(){});
      }
    };

    $scope.toCalenderView = function() {
      $timeout(function() {
        $scope.renderCalender('myCalendar');
      }, 1000);
    };
    
    if($rootScope.currentUser.role == 'LAB_TEACHER'){
      $scope.loadLabTeacherCalendar();
    }else{
      $scope.toCalenderView();  
    }

  });