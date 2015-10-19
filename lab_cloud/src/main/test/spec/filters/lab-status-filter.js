'use strict';

describe('Filter: labStatusFilter', function () {

  // load the filter's module
  beforeEach(module('prjApp'));

  // initialize a new instance of the filter before each test
  var labStatusFilter;
  beforeEach(inject(function ($filter) {
    labStatusFilter = $filter('labStatusFilter');
  }));

  it('should return the input prefixed with "labStatusFilter filter:"', function () {
    var text = 'angularjs';
    expect(labStatusFilter(text)).toBe('labStatusFilter filter: ' + text);
  });

});
