package com.prj.service;

import com.prj.entity.Semester;
import com.prj.util.DataWrapper;

public interface SemesterService extends BaseService<Semester, Long> {

	DataWrapper setCurrent(long semester);

	DataWrapper list();

	DataWrapper getCurrent();
	
	DataWrapper getSlots();
}
