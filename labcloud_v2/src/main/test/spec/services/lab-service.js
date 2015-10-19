'use strict';

describe('Service: labService', function () {

  // load the service's module
  beforeEach(module('labcloud'));

  // instantiate service
  var labService;
  beforeEach(inject(function (_labService_) {
    labService = _labService_;
  }));

  it('should do something', function () {
    expect(!!labService).toBe(true);
  });

});
