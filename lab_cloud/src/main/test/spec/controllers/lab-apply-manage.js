'use strict';

describe('Controller: LabApplyManageCtrl', function () {

  // load the controller's module
  beforeEach(module('prjApp'));

  var LabApplyManageCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    LabApplyManageCtrl = $controller('LabApplyManageCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
