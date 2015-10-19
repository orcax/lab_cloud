'use strict';

/**
 * 实验室的基本资源管理
 * 包括 课程，实验，实验室，学期以及班级课程
 *
 * @ngdoc function
 * @name prjApp.controller:LabManageCtrl
 * @description
 * # LabManageCtrl
 * Controller of the prjApp
 */
angular.module('prjApp')
  .controller('LabManageCtrl', function($scope, dialogs, SystemManageService,
    LoginService, $location, Generalservice, $upload, $anchorScroll) {
    $scope.semester = SystemManageService.getCurrentSemester();
    $scope.tabHref = function(path) {
      $location.path(path);
    };

    /*
      配置数据分页必须的配置字段
     */
    var pageSizeConfig = 10;
    var pagenageItemConfig = {
      totalItemNum: 0,
      currentPage: 1,
      pageMap: {},
      selectedList: []
    };

    
    /*
      用于存储这个页面所有信息的数据结构
     */
    $scope.itemPageResult = {
      lab: pagenageItemConfig,
      experiment: Generalservice.cloneObject(pagenageItemConfig),
      course: Generalservice.cloneObject(pagenageItemConfig),
      clazz: Generalservice.cloneObject(pagenageItemConfig)
    };


    function pagenateList(lst, pageSize) {
      var rcMap = {};
      for (var i = 1; i <= parseInt(lst.length / pageSize + 1); i++) {
        rcMap[i + ''] = lst.slice((i - 1) * pageSize, pageSize * i);
      };
      return rcMap;
    };

    $scope.pageChanged = function(item_type) {
      var currentPage = $scope.itemPageResult[item_type].currentPage + '';
      $scope.itemPageResult[item_type].selectedList = $scope.itemPageResult[item_type].pageMap[currentPage];
      Generalservice.toTop();
    };

    /*
      Lab 
    */
    loadLabs();

    function loadLabs() {
      var promise = SystemManageService.loadLab(1);
      promise.then(
        function(data) {
          data = JSOG.parse(JSOG.stringify(data));
          if (data.data != null) {
            $scope.itemPageResult.lab.pageMap = pagenateList(data.data, pageSizeConfig);
            $scope.itemPageResult.lab.selectedList = $scope.itemPageResult.lab.pageMap[$scope.itemPageResult.lab.currentPage + ''];
            $scope.itemPageResult.lab.totalItemNum = data.data.length;
          }
        },
        function(data) {
          Generalservice.informError('获取实验室列表失败！');
        }
      );
    };

    /*
      该接口目前是禁用的
     */
    $scope.addLab = function() {
      var dialog = dialogs.create('/template/lab-add-lab-dialog.html', 'AddLabCtrl', '', {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(data) {
        var promise = SystemManageService.addLab({
          "token": "fbcaad99740328e263d001d0379851da:2015/02/20",
          data: data
        });
        promise.then(
          function(data) {

            Generalservice.inform('添加实验室成功！');
          },
          function(data) {
            Generalservice.informError('添加实验室发生错误！');
          }
        );
        $scope.currentLabList.push(data);
      });
    };

    $scope.configExperimentForLab = function(lab) {
      var n_lab = {
        "id": lab.id,
        "labNumber": lab.labNumber,
        "labName": lab.labName,
        "description": lab.description,
        "status": lab.status,
        "capacity": lab.capacity
      };
      var dialog = dialogs.create('/template/lab-config-experiment-dialog.html', 'ConfigExperimentForLabCtrl', n_lab, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });
    }

    $scope.editLab = function(lab) {
      var n_lab = {
        "id": lab.id,
        "labNumber": lab.labNumber,
        "labName": lab.labName,
        "description": lab.description,
        "status": lab.status,
        "capacity": lab.capacity
      };

      var dialog = dialogs.create('/template/lab-lab-info-dialog.html', 'EditLabCtrl', n_lab, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(data) {
        var promise = SystemManageService.updateLab(data.id, {
          "token": "",
          "data": data
        });
        promise.then(
          function(data) {
            Generalservice.inform("更新实验室成功！");
            loadLabs();
          },
          function(data) {
            Generalservice.informError('更新实验室失败！');
          }
        );
      });
    };

    $scope.removeLab = function(lab) {
      var dialog = dialogs.create('/template/lab-confirm-dialog.html', 'ConfirmCtrl', {
        text: '你确定要移除这个实验室吗？',
        cancelBtnText: '取消',
        confirmBtnText: '确认'
      }, {
        size: 'sm',
        keyboard: true,
        backdrop: true,
        windowClass: 'model-overlay'
      });
      dialog.result.then(function(data) {
        var promise = SystemManageService.removeLab(lab.id);
        promise.then(
          function(data) {
            Generalservice.inform('删除成功');
            $scope.currentLabList.splice($scope.currentLabList.indexOf(lab), 1);
          },
          function(data) {
            Generalservice.informError('删除失败！');
          }
        );
      }, function() {});
    };


    /*
      Experiment Management
    */
    //$scope.currentExperimentList = SystemManageService.loadExperiment();
    loadExperiments();

    function loadExperiments() {
      var promise = SystemManageService.loadExperiment(1);
      promise.then(
        function(data) {
          data = JSOG.parse(JSOG.stringify(data));
          if (data.data != null) {
            $scope.itemPageResult.experiment.pageMap = pagenateList(data.data, pageSizeConfig);
            //$scope.itemPageResult.experiment.selectedList = $scope.itemPageResult.experiment.pageMap[$scope.itemPageResult.experiment.currentPage+''];
            $scope.pageChanged('experiment');
            $scope.itemPageResult.experiment.totalItemNum = data.data.length;
          }
          //$scope.currentExperimentList = data.data;
        },
        function(data) {
          Generalservice.informError('获取实验列表失败！');
        }
      );
    };

    $scope.addExperiment = function() {
      var dialog = dialogs.create('/template/lab-add-experiment-dialog.html', 'AddCourseCtrl', '', {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(exp) {
        var promise = SystemManageService.addExperiment({
          "token": '',
          "data": exp
        });
        promise.then(
          function(data) {
            loadExperiments();
            Generalservice.inform('添加实验成功');
          },
          function(data) {
            Generalservice.informError('添加实验失败！');
          }
        );
      });
    };

    $scope.configLab = function(exp) {

      var exists_labs = [];
      for (var i = 0; i < exp.labs.length; i++) {
        var entity = exp.labs[i];
        exists_labs.push({
          id: entity.id,
          labNumber: entity.labNumber,
          labName: entity.labName
        });
      };
      var n_exp = {
        "id": exp.id,
        "experimentNumber": exp.experimentNumber,
        "experimentName": exp.experimentName,
        "maximumStudent": exp.maximumStudent,
        "minimumStudent": exp.minimumStudent,
        "labs": exists_labs
      };
      var dialog = dialogs.create('/template/experiment-config-lab-dialog.html', 'ConfigLabForExperimentCtrl', n_exp, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });
      dialog.result.then(function(data) {
        loadExperiments();
      }, function(e) {
        //console.log('error when save configuration modal.')
      })
    };

    $scope.editExperimentDetails = function(exp) {

      var exists_labs = [];
      for (var i = 0; i < exp.labs.length; i++) {
        var entity = exp.labs[i];
        exists_labs.push({
          id: entity.id,
          labNumber: entity.labNumber,
          labName: entity.labName
        });
      };
      var n_exp = {
        "id": exp.id,
        "experimentNumber": exp.experimentNumber,
        "experimentName": exp.experimentName,
        "maximumStudent": exp.maximumStudent,
        "minimumStudent": exp.minimumStudent,
        "labs": exists_labs
      };
      var dialog = dialogs.create('/template/lab-experiment-info-dialog.html', 'EditLabCtrl', n_exp, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(data) {
        var promise = SystemManageService.updateExperiment(data.id, {
          "token": "",
          "data": data
        });
        promise.then(
          function(data) {
            Generalservice.inform("更新实验成功");
            loadExperiments();
          },
          function(data) {
            Generalservice.informError('更新实验失败！');
          }
        );
      });
    };

    $scope.removeExperiment = function(exp) {
      var dialog = dialogs.create('/template/lab-confirm-dialog.html', 'ConfirmCtrl', {
        text: '你确定要移除这个实验室吗？',
        cancelBtnText: '取消',
        confirmBtnText: '确认'
      }, {
        size: 'sm',
        keyboard: true,
        backdrop: true,
        windowClass: 'model-overlay'
      });
      dialog.result.then(function(data) {
        var promise = SystemManageService.removeExperiment(exp.id);
        promise.then(
          function(data) {
            Generalservice.inform("移除实验成功");
            loadExperiments();
          },
          function(data) {
            Generalservice.informError('移除实验失败！');
          }
        );

      }, function() {});
    };


    /*
      Course Management 
    */
    //$scope.currentCourseList = SystemManageService.loadCourse();
    loadCourses();

    function loadCourses() {
      var promise = SystemManageService.loadCourse(1);
      promise.then(
        function(data) {
          data = JSOG.parse(JSOG.stringify(data));
          //$scope.currentCourseList = data.data;
          if (data.data != null) {
            $scope.itemPageResult.course.pageMap = pagenateList(data.data, pageSizeConfig);
            //$scope.itemPageResult.experiment.selectedList = $scope.itemPageResult.experiment.pageMap[$scope.itemPageResult.experiment.currentPage+''];
            $scope.pageChanged('course');
            $scope.itemPageResult.course.totalItemNum = data.data.length;
          }
        },
        function(data) {
          //Generalservice.inform('获取课程列表失败！');
        }
      );
    };

    $scope.addCourse = function() {
      var dialog = dialogs.create('/template/lab-add-course-dialog.html', 'AddCourseCtrl', '', {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(course) {
        var promise = SystemManageService.addCourse({
          "token": "",
          "data": course
        });
        promise.then(
          function(data) {
            Generalservice.inform('添加课程成功！');
            loadCourses();
          },
          function(data) {
            Generalservice.informError('添加课程失败！');
          }
        );

      });
    };

    $scope.editCourseDetails = function(course) {
      var exps = [];
      for (var i = 0; i < course.courseExperiment.length; i++) {
        var entity = course.courseExperiment[i].experiment;
        exps.push({
          id: entity.id,
          experimentName: entity.experimentName,
          experimentNumber: entity.experimentNumber
        });
      };
      var n_course = {
        "id": course.id,
        "courseNumber": course.courseNumber,
        "courseName": course.courseName,
        "department": course.department,
        "startYear": course.startYear,
        "courseExperiment": exps
      };

      var dialog = dialogs.create('/template/lab-course-info-dialog.html', 'EditLabCtrl', n_course, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(data) {
        var promise = SystemManageService.updateCourse(data.id, {
          "token": "",
          "data": data
        });
        promise.then(
          function(data) {
            Generalservice.inform('更新课程成功！');
            loadCourses();
          },
          function(data) {
            Generalservice.informError('更新课程失败！');
          }
        );
      });
    };

    $scope.configExperiment = function(course) {

      var exps = [];
      for (var i = 0; i < course.courseExperiment.length; i++) {
        var entity = course.courseExperiment[i].experiment;
        exps.push({
          id: entity.id,
          experimentName: entity.experimentName,
          experimentNumber: entity.experimentNumber
        });
      };

      var n_course = {
        "id": course.id,
        "courseNumber": course.courseNumber,
        "courseName": course.courseName,
        "department": course.department,
        "startYear": course.startYear,
        "courseExperiment": exps
      };

      var dialog = dialogs.create('/template/course-config-exp-dialog.html', 'ConfigExperimentForCourseCtrl', n_course, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(data) {
        loadCourses();
      }, function(e) {
        //console.log('Error happen when config exp for course!');
      });
    };

    $scope.removeCourse = function(course) {
      var dialog = dialogs.create('/template/lab-confirm-dialog.html', 'ConfirmCtrl', {
        text: '你确定要移除这个实验室吗？',
        cancelBtnText: '取消',
        confirmBtnText: '确认'
      }, {
        size: 'sm',
        keyboard: true,
        backdrop: true,
        windowClass: 'model-overlay'
      });
      dialog.result.then(function(data) {
        var promise = SystemManageService.removeCourse(course.id);
        promise.then(
          function(data) {
            Generalservice.inform('移除课程成功！');
            loadCourses();
          },
          function(data) {
            Generalservice.inform('移除课程失败！');
          }
        );

      }, function() {});
    };


    //$scope.cureentClassList = [];
    loanClasses();

    function loanClasses() {
      var promise = SystemManageService.loanAllClasses();
      promise.then(function(data) {
        data = JSOG.parse(JSOG.stringify(data));
        if (data.data != null) {
          $scope.itemPageResult.clazz.pageMap = pagenateList(data.data, pageSizeConfig);
          //$scope.itemPageResult.experiment.selectedList = $scope.itemPageResult.experiment.pageMap[$scope.itemPageResult.experiment.currentPage+''];
          $scope.pageChanged('clazz');
          $scope.itemPageResult.clazz.totalItemNum = data.data.length;
        }

      });
    };

    $scope.addClass = function(semester) {
      var n_semester = {
        "name": $scope.semester.semesterName,
        "id": $scope.semester.id
      };
      var dialog = dialogs.create('/template/lab-add-class-dialog.html', 'AddClassCtrl', n_semester, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(data) {
          var url = "/Class/add/" + data.semester.id + '/' +
            data.course.id + '/' + data.teacher.id;
          var promise = SystemManageService.generalPost(url, {
            "data": {
              "classHour": data.classHour,
              "classRoom": data.classRoom,
              "classNumber": data.classNumber
            }
          });
          promise.then(function(data) {
            Generalservice.inform('添加班级成功！');
            loanClasses();
          }, function() {
            Generalservice.informError('添加班级失败！')
          });
        },
        function() {

        });
    };

    $scope.editClassDetails = function(cls) {
      var n_class = {
        "id": cls.id,
        "classHour": cls.classHour,
        "classRoom": cls.classRoom,
        "classNumber": cls.classNumber,
        "semester": cls.semester.semesterName,
        "teacher": cls.teacher.name,
        "course": cls.course.courseName
      };

      var dialog = dialogs.create('/template/lab-class-info-dialog.html', 'EditClassCtrl', n_class, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });
      dialog.result.then(function(data) {
          delete data.teacher;
          delete data.semester;
          delete data.course;

          var promise = SystemManageService.generalPost('/Class/update/' + data.id, {
            "data": data
          });
          promise.then(function(data) {
            Generalservice.inform('更新班级成功!');
            loanClasses();
          }, function(e) {
            Generalservice.informError('更新班级失败!');
          });
        },
        function() {
          loanClasses();
        });
    };

    $scope.removeClass = function(c_id) {
      var dialog = dialogs.create('/template/lab-confirm-dialog.html', 'ConfirmCtrl', {
        text: '你确定要移除这个实验室吗？',
        cancelBtnText: '取消',
        confirmBtnText: '确认'
      }, {
        size: 'sm',
        keyboard: true,
        backdrop: true,
        windowClass: 'model-overlay'
      });
      dialog.result.then(function(data) {
        var promise = SystemManageService.generalPost('/Class/delete/' + c_id, {});
        promise.then(
          function(data) {
            Generalservice.inform('移除课程成功！');
            loanClasses();
          },
          function(data) {
            Generalservice.informError('移除课程失败！');
          }
        );

      }, function() {});
    };

    $scope.getClassStudent = function(clazz_id) {
      var dialog = dialogs.create('/template/lab-class-student-dialog.html', 'ClassStudentCtrl', {
        clazz_id: clazz_id
      }, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });


    };

    $scope.setSemester = function() {
      var dialog = dialogs.create('/template/lab-semester-dialog.html', 'SetSemesterCtrl', $scope.semester, {
        size: 'md',
        keyboard: true,
        backdrop: 'static',
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(data) {
          //Generalservice.inform('学期设置成功！');
        },
        function() {
          //Generalservice.informError('学期设置失败！');
        });
    };

  }).controller('AddLabCtrl', function($scope, $modalInstance, data, DictService) {
    $scope.data = {};
    $scope.labStatus = DictService.getLabStatusDict();
    $scope.data['status'] = $scope.labStatus[0].id;
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };
    $scope.format = 'yyyy-MM-dd';
    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.opened = true;
    };
    $scope.save = function() {
      $modalInstance.close($scope.data);
    };

    $scope.hitEnter = function(evt) {
      if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
        $scope.save();
    };
  }).controller('EditLabCtrl', function($scope, $modalInstance, data, DictService) {
    $scope.data = data;
    $scope.labStatus = DictService.getLabStatusDict();
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    }; // end cancel

    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };
    $scope.format = 'yyyy-MM-dd';
    $scope.save = function() {
      $modalInstance.close($scope.data);
    }; // end save

    $scope.hitEnter = function(evt) {
      if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
        $scope.save();
    }; // end hitEnter
  }).controller('AddCourseCtrl', function($scope, $modalInstance, data, DictService) {
    $scope.data = {};
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };
    $scope.format = 'yyyy-MM-dd';
    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.opened = true;
    };
    $scope.save = function() {
      $modalInstance.close($scope.data);
    };

    $scope.hitEnter = function(evt) {
      if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
        $scope.save();
    };
  }).controller('SetSemesterCtrl', function($scope, $modalInstance, data, SystemManageService) {
    $scope.data = data;
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1
    };
    $scope.format = 'yyyy-MM-dd';
    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened = true;
    };
    $scope.save = function() {
      $modalInstance.close($scope.data);
      var startDate = new Date(Date.parse($scope.data.startDate));
      var endDate = new Date(Date.parse($scope.data.endDate));
      Date.prototype.format = function(format) {
        var o = {
          "M+": this.getMonth() + 1,
          //month
          "d+": this.getDate(),
          //day
          "h+": this.getHours(),
          //hour
          "m+": this.getMinutes(),
          //minute
          "s+": this.getSeconds(),
          //second
          "q+": Math.floor((this.getMonth() + 3) / 3),
          //quarter
          "S": this.getMilliseconds() //millisecond
        }
        if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
          if (new RegExp("(" + k + ")").test(format)) format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        return format;
      }
      var formattedStartDate = startDate.format($scope.format);
      var formattedEndDate = endDate.format($scope.format);
      $scope.data.startDate = formattedStartDate;
      $scope.data.endDate = formattedEndDate;
      $scope.data.status = "CURRENT";
      $scope.data.semesterName = $scope.data.courseName;
      setSemesterData();

      function setSemesterData() {
        var promise = SystemManageService.setCurretSemesterData($scope.data);
        promise.then(
          function(result) {
            if (result.errorCode == 'No_Error' && result.callStatus == "SUCCEED") {
              Generalservice.inform(result.callStatus);
            } else {
              Generalservice.inform(result.errorCode);
            }
          },
          function(result) {

          }
        );
      };
    };

    $scope.hitEnter = function(evt) {
      if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
        $scope.save();
    };
  }).controller('AddClassCtrl', function($scope, $modalInstance, data, DictService, SystemManageService) {
    $scope.data = {};
    $scope.data.semester = data;
    $scope.semester = data;
    //load teacher list
    var t_promise = SystemManageService.loanAllTeachers();
    t_promise.then(function(data) {
      var r_data = JSOG.parse(JSOG.stringify(data.data));
      if (r_data) {
        $scope.teachers = dict_parse(r_data, ['id', 'name'])
        $scope.data.teacher = $scope.teachers[0];
      } else {
        //load error process
      }
    });
    var c_promise = SystemManageService.loanAllCourses();
    c_promise.then(function(data) {
      var r_data = JSOG.parse(JSOG.stringify(data.data));
      $scope.courses = dict_parse(r_data, ['id', 'courseName']);
      $scope.data.course = $scope.courses[0];
    });

    function dict_parse(data, keys) {
      var r_list = [];
      for (var i = 0; i < data.length; i++) {
        var dict = {};
        for (var j = 0; j < keys.length; j++) {
          dict[keys[j]] = data[i][keys[j]];
        };
        if (data[i].role != 'LAB') { //实验教师不能开课
          r_list.push(dict);
        }
      };
      return r_list;
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened = true;
    };
    $scope.save = function() {
      $modalInstance.close($scope.data);
    };

    $scope.hitEnter = function(evt) {
      if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
        $scope.save();
    };
  }).controller('EditClassCtrl', function($scope, $modalInstance, data,
    DictService, SystemManageService, $upload, $sessionStorage, Generalservice) {
    $scope.data = data;

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened = true;
    };
    $scope.save = function() {
      $modalInstance.close($scope.data);
    };

    $scope.hitEnter = function(evt) {
      if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
        $scope.save();
    };
    $scope.$watch('files', function() {
      $scope.upload($scope.files);
    });

    $scope.upload = function(files) {
      if (files && files.length) {
        for (var i = 0; i < files.length; i++) {
          var file = files[i];
          $upload.upload({
            url: '/Class/' + $scope.data.id + '/upload/list',
            data: {
              'token': $scope.token,
              'accountCharacter': $sessionStorage.character
            },
            file: file,
            fileFormDataName: 'data'
          }).progress(function(evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            ////console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
          }).success(function(data, status, headers, config) {
            if (data.callStatus == 'SUCCEED') {
              $scope.cancel();
              Generalservice.inform('学生名单上传成功!');
            } else {
              Generalservice.informError('学生名单导入失败！');
            }
          }).error(function() {
            Generalservice.informError('学生名单导入失败！');
          });
        }
      }
    };
  }).controller('ConfigExperimentForCourseCtrl', function($scope, $modalInstance, data, SystemManageService) {
    $scope.data = data;
    $scope.currentExperimentList = SystemManageService.loadExperiment();
    loadExperiments();
    $scope.cancel = function() {
      $modalInstance.close($scope.data);
    };

    function loadExperiments() {
      var promise = SystemManageService.loadExperiment(1);
      promise.then(
        function(data) {
          $scope.currentExperimentList = JSOG.parse(JSOG.stringify(data.data));
        },
        function(data) {
          //Generalservice.inform('获取实验列表失败！');
        }
      );
    };

    $scope.removeExperimentOfCourse = function(data, exp) {
      var promise = SystemManageService.removeExperimentOfCourse($scope.data.id, exp.id);
      promise.then(
        function(data) {
          if (data.errorCode == 'No_Error' && data.callStatus == "SUCCEED") {
            $scope.data.courseExperiment.splice($scope.data.courseExperiment.indexOf(exp), 1);
          } else {
            Generalservice.inform(data.errorCode);
          }
        },
        function(data) {}
      );
    };

    $scope.addExperimentForCourse = function(data) {
      var index = $('#experiments-select').get(0).selectedIndex;
      var selectedExpId = $('#experiments-select').get(0).options[index].getAttribute('expid');
      var selectedExpNO = $('#experiments-select').get(0).options[index].getAttribute('expno');
      var selectedExpNa = $('#experiments-select').get(0).options[index].getAttribute('expna');

      var selectedExperiment = {
        id: selectedExpId,
        experimentNumber: selectedExpNO,
        experimentName: selectedExpNa
      };

      var promise = SystemManageService.addExperimentForCourse(data.id, selectedExpId);
      promise.then(
        function(data) {
          if (data.errorCode == 'No_Error' && data.callStatus == "SUCCEED") {
            $scope.data.courseExperiment.push(selectedExperiment);
          } else {
            Generalservice.inform(data.errorCode);
          }
        },
        function(data) {}
      );
    };

  }).controller('ConfigLabForExperimentCtrl', function($scope, $modalInstance, data, SystemManageService) {
    $scope.data = data;

    $scope.currentLabList = [];
    loadLabs();
    $scope.cancel = function() {
      //$modalInstance.dismiss('canceled');
      $modalInstance.close($scope.data);
    };

    function loadLabs() {
      var promise = SystemManageService.loadLab(1);
      promise.then(
        function(data) {
          //去掉重复的实验室
          $scope.currentLabList = JSOG.parse(JSOG.stringify(data.data));

        },
        function(data) {
          Generalservice.inform('获取实验室列表失败！');
        }
      );
    };

    $scope.removeLabForExperiment = function(expData, selectedlab) {
      var promise = SystemManageService.removeLabForExperiment(expData.id, selectedlab.id);
      promise.then(
        function(data) {
          if (data.errorCode == 'No_Error' && data.callStatus == "SUCCEED") {
            $scope.data.labs.splice($scope.data.labs.indexOf(selectedlab), 1);
          } else {
            Generalservice.inform(data.errorCode);
          }
        },
        function(data) {}
      );
    };

    $scope.addLabForExperiment = function(expData) {
      var expId = expData.id;

      var index = $('#labs-select').get(0).selectedIndex;
      var selectedLabId = $('#labs-select').get(0).options[index].getAttribute('labid');
      var selectedLabNO = $('#labs-select').get(0).options[index].getAttribute('labno');
      var selectedLabNa = $('#labs-select').get(0).options[index].getAttribute('labna');

      var selectedLab = {
        id: selectedLabId,
        labNumber: selectedLabNO,
        labName: selectedLabNa
      };

      var promise = SystemManageService.addLabForExperiment(expId, selectedLabId);
      promise.then(
        function(data) {
          if (data.errorCode == 'No_Error' && data.callStatus == "SUCCEED") {
            $scope.data.labs.push(selectedLab);
          } else {
            Generalservice.inform(data.errorCode);
          }
        },
        function(data) {}
      );
    }
  }).controller('ConfigExperimentForLabCtrl', function($scope, $modalInstance, data, SystemManageService) {
    $scope.data = data;
    //console.log(data);
    $scope.currentExperimentList = SystemManageService.loadExperiment();
    loadExperiments();
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };

    function loadExperiments() {
      var promise = SystemManageService.loadExperiment(1);
      promise.then(
        function(data) {
          $scope.currentExperimentList = data.data;
        },
        function(data) {
          //Generalservice.inform('获取实验列表失败！');
        }
      );
    };

    $scope.removeExperimentOfLab = function(labData, exp) {
      var promise = SystemManageService.removeExperimentOfLab(data.id, exp.id);
      promise.then(
        function(data) {
          if (data.errorCode == 'No_Error' && data.callStatus == "SUCCEED") {
            $scope.data.experiments.splice($scope.data.experiments.indexOf(exp), 1);
          } else {
            Generalservice.inform(data.errorCode);
          }
        },
        function(data) {}
      );
    }

    $scope.addExperimentForLab = function(labData) {
      //console.log(labData);
      var labId = labData.id;

      var index = $('#lab-experiments-select').get(0).selectedIndex;
      var selectedExpId = $('#lab-experiments-select').get(0).options[index].getAttribute('expid');
      var selectedExpNO = $('#lab-experiments-select').get(0).options[index].getAttribute('expno');
      var selectedExpNa = $('#lab-experiments-select').get(0).options[index].getAttribute('expna');

      var selectedExperiment = {
        id: selectedExpId,
        experimentNumber: selectedExpNO,
        experimentName: selectedExpNa
      };

      var promise = SystemManageService.addExperimentForLab(labId, selectedExpId);
      promise.then(
        function(data) {
          if (data.errorCode == 'No_Error' && data.callStatus == "SUCCEED") {
            $scope.data.experiments.push(selectedExperiment);
          } else {
            Generalservice.inform(data.errorCode);
          }
        },
        function(data) {}
      );
    };
  }).controller('ClassStudentCtrl', function($scope, $modalInstance, data,
    SystemManageService, Generalservice) {
    $scope.data = data;
    $scope.config = {
      currentPage: 1,
      allCount: 0
    };
    $scope.studentList = [];

    $scope.loadList = function() {
      var promise = SystemManageService.getClassStudentList(data.clazz_id, $scope.config.currentPage);
      promise.then(function(data) {
        var data = JSOG.parse(JSOG.stringify(data));
        $scope.studentList = data.data;
        $scope.config.currentPage = data.currPageNum;
        $scope.config.allCount = data.totalItemNum;

      }, function(e) {
        Generalservice.informError('获取学生列表失败！');
      });
    };
    $scope.loadList();


    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      $modalInstance.close($scope.data);
    };
  });