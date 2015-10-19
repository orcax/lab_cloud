'use strict';

describe('Controller: MyExperimentCtrl', function () {

  // load the controller's module
  beforeEach(module('prjApp'));

  var MyExperimentCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MyExperimentCtrl = $controller('MyExperimentCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
