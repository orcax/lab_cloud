// Karma configuration
// http://karma-runner.github.io/0.12/config/configuration-file.html
// Generated on 2015-04-26 using
// generator-karma 1.0.0

module.exports = function(config) {
  'use strict';

  config.set({
    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,

    // base path, that will be used to resolve files and exclude
    basePath: '',

    // testing framework to use (jasmine/mocha/qunit/...)
    // as well as any additional frameworks (requirejs/chai/sinon/...)
    frameworks: [
      "jasmine"
    ],

    // list of files / patterns to load in the browser
    files: [
      // bower:js
      'bower_components/jquery/dist/jquery.js',
      'bower_components/jsog/lib/JSOG.js',
      'bower_components/angular/angular.js',
      'bower_components/bootstrap/dist/js/bootstrap.js',
      'bower_components/angular-animate/angular-animate.js',
      'bower_components/angular-aria/angular-aria.js',
      'bower_components/angular-cookies/angular-cookies.js',
      'bower_components/angular-messages/angular-messages.js',
      'bower_components/angular-resource/angular-resource.js',
      'bower_components/angular-route/angular-route.js',
      'bower_components/jquery-ui/jquery-ui.js',
      'bower_components/angular-ui-sortable/sortable.js',
      'bower_components/angular-sanitize/angular-sanitize.js',
      'bower_components/angular-touch/angular-touch.js',
      'bower_components/angular-inform/dist/angular-inform.js',
      'bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
      'bower_components/tinymce/tinymce.min.js',
      'bower_components/angular-ui-tinymce/src/tinymce.js',
      'bower_components/angular-translate/angular-translate.js',
      'bower_components/angular-dialog-service/dialogs.min.js',
      'bower_components/angular-dialog-service/dialogs-default-translations.min.js',
      'bower_components/sweetalert/lib/sweet-alert.js',
      'bower_components/angular-sweetalert/SweetAlert.js',
      'bower_components/ngstorage/ngStorage.js',
      'bower_components/autotype/index.js',
      'bower_components/bootstrap-timepicker/js/bootstrap-timepicker.min.js',
      'bower_components/ng-file-upload/angular-file-upload.js',
      'bower_components/ng-file-upload-shim/angular-file-upload-shim.js',
      'bower_components/bootstrap-datepicker/js/bootstrap-datepicker.js',
      'bower_components/textAngular/dist/textAngular.min.js',
      'bower_components/rangy/rangy-core.min.js',
      'bower_components/rangy/rangy-cssclassapplier.min.js',
      'bower_components/rangy/rangy-selectionsaverestore.min.js',
      'bower_components/rangy/rangy-serializer.min.js',
      'bower_components/select2/select2.js',
      'bower_components/angular-ui-select2/src/select2.js',
      'bower_components/angular-ui-date/src/date.js',
      'bower_components/angular-ui-router/release/angular-ui-router.js',
      'bower_components/requirejs/require.js',
      'bower_components/oclazyload/dist/ocLazyLoad.min.js',
      'bower_components/fullcalendar/fullcalendar.js',
      'bower_components/angular-ui-calendar/src/calendar.js',
      'bower_components/moment/moment.js',
      'bower_components/angular-mocks/angular-mocks.js',
      // endbower
      '../app/scripts/**/*.js',
      '../test/mock/**/*.js',
      '../test/spec/**/*.js'
    ],

    // list of files / patterns to exclude
    exclude: [
    ],

    // web server port
    port: 8080,

    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera
    // - Safari (only Mac)
    // - PhantomJS
    // - IE (only Windows)
    browsers: [
      "PhantomJS"
    ],

    // Which plugins to enable
    plugins: [
      "karma-phantomjs-launcher",
      "karma-jasmine"
    ],

    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false,

    colors: true,

    // level of logging
    // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
    logLevel: config.LOG_INFO,

    // Uncomment the following lines if you are using grunt's server to run the tests
    // proxies: {
    //   '/': 'http://localhost:9000/'
    // },
    // URL root prevent conflicts with the site root
    // urlRoot: '_karma_'
  });
};
