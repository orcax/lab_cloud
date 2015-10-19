'use strict';

describe('Controller: TeacherReservationCtrl', function () {

  // load the controller's module
  beforeEach(module('labcloud'));

  var TeacherReservationCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    TeacherReservationCtrl = $controller('TeacherReservationCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
