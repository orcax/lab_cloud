package com.prj.daoImpl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prj.dao.AbstractHibernateDao;
import com.prj.dao.SlotReservationDao;
import com.prj.entity.SlotReservation;
import com.prj.util.DataWrapper;

@Service("SlotReservationDaoImpl")
public class SlotReservationDaoImpl extends AbstractHibernateDao<SlotReservation,Integer> implements SlotReservationDao{

	protected SlotReservationDaoImpl() {
		super(SlotReservation.class);
	}

	@Override
	public Integer addSlotReservation(SlotReservation slr) {
		return super.add(slr);
	}

	@Override
	public SlotReservation findSlotReservationById(Integer slotResId) {
		return super.findById(slotResId);
	}

	@Override
	public boolean checkStudentByExperiment(Integer studentId, Integer expId) {
		return ((BigInteger)super.getCurrentSession().createSQLQuery("call check_stu_by_exp(:stu_id, :exp_id)")
					.setInteger("stu_id", studentId)
					.setInteger("exp_id", expId)
					.uniqueResult()).compareTo(BigInteger.valueOf(0)) > 0;
	}

	@Override
	public SlotReservation updateSlotReservation(SlotReservation slr) {
		return super.saveOrUpdate(slr);
	}

	@SuppressWarnings("unchecked")
	@Override
	public DataWrapper<List<SlotReservation>> getAvailableSlotReservationPage(
			Integer stuId, Integer pageSize, Integer pageNumber) {
		return super.convertToPageWrapper(
					super.createSQLQuery("call get_ava_slot_resvation_by_stu(:stu_id)")
						.setInteger("stu_id", stuId)
						.list(),
					pageNumber, pageSize);
	}

	@Override
	public DataWrapper<List<SlotReservation>> getSlotReservationPage(
			Integer pageSize, Integer pageNumber) {
		return super.findBySQLByPage("order by date desc", pageNumber, pageSize);
	}

	@Override
	public DataWrapper<List<SlotReservation>> getApprovedSlotReservationPage(
			Integer stuId, Integer pageSize, Integer pageNumber) {
		return super.findBySQLByPage("slr "
				+ " inner join slot_reservation_student slrs on slrs.slotReservationId=slr.id "
				+ " where slrs.studentId="+stuId
				+ " order by `date` desc" , pageNumber, pageSize);
	}
	
	@Override
	public DataWrapper<List<SlotReservation>> getSlotReservationByPageDur(
			Integer pageSize, Integer pageNumber, String startDate, String endDate) {
		String sql = String.format("where date>='%s' and date<='%s' order by modify_time desc", 
					startDate, endDate);
		return findBySQLByPage(sql, pageNumber, pageSize);
	}
}
