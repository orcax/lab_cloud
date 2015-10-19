package com.prj.daoImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.CourseDao;
import com.prj.entity.Course;
import com.prj.entity.CourseExperiment;
import com.prj.entity.Experiment;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

@Service("CourseDaoImpl")
public class CourseDaoImpl extends AbstractHibernateDao<Course, Integer>implements CourseDao {

	protected CourseDaoImpl() {
		super(Course.class);
	}

	public Integer addCourse(Course v) {
		return add(v);
	}

	public Course deleteCourseById(Integer id) {
		Course a = findById(id);
		if (a == null)
			return null;
		a.setIsActive(false);
		return a;
	}

	@SuppressWarnings("unchecked")
	public DataWrapper<List<Course>> getAllCourse() {
		List<Course> result = getCurrentSession().createCriteria(Course.class)
				.addOrder(Order.asc("courseNumber"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Course>> ret = new DataWrapper<List<Course>>();
		ret.setData(result);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public DataWrapper<List<Course>> getAllActiveCourse() {
		List<Course> result = getCurrentSession().createCriteria(Course.class)
				.add(Restrictions.eq("isActive", true))
				.addOrder(Order.asc("courseNumber"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Course>> ret = new DataWrapper<List<Course>>();
		ret.setData(result);
		return ret;
	}

	public Course findCourseById(int id) {
		return findById(id);
	}

	public Course updateCourse(Course v) {
		return saveOrUpdate(v);
	}

	public Course getCourseByNumber(String number) {
		Criteria criteria = getCurrentSession().createCriteria(Course.class);
		criteria.add(Restrictions.eq("courseNumber", number));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Course)ret.get(0);
		}
		return null;
	}

	public DataWrapper<List<Course>> getCourseByPage(int pagenumber, int pagesize) {
//		// TODO Auto-generated method stub
//		Criteria criteria = createCriteria()
//				//.addOrder(Order.desc("modify_time"))
//				.add(Restrictions.eq("isActive", true));
//		PageResult<Course> pr = findByCriteriaByPage(criteria, pagenumber, pagesize);
//		DataWrapper<List<Course>> ret = new DataWrapper<List<Course>>();
//		if (pr.getCurrPageNum() > pr.getTotalPageNum()) {
//			ret.setErrorCode(ErrorCodeEnum.Page_Number_Invaild);
//		} else {
//			ret.setData(pr.getData());
//		}
//		ret.setCurrPageNum(pr.getCurrPageNum());
//		ret.setNumPerPage(pr.getNumPerPage());
//		ret.setTotalItemNum(pr.getTotalItemNum());
//		ret.setTotalPageNum(pr.getTotalPageNum());
//		return ret;
		return findBySQLByPage("where isActive=true order by modify_time desc", pagenumber, pagesize);
	}

	public List<Course> getByCondition(List<SimpleExpression> list) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<Course> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addExperiment(int courseid, Experiment e, int seq) {
		// TODO Auto-generated method stub
		Course course = findById(courseid);
		if(course == null||course.getIsActive() == false)
			return false;
		Set<CourseExperiment> ces= course.getCourseExperiment();
		CourseExperiment ce = new CourseExperiment();
		ce.setCourse(course);
		Experiment exp = (Experiment)this.getCurrentSession().load(Experiment.class, e.getId());
		if(exp !=null)
			ce.setExperiment(exp);
		else
			ce.setExperiment(e);
		ce.setSequence(seq);
		ces.add(ce);
		return true;
	}

	@Override
	public Course updateExperimentSequence(int courseid, int experimentid, int seq) {
		// TODO Auto-generated method stub
		Course course = findById(courseid);
		if(course == null||course.getIsActive() == false)
			return null;
		Set<CourseExperiment> ces= course.getCourseExperiment();
		Iterator<CourseExperiment> i = ces.iterator();
		while ( i.hasNext()) {
			
			CourseExperiment ce = i.next();
			if(ce.getExperiment().getId()==experimentid)
				ce.setSequence(seq);
			
		}
		return course;
	}

	@Override
	public Map<Integer,Experiment> getExperimentsOfCourse(int courseid) {
		// TODO Auto-generated method stub
		Course course = findById(courseid);
		if(course == null||course.getIsActive() == false)
			return null;
		Set<CourseExperiment> ces = course.getCourseExperiment();
		Map<Integer,Experiment> list = new HashMap<Integer,Experiment>();
		Iterator<CourseExperiment> i = ces.iterator();
		while ( i.hasNext()) {
			
			CourseExperiment ce = i.next();
			list.put(ce.getSequence(), ce.getExperiment());
			
		}
		return list;
	}

	@Override
	public boolean deleteExperiment(int courseid, Experiment e) {
		// TODO Auto-generated method stub
		Course course = null;
		course = (Course)this.getCurrentSession().get(Course.class, courseid);
		if(course==null)
			course = findById(courseid);
		if(course == null||course.getIsActive() == false)
			return false;
		
		Set<CourseExperiment> ces = course.getCourseExperiment();
		Iterator<CourseExperiment> it = ces.iterator();
		CourseExperiment target = null;
		while(it.hasNext())
		{
			CourseExperiment ce = it.next();
			if(ce.getExperiment().getId() == e.getId())
			{
				target = ce;
			}
		}
		
		ces.remove(target);
		target.setCourse(null);
		target.getExperiment().getCourseExperiment().remove(target);
		target.setExperiment(null);
		this.getCurrentSession().delete(target);
		//this.saveOrUpdate(course);
		
		return true;
	}
}
