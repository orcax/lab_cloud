'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:ManageModalCtrl
 * @description
 * # ManageModalCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('ClassModalCtrl', function($scope, $modalInstance, baseData,
    teachers, semester, courses, modalService) {
    $scope.data = baseData;
    $scope.teachers = teachers.data;
    $scope.courses = courses.data;
    $scope.data["semester"] = semester;
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      if (!$scope.data.number || $scope.data.number.length == 0) {
        modalService.signleConfirmInform('班级编号不能为空', '请输入编号', 'warning', function() {});
      } else if (!$scope.data.teacher) {
        modalService.signleConfirmInform('教师不能为空', '请选择教师', 'warning', function() {});
      } else if (!$scope.data.course) {
        modalService.signleConfirmInform('课程不能为空', '请选择教师', 'warning', function() {});
      } else {
        $modalInstance.close($scope.data);
      }
    };
  })
  /*************
   * 实验配置弹窗
   ***/
  .controller('ExpModalCtrl', function($scope, $modalInstance, data, qService, Exp,
    modalService) {

    $scope.data = data.data;
    $scope.labs = data.labs; //所有的labs
    $scope.selected = {
      'lab': $scope.labs[0],
      'labs': []
    };
    $scope.expLabs = []; //已选中的labs

    for (var i = 0; i < data.expLabs.length; i++) {
      var obj = data.expLabs[i];
      for (var j = 0; j < $scope.labs.length; j++) {
        if (obj.id == $scope.labs[j].id) {
          $scope.expLabs.push($scope.labs[j]);
        }
      };
    };
    reloadLabs();

    function reloadLabs() {
      $scope.selected.labs = [];
      for (var i = 0; i < $scope.labs.length; i++) {
        var obj = $scope.labs[i];
        if ($scope.expLabs.indexOf(obj) < 0) {
          $scope.selected.labs.push(obj);
        }
      };
      $scope.selected.lab = $scope.selected.labs[0];
    };

    $scope.remove = function(lab){
      if(lab!= undefined){
        qService.tokenHttpDelete(Exp.lab, {
          "id": $scope.data.id,
          "labId": lab.id
        }).then(function(rc){
          $scope.expLabs = _.without($scope.expLabs, lab);
          reloadLabs();
        });
      }
    };

    $scope.addLab = function() {
      if ($scope.selected.lab != undefined) {
        qService.tokenHttpPost(Exp.lab, {
          "id": $scope.data.id,
          "labId": $scope.selected.lab.id
        }, {}).then(function(rc) {
          $scope.expLabs.push($scope.selected.lab); // add  
          reloadLabs();
        });
      }
    };
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      if (!$scope.data.number || $scope.data.number.length == 0) {
        modalService.signleConfirmInform('实验编号不能为空', '请输入编号', 'warning', function() {});
      } else if (!$scope.data.name || $scope.data.name.length == 0) {
        modalService.signleConfirmInform('实验名称不能为空', '请输入编号', 'warning', function() {});
      } else {
        $modalInstance.close($scope.data);
      }
      //$modalInstance.close($scope.data);
    };
  })
  /*************
   * 课程配置弹窗
   ***/
  .controller('CourseModalCtrl', function($scope, $modalInstance, data,
    qService, Course) {
    $scope.data = data.data;
    $scope.exps = data.exps; //所有的
    $scope.selected = {
      'exp': $scope.exps[0],
      'exps': []
    };
    $scope.courseExps = []; //已选中的
    for (var i = 0; i < data.courseExps.length; i++) {
      var obj = data.courseExps[i];
      for (var j = 0; j < $scope.exps.length; j++) {
        if (obj.id == $scope.exps[j].id) {
          $scope.courseExps.push($scope.exps[j]);
        }
      };
    };
    reloadExps();

    function reloadExps() {
      $scope.selected.exps = [];
      for (var i = 0; i < $scope.exps.length; i++) {
        var obj = $scope.exps[i];
        if ($scope.courseExps.indexOf(obj) < 0) {
          $scope.selected.exps.push(obj);
        }
      };
      $scope.selected.exp = $scope.selected.exps[0];
    };

    $scope.remove = function(exp){
      if(exp!= undefined){
        qService.tokenHttpDelete(Course.exp, {
          "id": $scope.data.id,
          "expId": exp.id
        }).then(function(rc){
          $scope.courseExps = _.without($scope.courseExps, exp);
          reloadExps();
        });
      }
    };

    $scope.add = function() {
      if ($scope.selected.exp != undefined) {
        qService.tokenHttpPost(Course.exp, {
          'id': $scope.data.id,
          'expId': $scope.selected.exp.id
        }, {}).then(function(rc) {
          $scope.courseExps.push($scope.selected.exp); // add
          reloadExps();
        });

      }
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      $modalInstance.close($scope.data);
    };
  })
  /***
    提交教师预约的弹窗
  **/
  .controller('ClazzReservationModalCtrl', function($scope, $modalInstance, data,
    clazzs, semester, slots, qService, Course, Exp, Reservation, modalService) {
    $scope.slots = slots.data,
    $scope.clazzs = clazzs.data;
    $scope.data = data;
    $scope.data['semester'] = semester;
    $scope.data['slot'] = slots.data[0];
    $scope.data['applyDate'] = new Date();
    $scope.exps = [];
    $scope.labs = [];
    $scope.now = new Date();
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

    $scope.clazzChanged = function() {
      qService.tokenHttpGet(Course.courseExps, {
        id: $scope.data.clazz.course.id
      }).then(function(rc) {
        $scope.exps = rc.data;
      });
    };

    $scope.expChanged = function() {
      qService.tokenHttpGet(Exp.labs, {
        id: $scope.data.exp.id
      }).then(function(rc) {
        $scope.labs = rc.data;
      });
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      var currentDate = new Date();
      if (!$scope.data.clazz) {
        modalService.warningInfrom('请选择班级!', '');
      } else if (!$scope.data.exp) {
        modalService.warningInfrom('请选择实验!', '');
      } else if (!$scope.data.lab) {
        modalService.warningInfrom('请选择实验室!', '');
      } else if($scope.data.applyDate < currentDate){
        modalService.warningInfrom('预约日期不能是今天之前的日期','')
      }else {
        $scope.data.applyDate = moment($scope.data.applyDate).format('YYYY-MM-DD');
        var rc = $scope.data;
        var map = {
          "clazz": rc.clazz.id,
          "semester": rc.semester.id,
          "experiment": rc.exp.id,
          "lab": rc.lab.id,
          "slot": rc.slot.id
        };
        var data = {
          "number": "10001",
          "remark": rc.remark,
          "count": rc.count,
          "applyDate": rc.applyDate
        };
        qService.tokenHttpPost(Reservation.clazzReservation, map, data).then(function(rdata) {
          if(rdata.errorCode == "DUPLICATION"){
            modalService.signleConfirmInform('该实验室在您选择的时间段已有预约!', '请重新选择时间段进行预约', 
              'warning', function() {
            });  
          }else{
            modalService.signleConfirmInform('添加成功', '', 'success', function() {
              $modalInstance.close({"":""});    
            });  
          }
          
        });
        
      }
    };
  })

