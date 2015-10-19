'use strict';

describe('Service: Generalservice', function () {

  // load the service's module
  beforeEach(module('prjApp'));

  // instantiate service
  var Generalservice;
  beforeEach(inject(function (_Generalservice_) {
    Generalservice = _Generalservice_;
  }));

  it('should do something', function () {
    expect(!!Generalservice).toBe(true);
  });

});
