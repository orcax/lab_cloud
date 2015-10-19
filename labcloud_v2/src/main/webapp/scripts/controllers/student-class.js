'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:StudentClassCtrl
 * @description
 * # StudentClassCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
    .controller('StudentClassCtrl', function($scope, $upload, $localStorage, modalService, qService, Exp, Clazz, Course, StudentRecord, classes, sessionService) {
        $scope.awesomeThings = [
            'HTML5 Boilerplate',
            'AngularJS',
            'Karma'
        ];
        sessionService.storageChecking();

        $scope.courses = [];
        $scope.exps = [];
        $scope.cur_course = null;

        $scope.view_exp = function(course) {
            $scope.exps = [];
            $scope.cur_course = course;
            qService.tokenHttpGet(Course.exps, {
                "id": course.classId
            }).then(function(rc) {
                $scope.exps = rc.data;
            });
        }

        $scope.exp_detail = function(eachExpResult) {
            var expDataList = [];
            var reports = [];
            var photos = [];
            var homeworks = [];

            var txt = [];
            var bigtxt = [];
            var xls = [];

            var pairs = [];
            var single = [];

            qService.tokenHttpGet(StudentRecord.fileList, {
                "recordId": eachExpResult.recordId
            }).then(function(rc) {
                for (var i = 0; i < rc.data.length; i++) {
                    
                    var expData = {
                        "create_time": "2015-05-20",
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
                        '/templates/student/experiment-details-dialog.html',
                        'StExpDetailsModalCtrl', {
                            'experimentName': eachExpResult.expName,
                            'semesterName': $localStorage.semester.name,
                            'courseName': $scope.cur_course.coursename,
                            'studentNumber': $localStorage.currentUser.number,
                            'studentName': $localStorage.currentUser.name,
                            'expDataList': expDataList,
                            'reports': pairs,
                            'xlss':single,
                            'experimentRecord':eachExpResult.experiment_record,
                            'experimentComment':eachExpResult.experiment_comment,
                            "photos":photos,
                            "homeworks":homeworks
                        }
                    );
                // qService.tokenHttpPost(StudentRecord.report, {
                //     "srId": eachExpResult.recordId
                // }, {}).then(function(rc) {
                    
                //     var report = {
                //         "create_time": "2015-05-20",
                //         "path": rc.data
                //     }
                //     reports.push(report);
                //     var dialog = modalService.mdDialog(
                //         '/templates/student/experiment-details-dialog.html',
                //         'StExpDetailsModalCtrl', {
                //             'experimentName': eachExpResult.expName,
                //             'semesterName': $localStorage.semester.name,
                //             'courseName': $scope.cur_course.coursename,
                //             'studentNumber': $localStorage.currentUser.number,
                //             'studentName': $localStorage.currentUser.name,
                //             'expDataList': expDataList,
                //             'reports': reports,
                //             'experimentRecord':eachExpResult.experiment_record,
                //             'experimentComment':eachExpResult.experiment_comment,
                //             "photos":photos,
                //             "homeworks":homeworks
                //         }
                //     );
                // });

            });
        };

        $scope.upload_assignment = function(files, srid, clazzid, expid){
            if(srid == undefined || srid == null){
                modalService.signleConfirmInform("这个实验还没有做，不能上传作业!",
                    "请联系任何教师预约实验，并用实验平台终端完成实验!",'warning',function(){});
                return null;
            }
            if (files && files.length) {
            for (var i = 0; i < files.length; i++) {
                var file = files[i];
                $upload.upload({
                    url: 'api/file/experiment/homework',
                    method: 'POST',
                    headers: sessionService.headers(),
                    data: {
                        stuResId: srid,
                        clazzId: clazzid,
                        expId: expid
                    },
                    file: files
                }).progress(function(evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    ////console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                }).success(function(data, status, headers, config) {
                    //Generalservice.inform('导入成功!');
                    modalService.signleConfirmInform('上传作业成功!','','success',function(){
                    });
                });
            }
          }
        };

        function initClassList() {
            for (var i = 0; i < classes.data.length; i++) {

                var entity = {
                    'id': classes.data[i].course.id,
                    'coursename': classes.data[i].course.name,
                    'classnumber': classes.data[i].number,
                    'teacher': classes.data[i].teacher.name,
                    'classId': classes.data[i].id
                };

                if (i == 0) {
                    entity.active = true;
                } else {
                    entity.active = false;
                }

                $scope.courses.push(entity);

            }

            if ($scope.courses.length > 0) {
                //$scope.view_exp($scope.courses[0].id);
                $scope.cur_course = $scope.courses[0];
                $scope.view_exp($scope.cur_course);
            };
        }

        initClassList();


    });