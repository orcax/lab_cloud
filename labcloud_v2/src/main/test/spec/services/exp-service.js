'use strict';

describe('Service: expService', function () {

  // load the service's module
  beforeEach(module('labcloud'));

  // instantiate service
  var expService;
  beforeEach(inject(function (_expService_) {
    expService = _expService_;
  }));

  it('should do something', function () {
    expect(!!expService).toBe(true);
  });

});
