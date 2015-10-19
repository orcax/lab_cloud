'use strict';

/**
 * @ngdoc overview
 * @description
 *
 * Main module of the application.
 */
angular
    .module('prjApp', [
        'ngAnimate',
        'ngCookies',
        'ngResource',
        'ngRoute',
        'ngSanitize',
        'ngTouch',
        'ngStorage',
        'ui.sortable',
        'ui.bootstrap',
        'ui.date',
        'ui.tinymce',
        'ui.select2',
        'angularFileUpload',
        'dialogs.main',
        'ui.bootstrap.datepicker',
        'inform'
    ])
    .config(function ($httpProvider){
        $httpProvider.interceptors.push('loadingHttpInterceptor');
    })
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                redirectTo: '/login'
            })
            .when('/login', {
                templateUrl: 'views/login.html',
                controller: 'LoginCtrl'
            })
            .when('/profile', {
                templateUrl: 'views/profile.html',
                controller: 'ProfileCtrl'
            })
            .when('/register', {
                templateUrl: 'views/register.html',
                controller: 'RegisterCtrl'
            })
            .when('/system-manage', {
              templateUrl: 'views/system-manage.html',
              controller: 'SystemManageCtrl'
            })
            .when('/my-experiment', {
              templateUrl: 'views/my-experiment.html',
              controller: 'MyExperimentCtrl'
            })
            .when('/my-class', {
              templateUrl: 'views/my-class.html',
              controller: 'MyClassCtrl'
            })
            .when('/lab-user-manage', {
              templateUrl: 'views/lab-user-manage.html',
              controller: 'LabUserManageCtrl'
            })
            .when('/lab-manage', {
              templateUrl: 'views/lab-manage.html',
              controller: 'LabManageCtrl'
            })
            .when('/lab-course-arrangement', {
              templateUrl: 'views/lab-course-arrangement.html',
              controller: 'LabCourseArrangementCtrl'
            })
            .when('/lab-apply-manage', {
              templateUrl: 'views/lab-apply-manage.html',
              controller: 'LabApplyManageCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });
    }).run(
      function($sessionStorage, SystemManageService){
        /*
          系统载入时候执行的任务
         */
        //console.log('');
        var semesterPromise = SystemManageService.loanCurrentSemester();
        semesterPromise.then(function(data){
          $sessionStorage.current_semester = data.data;
        });
        // $sessionStorage.current_semester = {
        //   "current":1,
        //   "all_semester":17,
        //   "semesterName":"2014第二学期",
        //   "startDate": "2015-07-01",
        //   "endDate": "2015-07-30"
        // };
      }
    );
