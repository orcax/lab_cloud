package com.prj.service;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Course;
import com.prj.entity.Experiment;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

public interface CourseService {
	DataWrapper<Course> addCourse(Course course);

	public DataWrapper<Course> deleteCourseById(Integer id);

	DataWrapper<List<Course>> getAllCourse();

	DataWrapper<List<Course>> getAllActiveCourse();
	
	DataWrapper<Course> getCourseById(int id);

	DataWrapper<Course> updateCourse(int id, Course entity);

	DataWrapper<List<Course>> getCourseByPage(int pagenumber, int pagesize);

	Page<Course> searchCourse(int pagenumber, int pagesize, String name);

	Page<Course> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);
	
	DataWrapper<Course> addExperiment(int courseid, int experimentid, int seq);
	
	DataWrapper<Map<Integer,Experiment>> getExperimentsOfCourse(int courseid);
	
	DataWrapper<Course> deleteExperiment(int courseid, int experimentid);
}
