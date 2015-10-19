'use strict';

describe('Controller: StudentReservationCtrl', function () {

  // load the controller's module
  beforeEach(module('labcloud'));

  var StudentReservationCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    StudentReservationCtrl = $controller('StudentReservationCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
