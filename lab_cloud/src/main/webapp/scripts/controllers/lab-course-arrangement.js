'use strict';

/**
 * @ngdoc function
 * @name prjApp.controller:LabCourseArrangementCtrl
 * @description
 * # LabCourseArrangementCtrl
 * Controller of the prjApp
 */
angular.module('prjApp')
    .controller('LabCourseArrangementCtrl', function($scope, dialogs, $location,
        SystemManageService, Generalservice) {
        $scope.tabHref = function(path) {
            $location.path(path);
        };

        $scope.currentLabList = [];
        $scope.currentDate = new Date();
        $scope.currentWeekList = [];

        $scope.weekConfig = {
            selected: null,
            selectedLab: null
        };
        $scope.reservationResult = [];

        $scope.currentExperimentList = [];
        $scope.currentClassList = [];
        $scope.copiedClassList = [];

        var semesterStartDate;
        var semesterEndDate;
        var weekStartDate;

        loadLabs();
        loadClasses();

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
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        };

        function loadLabs() {
            var promise = SystemManageService.loadLab(1);
            promise.then(
                function(data) {
                    var data = JSOG.parse(JSOG.stringify(data.data));
                    var filteredList = [];
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].status != 'CLOSED') {
                            if (filteredList.length == 0) {
                                data[i].active = true;
                                $scope.weekConfig.selectedLab = data[i];
                            } else {
                                data[i].active = false;
                            }
                            filteredList.push(data[i]);
                        }
                    }
                    $scope.currentLabList = filteredList;
                    loadSemester();
                },
                function(data) {
                    Generalservice.informError('获取实验室列表失败！');
                }
            );
        };

        function loadSemester() {
            var promise = SystemManageService.getCurrentSemesterUnderAdminProfile();
            promise.then(
                function(data) {
                    semesterStartDate = data.data.startDate;
                    semesterEndDate = data.data.endDate;

                    initWeekList();
                    semesterStartDate = semesterStartDate.replace(/-/g, "/");
                    weekStartDate = weekStartDate.Format("yyyy-MM-dd").replace(/-/g, "/");

                    var dateStart = Date.parse(semesterStartDate);
                    var dateEnd = Date.parse(semesterEndDate);
                    var dateCurrent = Date.parse(weekStartDate);
                    var diffDay = Math.ceil((dateCurrent - dateStart) / (24 * 60 * 60 * 1000));
                    $scope.weekNumber = Math.ceil(Math.ceil((dateEnd - dateStart) / (24 * 60 * 60 * 1000)) / 7);

                    if (diffDay < 0) {
                        Generalservice.inform('当前不在本学期内！')
                    } else if ((diffDay % 7) == 0) {
                        $scope.weekIndex = Math.ceil(diffDay / 7) + 1;
                    } else {
                        $scope.weekIndex = Math.ceil(diffDay / 7);
                    }

                    $scope.weeks = [];
                    for (var i = 0; i < $scope.weekNumber; i++) {
                        $scope.weeks.push({
                            'week': i + 1
                        });
                    };
                    $scope.weekConfig.selected = $scope.weeks[$scope.weekIndex - 1];

                    viewWeekPlan($scope.weekIndex);
                },
                function(data) {
                    Generalservice.informError('内部错误，请联系管理员！');
                }
            );
        };

        function loadClasses() {
            var promise = SystemManageService.loadClasses();
            promise.then(
                function(data) {
                    data = JSOG.parse(JSOG.stringify(data));
                    $scope.currentClassList = data.data;
                    for (var i = 0; i < $scope.currentClassList.length; i++) {
                        var courseExperiment = [];
                        for (var j = 0; j < $scope.currentClassList[i].course.courseExperiment.length; j++) {
                            var labs = [];
                            for (var k = 0; k < $scope.currentClassList[i].course.courseExperiment[j].experiment.labs.length; k++) {
                                labs.push($scope.currentClassList[i].course.courseExperiment[j].experiment.labs[k].id);
                            };
                            var temp = {
                                experimentName: $scope.currentClassList[i].course.courseExperiment[j].experiment.experimentName,
                                id: $scope.currentClassList[i].course.courseExperiment[j].experiment.id,
                                experimentNumber: $scope.currentClassList[i].course.courseExperiment[j].experiment.experimentNumber,
                                labs: labs
                            };
                            courseExperiment.push(temp);
                        };
                        var entity = {
                            'id': $scope.currentClassList[i].id,
                            'classNumber': $scope.currentClassList[i].classNumber,
                            'classHour': $scope.currentClassList[i].classHour,
                            'classRoom': $scope.currentClassList[i].classRoom,
                            'courseName': $scope.currentClassList[i].course.courseName,
                            'courseExperiment': courseExperiment
                        };
                        if (i == 0) {
                            entity.active = true;
                        } else {
                            entity.active = false;
                        }
                        $scope.copiedClassList.push(entity);
                    };
                },
                function(data) {
                    Generalservice.informError('内部错误，请联系管理员！');
                }
            );
        }

        $scope.changeLab = function(lab) {
            $scope.weekConfig.selectedLab = lab;
            viewWeekPlan($scope.weekIndex);
        };

        function wrapSlotPlan(entity, slot_offset, dayIndex) {
            var plan = {};
            var keyPrefix = 'slot' + slot_offset;
            plan['status'] = entity[keyPrefix + 'OpenStatus'];
            var d = new Date(entity.date);
            if(d < Generalservice.getCurrentDate() && plan['status']=='OPEN'){
                plan['status'] = 'blank';
            }else{
                plan['experiment'] = entity[keyPrefix + 'Experiment'];
                
                plan['reservation'] = entity[keyPrefix + 'ClassReservation'];
                plan['type'] = entity[keyPrefix + 'Type'];
                plan['slotReservation'] = entity[keyPrefix + 'SlotReservation'];
                plan['slotIndex'] = slot_offset;
                plan['dayIndex'] = dayIndex;
            }
            return plan;
        };

        function viewWeekPlan(weekIndex) {
            $scope.reservationResult = [];
            var promise = SystemManageService.getAdminLabWeekPlan(weekIndex, $scope.weekConfig.selectedLab.id);
            promise.then(function(result) {
                    var data = JSOG.parse(JSOG.stringify(result.data));
                    var date = new Date(result.data[0].date);
                    $scope.currentDate = date;
                    initWeekList();

                    //解析每周的预定列表，组成各个slot的数据结构
                    //2015-03-30 添加slot
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


                    for (var i = 0; i < data.length; i++) {
                        for (var slot_j = 1; slot_j <= $scope.slotList.length; slot_j++) {
                            $scope.slotList[slot_j - 1].list.push(wrapSlotPlan(data[i], slot_j, i + 1));
                        }
                    };
                },
                function(result) {
                    ////console.log(result);
                    Generalservice.informError('内部错误，请联系管理员！');
                });
        }

        function initWeekList() {
            $scope.currentWeekList = [];

            var currentIndex = $scope.currentDate.getDay();
            if (currentIndex == 0)
                currentIndex = 7;

            weekStartDate = new Date($scope.currentDate.getTime() - (currentIndex - 1) * 1000 * 60 * 60 * 24);
            for (var i = 0; i < 7; i++) {
                var date = new Date(weekStartDate.getTime() + i * 1000 * 60 * 60 * 24);
                switch (i) {
                    case 0:
                        $scope.currentWeekList.push({
                            label: '周一',
                            date: date,
                            list: []
                        });
                        break;
                    case 1:
                        $scope.currentWeekList.push({
                            label: '周二',
                            date: date,
                            list: []
                        });
                        break;
                    case 2:
                        $scope.currentWeekList.push({
                            label: '周三',
                            date: date,
                            list: []
                        });
                        break;
                    case 3:
                        $scope.currentWeekList.push({
                            label: '周四',
                            date: date,
                            list: []
                        });
                        break;
                    case 4:
                        $scope.currentWeekList.push({
                            label: '周五',
                            date: date,
                            list: []
                        });
                        break;
                    case 5:
                        $scope.currentWeekList.push({
                            label: '周六',
                            date: date,
                            list: []
                        });
                        break;
                    case 6:
                        $scope.currentWeekList.push({
                            label: '周日',
                            date: date,
                            list: []
                        });
                        break;
                }
            }

            $scope.currentWeekList[currentIndex - 1].date = new Date($scope.currentDate.getTime());
        };

        $scope.lastWeek = function() {
            if ($scope.weekIndex > 1) {
                $scope.weekIndex = $scope.weekIndex - 1;
                $scope.weekConfig.selected = $scope.weeks[$scope.weekIndex - 1];
                viewWeekPlan($scope.weekIndex);
            } else {
                Generalservice.informError("已到本学期最后一周");
            }
        };

        $scope.changeWeek = function(){
            $scope.weekIndex = $scope.weeks.indexOf($scope.weekConfig.selected) + 1;
            viewWeekPlan($scope.weekIndex);
        };


        $scope.nextWeek = function() {
            if ($scope.weekIndex < $scope.weekNumber) {
                $scope.weekIndex = $scope.weekIndex + 1;
                $scope.weekConfig.selected = $scope.weeks[$scope.weekIndex - 1];
                viewWeekPlan($scope.weekIndex);
            } else {
                Generalservice.informError("已到本学期第一周");
            }
        };

        $scope.openCloseReservation = function(entity, flag) {
            var n_entity = {
                slotIndex:entity.slotIndex,
                date : entity.date,
                status: entity.status,
                dayIndex : entity.dayIndex,
                labName : $scope.weekConfig.selectedLab.labName
            };
            if (flag == 0) {
                closeSlotReservation(n_entity);
            } else if (flag == 1) {  
                openSlotReservation(n_entity);
            }
        };

        function getSlotParameters(entity, status){
            var parameters = {};
            parameters.lab = $scope.weekConfig.selectedLab.id;
            parameters.labName = $scope.weekConfig.selectedLab.labName;
            parameters.status = status;
            parameters.date = $scope.currentWeekList[entity.dayIndex - 1].date.Format('yyyy-MM-dd');
            switch (entity.slotIndex) {
                case 1:
                    parameters.slot = 'ONE';
                    break;
                case 2:
                    parameters.slot = 'TWO';
                    break;
                case 3:
                    parameters.slot = 'THREE';
                    break;
                case 4:
                    parameters.slot = 'FOUR';
                    break;
                case 5:
                    parameters.slot = 'FIVE';
                    break;
                case 6:
                    parameters.slot = 'SIX';
                    break;
                case 7:
                    parameters.slot = 'SEVEN';
                    break;
            };
            return parameters;
        }

        function closeSlotReservation(entity){
            var parameters = getSlotParameters(entity, 'CLOSED');
            var dialog = dialogs.create('/template/lab-close-reservation-dialog.html', 'ConfirmCloseReservationCtrl',
                parameters, {
                size: 'md',
                keyboard: true,
                backdrop: true,
                windowClass: 'model-overlay'
            });
            dialog.result.then(function(data) {
                var promise;
                if(data.closeScope == 'one'){
                    promise = SystemManageService.adminOpenCloseReservation(parameters);
                }else{
                    promise = SystemManageService.adminCloseSemeterSlotReservation(parameters);
                }
                
                promise.then(function(data) {
                    Generalservice.inform('修改状态成功！');
                    viewWeekPlan($scope.weekIndex);
                }, function(e) {
                    Generalservice.informError('修改状态失败！');
                });
                
            }, function(e) {
                //console.log('关闭');
            });
        };

        function openSlotReservation(entity){
            var parameters = getSlotParameters(entity, 'OPEN');
            var dialog = dialogs.create('/template/lab-confirm-dialog.html', 'ConfirmCtrl', {
                text: '你确定要开启这个预约吗？',
                cancelBtnText: '取消',
                confirmBtnText: '确认'
            }, {
                size: 'sm',
                keyboard: true,
                backdrop: true,
                windowClass: 'model-overlay'
            });
            dialog.result.then(function(data) {
                var promise = SystemManageService.adminOpenCloseReservation(parameters);
                promise.then(function(data) {
                    Generalservice.inform('修改状态成功！');
                    viewWeekPlan($scope.weekIndex);
                }, function(e) {
                    Generalservice.informError('修改状态失败！');
                });
            }, function(e) {
                //console.log('关闭');
            });
        };

        $scope.order = function(entity) {
            var lab = {
                id: $scope.weekConfig.selectedLab.id,
                name: $scope.weekConfig.selectedLab.labName,
                capacity: $scope.weekConfig.selectedLab.capacity
            };
            var date = $scope.currentWeekList[entity.dayIndex - 1];
            var dialog = dialogs.create('template/lab-admin-reservation-dialog.html', 'AdminOrderCtrl', {
                'date': date,
                'slotIndex': entity.slotIndex,
                'lab': lab
            }, {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });

            dialog.result.then(function(data) {
                var promise = SystemManageService.adminSubmitReservation(data);
                promise.then(function(data) {
                    Generalservice.inform('提交预约成功！');
                    viewWeekPlan($scope.weekIndex);
                }, function(e) {
                    Generalservice.inform('提交预约失败！');
                });
            }, function(e) {
                //Generalservice.informError('');
            });
        };

        $scope.openStReservation = function(entity) {
            var lab = {
                id: $scope.weekConfig.selectedLab.id,
                name: $scope.weekConfig.selectedLab.labName
            };
            var date = $scope.currentWeekList[entity.dayIndex - 1];
            var dialog = dialogs.create('template/lab-add-student-reservation-dialog.html', 'OpenStudentReservationCtrl', {
                'date': date,
                'slotIndex': entity.slotIndex,
                'lab': lab
            }, {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });

            dialog.result.then(function(data) {
                var promise = SystemManageService.openStudentReservations(data);
                promise.then(function(r_data) {
                    var f_data = JSOG.parse(JSOG.stringify(r_data));
                    if (f_data) {
                        Generalservice.inform('开启学生预约预约成功！');
                    }
                    viewWeekPlan($scope.weekIndex);
                }, function(e) {
                    Generalservice.informError('提交预约失败！');
                });

            }, function(e) {

            });
        };

        $scope.viewStudentList = function(entity) {
            var dialog = dialogs.create('/template/lab-class-student-dialog.html', 'OpenedReservationStudentCtrl', {
                slotReservationId: entity.slotReservation.id,
                experimentName : entity.experiment.experimentName
            }, {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });
        };
        //弹出审核窗口
        //获取列表需要的数据
        function wrapperReservation(entity) {
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
            obj['remark'] = entity.remark;
            return obj;
        };

        //弹出审核窗口
        $scope.loadVerifyConsole = function(data) {
            var n_data = wrapperReservation(data);
            var dialog = dialogs.create('/template/lab-apply-verify-dialog.html', 'ApplyVerifyCtrl',
                n_data, {
                    size: 'md',
                    keyboard: true,
                    backdrop: 'static',
                    windowClass: 'model-overlay'
                });
            dialog.result.then(function(data) {
                    //提交审核结果
                    var promise = SystemManageService.submitVerifyResult(data);
                    promise.then(function(data) {
                            Generalservice.inform('审核结果提交成功！');
                            viewWeekPlan($scope.weekIndex);
                        },
                        function(e) {
                            Generalservice.informError('内部错误，请联系管理员');
                        });
                },
                function(e) {
                    //Generalservice.informError('内部错误，请联系管理员');
                }
            );
        }

    }).controller('AdminOrderCtrl', function($scope, $modalInstance, data,
        SystemManageService, Generalservice, DictService) {
        /*
            管理员帮助教师提交预约申请 - 直接进入同意状态
         */
        $scope.data = data;
        $scope.message = "";
        $scope.data.date = $scope.data.date.date.Format('yyyy-MM-dd');
        $scope.selected = { //储存当前选择的列表
            teacher: null,
            clazz: null,
            exp: null
        };
        $scope.data.count = data.lab.capacity;

        var getCount =  function(){
            return $scope.data.count;
        };
        $scope.$watch(getCount, function(newValue, oldValue){
            if(newValue > data.lab.capacity){
                $scope.message = "预约人数超过了实验室容量人数(仍然可以提交预约)";
            }else{
                $scope.message = "";
            }
        });

        //1. 获取教师列表
        var promise = SystemManageService.loadLabAvailableTeacher($scope.data.lab.id);
        promise.then(function(data) {
            var data = JSOG.parse(JSOG.stringify(data.data));
            var tList = [];
            var idList = [];
            for (var i = 0; i < data.length; i++) {
                var user = {
                    id: data[i].id,
                    name: data[i].name
                };
                if (idList.indexOf(data[i].id) < 0) {
                    tList.push(user);
                    idList.push(user.id);
                }
            };
            $scope.teacherList = tList;
            $scope.selected.teacher = tList[0];
            $scope.getClazzList();

        }, function(e) {
            Generalservice.informError('获取教师列表失败!');
        });

        //2. 获取班级列表
        $scope.getClazzList = function() {
            var promise = SystemManageService.loadClassByLabTeacher($scope.data.lab.id,
                $scope.selected.teacher.id);
            promise.then(function(data) {
                var data = JSOG.parse(JSOG.stringify(data.data));
                var clazzList = [];
                var idList = [];
                for (var i = 0; i < data.length; i++) {
                    var entity = {
                        id: data[i].id,
                        name: data[i].teacher.name + '-' + data[i].classHour + '-' + data[i].course.courseName
                    };
                    if (idList.indexOf(data[i].id) < 0) {
                        clazzList.push(entity);
                        idList.push(data[i].id);
                    }
                };
                $scope.clazzList = clazzList;
                $scope.selected.clazz = clazzList[0];
                $scope.getExpList();
            }, function(e) {
                Generalservice.informError('获取该教师班级列表失败!');
            });
        };

        //3. 获取实验列表
        $scope.getExpList = function() {
            var promise = SystemManageService.getAvailableExpByLabClass($scope.data.lab.id,
                $scope.selected.clazz.id);
            promise.then(function(data) {
                var data = JSOG.parse(JSOG.stringify(data.data));
                var expList = [];
                for (var i = 0; i < data.length; i++) {
                    var entity = {
                        id: data[i].id,
                        name: data[i].strValue
                    };
                    expList.push(entity);
                };
                $scope.expList = expList;
                $scope.selected.exp = expList[0];
            }, function(e) {
                Generalservice.informError('获取实验列表失败!');
            });
        };
        var slotKeyMap = DictService.getSlotKey($scope.data.slotIndex);
        $scope.data.slot = slotKeyMap.slot;
        $scope.slotExpression = slotKeyMap.value;

        $scope.cancel = function() {
            $modalInstance.dismiss('canceled');
        };
        $scope.save = function() {
            var n_data = $scope.selected;
            n_data.lab = $scope.data.lab;
            n_data.realdata = {
                count: $scope.data.count,
                date: $scope.data.date,
                slot: $scope.data.slot,
                remark: $scope.data.remark
            }
            $modalInstance.close(n_data);
        };
    }).controller('OpenStudentReservationCtrl', function($scope, $modalInstance, data,
        SystemManageService, Generalservice, DictService) {
        $scope.data = data;
        $scope.data.date = $scope.data.date.date.Format('yyyy-MM-dd');
        $scope.selectedItem = {
            experiment: ''
        };
        $scope.openNumber = 0;
        $scope.remark = '';
        var explist_promise = SystemManageService.getAvailableExpByLab(data.lab.id);
        explist_promise.then(function(data) {
            var expPromiseResult = JSOG.parse(JSOG.stringify(data));
            $scope.expListResults = expPromiseResult.data;
            if ($scope.expListResults.length > 0) {
                $scope.selectedItem.experiment = $scope.expListResults[0];
            } else {
                Generalservice.informError('获取实验列表失败，无法开放预约！');
            }
        }, function(e) {
            Generalservice.informError('获取实验列表失败，无法开放预约！');
        });

        var slotKeyMap = DictService.getSlotKey($scope.data.slotIndex);
        $scope.data.slot = slotKeyMap.slot;
        $scope.slotExpression = slotKeyMap.value;


        $scope.cancel = function() {
            $modalInstance.dismiss('canceled');
        };
        $scope.save = function() {
            var slot_data = {
                "data": {
                    "date": $scope.data.date,
                    "slot": $scope.data.slot,
                    "max": $scope.openNumber
                },
                "lab_id": $scope.data.lab.id,
                "exp_id": $scope.selectedItem.experiment.id
            };

            $modalInstance.close(slot_data);
        };
    }).controller('OpenedReservationStudentCtrl', function($scope, $modalInstance, data,
        SystemManageService, Generalservice) {
        $scope.data = data;
        $scope.config = {
            currentPage: 1,
            allCount: 0
        };
        $scope.studentList = [];

        $scope.loadList = function() {
            var promise = SystemManageService.viewOpenedReservationStList($scope.data.slotReservationId,
                $scope.config.currentPage);
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
    }).controller('ConfirmCloseReservationCtrl', function($scope, $modalInstance, data,
        Generalservice){
        $scope.data = data;
        $scope.closeScopes = [
            {
                key : '只是该时段',
                value : 'one'
            },
            {
                key: '本学期所有该时段',
                value : 'all'
            }
        ];
        $scope.selectedItem = {
            closeScope: $scope.closeScopes[0]
        };
        

        $scope.cancel = function() {
            $modalInstance.dismiss('canceled');
        };
        $scope.save = function() {
            $scope.data.closeScope = $scope.selectedItem.closeScope.value;
            $modalInstance.close($scope.data);
        };
    });