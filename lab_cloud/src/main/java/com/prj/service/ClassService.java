package com.prj.service;

import java.util.List;

import com.prj.entity.Class;
import com.prj.entity.ExperimentStatus;
import com.prj.entity.Student;
import com.prj.entity.StudentRecord;
import com.prj.util.DataWrapper;

public interface ClassService {
	DataWrapper<Class> addClass(Class c, int semesterId, int courseId, int teacherId);
	
	DataWrapper<List<Class>> getAllClass();
	
	DataWrapper<Class> setTeacher(int classid, int teacherid);
	
	DataWrapper<Class> setCourse(int classid, int courseid);
	
	DataWrapper<Class> setSemester(int classid, int semesterid);
	
	DataWrapper<Class> deleteClassById(int id);
	
	DataWrapper<Class> updateClassById(Integer id, Class clazz);
	
	DataWrapper<List<Student>> addStudents(List<Student> list, Integer classId);
	
	DataWrapper<Class> findClassById(int classId);
	
	DataWrapper<List<Student>> getStudents(int id, int page, int pagesize);

	DataWrapper<List<Class>> getCurSemesterClassByPage(Integer pageSize,
			Integer pageNumber);
	
	DataWrapper<List<Student>> getStudentByClassByPage(int classid, int pagesize, int pagenumber);

	DataWrapper<List<ExperimentStatus>> experimentStatusList(Integer classId);

	DataWrapper<List<StudentRecord>> getStudentRecordsByClassExpInPage(
			Integer classId, Integer expId, Integer pageSize, Integer pageNumber);

	DataWrapper<List<Class>> getClassList(Integer labId, Integer teacherId);
}
