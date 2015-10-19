'use strict';

/**
 * @ngdoc function
 * @name prjApp.controller:ProfileCtrl
 * @description
 * # ProfileCtrl
 * Controller of the prjApp
 */
angular.module('prjApp')
    .controller('ProfileCtrl', function($scope, SystemManageService,
        dialogs, $sessionStorage, $rootScope, LoginService, $upload, Generalservice) {
        
        $scope.passItem = {
            originPass: '',
            newPass: '',
            retypeNewPass: ''
        };

        $scope.$watch('files', function () {
            $scope.upload($scope.files);
        });

        $scope.upload = function(files) {
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    $upload.upload({
                        url: '/Account/upload/icon',
                        data: {
                            'token': $scope.token,
                            'accountCharacter':$rootScope.character
                        },
                        file: file,
                        fileFormDataName: 'data'
                    }).progress(function(evt) {
                        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        ////console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
                    }).success(function(data, status, headers, config) {
                        //console.log('file ' + config.file.name + 'uploaded. Response: ' + data);
                        Generalservice.inform('头像修改成功!');
                        $sessionStorage.loginUser.iconPath = data.data;
                    });
                }
            }
        };

        $scope.userInfo = {};

        $scope.editProfile = function() {
            var character = $rootScope.character;
            var userType;
            if (character == "ADMINISTRATOR") {
                userType = "Administrator";
                var dialog = dialogs.create('/template/lab-admin-edit-dialog.html', 'EditAdminProfileCtrl', {
                    size: 'md',
                    keyboard: true,
                    backdrop: 'static',
                    windowClass: 'model-overlay'
                });
            } else if (character == "TEACHER") {
                userType = "Teacher";
                var dialog = dialogs.create('/template/lab-teacher-edit-dialog.html', 'EditTeacherProfileCtrl', {
                    size: 'md',
                    keyboard: true,
                    backdrop: 'static',
                    windowClass: 'model-overlay'
                });
            } else if (character == "STUDENT") {
                userType = "Student";
                var dialog = dialogs.create('/template/lab-student-edit-dialog.html', 'EditStudentProfileCtrl', {
                    size: 'md',
                    keyboard: true,
                    backdrop: 'static',
                    windowClass: 'model-overlay'
                });

            }
            dialog.result.then(function(data) {
                var promise = SystemManageService.updateProfile(userType, {
                    "data": data,
                    "token": $rootScope.token,
                    "accountCharacter": $rootScope.character
                });
                promise.then(
                    function(data) {
                        if (data.callStatus == "SUCCEED") {
                            Generalservice.inform('更新用户信息成功！');
                            LoginService.persitentLogin(data.data, $rootScope.token, $rootScope.character);
                        } else {
                            Generalservice.informError('内部错误，更新失败！');
                        }
                    },
                    function(data) {
                        Generalservice.informError('内部错误，更新失败！');
                    }
                );
            });
        };

        $scope.resetPasswrod = function() {
            var dialog = dialogs.create('/template/lab-reset-password-dialog.html', 'ResetPasswordCtrl',
                $scope.passItem, {
                    size: 'md',
                    keyboard: true,
                    backdrop: 'static',
                    windowClass: 'model-overlay'
                });
            dialog.result.then(function(data) {
                $scope.update_pwd = {
                    oldPassword: '',
                    newPassword: ''
                };
                $scope.userType = '';
                $scope.update_pwd.oldPassword = data.originPass;
                $scope.update_pwd.newPassword = data.newPass;
                if ($rootScope.character == "STUDENT") {
                    $scope.userType = "Student";
                } else if ($rootScope.character == "TEACHER") {
                    $scope.userType = "Teacher";
                } else if ($rootScope.character == "ADMINISTRATOR") {
                    $scope.userType = "Administrator";
                }

                var promise = SystemManageService.updatePassword($scope.userType, {
                    "data": $scope.update_pwd,
                    "token": $rootScope.token,
                    "accountCharacter": $rootScope.character
                });
                promise.then(
                    function(data) {
                        if (data.callStatus == "SUCCEED") {
                            Generalservice.inform('更新密码成功,请用新密码重新登陆!');
                            LoginService.logout();
                            LoginService.clearSessionStorage();
                        } else if (data.errorCode == "Password_Wrong") {
                            Generalservice.informError('原密码错误,更新失败！');
                        } else {
                            Generalservice.informError('内部错误，更新失败！');
                        }

                    },
                    function(data) {
                         Generalservice.informError('内部错误，更新失败！');
                    }
                );
            });
        };
    }).controller('EditStudentProfileCtrl', function($scope, $modalInstance, $sessionStorage) {
        $scope.data = {
            name: '',
            email: '',
            grade: '',
            major: '',
            number: ''
        };
        $scope.data.name = $sessionStorage.loginUser.name;
        $scope.data.email = $sessionStorage.loginUser.email;
        $scope.data.grade = $sessionStorage.loginUser.grade;
        $scope.data.major = $sessionStorage.loginUser.major;
        $scope.data.number = $sessionStorage.loginUser.number;
        $scope.cancel = function() {
            $modalInstance.dismiss('Canceled');
        };
        $scope.save = function() {
            $modalInstance.close($scope.data);
        };
    }).controller('EditTeacherProfileCtrl', function($scope, $modalInstance, $sessionStorage) {
        $scope.data = {
            name: '',
            title: 'teacher',
            gender: '',
            number: ''
        };
        if ($sessionStorage.loginUser.gender == 'MALE') {
            $scope.data.gender = 0;
        } else {
            $scope.data.gender = 1;
        }
        $scope.data.name = $sessionStorage.loginUser.name;
        $scope.data.number = $sessionStorage.loginUser.number;
        $scope.cancel = function() {
            $modalInstance.dismiss('Canceled');
        };
        $scope.save = function() {
            $modalInstance.close($scope.data);
        };
    }).controller('EditAdminProfileCtrl', function($scope, $modalInstance, $sessionStorage) {
        $scope.data = {
            name: '',
            title: 'admin',
            number: ''
        };
        $scope.data.name = $sessionStorage.loginUser.name;
        $scope.data.number = $sessionStorage.loginUser.number;
        $scope.cancel = function() {
            $modalInstance.dismiss('Canceled');
        };
        $scope.save = function() {
            $modalInstance.close($scope.data);
        };
    }).controller('ResetPasswordCtrl', function($log, $scope, $modalInstance, data) {
        $scope.data = data;
        $scope.canSave = false;
        $scope.cancel = function() {
            $modalInstance.dismiss('canceled');
        }; // end cancel
        $scope.$watch('data.retypeNewPass', function(val, old) {
            if (val != $scope.data.newPass) {
                $scope.message = "两次输入的密码不一致";
            } else {
                clearMessage();
            }
        });
        $scope.$watch('data.newPass', function(val, old) {
            if ($scope.data.retypeNewPass.length > 0 && val != $scope.data.retypeNewPass) {
                $scope.message = "两次输入的密码不一致";
            } else {
                clearMessage();
            }
        });

        function clearMessage() {
            $scope.message = "";
        };
        $scope.save = function() {
            //alert($scope.data.newPass);
            if ($scope.message.lenth == 0) {
                $scope.canSave = true;
            }
            $modalInstance.close($scope.data);
        }; // end save

        $scope.hitEnter = function(evt) {
            if (angular.equals(evt.keyCode, 13) && !(angular.equals($scope.name, null) || angular.equals($scope.name, '')))
                $scope.save();
        }; // end hitEnter
    });