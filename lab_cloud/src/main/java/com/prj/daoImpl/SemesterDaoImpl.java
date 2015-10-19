package com.prj.daoImpl;

import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.SemesterDao;
import com.prj.entity.Semester;
import com.prj.entity.Semester.Status;

@Service("SemesterDaoImpl")
public class SemesterDaoImpl extends AbstractHibernateDao<Semester,Integer>implements SemesterDao{
	protected SemesterDaoImpl() {
		super(Semester.class);
	}

	@Override
	public Semester findSemesterById(int id) {
		return findById(id);
	}

	@Override
	public Integer addSemester(Semester s) {
		return add(s);
	}

	@Override
	public Semester updateSemester(Semester s) {
		return saveOrUpdate(s);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Semester getLastSemester() {
		List<Semester> result = getCurrentSession().createCriteria(Semester.class)
								.addOrder(Order.asc("id"))
								.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
								.list();
		if(result!=null&&result.size()!=0)
			return result.get(result.size()-1);
		else
			return null;
	}
	
	@Override
	public Semester getCurSemester() {
		return (Semester) createCriteria()
				.add(Restrictions.eq("status", Status.CURRENT))
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Semester> getAllSemesters() {
		return createCriteria()
				.addOrder(Order.desc("startDate"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
	}
}
