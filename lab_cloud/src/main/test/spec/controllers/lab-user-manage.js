'use strict';

describe('Controller: LabUserManageCtrl', function () {

  // load the controller's module
  beforeEach(module('prjApp'));

  var LabUserManageCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    LabUserManageCtrl = $controller('LabUserManageCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
