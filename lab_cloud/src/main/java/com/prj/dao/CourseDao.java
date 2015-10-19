package com.prj.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Course;
import com.prj.entity.Experiment;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

public interface CourseDao {
	Integer addCourse(Course v);

	Course deleteCourseById(Integer id);

	Course findCourseById(int id);

	DataWrapper<List<Course>> getAllCourse();

	DataWrapper<List<Course>> getAllActiveCourse();

	Course updateCourse(Course v);

	Course getCourseByNumber(String number);

	DataWrapper<List<Course>> getCourseByPage(int pagenumber, int pagesize);

	List<Course> getByCondition(List<SimpleExpression> list);

	Page<Course> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);
	
	boolean addExperiment(int courseid, Experiment e, int seq);
	
	Course updateExperimentSequence(int courseid, int experimentid, int seq);
	
	Map<Integer,Experiment> getExperimentsOfCourse(int courseid);
	
	boolean deleteExperiment(int courseid, Experiment e);
}
