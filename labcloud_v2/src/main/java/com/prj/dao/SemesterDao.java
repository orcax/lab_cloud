package com.prj.dao;

import com.prj.entity.Semester;
import com.prj.util.DataWrapper;

public interface SemesterDao extends BaseDao<Semester, Long> {
	
	DataWrapper getCurrentSemester();
	DataWrapper getSlots();
}
