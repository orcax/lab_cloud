'use strict';

describe('Controller: StudentClassCtrl', function () {

  // load the controller's module
  beforeEach(module('labcloud'));

  var StudentClassCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    StudentClassCtrl = $controller('StudentClassCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
