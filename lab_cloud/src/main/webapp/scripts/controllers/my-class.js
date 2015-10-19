'use strict';

/**
 * @ngdoc function
 * @name prjApp.controller:MyClassCtrl
 * @description
 * # MyClassCtrl
 * Controller of the prjApp
 */
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
angular.module('prjApp')
    .controller('MyClassCtrl', function ($scope, dialogs, SystemManageService, 
        Generalservice, $sessionStorage, $upload){
        $scope.currentLabList = [];
        $scope.selectedLab = {'lab':undefined};

        $scope.currentExperimentList = [];
        $scope.currentClassList = [];
        $scope.copiedExperimentList = [];
        $scope.copiedClassList = [];

        $scope.weekConfig = {'select':null};


        $scope.classList = [];

        var semesterStartDate;
        var semesterEndDate;
        var weekStartDate;

        $scope.currentDate = new Date();

        $scope.mondayResult = [];
        $scope.tuesdayResult = [];
        $scope.wednesdayResult = [];
        $scope.thursdayResult = [];
        $scope.fridayResult = [];
        $scope.saturdayResult = [];
        $scope.sundayResult = [];

        $scope.teacherReservations = [];
        $scope.teacherReservationCount = '';
        $scope.reservationPage = {currPageNum: 1};

        $scope.labTeacherReservationCount = '';

        $scope.reservationTypes = [
            {
                value:'ALL',
                name:'所有预约'
            },
            {
                value:'APPROVED',
                name:'审核通过的预约'
            },
            {
                value:'PENDING',
                name:'审核中的预约'
            },
            {
                value:'REJECTED',
                name:'被拒绝的预约'
            }
        ];


        $scope.selectedReservationType = {selected:$scope.reservationTypes[0]};

        $scope.currentStuListData = {
            'class_id': null,
            'exp_id':null
        };
        loadLabs();

        function loadLabs() {
            var promise = SystemManageService.loadLab(1);
            promise.then(
                function(data) {
                    var data = JSOG.parse(JSOG.stringify(data));
                    //$scope.currentLabList = data.data;
                    var tempLabs = [];
                    for (var i = 0; i < data.data.length; i++) {
                        if(data.data[i].status!='CLOSED'){
                            tempLabs.push(data.data[i]);
                        }
                    };
                    $scope.currentLabList = tempLabs;
                    if($scope.currentLabList.length > 0){
                        $scope.selectedLab.lab = $scope.currentLabList[0];
                    }
                    loadSemester();
                },
                function(data) {
                    Generalservice.inform('获取实验室列表失败！');
                }
            );
        };

        function loadSemester() {
            var promise = SystemManageService.getCurrentSemesterUnderTeacherProfile();
            promise.then(
                function(data) {
                    semesterStartDate = data.data.startDate;
                    semesterEndDate = data.data.endDate;

                    initWeekList();
                    semesterStartDate = semesterStartDate.replace(/-/g,"/");
                    weekStartDate = weekStartDate.Format("yyyy-MM-dd").replace(/-/g,"/");

                    var dateStart = Date.parse(semesterStartDate);
                    var dateEnd = Date.parse(semesterEndDate);
                    var dateCurrent = Date.parse(weekStartDate); 
                    var diffDay = Math.ceil((dateCurrent - dateStart) / (24 * 60 * 60 * 1000));
                    $scope.weekNumber = Math.ceil(Math.ceil((dateEnd - dateStart) / (24 * 60 * 60 * 1000)) / 7);

                    if(diffDay < 0){
                        Generalservice.inform('当前不在本学期内！')
                    }else if((diffDay%7) == 0){
                        $scope.weekIndex = Math.ceil(diffDay / 7) + 1;
                    }else{
                        $scope.weekIndex = Math.ceil(diffDay / 7);
                    }

                    $scope.weeks = [];
                    for (var i = 0; i < $scope.weekNumber; i++) {
                        $scope.weeks.push({'week': i + 1});
                    };
                    $scope.weekConfig.selected = $scope.weeks[$scope.weekIndex - 1];
                    viewWeekPlan($scope.weekIndex, $scope.selectedLab.lab.id);
                },
                function(data) {

                }
            );
        };

        $scope.loadReservation = function() {
            var promise = SystemManageService.getTeacherReservations($scope.reservationPage.currPageNum
                ,$scope.selectedReservationType.selected.value);
            promise.then(
                function(data) {
                  var data = JSOG.parse(JSOG.stringify(data));
                  $scope.teacherReservationCount = data.totalItemNum;
                  $scope.reservationPage.currPageNum = data.currPageNum;
                  $scope.teacherReservations = wrapperApplications(data.data);
                },
                function(data) {

                }
            );
        };

        $scope.labTeacherRvMap = {};
        $scope.labTeacherRvPage = {'current':1};
        $scope.loadLabTeacherReservation = function(){
            var promise = SystemManageService.getLabTeacherReservation();
            promise.then(
                function(data){
                    var data = JSOG.parse(JSOG.stringify(data));
                    var lst = wrapperApplications(data.data);

                    var pageSize = 8;
                    var totlePage = lst.length / pageSize + 1;
                    var reservationMap = {};
                    for (var i = 1; i <= parseInt(totlePage); i++) {
                      reservationMap[i+''] = lst.slice((i-1)*pageSize, pageSize*i);
                    };
                    $scope.labTeacherRvMap = reservationMap;
                    $scope.labTeacherReservationCount  = lst.length;
                    if(lst.length > 0){
                        $scope.changeLabTeacherReservation();
                    }
                },
                function(e){

                }
            );
        }

        $scope.changeLabTeacherReservation = function(){
            $scope.labTeacherReservations =  $scope.labTeacherRvMap[$scope.labTeacherRvPage.current + ''];
        };

        $scope.loadReservation();
        $scope.loadLabTeacherReservation();
        //获取列表需要的数据
        function wrapperApplications(lst) {
            if (lst ==null){
                return [];
            }
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

        loadExperiments();

        function loadExperiments() {
            var promise = SystemManageService.loadExperiment(1);
            promise.then(
                function(data) {
                    data = JSOG.parse(JSOG.stringify(data));
                    $scope.currentExperimentList = data.data;
                    for (var i = 0; i < $scope.currentExperimentList.length; i++) {
                        $scope.copiedExperimentList.push({
                            'id': $scope.currentExperimentList[i].id,
                            'experimentName': $scope.currentExperimentList[i].experimentName,
                            'experimentNumber': $scope.currentExperimentList[i].experimentNumber
                        });
                    };
                },
                function(data) {

                }
            );
        };

        loadTeacherClasses();
        
        function loadTeacherClasses() {
            var promise = SystemManageService.loadTeacherClasses();
            promise.then(
                function(data) {
                    data = JSOG.parse(JSOG.stringify(data));
                    $scope.currentClassList = data.data;
                    for (var i = 0; i < $scope.currentClassList.length; i++) {
                        var entity = {
                            'id': $scope.currentClassList[i].id,
                            'classNumber': $scope.currentClassList[i].classNumber,
                            'classHour': $scope.currentClassList[i].classHour,
                            'classRoom': $scope.currentClassList[i].classRoom,
                            'courseName': $scope.currentClassList[i].course.courseName
                        };
                        if(i == 0){
                            entity.active = true;
                        }else{
                            entity.active = false;
                        }
                        $scope.copiedClassList.push(entity);
                    };
                    if ($scope.copiedClassList.length > 0){
                        $scope.view_exp($scope.copiedClassList[0].id);
                    };
                },
                function(data) {

                }
            );
        }

        function wrapSlotPlan(entity, offset, dayIndex){
            var plan = {};
            var keyPrefix = 'slot' + offset;
            plan['experiment'] = entity[keyPrefix+'Experiment'];
            plan['status'] = entity[keyPrefix+'OpenStatus'];
            plan['reservation'] = entity[keyPrefix+'ClassReservation'];
            plan['type'] = entity[keyPrefix + 'Type'];
            plan['slotIndex'] = offset;
            plan['dayIndex'] = dayIndex;

            if(plan.type == 'STUDENT'){
                plan['status'] = 'CLOSED';
            }else if(plan.reservation != null && plan.reservation.reserver.number != $sessionStorage.loginUser.number){
                plan['status'] = 'CLOSED';
            }
            var d = new Date(entity.date);
            if(d < Generalservice.getCurrentDate() && plan['status']=='OPEN'){
                plan['status'] = 'blank';
            }
            return plan;
        };

        //查看每周的预约
        function viewWeekPlan(weekIndex, labid) {
            var promise = SystemManageService.getTeacherWeekPlan(weekIndex, labid);
            promise.then(function(result) {
                var data = JSOG.parse(JSOG.stringify(result.data));
                $scope.weekPlanResult = data;
                var date = new Date(result.data[0].date);
                $scope.currentDate = date;
                initWeekList();

                //解析每周的预定列表，组成各个slot的数据结构
                 $scope.slotList = [{
                        slot: '1,2节',
                        list: []
                    }, {
                        slot: '3,4节',
                        list: []
                    }, {
                        slot: '中午',
                        list: []
                    }, {
                        slot: '5,6节',
                        list: []
                    }, {
                        slot: '7,8节',
                        list: []
                    }, {
                        slot: '晚（上）',
                        list: []
                    }, {
                        slot: '晚（下）',
                        list: []
                    }];
                for (var i = 0; i < $scope.weekPlanResult.length; i++) {
                    var entity = $scope.weekPlanResult[i];
                    for(var j = 1; j <= $scope.slotList.length ; j++){
                        $scope.slotList[j-1].list.push(wrapSlotPlan(entity,j,i+1));
                    }
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


        $scope.uploadStList =  function(files, clazz_id) {
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    // $upload.upload({
                    //     url: '/Account/upload/icon',
                    //     data: {
                    //         'token': $scope.token,
                    //         'accountCharacter':$rootScope.character
                    //     },
                    //     file: file,
                    //     fileFormDataName: 'data'
                    // }).progress(function(evt) {
                    //     var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    //     ////console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                    // }).success(function(data, status, headers, config) {
                    //     //console.log('file ' + config.file.name + 'uploaded. Response: ' + data);
                    //     Generalservice.inform('头像修改成功!');
                    //     $sessionStorage.loginUser.iconPath = data.data;
                    // });
                }
            }
        };


        $scope.changeLab = function(){
            ////console.log($scope.selectedLab);
            loadSemester();
        };

        $scope.lastWeek = function(){
            if($scope.weekIndex > 1){
                $scope.weekIndex = $scope.weekIndex - 1;
                $scope.weekConfig.selected = $scope.weeks[$scope.weekIndex - 1];
                var selectedLabIndex = $('#lab-select').get(0).selectedIndex;
                var labid = $('#lab-select').get(0).options[selectedLabIndex].getAttribute('labid');
                viewWeekPlan($scope.weekIndex, $scope.selectedLab.lab.id);
            }else{
                Generalservice.inform("已到本学期最后一周");
            }
        };

        $scope.nextWeek = function(){
            if($scope.weekIndex < $scope.weekNumber){
                $scope.weekIndex = $scope.weekIndex + 1;
                $scope.weekConfig.selected = $scope.weeks[$scope.weekIndex - 1];
                var selectedLabIndex = $('#lab-select').get(0).selectedIndex;
                var labid = $('#lab-select').get(0).options[selectedLabIndex].getAttribute('labid');
                viewWeekPlan($scope.weekIndex, $scope.selectedLab.lab.id);
            }else{
                Generalservice.inform("已到本学期第一周");
            }
        };

        $scope.toSelectedWeek = function(){
            $scope.weekIndex = $scope.weeks.indexOf($scope.weekConfig.selected) + 1;
            viewWeekPlan($scope.weekIndex, $scope.selectedLab.lab.id);
        };

      	$scope.is_class = false;
        $scope.stPage = {stPage:1};
        $scope.stCount = 0;
        $scope.view_class = function(class_id, exp_id){
            var promise = SystemManageService.loadClassStudentStatus(class_id, exp_id, $scope.stPage.stPage);
            //获取学生列表
            promise.then(function(data){
                var data = JSOG.parse(JSOG.stringify(data));
                $scope.stCount = data.totalItemNum;
                $scope.stPage.stPage = data.currPageNum;
                $scope.studentList = data.data;
                $scope.is_class = true;
                $scope.currentStuListData.class_id = class_id;
                $scope.currentStuListData.exp_id = exp_id;
            },function(e){
                Generalservice.inform('获取学生列表失败');
            });
        };

        $scope.view_st = function(){
            $scope.view_class($scope.currentStuListData.class_id,$scope.currentStuListData.exp_id);
        };


        $scope.refresh_class_stu_list = function(){
            if($scope.is_class){
                $scope.view_class($scope.currentStuListData.class_id,$scope.currentStuListData.exp_id);
            }
        };

        $scope.view_exp = function(class_id){
        	$scope.is_class = false;
            $scope.stPage.stPage = 1;
            $scope.stCount = 0;
            var promise = SystemManageService.loadTeacherClassesExps(class_id);
            promise.then(function(data){
                $scope.teacherExpList = JSOG.parse(JSOG.stringify(data.data));
            },function(e){
                //console.log('Error happen when load teacher class exps');
            });
        };

        $scope.view_order_details = function(){
        	var dialog = dialogs.create('/template/t-experiment-details-dialog.html','ExpOrderCtrl',{
                role:"teacher",date:"1月2日",mission:"材料实验1",room:"力学实验室1",slot:"3,4节",clazz:"02232402",course:"工程力学",status:"已预约"
            },{
                size:'md',keyboard: true,backdrop: 'static',windowClass: 'model-overlay'
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
        $scope.view_exp_details = function(expEntity){
            var entity ={
                id : expEntity.id,
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

            var dialog = dialogs.create('/template/lab-ta-exp-details-dialog.html','TeacherReviewExp',entity,{
                size:'md',keyboard: true,backdrop: 'static',windowClass: 'model-overlay'
            });
            dialog.result.then(function(data){
                //保存教师给学生的打分
                var promise = SystemManageService.teacherUpdateExpStatus(data.data,data.id);
                promise.then(function(data){
                     $scope.refresh_class_stu_list();
                },function(e){
                    Generalservice.inform('未知错误,保存失败');
                });
            },function(e){
                //console.log('');
            });
        };

        $scope.order = function(dayIndex, slotIndex){
            var dialog = dialogs.create('/template/t-order-dialog.html', 'TeacherOrderCtrl', {
                'dayIndex': dayIndex,
                'slotIndex': slotIndex,
                'weekList': $scope.currentWeekList,
                'experimentList': $scope.copiedExperimentList,
                'classList': $scope.copiedClassList,
                'labid':$scope.selectedLab.lab.id,
                'capacity': $scope.selectedLab.lab.capacity
            },{
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });

            dialog.result.then(function(data){
                if(data.result){
                    viewWeekPlan($scope.weekIndex, $scope.selectedLab.lab.id);
                }
            },function(e){
                $scope.toSelectedWeek();
            });
        };

        $scope.order_details = function(slotClassReservation, slotExperiment){

            var copiedSlotClassReservation = {
                approvalStatus: slotClassReservation.approvalStatus,
                classReservationNumber: slotClassReservation.classReservationNumber,
                clazz: {
                    classHour: slotClassReservation.clazz.classHour,
                    classRoom: slotClassReservation.clazz.classRoom
                },
                reserver: {
                    name: slotClassReservation.reserver.name
                }
            };
            var copiedSlotExperiment = {
                experimentName: slotExperiment.experimentName,
                maximumStudent: slotExperiment.maximumStudent
            };
            var dialog = dialogs.create('/template/t-order-details-dialog.html','OrderDetailsCtrl', {
                slotClassReservation: copiedSlotClassReservation,
                slotExperiment: copiedSlotExperiment
            },{
                size:'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });
        }
        
        $scope.cancel_order = function(){
            var dialog = dialogs.create('/template/lab-confirm-dialog.html','LabConfirmCtrl',{
                text:"您确定要取消2014年10月10日3,4节的材料力学实验吗？",cancelBtnText:"否",confirmBtnText:"是"
            },{
                size:'md',keyboard: true,backdrop: 'static',windowClass: 'model-overlay'
            });
        }

    }).controller('ExpDetailsCtrl',function($scope,$modalInstance,data){
        $scope.user = {name : ''};
        $scope.cancel = function(){
            $modalInstance.dismiss('canceled');
        }; // end cancel

        $scope.save = function(){
            $modalInstance.close($scope.user.name);
        }; // end save

        $scope.hitEnter = function(evt){
            if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.name,null) || angular.equals($scope.name,'')))
                $scope.save();
        }; // end hitEnter
    }).controller('TeacherOrderCtrl',function($scope, $modalInstance, 
        data, SystemManageService, Generalservice, DictService){

        $scope.data = data;

        $scope.count = data.capacity;
        $scope.remark = '';
        $scope.currentExperimentList = [];
        $scope.currentClassList = [];
        $scope.selected = {
            selectedExp : null,
            selectedLab : null,
            selectedClazz : null
        };
        $scope.orderDay = $scope.data.weekList[$scope.data.dayIndex - 1].date.Format("yyyy-MM-dd");
        $scope.user = {name : ''};
        loadAvailableClass();
        $scope.data.slotKeyMap = DictService.getSlotKey($scope.data.slotIndex);
        $scope.message = "";
        var getCount =  function(){
            return $scope.count;
        };
        $scope.$watch(getCount, function(newValue, oldValue){
            if(newValue > data.capacity){
                $scope.message = "预约人数超过了实验室容量人数(仍然可以提交预约)";
            }else{
                $scope.message = "";
            }
        });

        
        //获取教师在已选择实验室下可以预定的班级
        function loadAvailableClass(){
            var promise = SystemManageService.getTeacherAvailsCoursesInLab($scope.data.labid);
            promise.then(function(data){
                var data = JSOG.parse(JSOG.stringify(data));
                $scope.currentClassList = wrrapClassList(data.data);
                $scope.selected.selectedClazz = $scope.currentClassList[0];
                $scope.loadAvailsExps($scope.data.labid, $scope.currentClassList[0].id);
            },
            function(e){
                Generalservice.informError('无法获取可选择的班级列表!');
            });
        };
            
        //获取当前班级下在该实验室可以做实验的列表
        $scope.loadAvailsExps = function(){
            var promise = SystemManageService.getAvailableExpByLabClass($scope.data.labid,
                $scope.selected.selectedClazz.id);
            promise.then(function(data){
                var data = JSOG.parse(JSOG.stringify(data));
                $scope.selected.selectedExp = data.data[0];
                $scope.currentExperimentList = data.data;
            },
            function(e){
                Generalservice.informError('无法获取可选择的实验列表列表!');
            });
        };

        function wrrapClassList(clazzs) {
            var wrraperedList = [];
            if(clazzs==null || clazzs==undefined){
                return wrraperedList;
            }
            for (var i = 0; i < clazzs.length; i++) {
                var entity = {
                    "name":clazzs[i].course.courseName + '-' + clazzs[i].classNumber,
                    "id":clazzs[i].id
                };
                wrraperedList.push(entity);
            };
            return wrraperedList;
        }

        $scope.cancel = function(){
            $modalInstance.dismiss('canceled');
        };

        $scope.save = function(){
            var promise = SystemManageService.addReservation($scope.data.labid, $scope.selected.selectedExp.id, 
                $scope.selected.selectedClazz.id, $scope.orderDay, $scope.data.slotKeyMap.slot, $scope.count,
                $scope.remark);
            promise.then(
                function(data) {
                    Generalservice.inform('提交预约成功，等待管理员审核！');
                    $modalInstance.close({result:true});
                },
                function(data) {
                    Generalservice.informError('提交预约失败，请联系管理员！');
                    $modalInstance.close({result:false});
                }
            );
            
        };

        $scope.hitEnter = function(evt){
            if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.name,null) || angular.equals($scope.name,'')))
                $scope.save();
        };
    }).controller('OrderDetailsCtrl',function($scope,$modalInstance,data){
        $scope.user = {name : ''};
        $scope.slotClassReservation = data.slotClassReservation;
        $scope.slotExperiment = data.slotExperiment;

        //console.log($scope.slotClassReservation);
        //console.log($scope.slotExperiment);
        if($scope.slotClassReservation.approvalStatus == 'APPROVED'){
            $scope.is_approve = true;
        }else{
            $scope.is_approve = false;
        }
        
        if(data == 'finish'){
            $scope.is_approve = true;
        }

        $scope.cancel = function(){
            $modalInstance.dismiss('canceled');
        }; // end cancel

        $scope.save = function(){
            $modalInstance.close($scope.user.name);
        }; // end save

        $scope.hitEnter = function(evt){
            if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.name,null) || angular.equals($scope.name,'')))
                $scope.save();
        }; // end hitEnter
    }).controller('LabConfirmCtrl',function($scope,$modalInstance,data){
        $scope.user = {name : ''};
        $scope.data = data;

        $scope.cancel = function(){
            $modalInstance.dismiss('canceled');
        }; // end cancel

        $scope.confirm = function(){
            $modalInstance.close($scope.user.name);
        }; // end save

        $scope.hitEnter = function(evt){
            if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.name,null) || angular.equals($scope.name,'')))
                $scope.save();
        };
    }).controller('TeacherReviewExp',function($scope,$modalInstance,data){
        $scope.data = data;

        $scope.cancel = function(){
            $modalInstance.dismiss('canceled');
        }; // end cancel

        $scope.save = function(){
            var n_entity = {
                'id':$scope.data.id,
                'data':{
                     "experimentRecord": $scope.data.experimentRecord,
                     "experimentComment": $scope.data.experimentComment
                }
            };
            $modalInstance.close(n_entity);
        }; // end save

        $scope.hitEnter = function(evt){
            if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.name,null) || angular.equals($scope.name,'')))
                $scope.save();
        }; // end hitEnter
    });
