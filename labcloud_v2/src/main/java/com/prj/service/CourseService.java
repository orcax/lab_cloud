package com.prj.service;

import com.prj.entity.Course;
import com.prj.util.DataWrapper;

public interface CourseService extends BaseService<Course, Long> {

	DataWrapper addExp(long courseId, long expId);

	DataWrapper deleteExp(long courseId, long expId);

	DataWrapper listExperiments(long classId, long stuId);

	DataWrapper listByStudent(long stuId);
	
	DataWrapper listCourseExperiments(long courseId);
}
