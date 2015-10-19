'use strict';

/**
 * @ngdoc function
 * @name labcloud.controller:ApplyManageCtrl
 * @description
 * # ApplyManageCtrl
 * Controller of the labcloud
 */
angular.module('labcloud')
  .controller('ApplyManageCtrl', function($scope, modalService, reservations,
    qService, Reservation, sessionService, generalService ,$modal) {
    sessionService.storageChecking();

    var pageSize = generalService.pageSize();
    var semester = sessionService.getCurrSemeter();
    $scope.unVerifyCount = reservations.totalItemNum;
    $scope.filters = [{
      "status": 'PENDING',
      "value": "待审核的"
    }, {
      "status": 'APPROVED',
      "value": "已通过的"
    }];
    $scope.map = {
      reservations: reservations.data,
      status: $scope.filters[0],
      listShow: false,
      currentPage: 1,
      totalPageNum: reservations.totalPageNum,
      totalItemNum: reservations.totalItemNum
    };

    $scope.reload = function() {
      qService.tokenHttpGet(Reservation.allByStatusPage, {
        "semesterId": semester.id,
        "status": $scope.map.status.status,
        "pageSize": pageSize,
        "pageNumber": $scope.map.currentPage 
      }).then(function(rc) {
        $scope.map.reservations = rc.data;
        $scope.map.currentPage = rc.curPageNum;
        $scope.map.totalPageNum = rc.totalPageNum;
        $scope.map.totalItemNum = rc.totalItemNum;
        if($scope.map.status.status == 'PENDING'){
          $scope.unVerifyCount = rc.totalItemNum;
        }
        $scope.map.listShow = false;
      });
    };

    $scope.pageChanged = function(){
      $scope.reload();
    };

    $scope.search = function() {
      $scope.map.listShow = true;
    };

    $scope.changeScope = function() {
      $scope.map.currentPage = 1;
      $scope.map.totalItemNum = 0;
      $scope.reload();
    };
    $scope.form = function(reservation) {
      var dialog = modalService.mdDialog(
        '/templates/common/reservation-details-dialog.html',
        'ReservationDetailModalCtrl', reservation);
    };
    
    $scope.verifyApp = function(entity) {
      var obj = _.clone(entity);
      var dialog = $modal.open({
        templateUrl: '/templates/manage/apply-verify-form-dialog.html',
        controller: 'VerifyModalCtrl',
        resolve: {
          baseData: function(){
            return obj;
          },
          teachers: function(qService, Account) {
            return qService.tokenHttpGet(Account.all, {
              'userType': 'ALL_TEACHER'
            });
          }
        }
      });
      dialog.result.then(function(rc){
        qService.tokenHttpPost(Reservation.verify, {
          "id": entity.id,
          "status": rc.status,
          "teacherIds": rc.teacherIds
        }).then(function(result) {
          modalService.signleConfirmInform("审核成功","","success",function(){
            $scope.reload();
          });
        });
      });
    };

    $scope.rejectApp = function(entity) {
      modalService.doubleConfirmInform('真的要拒绝这个申请么?', '拒绝后将不可重新激活!', 'warning',
        function() {
          qService.tokenHttpPost(Reservation.verify, {
            "id": entity.id,
            "status": "REJECTED",
            "teacherIds": [0]
          }, {}).then(function(rc) {
            $scope.reload();
          });
        });
    };

  });