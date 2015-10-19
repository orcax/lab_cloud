package com.prj.daoImpl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.ClassReservationDao;
import com.prj.entity.ClassReservation;
import com.prj.entity.LabPlan.ApprovalStatus;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.Teacher;
import com.prj.util.DataWrapper;

@Service("ClassReservationDaoImpl")
public class ClassReservationDaoImpl extends AbstractHibernateDao<ClassReservation, Integer> implements ClassReservationDao{
	protected ClassReservationDaoImpl(){
		super(ClassReservation.class);
	}

	@Override
	public Integer addClassReservation(ClassReservation cr) {
		return add(cr);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ClassReservation> getAllClassReservation() {
		// TODO Auto-generated method stub
		List<ClassReservation> result = getCurrentSession().createCriteria(ClassReservation.class)
				.addOrder(Order.asc("id"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		return result;
	}

	@Override
	public DataWrapper<List<ClassReservation>> getClassReservationByTeacherByPageAS(int pagenumber,
			int pagesize, Teacher teacher, ApprovalStatus as) {
		String sql;
		if (as == ApprovalStatus.ALL) {
			sql = "where reserverId="+teacher.getId()+" order by modify_time desc";
		} else {
			sql = "where reserverId="+teacher.getId()+" and approvalStatus="+as.ordinal()+" order by modify_time desc";
		}
		return findBySQLByPage(sql, pagenumber, pagesize);
	}
	public ClassReservation findClassReservationById(Integer id) {
		return findById(id);
	}

	@Override
	public ClassReservation updateClassReservation(ClassReservation cr) {
		return saveOrUpdate(cr);
	}

	@Override
	public DataWrapper<List<ClassReservation>> getClassReservationByPageDur(
			ApprovalStatus approvalStatus, Integer pageSize, Integer pageNumber, String startDate, String endDate) {
		String sql;
		if (approvalStatus == ApprovalStatus.ALL)
			sql = String.format("where `date` between '%s' and '%s' order by modify_time desc", 
					startDate, endDate);
		else
			sql = String.format("where approvalStatus=%d and `date` between '%s' and '%s' order by modify_time desc", 
				approvalStatus.ordinal(), startDate, endDate);
		return findBySQLByPage(sql, pageNumber, pageSize);
	}

	// DONE pending时才可删除
	@Override
	public ClassReservation deleteClassReservationById(int id) {
		ClassReservation cr= (ClassReservation)this.getCurrentSession().get(ClassReservation.class,id);
		if(cr == null)
			cr = findById(id);
		if (cr.getApprovalStatus() != ApprovalStatus.PENDING)
			return null;
		if(cr.getSlot()==SlotNo.ONE){
			cr.getLabPlan().setSlot1ClassReservation(null);
			cr.setLabPlan(null);
		}else if(cr.getSlot()==SlotNo.TWO){
			cr.getLabPlan().setSlot2ClassReservation(null);
			cr.setLabPlan(null);
		}else if(cr.getSlot()==SlotNo.THREE){
			cr.getLabPlan().setSlot3ClassReservation(null);
			cr.setLabPlan(null);
		}else if(cr.getSlot()==SlotNo.FOUR){
			cr.getLabPlan().setSlot4ClassReservation(null);
			cr.setLabPlan(null);
		}else if(cr.getSlot()==SlotNo.FIVE){
			cr.getLabPlan().setSlot5ClassReservation(null);
			cr.setLabPlan(null);
		}else if(cr.getSlot()==SlotNo.SIX){
			cr.getLabPlan().setSlot6ClassReservation(null);
			cr.setLabPlan(null);
		}
		this.getCurrentSession().delete(cr);
		return cr;
	}

	@Override
	public ClassReservation findClassReservationById(int id) {
		// TODO Auto-generated method stub
		return findById(id);
	}

//	@Override
//	public ClassReservation updateClassReservation(ClassReservation cr) {
//		// TODO Auto-generated method stub
//		
//		return saveOrUpdate(cr);
//	}
	public DataWrapper<List<ClassReservation>> getPendingClassReservationByPageDur(
			Integer pageSize, Integer pageNumber, String startDate, String endDate) {
		String sql = String.format("where approvalStatus=0 and date>='%s' and date<='%s' order by modify_time desc", startDate, endDate);
		return findBySQLByPage(sql, pageNumber, pageSize);
	}
	@SuppressWarnings("unchecked")
	@Override
	public ClassReservation getClassReservationsByDateSlot(Date date,
			SlotNo slot) {
		
		List<ClassReservation> crlist = createCriteria()
				.add(Restrictions.eq("date", date))
				.add(Restrictions.eq("slot", slot))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		if(crlist != null&& crlist.size() != 0){
			return crlist.get(0);
		}
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClassReservation> getAllClassReservationByLabTeacher(Integer labTeaId) {
		return getCurrentSession().createSQLQuery("call get_cr_by_labtea(:id)")
				.addEntity(ClassReservation.class)
				.setInteger("id", labTeaId)
				.list();
	}
}
