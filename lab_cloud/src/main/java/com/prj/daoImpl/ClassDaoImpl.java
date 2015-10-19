package com.prj.daoImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.ClassDao;
import com.prj.entity.Class;
import com.prj.entity.ExperimentStatus;
import com.prj.entity.Semester;
import com.prj.util.DataWrapper;

@Service("ClassDaoImpl")
public class ClassDaoImpl extends AbstractHibernateDao<Class,Integer> implements ClassDao{
	
	protected ClassDaoImpl(){
		super(Class.class);
	}
	
	public Integer addClass(Class c){
		return add(c);
	}

	@Override
	public Class findClassById(int id) {
		return findById(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DataWrapper<List<Class>> getAllClass() {
		List<Class> result = getCurrentSession().createCriteria(Class.class)
				.addOrder(Order.asc("id"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		DataWrapper<List<Class>> ret = new DataWrapper<List<Class>>();
		ret.setData(result);
		return ret;
	}

	@Override
	public Class deleteClassById(Integer id) {
		Class c = findById(id);
		if(c == null)
			return null;
		c.getCourse().getClasses().remove(c);
		c.setCourse(null);
		c.getTeacher().getClasses().remove(c);
		c.setTeacher(null);
		c.getSemester().getClasses().remove(c);
		c.setSemester(null);
		this.getCurrentSession().delete(c);
		return c;
	}

	@Override
	public Class updateClass(Class c) {
		return saveOrUpdate(c);
	}

	@Override
	public DataWrapper<List<Class>> getCurSemesterClassByPage(
			Semester curSemester, Integer pageSize, Integer pageNumber) {
//		Criteria criteria = createCriteria()
//				.addOrder(Order.asc("classNumber"))
//				.add(Restrictions.eq("semester.id", curSemester.getId()));
//		PageResult<Class> pr = 
		return findBySQLByPage("where semesterId="+curSemester.getId()+" order by classNumber asc", pageNumber, pageSize);
//		DataWrapper<List<Class>> ret = new DataWrapper<List<Class>>();
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
	}

	@Override
	public Class getClassByNumber(String classNumber) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("classNumber", classNumber));
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Class) ret.get(0);
		}
		return null;
	}

	@Override
	public Class getClassByComponentIds(int semesterId, int courseId,
			int teacherId) {
		Criteria criteria = createCriteria();
		criteria.add(
				Restrictions.and(
						Restrictions.eq("semester.id", semesterId),
						Restrictions.eq("course.id", courseId),
						Restrictions.eq("teacher.id", teacherId)
				)
		);
		List<?> ret = criteria.list();
		if (ret != null && ret.size() > 0) {
			return (Class) ret.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class> getClassesInLabByTeacher(Integer teacherId, Integer labId) {
		return super.createSQLQuery("call get_classes_in_lab_by_teacher(:teacher_id, :lab_id)")
				.setInteger("teacher_id", teacherId)
				.setInteger("lab_id", labId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExperimentStatus> experimentStatusList(Integer classId) {
		return getCurrentSession().createSQLQuery("call get_exp_status_by_class(:class_id)")
				.addEntity(ExperimentStatus.class)
				.setInteger("class_id", classId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class> getClassList(Integer labId, Integer teacherId) {
		return super.createSQLQuery("call get_class_list_by_lab_tea(:lab_id, :teacher_id)")
				.setInteger("lab_id", labId)
				.setInteger("teacher_id", teacherId)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class> getClassesBySE(Integer studentId, Integer expId) {
		return super.createSQLQuery("call get_class_by_stu_exp(:stu_id, :exp_id)")
				.setInteger("stu_id", studentId)
				.setInteger("exp_id", expId)
				.list();
	}
}
