'use strict'

var app = angular.module('labcloud')
  .config(['$stateProvider','$urlRouterProvider','$locationProvider',
    function($stateProvider,$urlRouterProvider,$locationProvider){

    $locationProvider.html5Mode({
      enabled: false,
      requireBase: false
    });
    $urlRouterProvider
          .otherwise('/login');
    $stateProvider
        .state('login', {
          url: "/login",
          templateUrl: "views/login.html",
          controller: 'LoginController',
          resolve: {
            deps: ['$ocLazyLoad',
              function($ocLazyLoad){
                return $ocLazyLoad.load('oitozero.ngSweetAlert');
            }]
          }
        })
        .state('labmanage',{ //实验室管理
          url: '/labmanage',
          templateUrl: "views/lab-manage.html",
          resolve:{
            labs: function(Lab, qService){
              return qService.tokenHttpGet(Lab.all,{});
            },
            exps: function(Exp, qService){
              return qService.tokenHttpGet(Exp.all,{});
            },
            courses: function(Course, qService){
              return qService.tokenHttpGet(Course.all,{});
            }
          },
          controller: 'LabManageCtrl'
        })
        .state('applymanage',{
          url: '/applymanage',
          templateUrl: "views/apply-manage.html",
          resolve:{
            reservations: function(qService, Reservation, sessionService){
              var semester = sessionService.getCurrSemeter();
              return qService.tokenHttpGet(Reservation.allByStatusPage,{
                semesterId: semester.id,
                status: 'PENDING',
                pageSize: 10,
                pageNumber: 1
              });
            }
          },
          controller: 'ApplyManageCtrl'
        })
        .state('semestermanage',{
          url: '/semestermanage',
          templateUrl: "views/semester-manage.html",
          resolve:{
            
          },
          controller: 'SemesterManageCtrl'
        })
        .state('usermanage',{
          url: '/usermanage',
          templateUrl: "views/user-manage.html",
          controller: 'UserManageCtrl'
        })
        .state('coursemanage',{
          url: '/coursemanage',
          templateUrl: "views/course-manage.html",
          controller: 'CourseManageCtrl'
        })
        .state('teacher-reservation',{
          url: '/teacher/reservation',
          templateUrl: "views/teacher-reservation.html",
          controller: 'TeacherReservationCtrl'
        })
        .state('student-reservation',{
          url: '/student/reservation',
          templateUrl: "views/student-reservation.html",
          controller: 'StudentReservationCtrl'
        })
        .state('teacher-class',{
          url: '/teacher/class',
          templateUrl: "views/teacher-class.html",
          resolve:{
            classes:function(Clazz, qService){
              return qService.tokenHttpGet(Clazz.clazzByTeacher,{});
            }
          },
          controller: 'TeacherClassCtrl'
        })
        .state('student-class',{
          url: '/student/class',
          templateUrl: "views/student-class.html",
          resolve:{
            classes:function(Clazz, qService){
              return qService.tokenHttpGet(Clazz.clazzByStudent,{});
            }
          },
          controller: 'StudentClassCtrl'
        })
        .state('profile',{
          url: '/account/profile',
          templateUrl: "views/profile.html",
          controller: 'ProfileCtrl'
        })
        .state('message',{
          url: '/account/message',
          templateUrl: "views/message.html",
          controller: 'MessageCtrl'
        })
        .state('help',{
          url: '/account/help',
          templateUrl: "views/help.html",
          controller: 'HelpCtrl',
          resolve: {
            deps: ['$ocLazyLoad',
              function($ocLazyLoad) {
                return $ocLazyLoad.load([
                  'styles/blog.css'
                ]);
              }
            ]
          }
        })

        // portal route
        .state('portal', {
          abstract: true,
          url: '/portal',
          templateUrl: 'views/portal/portal.html'
        })
        .state('portal.login', {
          url: '/login',
          templateUrl: 'views/portal/portal-login.html',
          controller: 'PortalLoginCtrl'
        })
        .state('portal.home', {
          url: '/home',
          templateUrl: 'views/portal/portal-home.html',
          controller: 'PortalHomeCtrl'
        })
        .state('portal.reservation', {
          url: '/reservation',
          templateUrl: 'views/portal/portal-reservation.html',
          controller: 'PortalReservationCtrl'
        })
        .state('portal.experiment', {
          url: '/experiment/:id',
          templateUrl: 'views/portal/portal-experiment.html',
          controller: 'PortalExperimentCtrl'
        })
        .state('portal.setting', {
          url: '/setting',
          templateUrl: 'views/portal/portal-setting.html',
          controller: 'PortalSettingCtrl'
        })
        .state('portal.calendar', {
          url: '/calendar',
          templateUrl: 'views/portal/portal-calendar.html',
          controller: 'PortalCalendarCtrl'
        })
        .state('portal.notification', {
          url: '/notification',
          templateUrl: 'views/portal/portal-notification.html',
          controller: 'PortalNotificationCtrl'
        })
        .state('portal.course', {
          url: '/course',
          templateUrl: 'views/portal/portal-course.html',
          controller: 'PortalCourseCtrl'
        })
        .state('portal.course-detail', {
          url: '/course/:id',
          templateUrl: 'views/portal/portal-course-detail.html',
          controller: 'PortalCourseDetailCtrl'
        })
        .state('portal.class', {
          url: '/class',
          templateUrl: 'views/portal/portal-class.html',
          controller: 'PortalClassCtrl'
        })
        .state('portal.class-detail', {
          url: '/class/:id',
          templateUrl: 'views/portal/portal-class-detail.html',
          controller: 'PortalClassDetailCtrl'
        })
        .state('portal.student', {
          url: '/student',
          templateUrl: 'views/portal/portal-student.html',
          controller: 'PortalStudentCtrl'
        });
  }]);




