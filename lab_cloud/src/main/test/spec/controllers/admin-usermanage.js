'use strict';

describe('Controller: AdminUsermanageCtrl', function () {

  // load the controller's module
  beforeEach(module('prjApp'));

  var AdminUsermanageCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AdminUsermanageCtrl = $controller('AdminUsermanageCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
