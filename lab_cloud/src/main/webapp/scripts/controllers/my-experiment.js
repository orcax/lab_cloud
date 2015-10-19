'use strict';

/**
 * @ngdoc function
 * @name prjApp.controller:MyExperimentCtrl
 * @description
 * # MyExperimentCtrl
 * Controller of the prjApp
 */
angular.module('prjApp')
  .controller('MyExperimentCtrl', function ($scope, dialogs, SystemManageService,
    Generalservice, $upload, $sessionStorage, $window) {

    var semesterStartDate;
    var semesterEndDate;
    var weekStartDate;

    $scope.currentDate = new Date();
    $scope.currentSelectedDay = new Date();
    $scope.weekConfig = {'select':null};

    $scope.mondayResult = [];
    $scope.tuesdayResult = [];
    $scope.wednesdayResult = [];
    $scope.thursdayResult = [];
    $scope.fridayResult = [];
    $scope.saturdayResult = [];
    $scope.sundayResult = [];

    $scope.classResult = [];
    $scope.reservationResult = [];
    $scope.totalNum = 0;
    $scope.reservationMap = {};
    $scope.currentPage = {'currentPage':1 , 'openedRservationPage':1};
    $scope.openedRservationSize = 0;
    $scope.reservationFilterItems = [
      {
        value:'所有预约',
        key : 'ALL'
      },
      {
        value:'班级预约',
        key : 'CLASS'
      },
      {
        value:'自行预约',
        key:'STUDENT'
      }
    ];
    $scope.reservationSelected = {filter:$scope.reservationFilterItems[0]};


    Date.prototype.Format = function(fmt) { 
      var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds()
      };
      if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
      for (var k in o) if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
      return fmt;
    };

    loadSemester();
    loadReservation();
    loadClasses();

    function pagenateList(lst){
      var pageSize = 8;
      var rcMap = {};
      for (var i = 1; i <= parseInt(lst.length / pageSize + 1); i++) {
        rcMap[i+''] = lst.slice((i-1)*pageSize, pageSize*i);
      };
      return rcMap;
    };

    function wrapperStReservation(entity, rtype){
      return  {
        lab : entity.lab,
        experiment : entity.experiment,
        clazz : entity.clazz,
        id : entity.id,
        type : rtype,
        date : entity.date,
        slot : entity.slot
      };
    };

    function loadReservation() {
      var promise = SystemManageService.getAllReservationsUnderStudentProfile();
      promise.then(
        function(data) {
          var lst = JSOG.parse(JSOG.stringify(data.data));
          var filterList = [];
          //filter rejected entity
          for (var i = 0; i < lst.length; i++) {
            var entity = lst[i];
            if(entity.approvalStatus == "APPROVED"){
              filterList.push(wrapperStReservation(entity,'CLASS'));
            }
          };
          $scope.allStReservation = filterList; //存储所有的学生预约
          loadStReservation();
        },
        function(data){
          Generalservice.informError('获取预约失败！');
        }
      );
    };

    function loadStReservation(){
      var promise = SystemManageService.studentGetGrabedReservations();
      promise.then(function(data){
        var lst = JSOG.parse(JSOG.stringify(data.data));
        for (var i = 0; i < lst.length; i++) {
          $scope.allStReservation.push(wrapperStReservation(lst[i],'STUDENT'));
        };
        $scope.allStReservation = $scope.allStReservation.reverse();
        $scope.totalNum = $scope.allStReservation.length;
        $scope.reservationMap = pagenateList($scope.allStReservation);;
        $scope.reservationResult = $scope.reservationMap[$scope.currentPage.currentPage+''];
      },function(e){
        Generalservice.informError('获取个人预约失败！');
      });
    };

    function filterType(key){
      var result_reservation_lst = [];
      if(key == 'ALL'){
        result_reservation_lst = $scope.allStReservation;
      }else{
        for (var i = 0; i < $scope.allStReservation.length; i++) {
          if($scope.allStReservation[i].type == key){
            result_reservation_lst.push($scope.allStReservation[i]);
          }
        }
      }
      $scope.totalNum = result_reservation_lst.length;
      $scope.reservationMap = pagenateList(result_reservation_lst);
      $scope.reservationResult = $scope.reservationMap[$scope.currentPage.currentPage+''];
    }

    $scope.changeRservationFitler = function(){
      $scope.currentPage.currentPage  = 1;
      filterType($scope.reservationSelected.filter.key);
    };

    $scope.loadStReservation = function(){
      //获取班级预约
      $scope.reservationResult = $scope.reservationMap[$scope.currentPage.currentPage+''];
    };

    $scope.loadStOpenedReservations = function(){
      var openedRservationPromise = SystemManageService.loadStudentOpenedReservations(1);
      openedRservationPromise.then(function(data){
        var rc = JSOG.parse(JSOG.stringify(data));
        $scope.openedRservationList = rc.data;
        $scope.currentPage.openedRservationPage = rc.currPageNum;
        $scope.openedRservationSize = rc.totalItemNum;
        if(rc.totalItemNum == 0){
          Generalservice.inform('没有开放的自行预约实验！');
        }
      },function(e){
        Generalservice.informError('获取自行预约列表失败!');
      });
    };

    //todo
    $scope.sendGrapReservation = function(entity){
      var dialog = dialogs.create('/template/lab-confirm-dialog.html', 'GrabReservationConfirmCtrl', {
        text: '你确定要在这个时间段做 '+entity.experiment.experimentName+' 实验么？',
        cancelBtnText: '取消',
        confirmBtnText: '确认'
      }, {
        size: 'sm',
        keyboard: true,
        backdrop: true,
        windowClass: 'model-overlay'
      });

      dialog.result.then(function(data){
        var promise = SystemManageService.studentGrabReservation(entity.id);
        promise.then(function(data){
          var data = JSOG.parse(JSOG.stringify(data));
          Generalservice.inform('提交预约成功，可以在已预约列表查看！');
          loadReservation();
        },function(e){
          Generalservice.informError('提交预约失败，请重新提交！');
        })
      });

    };

    function loadClasses() {
      var promise = SystemManageService.loadStudentClasses();
      promise.then(
        function(data) {
          $scope.classResult = JSOG.parse(JSOG.stringify(data.data));
          if($scope.classResult!= null && $scope.classResult.length > 0){
            $scope.viewCourseDetails($scope.classResult[0]);
            for (var i = 0; i < $scope.classResult.length; i++) {
              $scope.classResult[i]['acitve'] = false;
            };
            $scope.classResult[0]['acitve']  = true;
          }
        },
        function(data) {

        }
      );
    };

    $scope.viewCourseDetails = function(result){
      var promise = SystemManageService.studentCourseExps(result.id);
      promise.then(function(data){
        $scope.expList = JSOG.parse(JSOG.stringify(data.data));
        console.log($scope.expList);
      },function(e){
        //console.log('Error happened when load student courses exps.')
      });
    };


    $scope.loadReports = function(fileList){
      var lst = $scope.fitlerFileList(fileList, 'report');
      if(lst!=null){
        return lst;
      }else{
        return [];
      }
    };

    $scope.loadDatas = function(fileList){
      var lst = $scope.fitlerFileList(fileList, 'expData');
      if(lst!=null){
        return lst;
      }else{
        return [];
      }
    };

    $scope.loadPhotos = function(fileList){
      var lst = $scope.fitlerFileList(fileList, 'photo');
      if(lst!=null){
        return lst;
      }else{
        return [];
      }
    };

    $scope.fitlerFileList = function(filterList, key){
      if(filterList.length == 0){
        return null;
      }else{
        var list = [];
        for (var i = 0; i < filterList.length; i++) {
          if(filterList[i].type == key){
            list.push($scope.wrapperFileRecord(filterList[i]));
          }
        }
        return list;
      }
    };

    $scope.wrapperFileRecord = function(file){
      var entity = {
        create_time : file.create_time,
        type : file.type,
        path : file.path
      };
      return entity;
    };


    $scope.view_exp_details = function(expEntity){
      var entity ={
        courseName : expEntity.clazz.course.courseName,
        studentNumber : expEntity.student.number,
        studentName : expEntity.student.name,
        semester : expEntity.clazz.semester.semesterName,
        reports : $scope.loadReports(expEntity.fileRecords),
        expDataList :$scope.loadDatas(expEntity.fileRecords),
        photos: $scope.loadPhotos(expEntity.fileRecords),
        experimentName:expEntity.experiment.experimentName,
        experimentComment : expEntity.experimentComment,
        experimentRecord : expEntity.experimentRecord
      };
      var dialog = dialogs.create('template/lab-exp-details-dialog.html','ViewExpDetailsCtrl', entity,{
        size:'md',keyboard: true,backdrop: 'static',windowClass: 'model-overlay'
      });
    };

    $scope.generateTemplate = function(srid){
      var promise = SystemManageService.generateReportTemplate(srid);
      promise.then(function(data){
        var data = JSOG.parse(JSOG.stringify(data));
        $window.open(data.data, '_blank');
      },function(e){
        Generalservice.informError('内部错误，生成模板失败！');
      });
    };



    $scope.uploadAssignment = function(files, clazz_id, exp_id) {
      if (files && files.length) {
          for (var i = 0; i < files.length; i++) {
              var file = files[i];
              $upload.upload({
                  url: '/Student/upload/report/class/'+clazz_id+'/experiment/' + exp_id,
                  data: {
                      'token': $scope.token,
                      'accountCharacter':$sessionStorage.character
                  },
                  file: file,
                  fileFormDataName: 'data'
              }).progress(function(evt) {
                  var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
              }).success(function(data, status, headers, config) {
                  Generalservice.inform('作业上传成功!');
                  $scope.viewCourseDetails();
              }).error(function(){
                  Generalservice.informError('作业上传失败!');
              });

          }
      }
    };

    function loadSemester() {
      var promise = SystemManageService.getCurrentSemesterUnderStudentProfile();
      promise.then(
        function(data) {
          semesterStartDate = data.data.startDate;
          semesterEndDate = data.data.endDate;

          initWeekList();
          semesterStartDate = semesterStartDate.replace(/-/g,"/");
          weekStartDate = weekStartDate.Format("yyyy-MM-dd");

          var dateStart = Date.parse(semesterStartDate);
          var dateEnd = Date.parse(semesterEndDate);
          var dateCurrent = Date.parse(weekStartDate); 
          var diffDay = Math.ceil((dateCurrent - dateStart) / (24 * 60 * 60 * 1000));
          $scope.weekNumber = Math.ceil(Math.ceil((dateEnd - dateStart) / (24 * 60 * 60 * 1000)) / 7);
          
          if(diffDay < 0){
            Generalservice.inform('当前不在本学期内！')
          }else{
            $scope.weekIndex = Math.ceil(diffDay / 7);
          }

          $scope.weeks = [];
          for (var i = 0; i < $scope.weekNumber; i++) {
            $scope.weeks.push({'week': i + 1});
          };
          $scope.weekConfig.selected = $scope.weeks[$scope.weekIndex - 1];
          viewWeekPlan($scope.weekIndex);
        },
        function(data) {

        }
      );
    };

    function wrapSlotPlan(entity, slotIndex, dayIndex){

    };
    

    function viewWeekPlan(weekIndex) {
      var promise = SystemManageService.getStudentWeekPlan(weekIndex);
      promise.then(function(result) {
        $scope.weekPlanResult = JSOG.parse(JSOG.stringify(result.data));

        //更新一下 Week List
        var date = new Date(result.data[0].date);
        $scope.currentDate = date;
        initWeekList();

        //构建学生二维表
        $scope.slotList = [
                    {
                        slot:'1,2节',
                        slotKey:'ONE',
                        list:[[],[],[],[],[],[],[]]
                    },
                    {
                        slot:'3,4节',
                        slotKey:'TWO',
                        list:[[],[],[],[],[],[],[]]
                    },
                    {
                        slot:'中午',
                        slotKey:'THREE',
                        list:[[],[],[],[],[],[],[]]
                    },
                    {
                        slot:'5,6节',
                        slotKey:'FOUR',
                        list:[[],[],[],[],[],[],[]]
                    },
                    {
                        slot:'7,8节',
                        slotKey:'FIVE',
                        list:[[],[],[],[],[],[],[]]
                    },
                    {
                        slot:'晚上(上)',
                        slotKey:'SIX',
                        list:[[],[],[],[],[],[],[]]
                    },
                    {
                        slot:'晚上(下)',
                        slotKey:'SEVEEN',
                        list:[[],[],[],[],[],[],[]]
                    }
                ];

        //循环构建学生预约二维表
        for (var i = 0; i < $scope.currentWeekList.length; i++) {
          var weekDate = $scope.currentWeekList[i].date.Format('yyyy-MM-dd');
          for (var j = 0; j < $scope.allStReservation.length; j++) {
            var entity = $scope.allStReservation[j];
            if(entity.date == weekDate){
              for (var k = 0; k < $scope.slotList.length; k++) {
                if($scope.slotList[k].slotKey == entity.slot){
                  $scope.slotList[k].list[i].push(entity);
                }
              };
            }
          };
        };

      },
      function(result) {
        //console.log(result);
      });
    }

    function initWeekList() {
      $scope.currentWeekList = [];

      var currentIndex = $scope.currentDate.getDay();
      if(currentIndex == 0)
      currentIndex = 7;

      weekStartDate = new Date($scope.currentDate.getTime() - (currentIndex - 1) * 1000 * 60 * 60 * 24);
      for(var i = 0; i < 7; i++) {
        var date = new Date(weekStartDate.getTime() + i * 1000 * 60 * 60 * 24);
        switch(i) {
          case 0:
            $scope.currentWeekList.push({label: '周一', date: date, list: []});
            break;
          case 1:
            $scope.currentWeekList.push({label: '周二', date: date, list: []});
            break;
          case 2:
            $scope.currentWeekList.push({label: '周三', date: date, list: []});
            break;
          case 3:
            $scope.currentWeekList.push({label: '周四', date: date, list: []});
            break;
          case 4:
            $scope.currentWeekList.push({label: '周五', date: date, list: []});
            break;
          case 5:
            $scope.currentWeekList.push({label: '周六', date: date, list: []});
            break;
          case 6:
            $scope.currentWeekList.push({label: '周日', date: date, list: []});
            break;
        }
      }

      $scope.currentWeekList[currentIndex - 1].date = new Date($scope.currentDate.getTime());
    };

    $scope.lastWeek = function(){
      if($scope.weekIndex > 1){
        $scope.weekIndex = $scope.weekIndex - 1;
        $scope.weekConfig.selected = $scope.weeks[$scope.weekIndex - 1];
        viewWeekPlan($scope.weekIndex);
      }else{
        Generalservice.inform("已到本学期最后一周");
      }
    };

    $scope.nextWeek = function(){
      if($scope.weekIndex < $scope.weekNumber){
        $scope.weekIndex = $scope.weekIndex + 1;
        $scope.weekConfig.selected = $scope.weeks[$scope.weekIndex - 1];
        viewWeekPlan($scope.weekIndex);
      }else{
        Generalservice.inform("已到本学期第一周");
      }
    };

    $scope.toSelectedWeek = function(){
      $scope.weekIndex = $scope.weeks.indexOf($scope.weekConfig.selected);
      $scope.weekIndex = $scope.weekIndex + 1;
      viewWeekPlan($scope.weekIndex);
    };

    $scope.experiment_detail = function(record){
      var entity = {
        'date':record.date,
        'slot':record.slot,
        'mission':record.experiment.experimentName,
        //'course': record.clazz.course.courseName,
        //'clazz': record.clazz.classHour,
        'room':record.lab.labName
      };
      if(record.type == 'CLASS'){
        entity.course = record.clazz.course.courseName;
        entity.clazz = record.clazz.classHour;
      }
      var dialog = dialogs.create('template/t-experiment-details-dialog.html','ExpOrderCtrl', entity,{
          size:'md',keyboard: true,backdrop: 'static',windowClass: 'model-overlay'
      });
    };


  }).controller('ExpOrderCtrl',function($scope,$modalInstance,data){
        $scope.user = {name : ''};
        $scope.data = data;
        $scope.student = false;
        if(data.role == "student"){
            $scope.student = true;
        }
        $scope.teacher = false;
        if (data.role == "teacher") {
            $scope.teacher = true;
        };
        $scope.admin = false;
        if (data.role == "admin") {
          $scope.admin = true;
        };

        $scope.cancel = function(){
            $modalInstance.dismiss('canceled');
        }; // end cancel

        $scope.save = function(){
            $modalInstance.close($scope.user.name);
        }; // end save

        $scope.hitEnter = function(evt){
            if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.name,null) || angular.equals($scope.name,'')))
                $scope.save();
        };
  }).controller('ViewExpDetailsCtrl',function($scope,$modalInstance,data){
        $scope.user = {name : ''};
        $scope.data = data;
        $scope.student = false;
        if(data.role == "student"){
            $scope.student = true;
        }
        $scope.teacher = false;
        if (data.role == "teacher") {
            $scope.teacher = true;
        };
        $scope.cancel = function(){
            $modalInstance.dismiss('canceled');
        }; // end cancel
        //Generalservice.inform(data);
        
        $scope.save = function(){
            $modalInstance.close($scope.user.name);
        }; // end save

        $scope.hitEnter = function(evt){
            if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.name,null) || angular.equals($scope.name,'')))
                $scope.save();
        }; // end hitEnter
  }).controller('GrabReservationConfirmCtrl',function($scope,$modalInstance,data){
        $scope.data = data;
        $scope.cancel = function(){
            $modalInstance.dismiss('canceled');
        }; // end cancel
        $scope.confirm = function(){
            $modalInstance.close({});
        }; // end save

        $scope.hitEnter = function(evt){
            if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.name,null) || angular.equals($scope.name,'')))
                $scope.save();
        }; // end hitEnter
  });
