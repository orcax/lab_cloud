'use strict';

/**
 * @ngdoc function
 * @name prjApp.controller:LabApplyManageCtrl
 * @description
 * # LabApplyManageCtrl
 * Controller of the prjApp
 */

angular.module('prjApp')
  .controller('LabApplyManageCtrl', function($scope, $location, dialogs, SystemManageService, Generalservice) {
    $scope.tabHref = function(path) {
      $location.path(path);
    };
    $scope.currentNumber = 1;
    $scope.totalCount = 0;
    $scope.currentPage = {
      currentPage: ''
    };
    $scope.isAll = {
      curr:false
    };
    $scope.reservationList = [];
    $scope.pageScaleList = [];

    $scope.format = 'yyyy-MM-dd';
    var now = new Date();
    var endDt;
    if(now.getMonth() != 11){
      endDt = new Date(now).setMonth(now.getMonth() + 1);
    }else{
      endDt = new Date(now).setYear(now.getYear() + 1).setMonth(0);
    }
    $scope.filterDate = {
      startDate : now,
      endDate   : new Date(endDt)
    };
    var stDate = function(){
      return $scope.filterDate.filterDate;
    }

    $scope.stDt = function(){
      return $scope.filterDate.startDate;
    };
    $scope.edDt = function(){
      return $scope.filterDate.endDate;
    };

    $scope.$watch($scope.stDt,function(newValue, oldValue){
      if(newValue > $scope.filterDate.endDate){
        Generalservice.informError('起始时间不能晚于结束时间！');
        $scope.filterDate.startDate = oldValue;
      }else{
        $scope.changeScope();
      }
    });

    $scope.$watch($scope.edDt,function(newValue, oldValue){
      if(newValue < $scope.filterDate.startDate){
        Generalservice.informError('结束时间不能早于起始时间！');
        $scope.filterDate.endDate = oldValue;
      }else{
        $scope.changeScope();
      }
    });

    //获取待审核的列表
    $scope.loadPeddingList = function(pageNum) {
      var promise = SystemManageService.getPeddingReservertions(pageNum,
        $scope.filterDate.startDate.format($scope.format),
        $scope.filterDate.endDate.format($scope.format));
      promise.then(function(data) {
          var data = JSOG.parse(JSOG.stringify(data));
          $scope.currentNumber = data.totalPageNum;
          $scope.currentPage.currentPage = data.currPageNum;
          $scope.totalCount = data.totalItemNum;
          if($scope.currentNumber == 0){
            $scope.reservationList = [];
          }else{
            $scope.reservationList = wrapperApplications(data.data);  
          }
        },
        function(data) {
          //console.log('Error happen when load reservation.')
        });
    };

    //获取已审核的列表
    $scope.loadAllApplicationList = function(pageNum) {
      var promise = SystemManageService.getAllReservertions(pageNum,
        $scope.filterDate.startDate.format($scope.format),
        $scope.filterDate.endDate.format($scope.format));
      promise.then(function(data) {
          var data = JSOG.parse(JSOG.stringify(data));
          $scope.currentNumber = data.totalPageNum;
          $scope.currentPage.currentPage = data.currPageNum;
          $scope.totalCount = data.totalItemNum;
          if($scope.currentNumber == 0){
            $scope.reservationList = [];
          }else{
            $scope.reservationList = wrapperApplications(data.data);  
          }
        },
        function(data) {
          //console.log('Error happen when load reservation.')
        });
    };

    function generateScaleList(size) {
      var lst = [];
      for (var i = 1; i <= size; i++) {
        lst.push(i);
      };
      return lst;
    }

    //获取列表需要的数据
    function wrapperApplications(lst) {
      var rcList = [];
      //console.log(lst);
      for (var i = 0; i < lst.length; i++) {
        var entity = lst[i];
        var obj = {};
        obj['id'] = entity.id;
        obj['date'] = entity.date;
        obj['status'] = entity.approvalStatus;
        obj['slot'] = entity.slot;
        obj['course'] = entity.clazz.course.courseName;
        obj['class'] = entity.clazz.classNumber;
        obj['teacher'] = entity.clazz.teacher.name;
        obj['experimentName'] = entity.experiment.experimentName;
        obj['labName'] = entity.lab.labName;
        rcList.push(obj);
      };
      return rcList;
    };

    $scope.changeScope = function() {
      if ($scope.isAll.curr) {
        $scope.loadAllApplicationList(1);
      } else {
        $scope.loadPeddingList(1);
      }
    };
    $scope.pageChanged = function() {
      ////console.log($scope.currentPage);
      if ($scope.isAll.curr) {
        $scope.loadAllApplicationList($scope.currentPage.currentPage);
      } else {
        $scope.loadPeddingList($scope.currentPage.currentPage);
      }
    };

    $scope.changeScope();
    //弹出窗口
    $scope.loadDetails = function(data) {
      var dialog = dialogs.create('/template/lab-apply-verify-dialog.html', 'ApplyVerifyCtrl',
        data, {
          size: 'md',
          keyboard: true,
          backdrop: 'static',
          windowClass: 'model-overlay'
        });
      dialog.result.then(function(data){
        
        //提交审核结果
        var promise = SystemManageService.submitVerifyResult(data);
        promise.then(function(data){
          Generalservice.inform('审核结果提交成功！');
          $scope.pageChanged();
        },
        function(e){
          Generalservice.informError('内部错误，请联系管理员');  
        });
      },
      function(e){
        //Generalservice.informError('内部错误，请联系管理员');
      }
      );
    };
  }).controller('ApplyVerifyCtrl', function($scope, $modalInstance, data, 
    SystemManageService, Generalservice) {
    $scope.data = data;
    $scope.labTeachers = [];
    $scope.selectedLabTeacher = $scope.labTeachers[0];
    $scope.selectedLabTeachers = [];

    $scope.verifyItems = [{
      name: '同意',
      value: 'APPROVED'
    }, {
      name: '拒绝',
      value: 'REJECTED'
    }];
    $scope.verify = $scope.verifyItems[0];

    $scope.addTeacherForReservation = function() {
      if ($scope.selectedLabTeachers.indexOf($scope.selectedLabTeacher) < 0 && $scope.selectedLabTeacher != undefined) {
        $scope.selectedLabTeachers.push($scope.selectedLabTeacher);
        removeTeacher($scope.selectedLabTeacher);
      }
    };

    var promise = SystemManageService.getAvailableLabTeacher(data.id);
    promise.then(function(data){
      var entities = data.data;
      var tempList = [];
      for (var i = 0; i < entities.length; i++) {
        var entity = entities[i];
        var tempEntity = {};
        tempEntity['id'] = entity.id;
        tempEntity['name'] = entity.name;
        tempList.push(entity);
      };
      $scope.labTeachers = tempList;
      $scope.selectedLabTeacher = tempList[0];
    },function(e){
      ////console.log('Fail to get available lab teachers.');
      Generalservice.informError('获取教师数据失败，请联系管理员');
    });

    $scope.removeLabTeacher = function(teacher) {
      var offset = $scope.selectedLabTeachers.indexOf(teacher);
      $scope.selectedLabTeachers.splice(offset, 1);
      if ($scope.labTeachers.indexOf(teacher) < 0) {
        addTeadher(teacher);
      }
    };

    function addTeadher(teacher) {
      $scope.labTeachers.push(teacher);
      if ($scope.labTeachers.length == 1) {
        $scope.selectedLabTeacher = $scope.labTeachers[0];
      }
    };

    function removeTeacher(teacher) {
      for (var i = 0; i < $scope.labTeachers.length; i++) {
        if ($scope.labTeachers[i] == teacher) {
          $scope.labTeachers.splice(i, 1);
          if ($scope.labTeachers.length > 0) {
            $scope.selectedLabTeacher = $scope.labTeachers[0];
          } else {
            $scope.selectedLabTeacher = undefined;
          }
          break;
        };
      };
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
      $scope.data.result = $scope.verify.value;
      var labTeacherIds = [];
      for (var i = 0; i < $scope.selectedLabTeachers.length; i++) {
        labTeacherIds.push($scope.selectedLabTeachers[i].id);
      };
      $scope.data.labTeachers = labTeacherIds;
      if($scope.labTeachers.length <= 0 && $scope.verify.value=='APPROVED'){
        Generalservice.informError('还没有安排实验教师！');
      }else{
        $modalInstance.close($scope.data);
      }
    };

    $scope.hitEnter = function(evt) {
      if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
        $scope.save();
    };
  });


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