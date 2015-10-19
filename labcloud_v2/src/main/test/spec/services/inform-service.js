'use strict';

describe('Service: informService', function () {

  // load the service's module
  beforeEach(module('labcloud'));

  // instantiate service
  var informService;
  beforeEach(inject(function (_informService_) {
    informService = _informService_;
  }));

  it('should do something', function () {
    expect(!!informService).toBe(true);
  });

});
