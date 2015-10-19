'use strict';

/**
 * @ngdoc directive
 * @name srcApp.directive:labCollapse
 * @description
 * # labCollapse
 */
angular.module('prjApp')
  .directive('labCollapse', function () {
    return {
      restrict: 'A',
      link: function postLink(scope, element, attrs) {
        var ulElement = element.find('ul');
        element.mouseenter(function(){
          ulElement.stop().show(300);
          var aElement = angular.element(element.children()[0]);
          switch(aElement.text()){
            case '项目和研究':
            case '合作和联络':
            case 'COLLISIONcollective':
              aElement.css('padding-right','55px');
            break;

            case '实验室':
              aElement.css('padding-right','110px');
            break;

            case '活动和新闻':
              aElement.css('padding-right','80px');
            break;

            default:
              aElement.css('padding-right','55px');
          }
        });
        element.mouseleave(function(){
          ulElement.stop().hide(300);
          var aElement = angular.element(element.children()[0]);
          aElement.css('padding-right','15px');
        });
      }
    };
  });
