package com.prj.service;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.SimpleExpression;

import com.prj.entity.Experiment;
import com.prj.entity.Lab;
import com.prj.entity.LabPlan;
import com.prj.entity.LabPlan.OpenStatus;
import com.prj.entity.LabPlan.SlotType;
import com.prj.entity.LabPlan.Weekday;
import com.prj.entity.Reservation.SlotNo;
import com.prj.util.DataWrapper;
import com.prj.util.Page;

public interface LabService {
	DataWrapper<Lab> addLab(Integer id, Lab lab);

	public DataWrapper<Lab> deleteLabById(Integer id, Integer updaterId);

	DataWrapper<List<Lab>> getAllLab();

	DataWrapper<List<Lab>> getAllActiveLab();
	
	DataWrapper<Lab> getLabById(int id);

	DataWrapper<Lab> updateLab(Integer id, Lab entity, Integer adminId);

	Page<Lab> getLabByPage(int pagenumber, int pagesize);

	Page<Lab> searchLab(int pagenumber, int pagesize, String name);

	Page<Lab> getByPageWithConditions(int pagenumber, int pagesize,
			List<SimpleExpression> list);
	
	DataWrapper<Lab> addExperiment(int labid, int experimentid);
	
	DataWrapper<Lab> deleteExperiment(int labid, int experimentid);
	
	DataWrapper<List<Experiment>> getExperimentList(int labid);

	DataWrapper<LabPlan> setLabSlotStatus(int labId, Date date, SlotNo slot,
			OpenStatus openStatus);
	
	DataWrapper<LabPlan> setLabSlotType(int labId, Date date, SlotNo slot, SlotType slotType, Integer stuReservationMax);

	DataWrapper<Void> closeLabSlot(int id, SlotNo slot);

	DataWrapper<Void> closeLabSlot(int id, SlotNo slot, Weekday weekday);
}
