'use strict';

/**
 * @ngdoc function
 * @name prjApp.controller:LabUserManageCtrl
 * @description
 * # LabUserManageCtrl
 * Controller of the prjApp
 */
angular.module('prjApp')
    .controller('LabUserManageCtrl', function($scope, dialogs, SystemManageService, LoginService, Generalservice, $location) {
        /*
          User Management
        */
        $scope.currentUserList = [];
        $scope.currentAdminList = [];
        $scope.currentStudentList = [];
        $scope.currentTeacherList = [];
        $scope.totalItemNum = '';
        $scope.currPageNum = {currPageNum:''};

        $scope.tabStatus = {
            "tab1":true,
            "tab2":false,
            "tab3":false
        };
        $scope.teacherRoles = [{'role':'所有教师','value':'ALL'},
                               {'role':'普通教师','value':'NORMAL'},
                               {'role':'实验教师','value':'LAB'}];
        $scope.selectedTeacherRole = {'role':$scope.teacherRoles[0]};


        $scope.loadUser =function(userType){
           $scope.currentUserList = [];
           $scope.totalItemNum = '';
           $scope.currPageNum = {currPageNum:''};

           var promise = SystemManageService.loadUser(userType, 1, $scope.selectedTeacherRole.role.value);
            promise.then(
                function(data) {
                     data =  JSOG.parse(JSOG.stringify(data));
                    $scope.totalItemNum = data.totalItemNum;
                    $scope.currPageNum.currPageNum  = data.currPageNum;
                    $scope.currentUserList = data.data;
                },
                function(data) {
                    //alert(data.errorCode);
                    Generalservice.informError('获取用户数据失败！');
                });
        };

        $scope.loadUser('Teacher');

        $scope.pageChanged =function(userType){
            var promise = SystemManageService.loadUser(userType, $scope.currPageNum.currPageNum, $scope.selectedTeacherRole.role.value);
            promise.then(
                function(data) {
                    data =  JSOG.parse(JSOG.stringify(data));
                    $scope.totalItemNum = data.totalItemNum;
                    $scope.currPageNum.currPageNum  = data.currPageNum;
                    $scope.currentUserList = data.data;
                },
                function(data) {
                    Generalservice.informError('获取用户数据失败！');
                });

        };

        $scope.removeUser = function(user,character) {
            var dialog = dialogs.create('/template/lab-confirm-dialog.html', 'ConfirmCtrl', {
                text: '你确定要移除这个用户吗？',
                cancelBtnText: '取消',
                confirmBtnText: '确认'
            }, {
                size: 'sm',
                keyboard: true,
                backdrop: true,
                windowClass: 'model-overlay'
            });
            dialog.result.then(function(data) {
                var promise = SystemManageService.removeUser(user.id,character);
                promise.then(
                    function(data) {
                       if(data.callStatus == "SUCCEED"){
                        Generalservice.inform('删除成功!');
                        $scope.currentUserList.splice($scope.currentUserList.indexOf(user), 1);                           
                    }else{
                        Generalservice.informError('发生错误，删除失败！');
                    }

                    },
                    function(data) {
                        Generalservice.informError('发生错误，删除失败！');
                    });


            }, function() {});
        };

        $scope.addTeacher = function() {
            var dialog = dialogs.create('/template/lab-add-teacher-dialog.html', 'addTeacherCtrl', '', {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });

            dialog.result.then(function(data) {

                var promise = SystemManageService.addTeacher({
                    "token": "",
                    "accountCharacter":"",
                    "data": data
                });
                promise.then(function(data) {
                     if(data.callStatus == "SUCCEED"){
                        Generalservice.inform('添加用户成功!');
                        $scope.loadUser("Teacher");                        
                    } else{
                        Generalservice.informError('添加用户失败!');
                    }                

                    },
                    function(data) {
                        Generalservice.informError('内部错误，请联系管理员!');
                    });

            });
        };

        $scope.addStudent = function() {
            var dialog = dialogs.create('/template/lab-add-student-dialog.html', 'addStudentCtrl', '', {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });

            dialog.result.then(function(data) {

                var promise = SystemManageService.addStudent({
                    "token": "",
                    "accountCharacter":"",
                    "data": data
                });
                promise.then(function(data) {
                     if(data.callStatus == "SUCCEED"){
                        $scope.loadUser("Student");  
                        Generalservice.inform('添加用户成功!');                 
                    } else{
                        Generalservice.informError('添加用户失败!');
                    }      

                    },
                    function(data) {
                        Generalservice.informError('内部错误，请联系管理员!');
                    });

            });
        };

        $scope.addAdministrator = function() {
            var dialog = dialogs.create('/template/lab-add-admin-dialog.html', 'addAdminCtrl', '', {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });

            dialog.result.then(function(data) {

                var promise = SystemManageService.addAdmin({
                    "token": "",
                    "accountCharacter":"",
                    "data": data
                });
                promise.then(function(data) {
                     if(data.callStatus == "SUCCEED"){
                        $scope.loadUser("Administrator");
                        Generalservice.inform('添加用户成功!');                 
                    } else{
                        Generalservice.informError('添加用户失败!');
                    }                       

                    },
                    function(data) {
                        Generalservice.informError('内部错误，请联系管理员!');
                    });

            });
        };

        $scope.adminDetails = function(user) {
            var dialog = dialogs.create('/template/lab-add-admin-dialog.html', 'AdminEditCtrl', user, {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });

            dialog.result.then(function(data) {
                var promise = SystemManageService.updateAdmin(data.id, {
                    "data": data,
                    "token": "",
                    "accountCharacter":""
                });
                promise.then(
                    function(data) {
                    if(data.callStatus == "SUCCEED"){
                        $scope.loadUser("Administrator");
                        Generalservice.inform('更新用户信息成功！');         
                    } else{
                        Generalservice.informError('更新用户信息失败！');
                    }    

                    },
                    function(data) {
                       Generalservice.informError('内部错误，请联系管理员!');
                    }
                );
                //console.log(data);
            });
        };

        $scope.teacherDetails = function(user) {
            var n_user = {
                'name':user.name,
                'number':user.number,
                'password':user.password,
                'gender':user.gender,
                'title':user.title,
                'id':user.id,
                'role':user.role
            };

            var dialog = dialogs.create('/template/lab-add-teacher-dialog.html', 'TeacherEditCtrl', n_user, {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });

            dialog.result.then(function(data) {
                var promise = SystemManageService.updateTeacher(data.id, {
                    "data": data,
                    "token": "",
                    "accountCharacter":""
                });
                promise.then(
                    function(data) {
                     if(data.callStatus == "SUCCEED"){
                        $scope.loadUser("Teacher");
                        Generalservice.inform('更新用户信息成功！');         
                    } else{
                        Generalservice.informError('更新用户信息失败！');
                    }                         

                    },
                    function(data) {
                       Generalservice.informError('内部错误，请联系管理员!');
                    }
                );
                //console.log(data);
            });
        };

        $scope.studentDetails = function(user) {
            var entity = {
                "id":user.id,
                "name":user.name,
                "number":user.number,
                "password":user.password,
                "grade":user.grade,
                "major":user.major,
                "email":user.email
            };
            var dialog = dialogs.create('/template/lab-add-student-dialog.html', 'StudentEditCtrl', entity, {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });

            dialog.result.then(function(data) {
                var promise = SystemManageService.updateStudent(data.id, {
                    "data": data,
                    "token": "",
                    "accountCharacter":""
                });
                promise.then(
                    function(data) {
                     if(data.callStatus == "SUCCEED"){
                        $scope.loadUser("Student");
                        Generalservice.inform('更新用户信息成功！');
                    } else{
                        Generalservice.informError('更新用户信息失败！');
                    }   

                    },
                    function(data) {
                        Generalservice.informError('内部错误，请联系管理员!');
                    }
                );
                //console.log(data);
            });
        };

        $scope.resetPassword = function(user, userType){
            
            var dialog = dialogs.create('/template/lab-admin-reset-user-password-dialog.html', 'AdminResetPasswordCtrl', {'id':user.id, 'name':user.name}, {
                size: 'md',
                keyboard: true,
                backdrop: 'static',
                windowClass: 'model-overlay'
            });
            dialog.result.then(function(data) {
                var promise = SystemManageService.resetPassword(userType,
                    user.id, data.newPassword);
                promise.then(
                    function(data){
                        Generalservice.inform('修改密码成功！');
                    },
                    function(data){
                        Generalservice.informError('内部错误，请联系管理员!');
                    });
            });

        };

        $scope.tabHref = function(path) {
            $location.path(path);
        };
    }).controller('addStudentCtrl', function($log, $scope, $modalInstance, data, DictService) {
        $scope.userRoles = DictService.getRoleDict();
        $scope.showPassword = true;
        $scope.data = {
            number: '',
            initialPassword: '',
            iconPath: '',
            name: '',
            email:'',
            grade:'',
            status:'NEW'
        };
        $scope.cancel = function() {
            $modalInstance.dismiss('canceled');
        }; // end cancel
        $scope.save = function() {
            $modalInstance.close($scope.data);
        }; // end save
        $scope.hitEnter = function(evt) {
            if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
                $scope.save();
        }; // end hitEnter

    }).controller('addAdminCtrl', function($log, $scope, $modalInstance, data, DictService) {
        $scope.userRoles = DictService.getRoleDict();
        $scope.showPassword = true;
        $scope.data = {
            number: '',
            initialPassword: '',
            name: '',
            title:'admin'
        };
        $scope.cancel = function() {
            $modalInstance.dismiss('canceled');
        }; // end cancel
        $scope.save = function() {
            $modalInstance.close($scope.data);
        }; // end save
        $scope.hitEnter = function(evt) {
            if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
                $scope.save();
        }; // end hitEnter

    }).controller('addTeacherCtrl', function($log, $scope, $modalInstance, data, DictService) {
        $scope.userRoles = DictService.getRoleDict();
        $scope.showPassword = true;
        $scope.data = {
            number: '',
            initialPassword: '',
            iconPath: '',
            name: '',
            title:'',
            gender:'0',
            role:'NORMAL'
        };
        $scope.cancel = function() {
            $modalInstance.dismiss('canceled');
        }; // end cancel
        $scope.save = function() {
            $modalInstance.close($scope.data);
        }; // end save
        $scope.hitEnter = function(evt) {
            if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
                $scope.save();
        }; // end hitEnter

    }).controller('ConfirmCtrl', function($scope, $modalInstance, data) {
        $scope.data = data;
        if ($scope.data.cancelBtnText == undefined) {
            $scope.data.cancelBtnText = "取消";
        }
        if ($scope.data.confirmBtnText == undefined) {
            $scope.data.confirmBtnText = "确定";
        }
        $scope.cancel = function() {
            $modalInstance.dismiss('Canceled');
        };
        $scope.confirm = function() {
            $modalInstance.close();
        };
    }).controller('TeacherEditCtrl', function($scope, $modalInstance, data, DictService) {
        if(data.gender == "MALE")
            data.gender = '0';
        else if(data.gender == "FEMALE")
             data.gender = '1';
        $scope.data = data;
        $scope.showPassword = false;
        //$scope.userRoles = DictService.getRoleDict();

        $scope.cancel = function() {
            $modalInstance.dismiss('Canceled');
        };
        $scope.save = function() {
            $modalInstance.close($scope.data);
        };
    }).controller('StudentEditCtrl', function($scope, $modalInstance, data, DictService) {
        $scope.data = data;
        $scope.showPassword = false;
        //$scope.userRoles = DictService.getRoleDict();

        $scope.cancel = function() {
            $modalInstance.dismiss('Canceled');
        };
        $scope.save = function() {
            $modalInstance.close($scope.data);
        };
    }).controller('AdminEditCtrl', function($scope, $modalInstance, data, DictService) {

        $scope.data = data;
        $scope.showPassword = false;
        $scope.cancel = function() {
        $modalInstance.dismiss('Canceled');
        };
        $scope.save = function() {
            $scope.update_data = {};
              //console.log($scope.data);
            
        $scope.update_data.id = $scope.data.id;
        $scope.update_data.name = $scope.data.name;
        $scope.update_data.title = $scope.data.title;
        $scope.update_data.number = $scope.data.number;
        $scope.update_data.iconPath = '';
        $modalInstance.close($scope.update_data);
        };
    }).controller('AdminResetPasswordCtrl', function($scope, $modalInstance, data) {

        $scope.data = data;
        $scope.cancel = function() {
            $modalInstance.dismiss('Canceled');
        };
        $scope.save = function() {
            $modalInstance.close($scope.data);
        };
    });

