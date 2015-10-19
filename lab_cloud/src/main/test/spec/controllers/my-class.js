'use strict';

describe('Controller: MyClassCtrl', function () {

  // load the controller's module
  beforeEach(module('prjApp'));

  var MyClassCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MyClassCtrl = $controller('MyClassCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
