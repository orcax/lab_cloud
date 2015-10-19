package com.prj.service;

import java.util.Date;
import java.util.List;

import com.prj.entity.Semester;
import com.prj.util.DataWrapper;

public interface SemesterService {
	DataWrapper<Semester> addSemester(Semester semester, Integer adminId);

	DataWrapper<Semester> getCurSemester();

	DataWrapper<Semester> setCurSemester(Integer id);

	DataWrapper<List<Semester>> getAllSemester();
	
	DataWrapper<List<String>> getDateByWeekIndex(int index);
}
