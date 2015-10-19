'use strict';

/**
 * @ngdoc service
 * @name prjApp.SystemManageService
 * @description
 * # SystemManageService
 * Service in the prjApp.
 */
angular.module('prjApp')
    .service('SystemManageService', function SystemManageService($http, $q, $rootScope, $location, $localStorage, $sessionStorage, Generalservice) {
        
        var pageSize = 8;
        //标准 post 请求
        this.generalPost = function(url, data){
            data.token = $sessionStorage.token;
            data.accountCharacter = $sessionStorage.character;
            return Generalservice.generalPost(url, data);
        };

        this.resetPassword = function(userType, uid, newPassword) {
            return Generalservice.generalPost('/Administrator/reset/' + userType + '/' + uid, {
                'token': $rootScope.token,
                'accountCharacter': $rootScope.character,
                'data': {
                    'newPassword': newPassword
                }
            });
        };

        this.loadUser = function(userType, pageNum, teacherType) {
            if (userType == 'Teacher'){
                return Generalservice.generalPost('/' + userType + '/role/' + teacherType+'/page/8/' + pageNum, {
                    'token': $sessionStorage.token,
                    'accountCharacter':$sessionStorage.character
                });
            }else{
                return Generalservice.generalPost('/' + userType + '/page/8/' + pageNum, {
                    'token': $sessionStorage.token,
                    'accountCharacter':$sessionStorage.character
                });
            }
        };

        this.removeUser = function(uid, character) {
            return Generalservice.generalPost('/Teacher/delete/' + uid, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.addTeacher = function(data) {
            data.token = $rootScope.token;
            data.accountCharacter = $rootScope.character;
            return Generalservice.generalPost('/Teacher/add', data);
        };

        this.addAdmin = function(data) {
            data.token = $rootScope.token;
            data.accountCharacter = $rootScope.character;
            return Generalservice.generalPost('/Administrator/add', data);
        };

        this.addStudent = function(data) {
            data.token = $rootScope.token;
            data.accountCharacter = $rootScope.character;
            //console.log(data);
            return Generalservice.generalPost('/Student/add', data);
        };

        this.updateAdmin = function(uid, data) {
            data.token = $rootScope.token;
            data.accountCharacter = $rootScope.character;
            return Generalservice.generalPost('/Administrator/update/' + uid, data);
        };

        this.updateTeacher = function(uid, data) {
            data.token = $rootScope.token;
            data.accountCharacter = $rootScope.character;
            return Generalservice.generalPost('/Teacher/update/' + uid, data);
        };

        this.updateStudent = function(uid, data) {
            data.token = $rootScope.token;
            data.accountCharacter = $rootScope.character;
            return Generalservice.generalPost('/Student/update/' + uid, data);
        };

        this.updatePassword = function(userType, data) {
            return Generalservice.generalPost('/' + userType + '/reset', data);
        };

        this.updateProfile = function(userType, data) {
            return Generalservice.generalPost('/' +  userType + '/update', data);
        };

        this.loadLab = function(pageNum) {
            return Generalservice.generalPost('/Lab/all', {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.removeLab = function(uid) {
            return Generalservice.generalDelete('/Lab/delete/' + uid);
        };

        this.addLab = function(data) {
            data.token = $sessionStorage.token;
            data.accountCharacter = $sessionStorage.character;
            return Generalservice.generalPost('Lab/add', data);
        };

        this.updateLab = function(uid, data) {
            data.token = $sessionStorage.token;
            data.accountCharacter = $sessionStorage.character;
            return Generalservice.generalPost('/Lab/update/' + uid, data);
        };


        this.loadExperiment = function(pageNum) {
            return Generalservice.generalPost('/Experiment/all', {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.removeExperiment = function(uid) {
            return Generalservice.generalDelete('/Experiment/delete/' + uid);
        };

        this.addExperiment = function(data) {
            data.token = $sessionStorage.token;
            data.accountCharacter = $sessionStorage.character;
            return Generalservice.generalPost('/Experiment/add', data);
        };

        this.updateExperiment = function(uid, data) {
            data.token = $sessionStorage.token;
            data.accountCharacter = $sessionStorage.character;
            return Generalservice.generalPost('/Experiment/update/' + uid, data);
        };

        this.loadCourse = function(pageNum) {
            return Generalservice.generalPost('/Course/all', {
                'token': $sessionStorage.token,
                'accountCharacter': 'ADMINISTRATOR'
            });
        };

        this.removeCourse = function(uid) {
            return Generalservice.generalDelete('/Course/delete/' + uid);
        };

        this.addCourse = function(data) {
            data.token = $sessionStorage.token;
            data.accountCharacter = $sessionStorage.character;
            return Generalservice.generalPost('/Course/add', data);
        };

        this.updateCourse = function(uid, data) {
            data.token = $rootScope.token;
            data.accountCharacter = $sessionStorage.character;
            return Generalservice.generalPost('/Course/update/' + uid, data);
        };


        this.removeExperimentOfCourse = function(courseid, experimentid) {
            return Generalservice.generalPost('/Course/' + courseid + '/experiment/delete/' + experimentid, {
                'token': $sessionStorage.token,
                'accountCharacter': 'ADMINISTRATOR'
            });
        };

        this.addExperimentForCourse = function(courseid, experimentid) {
            var seq = 1;
            return Generalservice.generalPost('/Course/' + courseid + '/experiment/add/' + experimentid + '/' + seq, {
                'token': $sessionStorage.token,
                'accountCharacter': 'ADMINISTRATOR'
            });
        };

        this.removeLabForExperiment = function(experimentid, labid) {
            return Generalservice.generalPost('/Experiment/' + experimentid + '/lab/delete/' + labid, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.addLabForExperiment = function(experimentid, labid) {
            return Generalservice.generalPost('/Experiment/' + experimentid + '/lab/add/' + labid, {
                'token': $sessionStorage.token,
                'accountCharacter': 'ADMINISTRATOR'
            });
        };

        this.removeExperimentOfLab = function(labid, experimentid) {
            return Generalservice.generalPost('Lab/' + labid + '/experiment/delete/' + experimentid, {
                'token': $sessionStorage.token,
                'accountCharacter': 'ADMINISTRATOR'
            });
        };

        this.addExperimentForLab = function(labid, experimentid) {
            return Generalservice.generalPost('/Lab/' + labid + '/experiment/add/' + experimentid, {
                'token': $sessionStorage.token,
                'accountCharacter': 'ADMINISTRATOR'
            });
        };

        this.getCurrentSemesterUnderStudentProfile = function() {
            return Generalservice.generalPost('/Semester/profile/current', {
                'token': $sessionStorage.token,
                'accountCharacter': 'STUDENT'
            });
        }

        this.getCurrentSemesterUnderTeacherProfile = function() {
            return Generalservice.generalPost('/Semester/profile/current', {
                'token': $sessionStorage.token,
                'accountCharacter': 'TEACHER'
            });
        }

        this.setCurretSemesterData = function(data) {
            return Generalservice.generalPost('/Semester/add', {
                'token': $sessionStorage.token,
                'accountCharacter': 'ADMINISTRATOR',
                'data': {
                    'semesterName': data.semesterName,
                    'startDate': data.startDate,
                    'endDate': data.endDate,
                    'status': 'CURRENT'
                }
            });
        }

        this.getStudentWeekPlan = function(weekIndex) {
            return Generalservice.generalPost('/Student/weekplan/' + weekIndex, {
                'token': $sessionStorage.token,
                'accountCharacter': 'STUDENT'
            });
        };

        this.loadStudentClasses = function() {
            return Generalservice.generalPost('/Student/class/all', {
                'token': $sessionStorage.token,
                'accountCharacter': 'STUDENT'
            });
        }

        //获取学生所有的预约
        this.getAllReservationsUnderStudentProfile = function() {
            return Generalservice.generalPost('/Student/reservation/all', {
                'token': $sessionStorage.token,
                'accountCharacter': 'STUDENT'
            });
        };

        this.loanCurrentSemester = function() {
            return Generalservice.generalPost('/Semester/profile/current', {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.getCurrentSemester = function(){
            if($sessionStorage.current_semester){
                return $sessionStorage.current_semester;
            }else{
                return null;
            };
        };

        this.loanApplications = function(pageNum) {
            var pageSize = 8;
            return Generalservice.generalPost('/Administrator/reserve/page/all/' + pageSize + '/' + pageNum, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };


        this.loanAllTeachers = function(){
            return Generalservice.generalPost('/Teacher/all', {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.loanAllClasses = function(){
            return Generalservice.generalPost('/Class/all', {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.loanAllCourses = function(){
            return Generalservice.generalPost('/Course/all', {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.getTeacherWeekPlan = function(weekIndex, labid) {
            return Generalservice.generalPost('/Teacher/weekplan/' + weekIndex + '/' + labid, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //获取教师在某实验室下能做的实验的班级列表
        this.getTeacherAvailsCoursesInLab = function(lab_id){
            return Generalservice.generalPost('/Teacher/class/all/lab/'+lab_id,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //获取某个班级在某个实验室下的实验列表
        this.getAvailableExpByLabClass = function(lab_id, class_id){
            var url = '/Teacher/experiment/infoList/class/'+ class_id +'/lab/'+ lab_id;
            if($sessionStorage.character == 'ADMINISTRATOR'){
                url = '/Administrator/experiment/infoList/class/'+ class_id +'/lab/'+ lab_id;
            }
            return Generalservice.generalPost(url,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //获取教师所有预约-分页
        this.getTeacherReservations = function(currentPage, status){
            var pageSize = 8;
            return Generalservice.generalPost('/Teacher/reserve/status/'+status+'/page/'+pageSize+'/'+currentPage,
            {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.loadTeacherClasses = function() {
            return Generalservice.generalPost('/Teacher/class/all', {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //获取教师班级的的实验情况
        this.loadTeacherClassesExps = function(class_id){
            return Generalservice.generalPost('/Class/'+ class_id + '/experiment/statusList',{
                'token' : $sessionStorage.token,
                'accountCharacter' : $sessionStorage.character
            });
        };

        //获取班级中学生的实验状态
        this.loadClassStudentStatus = function(class_id, exp_id, pageNum){
            var pageSize = 8;
            return Generalservice.generalPost('/Class/'+ class_id +'/experiment/'+ exp_id +'/record/page/'+pageSize+'/'+pageNum,{
                'token' : $sessionStorage.token,
                'accountCharacter' : $sessionStorage.character
            });
        };

        //教师给学生打分评价
        this.teacherUpdateExpStatus = function(data, recordId){
            return Generalservice.generalPost('/Teacher/record/update/' + recordId,{
                'token' : $sessionStorage.token,
                'accountCharacter' : $sessionStorage.character,
                'data':data
            });
        };

        //获取学生课程相关实验
        this.studentCourseExps = function(class_id){
            return Generalservice.generalPost('/Student/class/'+ class_id + '/record/list',{
                'token' : $sessionStorage.token,
                'accountCharacter' : $sessionStorage.character
            });
        };

        this.addReservation = function(labid, expid, classid, orderDay, slotNumber, count, remark) {
            var data = {
                "date": orderDay,
                "slot": slotNumber,
                "count": count,
                "remark": remark
            };
            return Generalservice.generalPost('/Teacher/reserve/add/' + labid + '/' + expid + '/' + classid, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character,
                'data': data
            })
        };

        //审核相关API
        this.getAllReservertions = function(pageNum, startDate, endDate){
            var pageSize = 8;
            return Generalservice.generalPost('/Administrator/reserve/page/ALL/'+pageSize+'/'+
                pageNum + '/start/' + startDate + '/end/' + endDate,{
                    'token': $sessionStorage.token,
                    'accountCharacter':$sessionStorage.character
                });
        };

        this.getPeddingReservertions = function(pageNum, startDate, endDate){
            var pageSize = 8;
            return Generalservice.generalPost('/Administrator/reserve/page/PENDING/'+pageSize+'/'+
                pageNum + '/start/' + startDate + '/end/' + endDate,{
                    'token': $sessionStorage.token,
                    'accountCharacter':$sessionStorage.character
                });
        };

        //列出空闲时段的实验教师
        this.getAvailableLabTeacher = function(reservationId){
            return Generalservice.generalPost('/Teacher/reserve/'+ reservationId +'/labTeacher/all',
            {
                'token': $sessionStorage.token,
                'accountCharacter':$sessionStorage.character
            });
        };

        //提交审核结果
        this.submitVerifyResult = function(data){
            return Generalservice.generalPost(' /Administrator/reserve/class/approve/'+data.id,
                {
                    'token': $sessionStorage.token,
                    'accountCharacter':$sessionStorage.character,
                    'data':{
                        "approvalResult": data.result,
                        "labTeaIds": data.labTeachers
                    }
                }
            );
        };

        this.getCurrentSemesterUnderAdminProfile = function() {
            return Generalservice.generalPost('/Semester/profile/current', {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.getAdminWeekPlan = function(weekIndex) {
            return Generalservice.generalPost('/Administrator/weekplan/' + weekIndex, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.getAdminLabWeekPlan = function(weekIndex, labid){
            return Generalservice.generalPost('/Administrator/weekplan/' +
             weekIndex + '/' + labid, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        this.loadClasses = function() {
            return Generalservice.generalPost('/Class/all', {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //管理员获取某个实验室下有实验的教师名单
        this.loadLabAvailableTeacher = function(lab_id){
            return Generalservice.generalPost('/Teacher/list/lab/'+lab_id, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //管理员根据教师和实验室获取可以预约的班级
        this.loadClassByLabTeacher = function(labid,teacherid){
            return Generalservice.generalPost('/Class/lab/'+ labid+'/teacher/'+teacherid, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //管理员提交预约
        this.adminSubmitReservation = function(data){
            return Generalservice.generalPost('/Administrator/reserve/add/'+data.lab.id+
                '/'+data.exp.id+'/'+data.clazz.id, {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character,
                'data':data.realdata
            });
        };

        //管理员关闭一个预约
        this.adminOpenCloseReservation = function(data){
            var url = '/Lab/'+data.lab+'/date/'+
                data.date+'/slot/'+data.slot+'/status/'+data.status;
            return Generalservice.generalPost(url,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //管理员关闭某实验室一学期选定时间段的预约
        this.adminCloseSemeterSlotReservation = function(data){
            var url = '/Lab/'+data.lab+'/slot/'+ data.slot +'/close';
            return Generalservice.generalPost(url,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };
        
        //获取班级学生列表
        this.getClassStudentList = function(clazz_id, pageNum){
            var pageSize = 8;
            return Generalservice.generalPost('/Class/'+clazz_id+'/student/page/'+pageSize+'/'+pageNum,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });

        };

        //获取实验教师负责的预约
        this.getLabTeacherReservation = function(){
            return Generalservice.generalPost('/Teacher/labTeacher/classReservation/all',{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //获取生成实验报告
        this.generateReportTemplate = function(srid){
            return Generalservice.generalPost('/Student/report/generate/studentRecord/'+srid,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //获取实验室可以做的实验列表
        this.getAvailableExpByLab = function(lab_id){
            return Generalservice.generalPost('/Lab/'+lab_id+'/experiment/list',{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });
        };

        //管理员开放学生预约
        this.openStudentReservations = function(slot_data){
            return Generalservice.generalPost('/Administrator/slotReserve/add/lab/'+ slot_data.lab_id
             +'/experiment/'+ slot_data.exp_id,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character,
                "data" : slot_data.data 
            });
        };

        //获取学生可以提交的预约的列表
        this.loadStudentOpenedReservations = function(pageNum){
            return Generalservice.generalPost('Student/slotReserve/available/page/'+ pageSize
                + '/' + pageNum,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });  
        };

        //学生自行选择实验时段
        this.studentGrabReservation = function(slotReservationId){
            return Generalservice.generalPost('Student/slotReserve/add/slotReservation/'+slotReservationId,
            {
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });  
        };

        //管理员查看开放的预约里学生列表
        this.viewOpenedReservationStList = function(slotReservationId, pageNum){
            return Generalservice.generalPost('/Student/slotReserve/'+slotReservationId+
                '/page/'+pageSize + '/' +pageNum,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            }); 
        };

        //学生获取自己抢到的学生预约
        this.studentGetGrabedReservations = function(pageNum){
            var pageSize = 100;
            var pageNum = 1;
            return Generalservice.generalPost('/Student/slotReserve/applied/page/' +
                pageSize + '/' +pageNum,{
                'token': $sessionStorage.token,
                'accountCharacter': $sessionStorage.character
            });   
        }; 

    });