'use strict';

/**
 * @ngdoc overview
 * @name morningStudioApp
 * @description
 * # morningStudioApp
 *
 * Main module of the application.
 */
angular
  .module('labcloud', [
    'ngAnimate',
    'ngAria',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ngStorage',
    'ui.router',
    'ui.sortable',
    'ui.bootstrap',
    'ui.date',
    'ui.tinymce',
    'ui.select2',
    'oitozero.ngSweetAlert',
    'angularFileUpload',
    'dialogs.main',
    'ui.bootstrap.datepicker',
    "oc.lazyLoad",
    'ui.calendar',
    // 注释掉这三个才可以使用
    'datePicker',
    //'brantwills.paging',
    'angularMoment'
  ]).constant('angularMomentConfig', {
    timezone: 'Asia/Shanghai' // e.g. 'Europe/London'
  }).config(function ($httpProvider){
    $httpProvider.interceptors.push('loadingHttpInterceptor');
  }).run(function(amMoment){
     amMoment.changeLocale('zh-cn');
  });