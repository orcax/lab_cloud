'use strict';

describe('Controller: UserManageCtrl', function () {

  // load the controller's module
  beforeEach(module('labcloud'));

  var UserManageCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    UserManageCtrl = $controller('UserManageCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
