'use strict';

describe('Controller: TeacherClassCtrl', function () {

  // load the controller's module
  beforeEach(module('labcloud'));

  var TeacherClassCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TeacherClassCtrl = $controller('TeacherClassCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
