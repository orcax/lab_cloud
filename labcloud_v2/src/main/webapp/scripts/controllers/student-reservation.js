'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:StudentReservationCtrl
 * @description
 * # StudentReservationCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('StudentReservationCtrl', function ($scope, $rootScope, $localStorage, modalService,
    qService, Reservation, Clazz, Semester, sessionService, generalService ,$modal,
    uiCalendarConfig, $timeout, $compile) {
    var semester = sessionService.getCurrSemeter();
    sessionService.checkToken();    
    $scope.map = {
      'reservation':{
        'clazz':{
          data:{
            curPageNum: 1,
            totalItemNum: 0,
            totalPageNum: 0,
            data:[]
          }
        },
        'student':{
          data:{
            curPageNum: 1,
            totalItemNum: 0,
            totalPageNum: 0,
            data:[]
          }
        }
      },
      'grabRes':{//可以抢的预约
        data:{
            curPageNum: 1,
            totalItemNum: 0,
            totalPageNum: 0,
            data:[]
          }
      },
      'calendar':{//日期配置

      }
    };

    var loadFactory  = {};
    loadFactory.reservation = function(type){
      qService.tokenHttpGet(Reservation.studentResByStatusPage,{
        'semester':semester.id,
        'accountId': $rootScope.currentUser.id,
        'pageSize':generalService.pageSize(),
        'pageNumber': $scope.map.reservation[type].data.curPageNum,
        'type': type
      }).then(function(rc){
        $scope.map.reservation[type].data = rc;
      })
    };

    loadFactory.grabRes = function(type){
      qService.tokenHttpGet(Reservation.studentAvailableResByPage,{
        'semester':semester.id,
        'accountId': $rootScope.currentUser.id,
        'pageSize':generalService.pageSize(),
        'pageNumber': $scope.map.grabRes.data.curPageNum
      }).then(function(rc){
        $scope.map.grabRes.data = rc;
      });
    };

    $scope.load = function(key, type){
      loadFactory[key](type);
    };
    $scope.cancel = function(record){
      modalService.deleteConfirmInform(function(){
        qService.tokenHttpDelete(Reservation.studentGrabLab, {
          'id':record.id
        }).then(function(rc){
          $timeout(function() {
            modalService.signleConfirmInform('取消预约成功！','','success',
            function(){
              $scope.load('reservation','student');
            });
          }, 200);
        });
      });
    };
    $scope.form = function(reservation){
      var dialog = modalService.mdDialog(
        '/templates/common/reservation-details-dialog.html',
        'ReservationDetailModalCtrl',reservation);
    };

    $scope.grab = function(record){
      qService.tokenHttpPost(Reservation.studentGrabLab, {
        'id':record.id
      }).then(function(rc){
        modalService.signleConfirmInform('预约成功！','','success',
            function(){
              $scope.load('grabRes','');
          });
      });
    };

    $scope.test = function(){
      var dialog = modalService.mdDialog(
        '/templates/student/experiment-details-dialog.html',
        'StExpDetailsModalCtrl',{}
        );
    };

    /**************************************
     * 日历定义
     ***************************************/
    $scope.events = [];
    $scope.tempList = [];
    $scope.eventSources = [$scope.events];
    $scope.timeSpan = {
      'changeCount': 0,
      'start': null,
      'end': null,
      'type': 'agendaWeek'
    };
    $scope.refreshCalendar = function() {
      $scope.events.splice(0, $scope.events.length);
      $scope.load($scope.timeSpan.start.toDate(), $scope.timeSpan.end.toDate());
    };
    $scope.add = function(res) {

    };
    $scope.loadCalendar = function() {
      qService.tokenHttpGet(Reservation.studentResByStatusPage, {
        'semester':semester.id,
        'accountId': $rootScope.currentUser.id,
        'pageSize':10000,
        'pageNumber': 1,
        'type': 'clazz'
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
            'data': res
          };
          rcList.push(map);
        };
        $scope.tempList.splice(0, $scope.tempList.length);
        $scope.tempList = rcList;
        $scope.loadStReservation();
      });
    };

    $scope.loadStReservation = function(){
      qService.tokenHttpGet(Reservation.studentResByStatusPage, {
        'semester':semester.id,
        'accountId': $rootScope.currentUser.id,
        'pageSize':10000,
        'pageNumber': 1,
        'type': 'student'
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
            'type': type
          };
          rcList.push(map);
        };
        $scope.eventSources.splice(0, $scope.eventSources.length);
        rcList = _.union($scope.tempList, rcList);
        $scope.addRemoveEventSource($scope.eventSources, rcList);
        $scope.renderCalender('myCalendar');
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
        selectable: true,
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
          $scope.loadCalendar();
        }
      }
    };
    $scope.toCalenderView = function() {
      $timeout(function() {
        $scope.renderCalender('myCalendar');
      }, 1000);
    };
    $scope.toCalenderView();

  });
