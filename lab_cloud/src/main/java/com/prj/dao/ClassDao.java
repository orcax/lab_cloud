package com.prj.dao;

import java.util.List;

import com.prj.entity.Class;
import com.prj.entity.ExperimentStatus;
import com.prj.entity.Semester;
import com.prj.util.DataWrapper;

public interface ClassDao {
	Integer addClass(Class c);
	
	Class findClassById(int id);
	
	DataWrapper<List<Class>> getAllClass();
	
	Class deleteClassById(Integer id);
	
	Class updateClass(Class c);

	DataWrapper<List<Class>> getCurSemesterClassByPage(Semester curSemester,
			Integer pageSize, Integer pageNumber);

	Class getClassByNumber(String classNumber);

	Class getClassByComponentIds(int semesterId, int courseId, int teacherId);

	List<Class> getClassesInLabByTeacher(Integer teacherId, Integer expId);

	List<ExperimentStatus> experimentStatusList(Integer classId);

	List<Class> getClassList(Integer labId, Integer teacherId);

	List<Class> getClassesBySE(Integer studentId, Integer expId);
	
}