//学生预约窗口
.controller('StudentReservationModalCtrl', function($scope, $modalInstance,
    data, semester, slots, exps, qService, Exp, modalService) {
    $scope.slots = slots.data;
    $scope.exps = exps.data;
    $scope.labs = [];
    $scope.data = data;
    $scope.data['semester'] = semester;

    $scope.now = new Date();
    $scope.data['applyDate'] = new Date();
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

    $scope.expChanged = function() {
      qService.tokenHttpGet(Exp.labs, {
        id: $scope.data.exp.id
      }).then(function(rc) {
        $scope.labs = rc.data;
      });
    };
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      var currentDate = new Date();
      if (!$scope.data.exp) {
        modalService.warningInfrom('请选择实验!', '');
      } else if (!$scope.data.lab) {
        modalService.warningInfrom('请选择实验室!', '');
      } else if (!$scope.data.slot) {
        modalService.warningInfrom('请选择时段!', '');
      } else if($scope.data.applyDate < currentDate){
        modalService.warningInfrom('预约日期不能是今天之前的日期','')
      } else {
        $scope.data.applyDate = moment($scope.data.applyDate).format('YYYY-MM-DD');
        $modalInstance.close($scope.data);
      }
    };
  })
  .controller('EmptyModalCtrl', function($scope, $modalInstance, data, rId,
    qService, Reservation, modalService) {
    $scope.data = data;
    $scope.rId = rId;
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      //modalService.signleConfirmInform('实验编号不能为空', '请输入编号', 'warning', function() {});
      $modalInstance.close($scope.data);
    };

    $scope.cancelStReservation = function(rId){
      modalService.doubleConfirmInform("确定要取消这个预约么?","",'warning',function(){
        qService.tokenHttpDelete(Reservation.cancelReservation, {
          id: rId,
          resType: 'studentReservation'
        }).then(function(rc){
          $modalInstance.close('refresh');
        });        
      });
    };

  })
  .controller('ReservationDetailModalCtrl', function($scope, $modalInstance, data,
    qService, Reservation, modalService) {
    //
    $scope.data = data;
    if($scope.data.status == 'APPROVED'){
      //load 教师
      qService.tokenHttpGet(Reservation.reservationLabTeachers, 
        {"id":$scope.data.id}).then(function(data){
          $scope.data.labTeachers = data.data;
        });
    }
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      $modalInstance.close($scope.data);
    };

    $scope.cancelReservation = function(rId){
      modalService.doubleConfirmInform("确定要取消这个预约么?","",'warning',function(){
        qService.tokenHttpDelete(Reservation.cancelReservation, {
          id: rId,
          resType: 'classReservation'
        }).then(function(rc){
          $modalInstance.close('refresh');
        });        
      });
    };
  })
  .controller('EmptyExpModalCtrl', function($scope, $modalInstance, data, modalService) {
    $scope.data = data;

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      if (!$scope.data.name || $scope.data.name.length == 0) {
        modalService.signleConfirmInform('实验编号不能为空', '请输入编号', 'warning', function() {});
      } else if (!$scope.data.number || $scope.data.number.length == 0) {
        modalService.signleConfirmInform('实验名称不能为空', '请输入名称', 'warning', function() {});
      } else {
        $modalInstance.close($scope.data);
      }
    };
  })
  .controller('EmptyCourseModalCtrl', function($scope, $modalInstance, data, modalService) {
    $scope.data = data;

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      if (!$scope.data.name || $scope.data.name.length == 0) {
        modalService.signleConfirmInform('课题编号不能为空', '请输入编号', 'warning', function() {});
      } else if (!$scope.data.number || $scope.data.number.length == 0) {
        modalService.signleConfirmInform('课题名称不能为空', '请输入名称', 'warning', function() {});
      } else if (!$scope.data.department || $scope.data.department.length == 0) {
        modalService.signleConfirmInform('开课单位不能为空', '请输入开课单位', 'warning', function() {});
      } else {
        $modalInstance.close($scope.data);
      }
    };
  })
  .controller('VerifyModalCtrl', function($scope, $modalInstance, baseData,
    teachers, modalService) {
    $scope.data = baseData;
    $scope.teachers = [];
    for (var i = 0; i < teachers.data.length; i++) {
      var t = teachers.data[i];
      if(t.role == 'LAB_TEACHER' || t.role == 'ALL_TEACHER'){
        $scope.teachers.push(t);
      }
    };
    updateSelectedTeacher(); 
    $scope.selectedTeachers = [];
    $scope.filters = [{
      "status": 'REJECTED',
      "value": "拒绝"
    }, {
      "status": 'APPROVED',
      "value": "同意"
    }];
    $scope.data['status'] = $scope.filters[1];

    $scope.add = function() {
      if ($scope.teachers.length == 0) {
        modalService.warningInfrom("没有可用的实验教师了!", "");
        return;
      }
      $scope.selectedTeachers.push($scope.data.teacher);
      var list = [];
      for (var i = 0; i < $scope.teachers.length; i++) {
        if ($scope.selectedTeachers.indexOf($scope.teachers[i]) < 0) {
          list.push($scope.teachers[i]);
        }
      };
      $scope.teachers = list;
      updateSelectedTeacher();
    };

    function updateSelectedTeacher() {
      if ($scope.teachers.length > 0) {
        $scope.data.teacher = $scope.teachers[0];
      } else {
        if ($scope.selectedTeachers.length == 0)
          modalService.warningInfrom("没有可用的实验教师了!", "");
      }
    }

    $scope.remove = function(entity) {
      var index = $scope.selectedTeachers.indexOf(entity);
      $scope.selectedTeachers.splice(index, 1);
      $scope.teachers.push(entity);
      updateSelectedTeacher();
    };

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      var map = {
        'status': $scope.data.status.status,
        'teacherIds': []
      };
      for (var i = 0; i < $scope.selectedTeachers.length; i++) {
        map.teacherIds.push($scope.selectedTeachers[i].id);
      };
      if (map.status == 'APPROVED' && map.teacherIds.length == 0) {
        modalService.warningInfrom('必须选择一个实验教师！', '');
        return;
      } else if (map.status == 'REJECTED') {
        map.teacherIds = [-1];
      }
      $modalInstance.close(map);
    };
  })
  .controller('SemesterModalCtrl', function($scope, $modalInstance, data, modalService) {
    $scope.data = data;
    $scope.data['startDate'] = new Date();
    $scope.data['endDate'] = new Date();
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
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      var map = {
        'name': $scope.data.name
      };
      map['startDate'] = moment($scope.data.startDate).format('YYYY-MM-DD');
      map['endDate'] = moment($scope.data.endDate).format('YYYY-MM-DD');
      if (!$scope.data.name || $scope.data.name.length == 0) {
        modalService.signleConfirmInform('学期名不能为空', '请输入学期名', 'warning', function() {});
      } else {
        $modalInstance.close(map);
      }
    };
  })
  .controller('LabFormCtrl', function($scope, $modalInstance, data, modalService) {
    //实验室弹窗
    $scope.data = data;
    $scope.selected = [{
      value: true,
      text: '开放'
    }, {
      value: false,
      text: '关闭'
    }];
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      $scope.data.capacity = parseInt($scope.data.capacity);
      if (!$scope.data.number || $scope.data.number.length == 0) {
        modalService.signleConfirmInform('编号不能为空', '请输入编号', 'warning', function() {});
      } else if (!$scope.data.name || $scope.data.name.length == 0) {
        modalService.signleConfirmInform('实验室名不能为空', '请输入实验室名', 'warning', function() {});
      } else {
        $modalInstance.close($scope.data);
      }
    };
  })
  .controller('PasswordModalCtrl', function($scope, $modalInstance, data, modalService) {
    //修改密码弹窗
    $scope.data = {
      "password": "",
      "rpassword": ""
    };
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      if ($scope.data.password.length == 0) {
        modalService.signleConfirmInform('密码不能为空', '请重新输入要更改的密码', 'warning', function() {});
      } else if ($scope.data.password != $scope.data.rpassword) {
        modalService.signleConfirmInform('密码输入不一致', '请重新输入要更改的密码', 'warning', function() {});
        $scope.data.password = "";
        $scope.data.rpassword = "";
      } else {
//    	  qService.tokenHttpPut(Account.password, {
//            id: $rootScope.currentUser.id
//          }, {
//          	
//            "oldPassword": rc.oldPassword,
//            "newPassword": rc.newPassword
//          }).then(function(rc) {
//            if (rc.data) {
//              modalService.signleConfirmInform('修改密码成功', '', 'success', function() {});
//            }
//          });
       $modalInstance.close($scope.data);
      }
    };
  })
  .controller('UserPasswordModalCtrl', function($scope, $modalInstance, data, modalService,
    qService, $rootScope, Account, sessionService) {
    //修改密码弹窗
    $scope.data = {
    	
      "oldPassword": "",
      "newPassword": "",
      "rnewPassword": ""
    };
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      if ($scope.data.oldPassword.length == 0) {
    	  modalService.signleConfirmInform('旧密码不能为空', '请输入旧密码', 'warning', function() {});
//      } else if ($scope.data.password != $scope.data.oldPassword) {
//    	  modalService.signleConfirmInform('旧密码输入错误', '请重新输入要旧密码:'+$scope.data.password, 'warning', function() {});
      } else if ($scope.data.newPassword.length == 0) {
    	  modalService.signleConfirmInform('新密码不能为空', '请输入新密码', 'warning', function() {});
      } else if ($scope.data.rnewPassword.length == 0) {
    	  modalService.signleConfirmInform('确认新密码不能为空', '请再次输入新密码', 'warning', function() {});
      } else if ($scope.data.newPassword != $scope.data.rnewPassword) {
    	  modalService.signleConfirmInform('密码输入不一致', '请重新输入要更改的密码', 'warning', function() {});
      } else {
        qService.tokenHttpPut(Account.password, {
          id: $rootScope.currentUser.id
        }, {
          "oldPassword": $scope.data.oldPassword,
          "newPassword": $scope.data.newPassword
        }).then(function(rc) {
          if (rc.data) {
            modalService.signleConfirmInform('修改密码成功', '请用新密码重新登录', 'success', function() {
              sessionService.delToken();
              $modalInstance.close({});
            });
          } else {
            modalService.signleConfirmInform('修改密码失败', '密码错误', 'warning', function() {});
          }
        });
      }
      
    };
  })
  .controller('TeacherModalCtrl', function($scope, $modalInstance, data, modalService) {
    $scope.data = data;
    if (!$scope.data.id) {
      $scope.data['gender'] = 'MALE';
      $scope.data['role'] = 'NOR_TEACHER';
    }
    $scope.genderMap = {
      'MALE': "男",
      'FEMALE': '女'
    };
    $scope.genders = ['MALE', 'FEMALE'];

    $scope.teacherMap = {
      'ALL_TEACHER': '课程和实验教师',
      'NOR_TEACHER': '课程教师',
      'LAB_TEACHER': '实验教师'
    };
    $scope.teachers = ['ALL_TEACHER', 'NOR_TEACHER', 'LAB_TEACHER'];

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      if (!$scope.data.number) {
        modalService.signleConfirmInform('账号名不能为空', '请输入账号', 'warning', function() {});
      } else if (!$scope.data.initialPassword && !$scope.data.id) {
        modalService.signleConfirmInform('密码不能为空', '请输入密码', 'warning', function() {});
      } else if (!$scope.data.name) {
        modalService.signleConfirmInform('用户名不能为空', '请输入用户名', 'warning', function() {});
      } else {
        $modalInstance.close($scope.data);
      }
      $modalInstance.close($scope.data);
    };
  })
  .controller('StudentModalCtrl', function($scope, $modalInstance, data, modalService) {
    $scope.data = data;
    if (!$scope.data.id) {
      $scope.data['gender'] = 'MALE';
    }
    $scope.genderMap = {
      'MALE': "男",
      'FEMALE': '女'
    };
    $scope.genders = ['MALE', 'FEMALE'];

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      if (!$scope.data.number) {
        modalService.signleConfirmInform('账号名不能为空', '请输入账号', 'warning', function() {});
      } else if (!$scope.data.initialPassword && !$scope.data.id) {
        modalService.signleConfirmInform('密码不能为空', '请输入密码', 'warning', function() {});
      } else if (!$scope.data.name) {
        modalService.signleConfirmInform('姓名不能为空', '请输入姓名', 'warning', function() {});
      } else {
        $modalInstance.close($scope.data);
      }
    };
  })

  .controller('AddStudentToClassCtrl', function($scope, $modalInstance, data, modalService) {
    $scope.data = data;
    if (!$scope.data.id) {
      $scope.data['gender'] = 'MALE';
    }
    $scope.genderMap = {
      'MALE': "男",
      'FEMALE': '女'
    };
    $scope.genders = ['MALE', 'FEMALE'];

    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      if (!$scope.data.number) {
        modalService.signleConfirmInform('学号不能为空', '请输入学号', 'warning', function() {});
      }else if (!$scope.data.name) {
        modalService.signleConfirmInform('姓名不能为空', '请输入姓名', 'warning', function() {});
      }  else {
        $modalInstance.close($scope.data);
      }
    };
  })
  /******************************
  * 学生相关的弹窗
  *******************************/
  .controller('StExpDetailsModalCtrl', function($scope, $modalInstance, data, modalService, qService, StudentRecord) {
    $scope.data = data;
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      $modalInstance.close($scope.data);
    };
    $scope.download = function(filename){
      var report;

      for (var i = 0; i < data.reports.length; i++) {
        if(data.reports[i].filename == filename){
          report = data.reports[i];
          break;
        }
      }
      

      qService.tokenHttpGet(StudentRecord.lashenreport,{
               'filename':filename,
                'file1':report['file1'],
                'file2':report['file2']
            },{}).then(function(rc){
                window.open("http://10.10.92.189:8081/report/"+filename+".doc");
            });
    };

    $scope.download2 = function(xls){
      qService.tokenHttpGet(StudentRecord.niuzhuanreport,{
               'filename':xls['filename'],
                'file':xls['file']
            },{}).then(function(rc){
                window.open("http://10.10.92.189:8081/report/"+xls['filename']+".doc");
            });
    }
  })
  .controller('TeaExpDetailsModalCtrl', function($scope, $modalInstance, data, modalService, qService, StudentRecord) {
    $scope.data = data;
    $scope.cancel = function() {
      $modalInstance.dismiss('canceled');
    };
    $scope.save = function() {
      qService.tokenHttpPut(StudentRecord.update,{
        "id": data.recordId
    },{"experimentRecord":data.experimentRecord,"experimentComment":data.experimentComment}).then(function(rc){
      if (rc.data) {
          modalService.signleConfirmInform('修改成功', '', 'success', function() {});
      }
    });
      $modalInstance.close($scope.data);
    };

    $scope.download = function(filename){
      var report;

      for (var i = 0; i < data.reports.length; i++) {
        if(data.reports[i].filename == filename){
          report = data.reports[i];
          break;
        }
      }
      

      qService.tokenHttpGet(StudentRecord.lashenreport,{
               'filename':filename,
                'file1':report['file1'],
                'file2':report['file2']
            },{}).then(function(rc){
                window.open("http://10.10.92.189:8081/report/"+filename+".doc");
            });
    };

    $scope.download2 = function(xls){
      qService.tokenHttpGet(StudentRecord.niuzhuanreport,{
               'filename':xls['filename'],
                'file':xls['file']
            },{}).then(function(rc){
                window.open("http://10.10.92.189:8081/report/"+xls['filename']+".doc");
            });
    }

  })
  ;