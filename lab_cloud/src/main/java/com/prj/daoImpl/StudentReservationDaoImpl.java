package com.prj.daoImpl;

import java.util.List;
import java.util.Set;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.StudentReservationDao;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.StudentReservation;
import com.prj.util.DataWrapper;

@Service("StudentReservationDaoImpl")
public class StudentReservationDaoImpl extends AbstractHibernateDao<StudentReservation,Integer> implements StudentReservationDao{

	protected StudentReservationDaoImpl() {
		super(StudentReservation.class);
	}

	@Override
	public Set<StudentReservation> getStudentReservationByLabPlan(LabPlan lp) {
		// TODO Auto-generated method stub
		return lp.getSlot1StudentReservations();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StudentReservation> getAllStudentReservation() {
		// TODO Auto-generated method stub
		List<StudentReservation> ret = getCurrentSession().createCriteria(StudentReservation.class)
				.addOrder(Order.asc("id"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		return ret;
	}

	@Override
	public Integer addStudentReservation(
			StudentReservation sr) {
		return super.add(sr);
	}

	@Override
	public DataWrapper<List<StudentReservation>> getStudentReservationPage(Integer stuId,
			ApprovalStatus as, Integer pageSize, Integer pageNumber) {
		String sql;
		if (as == ApprovalStatus.ALL)
			sql = String.format("where studentId=%d orderBy date decs", 
					stuId, as);
		else
			sql = String.format("where studentId=%d, approvalStatus=%d orderBy date decs", 
				stuId, as);
		return findBySQLByPage(sql, pageNumber, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StudentReservation> getStudentReservationBySLE(Integer studentId,
			Integer labId, Integer expId) {
		return createCriteria()
				.add(Restrictions.and(
						Restrictions.eq("student.id", studentId),
						Restrictions.eq("lab.id", labId),
						Restrictions.eq("experiment.id", expId),
						Restrictions.ne("approvalStatus", ApprovalStatus.REJECTED)))
				.list();
	}

	@Override
	public DataWrapper<List<StudentReservation>> getClassReservationByPageDur(
			ApprovalStatus approvalStatus, Integer pageSize, Integer pageNumber,
			String startDate, String endDate) {
		String sql;
		if (approvalStatus == ApprovalStatus.ALL)
			sql = String.format("where date>='%s' and date<='%s' order by modify_time desc", 
					startDate, endDate);
		else
			sql = String.format("where approvalStatus=%d date>='%s' and date<='%s' order by modify_time desc", 
				approvalStatus, startDate, endDate);
		return findBySQLByPage(sql, pageNumber, pageSize);
	}
}
