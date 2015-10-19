'use strict';

describe('Controller: CourseManageCtrl', function () {

  // load the controller's module
  beforeEach(module('labcloud'));

  var CourseManageCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CourseManageCtrl = $controller('CourseManageCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
