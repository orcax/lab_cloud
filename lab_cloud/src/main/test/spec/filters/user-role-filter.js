'use strict';

describe('Filter: userRoleFilter', function () {

  // load the filter's module
  beforeEach(module('prjApp'));

  // initialize a new instance of the filter before each test
  var userRoleFilter;
  beforeEach(inject(function ($filter) {
    userRoleFilter = $filter('userRoleFilter');
  }));

  it('should return the input prefixed with "userRoleFilter filter:"', function () {
    var text = 'angularjs';
    expect(userRoleFilter(text)).toBe('userRoleFilter filter: ' + text);
  });

});
