'use strict';

/**
 * @ngdoc function
 * @name prjApp.controller:AdminUsermanageCtrl
 * @description
 * # AdminUsermanageCtrl
 * Controller of the prjApp
 */
angular.module('prjApp')
  .controller('SystemManageCtrl', function ($scope, dialogs, SystemManageService, Generalservice) {
    


  }).controller('whatsYourNameCtrl',function($scope,$modalInstance,data){
    $scope.user = {name : ''};
    $scope.cancel = function(){
      $modalInstance.dismiss('canceled');
    }; // end cancel

    $scope.save = function(){
      $modalInstance.close($scope.user.name);
    }; // end save

    $scope.hitEnter = function(evt){
      if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.name,null) || angular.equals($scope.name,'')))
        $scope.save();
    }; // end hitEnter
  });
