'use strict';

describe('Service: clazzService', function () {

  // load the service's module
  beforeEach(module('labcloud'));

  // instantiate service
  var clazzService;
  beforeEach(inject(function (_clazzService_) {
    clazzService = _clazzService_;
  }));

  it('should do something', function () {
    expect(!!clazzService).toBe(true);
  });

});
