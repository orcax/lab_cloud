'use strict';

describe('Service: generalService', function () {

  // load the service's module
  beforeEach(module('labcloud'));

  // instantiate service
  var generalService;
  beforeEach(inject(function (_generalService_) {
    generalService = _generalService_;
  }));

  it('should do something', function () {
    expect(!!generalService).toBe(true);
  });

});
