'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:CourseManageCtrl
 * @description
 * # CourseManageCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('CourseManageCtrl', function($scope, $modal, $compile, generalService,
    uiCalendarConfig, qService, Account, Reservation, $timeout, sessionService, modalService) {
    sessionService.storageChecking();
    var currSemester = sessionService.getCurrSemeter();
    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    $scope.dataMap = {
      'class': {
        'templateUrl': ''
      }
    };

    var dialogFactory = {};
    dialogFactory.class = function() {
      var dialog = $modal.open({
        templateUrl: '/templates/common/admin-reservation-dialog.html',
        controller: 'ClazzReservationModalCtrl',
        resolve: {
          data: function() {
            return {};
          },
          clazzs: function(qService, Clazz) {
            return qService.tokenHttpGet(Clazz.all, {});
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
    dialogFactory.close = function() {
      $scope.renderCalender('myCalendar');
    };
    dialogFactory.student = function() {

      var dialog = $modal.open({
        templateUrl: '/templates/common/admin-st-reservation-dialog.html',
        controller: 'StudentReservationModalCtrl',
        resolve: {
          data: function() {
            return {};
          },
          exps: function(qService, Exp) {
            return qService.tokenHttpGet(Exp.all, {});
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
        var map = {
          "semester": rc.semester.id,
          "experiment": rc.exp.id,
          "lab": rc.lab.id,
          "slot": rc.slot.id
        };
        var data = {
          "number": "10001",
          'applyDate': rc.applyDate,
          'maxCount': rc.maxCount
        };
        qService.tokenHttpPost(Reservation.studentReservation, map, data).then(function(result) {
          modalService.signleConfirmInform('添加成功', '', 'success', function() {
            $scope.refreshCalendar();
          });
        });
      });
    };
    $scope.addReservation = function(type) {
      dialogFactory[type]();
    };

    /* event source that contains custom events on the scope */
    $scope.events = [];
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

    $scope.load = function(start, end) {
      qService.tokenHttpGet(Reservation.all, {
        "semester": currSemester.id,
        "startDate": start,
        "endDate": end
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
        $scope.eventSources.splice(0, $scope.eventSources.length);
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
      console.log(data);
      if(data.status == 'PENDING'){
        //开启审核列表
        var dialog = $modal.open({
          templateUrl: '/templates/manage/apply-verify-form-dialog.html',
          controller: 'VerifyModalCtrl',
          resolve: {
            baseData: function(){
              return data.data;
            },
            teachers: function(qService, Account) {
              return qService.tokenHttpGet(Account.all, {
                'userType': 'ALL_TEACHER'
              });
            }
          }
        });
        dialog.result.then(function(rc){
          qService.tokenHttpPost(Reservation.verify, {
            "id": data.id,
            "status": rc.status,
            "teacherIds": rc.teacherIds
          }).then(function(result) {
            modalService.signleConfirmInform('审核成功!','','success',function(){
              $scope.refreshCalendar(); 
            });
          });
        });

      }else if(data.status == 'APPROVED' && data.type == 'clazz'){
        var dialog = modalService.mdDialog('/templates/common/reservation-details-dialog.html',
          'ReservationDetailModalCtrl',data.data);
        dialog.result.then(function(rc){
          if(rc=='refresh'){
            $scope.refreshCalendar(); 
          }
        });
      }else{
        //获取学生列表
        qService.tokenHttpGet(Reservation.reservationedStudents, {
          'id':data.id
        }).then(function(rc){
          var count = rc.data.length;
          if(count == 0 || count ==undefined){
            modalService.signleConfirmInform('还没有任何学生选择这个预约！','','info',function(){});
          }else{
            var dialog = $modal.open({
              templateUrl: '/templates/manage/student-list-dialog.html',
              controller: 'EmptyModalCtrl',
              resolve: {
                data: function() {
                  return rc.data;
                },
                rId: function(){
                  return data.id;
                }
              }
            });
            dialog.result.then(function(rc){
              if(rc=='refresh'){
                $scope.refreshCalendar(); 
              }
            });
          }
        });
      }
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
          $scope.timeSpan.start = start;
          $scope.timeSpan.end = end;
          $scope.timeSpan.type = view.type;
          $scope.load(start.toDate(), end.toDate());
        }
      }
    };
    $timeout(function() {
      $scope.renderCalender('myCalendar');
    }, 1000);
  });