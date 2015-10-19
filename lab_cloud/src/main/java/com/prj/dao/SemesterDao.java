package com.prj.dao;

import java.util.List;

import com.prj.entity.Semester;

public interface SemesterDao {
	Semester findSemesterById(int id);
	
	Integer addSemester(Semester s);
	
	Semester getLastSemester();

	Semester getCurSemester();

	Semester updateSemester(Semester s);

	List<Semester> getAllSemesters();
}
