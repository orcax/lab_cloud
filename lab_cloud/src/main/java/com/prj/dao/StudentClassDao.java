package com.prj.dao;

import java.util.List;

import com.prj.entity.Class;
import com.prj.entity.StudentClass;
import com.prj.util.DataWrapper;

public interface StudentClassDao {
	Integer addStudentClass(StudentClass sc);

	StudentClass findStudentClassById(Integer id);

	StudentClass getStudentClassBySC(Integer studentId, Integer classId);

	List<StudentClass> addStudentClasses(List<StudentClass> list);
	
	DataWrapper<List<StudentClass>> getStudentClassByPage(int pagenumber, int pagesize);
	

	DataWrapper<List<StudentClass>> getStudentByClassByPage(Class c, int pagenumber,int pagesize);
}
