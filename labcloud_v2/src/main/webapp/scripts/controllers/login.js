'use strict';

/**
 * @ngdoc function
 * @name morningStudioApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the morningStudioApp
 */
angular.module('labcloud')
  .controller('LoginController', function($scope,$localStorage,$http,
    sessionService, tokenFactory, modalService, qService, Semester) {
    $scope.accountCharacter = 'TEACHER';
    $scope.login_name = "";
    $scope.login_password = "";

    //登录方法
    $scope.login = function() {
      $scope.message = "";
      var _n = $scope.login_name;
      var _p = $scope.login_password;
      if (_n == undefined || _n == "" || _p == undefined || _p == "") {
        $scope.message = '用户名/密码不能为空!';
        return;
      }
      tokenFactory.login({
        'X-Username': _n,
        'X-Password': _p
      }).post({},
        function success(data, headers) {
          sessionService.saveToken(data.data, headers()['x-auth-token']);
          $http.get('/api/semester/current',
            {headers:{'x-auth-token':headers()['x-auth-token']}})
          .success(function(rc){
            sessionService.saveCurrSemeter(rc.data);
          }).error(function(error){
            modalService.signleConfirmInform('未知错误发生!','','warning',function(){});
          });
        },
        function error(data) {
          modalService.signleConfirmInform('登录失败','用户名或密码错误','warning',function(){});
        });
    };

    $scope.forgotPassword = function(){
      modalService.signleConfirmInform('请联系管理员','联系电话: 021-65982267 力学实验中心','info', function(){});
    };

  });