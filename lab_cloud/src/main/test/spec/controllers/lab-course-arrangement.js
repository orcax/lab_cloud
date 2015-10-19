'use strict';

describe('Controller: LabCourseArrangementCtrl', function () {

  // load the controller's module
  beforeEach(module('prjApp'));

  var LabCourseArrangementCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    LabCourseArrangementCtrl = $controller('LabCourseArrangementCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
