'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:ProfileCtrl
 * @description
 * # ProfileCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('ProfileCtrl', function($scope, modalService, $rootScope, $localStorage,
    Account, qService, generalService, sessionService, $upload) {
    sessionService.storageChecking();
    $scope.accountMap = {
      ADMINISTRATOR: {
        templateUrl: '/templates/manage/edit-admin-form-dialog.html',
        controller: 'StudentModalCtrl',
        searchText: ''
      },
      STUDENT: {
        templateUrl: '/templates/manage/edit-student-form-dialog.html',
        controller: 'StudentModalCtrl',
        searchText: ''
      },
      TEACHER: {
        templateUrl: '/templates/manage/edit-teacher-form-dialog.html',
        controller: 'TeacherModalCtrl',
        searchText: ''
      }
    };
    $scope.resetPassword = function() {
      var dialog = modalService.mdDialog('/templates/common/user-reset-password-dialog.html',
        'UserPasswordModalCtrl', {});
      dialog.result.then(function(rc) {
        //取回旧密码的接口
      });
    };

    $scope.profile = function() {
      var dialog = modalService.mdDialog('/templates/common/user-reset-password-dialog.html',
        'UserPasswordModalCtrl', {});
    };

    $scope.editProfile = function() {
      var user = $rootScope.currentUser;
      if (user == undefined) {
        user = {};
      }
      var dialog = modalService.mdDialog($scope.accountMap[user.show_role].templateUrl,
        $scope.accountMap[user.show_role].controller, user, modalService);

      dialog.result.then(function(rc) {
        if (!rc.role) {
          rc['role'] = $scope.accountMap[role].currRole.value;
        }
        qService.tokenHttpPut(Account.account, {
          "id": rc.id
        }, rc).then(function(d) {
          if (d.data) {
            modalService.signleConfirmInform('更新成功', '', 'success', function() {
              var promise = qService.tokenHttpGet(Account.account, {'id':$rootScope.currentUser.id});
              promise.then(function(profile){
                $rootScope.currentUser = wrapperUser(profile.data);
              });
            });
          }
        });
      });
    };

    function wrapperUser(user){
      if(user.role == 'STUDENT' || user.role =='ADMINISTRATOR'){
        user.show_role = user.role;
      }else{
        user.show_role = 'TEACHER';
      }
      return user;
    };

    $scope.upload = function(files) {
      if (files && files.length) {
        for (var i = 0; i < files.length; i++) {
          var file = files[i];
          $upload.upload({
            url: '/api/account/'+ $rootScope.currentUser.id +'/icon',
            headers: {
              'x-auth-token': $rootScope.token
            },
            file: file,
            fileFormDataName: 'file'
          }).progress(function(evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
          }).success(function(data, status, headers, config) {
            //console.log('file ' + config.file.name + 'uploaded. Response: ' + data);
            modalService.signleConfirmInform('头像修改成功!','','success',function(){
              $localStorage.currentUser.iconPath = data.data;
              $rootScope.currentUser.iconPath = data.data;
            });
          });
        }
      }
    };

  });