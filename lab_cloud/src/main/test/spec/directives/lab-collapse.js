'use strict';

describe('Directive: labCollapse', function () {

  // load the directive's module
  beforeEach(module('prjApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<lab-collapse></lab-collapse>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('');
  }));
});
