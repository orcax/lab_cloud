'use strict';

describe('Directive: tabHeading', function () {

  // load the directive's module
  beforeEach(module('prjApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<tab-heading></tab-heading>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the tabHeading directive');
  }));
});
