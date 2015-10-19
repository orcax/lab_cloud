'use strict';

describe('Service: DictService', function () {

  // load the service's module
  beforeEach(module('prjApp'));

  // instantiate service
  var DictService;
  beforeEach(inject(function (_DictService_) {
    DictService = _DictService_;
  }));

  it('should do something', function () {
    expect(!!DictService).toBe(true);
  });

});
