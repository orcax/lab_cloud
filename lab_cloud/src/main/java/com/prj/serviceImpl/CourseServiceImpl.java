package com.prj.serviceImpl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Service;

import com.prj.dao.CourseDao;
import com.prj.dao.ExperimentDao;
import com.prj.entity.Course;
import com.prj.entity.CourseExperiment;
import com.prj.entity.Experiment;
import com.prj.service.CourseService;
import com.prj.util.DataWrapper;
import com.prj.util.ErrorCodeEnum;
import com.prj.util.Page;

@Service("CourseServiceImpl")
public class CourseServiceImpl implements CourseService {

	@Resource(name = "CourseDaoImpl")
	CourseDao courseDao;
	@Resource(name = "ExperimentDaoImpl")
	ExperimentDao experimentdao;

	public DataWrapper<Course> addCourse(Course course) {
		DataWrapper<Course> ret = new DataWrapper<Course>();
		Course a = courseDao.getCourseByNumber(course.getCourseNumber());
		if (a != null) {
			ret.setErrorCode(ErrorCodeEnum.Course_Exist);
		} else if (courseDao.addCourse(course) != null) {
			ret.setData(course);
		} else {
			ret.setErrorCode(ErrorCodeEnum.Unknown_Error);
		}
		return ret;
	}

	public DataWrapper<Course> deleteCourseById(Integer id) {
		Course a = courseDao.deleteCourseById(id);
		DataWrapper<Course> ret = new DataWrapper<Course>(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
		}
		return ret;
	}

	public DataWrapper<List<Course>> getAllCourse() {
		return courseDao.getAllCourse();
	}

	public DataWrapper<List<Course>> getAllActiveCourse() {
		return courseDao.getAllActiveCourse();
	}

	public DataWrapper<Course> getCourseById(int id) {
		DataWrapper<Course> ret = new DataWrapper<Course>();
		Course a = courseDao.findCourseById(id);
		ret.setData(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
		}
		return ret;
	}

	public DataWrapper<Course> updateCourse(int id, Course v) {
		Course a = courseDao.findCourseById(id);
		DataWrapper<Course> ret = new DataWrapper<Course>(a);
		if (a == null) {
			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
		} else {
			a.setCourseName(v.getCourseName());
			a.setCourseNumber(v.getCourseNumber());
			a.setDepartment(v.getDepartment());
			a.setStartYear(v.getStartYear());
			courseDao.updateCourse(a);
		}
		return ret;
	}

	// Methods Following Are Not Checked... YET!
	public DataWrapper<List<Course>> getCourseByPage(int pagenumber, int pagesize) {
		return courseDao.getCourseByPage(pagenumber, pagesize);
	}

	public Page<Course> searchCourse(int pagenumber, int pagesize,
			String name) {
		//return dao.searchCourse(pagenumber, pagesize, name);
		return null;
	}

	public Page<Course> getByPageWithConditions(int pagenumber,
			int pagesize, List<SimpleExpression> list) {
		return courseDao.getByPageWithConditions(pagenumber, pagesize, list);
	}

	//@Transactional
	@Override
	public DataWrapper<Course> addExperiment(int courseid, int experimentid,
			int seq) {
		// TODO Auto-generated method stub
		DataWrapper<Course> ret = new DataWrapper<Course>();
		Experiment experiment = experimentdao.findExperimentById(experimentid);
		if(experiment == null||experiment.getIsActive() == false)
		{
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
			return ret;
		}
		Course course = courseDao.findCourseById(courseid);
		if(course == null||course.getIsActive() == false)
		{
			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
			return ret;
		}
		Set<CourseExperiment> ces = course.getCourseExperiment();
		if(seq<1||seq>ces.size()+1)
		{
			ret.setErrorCode(ErrorCodeEnum.Out_Of_Bound);
			return ret;
		}
		Iterator<CourseExperiment> it = ces.iterator();
		while(it.hasNext())
		{
			CourseExperiment ce = it.next();
			if(ce.getExperiment().getId() == experiment.getId())
			{
				ret.setErrorCode(ErrorCodeEnum.Experiment_Exist);
				return ret;
			}
			if(ce.getSequence()>=seq)
			{
				courseDao.updateExperimentSequence(courseid, ce.getExperiment().getId(), ce.getSequence()+1);
			}
		}
		
		if(!courseDao.addExperiment(courseid, experiment, seq))
			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
		//ret.setData(course);
		return ret;
	}

	@Override
	public DataWrapper<Map<Integer,Experiment>> getExperimentsOfCourse(int courseid) {
		// TODO Auto-generated method stub
		DataWrapper<Map<Integer,Experiment>> ret = new DataWrapper<Map<Integer,Experiment>>();
		Map<Integer,Experiment> list  = courseDao.getExperimentsOfCourse(courseid);
		if(list == null)
			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
		else
		{
			for(Map.Entry<Integer, Experiment> entry : list.entrySet())
			{
				entry.getValue().setLabs(null);
			}
			ret.setData(list);
		}
		return ret;
	}

	@Override
	public DataWrapper<Course> deleteExperiment(int courseid, int experimentid) {
		// TODO Auto-generated method stub
		DataWrapper<Course> ret = new DataWrapper<Course>();
		Experiment experiment = experimentdao.findExperimentById(experimentid);
		if(experiment == null||experiment.getIsActive() == false)
		{
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Exist);
			return ret;
		}
		Course course = courseDao.findCourseById(courseid);
		if(course == null||course.getIsActive() == false)
		{
			ret.setErrorCode(ErrorCodeEnum.Course_Not_Exist);
			return ret;
		}
		Set<CourseExperiment> ces = course.getCourseExperiment();
		Iterator<CourseExperiment> it = ces.iterator();
		int seq = 100000;
		
		while(it.hasNext())
		{
			CourseExperiment ce = it.next();
			if(ce.getExperiment().getId() == experimentid)
			{
				seq = ce.getSequence();
				
			}
		}
		if(seq == 100000)
		{
			ret.setErrorCode(ErrorCodeEnum.Experiment_Not_Included);
			return ret;
		}
		it = ces.iterator();
		while(it.hasNext())
		{
			CourseExperiment ce = it.next();
			
			if(ce.getSequence()>seq)
			{
				course = courseDao.updateExperimentSequence(courseid, ce.getExperiment().getId(), ce.getSequence()-1);
				//ce.setSequence(ce.getSequence()-1);
			}
			
		}
		/*TODO: delete op*/
		courseDao.deleteExperiment(courseid, experiment);
		
		return ret;
	}
}
