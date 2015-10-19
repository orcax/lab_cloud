'use strict';

describe('Service: semesterService', function () {

  // load the service's module
  beforeEach(module('labcloud'));

  // instantiate service
  var semesterService;
  beforeEach(inject(function (_semesterService_) {
    semesterService = _semesterService_;
  }));

  it('should do something', function () {
    expect(!!semesterService).toBe(true);
  });

});
