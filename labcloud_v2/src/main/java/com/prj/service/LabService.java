package com.prj.service;

import com.prj.entity.Lab;
import com.prj.util.DataWrapper;

public interface LabService extends BaseService<Lab, Long> {

	DataWrapper listInfoByExp(long experiment);

	
}
