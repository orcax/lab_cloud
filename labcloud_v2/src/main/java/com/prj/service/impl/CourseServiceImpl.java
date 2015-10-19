package com.prj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prj.dao.ClazzDao;
import com.prj.dao.CourseDao;
import com.prj.entity.Clazz;
import com.prj.entity.Course;
import com.prj.service.CourseService;
import com.prj.util.DataWrapper;

@Service
public class CourseServiceImpl extends BaseServiceImpl<Course, Long> implements
		CourseService {
	CourseDao courseDao;

	@Autowired
	ClazzDao clazzDao;

	@Autowired
	public CourseServiceImpl(CourseDao cd) {
		super(Course.class, cd);
		this.courseDao = cd;
	}

	public DataWrapper addExp(long courseId, long expId) {
		return courseDao.addCourseExp(courseId, expId);
	}

	public DataWrapper create(Course entity) {
		assertUniqueNumber(entity.getNumber());
		return super.createEntity(entity);
	}

	public DataWrapper listExperiments(long classId, long stuId) {
		return courseDao.listExps(classId, stuId);
	}

	public DataWrapper listByStudent(long stuId) {
		List<Clazz> clazzs = clazzDao.getClazzByStudent(stuId);
		List<Course> courses = new ArrayList<Course>();
		for (Clazz clazz : clazzs) {
			courses.add(clazz.getCourse());
		}
		return new DataWrapper(courses);
	}

	public DataWrapper deleteExp(long courseId, long expId) {
		return courseDao.deleteCourseExp(courseId, expId);
	}

	public DataWrapper listCourseExperiments(long courseId) {
		return courseDao.listCourseExps(courseId);
	}

}
