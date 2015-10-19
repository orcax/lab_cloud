'use strict';

angular.module('labcloud')
  .directive('portalTimepicker', function () {
    return {
      restrict: 'A',
      scope: {
      	time: '='
      },
      link: function postLink(scope, element, attrs) {
        element.timepicker({
		  	showMeridian: false,
		  	minuteStep: 1,
            showInputs: false,
            disableFocus: true
	    });
	    var date = new Date(scope.time);
	    var time = date.getHours() + ':' + date.getMinutes();
	    element.timepicker('setTime', time);
	    element.timepicker().on('changeTime.timepicker', function(e) {
		    date.setHours(e.time.hours);
		    date.setMinutes(e.time.minutes);
		    scope.time = date.getTime();
	    });
      }
    };
  });
