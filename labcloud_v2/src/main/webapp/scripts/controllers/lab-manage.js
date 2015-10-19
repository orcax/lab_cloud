'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:LabManageCtrl
 * @description
 * # LabManageCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('LabManageCtrl', function($scope, modalService,
    qService, Lab, Exp, Course, labs, exps, courses, sessionService) {
    sessionService.storageChecking();
    $scope.list = {
      labs: labs,
      exps: exps,
      courses: courses
    };

    var dialogFactory = {};
    //统一处理表单
    function formHanlder(dialog, factory, f) {
      dialog.result.then(function(data) {
        if (data.id) {
          qService.tokenHttpPut(factory.fid, {
            id: data.id
          }, data).then(function(rc) {
            modalService.signleConfirmInform('更新成功', '', 'success', function() {
              f();
            });
          });
        } else {
          qService.tokenHttpPost(factory.create, {}, data).then(function(rc) {
            modalService.signleConfirmInform('添加成功', '', 'success', function() {
              f();
            });
          });
        }
      });
    };

    dialogFactory.lab = function(data) {
      var dialog;
      if (data) {
        dialog = modalService.mdDialog('/templates/manage/lab-form-dialog.html',
          'LabFormCtrl', data);
      } else {
        dialog = modalService.mdDialog('/templates/manage/lab-form-dialog.html',
          'LabFormCtrl', Lab.template(),modalService);
      }
      formHanlder(dialog, Lab, $scope.refreshLabs);
    };

    dialogFactory.exp = function(data) {
      var dialog;
      if (data) {
        qService.tokenHttpGet(Exp.labs, {
          "id": data.id
        }).then(function(expLabs) {
          dialog = modalService.mdDialog('/templates/manage/exp-form-dialog.html',
            'ExpModalCtrl', {
              "data": data,
              "labs": $scope.list.labs.data,
              "expLabs": expLabs.data
            });
          formHanlder(dialog, Exp, $scope.refreshExps);
        });

      } else {
        dialog = modalService.mdDialog('/templates/manage/exp-form-dialog.html',
          'EmptyExpModalCtrl', Exp.template(), modalService);
        formHanlder(dialog, Exp, $scope.refreshExps);
      }
      
    };

    dialogFactory.course = function(data) {
      var dialog;
      if (data) {
        qService.tokenHttpGet(Course.courseExps, {
          "id": data.id
        }).then(function(rc) {
          //console.log(rc);
          dialog = modalService.mdDialog('/templates/manage/course-form-dialog.html',
            'CourseModalCtrl', {
              "data": data,
              "exps": $scope.list.exps.data,
              "courseExps": rc.data
            });
          formHanlder(dialog, Course, $scope.refreshCourses);
        });
        
      } else {
        dialog = modalService.mdDialog('/templates/manage/course-form-dialog.html',
          'EmptyCourseModalCtrl', Course.template(), modalService);
        formHanlder(dialog, Course, $scope.refreshCourses);
      }
    };

    $scope.entityForm = function(role, data) {
      dialogFactory[role](data);
    };

    $scope.refreshLabs = function() {
      qService.tokenHttpGet(Lab.all, {}).then(function(rc) {
        $scope.list.labs = rc;
      });
    };
    $scope.refreshExps = function() {
      qService.tokenHttpGet(Exp.all, {}).then(function(rc) {
        $scope.list.exps = rc;
      });
    };
    $scope.refreshCourses = function() {
      qService.tokenHttpGet(Course.all, {}).then(function(rc) {
        $scope.list.courses = rc;
      });
    };

    $scope.deleteItem = function(type, id) {
      modalService.deleteConfirmInform(function() {
        if (type == 'lab') {
          qService.tokenHttpDelete(Lab.fid, {
            id: id
          }).then(function(rc) {
            modalService.signleConfirmInform('删除成功', '', 'success', function() {
              $scope.refreshLabs();
            });
          });
        } else if (type == 'exp') {
          qService.tokenHttpDelete(Exp.fid, {
            id: id
          }).then(function(rc) {
            modalService.signleConfirmInform('删除成功', '', 'success', function() {
              $scope.refreshExps();
            });
          });
        } else if (type == 'course') {
          qService.tokenHttpDelete(Course.fid, {
            id: id
          }).then(function(rc) {
            modalService.signleConfirmInform('删除成功', '', 'success', function() {
              $scope.refreshCourses();
            });
          });
        }
      });
    };


  });