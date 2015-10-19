'use strict';

describe('Controller: ApplyManageCtrl', function () {

  // load the controller's module
  beforeEach(module('labcloud'));

  var ApplyManageCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ApplyManageCtrl = $controller('ApplyManageCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
