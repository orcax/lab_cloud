'use strict';

/**
 * @ngdoc service
 * @name labcloud.modalService
 * @description
 * # modalService
 * Service in the labcloud.
 */
angular.module('labcloud')
  .service('modalService', function (dialogs, SweetAlert) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    this.mdDialog = function(template, controller, data){
      var dialog = dialogs.create(template, controller, data, {
        size: 'md',
        keyboard: true,
        backdrop: true,
        windowClass: 'model-overlay'
      });
      return dialog;
    };

    this.signleConfirmInform = function(title, text, type, f){
      SweetAlert.swal({
         title: title,
         text: text,
         type: type,
         confirmButtonText: "好的"}, 
      function(){ 
         f();
      });
    };

    this.doubleConfirmInform = function(title, text, type, f){
      SweetAlert.swal({
         title: title,
         text: text,
         type: type,
         showCancelButton: true,
         cancelButtonText: "取消",
         confirmButtonText: "好的"}, 
      function(isConfirm){ 
        if(isConfirm){
         f();
        }
      });
    };

    this.deleteConfirmInform = function(f){
      this.doubleConfirmInform('是否真的删除?','删除后将不可恢复!','warning',f);
    };

    this.warningInfrom = function(title, subTile){
      SweetAlert.swal({
         title: title,
         text: subTile,
         type: 'warning',
         confirmButtonText: "好的"}, 
        function(){ });
    };
  });
