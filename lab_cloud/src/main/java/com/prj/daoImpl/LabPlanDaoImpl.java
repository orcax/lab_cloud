package com.prj.daoImpl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.LabPlanDao;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.Weekday;
import com.prj.entity.Reservation.SlotNo;
import com.prj.entity.StudentReservation;

@Service("LabPlanDaoImpl")
public class LabPlanDaoImpl extends AbstractHibernateDao<LabPlan,Integer> implements LabPlanDao{
	
	protected LabPlanDaoImpl() {
		super(LabPlan.class);
	}

	@Override
	public LabPlan updateLabPlan(LabPlan lp) {
		return saveOrUpdate(lp);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LabPlan> getLabPlanByWeekByLab(int weekid, int labid, Date fromdate) {
		//Integer minid = (Integer)getCurrentSession().createSQLQuery("select min(id) from lab_plan").uniqueResult();
		//LabPlan lp = findById(minid);
		LabPlan lp = (LabPlan)super.createCriteria()
				.add(Restrictions.eq("date", fromdate))
				.setMaxResults(1)
				.uniqueResult();
		int presize = lp.getWeekday().ordinal();
		int today = 0;
		today = 7*weekid - presize;
		
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(df.parse(lp.getDate().toString()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cal.add(Calendar.DATE, today);
		
		
		Date todate = new Date(cal.getTime().getTime());
		
		System.out.println(todate);
		
		//Date fromdate = lp.getDate();
		
		
		if(weekid!=1){
			int fromday = 0;
			fromday = 7*(weekid-1) - presize+1;
			try {
				cal.setTime(df.parse(lp.getDate().toString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cal.add(Calendar.DATE, fromday);
			
			fromdate = new Date(cal.getTime().getTime());
		}
		
		
		List<LabPlan> ret = getCurrentSession().createCriteria(LabPlan.class)
				.add(Restrictions.eq("lab.id",labid))
				.add(Restrictions.between("date", fromdate, todate))
				.addOrder(Order.asc("date"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		return ret;
	}

	@Override
	public LabPlan getLabPlanByStudentReservation(StudentReservation sr) {
		return sr.getLabPlan();
	}

	@Override
	@SuppressWarnings("unchecked")
	public LabPlan getLabPlanByDate(int labid, Date date) {
		List<LabPlan> ret = getCurrentSession().createCriteria(LabPlan.class)
				.add(Restrictions.eq("lab.id", labid))
				.add(Restrictions.eq("date", date))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		if(ret != null && ret.size()!=0)
			return  ret.get(0);
		else
			return null;
	}

	@Override
	public LabPlan findLabPlanById(int id) {
		return findById(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<LabPlan> getLabPlanByWeek(int weekid, Date fromdate) {
		// TODO Auto-generated method stub
		//Date minid = (Date)getCurrentSession().createSQLQuery("select startDate from semester where status = 0").uniqueResult();
		//LabPlan lp = findById(minid);
		LabPlan lp = (LabPlan)super.createCriteria()
				.add(Restrictions.eq("date", fromdate))
				.setMaxResults(1)
				.uniqueResult();
		int presize = lp.getWeekday().ordinal();
		int today = 0;
		today = 7*weekid - presize;
		
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(df.parse(lp.getDate().toString()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cal.add(Calendar.DATE, today);
		
		
		Date todate = new Date(cal.getTime().getTime());
		
		//System.out.println(todate);
		
		//Date fromdate = lp.getDate();
		
		
		if(weekid!=1){
			int fromday = 0;
			fromday = 7*(weekid-1) - presize+1;
			try {
				cal.setTime(df.parse(lp.getDate().toString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cal.add(Calendar.DATE, fromday);
			
			fromdate = new Date(cal.getTime().getTime());
		}
		
		
		List<LabPlan> ret = getCurrentSession().createCriteria(LabPlan.class)
				.add(Restrictions.between("date", fromdate, todate))
				.addOrder(Order.asc("date"))
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
				.list();
		return ret;
	}

	@Override
	public List<String> getDateByWeek(int weekid, Date fromdate) {
		// TODO Auto-generated method stub
		LabPlan lp = (LabPlan)super.createCriteria()
				.add(Restrictions.eq("date", fromdate))
				.setMaxResults(1)
				.uniqueResult();
		int presize = lp.getWeekday().ordinal();
		int today = 0;
		today = 7*weekid - presize;
		
		List<String> ret = new ArrayList<String>();
				
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		
		
		for(int i = 0 ; i < 7 ; i++){
			try {
				cal.setTime(df.parse(lp.getDate().toString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cal.add(Calendar.DATE, today-i);
			
			
			Date todate = new Date(cal.getTime().getTime());
			System.out.println(todate);
			
			ret.add(df.format(cal.getTime()));
			
		}
		
		//System.out.println(todate);
		
		//Date fromdate = lp.getDate();
		
		
//		if(weekid!=1){
//			int fromday = 0;
//			fromday = 7*(weekid-1) - presize+1;
//			try {
//				cal.setTime(df.parse(lp.getDate().toString()));
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			cal.add(Calendar.DATE, fromday);
//			
//			fromdate = new Date(cal.getTime().getTime());
//		}
		
		
		return ret;
	}

	@Override
	public int getSlotOccupiedNum(int id, SlotNo slot) {
		return ((BigInteger) super.getCurrentSession()
				.createSQLQuery("call get_slot_occupied_num(:lab_id, :slot)")
				.setInteger("lab_id", id)
				.setInteger("slot", slot.ordinal())
				.uniqueResult()).intValue();
	}

	@Override
	public int closeLabSlot(int id, SlotNo slot) {
		return super.getCurrentSession()
			.createSQLQuery("call close_lab_slot(:lab_id, :slot)")
			.setInteger("lab_id", id)
			.setInteger("slot", slot.ordinal())
			.executeUpdate();
	}
	
	@Override
	public int getSlotOccupiedNum(int id, SlotNo slot, Weekday weekday) {
		return ((BigInteger) super.getCurrentSession()
				.createSQLQuery("call get_slot_occupied_num_by_date(:lab_id, :slot, :day_of_week)")
				.setInteger("lab_id", id)
				.setInteger("slot", slot.ordinal())
				.setInteger("day_of_week", weekday.ordinal()+1)
				.uniqueResult()).intValue();
	}

	@Override
	public int closeLabSlot(int id, SlotNo slot, Weekday weekday) {
		return super.getCurrentSession()
				.createSQLQuery("call close_lab_slot_by_date(:lab_id, :slot, :day_of_week)")
				.setInteger("lab_id", id)
				.setInteger("slot", slot.ordinal())
				.setInteger("day_of_week", weekday.ordinal()+1)
				.executeUpdate();
	}
}
