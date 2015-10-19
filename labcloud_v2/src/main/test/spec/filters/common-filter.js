'use strict';

describe('Filter: commonFilter', function () {

  // load the filter's module
  beforeEach(module('labcloud'));

  // initialize a new instance of the filter before each test
  var commonFilter;
  beforeEach(inject(function ($filter) {
    commonFilter = $filter('commonFilter');
  }));

  it('should return the input prefixed with "commonFilter filter:"', function () {
    var text = 'angularjs';
    expect(commonFilter(text)).toBe('commonFilter filter: ' + text);
  });

});
