'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:TeacherClassCtrl
 * @description
 * # TeacherClassCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('TeacherClassCtrl', function ($scope, $localStorage, $upload, modalService, qService, Exp, 
    Clazz, StudentRecord, classes, sessionService, generalService, Account, $timeout) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];

    sessionService.storageChecking();

    $scope.classes = [];
    $scope.exps = [];
    $scope.students = {
        data:[],
        totalPageNum: 0,
        curPageNum:1,
        totalItemNum:0,
        isShow: false
    };
    $scope.records = [];

    $scope.cur_class = 0;
    $scope.cur_exp = 0;

    $scope.list_name = 'class';
    $scope.student_totalNumber = 0;
    $scope.cur_student_page = 1;
    $scope.total_student_page = 0;

    $scope.record_totalNumber = 0;
    $scope.cur_record_page = 1;
    $scope.total_record_page = 0;

    var pageSize = generalService.pageSize();

    $scope.view_student = function(class_id, page_num, isRefresh){
        $scope.list_name = 'student';
        var pageNumber = $scope.students.curPageNum;
        if(isRefresh){
            pageNumber = page_num;
        }
        qService.tokenHttpGet(Clazz.studentListByPage, {
            "id": class_id,
            "pageSize": pageSize,
            "pageNumber": pageNumber
        }).then(function(rc){
            $scope.students = rc;
            $scope.students.isShow = true;
            $scope.student_totalNumber = rc.totalItemNum;
            $scope.cur_student_page = rc.curPageNum;
            $scope.total_student_page = rc.totalPageNum;
        });
    };

    $scope.back = function(){
        $scope.list_name = 'class';
    };


    //移除学生
    $scope.removeStudent = function(stId, clazzId){
        qService.tokenHttpDelete(Account.removeClazzStudent,{
            'clazzId': clazzId,
            'studentId': stId,
        }).then(function(rc){
            if(rc.errorCode == 'NO_ERROR'){
                modalService.signleConfirmInform('删除学生成功!','','success',function(){
                $scope.view_student(clazzId, 1, true);
                });
            }else{
                modalService.warningInfrom("导入学生名单失败!","未知错误");
            }
        });
    };

    $scope.clearStudents = function(classId){
         modalService.deleteConfirmInform(function(){
            qService.tokenHttpDelete(Account.clearClazzStudent, {
                'classId': classId
            }).then(function(rc){
                if(rc.errorCode == 'NO_ERROR'){
                   $scope.view_student(classId, 1, true);
                   alert('清除学生成功!');
                }else{
                   alert('清除学生失败!');
                }
            }); 
        });
    };

    //添加学生
    $scope.addStudent = function(clazzId){        
        var dialog = modalService.mdDialog('/templates/teacher/add-class-student-dialog.html',
            'AddStudentToClassCtrl', {});
        dialog.result.then(function(rc){
            qService.tokenHttpPost(Account.addStudentToClazz,{
                'classId': clazzId
            },rc).then(function(rc2){
                if(rc2.errorCode == 'NO_ERROR'){
                    modalService.signleConfirmInform('添加学生成功!','','success',function(){
                    $scope.view_student(clazzId, 1);
                    });
                }else if(rc2.errorCode == 'DUPLICATION'){
                    modalService.warningInfrom("该学生已在班级中!","");
                }else{
                    modalService.warningInfrom("添加学生失败失败!","未知错误");
                }
            });
        });

    };

    $scope.view_exp = function(class_id){
        $scope.cur_class = class_id;
        $scope.list_name = 'class';
        $scope.cur_student_page = 1;
        $scope.student_totalNumber = 0;
        $scope.total_student_page = 0;
        qService.tokenHttpGet(Exp.statusListByClazz, {
            "classId": class_id 
        }).then(function(rc){
            $scope.exps = rc;
        });
    }

    $scope.view_record = function(class_id, exp_id,page_num){
        $scope.list_name = 'exp_detail';
        $scope.cur_exp = exp_id;
        $scope.records = [];
        qService.tokenHttpGet(StudentRecord.recordListByPage,{
            "clazzId": class_id,
            "experimentId": exp_id,
            "pageSize":pageSize,
            "pageNumber":page_num
        }).then(function(rc){
            $scope.records = rc.data;
            $scope.record_totalNumber = rc.totalItemNum;
            $scope.cur_record_page = rc.curPageNum;
            $scope.total_record_page = rc.totalPageNum;
        });
    }

    $scope.page_student = function(){
        //alert($scope.cur_page);
        $scope.view_student($scope.cur_class, $scope.cur_student_page);
    }

    $scope.page_record = function(){
        $scope.view_record($scope.cur_class,$scope.cur_exp,$scope.cur_record_page);
    }

    $scope.uploadStuList = function(files, clazz_id) {
        if (files && files.length) {
            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                $upload.upload({
                    url: 'api/account/list',
                    method: 'POST',
                    headers: sessionService.headers(),
                    data: {
                        class: clazz_id
                    },
                    file: files
                }).progress(function(evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    ////console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                }).success(function(data, status, headers, config) {
                    //Generalservice.inform('导入成功!');
                    if (data.errorCode == "NO_ERROR") {
                        modalService.signleConfirmInform('导入学生名单成功!','成功导入' + data.data.length +'名学生','success',function(){
                            $scope.view_exp($scope.cur_class);
                        });
                    }else{
                        modalService.warningInfrom("导入学生名单失败!","请修改文件格式");
                    }
                }).error(function(data){
                    modalService.warningInfrom("导入学生名单失败!","请修改文件格式");
                });
            }
        }
    }

    $scope.exp_detail = function(record){
        var expDataList = [];
        var reports = [];
        var photos = [];
        var homeworks = [];
        
        var txt = [];
        var bigtxt = [];
        var xls = [];

        var pairs = [];
        var single = [];

        var flag = 0;

        qService.tokenHttpGet(StudentRecord.fileList, {
            "recordId": record.id
        }).then(function(rc){
            for(var i = 0; i< rc.data.length;i++){
                var expData = {
                    "create_time":"2015-05-20",
                    "path": rc.data[i].path
                }
                if(rc.data[i].type=="EXPDATA"){
                    expDataList.push(expData);
                }
                if(rc.data[i].type=="REPORT"){
                    homeworks.push(expData);
                }
                if(rc.data[i].path.split('.')[rc.data[i].path.split('.').length-1]=='jpg'){
                    var photo = {
                        "path":rc.data[i].path
                    }
                    photos.push(photo);
                }
            }
            
            for(var i =0;i<expDataList.length;i++){
                //alert(expDataList[i].path.split('.')[expDataList[i].path.split('.').length-1]);
                if(expDataList[i].path.split('.')[expDataList[i].path.split('.').length-1]=='txt'){
                    txt.push(expDataList[i]);
                }
                else if(expDataList[i].path.split('.')[expDataList[i].path.split('.').length-1]=='TXT'){
                    bigtxt.push(expDataList[i]);
                }
                else if(expDataList[i].path.split('.')[expDataList[i].path.split('.').length-1]=='xls'||expDataList[i].path.split('.')[expDataList[i].path.split('.').length-1]=='xlsx'){
                    xls.push(expDataList[i]);
                }
            }
            
            for(var i = 0; i< txt.length; i++){
                var p = txt[i].path.split('/')[txt[i].path.split('/').length-1];
                var expname = p.split('-')[0];
                for (var j = 0; j < bigtxt.length; j++) {
                    var bigp = bigtxt[j].path.split('/')[bigtxt[j].path.split('/').length-1];
                    var bigexpname = bigp.substring(0,expname.length);
                    
                    if (bigexpname == expname&&(bigp.substring(expname.length,1)!="."||bigp.substring(expname.length,1)!="_"||bigp.substring(expname.length,1)!=" ")) {
                        var pair = {
                            "file1": "http://labcom.tongji.edu.cn/"+bigtxt[j].path,
                            "file2": "http://labcom.tongji.edu.cn/"+txt[i].path,
                            "filename": expname
                        }
                        pairs.push(pair);
                        break;
                    }
                };
            }

            for(var i = 0; i< xls.length; i++){
                var p = xls[i].path.split('/')[xls[i].path.split('/').length-1];
                var expname = p.split('.')[0];
                var s= {
                    "file": "http://labcom.tongji.edu.cn/"+xls[i].path,
                    "filename": expname
                }
                single.push(s);
            }

             var dialog = modalService.mdDialog(
            '/templates/teacher/experiment-details-dialog.html',
            'TeaExpDetailsModalCtrl',{
                'recordId':record.id,
                'experimentName': record.experiment.name,
                'semesterName': $localStorage.semester.name,
                'courseName': record.clazz.course.name,
                'studentNumber': record.student.number,
                'studentName':record.student.name,
                'expDataList': expDataList,
                'reports':pairs,
                'xlss':single,
                'experimentRecord':record.experimentRecord,
                'experimentComment':record.experimentComment,
                'photos':photos,
                'homeworks':homeworks

                    });
            
            // for(var i =0;i<pairs.length;i++){

            //     //alert(pairs[i]["file1"]);
                
            // qService.tokenHttpGet(StudentRecord.lashenreport,{
            //    'filename':pairs[i]['filename'],
            //     'file1':pairs[i]['file1'],
            //     'file2':pairs[i]['file2']
            // },{}).then(function(rc){
            //     alert(rc);
            //     var report = {
            //         "create_time":"2015-05-20",
            //         "path": rc
            //     }
            //     reports.push(report);
            //     flag = 0;
            //     if (i == pairs.length) {
            //         var dialog = modalService.mdDialog(
            // '/templates/teacher/experiment-details-dialog.html',
            // 'TeaExpDetailsModalCtrl',{
            //     'recordId':record.id,
            //     'experimentName': record.experiment.name,
            //     'semesterName': $localStorage.semester.name,
            //     'courseName': record.clazz.course.name,
            //     'studentNumber': record.student.number,
            //     'studentName':record.student.name,
            //     'expDataList': expDataList,
            //     'reports':reports,
            //     'experimentRecord':record.experimentRecord,
            //     'experimentComment':record.experimentComment,
            //     'photos':photos,
            //     'homeworks':homeworks

            //         });
            //     }
            // });

            // }
        })
    }

    function initClassList(){
        for(var i = 0 ; i < classes.data.length; i++){

            var entity = {
                'id': classes.data[i].id,
                'number': classes.data[i].number,
                'course': classes.data[i].course
            };

            if(i == 0){
                entity.active = true;
            }
            else{
                entity.active = false;
            }

            $scope.classes.push(entity);

        }

        if ($scope.classes.length > 0) {
            $scope.view_exp($scope.classes[0].id);
            $scope.cur_class = $scope.classes[0].id;
        };

    }

    initClassList();


  });
