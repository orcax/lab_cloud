'use strict';

describe('Service: SystemManageService', function () {

  // load the service's module
  beforeEach(module('prjApp'));

  // instantiate service
  var SystemManageService;
  beforeEach(inject(function (_SystemManageService_) {
    SystemManageService = _SystemManageService_;
  }));

  it('should do something', function () {
    expect(!!SystemManageService).toBe(true);
  });

});
