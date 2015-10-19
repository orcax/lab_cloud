package com.prj.dao;

import com.prj.entity.Course;
import com.prj.util.DataWrapper;

public interface CourseDao extends BaseDao<Course, Long> {

	DataWrapper listCourseExps(long courseId);

	DataWrapper listExps(long classId, long stuId);

	DataWrapper addCourseExp(long courseId, long expId);

	DataWrapper deleteCourseExp(long courseId, long expId);
}
