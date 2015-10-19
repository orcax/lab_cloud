'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:SemesterCtrl
 * @description
 * # SemesterCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('SemesterManageCtrl', function($scope, modalService, Semester, Clazz,
    qService, generalService, $modal, sessionService) {
    sessionService.storageChecking();
    $scope.semester = sessionService.getCurrSemeter();
    var pageSize = generalService.pageSize();
    $scope.dataMap = {
      'clazz': {
        currList: [],
        totalItemNum: 0,
        currPage: 1,
        totalPageNum: 0,
        service: Clazz
      },
      'semester': {
        currList: [],
        totalItemNum: 0,
        currPage: 1,
        totalPageNum: 0,
        service: Semester
      }
    };

    function loadAll(obj) {
      var queryMap = {
        "pageSize": pageSize, //config pageSize
        "pageNumber": obj.currPage
      };
      qService.tokenHttpGet(obj.service.page, queryMap).then(function(rc) {
        obj.currList = rc.data;
        obj.totalItemNum = rc.totalItemNum;
        obj.currPage = rc.curPageNum;
        obj.totalPageNum = rc.totalPageNum;
      });
    };

    $scope.loadObjects = function(key) {
      var obj = $scope.dataMap[key];
      if (obj.currList.length == 0) {
        loadAll(obj);
      }
    };
    $scope.pageChanged = function(key){
      var obj = $scope.dataMap[key];
      loadAll(obj);
    };

    $scope.loadObjects('clazz');

    $scope.form = function(clazz) {
      if(!clazz){
        clazz = {};
      }
      var dialog = $modal.open({
        templateUrl: '/templates/manage/class-form-dialog.html',
        controller: 'ClassModalCtrl',
        resolve: {
          baseData: function(){
            return clazz;
          },
          teachers: function(qService, Account) {
            return qService.tokenHttpGet(Account.all, {
              'userType': 'ALL_TEACHER'
            });
          },
          semester: function(sessionService) {
            return sessionService.getCurrSemeter();
          },
          courses: function(qService, Course) {
            return qService.tokenHttpGet(Course.all);
          }
        }
      });
      dialog.result.then(function(rc) {
        var map = {
          "course": rc.course.id,
          "teacher": rc.teacher.id,
          "semester": rc.semester.id
        };
        if(rc.id){
          qService.tokenHttpPut(Clazz.clazz, {"id":rc.id}, rc).then(function(result) {
            if (result.data) {
              modalService.signleConfirmInform('更新成功', '', 'success', function() {
                //reload
                loadAll($scope.dataMap['clazz'], modalService);
              });
            }
          });  
        }else{
          qService.tokenHttpPost(Clazz.create, map, rc).then(function(result) {
            if (result.data) {
              modalService.signleConfirmInform('添加成功', '', 'success', function() {
                //reload
                loadAll($scope.dataMap['clazz']);
              });
            }
          });  
        }
      });
    };

    $scope.deleteClass = function(clazz) {
      modalService.deleteConfirmInform(function() {
        qService.tokenHttpDelete(Clazz.clazz, {
          id: clazz.id
        }).then(function(result) {
          if(result.data){
            modalService.signleConfirmInform('删除成功', '', 'success', function() {});
            loadAll($scope.dataMap['clazz']);
          }
        });
      });
    };

    $scope.deleteSemester = function(semester) {
      if(semester.status == 'CURRENT'){
        modalService.signleConfirmInform('不可以删除当前学期','','warning',function(){});
      }else{
        modalService.deleteConfirmInform(function() {
          qService.tokenHttpDelete(Semester.semester, {
            id: semester.id
          }).then(function(result) {
            if(result.data){
              modalService.signleConfirmInform('删除成功', '', 'success', function() {});
              loadAll($scope.dataMap['semester']);
            }
          });
        });
      }
    };


    $scope.addSemeter = function(){
      var dialog = modalService.mdDialog('/templates/manage/semester-form-dialog.html',
        'SemesterModalCtrl', {}, modalService);
      dialog.result.then(function(rc){
        qService.tokenHttpPost(Semester.create,{},rc).then(function(result){
          if (result.data) {
            modalService.signleConfirmInform('添加成功', '', 'success', function() {
              //reload
              loadAll($scope.dataMap['semester']);
            });
          }
        });
      });
    };

  });