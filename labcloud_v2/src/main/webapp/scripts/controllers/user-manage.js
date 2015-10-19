'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:UserManageCtrl
 * @description
 * # UserManageCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('UserManageCtrl', function($scope, modalService,
    Account, qService, generalService, sessionService) {
    sessionService.storageChecking();
    var pageSize = generalService.pageSize();
    $scope.teacherRoles = [];
    //存储本页数据的数据结构
    $scope.accountMap = {
      ADMIN: {
        currRole: {
          key: '管理员',
          value: 'ADMINISTRATOR'
        },
        currList: [],
        listShow: true,
        searchList: [],
        totalItemNum: 0,
        currPage: 1,
        totalPageNum: 0,
        tabActive: true,
        templateUrl: '/templates/manage/admin-form-dialog.html',
        controller: 'StudentModalCtrl',
        searchText: ''
      },
      STUDENT: {
        currRole: {
          key: '学生',
          value: 'STUDENT'
        },
        currList: [],
        listShow: true,
        searchList: [],
        totalItemNum: 0,
        currPage: 1,
        totalPageNum: 0,
        tabActive: false,
        templateUrl: '/templates/manage/student-form-dialog.html',
        controller: 'StudentModalCtrl',
        searchText: ''
      },
      TEACHER: {
        currRole: {
          key: '教师',
          value: 'ALL_TEACHER'
        },
        currList: [],
        listShow: true,
        searchList: [],
        totalItemNum: 0,
        totalPageNum: 0,
        currPage: 1,
        tabActive: false,
        templateUrl: '/templates/manage/teacher-form-dialog.html',
        controller: 'TeacherModalCtrl',
        searchText: ''
      }
    };

    //切换 tab 时候初次load
    $scope.loadUser = function(role) {
      var obj = $scope.accountMap[role];
      if (obj.currList.length == 0) {
        loadList(obj);
      };
    };

    function loadList(obj) {
      var queryMap = {
        "userType": obj.currRole.value,
        "pageSize": pageSize, //config pageSize
        "pageNumber": obj.currPage
      };
      Account.page().get(queryMap,
        function success(rs) {
          obj.totalItemNum = rs.totalItemNum;
          obj.currPage = rs.curPageNum;
          obj.totalPageNum = rs.totalPageNum;
          obj.currList = rs.data;
         
        });
    };

    $scope.pageChanged = function(role){
      console.log(role);
      var obj = $scope.accountMap[role];
      loadList(obj);
    };
    //编辑用户
    $scope.form = function(role, user) {
      if (user == undefined) {
        user = {};
      }
      var dialog = modalService.mdDialog($scope.accountMap[role].templateUrl,
        $scope.accountMap[role].controller, user, modalService);
      dialog.result.then(function(rc) {
        if (!rc.role) {
          rc['role'] = $scope.accountMap[role].currRole.value;
        }
        if (rc.id) {
          qService.tokenHttpPut(Account.account, {
            "id": rc.id
          }, rc).then(function(d) {
            if (d.data) {
              modalService.signleConfirmInform('更新成功', '', 'success', function() {
                //reload
                loadList($scope.accountMap[role]);
              });
            }
          });
        } else {
          qService.tokenHttpPost(Account.create, {}, rc).then(function(d) {
            if (d.data) {
              modalService.signleConfirmInform('添加成功', '', 'success', function() {
                //reload
                loadList($scope.accountMap[role]);
              });
            }
          });
        }
      });
    };
    //删除用户
    $scope.deleteUser = function(user) {
      modalService.deleteConfirmInform(function(rc) {
        qService.tokenHttpDelete(Account.account, {
          id: user.id
        }, {}).then(function(rc) {
          if (rc.data) {
            modalService.signleConfirmInform('删除成功', '', 'success', function() {});
          }
        });
      });
    };
    //修改密码
    $scope.resetPassword = function(user) {
      var dialog = modalService.mdDialog('/templates/common/reset-password-dialog.html',
        'PasswordModalCtrl', {});
      dialog.result.then(function(rc) {
        qService.tokenHttpPut(Account.passwordByAdmin, {
          id: user.id
        }, {
          "newPassword": rc.password
        }).then(function(rc) {
          if (rc.data) {
            modalService.signleConfirmInform('修改密码成功', '', 'success', function() {});
          }
        });
      });
    };

    //搜索功能
    $scope.search = function(role) {
      var text = $scope.accountMap[role].searchText;
      if (text.length > 0) {
        qService.tokenHttpGet(Account.search, {
          "userType": $scope.accountMap[role].currRole.value,
          "value": text
        }).then(function(rc) {
          if(rc.data.length>0){
            $scope.accountMap[role].searchList = rc.data;
            $scope.accountMap[role].listShow = false;
          }else{
            modalService.signleConfirmInform('没有搜索匹配的资源','','warning',function(){});
            $scope.clearSearch(role);
          }
        })
      }
    };

    $scope.clearSearch = function(role){
      $scope.accountMap[role].searchList = [];
      $scope.accountMap[role].listShow = true;
      $scope.accountMap[role].searchText = "";
    };
    
    $scope.loadUser('ADMIN'); //init user of first tab
  });